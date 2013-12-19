package br.com.totvs.java3C.util;

import br.com.totvs.java3C.bean.ParamLatenciaMonit;


public class LatenciaMonitsStrMaker {
	
	
	
	private String latenciaMonitsStr;
	
	public LatenciaMonitsStrMaker(ParamLatenciaMonit[] paramLatenciaMonit) {
					
		if(paramLatenciaMonit.length == 0) {
			this.latenciaMonitsStr = "Nao foi possivel criar sequencia de latenciaMonits. Valor de entrada vazio.";
		}
		else {
			this.latenciaMonitsStr = "$";
			for(int p = 0; p < paramLatenciaMonit.length; p++) {
				this.latenciaMonitsStr += paramLatenciaMonit[p].getZcfAmbien() + "#" + String.valueOf(paramLatenciaMonit[p].getZcfAvg()) + "#" + paramLatenciaMonit[p].getZcfData() + "#" + paramLatenciaMonit[p].getZcfDtLog() + "#" + paramLatenciaMonit[p].getZcfHora() + "#" + paramLatenciaMonit[p].getZcfHrLog() + "#" + paramLatenciaMonit[p].getZcfIp() + "#" + String.valueOf(paramLatenciaMonit[p].getZcfLost()) + "#" + String.valueOf(paramLatenciaMonit[p].getZcfMax()) + "#" + String.valueOf(paramLatenciaMonit[p].getZcfMin()) + "#" + paramLatenciaMonit[p].getZcfNome() + "#" + paramLatenciaMonit[p].getZcfTempo() + "#" + paramLatenciaMonit[p].getZcfThread() + "#" + paramLatenciaMonit[p].getZcfUser() + "$"; 
			}
			
		}
		
	
	}
	
	public String getLatenciaMonitsStr() {
		return this.latenciaMonitsStr;
	}


}








