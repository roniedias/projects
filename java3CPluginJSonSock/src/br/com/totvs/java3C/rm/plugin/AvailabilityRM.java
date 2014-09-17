package br.com.totvs.java3C.rm.plugin;

import java.util.ArrayList;

import br.com.totvs.java3C.JSonParser.monit.AvailabilityParserMon;
import br.com.totvs.java3C.JSonParser.read.ParserRead;
import br.com.totvs.java3C.JSonParser.write.ParserWriteAvail;
import br.com.totvs.java3C.bean.ParamServicosMonit;
import br.com.totvs.java3C.bean.ServicosItemTipoAmb;
import br.com.totvs.java3C.rm.JSonParser.RmSrvRetAvbtyParser;
import br.com.totvs.java3C.rm.bean.Server;
import br.com.totvs.java3C.util.StrToUTF8;
import br.com.totvs.java3C.util.TcpConnection;
import br.com.totvs.java3C.util.ValidacaoStatusAmb;

public class AvailabilityRM {
	
	private String ZCA_PARAM; 
	private String ZCA_CODAMB;     
	private String ZCA_TIPAMB;
	
	private String[] zbhTipSrvs;
	
	private ArrayList<ServicosItemTipoAmb> servicos;
	
	private String ZCA_ITEM;
	
	private ArrayList<String> auxSERVICOSListZCC_OBS = new ArrayList<String>();
	private ArrayList<String> auxSERVICOSListZCC_SEQSRV = new ArrayList<String>(); 
	private ArrayList<String> auxSERVICOSListZCC_STATUS = new ArrayList<String>(); 
	private ArrayList<String> auxSERVICOSListZCC_TIPSRV = new ArrayList<String>();
	
	private String serverReturn;
	private ArrayList<Server> servers = new ArrayList<Server>();
	
	
	private ArrayList<String> auxListZCA_MEMO = new ArrayList<String>();
	
	private String empresaFilial;
	
	private AvailabilityParserMon availabilityParser;
	
	private ParamServicosMonit[] SERVICOSARRAY;
	
	private String ZCA_MEMO;


	// Continuar daqui.
	// Verificar o funcionamento do monitoramento de PORTAL RM no ambiente
	// de teste 99401. IP: 10.10.2.56
	
	//Obs: porta do RM HOST situado no IP 10.10.1.95 = 8051. A porta default é a 8050

