package br.com.totvs.java3C.plugin.bdconn;


import java.util.ArrayList;

import br.com.totvs.java3C.JSonParser.monit.ActiveConnParserMon;
//import br.com.totvs.java3C.JSonParser.read.ParserRead;
import br.com.totvs.java3C.JSonParser.write.ParserWrite;

import br.com.totvs.java3C.bean.AmbienteFull;
import br.com.totvs.java3C.bean.Servico;
//import br.com.totvs.java3C.bean.ServicosItemTipoAmb;
import br.com.totvs.java3C.util.ValidacaoStatusAmb;



/**
* @author Ronie Dias Pinto
*/


public class ActiveConnections {
	
	

	
	private String ZCA_PARAM;
	private String ZCA_CODAMB;
	private String ZCA_TIPAMB;
	private String ZCA_ITEM;
	private float ZCA_RESULT;
	private String ZCA_MEMO;
	
	private ArrayList<String> auxListZCA_MEMO = new ArrayList<String>();

	private String ip;
	private String porta;
	private String environment; 

	
	private String codTipoServicoLicense = "13";
	private String codTipoServicoTopConnect = "16";
	
	private String codTipoServicoErpMasterInternet = "10";
	private String codTipoServicoErpMasterMpls = "23";
	private String codTipoServicoErpMasterIntranet = "24";
	
	
	private ActiveConnParserMon aCJSonParser;
	private String [] aCJSonParserReturn;
	

	private String[] codTodosTiposServicos;

	private ArrayList<Servico> servicosItemTipoAmb;
	private ArrayList<Servico> servicos = new ArrayList<Servico>();

	
	
	 
	public ActiveConnections(String codAmbiente, String codTipoAmbiente, String codMonitoramento, String codProduto) {
			
		this.ZCA_PARAM = codMonitoramento; 
		this.ZCA_CODAMB = codAmbiente;
		this.ZCA_TIPAMB = codTipoAmbiente;
				
		AmbienteFull aFull = new AmbienteFull(codAmbiente, codTipoAmbiente, codProduto);
		
		codTodosTiposServicos = new String[aFull.getTiposServico().size()];
		
		for(int a = 0; a < aFull.getTiposServico().size(); a++) {
			codTodosTiposServicos[a] = aFull.getTiposServico().get(a).getCodigo();
		}

				
		servicosItemTipoAmb = aFull.getServicos();		
		
		// Preenchendo o ArrayList servicos, excluindo os Tipos TOP e LICENSE e validando a regra abaixo: Caso haja dois MASTER´s, 
		//um INTERNET e outro MPLS, ignorar aquele que não tiver os Slaves/balances cadastrados abaixo dele 
		for(int s = 0; s < servicosItemTipoAmb.size(); s++) {
			if(!(servicosItemTipoAmb.get(s).getCodTipoServico().equals(codTipoServicoLicense) || servicosItemTipoAmb.get(s).getCodTipoServico().equals(codTipoServicoTopConnect))) {
				
				if(servicosItemTipoAmb.get(s).getCodTipoServico().equals(codTipoServicoErpMasterInternet) || servicosItemTipoAmb.get(s).getCodTipoServico().equals(codTipoServicoErpMasterMpls) || servicosItemTipoAmb.get(s).getCodTipoServico().equals(codTipoServicoErpMasterIntranet)) {
					if(servicosItemTipoAmb.get(s).getBalance().equals("S")) // Apenas os tipos de serviços balance (excluindo master)
						servicos.add(servicosItemTipoAmb.get(s));					
				}
				else {
					servicos.add(servicosItemTipoAmb.get(s)); // codTipoServicoErpInternet = "29", 
				}										     // codTipoServicoErpMpls = "30" ou qualquer outro  
			}												// cujo campo "balance" estiver igual a "N" SERÁ SIM, monitorado
		}

		
		for(int s = 0; s < servicos.size(); s++) {
				
				
		   	this.ip = servicos.get(s).getIpHost().trim();
		   	this.porta = servicos.get(s).getPorta().trim();
			this.environment = servicos.get(s).getEnvironment().trim();	


				
			if(servicos.size() == 1) { // Se houver apenas um serviço cadastrado					
		
				aCJSonParser = new ActiveConnParserMon(ip, porta, environment);
				this.aCJSonParserReturn = aCJSonParser.getReturnArray();         // aCJSonParserReturn[0] = CONNECTIONS 
																			     // aCJSonParserReturn[1] = MESSAGE
																			     // aCJSonParserReturn[2] = ELAPTIME
							
													   
				if(aCJSonParserReturn[0].equals("-1")) {
					auxListZCA_MEMO.add("Environment: " + environment.replaceAll("\\s", "") + ", porta: " + porta.replaceAll("\\s", "") + " = 0; ");					
				}
				else {
					auxListZCA_MEMO.add("Environment: " + environment.replaceAll("\\s", "") + ", porta: " + porta.replaceAll("\\s", "") + " = " + aCJSonParserReturn[0] + "; ");
				}
								 
									 		
				if(Float.valueOf(aCJSonParserReturn[0]) != -1) 
					ZCA_RESULT += Float.valueOf(aCJSonParserReturn[0]); 
				
			}
			else {
									
				aCJSonParser = new ActiveConnParserMon(ip, porta, environment);
				this.aCJSonParserReturn = aCJSonParser.getReturnArray();     // aCJSonParserReturn[0] = CONNECTIONS 
																					 // aCJSonParserReturn[1] = MESSAGE
																					 // aCJSonParserReturn[2] = ELAPTIME
						
												
				if(aCJSonParserReturn[0].equals("-1")) {				
					auxListZCA_MEMO.add("Environment: " + environment.replaceAll("\\s", "") + ", porta: " + porta.replaceAll("\\s", "") + " = 0");
				}
				else {
					auxListZCA_MEMO.add("Environment: " + environment.replaceAll("\\s", "") + ", porta: " + porta.replaceAll("\\s", "") + " = " + aCJSonParserReturn[0] + "; ");
				}
									 			 		
					if(Float.valueOf(aCJSonParserReturn[0]) != -1) 
						ZCA_RESULT += Float.valueOf(aCJSonParserReturn[0]); 																																					
						   						
					
						
			}
		}


			
		this.ZCA_ITEM = aFull.getCodItemAmbiente(codProduto);
		
		
		// Validação se o ambiente encontra-se com status ATIVO, EM MANUTENÇÃO, CADASTRO, SUSPENSO ou DESATIVADO. 
		// Ocorre neste ponto pela necessidade de passar-se para ValidacaoStatusAmb também o valor de ZCA_ITEM
		new ValidacaoStatusAmb(aFull.getStatusItemAmbiente(codProduto), ZCA_CODAMB, ZCA_ITEM, ZCA_PARAM, ZCA_TIPAMB);	
		
		
	  	StringBuilder sb = new StringBuilder();    
				 
	   	for (String s1 : auxListZCA_MEMO)
	   	{
	   	    sb.append(s1);
	   	}

		   	
	   	ZCA_MEMO = sb.toString();
	   	
	   	
	   	
	   		   		   	
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
	
}
