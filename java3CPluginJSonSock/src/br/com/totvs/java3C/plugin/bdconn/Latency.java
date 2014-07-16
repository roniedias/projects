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
	

	private ArrayList<String> tiposServicos = new ArrayList<String>(); // Tipo de servi�o ERP (10 ou 29)
	
		
	
	private String dataUltimaVerif; // Dados extra�dos do 3C, utilizados para execu��o do monitoramento atual
	private String horaUltimaVerif;	// Obs: Informa��es contidas em ZBD_DTMONI e ZBD_HRMONI 
	
	private String ZCA_DATLAT; // Dados que ser�o a base para o pr�ximo monitoramento.
	private String ZCA_HORLAT; 
	
	
	private ArrayList<String> auxListZCA_MEMO = new ArrayList<String>();
	
	private ArrayList<ParamLatenciaMonit> latenciaArrayList = new ArrayList<ParamLatenciaMonit>();

	private ParamLatenciaMonit[] LATENCIA;
	
	private String PINGDATE;
	private String LOGDATE;
	
	
	
	private ArrayList<ServerLatency> servers = new ArrayList<ServerLatency>(); // ArrayList "servers", cujo tamanho ser� igual � quantidade de servi�os cadastrados no 3C
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
		
		// Procedimento para atribuir valor � vari�vel "tipoServico"		
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
		
		
		
		
		
		// Valores iguais, independentemente da posi��o do ArrayList
		this.ZCA_ITEM = servicosItemTipoAmb.get(0).getZbcItem();
		this.dataUltimaVerif = servicosItemTipoAmb.get(0).getZbdDtMoni(); // Sempre informada a data de execu��o do �ltimo monitoramento cujo param. monit. � igual a 023 (Qualidade de rede) 
		this.horaUltimaVerif = servicosItemTipoAmb.get(0).getZbdHrMoni(); // Sempre informada a hora de execu��o do �ltimo monitoramento cujo param. monit. � igual a 023 (Qualidade de rede)
		
		
		
		// Valida��o se o ambiente encontra-se com status ATIVO, EM MANUTEN��O, CADASTRO, SUSPENSO ou DESATIVADO. 
		// Ocorre neste ponto pela necessidade de passar-se para ValidacaoStatusAmb tamb�m o valor de ZCA_ITEM
		new ValidacaoStatusAmb(parserRead.getZbbStatus().replaceAll("\\s", ""), ZCA_CODAMB, ZCA_ITEM, ZCA_PARAM, ZCA_TIPAMB);

		
		
		
		

		// Se um dos campos (DATA ou HORA) da �ltima verifica��o estiver vazio, 
		// ser�o atribu�dos os valores correntes para AMBOS os atributos 
		
		
		if(dataUltimaVerif.replaceAll("\\s", "").equals("") || horaUltimaVerif.replaceAll("\\s", "").equals("")) { 

//			DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
			DateFormat dateFormat = new SimpleDateFormat("yyyyMMdd");
			DateFormat timeFormat = new SimpleDateFormat("HH:mm:ss");
			

			Date date = new Date();	
			
			dataUltimaVerif = dateFormat.format(date);
			horaUltimaVerif = timeFormat.format(date);
		}
				

		
			
	  /* Execu��o da chamada � rotina que verifica a lat�ncia de rede. 
	   * 
	   * Para efetuar a requisi��o, � necess�rio passar as seguintes informa��es:
	   *
	   * IP do servidor Protheus;
	   * Porta de conex�o;
	   * Nome do ambiente;
	   * Data e hora a partir das quais ser� feita a leitura do arquivo. Somente ser�o lidas as linhas dentro do 
	   * per�odo enviado;
	   * 	
 	   */
	
				
		dataUltimaVerif = new DateConverter(dataUltimaVerif, "yyyyMMdd", "dd/MM/yyyy").convert();
		
		
