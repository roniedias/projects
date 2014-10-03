/**
 * @author Ronie Dias Pinto
 */


package br.com.totvs.java3C.plugin;

import java.util.ArrayList;


import br.com.totvs.java3C.JSonParser.monit.PerformanceParserMon;
import br.com.totvs.java3C.JSonParser.read.ParserRead;
import br.com.totvs.java3C.JSonParser.write.ParserWritePerformance;
import br.com.totvs.java3C.bean.PerformanceInfo;
import br.com.totvs.java3C.bean.ServicosItemTipoAmb;
import br.com.totvs.java3C.util.ValidacaoStatusAmb;


public class Performance {
		
	private String ZCA_PARAM;
	private String ZCA_CODAMB;
	private String ZCA_TIPAMB;
	private String ZCA_ITEM;
	
	private String[] zbhTipSrvs;
	private ArrayList<ServicosItemTipoAmb> servicosItemTipoAmb;

	private ArrayList<String> LIST_ZBC_SEQ = new ArrayList<String>();
	private ArrayList<String> LIST_ZBC_PORTA = new ArrayList<String>();
	private ArrayList<String> LIST_ZBC_IPHOST = new ArrayList<String>();
	private ArrayList<String> LIST_ZBC_DNS = new ArrayList<String>();
	private ArrayList<String> LIST_ZBC_IPEXT = new ArrayList<String>();
	
	private String empresaFilial;
	
	ArrayList<Thread> execucoes = new ArrayList<Thread>();
	
	ArrayList<PerformanceInfo> performanceInfoArr = new ArrayList<PerformanceInfo>();
	

	
	
	public Performance(String codAmbiente, String codTipoAmbiente, String codMonitoramento, String codProduto) {
		
		this.ZCA_PARAM = codMonitoramento; 
		this.ZCA_CODAMB = codAmbiente;     
		this.ZCA_TIPAMB = codTipoAmbiente;
		
		ParserRead parserRead = new ParserRead("PERFORMANCE_READ", codAmbiente, codTipoAmbiente, codProduto);
		
		if(parserRead.getZbbStatus().replaceAll("\\s", "").equals("-")) {
			System.out.println("Retorno JSON (leitura) invalido. Verifique o cadastro dos dados do ambiente no Nagios/3C");
			System.exit(3);
		}
		
		zbhTipSrvs = parserRead.getZbhTipSrvs();
		
		for(int lst = 0; lst < zbhTipSrvs.length; lst++)
			servicosItemTipoAmb = parserRead.getServicosItemTipoAmb(zbhTipSrvs[lst]);
		
		
		for(int s = 0; s < servicosItemTipoAmb.size(); s++) {								   				
			LIST_ZBC_IPHOST.add(servicosItemTipoAmb.get(s).getZbcIpHost().replaceAll("\\s", ""));
			LIST_ZBC_PORTA.add(servicosItemTipoAmb.get(s).getZbcPorta().replaceAll("\\s", ""));
			LIST_ZBC_DNS.add(servicosItemTipoAmb.get(s).getZbcDns().replaceAll("\\s", ""));
			LIST_ZBC_IPEXT.add(servicosItemTipoAmb.get(s).getZbcIpExt().replaceAll("\\s", ""));
			LIST_ZBC_SEQ.add(servicosItemTipoAmb.get(s).getZbcSeq().replaceAll("\\s", ""));			   		
		}
			
						
		this.ZCA_ITEM = servicosItemTipoAmb.get(0).getZbcItem(); // ZBC_ITEM, do JSON. Mesmo valor, qualquer que seja a posição do ArrayList escolhida
			
		new ValidacaoStatusAmb(parserRead.getZbbStatus().replaceAll("\\s", ""), ZCA_CODAMB, ZCA_ITEM, ZCA_PARAM, ZCA_TIPAMB);
	
		this.empresaFilial = servicosItemTipoAmb.get(0).getZbbParNag(); // ZBB_PARNAG, do JSON. Mesmo valor, qualquer que seja a posição do ArrayList escolhida
			
		for(int s = 0; s < servicosItemTipoAmb.size(); s++) {
			
	    	String ip = servicosItemTipoAmb.get(s).getZbcIpHost();
		    String porta = servicosItemTipoAmb.get(s).getZbcPorta();
			String environment = servicosItemTipoAmb.get(s).getZbcEnviro();
			String tipoServico = servicosItemTipoAmb.get(s).getZbcTipSrv();

			Thread t = executa(ip, porta, environment, tipoServico);
			execucoes.add(t);
						
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
		

		// Escrita
		
		ParserWritePerformance parserWritePerformance = new ParserWritePerformance("PERFORMANCE_WRITE", ZCA_CODAMB, ZCA_TIPAMB, ZCA_ITEM, ZCA_PARAM, performanceInfoArr);
		
	   	if(parserWritePerformance.getStatus().equals("0")) { // Se correu tudo bem com a gravação
	   		System.out.println("Resultado: " + parserWritePerformance.getZcaResult());
	   		System.exit(Integer.parseInt(parserWritePerformance.getLimiar()));
	   	}
	   	else {
	   		System.out.println(parserWritePerformance.getMessage());
	   		System.exit(Integer.parseInt(parserWritePerformance.getLimiar()));
	   	}
				
				
	}//Fim construtor


	private Thread executa(final String ip, final String porta, final String environment, final String tipoServico) {
		Thread t = new Thread(){
			public void run() {
				
				// Execução do monitoramento, propriamente dito
				PerformanceParserMon availabilityParser = new PerformanceParserMon(ip, porta, environment, empresaFilial);
				
				// Armazenamento do resultado retornado por cada thread em um ArrayList de objetos do tipo "performanceInfoArr"
				performanceInfoArr.add(new PerformanceInfo(ip, porta, environment, tipoServico, availabilityParser.getTotaltime()));				
	        }
	    };	    
	    return t; // Para fazer o join das threads

	}

}
