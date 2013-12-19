package br.com.totvs.java3C.factory;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.net.Socket;
//import java.util.Random;

import br.com.totvs.java3C.util.RandomTcpSrv;
//import br.com.totvs.java3C.util.TcpConnAudit;

public class TcpConnFactory {
	
	private String tcpSrvReturn = "Sem informacao.";
	private RandomTcpSrv randomTcpSrv;
	private String strIn;
	private int attempts;
	private String nodeName;
	
	private boolean isConnected;
	
	
	public TcpConnFactory(String strIn, String nodeName) {
		this.strIn = strIn;
		this.nodeName = nodeName;
	}
	
	
	public void execute() {
		
		int a = 0;
		Socket s;
		PrintStream ps;
		BufferedReader br;
		String tcpSrvIp = "Nenhum IP atribuido.";
		int tcpSrvPort = 0;
//		Random r = new Random();
//		int conexao = 0;
		
//		TcpConnAudit tcpConnAudit = new TcpConnAudit("C:\\testFiles\\tcpConnAudit.txt");
//		TcpConnAudit tcpConnAudit = new TcpConnAudit("/usr/lib/nagios/plugins/tcpConnAudit.txt");
		
		
//		conexao = r.nextInt(1000);
		
		attempts = new RandomTcpSrv(nodeName).getAttempts();
		
		while(a < attempts) {	
			
			if(isConnected) {
				break;
			}
				
			
			randomTcpSrv = new RandomTcpSrv(nodeName);
			tcpSrvIp = randomTcpSrv.getIp();
			tcpSrvPort = randomTcpSrv.getPorta();
			
			
			try {	
				s = new Socket(tcpSrvIp, tcpSrvPort);
				ps = new PrintStream(s.getOutputStream());
		    	br = new BufferedReader(new InputStreamReader(s.getInputStream()));
		    	ps.print(strIn); 	        	 
	        	tcpSrvReturn = br.readLine();
	        	s.close();
//	        	tcpConnAudit.execute("Conexao #" + conexao + " efetuada com sucesso no TCP Server " + tcpSrvIp + " : " + tcpSrvPort);
//	        	System.out.println("Conexao #" + conexao + " efetuada com sucesso no TCP Server " + tcpSrvIp + " : " + tcpSrvPort);
	        	isConnected = true;
			}
	        catch (IOException e) {
//	        	tcpConnAudit.execute("Erro! Conexao #" + conexao + " nao foi efetuada no TCP Server " + tcpSrvIp + " : " + tcpSrvPort);
//	        	System.out.println("Erro! Conexao #" + conexao + " nao foi efetuada no TCP Server " + tcpSrvIp + " : " + tcpSrvPort);
	        }		
			a++;				
		}
	}
	
	
	
	public String getTcpSrvReturn() {
		return tcpSrvReturn;
	}
	
	public boolean tcpSrvConn() {
		return isConnected; 
	}
		


}
