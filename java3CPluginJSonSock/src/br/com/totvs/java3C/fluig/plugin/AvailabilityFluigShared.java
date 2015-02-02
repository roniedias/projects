package br.com.totvs.java3C.fluig.plugin;

import java.util.ArrayList;

import br.com.totvs.java3C.JSonParser.write.ParserWrite;
import br.com.totvs.java3C.dao.Dao;
import br.com.totvs.java3C.fluig.bean.FluigAPIResults;
import br.com.totvs.java3C.fluig.bean.ItensAmbienteFluig;
import br.com.totvs.java3C.fluig.bean.TelnetResult;
import br.com.totvs.java3C.fluig.bean.TiposServicosFluig;
import br.com.totvs.java3C.fluig.util.FluigAPI;
import br.com.totvs.java3C.util.Telnet;
import br.com.totvs.java3C.util.ValidacaoStatusAmb;

public class AvailabilityFluigShared {
	
	private String ZCA_PARAM; 
	private String ZCA_CODAMB;
	private String ZCA_TIPAMB;
	private float ZCA_RESULT = 100;
	private String ZCA_MEMO = "";
	private String ZCA_ITEM;
	private String ZBB_STATUS;
	
	private ItensAmbienteFluig itensAmbienteFluig;
	private ArrayList<TiposServicosFluig> tiposServicosFluig;
	
	private String goodDataAvailability;
	private String memcachedAvailability;
	private String openOfficeServerAvailability;
	private String solrServerAvailability;
	private String identityAvailability;
	private String databaseAvailability;
	private String licenseServerAvailability;
	
	private ArrayList<TelnetResult> telnetResults;
	
	
	
