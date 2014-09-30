package br.com.totvs.java3C.plugin;

import br.com.totvs.java3C.fluig.plugin.AvailabilityFluigShared;
import br.com.totvs.java3C.fluig.plugin.StorageFluigShared;
import br.com.totvs.java3C.fluig.plugin.TelnetFluig;
import br.com.totvs.java3C.plugin.bdconn.Tss;
import br.com.totvs.java3C.rm.plugin.AvailabilityRM;
import br.com.totvs.java3C.rm.plugin.DiskSpaceRM;
import br.com.totvs.java3C.datasul.plugin.DispAppServer;
import br.com.totvs.java3C.datasul.plugin.DispDatasul;
import br.com.totvs.java3C.datasul.plugin.DispDatasulDatabases;
import br.com.totvs.java3C.datasul.plugin.DispEAI1;
import br.com.totvs.java3C.datasul.plugin.DispEAI2;
import br.com.totvs.java3C.datasul.plugin.DispEMSDatabases;
import br.com.totvs.java3C.datasul.plugin.DispJBoss;
import br.com.totvs.java3C.datasul.plugin.DispLoginDatasul;
import br.com.totvs.java3C.datasul.plugin.DispLoginEMS;
import br.com.totvs.java3C.datasul.plugin.DispRPWDatasul;
import br.com.totvs.java3C.datasul.plugin.DispRPWEMS;
import br.com.totvs.java3C.datasul.plugin.DispSQL;
import br.com.totvs.java3C.datasul.plugin.Storage;



/**
 * @author Ronie Dias Pinto
 */


public class Nagios3C {
	
	
	public Nagios3C(String codCliente, String codEmpresa, String codAmbiente, String codTipoAmbiente, String codMonitoramento, String codProduto) {
						
		
		if(codMonitoramento.equals("004")) { 
			new AvailabilityMultiThread(codAmbiente, codTipoAmbiente, codMonitoramento, codProduto);
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
		else if(codMonitoramento.equals("030")) {
			new AvailabilityRM(codAmbiente, codTipoAmbiente, codMonitoramento, codProduto);
		}
		else if(codMonitoramento.equals("028")) {
			new DiskSpaceRM(codAmbiente, codTipoAmbiente, codMonitoramento, codProduto);
		}
		else if(codMonitoramento.equals("108")) {
			new DispAppServer(codAmbiente, codTipoAmbiente, codMonitoramento, codProduto);
		}
		else if(codMonitoramento.equals("109")) {
			new DispDatasul(codAmbiente, codTipoAmbiente, codMonitoramento, codProduto);
		}
		else if(codMonitoramento.equals("110")) {
			new DispDatasulDatabases(codAmbiente, codTipoAmbiente, codMonitoramento, codProduto);
		}
		else if(codMonitoramento.equals("111")) {
			new DispLoginDatasul(codAmbiente, codTipoAmbiente, codMonitoramento, codProduto);
		}
		else if(codMonitoramento.equals("112")) {
			new DispRPWDatasul(codAmbiente, codTipoAmbiente, codMonitoramento, codProduto);
		}
		else if(codMonitoramento.equals("113")) {
			new DispEAI1(codAmbiente, codTipoAmbiente, codMonitoramento, codProduto);
		}
		else if(codMonitoramento.equals("114")) {
			new DispEAI2(codAmbiente, codTipoAmbiente, codMonitoramento, codProduto);
		}										
		else if(codMonitoramento.equals("115")) {
			new DispEMSDatabases(codAmbiente, codTipoAmbiente, codMonitoramento, codProduto);
		}
		else if(codMonitoramento.equals("116")) {
			new DispJBoss(codAmbiente, codTipoAmbiente, codMonitoramento, codProduto);
		}
		else if(codMonitoramento.equals("117")) {
			new DispLoginEMS(codAmbiente, codTipoAmbiente, codMonitoramento, codProduto);
		}
		else if(codMonitoramento.equals("118")) {
			new DispRPWEMS(codAmbiente, codTipoAmbiente, codMonitoramento, codProduto);
		}
		else if(codMonitoramento.equals("119")) {
			new Storage(codAmbiente, codTipoAmbiente, codMonitoramento, codProduto);
		}	
		else if(codMonitoramento.equals("120")) {
			new DispSQL(codAmbiente, codTipoAmbiente, codMonitoramento, codProduto);
		}
		else if(codMonitoramento.equals("104")) {
			new TelnetFluig(codAmbiente, codTipoAmbiente, codMonitoramento, codProduto);
		}
		else if(codMonitoramento.equals("00")) {
			new StorageFluigShared(codAmbiente, codTipoAmbiente, codMonitoramento, codProduto);
		}
		else if(codMonitoramento.equals("01")) {
			new AvailabilityFluigShared(codAmbiente, codTipoAmbiente, codMonitoramento, codProduto);
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
//		    new Nagios3C("T01489", "00", "000557", "01", "004", "000030");
//		    new Nagios3C("T87332", "00", "000070", "01", "004", "000030");
			
//			new Nagios3C("TEZFI8", "00", "001773", "01", "104", "000075"); // Disponibilidade Fluig
//		    new Nagios3C("99401", "00", "000039", "02", "030", "000029"); // Disponibilidade RM
//			new Nagios3C("99401", "00", "000039", "02", "028", "000029"); // DiskSpaceRM
		    
//		    new Nagios3C("99061", "00", "000117", "01", "004", "000030"); // TOTVS TDI

//			new Nagios3C("99958", "00", "002251", "02", "108", "000019");
//		    new Nagios3C("99958", "00", "002251", "02", "109", "000019");
//		    new Nagios3C("99958", "00", "002251", "02", "110", "000019");
//			new Nagios3C("99958", "00", "002251", "02", "111", "000019");
//		    new Nagios3C("99958", "00", "002251", "02", "112", "000019");
//		    new Nagios3C("99958", "00", "002251", "02", "113", "000019");
//	        new Nagios3C("99958", "00", "002251", "02", "114", "000019");
//          new Nagios3C("99958", "00", "002251", "02", "115", "000019");
//          new Nagios3C("99958", "00", "002251", "02", "116", "000019");
//        	new Nagios3C("99958", "00", "002251", "02", "117", "000020"); 
//		    new Nagios3C("99958", "00", "002251", "02", "118", "000020"); 
//		    new Nagios3C("99958", "00", "002251", "02", "119", "000019"); 
//			new Nagios3C("99958", "00", "002251", "02", "120", "000019");
		
//			new Nagios3C("TEZFF4", "00", "000128", "01", "01", "000071");
		
		    
		}
		else {
			System.out.println("Este plugin requer 6 argumentos: ARG1: CODIGO DO CLIENTE; ARG2: CODIGO DA EMPRESA; ARG3: CODIGO DO AMBIENTE; ARG4: CODIGO DO TIPO DO AMBIENTE; ARG5: CODIGO DO MONITORAMENTO DESEJADO; ARG6: CODIGO DO PRODUTO.");
			System.exit(3);
		}
			
	}
		
}
