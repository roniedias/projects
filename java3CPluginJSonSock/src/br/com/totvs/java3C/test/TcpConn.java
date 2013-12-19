package br.com.totvs.java3C.test;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.net.Socket;



public class TcpConn {
	
	public TcpConn(String ip, String port, String str) {
	
		try {	
			Socket s = new Socket(ip, Integer.valueOf(port));
			PrintStream ps = new PrintStream(s.getOutputStream());
	    	BufferedReader br = new BufferedReader(new InputStreamReader(s.getInputStream()));
	    	ps.print(str + "\0"); 	        	 
        	String tcpSrvReturn = br.readLine();
        	System.out.println(tcpSrvReturn);
        	s.close();
        	System.exit(0);
		}
        catch (IOException e) {
        	System.out.println("Não foi possivel conectar-se via socket. IP: " + ip + " PORTA: " + port);
        	System.exit(0);
        }		

	}
	
	
	public static void main(String[] args) {
		if(args.length == 3) 		
			new TcpConn(args[0], args[1], args[2]);
		else {
			System.out.println("Este arquivo requer 3 argumentos: ARG1: IP; ARG2: PORTA; ARG3: STRING A SER ENVIADA PARA O SERVIDOR.");
		}
		
	}
	
	
	
	

}
