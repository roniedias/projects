package br.com.totvs.java3C.test;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.net.Socket;

public class TcpProtheusConnTest {

	private String strOut;
	
    private Socket s = null;  
    private PrintStream ps = null;
    private BufferedReader in = null;
    
//    private static int count;
    
    
	public TcpProtheusConnTest(String ip, int porta) {		
	
		try {
			s = new Socket(ip, porta);
			ps = new PrintStream(s.getOutputStream());
	    	in = new BufferedReader(new InputStreamReader(s.getInputStream())); 
	    		
        	//ps.println("ACTIVECONNECTIONS#10.10.3.38#10301#KPMG_PRODUCAO#\0");
	    	//ps.println("AVAILABILITY#10.10.3.38#10317#KPMG_PRODUCAO#01/01#1#\0");
	    	//ps.println("TSSINFO#10.10.1.53#7919#SPED#MSSQL#04919351000151#\0");
	    	//ps.print("LATENCY#4$10.10.3.38#10301#KPMG_PRODUCAO#04/12/2013#15:19:00$10.10.3.38#10302#KPMG_PRODUCAO#04/12/2013#15:19:00$10.10.3.38#10303#KPMG_PRODUCAO#04/12/2013#15:19:00$10.10.3.38#10304#KPMG_PRODUCAO#04/12/2013#15:19:00$#\0");
	    	//ps.print("DISKSPACE#10.10.3.38#10317#KPMG_PRODUCAO#D:\\OUTSOURCING\\CLIENTES\\KPMG_PRODUCAO\\#\0");
	    	ps.print("LICENSEINFO#10.10.3.38#3007#ENVIRONMENT#\\\\10.10.3.38\\D$\\\\OUTSOURCING\\\\TOTVSLICENSE\\\\BIN#\0");
	    	
	    	                     
        	strOut = in.readLine();
	    	

////        	
////        	while((line = in.readLine()) != null) {
////        		System.out.println(line);
////        	}
//        	
//        	
//        	int lineNumber = 0;
//            while ((line = in.readLine()) != null) {
//                System.out.printf("%04d: %s%n", ++lineNumber, line);
//            }
//        	
        	
        	System.out.println(strOut);
        	//System.out.println("Conexão " + count++ + ": " + strOut);
        	
        	s.close();
		}
        catch (IOException e) {
            e.printStackTrace();
        	System.out.println("Nao foi possível conectar-se ao servidor TCP via socket: " + ip + " : " + porta);
        	System.exit(3);
        }

	}
	
	
	public static void main(String[] args) {
		
		new TcpProtheusConnTest("172.18.0.149", 9199);
//		new TcpProtheusConnTest("172.16.84.164", 9110);
		
		
		
//		new Thread(new Runnable() {
//		
//			public void run() {
//				
//				for(int i = 0; i < 10; i++) {
//					new TcpProtheusConnTest("172.18.0.149", 9199);
//				}				
//			}
//			
//		}).start();

		
	}
	
	
	
}
