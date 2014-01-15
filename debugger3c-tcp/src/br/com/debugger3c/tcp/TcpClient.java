package br.com.debugger3c.tcp;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.UnknownHostException;

public class TcpClient implements Runnable {
	
	public void run() {
		
		Socket clientSocket = null;
		InputStreamReader is = null;
		BufferedReader bufferedreader = null;
		PrintWriter os = null;
    
		try {
			
			clientSocket = new Socket("172.16.101.72", 2228);
			is = new InputStreamReader(clientSocket.getInputStream());
			bufferedreader = new BufferedReader(is);
			os = new PrintWriter(clientSocket.getOutputStream(), true);
		}
		catch (UnknownHostException e) {
			System.err.println("Host desconhecido!");
		}
		catch (IOException e) {
			System.err.println("Nao foi possivel efetuar conexao (I/O) com o host.");
		}
		if ((clientSocket != null) && (os != null) && (is != null)) {

			while(true) {
				
				try {
					os.println("sentRequest");          
					String responseLine = bufferedreader.readLine();
					
					if (!responseLine.equals("nao_modificado")) {
						System.out.println(responseLine);
					}
				}
				catch (UnknownHostException e) {
					System.err.println("Tentando se conectar a um host desconhecido: " + e);
				}
				catch (IOException e)
				{
					System.err.println("IOException: " + e);
				}
				
				try {
					Thread.sleep(500);
				}
				catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
		}
	}
  

	public static void main(String[] args) {
		new Thread(new TcpClient()).start();
	}
	
	
}
