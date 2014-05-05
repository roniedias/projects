package br.com.totvs.java3C.util;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.net.Socket;
import java.net.UnknownHostException;

public class TcpConnection {
	
	private String srvReturn;
	
	public TcpConnection(String ip, int porta, String mensagem) {
		try {			
			Socket s = new Socket(ip, porta);
			PrintStream ps = new PrintStream(s.getOutputStream());
			BufferedReader br = new BufferedReader(new InputStreamReader(s.getInputStream()));
	    	ps.println(mensagem);
	    	
	    	srvReturn = br.readLine();
	    	
        	s.close();
        	br.close();
        	ps.close();
		}
        catch (UnknownHostException uhe) {
        	srvReturn = "Endereco IP/DNS invalido: " + ip;
        }
        catch (IOException ioe) {
        	srvReturn = "Nao foi possivel efetuar conexao com endereco " + ip + ", porta " + String.valueOf(porta);
        }
		catch (IllegalArgumentException iae) {
			srvReturn = "Porta invalida: " + String.valueOf(porta);
		}
		
	}
	
	
	
	public String getSrvReturn() {
		return srvReturn;
	}

	
	
	

}
