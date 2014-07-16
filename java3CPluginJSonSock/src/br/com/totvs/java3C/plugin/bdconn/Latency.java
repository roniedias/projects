package br.com.totvs.java3C.plugin.bdconn;


import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import br.com.totvs.java3C.JSonParser.monit.LatencyParserMon;
import br.com.totvs.java3C.JSonParser.read.ParserReadLatency;
import br.com.totvs.java3C.JSonParser.write.ParserWriteLatency;

import br.com.totvs.java3C.bean.LatencyLog;
import br.com.totvs.java3C.bean.LogInfo;
import br.com.totvs.java3C.bean.ParamLatenciaMonit;
import br.com.totvs.java3C.bean.ServerLatency;
import br.com.totvs.java3C.bean.ServicosItemTipoAmbLatency;
import br.com.totvs.java3C.util.DateConverter;

import br.com.totvs.java3C.util.ValidacaoStatusAmb;


/**
 * @author Ronie Dias Pinto
 */


public class Latency {
	
	

	
	private String ZCA_PARAM;
	private String ZCA_CODAMB;
	private String ZCA_TIPAMB;
	private String ZCA_ITEM;
	private String ZCA_MEMO;
	

	private ArrayList<String> tiposServicos = new ArrayList<String>(); // Tipo de serviço ERP (10 ou 29)
	
		
	
	private String dataUltimaVerif; // Dados extraídos do 3C, utilizados para execução do monitoramento atual
	private String horaUltimaVerif;	// Obs: Informações contidas em ZBD_DTMONI e ZBD_HRMONI 
	
	private String ZCA_DATLAT; // Dados que serão a base para o próximo monitoramento.
	private String ZCA_HORLAT; 
	
	
	private ArrayList<String> auxListZCA_MEMO = new ArrayList<String>();
	
	private ArrayList<ParamLatenciaMonit> latenciaArrayList = new ArrayList<ParamLatenciaMonit>();

	private ParamLatenciaMonit[] LATENCIA;
	
	private String PINGDATE;
	private String LOGDATE;
	
	
	
	private ArrayList<ServerLatency> servers = new ArrayList<ServerLatency>(); // ArrayList "servers", cujo tamanho será igual à quantidade de serviços cadastrados no 3C
	private LatencyParserMon latencyParser;
	private ArrayList<LatencyLog> latencyLog = new ArrayList<LatencyLog>();
	private LogInfo[] logInfo;
	
