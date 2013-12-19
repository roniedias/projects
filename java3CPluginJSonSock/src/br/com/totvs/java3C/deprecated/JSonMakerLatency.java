package br.com.totvs.java3C.deprecated;


import java.util.ArrayList;

import br.com.totvs.java3C.bean.ServerLatency;


public class JSonMakerLatency {
	
	public String jSon;
	

	public JSonMakerLatency(ArrayList<ServerLatency> servers) {	
							
		

		if(servers.size() == 0) {
			this.jSon = "Não foi possivel gerar o JSon. Valor de entrada vazio.";			
		}
		else {
			
			

			this.jSon = "[{\"nomeOperacao\":\"LATENCY\",\"SERVERS\":[";
			

			for(int s = 0; s < servers.size(); s++) {			

				jSon += "{\"IP\":\"" + servers.get(s).getIp() + "\",\"PORTA\":\"" + servers.get(s).getPorta() + "\",\"AMBIENTE\":\"" + servers.get(s).getAmbiente() + "\",\"DATA\":\"" + servers.get(s).getData() + "\",\"HORA\":\"" + servers.get(s).getHora() + "\"},";
			}

			int limit = this.jSon.lastIndexOf(",");
			this.jSon = this.jSon.substring(0, limit);
			this.jSon += "]}]";

			
		}
		
	
	}
	
	public String getJSon() {
		return this.jSon;
	}
		
}