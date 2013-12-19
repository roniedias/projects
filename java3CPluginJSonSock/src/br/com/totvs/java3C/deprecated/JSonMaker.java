package br.com.totvs.java3C.deprecated;

import java.util.HashMap;
import java.util.Map.Entry;


public class JSonMaker {
	
	public String jSon;
	
	public JSonMaker(HashMap<String, String> pairs) {
					
		
		this.jSon = "[{";
		
		if(pairs.size() == 1) {
			for(Entry<String, String> entry : pairs.entrySet()) {
				this.jSon += "\"" + entry.getKey() + "\":\"" + entry.getValue() + "\"}]";
			}
		}			
		
		if(pairs.size() > 1) {

			for(Entry<String, String> entry : pairs.entrySet()) {
				this.jSon += "\"" + entry.getKey() + "\":\"" + entry.getValue() + "\",";
			}
			
			int limit = this.jSon.lastIndexOf(",");
			this.jSon = this.jSon.substring(0, limit);
			this.jSon += " }]";
		}
		
		
		if(pairs.size() == 0) {
			this.jSon = "Não foi possivel gerar o JSon. Valor de entrada (HashMap) vazio.";
		}
		
	
	}
	
	public String getJSon() {
		//System.out.println(jSon);
		return this.jSon;
	
	}
		
}