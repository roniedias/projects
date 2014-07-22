package br.com.totvs.java3C.deprecated;

import java.util.ArrayList;

import br.com.totvs.java3C.JSonParser.monit.AvailabilityParserMon;
import br.com.totvs.java3C.JSonParser.read.ParserRead;
import br.com.totvs.java3C.JSonParser.write.ParserWriteAvail;
import br.com.totvs.java3C.bean.ParamServicosMonit;
import br.com.totvs.java3C.bean.ServerAvailability;
import br.com.totvs.java3C.bean.ServicosItemTipoAmb;

import br.com.totvs.java3C.util.ValidacaoStatusAmb;



/**
 * @author Ronie Dias Pinto
 */


public class Availability {
	
	
	private ParamServicosMonit[] SERVICOSARRAY;
	

	private String ZCA_PARAM;
	private String ZCA_CODAMB;
	private String ZCA_TIPAMB;
	private String ZCA_ITEM;
	private String ZCA_MEMO;

	

	private ArrayList<String> auxListZCA_MEMO = new ArrayList<String>();
	
	private String empresaFilial;

	
	private ArrayList<String> LIST_ZBC_SEQ = new ArrayList<String>();
	private ArrayList<String> LIST_ZBC_PORTA = new ArrayList<String>();
	private ArrayList<String> LIST_ZBC_IPHOST = new ArrayList<String>();
	private ArrayList<String> LIST_ZBC_DNS = new ArrayList<String>();
	private ArrayList<String> LIST_ZBC_IPEXT = new ArrayList<String>();
	
	
	
	private ArrayList<String> auxSERVICOSListZCC_OBS = new ArrayList<String>();
	private ArrayList<String> auxSERVICOSListZCC_SEQSRV = new ArrayList<String>(); 
	private ArrayList<String> auxSERVICOSListZCC_STATUS = new ArrayList<String>(); 
	private ArrayList<String> auxSERVICOSListZCC_TIPSRV = new ArrayList<String>();


		
	private String ip;
	private String porta;
	private String environment;
	
	private String ZCC_SEQSRV = ""; 
	private String ZCC_STATUS;
	private String ZCC_OBS; 
	private String ZCC_TIPSRV;
	
	
	private ParamServicosMonit SERVICOS;
	
	private boolean serviceMatch;
	
	private int statusGeralAmbiente;
	private static int numberOfChecks = 0;
	
	private AvailabilityParserMon availabilityParser;
	private ArrayList<ServerAvailability> servers = new ArrayList<ServerAvailability>();
	
	private String codTipoServicoTopConnect = "16";
	private String codTipoServicoCtree = "21"; 
	
	private String[] zbhAllTipSrvs;
	private ArrayList<String> zbhTipSrvs = new ArrayList<String>();
	private ArrayList<ServicosItemTipoAmb> servicosItemTipoAmb;


	
	
	
 
