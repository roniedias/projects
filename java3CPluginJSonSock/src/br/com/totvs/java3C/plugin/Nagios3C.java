package br.com.totvs.java3C.plugin;



/**
 * @author Ronie Dias Pinto
 */


public class Nagios3C {
	
	
	public Nagios3C(String codCliente, String codEmpresa, String codAmbiente, String codTipoAmbiente, String codMonitoramento, String codProduto) {
						
		
		if(codMonitoramento.equals("004")) { 
			new Availability(codAmbiente, codTipoAmbiente, codMonitoramento, codProduto);
		}
		else if(codMonitoramento.equals("025")) {
			new ActiveConnections(codAmbiente, codTipoAmbiente, codMonitoramento, codProduto);
		}
		else if(codMonitoramento.equals("045")) {
			new LicenseInfo(codAmbiente, codTipoAmbiente, codMonitoramento, codProduto);
		}
		else if(codMonitoramento.equals("002")) {
			new DiskSpace(codAmbiente, codTipoAmbiente, codMonitoramento, codProduto);
		}
		else if(codMonitoramento.equals("046")) {
			new Sigamat(codAmbiente, codTipoAmbiente, codMonitoramento, codProduto);
		}
		else if(codMonitoramento.equals("026")) {
			new Tss(codAmbiente, codTipoAmbiente, codMonitoramento, codProduto);
		}
		else if(codMonitoramento.equals("023")) {
			new Latency(codAmbiente, codTipoAmbiente, codMonitoramento, codProduto);
		}
		else if(codMonitoramento.equals("079")) {
			new StatusItemAmbiente(codAmbiente, codTipoAmbiente, codMonitoramento, codProduto);
		}
		else {
			System.out.println("Codigo de monitoramento invalido.");
			System.exit(3);
		}		
		
	}
	
				
		
	public static void main(String[] args) {
			
		if(args.length == 6) {		
			new Nagios3C(args[0], args[1], args[2], args[3], args[4], args[5]);
			
				
//          new Nagios3C("T16423", "00", "000051", "01", "079", "000030");		
//	    	new Nagios3C("T16423", "00", "000051", "01", "023", "000030");
//	        new Nagios3C("T16423", "00", "000051", "01", "025", "000030");		
//          new Nagios3C("T16423", "00", "000051", "01", "045", "000030");
//     	    new Nagios3C("T16423", "00", "000051", "01", "046", "000030");
//			new Nagios3C("T16423", "00", "000051", "01", "002", "000030");
//		    new Nagios3C("T16423", "00", "000051", "01", "004", "000030");
//			new Nagios3C("T16423", "00", "000051", "01", "026", "000036");
     	         	    
//            new Nagios3C("TEXKQL", "00", "000069", "01", "002", "000024");
		

//		    new Nagios3C("T87332", "00", "000070", "01", "004", "000030");
//			new Nagios3C("T87332", "00", "000070", "01", "023", "000030");
//			new Nagios3C("T87332", "00", "000070", "01", "045", "000030");
//		    new Nagios3C("T87332", "00", "000070", "01", "002", "000030");
//			new Nagios3C("T87332", "00", "000070", "01", "026", "000036");
//		    new Nagios3C("T87332", "00", "000070", "01", "025", "000030");
			
//			new Nagios3C("TEXKQL", "00", "000069", "01", "004", "000024");
			 
		
			
			
		}
		else {
			System.out.println("Este plugin requer 6 argumentos: ARG1: CODIGO DO CLIENTE; ARG2: CODIGO DA EMPRESA; ARG3: CODIGO DO AMBIENTE; ARG4: CODIGO DO TIPO DO AMBIENTE; ARG5: CODIGO DO MONITORAMENTO DESEJADO; ARG6: CODIGO DO PRODUTO.");
			System.exit(3);
		}
			
	}
		
}


