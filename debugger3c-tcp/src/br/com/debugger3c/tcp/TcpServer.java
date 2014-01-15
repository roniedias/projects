package br.com.debugger3c.tcp;

import java.io.File;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class TcpServer implements Runnable {
  
	protected int serverPort;
	protected ServerSocket serverSocket = null;
	protected boolean isStopped = false;
	protected Thread runningThread = null;
	private static final String FILE_PATH = "C:\\Users\\ronie.dias\\Desktop\\appletTst.txt";
	protected File file;
  
	public TcpServer(int port) {
		this.serverPort = port;
		this.file = new File(FILE_PATH);
	}
  
	public void run()   {
	  
		synchronized (this) {
			this.runningThread = Thread.currentThread();
		}
	  
		openServerSocket();

		while (!isStopped()) {
		
			Socket clientSocket = null;

			try {
				clientSocket = this.serverSocket.accept();
			}
			catch (IOException e) {
			  
				if (isStopped()) {
					System.out.println("Servidor parado.");
					return;
				}
			  
				throw new RuntimeException("Erro ao receber conexao cliente.", e);
			}
		  	
			new Thread(new TailWork(clientSocket, this.file)).start();	  
		}
	  
		System.out.println("Servidor nao iniciado.");
	}
  
  
	private synchronized boolean isStopped() {
		return this.isStopped;
	}
  
  
	public synchronized void stop() {
		this.isStopped = true;
		try {
			this.serverSocket.close();
		}
		catch (IOException e) {
			throw new RuntimeException("Erro ao tentar parar o servidor", e);
		}
	}
  

	private void openServerSocket() {
		try {
			this.serverSocket = new ServerSocket(this.serverPort);
		}
		catch (IOException e) {
			throw new RuntimeException("Nao foi possivel abrir conexao com a porta especificada.", e);
		}
	}
  
  
	public static void main(String[] args) {
		TcpServer server = new TcpServer(2228);
		new Thread(server).start();
	}
  
  
}