	public Availability(String codAmbiente, String codTipoAmbiente, String codMonitoramento, String codProduto) {
		
		++numberOfChecks;
		
//		System.out.println(numberOfChecks);
		
		
		
		
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
					
					
//					System.out.println("ip: " + ip);
//					System.out.println("porta: " + porta);
//					System.out.println("environment: " + environment);
//					System.out.println("TIPSRV: " + ZCC_TIPSRV);
				    	

						
						// Efetuar a chamada ao serviço de disponibilidade, com base nas informações obtidas a partir do cadastro dos serviços
						
						
						
							
						if(ZCC_TIPSRV.equals("10")) { // Se o serviço for do tipo MASTER/SLAVE
							availabilityParser = new AvailabilityParserMon("1", ip, porta, environment, empresaFilial);
						}
						else if(ZCC_TIPSRV.equals("02")) { // Se o serviço for do tipo WORKFLOW
							availabilityParser = new AvailabilityParserMon("2", ip, porta, environment, empresaFilial);
						}
						else if(ZCC_TIPSRV.equals("03")) { // Se o serviço for do tipo WEBSERVICE
							availabilityParser = new AvailabilityParserMon("4", ip, porta, environment, empresaFilial);
						}
						else if(ZCC_TIPSRV.equals("04")) { // Se o serviço for do tipo PORTAL
							availabilityParser = new AvailabilityParserMon("5", ip, porta, environment, empresaFilial);
						}
						else if(ZCC_TIPSRV.equals("13")) { // Se o serviço for do tipo LICENSE
							availabilityParser = new AvailabilityParserMon("6", ip, porta, environment, empresaFilial);
						}
						else if(ZCC_TIPSRV.equals("05")) { // Se o serviço for do tipo PROC 
								availabilityParser = new AvailabilityParserMon("7", ip, porta, environment, empresaFilial); // Obs: Todos os TIPOS DE SERVICO
						}																						     // abaixo do ITEM DE AMBIENTE 
						else if(ZCC_TIPSRV.equals("17")) { // Se o serviço for do tipo WEBSERVICE TSS                // "PROTHEUS TSS": PROC, WEBSERVICE TSS 
							availabilityParser = new AvailabilityParserMon("7", ip, porta, environment, empresaFilial); // (com exceção do TOPCONNECT, que nem
						}																						     // é analisado) terão codigo 7 na chamada 
						else { // Senão, serviço será do tipo "ESPECIALIZADO"                                        // ao WS de disponibilidade
								availabilityParser = new AvailabilityParserMon("3", ip, porta, environment, empresaFilial);
						}
							
																
						
						if(availabilityParser.getStatusInfo().equals("1")) {
							statusGeralAmbiente = 1;
						}
							
							
							
						auxListZCA_MEMO.add("Environment: " + environment.replaceAll("\\s", "") + ", porta: " + porta.replaceAll("\\s", "") + " - " + availabilityParser.getMessageInfo() + "; "); // Quinto parâmetro passado para STRUPARAMGRVMONITORAMENTO
						
//						System.out.println("Environment: " + environment.replaceAll("\\s", "") + ", porta: " + porta.replaceAll("\\s", "") + " - " + availabilityParser.getMessageInfo() + "; ");
						
														
						servers = availabilityParser.getServers(); // Atribuindo a SERVERS o array contendo as informações 
																	  // dos servidores monitorados pelo WS de disponibilidade
							
						for(int srv = 0; srv < servers.size(); srv++) {
								
								
							if(servers.get(srv).getStatus().equals("0")) { // Necessário verificar se o servidor está: 0 = Disponivel (A)...  
								this.ZCC_STATUS = "A"; // Terceiro parâmetro passado para STRUPARAMSERVICOSMONITORAMENTO
							}
							else {
								this.ZCC_STATUS = "I"; // ou 1 = Indisponivel (I)
							}

								
							// Rotina que faz o "match" entre os serviços cadastrados no 3C e os serviços "reais".
							// A condição somente será verdadeira se o IP e porta forem mutuamente iguais OU se o DNS e porta 
							// forem mutuamente iguais OU se o IP EXTERNO e porta forem mutuamente iguais
							
							for(int list = 0; list < LIST_ZBC_SEQ.size(); list++) {
																				
									
								int var1 = Integer.parseInt(LIST_ZBC_PORTA.get(list));
								int var2 = Integer.parseInt(servers.get(srv).getPort().replaceAll("\\s", ""));
																
								if(  ( (var1 == var2) && (LIST_ZBC_IPHOST.get(list).equals(servers.get(srv).getIp())) ) || ( (var1 == var2) && (servers.get(srv).getIp().equalsIgnoreCase(LIST_ZBC_DNS.get(list))) ) || ( (var1 == var2) && (LIST_ZBC_IPEXT.get(list).equals(servers.get(srv).getIp())) )  ) { 
									this.ZCC_SEQSRV = LIST_ZBC_SEQ.get(list);
									this.ZCC_OBS = "Servico localizado";
									serviceMatch = true;
								}										 
							}
								
							if(!serviceMatch) {									
								this.ZCC_OBS = "Servico nao localizado. Verifique o cadastro no 3C e tambem as configurações no arquivo \".ini\" do MASTER.";
							}

								
								
							// Os dados dos serviços, por fim devem ser armazenados em uma lista auxiliar para serem passados ao final, 
							// para GRVMONITORAMENTO
								
							auxSERVICOSListZCC_OBS.add(ZCC_OBS);
							auxSERVICOSListZCC_SEQSRV.add(ZCC_SEQSRV); 
							auxSERVICOSListZCC_STATUS.add(ZCC_STATUS); 
							auxSERVICOSListZCC_TIPSRV.add(ZCC_TIPSRV);
								
							this.ZCC_SEQSRV = ""; 	// Garantindo que o valor de ZCC_SEQSRV ficará em branco, para a próxima verificação
								
							serviceMatch = false;
								
					}
													
				}
										
			}
				

					
			
			/* Este procedimento se faz necessários devido a um problema que ocorre quando há concorrência no uso de tabelas do sistema
			 * A correção da lib já foi realizada e um pacote disponibilizado no Portal do Cliente:
			 * - FWTableDDL.prw, com data igual ou superior a 01/07/2013. 
			 * O fato é que nem todos os clientes possuem este arquivo atualizado, por isso ainda se tem que fazer essas diversas checagens 
			 */
			
			
			if(statusGeralAmbiente == 1 && numberOfChecks < 2) {   // Se o resultado final do método de disponibilidade 
																   // for igual a 1 (não O.K.) e o número de chamadas a    
																   // ele for menor do que o valor especificado em 
																   // numberOfChecks, uma nova chamada ao método será 
																   // efetuada
			
						
			   	try {
			   		Thread.sleep(60000); // Espera 1 minuto
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			

				new Availability(codAmbiente, codTipoAmbiente, codMonitoramento, codProduto);
						
			}
			else {			
			
				SERVICOSARRAY = new ParamServicosMonit[auxSERVICOSListZCC_SEQSRV.size()]; 
						  
				for(int f = 0; f < auxSERVICOSListZCC_SEQSRV.size(); f++) {
					SERVICOS = new ParamServicosMonit(auxSERVICOSListZCC_OBS.get(f), auxSERVICOSListZCC_SEQSRV.get(f), auxSERVICOSListZCC_STATUS.get(f), auxSERVICOSListZCC_TIPSRV.get(f));
					SERVICOSARRAY[f] = SERVICOS;
				}
			   	
			   	
			   	StringBuilder sb = new StringBuilder();  // Procedimento necessário para armazenar em ZBC_MEMO as informações de  
						   											 // todos os servidores, retornadas pelo método de disponibilidade
			   	for (String s : auxListZCA_MEMO)
			   	{
			   	    sb.append(s);
			   	}
	
			   	
			   	ZCA_MEMO = sb.toString();
			   	ZCA_MEMO = ZCA_MEMO + "Obtido na tentativa numero " + numberOfChecks + ".";
			   	ZCA_MEMO = ZCA_MEMO + " Status geral retornado pelo WS: " + String.valueOf(statusGeralAmbiente);
						   	
				
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
}
