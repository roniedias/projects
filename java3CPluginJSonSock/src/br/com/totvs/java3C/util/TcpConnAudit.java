package br.com.totvs.java3C.util;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;

public class TcpConnAudit {
	
	private BufferedWriter out;
	private String fileName;
	
	
	public TcpConnAudit(String fileName) {
		this.fileName = fileName;	
	}
	
	
	public boolean execute(String message) {
		
		boolean fileUpdated;
		
		try {
			out = new BufferedWriter(new FileWriter(fileName, true));
		    out.newLine();
		    out.write(message);
		    fileUpdated = true;
		    out.close();
		} 
		catch (IOException e) 
		{
			fileUpdated = false;
		}
		return fileUpdated;
	}
	

}