	private String[] zbhAllTipSrvs;
	private ArrayList<ServicosItemTipoAmbLatency> servicosItemTipoAmb = new ArrayList<ServicosItemTipoAmbLatency>();
	private ArrayList<ServicosItemTipoAmbLatency> todosServicosItemTipoAmb = new ArrayList<ServicosItemTipoAmbLatency>();
	

	
	// codMonitoramento = 023
	public Latency(String codAmbiente, String codTipoAmbiente, String codMonitoramento, String codProduto) {
					

		this.ZCA_PARAM = codMonitoramento; 
		this.ZCA_CODAMB = codAmbiente;
		this.ZCA_TIPAMB = codTipoAmbiente;
		
		
		ParserReadLatency parserRead = new ParserReadLatency("LATENCY_READ", codAmbiente, codTipoAmbiente, codProduto);
		
		
		if(parserRead.getZbbStatus().replaceAll("\\s", "").equals("-")) {
			System.out.println("Retorno JSON (leitura) invalido. Verifique o cadastro dos dados do ambiente no Nagios/3C");
			System.exit(3);
		}

		
		
		zbhAllTipSrvs = parserRead.getZbhTipSrvs();
		
		// Procedimento para atribuir valor à variável "tipoServico"		
		for(int z = 0; z < zbhAllTipSrvs.length; z++) {
			if(zbhAllTipSrvs[z].equals("10") || zbhAllTipSrvs[z].equals("29")) {
				tiposServicos.add(zbhAllTipSrvs[z]); 
			}
		}
		
		
		for(int t = 0; t < tiposServicos.size(); t++) {			
			
			servicosItemTipoAmb = parserRead.getServicosItemTipoAmb(tiposServicos.get(t));
			
			for(int s = 0; s < servicosItemTipoAmb.size(); s++) {
				todosServicosItemTipoAmb.add(servicosItemTipoAmb.get(s));
			}	
			
		}
		
		
		
		
		
		// Valores iguais, independentemente da posição do ArrayList
		this.ZCA_ITEM = servicosItemTipoAmb.get(0).getZbcItem();
		this.dataUltimaVerif = servicosItemTipoAmb.get(0).getZbdDtMoni(); // Sempre informada a data de execução do último monitoramento cujo param. monit. é igual a 023 (Qualidade de rede) 
		this.horaUltimaVerif = servicosItemTipoAmb.get(0).getZbdHrMoni(); // Sempre informada a hora de execução do último monitoramento cujo param. monit. é igual a 023 (Qualidade de rede)
		
		
		
		// Validação se o ambiente encontra-se com status ATIVO, EM MANUTENÇÃO, CADASTRO, SUSPENSO ou DESATIVADO. 
		// Ocorre neste ponto pela necessidade de passar-se para ValidacaoStatusAmb também o valor de ZCA_ITEM
		new ValidacaoStatusAmb(parserRead.getZbbStatus().replaceAll("\\s", ""), ZCA_CODAMB, ZCA_ITEM, ZCA_PARAM, ZCA_TIPAMB);

		
		
		
		

		// Se um dos campos (DATA ou HORA) da última verificação estiver vazio, 
		// serão atribuídos os valores correntes para AMBOS os atributos 
		
		
		if(dataUltimaVerif.replaceAll("\\s", "").equals("") || horaUltimaVerif.replaceAll("\\s", "").equals("")) { 

//			DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
			DateFormat dateFormat = new SimpleDateFormat("yyyyMMdd");
			DateFormat timeFormat = new SimpleDateFormat("HH:mm:ss");
			

			Date date = new Date();	
			
			dataUltimaVerif = dateFormat.format(date);
			horaUltimaVerif = timeFormat.format(date);
		}
				

		
			
	  /* Execução da chamada à rotina que verifica a latência de rede. 
	   * 
	   * Para efetuar a requisição, é necessário passar as seguintes informações:
	   *
	   * IP do servidor Protheus;
	   * Porta de conexão;
	   * Nome do ambiente;
	   * Data e hora a partir das quais será feita a leitura do arquivo. Somente serão lidas as linhas dentro do 
	   * período enviado;
	   * 	
 	   */
	
				
		dataUltimaVerif = new DateConverter(dataUltimaVerif, "yyyyMMdd", "dd/MM/yyyy").convert();
		
		
//		dataUltimaVerif = "19/11/2013"; // Temporario 
//		horaUltimaVerif = "09:30:00"; // Temporario


				
			
		for(int s = 0; s < todosServicosItemTipoAmb.size(); s++) { // Novo	
						
			if(todosServicosItemTipoAmb.size() == 1) { // Se houver apenas um serviço cadastrado (getZBC_BALANC().equals("N")) 
				servers.add(new ServerLatency(todosServicosItemTipoAmb.get(s).getZbcIpHost().replaceAll("\\s", ""), todosServicosItemTipoAmb.get(s).getZbcPorta().replaceAll("\\s", ""), todosServicosItemTipoAmb.get(s).getZbcEnviro().replaceAll("\\s", ""), dataUltimaVerif, horaUltimaVerif));				
			}
			else { 
				if(todosServicosItemTipoAmb.get(s).getZbcBalanc().equals("S")) { // Apenas serviços do tipo slave
					servers.add(new ServerLatency(todosServicosItemTipoAmb.get(s).getZbcIpHost().replaceAll("\\s", ""), todosServicosItemTipoAmb.get(s).getZbcPorta().replaceAll("\\s", ""), todosServicosItemTipoAmb.get(s).getZbcEnviro().replaceAll("\\s", ""), dataUltimaVerif, horaUltimaVerif));

//				System.out.println("dataUltimaVerif: " + dataUltimaVerif);
//				System.out.println("ENVIRONMENT: " + todosServicosItemTipoAmb.get(s).getZbcEnviro().replaceAll("\\s", ""));
//				System.out.println("horaUltimaVerif: " + horaUltimaVerif);
//				System.out.println("IP: " + todosServicosItemTipoAmb.get(s).getZbcIpHost().replaceAll("\\s", ""));
//				System.out.println("PORTA: " + todosServicosItemTipoAmb.get(s).getZbcPorta().replaceAll("\\s", ""));
//				System.out.println("\n ===================================================================");
					
				}
			}
			
		}
		
						
		
		latencyParser = new LatencyParserMon(servers); 
				
		ZCA_DATLAT = latencyParser.getLastDate();
		ZCA_HORLAT = latencyParser.getLastHour(); 
		
		latencyLog = latencyParser.getLatencyLog(); // Preenchendo a variável latencyLog com as informaçoes do segundo nível retornado no XML
				
		
		for(int l = 0; l < latencyLog.size(); l++) {
					
			if(latencyLog.get(l).getStatus().equals("0")) { // Se foi possível ler o arquivo (.out)
								
				auxListZCA_MEMO.add(latencyLog.get(l).getEnvironment() + ", " + latencyLog.get(l).getPort() + ": " + latencyLog.get(l).getMessage() + ". ");				
				logInfo = latencyLog.get(l).getLogInfo();
				
				for(int s = 0; s < logInfo.length; s++) { // Se chegou aqui, então os dados das estações serão adicionados ao ArrayList para serem enviados ao 3C 					
					
					this.PINGDATE = new DateConverter(logInfo[s].getPingDate(), "dd/MM/yyyy", "yyyyMMdd").convert();					
					
					// Convertendo o LOGDATE 
					this.LOGDATE = new DateConverter(logInfo[s].getLogDate(), "dd/MM/yyyy", "yyyyMMdd").convert();

					latenciaArrayList.add(new ParamLatenciaMonit(logInfo[s].getEnvironment(), Float.parseFloat(logInfo[s].getAvg()), PINGDATE, LOGDATE, logInfo[s].getPingHour(), logInfo[s].getLogHour(), logInfo[s].getRemoteIp(), Float.parseFloat(logInfo[s].getLost()), Float.parseFloat(logInfo[s].getMax()), Float.parseFloat(logInfo[s].getMin()), logInfo[s].getRemoteMachine(), logInfo[s].getActivityTime(), logInfo[s].getServerThread(), logInfo[s].getRemoteUser()));
				}
		
			}
			else if (latencyLog.get(l).getStatus().equals("1")) { 	// Se ocorrer um erro	
				auxListZCA_MEMO.add(latencyLog.get(l).getEnvironment() + ", " + latencyLog.get(l).getPort() + ": " + latencyLog.get(l).getMessage() + ". ");
			}
			else if(latencyLog.get(l).getStatus().equals("2")) { // Senão, o status vai ser 2, que significa que: "Nao foi possivel obter o arquivo de log no servidor. A chave LATENCYLOGFILE nao existe ou esta sem conteudo". Neste caso, não se trata de erro e sim uma característica (necessário pois nem todos os servidores PROTHEUS conterão a chave habilitada)
				auxListZCA_MEMO.add(latencyLog.get(l).getEnvironment() + ", " + latencyLog.get(l).getPort() + ": LATENCYLOGFILE nao habilitado. ");
			}
			else { // Senão o status vai ser 3. O arquivo não possui informações de latencia (um dos motivos PODE ser a data/hora informada(s) para verificação)
				auxListZCA_MEMO.add(latencyLog.get(l).getEnvironment() + ", " + latencyLog.get(l).getPort() + ": Arquivo de log nao possui informacoes de latencia dentro do periodo informado. ");				
			}			
		}
			
			
		
		if(latenciaArrayList.size() == 0) { // Se não houver nenhum dado, de nenhuma estação para ser 
											// enviado ao 3C (por erro ou pela chave LATENCYLOGFILE 
											// não existir ou estar sem conteúdo) 
			System.out.println("Nao foi possível obter dado(s) referente(s) a nenhuma estacao.");
			System.exit(3);
		}

		
		// Instanciando
		LATENCIA = new ParamLatenciaMonit[latenciaArrayList.size()];
		
		
				
		// Preenchendo o Array LATENCIA para passá-lo para a gravação 
		for(int lst = 0; lst < latenciaArrayList.size(); lst++) {
			LATENCIA[lst] = latenciaArrayList.get(lst);			
		}
		
		
				
	   	StringBuilder sb = new StringBuilder();  
	   	for (String s : auxListZCA_MEMO)
	   	{
	   		sb.append(s);
	   	}

	   	ZCA_MEMO = sb.toString();


	
		ParserWriteLatency parserWriteLatency = new ParserWriteLatency("LATENCY_WRITE", ZCA_CODAMB, ZCA_TIPAMB, ZCA_ITEM, ZCA_PARAM, ZCA_MEMO, ZCA_DATLAT, ZCA_HORLAT, LATENCIA);
		
	   	if(parserWriteLatency.getStatus().equals("0")) { // Se correu tudo bem com a gravação
	   		System.out.println("Resultado latencia: " + parserWriteLatency.getZcaResult() + "; " + "Resultado perda pacote(s): " + parserWriteLatency.getResLos() + ". " + ZCA_MEMO);
	   		System.exit(Integer.parseInt(parserWriteLatency.getLimiar()));
	   	}
	   	else {
	   		System.out.println(parserWriteLatency.getMessage());
	   		System.exit(Integer.parseInt(parserWriteLatency.getLimiar()));
	   	}
		
							
	}
	

}