	public AvailabilityRM(String codAmbiente, String codTipoAmbiente, String codMonitoramento, String codProduto) {
		
		this.ZCA_PARAM = codMonitoramento; 
		this.ZCA_CODAMB = codAmbiente;     
		this.ZCA_TIPAMB = codTipoAmbiente; 

		ParserRead parserRead = new ParserRead("GENERIC_READ", codAmbiente, codTipoAmbiente, codProduto);
		
		
		if(parserRead.getZbbStatus().replaceAll("\\s", "").equals("-")) {
			System.out.println("Retorno JSON (leitura) invalido. Verifique o cadastro dos dados do ambiente no Nagios/3C");
			System.exit(3);
		}
		
		zbhTipSrvs = parserRead.getZbhTipSrvs();

	    servicos = new ArrayList<ServicosItemTipoAmb>();

		for(int lst = 0; lst < zbhTipSrvs.length; lst++) {			
			servicos = parserRead.getServicosItemTipoAmb(zbhTipSrvs[lst]);
		}

		
		this.ZCA_ITEM = servicos.get(0).getZbcItem(); // ZBC_ITEM, do JSON. Mesmo valor, qualquer que seja a posição do ArrayList escolhida
		this.empresaFilial = servicos.get(0).getZbbParNag(); // ZBB_PARNAG, do JSON. Mesmo valor, qualquer que seja a posição do ArrayList escolhida

		// Validação se o ambiente encontra-se com status ATIVO, EM MANUTENÇÃO, CADASTRO, SUSPENSO ou DESATIVADO. 
		new ValidacaoStatusAmb(parserRead.getZbbStatus().replaceAll("\\s", ""), ZCA_CODAMB, ZCA_ITEM, ZCA_PARAM, ZCA_TIPAMB);
		
		
		for(int s = 0; s < servicos.size(); s++) {
			
			auxSERVICOSListZCC_OBS.add("Servico localizado");
			auxSERVICOSListZCC_SEQSRV.add(servicos.get(s).getZbcSeq()); 			 
			auxSERVICOSListZCC_TIPSRV.add(servicos.get(s).getZbcTipSrv()); // Variável que armazena o código do tipo de serviço 
																		   // (RM HOST, PORTAL RM, ETC.)

		
			// Se for do tipo License, será um serviço Protheus. 
			// A rotina de monitoramento será diferente da de monitoramento 
			// de disponibilidade de ambientes RM (será igual à de monitoramento de tipos de serviço license do Protheus)
			if(servicos.get(s).getZbcTipSrv().equals("13")) {
				
				availabilityParser = new AvailabilityParserMon("6", servicos.get(s).getZbcIpHost(), servicos.get(s).getZbcPorta(), servicos.get(s).getZbcEnviro(), empresaFilial);
				
				
				auxListZCA_MEMO.add("IP: " + servicos.get(s).getZbcIpHost() + ", Environment: " + servicos.get(s).getZbcEnviro() + ", porta: " + servicos.get(s).getZbcPorta() + " - " + availabilityParser.getMessageInfo() + "; ");
				
				if(availabilityParser.getStatusInfo().equals("0")) {
					auxSERVICOSListZCC_STATUS.add("A");
				}
				else {
					auxSERVICOSListZCC_STATUS.add("I");
				}
				
				
							
			}
			else { // Monitoramento RM, propriamente dito
			
			
				TcpConnection tcpConnection = new TcpConnection(servicos.get(s).getZbcIpHost(), 4000, "AVAILABILITY#" + servicos.get(s).getZbcIpHost() + "#8051#" + servicos.get(s).getZbcEnviro() + "#");
				
				//serverReturn = new StrToUTF8().convert(tcpConnection.getSrvReturn().replaceAll("Value cannot be null.", ""));
				serverReturn = new StrToUTF8().convert(tcpConnection.getSrvReturn());
				serverReturn = serverReturn.substring(1);
				
				/* serverReturn:
				 * 
				 * 
				 * {
	    				"ELAPTIME": "PT0.1092007S",
	    				"MESSAGE": "Ambiente OK",
	    				"SERVERS": [
	        				{
	            				"IP": "TOTVS-ASP257",
	            				"MESSAGE": "Servidor não OK: ",
	            				"NAME": "TOTVS-ASP257",
	            				"PORT": "8051",
	            				"STATUS": "1"
	        				}
	    				],
	    				"STATUS": "0"
					}
				 * 
				 */
				
				
				RmSrvRetAvbtyParser rsrap = new RmSrvRetAvbtyParser(serverReturn);
				
				servers = rsrap.getServers();
				
				if(rsrap.getStatus().equals("0")) {
					auxSERVICOSListZCC_STATUS.add("A");	
				}
				else {
					auxSERVICOSListZCC_STATUS.add("I");
				}
							
				
				
				if(!servers.get(0).getIp().equals("NO INFO")) { // Se houver valores em "SERVERS" (Array), essas informações devem ser passadas para auxListZCA_MEMO (Quinto parâmetro passado para STRUPARAMGRVMONITORAMENTO) 
					
					String aditionalInfo = ". Servicos Internos:";
					
					for(int i = 0; i < servers.size(); i++) {
						aditionalInfo += " " + servers.get(i).getIp() + " " + servers.get(i).getPort() + ": " + servers.get(i).getMessage(); 
					}
					
					auxListZCA_MEMO.add(servicos.get(s).getZbcEnviro().replaceAll("\\s", "") + ", porta: " + servicos.get(s).getZbcPorta().replaceAll("\\s", "") + " - " + rsrap.getMessage() + aditionalInfo + "; ");
					
				}
				else 
					auxListZCA_MEMO.add(servicos.get(s).getZbcEnviro().replaceAll("\\s", "") + ", porta: " + servicos.get(s).getZbcPorta().replaceAll("\\s", "") + " - " + rsrap.getMessage() + " "); 
			}
				
			
		}
		
		
		SERVICOSARRAY = new ParamServicosMonit[auxSERVICOSListZCC_SEQSRV.size()]; 
		  
		for(int slst = 0; slst < auxSERVICOSListZCC_SEQSRV.size(); slst++) {
			ParamServicosMonit SERVICOS = new ParamServicosMonit(auxSERVICOSListZCC_OBS.get(slst), auxSERVICOSListZCC_SEQSRV.get(slst), auxSERVICOSListZCC_STATUS.get(slst), auxSERVICOSListZCC_TIPSRV.get(slst));
			SERVICOSARRAY[slst] = SERVICOS;
		}

		
		
	   	StringBuilder sb = new StringBuilder();  
 
	   	for (String s : auxListZCA_MEMO) {
	   		sb.append(s);
	   	}
	   	
	   	ZCA_MEMO = sb.toString().replaceAll("ã", "a");
	   		   	
		
	   	ParserWriteAvail parserWriteAvail = new ParserWriteAvail("AVAILABILITY_WRITE", ZCA_CODAMB, ZCA_TIPAMB, ZCA_ITEM, ZCA_PARAM, ZCA_MEMO, SERVICOSARRAY);
	   	
	   	
		
	   	if(parserWriteAvail.getStatus().equals("0")) { // Se correu tudo bem com a gravação
	   		System.out.println("Resultado: " + parserWriteAvail.getZcaResult() + ". "+ ZCA_MEMO);
	   		System.exit(Integer.parseInt(parserWriteAvail.getLimiar()));
	   	}
	   	else {
	   		System.out.println(parserWriteAvail.getMessage());
	   		System.exit(Integer.parseInt(parserWriteAvail.getLimiar()));
	   		
	   	}	
		
		
	}
	
}
























//	System.out.println("ZCA_CODAMB: " + ZCA_CODAMB);
//	System.out.println("ZCA_TIPAMB: " + ZCA_TIPAMB);
//	System.out.println("ZCA_ITEM: " + ZCA_ITEM);
//	System.out.println("ZCA_PARAM: " + ZCA_PARAM);
//	System.out.println("ZCA_MEMO: " + ZCA_MEMO);
//	
//	System.out.println(" \n " );
//	
//	for(int s = 0; s < SERVICOSARRAY.length; s++) {
//		System.out.println("ZccTipSrv: " + SERVICOSARRAY[s].getZccTipSrv());
//		System.out.println("ZccObs: " + SERVICOSARRAY[s].getZccObs());
//		System.out.println("ZccSeqSrv: " + SERVICOSARRAY[s].getZccSeqSrv());
//		System.out.println("ZccStatus: " + SERVICOSARRAY[s].getZccStatus());
//		
//		System.out.println("=====================");
//	}
