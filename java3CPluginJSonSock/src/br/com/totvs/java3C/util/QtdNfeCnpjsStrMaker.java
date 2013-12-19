package br.com.totvs.java3C.util;

import br.com.totvs.java3C.bean.ParamQtdNfeCnpj;


public class QtdNfeCnpjsStrMaker {
		
	private String qtdNfeCnpjStr;
	
	public QtdNfeCnpjsStrMaker(ParamQtdNfeCnpj[] qtdNfeCnpj) {
					
		if(qtdNfeCnpj.length == 0) {
			this.qtdNfeCnpjStr = "Não foi possivel criar sequencia de qtdNfeCnpjs. Valor de entrada vazio.";
		}
		else {
			this.qtdNfeCnpjStr = "$";
			for(int q = 0; q < qtdNfeCnpj.length; q++) {
				this.qtdNfeCnpjStr += String.valueOf(qtdNfeCnpj[q].getZcgAcumMe()) + "#" + String.valueOf(qtdNfeCnpj[q].getZcgAcumSr()) + "#" + String.valueOf(qtdNfeCnpj[q].getZcgAcumTo()) + "#" + qtdNfeCnpj[q].getZcgAcumCnpj() + "$"; 
			}
			
		}
		
	
	}
	
	public String getQtdNfeCnpjStr() {
		return this.qtdNfeCnpjStr;
	}
	
	

}

