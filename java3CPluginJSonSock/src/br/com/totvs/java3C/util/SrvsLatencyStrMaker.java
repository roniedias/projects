package br.com.totvs.java3C.util;

import java.util.ArrayList;
import br.com.totvs.java3C.bean.ServerLatency;


public class SrvsLatencyStrMaker {
		
	private String srvsLatency;
	
	public SrvsLatencyStrMaker(ArrayList<ServerLatency> servers) {
					
		if(servers.size() == 0) {
			this.srvsLatency = "Não foi possivel criar sequencia de servidores. Valor de entrada vazio.";
		}
		else {
			this.srvsLatency = "$";
			for(ServerLatency s : servers) {
				this.srvsLatency += s.getIp() + "#" + s.getPorta() + "#" + s.getAmbiente() + "#" + s.getData() + "#" + s.getHora() + "$"; 
			}
			
		}
		
	
	}
	
	public String getSrvsLatency() {
		return this.srvsLatency;
	}
	
	

}