	/*
	 * Algumas disponibilidades ser�o analisadas a partir da chamada � API disponibilizada pelo FLUIG.
	 * Outras, devido � inexist�ncia de um m�todo de monitoramento por parte do produto,
	 * ser�o extra�das diretamente a partir de um m�todo que simula a execu��o de um comando "telnet"
	 * 
	 * Todos os servi�os a serem monitorados, encontram-se na aba Fluig > Tipos Servi�os - Fluig. Para que
	 * o monitoramento de um determinado servi�o ocorra, necessariamente a op��o "Monitoracao" deve estar 
	 * com valor igual a "Sim". Caso contr�rio, o monitoramento daquele servi�o em espec�fico n�o ser� executado.
	 * 
	 * 
	 * Servi�os analisados via API:
	 * 
	 *  goodDataAvailability        : M�todo que corresponde ao servi�o cadastrado no 3C com a descri��o "ANALYTICS FLUIG"
	 *  memcachedAvailability       : M�todo que corresponde ao servi�o cadastrado no 3C com a descri��o "CACHE FLUIG"
	 *  openOfficeServerAvailability: M�todo que corresponde ao servi�o cadastrado no 3C com a descri��o "OFFICE FLUIG"
	 *  solrServerAvailability      : M�todo que corresponde ao servi�o cadastrado no 3C com a descri��o "INDEX FLUIG"
	 *  identityAvailability        : M�todo que corresponde ao servi�o cadastrado no 3C com a descri��o "IDENTITY"
	 *   
	 *  Obs: Os m�todos "databaseAvailability" e "licenseServerAvailability", disponibilizados pela API, ser�o 
	 *  SEMPRE executados, n�o havendo necessidade de analisar sequer se os mesmos foram cadastrados no 3C
	 *  
	 */
	
	
	public AvailabilityFluigShared(String codAmbiente, String codTipoAmbiente, String codMonitoramento, String codProduto) {
		
		
		this.ZCA_PARAM = codMonitoramento; 
		this.ZCA_CODAMB = codAmbiente;
		this.ZCA_TIPAMB = codTipoAmbiente;
		
		tiposServicosFluig = new ArrayList<TiposServicosFluig>();
				
		Dao dao = new Dao();
		itensAmbienteFluig = dao.getItensAmbienteFluig(codAmbiente, codTipoAmbiente, codProduto);
		tiposServicosFluig = dao.getTiposServicosFluig(codAmbiente, codTipoAmbiente, codProduto);
		dao.closeConnection();
		
		this.ZBB_STATUS = itensAmbienteFluig.getStatus();	
		this.ZCA_ITEM = itensAmbienteFluig.getCodItem();
		
		// Valida��o se o ambiente encontra-se com status ATIVO, EM MANUTEN��O, CADASTRO, SUSPENSO ou DESATIVADO. 
		new ValidacaoStatusAmb(ZBB_STATUS, ZCA_CODAMB, ZCA_ITEM, ZCA_PARAM, ZCA_TIPAMB);

		
		if(!itensAmbienteFluig.getModalidade().equals("C")) { // Se o Fluig n�o for "Compartilhado"
			System.out.println("Modalidade configurada nao corresponde ao modelo \"Compartilhado\". Verifique o cadastro do Item de Ambiente no 3C." );
			System.exit(3);
		}
		
		FluigAPIResults fluigAPIResults = new FluigAPI(itensAmbienteFluig).getFluigAPIResults();
			
		goodDataAvailability 		 = fluigAPIResults.getGoodDataAvailability();
		memcachedAvailability 		 = fluigAPIResults.getMemcachedAvailability();
		openOfficeServerAvailability = fluigAPIResults.getOpenOfficeServerAvailability();
		solrServerAvailability 		 = fluigAPIResults.getSolrServerAvailability();
		identityAvailability 		 = fluigAPIResults.getIdentityAvailability();
		
		databaseAvailability 		 = fluigAPIResults.getDatabaseAvailability(); // Monitoramento obrigat�rio
		licenseServerAvailability 	 = fluigAPIResults.getLicenseServerAvailability(); // Monitoramento obrigat�rio

			
		telnetResults = new ArrayList<TelnetResult>();
		
		// Executando rotina de checagem (telnet) para os servi�os devidos, caso o monitoramento esteja habilitado  
		for(TiposServicosFluig t : tiposServicosFluig) {
						
			   //JBOSS FLUIG (TELNET)           //CHAT FLUIG (TELNET)            //REALTIME FLUIG (TELNET)
			if(t.getCodTipSrv().equals("68") || t.getCodTipSrv().equals("55") || t.getCodTipSrv().equals("70")) {
				
				// Executando o telnet
				Telnet telnet = new Telnet(t.getIp(), Integer.valueOf(t.getPorta()));
				
				// Atribuindo o resultado a um array com duas posi��es (status e mensagem)
				String[] result = telnet.getResult();
				
				// Criando um objeto tempor�rio para guardar o resultado
				TelnetResult telnetResult = new TelnetResult();
				telnetResult.setStatus(result[0]);
				telnetResult.setMessage(t.getNomeTipSrv() + ": " + result[1]);
				
				// Armazenando o objeto contendo o resultado em um ArrayList de resultados
				telnetResults.add(telnetResult);
			}
			else { // Condi��o para gravar os dados dos outros monitoramentos, efetuados pela API
				
				
				//CACHE FLUIG (API)
				if(t.getCodTipSrv().equals("55")) {
					if(!memcachedAvailability.equals("OK")) {
						ZCA_RESULT = 0;
					}						
					ZCA_MEMO += "CACHE FLUIG: " + memcachedAvailability + " (VIA API); ";
				}
				
				//OFFICE FLUIG (API)
				if(t.getCodTipSrv().equals("56")) {
					if(!openOfficeServerAvailability.equals("OK")) {
						ZCA_RESULT = 0;
					}						
					ZCA_MEMO += "OFFICE FLUIG: " + openOfficeServerAvailability + " (VIA API); ";
				}
				
				//INDEX FLUIG (API)
				if(t.getCodTipSrv().equals("69")) {
					if(!solrServerAvailability.equals("OK")) {
						ZCA_RESULT = 0;
					}						
					ZCA_MEMO += "INDEX FLUIG: " + solrServerAvailability + " (VIA API); ";										
				}
				
				//ANALYTICS FLUIG (API)
				if(t.getCodTipSrv().equals("57")) {
					if(!goodDataAvailability.equals("OK")) {
						ZCA_RESULT = 0;
					}						
					ZCA_MEMO += "ANALYTICS FLUIG: " + goodDataAvailability + " (VIA API); ";															
				}
				
				//IDENTITY (API)
				if(t.getCodTipSrv().equals("58")) {
					if(!identityAvailability.equals("OK")) {
						ZCA_RESULT = 0;
					}						
					ZCA_MEMO += "IDENTITY: " + identityAvailability + " (VIA API); ";																				
				}	
				
			} //end else
						
		} // end for
		
		
		for(TelnetResult tr : telnetResults) {
			if(tr.getStatus().equals("I")) {
				ZCA_RESULT = 0;
			}			
			ZCA_MEMO += tr.getMessage() + " (VIA TELNET); ";
		}
		

		// Monitoramentos obrigat�rios	
		if(databaseAvailability.equals("FAILURE")) {
			ZCA_RESULT = 0;
		}						
		if(licenseServerAvailability.equals("FAILURE")) {
			ZCA_RESULT = 0;
		}						
		ZCA_MEMO += "DATABASE FLUIG: " + databaseAvailability + " (VIA API); ";
		ZCA_MEMO += "LICENSE SERVER FLUIG: " + licenseServerAvailability + " (VIA API); ";
		
		ParserWrite parserWrite = new ParserWrite("GENERIC_WRITE", ZCA_CODAMB, ZCA_TIPAMB, ZCA_ITEM, ZCA_PARAM, String.valueOf(ZCA_RESULT), ZCA_MEMO);

		if(parserWrite.getStatus().equals("0")) { // Se correu tudo bem com a grava��o
	   		System.out.println("Resultado: " + ZCA_RESULT + ". "+ ZCA_MEMO);
	   		System.exit(Integer.parseInt(parserWrite.getLimiar()));
	   	}
	   	else {
	   		System.out.println(parserWrite.getMessage());
	   		System.exit(Integer.parseInt(parserWrite.getLimiar()));
	   	}
		
	}

	
	
	public static void main(String[] args) {
		new AvailabilityFluigShared("000128", "01", "0", "000071");
	}

}