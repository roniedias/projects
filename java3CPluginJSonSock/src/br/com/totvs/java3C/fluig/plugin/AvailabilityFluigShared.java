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
	 * Algumas disponibilidades serão analisadas a partir da chamada à API disponibilizada pelo FLUIG.
	 * Outras, devido à inexistência de um método de monitoramento por parte do produto,
	 * serão extraídas diretamente a partir de um método que simula a execução de um comando "telnet"
	 * 
	 * Todos os serviços a serem monitorados, encontram-se na aba Fluig > Tipos Serviços - Fluig. Para que
	 * o monitoramento de um determinado serviço ocorra, necessariamente a opção "Monitoracao" deve estar 
	 * com valor igual a "Sim". Caso contrário, o monitoramento daquele serviço em específico não será executado.
	 * 
	 * 
	 * Serviços analisados via API:
	 * 
	 *  goodDataAvailability        : Método que corresponde ao serviço cadastrado no 3C com a descrição "ANALYTICS FLUIG"
	 *  memcachedAvailability       : Método que corresponde ao serviço cadastrado no 3C com a descrição "CACHE FLUIG"
	 *  openOfficeServerAvailability: Método que corresponde ao serviço cadastrado no 3C com a descrição "OFFICE FLUIG"
	 *  solrServerAvailability      : Método que corresponde ao serviço cadastrado no 3C com a descrição "INDEX FLUIG"
	 *  identityAvailability        : Método que corresponde ao serviço cadastrado no 3C com a descrição "IDENTITY"
	 *   
	 *  Obs: Os métodos "databaseAvailability" e "licenseServerAvailability", disponibilizados pela API, serão 
	 *  SEMPRE executados, não havendo necessidade de analisar sequer se os mesmos foram cadastrados no 3C
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
		
		// Validação se o ambiente encontra-se com status ATIVO, EM MANUTENÇÃO, CADASTRO, SUSPENSO ou DESATIVADO. 
		new ValidacaoStatusAmb(ZBB_STATUS, ZCA_CODAMB, ZCA_ITEM, ZCA_PARAM, ZCA_TIPAMB);

		
		if(!itensAmbienteFluig.getModalidade().equals("C")) { // Se o Fluig não for "Compartilhado"
			System.out.println("Modalidade configurada nao corresponde ao modelo \"Compartilhado\". Verifique o cadastro do Item de Ambiente no 3C." );
			System.exit(3);
		}
		
		FluigAPIResults fluigAPIResults = new FluigAPI(itensAmbienteFluig).getFluigAPIResults();
			
		goodDataAvailability 		 = fluigAPIResults.getGoodDataAvailability();
		memcachedAvailability 		 = fluigAPIResults.getMemcachedAvailability();
		openOfficeServerAvailability = fluigAPIResults.getOpenOfficeServerAvailability();
		solrServerAvailability 		 = fluigAPIResults.getSolrServerAvailability();
		identityAvailability 		 = fluigAPIResults.getIdentityAvailability();
		
		databaseAvailability 		 = fluigAPIResults.getDatabaseAvailability(); // Monitoramento obrigatório
		licenseServerAvailability 	 = fluigAPIResults.getLicenseServerAvailability(); // Monitoramento obrigatório

			
		telnetResults = new ArrayList<TelnetResult>();
		
		// Executando rotina de checagem (telnet) para os serviços devidos, caso o monitoramento esteja habilitado  
		for(TiposServicosFluig t : tiposServicosFluig) {
						
			   //JBOSS FLUIG (TELNET)           //CHAT FLUIG (TELNET)            //REALTIME FLUIG (TELNET)
			if(t.getCodTipSrv().equals("68") || t.getCodTipSrv().equals("55") || t.getCodTipSrv().equals("70")) {
				
				// Executando o telnet
				Telnet telnet = new Telnet(t.getIp(), Integer.valueOf(t.getPorta()));
				
				// Atribuindo o resultado a um array com duas posições (status e mensagem)
				String[] result = telnet.getResult();
				
				// Criando um objeto temporário para guardar o resultado
				TelnetResult telnetResult = new TelnetResult();
				telnetResult.setStatus(result[0]);
				telnetResult.setMessage(t.getNomeTipSrv() + ": " + result[1]);
				
				// Armazenando o objeto contendo o resultado em um ArrayList de resultados
				telnetResults.add(telnetResult);
			}
			else { // Condição para gravar os dados dos outros monitoramentos, efetuados pela API
				
				
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
		

		// Monitoramentos obrigatórios	
		if(databaseAvailability.equals("FAILURE")) {
			ZCA_RESULT = 0;
		}						
		if(licenseServerAvailability.equals("FAILURE")) {
			ZCA_RESULT = 0;
		}						
		ZCA_MEMO += "DATABASE FLUIG: " + databaseAvailability + " (VIA API); ";
		ZCA_MEMO += "LICENSE SERVER FLUIG: " + licenseServerAvailability + " (VIA API); ";
		
		ParserWrite parserWrite = new ParserWrite("GENERIC_WRITE", ZCA_CODAMB, ZCA_TIPAMB, ZCA_ITEM, ZCA_PARAM, String.valueOf(ZCA_RESULT), ZCA_MEMO);

		if(parserWrite.getStatus().equals("0")) { // Se correu tudo bem com a gravação
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