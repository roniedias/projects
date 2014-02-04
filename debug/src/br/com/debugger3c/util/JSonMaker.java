package br.com.debugger3c.util;

import java.util.ArrayList;
import java.util.HashSet;


public class JSonMaker {
	
	public String nomeChave;
	public HashSet<String> hashSetValores;
	public ArrayList<String> arrayListValores;
	
	private static final String ERRO_GERACAO_JSON = "{\"erro\":\"Não foi possivel gerar o JSon. Verifique o(s) valor(es) de entrada.\"}"; 
	
	public String json = new String();
	
	
	public JSonMaker(String nomeChave, HashSet<String> hashSetValores) {
		this.hashSetValores = new HashSet<String>();	
		this.nomeChave = nomeChave;
		this.hashSetValores = hashSetValores;					
	}
	
	public JSonMaker(String nomeChave, ArrayList<String> arrayListValores) {
		this.arrayListValores = new ArrayList<String>();			
		this.nomeChave = nomeChave;
		this.arrayListValores = arrayListValores;
	}
	
	
	
	public String getArrayListJSon() {
		
		json = "{";
			
		if(arrayListValores.size() != 0) {
			for(int a = 0; a < arrayListValores.size(); a++)	
				json += "\"" + nomeChave + String.valueOf(a) + "\":\"" + arrayListValores.get(a) + "\"," ;
			
			int limit = json.lastIndexOf(",");
			json = json.substring(0, limit);
			json += "}";

		}
		else {
			json = ERRO_GERACAO_JSON;
		}
		return json;							
	}
	
	
	
	public String getHashSetJSon() {
		
		json = "{";
		
		if(hashSetValores.size() != 0) {
			
			ArrayList<String> tmpArrLst = new ArrayList<String>();
				
			for(String s : hashSetValores) {
				tmpArrLst.add(s);
			}
				
			for(int a = 0; a < tmpArrLst.size(); a++)	
				json += "\"" + nomeChave + String.valueOf(a) + "\":\"" + tmpArrLst.get(a) + "\"," ;
			
			int limit = json.lastIndexOf(",");
			json = json.substring(0, limit);
			json += "}";

		}
		else {
			json = ERRO_GERACAO_JSON;
		}

		return json;
		
		
	}
	
	
	
//	public static void main(String[] args) {
//
//		
//		HashSet<String> hashSetValores = new HashSet<String>();
//
//		hashSetValores.add("primeiroSet");
//		hashSetValores.add("segundoSet");
//		
//		JSonMaker jSonMaker = new JSonMaker("RonaldoSet", hashSetValores);
//		System.out.println(jSonMaker.getHashSetJSon());
//
//		
//		ArrayList<String> arrayListValores = new ArrayList<String>();		
//		
//		arrayListValores.add("primeiro");
//		arrayListValores.add("segundo");
//		
//		
//		JSonMaker jSonMaker1 = new JSonMaker("Ronaldo", arrayListValores);
//		System.out.println(jSonMaker1.getArrayListJSon());
//
//	}
	

	

}
