package br.com.debugger3c.tcp;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.io.RandomAccessFile;
import java.net.Socket;

public class TailWork implements Runnable {

	protected Socket clientSocket = null;
	protected File file = null;
	protected long lastPos;
	protected Thread runningThread = null;
	protected RandomAccessFile raf = null;
  
	public TailWork(Socket clientSocket, File file) {
	    this.clientSocket = clientSocket;
	    this.file = file;
	    this.lastPos = this.file.length();
	}
  
    public void run() {
    	synchronized (this) {
    		this.runningThread = Thread.currentThread();
    	}
    
    	try {
    		InputStreamReader input = new InputStreamReader(this.clientSocket.getInputStream());
    		BufferedReader bufferedreader = new BufferedReader(input);
    		PrintWriter output = new PrintWriter(this.clientSocket.getOutputStream(), true);
      
    		while (bufferedreader.readLine() != null) {
    			if (this.file.length() > this.lastPos) {
    				output.println(tailFile(this.file, this.lastPos));
    				this.lastPos = this.file.length();
    			}
    			else if (this.file.length() < this.lastPos) {
    				output.println("*** Arquivo de log truncado *** \n");
    				this.lastPos = this.file.length();
    			}
    			else
    			{
    				output.println("nao_modificado");
    			}
    		}
    	}
    	catch (IOException e) {
    		System.out.println("Thread " + Thread.currentThread().getId() + " finalizada.");
    	}
    }
  
    public synchronized String tailFile(File file, long readFrom) {
    
    	String line = new String();
    	String str = new String();
    
    	try {
    		this.raf = new RandomAccessFile(file, "r");
    		this.raf.seek(readFrom);

    		while ((line = this.raf.readLine()) != null) {
    			str = str + line + "\n";
    		}
    		this.raf.close();
    	}
    	catch (FileNotFoundException f) {
    		f.printStackTrace();
    	}
    	catch (IOException i) {
    		i.printStackTrace();
    	}
  
    	return str;
  }
    
    
}
