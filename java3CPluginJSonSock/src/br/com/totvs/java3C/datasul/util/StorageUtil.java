package br.com.totvs.java3C.datasul.util;

import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

import br.com.totvs.java3C.util.ConverteMedidasDados;

import com.totvs.cloud.message.items.StorageDirItem;
import com.totvs.cloud.message.params.StorageParamsDirItem;
import com.totvs.cloud.service.RMICloudService;



public class StorageUtil {
	
	private RMICloudService cloudService;
	private double tamanhoTotal = 0.0;
	private String info = new String();

	
	public StorageUtil(String hostMonit, String ... caminhos) {
		
    	List<StorageParamsDirItem> storageList = new ArrayList<StorageParamsDirItem>();
    	StorageParamsDirItem storageParamItem = new StorageParamsDirItem();
    	DecimalFormat df = new DecimalFormat("#.########");

    	for(int i = 0; i < caminhos.length; i++) {
		
			if(!caminhos[i].equals("no_info")) {
				
				caminhos[i] = caminhos[i].substring(0, caminhos[i].length() - (caminhos[i].endsWith("\\") ? 1 : 0)); // Checa se o path termina com uma barra invertida "\". Em caso afirmativo, remove
				caminhos[i] = caminhos[i].substring(0, caminhos[i].length() - (caminhos[i].endsWith("/") ? 1 : 0));  // Checa se o path termina com uma barra "/". Em caso afirmativo, remove
				
				storageParamItem = new StorageParamsDirItem();
				int limit = caminhos[i].lastIndexOf("\\");
	
				if(limit == -1) // Trata-se de servidor linux
					limit = caminhos[i].lastIndexOf("/");
				
				String pasta = caminhos[i].substring(limit + 1, caminhos[i].length());
				storageParamItem.setDirName(pasta.trim());
				storageParamItem.setPathDir(caminhos[i]);
				storageList.add(storageParamItem);
			}
    	}

		
		StringBuilder rmiStrConnection = new StringBuilder();
		
	    try
	    {
	    	rmiStrConnection.append("rmi://");
	    	rmiStrConnection.append(hostMonit);
	    	rmiStrConnection.append(":");
	    	rmiStrConnection.append("1099");
	    	rmiStrConnection.append("/DatasulCloudMonitor");
	      
	    	this.cloudService = ((RMICloudService)Naming.lookup(rmiStrConnection.toString()));
	    	
	    }
	    catch (RemoteException e)
	    {
	    	System.out.println("Nao foi possivel efetuar conexao RMI com o servidor de monitoramento: " + rmiStrConnection.toString());
	    	System.exit(1);
	    }
	    catch (MalformedURLException e1)
	    {
	    	System.out.println("Url INCORRETA para conexao RMI com o servidor de monitoramento: " + rmiStrConnection.toString());
	        System.exit(1);
	    }
	    catch (NotBoundException e1)
	    {
	    	System.out.println("Erro durante tentativa de procura (lookup) ou desvinculo (unbind). Nome nao possui qualquer ligacao associada ao registro (registry): " + rmiStrConnection.toString());
	        System.exit(1);
	    }
	    
		if(storageList.size() == 0) {
			System.out.println("Nenhum item de Storage a ser monitorado.");
			System.exit(1);
		}
		else { 
			
	        try
		    {
	        		
	        	List<StorageDirItem> result = this.cloudService.getStorageDirItems(storageList);
	        	
	        	
	        	for (StorageDirItem item : result)
	        	{
	        		double valorEmGiga = new ConverteMedidasDados(item.getSizeDir()).getValorEmGigaBytes();
	        		
	        		info += item.getPathDir() + ", tamanho (GB) = " + df.format(valorEmGiga) + "; "; 
	        		
			        tamanhoTotal += valorEmGiga; 
	        	}
		    }
		    catch (RemoteException e)
		    {
		    	System.out.println(e.getMessage());
	    		System.exit(1);
		    }
		
		
		}
		
	}
	

	public void setTamanhoTotal(double tamanhoTotal) {
		this.tamanhoTotal = tamanhoTotal;
	}
	public double getTamanhoTotal() {
		return tamanhoTotal;
	}

	public void setInfo(String info) {
		this.info = info;
	}	
	public String getInfo() {
		return info;
	}
	

	
//	public static void main(String[] args) {
//										 // SPOOL				  // Especiais                                     especificos
//		StorageUtil su = new StorageUtil("10.70.39.6", "D:\\temp\\totvs-cloud", "D:\\datasul\\clientes\\totvs-cloud\\especiais", "D:\\datasul\\clientes\\totvs-cloud\\especificos");
//														   // Dir. Banco  // Bkp AILog   // Bkp FULL
//		//StorageUtil su = new StorageUtil("172.16.102.246", "/etc/init.d", "/usr/bin",    "/root/datasul-cloud-monitor/libs");
//		                                                  // D. JBoss Cli.
////		StorageUtil su = new StorageUtil("172.18.107.31", "D:\\datasul\\clientes\\totvs-cloud\\jboss\\jboss-4.2.3.GA\\server\\instance-8080");
//		
//		System.out.println("Info: " + su.getInfo());
//		System.out.println("Tamanho total: " + su.tamanhoTotal);
//	}

}
