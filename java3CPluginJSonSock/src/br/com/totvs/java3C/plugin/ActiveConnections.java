package br.com.totvs.java3C.plugin;


import java.util.ArrayList;

import br.com.totvs.java3C.JSonParser.monit.ActiveConnParserMon;
import br.com.totvs.java3C.JSonParser.read.ParserRead;
import br.com.totvs.java3C.JSonParser.write.ParserWrite;

import br.com.totvs.java3C.bean.ServicosItemTipoAmb;
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
	
	private ActiveConnParserMon aCJSonParser;
	private String [] aCJSonParserReturn;
	
	
	private String[] zbhAllTipSrvs;
	private ArrayList<String> zbhTipSrvs = new ArrayList<String>();
	private ArrayList<ServicosItemTipoAmb> servicosItemTipoAmb;

	
	
	
 
	public ActiveConnections(String codAmbiente, String codTipoAmbiente, String codMonitoramento, String codProduto) {
			
		this.ZCA_PARAM = codMonitoramento; 
		this.ZCA_CODAMB = codAmbiente;
		this.ZCA_TIPAMB = codTipoAmbiente;
		
		ParserRead parserRead = new ParserRead("GENERIC_READ", codAmbiente, codTipoAmbiente, codProduto);
		
		
		if(parserRead.getZbbStatus().replaceAll("\\s", "").equals("-")) {
			System.out.println("Retorno JSON (leitura) invalido. Verifique o cadastro dos dados do ambiente no Nagios/3C");
			System.exit(3);
		}
		
		
		
		zbhAllTipSrvs = parserRead.getZbhTipSrvs();
		
		// Adicionando os tipos de serviço a zbhTipSrvs, excluindo os Tipos LICENSE e TOP CONNECT
		for(int z = 0; z < zbhAllTipSrvs.length; z++) {
			if(!(zbhAllTipSrvs[z].equals(codTipoServicoLicense) || zbhAllTipSrvs[z].equals(codTipoServicoTopConnect))) {
				zbhTipSrvs.add(zbhAllTipSrvs[z]); // Adicionando os tipos de serviço a zbhTipSrvs, excluindo os 
			}									  // Tipos TOP e LICENSE
		}
		
		
		
		
		for(int lst = 0; lst < zbhTipSrvs.size(); lst++) {
			servicosItemTipoAmb = new ArrayList<ServicosItemTipoAmb>();
			servicosItemTipoAmb = parserRead.getServicosItemTipoAmb(zbhTipSrvs.get(lst));	
		}
			
			
		
		for(int s = 0; s < servicosItemTipoAmb.size(); s++) {
				
				
		   	this.ip = servicosItemTipoAmb.get(s).getZbcIpHost();
		   	this.porta = servicosItemTipoAmb.get(s).getZbcPorta();
			this.environment = servicosItemTipoAmb.get(s).getZbcEnviro();	

				
			if(servicosItemTipoAmb.size() == 1) { // Se houver apenas um serviço cadastrado					
		
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
				
				System.out.println(servicosItemTipoAmb.get(s).getZbcBalanc());
					
//				if(servicosItemTipoAmb.get(s).getZbcBalanc().equals("S")) { // Pegar somente os servidores que forem balance
																			
								
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
						   						
//				}	
					
						
				}
			}

//		}
			
		
		this.ZCA_ITEM = servicosItemTipoAmb.get(0).getZbcItem(); // ZBC_ITEM, do JSON. Mesmo valor, qualquer que seja a posição do ArrayList escolhida
		
		
		// Validação se o ambiente encontra-se com status ATIVO, EM MANUTENÇÃO, CADASTRO, SUSPENSO ou DESATIVADO. 
		// Ocorre neste ponto pela necessidade de passar-se para ValidacaoStatusAmb também o valor de ZCA_ITEM
		new ValidacaoStatusAmb(parserRead.getZbbStatus().replaceAll("\\s", ""), ZCA_CODAMB, ZCA_ITEM, ZCA_PARAM, ZCA_TIPAMB);
		
		
		
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
