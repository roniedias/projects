package br.com.totvs.java3C.util;


import br.com.totvs.java3C.bean.ParamServicosMonit;

public class SrvsAvailStrMaker {
	
	private String srvsAvailStr;
	
	
	
	public SrvsAvailStrMaker(ParamServicosMonit[] paramServicosMonit) {
		
		if(paramServicosMonit.length == 0) {
			this.srvsAvailStr = "Nao foi possivel criar sequencia de latenciaMonits. Valor de entrada vazio.";
		}
		else {
			
			this.srvsAvailStr = "$";
			
			for(int s = 0; s < paramServicosMonit.length; s++) {
				this.srvsAvailStr += paramServicosMonit[s].getZccObs() + "#" + paramServicosMonit[s].getZccSeqSrv() + "#" + paramServicosMonit[s].getZccStatus() + "#" + paramServicosMonit[s].getZccTipSrv() + "$"; 
			}
						
		}

	}
	
	
	public String getSrvsAvailStr() {
		return srvsAvailStr;
	}


}








