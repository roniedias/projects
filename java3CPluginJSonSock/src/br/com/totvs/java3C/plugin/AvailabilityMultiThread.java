package br.com.totvs.java3C.plugin;

import java.util.ArrayList;
import java.util.HashSet;
import br.com.totvs.java3C.JSonParser.monit.AvailabilityParserMon;
import br.com.totvs.java3C.JSonParser.read.ParserRead;
import br.com.totvs.java3C.JSonParser.write.ParserWriteAvail;
import br.com.totvs.java3C.bean.AuxServicos;
import br.com.totvs.java3C.bean.ParamServicosMonit;
import br.com.totvs.java3C.bean.ServerAvailability;
import br.com.totvs.java3C.bean.ServicosItemTipoAmb;
import br.com.totvs.java3C.util.ValidacaoStatusAmb;



/**
 * @author Ronie Dias Pinto
 */


public class AvailabilityMultiThread {
	
	
	private ParamServicosMonit[] SERVICOSARRAY;
	

	private String ZCA_PARAM;
	private String ZCA_CODAMB;
	private String ZCA_TIPAMB;
	private String ZCA_ITEM;
	private String ZCA_MEMO;
	

	
	private String empresaFilial;

	
	private ArrayList<String> LIST_ZBC_SEQ = new ArrayList<String>();
	private ArrayList<String> LIST_ZBC_PORTA = new ArrayList<String>();
	private ArrayList<String> LIST_ZBC_IPHOST = new ArrayList<String>();
	private ArrayList<String> LIST_ZBC_DNS = new ArrayList<String>();
	private ArrayList<String> LIST_ZBC_IPEXT = new ArrayList<String>();
	
	
	
	
	private ArrayList<AuxServicos> auxServicosList = new ArrayList<AuxServicos>();


		
	private String ip;
	private String porta;
	private String environment;
	
	private String ZCC_TIPSRV;
	
	
	private ParamServicosMonit SERVICOS;
		

	private String codTipoServicoTopConnect = "16";
	private String codTipoServicoCtree = "21"; 
	
	private String[] zbhAllTipSrvs;
	private ArrayList<String> zbhTipSrvs = new ArrayList<String>();
	private ArrayList<ServicosItemTipoAmb> servicosItemTipoAmb;


