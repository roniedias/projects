package br.com.totvs.java3C.util;

import java.io.*;
import java.net.*;  


    
    public class Telnet 
    {  
    	
    	private String[] result;
    	
    	public Telnet(String endereco, int porta) {
    		
    		result = new String[2];
    		    		
    		try {
    			
    		Socket sock = new Socket(endereco, porta);
    		InputStream is = sock.getInputStream();
    		DataInputStream dis = new DataInputStream(is);
    		
    		if(dis != null) {
    			this.result[0] = "A";
    			this.result[1] = "Conexao com endereco " + endereco + ", porta " + String.valueOf(porta) + " efetuada com sucesso";
    		}
    		
            dis.close();  
            sock.close();
            
    		}
            catch (UnknownHostException uhe) {
            	this.result[0] = "I";
        		this.result[1] = "Endereco IP/DNS invalido: " + endereco;
            }
            catch (IOException ioe) {
            	this.result[0] = "I";
        		this.result[1] = "Nao foi possivel efetuar conexao com endereco " + endereco + ", porta " + String.valueOf(porta);
            }
    		catch (IllegalArgumentException iae) {
            	this.result[0] = "I";
        		this.result[1] = "Porta invalida: " + String.valueOf(porta);
    		}
        		  
            
    	}
    	

    	public String[] getResult() {	
    		return result;              
        }  
        
    }  