//		dataUltimaVerif = "19/11/2013"; // Temporario 
//		horaUltimaVerif = "09:30:00"; // Temporario


				
			
		for(int s = 0; s < todosServicosItemTipoAmb.size(); s++) { // Novo	
						
			if(todosServicosItemTipoAmb.size() == 1) { // Se houver apenas um servi�o cadastrado (getZBC_BALANC().equals("N")) 
				servers.add(new ServerLatency(todosServicosItemTipoAmb.get(s).getZbcIpHost().replaceAll("\\s", ""), todosServicosItemTipoAmb.get(s).getZbcPorta().replaceAll("\\s", ""), todosServicosItemTipoAmb.get(s).getZbcEnviro().replaceAll("\\s", ""), dataUltimaVerif, horaUltimaVerif));				
			}
			else { 
				if(todosServicosItemTipoAmb.get(s).getZbcBalanc().equals("S")) { // Apenas servi�os do tipo slave
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
		
		latencyLog = latencyParser.getLatencyLog(); // Preenchendo a vari�vel latencyLog com as informa�oes do segundo n�vel retornado no XML
				
		
		for(int l = 0; l < latencyLog.size(); l++) {
					
			if(latencyLog.get(l).getStatus().equals("0")) { // Se foi poss�vel ler o arquivo (.out)
								
				auxListZCA_MEMO.add(latencyLog.get(l).getEnvironment() + ", " + latencyLog.get(l).getPort() + ": " + latencyLog.get(l).getMessage() + ". ");				
				logInfo = latencyLog.get(l).getLogInfo();
				
				for(int s = 0; s < logInfo.length; s++) { // Se chegou aqui, ent�o os dados das esta��es ser�o adicionados ao ArrayList para serem enviados ao 3C 					
					
					this.PINGDATE = new DateConverter(logInfo[s].getPingDate(), "dd/MM/yyyy", "yyyyMMdd").convert();					
					
					// Convertendo o LOGDATE 
					this.LOGDATE = new DateConverter(logInfo[s].getLogDate(), "dd/MM/yyyy", "yyyyMMdd").convert();

					latenciaArrayList.add(new ParamLatenciaMonit(logInfo[s].getEnvironment(), Float.parseFloat(logInfo[s].getAvg()), PINGDATE, LOGDATE, logInfo[s].getPingHour(), logInfo[s].getLogHour(), logInfo[s].getRemoteIp(), Float.parseFloat(logInfo[s].getLost()), Float.parseFloat(logInfo[s].getMax()), Float.parseFloat(logInfo[s].getMin()), logInfo[s].getRemoteMachine(), logInfo[s].getActivityTime(), logInfo[s].getServerThread(), logInfo[s].getRemoteUser()));
				}
		
			}
			else if (latencyLog.get(l).getStatus().equals("1")) { 	// Se ocorrer um erro	
				auxListZCA_MEMO.add(latencyLog.get(l).getEnvironment() + ", " + latencyLog.get(l).getPort() + ": " + latencyLog.get(l).getMessage() + ". ");
			}
			else if(latencyLog.get(l).getStatus().equals("2")) { // Sen�o, o status vai ser 2, que significa que: "Nao foi possivel obter o arquivo de log no servidor. A chave LATENCYLOGFILE nao existe ou esta sem conteudo". Neste caso, n�o se trata de erro e sim uma caracter�stica (necess�rio pois nem todos os servidores PROTHEUS conter�o a chave habilitada)
				auxListZCA_MEMO.add(latencyLog.get(l).getEnvironment() + ", " + latencyLog.get(l).getPort() + ": LATENCYLOGFILE nao habilitado. ");
			}
			else { // Sen�o o status vai ser 3. O arquivo n�o possui informa��es de latencia (um dos motivos PODE ser a data/hora informada(s) para verifica��o)
				auxListZCA_MEMO.add(latencyLog.get(l).getEnvironment() + ", " + latencyLog.get(l).getPort() + ": Arquivo de log nao possui informacoes de latencia dentro do periodo informado. ");				
			}			
		}
			
			
		
		if(latenciaArrayList.size() == 0) { // Se n�o houver nenhum dado, de nenhuma esta��o para ser 
											// enviado ao 3C (por erro ou pela chave LATENCYLOGFILE 
											// n�o existir ou estar sem conte�do) 
			System.out.println("Nao foi poss�vel obter dado(s) referente(s) a nenhuma estacao.");
			System.exit(3);
		}

		
		// Instanciando
		LATENCIA = new ParamLatenciaMonit[latenciaArrayList.size()];
		
		
				
		// Preenchendo o Array LATENCIA para pass�-lo para a grava��o 
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
		
	   	if(parserWriteLatency.getStatus().equals("0")) { // Se correu tudo bem com a grava��o
	   		System.out.println("Resultado latencia: " + parserWriteLatency.getZcaResult() + "; " + "Resultado perda pacote(s): " + parserWriteLatency.getResLos() + ". " + ZCA_MEMO);
	   		System.exit(Integer.parseInt(parserWriteLatency.getLimiar()));
	   	}
	   	else {
	   		System.out.println(parserWriteLatency.getMessage());
	   		System.exit(Integer.parseInt(parserWriteLatency.getLimiar()));
	   	}
		
							
	}
	

}