	ArrayList<Thread> execucoes = new ArrayList<Thread>();
	
	
	
 
	public AvailabilityMultiThread(String codAmbiente, String codTipoAmbiente, String codMonitoramento, String codProduto) {
		
		
		this.ZCA_PARAM = codMonitoramento; // Sexto parâmetro passado para STRUPARAMGRVMONITORAMENTO
		this.ZCA_CODAMB = codAmbiente;     // Quarto parâmetro passado para STRUPARAMGRVMONITORAMENTO
		this.ZCA_TIPAMB = codTipoAmbiente; // Decimo primeiro parâmetro passado para STRUPARAMGRVMONITORAMENTO

		
		
		ParserRead parserRead = new ParserRead("GENERIC_READ", codAmbiente, codTipoAmbiente, codProduto);
		
		
		if(parserRead.getZbbStatus().replaceAll("\\s", "").equals("-")) {
			System.out.println("Retorno JSON (leitura) invalido. Verifique o cadastro dos dados do ambiente no Nagios/3C");
			System.exit(3);
		}

		
		zbhAllTipSrvs = parserRead.getZbhTipSrvs();
		
		// Adicionando os tipos de serviço a zbhTipSrvs, excluindo os Tipos TOP e CTREE
		for(int z = 0; z < zbhAllTipSrvs.length; z++) {
			if(!(zbhAllTipSrvs[z].equals(codTipoServicoTopConnect) || zbhAllTipSrvs[z].equals(codTipoServicoCtree))) {
				zbhTipSrvs.add(zbhAllTipSrvs[z]);  
			}									  
		}

		
				
		// Primeiro passo: Adicionar às listas abaixo os IP´s, PORTA´s, DNS´s e números SEQUENCIAIS dos serviços cadastrados no 3C. 
			

		servicosItemTipoAmb = new ArrayList<ServicosItemTipoAmb>();

		for(int lst = 0; lst < zbhTipSrvs.size(); lst++) {			
			servicosItemTipoAmb = parserRead.getServicosItemTipoAmb(zbhTipSrvs.get(lst));
		}
				

			
		for(int s = 0; s < servicosItemTipoAmb.size(); s++) {								   	
					
			LIST_ZBC_IPHOST.add(servicosItemTipoAmb.get(s).getZbcIpHost().replaceAll("\\s", ""));
			LIST_ZBC_PORTA.add(servicosItemTipoAmb.get(s).getZbcPorta().replaceAll("\\s", ""));
			LIST_ZBC_DNS.add(servicosItemTipoAmb.get(s).getZbcDns().replaceAll("\\s", ""));
			LIST_ZBC_IPEXT.add(servicosItemTipoAmb.get(s).getZbcIpExt().replaceAll("\\s", ""));
			LIST_ZBC_SEQ.add(servicosItemTipoAmb.get(s).getZbcSeq().replaceAll("\\s", ""));			   		
		}
			
			
			
		this.ZCA_ITEM = servicosItemTipoAmb.get(0).getZbcItem(); // ZBC_ITEM, do JSON. Mesmo valor, qualquer que seja a posição do ArrayList escolhida
			
		// Validação se o ambiente encontra-se com status ATIVO, EM MANUTENÇÃO, CADASTRO, SUSPENSO ou DESATIVADO. 
		// Ocorre neste ponto pela necessidade de passar-se para ValidacaoStatusAmb também o valor de ZCA_ITEM
		new ValidacaoStatusAmb(parserRead.getZbbStatus().replaceAll("\\s", ""), ZCA_CODAMB, ZCA_ITEM, ZCA_PARAM, ZCA_TIPAMB);

			
			
		this.empresaFilial = servicosItemTipoAmb.get(0).getZbbParNag(); // ZBB_PARNAG, do JSON. Mesmo valor, qualquer que seja a posição do ArrayList escolhida
			
			
			
		// Segundo passo: Efetuar as requisições ao método que verifica a disponibilidade dos serviços, de acordo com seu tipo
		// Fazer um comparativo entre os serviços que estão cadastrados no 3C e o que foi realmente verificado pelo WS
			
		
							
						   
		for(int s = 0; s < servicosItemTipoAmb.size(); s++) {
					
			if(servicosItemTipoAmb.get(s).getZbcBalanc().equals("N")) { // Efetuar o procedimento abaixo apenas para os 
																			 // servidores que não forem balance									  			
		    	this.ip = servicosItemTipoAmb.get(s).getZbcIpHost();
			    this.porta = servicosItemTipoAmb.get(s).getZbcPorta();
				this.environment = servicosItemTipoAmb.get(s).getZbcEnviro();
				this.ZCC_TIPSRV = servicosItemTipoAmb.get(s).getZbcTipSrv(); // Variável que armazena o código do tipo de serviço 
																				  // (02 = WORKFLOW, 03 = WEBSERVICE, 10 = MASTER, etc.)
						
				// Efetuar a chamada ao serviço de disponibilidade, com base nas informações obtidas a partir do cadastro dos serviços
							
							
				if(ZCC_TIPSRV.equals("10")) { // Se o serviço for do tipo MASTER/SLAVE
					Thread t = executa("1", ip, porta, environment, empresaFilial, ZCC_TIPSRV, LIST_ZBC_SEQ, LIST_ZBC_PORTA, LIST_ZBC_IPHOST, LIST_ZBC_DNS, LIST_ZBC_IPEXT);
					execucoes.add(t);
				}	
				else if(ZCC_TIPSRV.equals("02")) { // Se o serviço for do tipo WORKFLOW
					Thread t = executa("2", ip, porta, environment, empresaFilial, ZCC_TIPSRV, LIST_ZBC_SEQ, LIST_ZBC_PORTA, LIST_ZBC_IPHOST, LIST_ZBC_DNS, LIST_ZBC_IPEXT);
					execucoes.add(t);
				}	
				else if(ZCC_TIPSRV.equals("03")) { // Se o serviço for do tipo WEBSERVICE
					Thread t = executa("4", ip, porta, environment, empresaFilial, ZCC_TIPSRV, LIST_ZBC_SEQ, LIST_ZBC_PORTA, LIST_ZBC_IPHOST, LIST_ZBC_DNS, LIST_ZBC_IPEXT);
					execucoes.add(t);
				}	
				else if(ZCC_TIPSRV.equals("04")) { // Se o serviço for do tipo PORTAL
					Thread t = executa("5", ip, porta, environment, empresaFilial, ZCC_TIPSRV, LIST_ZBC_SEQ, LIST_ZBC_PORTA, LIST_ZBC_IPHOST, LIST_ZBC_DNS, LIST_ZBC_IPEXT);
					execucoes.add(t);
				}	
				else if(ZCC_TIPSRV.equals("13")) { // Se o serviço for do tipo LICENSE
					Thread t = executa("6", ip, porta, environment, empresaFilial, ZCC_TIPSRV, LIST_ZBC_SEQ, LIST_ZBC_PORTA, LIST_ZBC_IPHOST, LIST_ZBC_DNS, LIST_ZBC_IPEXT);
					execucoes.add(t);
				}	
				else if(ZCC_TIPSRV.equals("05")) { // Se o serviço for do tipo PROC
					Thread t = executa("7", ip, porta, environment, empresaFilial, ZCC_TIPSRV, LIST_ZBC_SEQ, LIST_ZBC_PORTA, LIST_ZBC_IPHOST, LIST_ZBC_DNS, LIST_ZBC_IPEXT);
					execucoes.add(t);
				}	
				else if(ZCC_TIPSRV.equals("17")) { // Se o serviço for do tipo WEBSERVICE TSS                 
					Thread t = executa("7", ip, porta, environment, empresaFilial, ZCC_TIPSRV, LIST_ZBC_SEQ, LIST_ZBC_PORTA, LIST_ZBC_IPHOST, LIST_ZBC_DNS, LIST_ZBC_IPEXT);
					execucoes.add(t);
				}	
				else { // Senão, serviço será do tipo "ESPECIALIZADO"                                        
					Thread t = executa("3", ip, porta, environment, empresaFilial, ZCC_TIPSRV, LIST_ZBC_SEQ, LIST_ZBC_PORTA, LIST_ZBC_IPHOST, LIST_ZBC_DNS, LIST_ZBC_IPEXT);
					execucoes.add(t);
				}
																							
													
			}
										
		}
		
		
		// Inicia as threads
		for(Thread t : execucoes) {
			t.start();
		}
		
		// Aguarda 2s
		try {
			Thread.sleep(2000); 
		} catch (InterruptedException e) {
			e.printStackTrace();
		}

		// Une as threads, para que somente ao término delas o programa continue abaixo
		for(Thread j : execucoes) {
			try {
				j.join();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}

				
			
		SERVICOSARRAY = new ParamServicosMonit[auxServicosList.size()];
		

		for(int f = 0; f < auxServicosList.size(); f++) {
			SERVICOS = new ParamServicosMonit(auxServicosList.get(f).getZCC_OBS(), auxServicosList.get(f).getZCC_SEQSRV(), auxServicosList.get(f).getZCC_STATUS(), auxServicosList.get(f).getZCC_TIPSRV());
			SERVICOSARRAY[f] = SERVICOS;
		}
			   	
			   	
	 	StringBuilder sb = new StringBuilder();  // Procedimento necessário para armazenar em ZBC_MEMO as informações de  
						   						
	 		 	
	 	// Procedimento para remover repetições
		 HashSet<String> hs = new HashSet<String>();
		 for(AuxServicos a : auxServicosList)
			 hs.add(a.getZCA_MEMO());
		 
		 for(String h : hs)
			 sb.append(h);
			 

		 
	   	ZCA_MEMO = sb.toString();

						   					
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
	
	
	private Thread executa(final String cod, final String ip, final String porta, final String environment, final String empresaFilial, final String ZCC_TIPSRV, final ArrayList<String> LIST_ZBC_SEQ, final ArrayList<String> LIST_ZBC_PORTA, final ArrayList<String> LIST_ZBC_IPHOST, final ArrayList<String> LIST_ZBC_DNS, final ArrayList<String> LIST_ZBC_IPEXT) {		
		Thread t = new Thread(){
			public void run() {
				//AvailabilityParserMonCustomer availabilityParser = new AvailabilityParserMonCustomer(cod, ip, porta, environment, empresaFilial);
				AvailabilityParserMon availabilityParser = new AvailabilityParserMon(cod, ip, porta, environment, empresaFilial);
				ArrayList<AuxServicos> auxServicos = configParametros(availabilityParser, ip, porta, environment, ZCC_TIPSRV, LIST_ZBC_SEQ, LIST_ZBC_PORTA, LIST_ZBC_IPHOST, LIST_ZBC_DNS, LIST_ZBC_IPEXT);
				for(AuxServicos a : auxServicos)
					auxServicosList.add(a);		
	        }
	    };	    
	    return t;
	}
	
	

	private ArrayList<AuxServicos> configParametros(AvailabilityParserMon availabilityParser, String ip, String porta, String environment, String ZCC_TIPSRV, ArrayList<String> LIST_ZBC_SEQ, ArrayList<String> LIST_ZBC_PORTA, ArrayList<String> LIST_ZBC_IPHOST, ArrayList<String> LIST_ZBC_DNS, ArrayList<String> LIST_ZBC_IPEXT) {
		
		String ZCC_STATUS;
		String ZCA_MEMO;
	    ArrayList<ServerAvailability> servers = new ArrayList<ServerAvailability>();
	    String ZCC_SEQSRV = "";
	    String ZCC_OBS = "";
	    boolean serviceMatch = false;
	    AuxServicos auxServicos;
	    ArrayList<AuxServicos> auxServicosList = new ArrayList<AuxServicos>();
	    	    
		
        //ZCA_MEMO = "Environment: " + environment.replaceAll("\\s", "") + ", porta: " + porta.replaceAll("\\s", "") + " - " + availabilityParser.getMessageInfo() + "; ";
	    ZCA_MEMO = "Ip: " + ip + ", environment: " + environment.replaceAll("\\s", "") + ", porta: " + porta.replaceAll("\\s", "") + " - " + availabilityParser.getMessageInfo() + "; ";
		servers = availabilityParser.getServers(); 
		
		                       // servers.size() = 5, 1, 1
		for(int srv = 0; srv < servers.size(); srv++) {				
			if(servers.get(srv).getStatus().equals("0")) {   
				ZCC_STATUS = "A";
			}
			else {
				ZCC_STATUS = "I"; // ou 1 = Indisponivel (I)
			}
						
			for(int list = 0; list < LIST_ZBC_SEQ.size(); list++) {
				
				
																							
				int var1 = Integer.parseInt(LIST_ZBC_PORTA.get(list));
				int var2 = Integer.parseInt(servers.get(srv).getPort().replaceAll("\\s", ""));
														
				if(  ( (var1 == var2) && (LIST_ZBC_IPHOST.get(list).equals(servers.get(srv).getIp())) ) || ( (var1 == var2) && (servers.get(srv).getIp().equalsIgnoreCase(LIST_ZBC_DNS.get(list))) ) || ( (var1 == var2) && (LIST_ZBC_IPEXT.get(list).equals(servers.get(srv).getIp())) )  ) { 
					ZCC_SEQSRV = LIST_ZBC_SEQ.get(list);
					ZCC_OBS = "Servico localizado";
					serviceMatch = true;
				}										 
			}
					
			if(!serviceMatch) {									
				ZCC_OBS = "Servico nao localizado. Verifique o cadastro no 3C e tambem as configurações no arquivo \".ini\" do MASTER.";
			}
			

			
			auxServicos = new AuxServicos();
			
			auxServicos.setZCC_OBS(ZCC_OBS);
			auxServicos.setZCC_SEQSRV(ZCC_SEQSRV);
			auxServicos.setZCC_STATUS(ZCC_STATUS);
			auxServicos.setZCC_TIPSRV(ZCC_TIPSRV);
			auxServicos.setZCA_MEMO(ZCA_MEMO);
			
			auxServicosList.add(auxServicos);
			
			serviceMatch = false;
 			
		}
		
		return auxServicosList;
	}
	
}

