package br.com.totvs.java3C.fluig.util;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

/*
 * Retorna o tamanho de um determinado diretório (Linux), a partir de um servidor Linux. Necessário ter o 
 * "sshpass" instalado no servidor de origem.   
 */


public class ServerVolumeDir {
	
	private String size;
	
	public ServerVolumeDir(String usuario, String senha, String dir, String ip) {		
			
		try 
		{ 			
			String str = "sshpass -p " + senha + " ssh " + usuario + "@" + ip + " du -sh " + dir; // Ex. no Windows: // String str = "plink " + usuario + "@" + ip + "-pw " + senha + " du -sh " + dir;
			String[] command = {"/bin/sh", "-c", str};
			Process p = Runtime.getRuntime().exec(command); 
			BufferedReader stdout=new BufferedReader(new InputStreamReader(p.getInputStream())); 
			size = stdout.readLine();
			
			if(size.contains("/")) { // Parseando apenas o valor + unidade. Ex: "2.5G"
			   size = size.substring(0, size.indexOf("/")).trim();			   
			}
			
		} 
		catch(IOException e1) {
			e1.printStackTrace();
		}

	}
	
		
	public String getSize() {
		return this.size;
	}
	

}