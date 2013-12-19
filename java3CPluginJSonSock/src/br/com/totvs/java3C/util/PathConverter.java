package br.com.totvs.java3C.util;

import java.util.regex.Matcher;


/**
 * @author Ronie Dias Pinto
 */


// Altera o path absoluto para um path do tipo "compartilhado". 
// Ex: Path original:  C:\OUTSOURCING\CLIENTES\PROJETO3C_PROD\
//	   Path novo    :  \\172.16.90.201\C$\OUTSOURCING\CLIENTES\PROJETO3C_PROD\




public class PathConverter {
			
	private String path;
	private String ip;
	
	
	public PathConverter(){}
			
	public PathConverter(String path, String ip) {
		this.path = path;
		this.ip = ip;
	}
	
	public PathConverter(String path) {
		this.path = path;
	}
			

	public String convertToRelative() {
		return "\\" + "\\" + this.ip + "\\" + this.path.replace(":", "$");
	}
	
	public String convertToAbsolute() {
		int index = path.indexOf("$");
		String subStr1 = path.substring(0, index - 1);
		String subStr2 = path.replace(subStr1, "");
		return subStr2.replace("$", ":");		
	}
	
	
	public String duplicateBackSlash(String path) {
		return path.replaceAll("\\\\", Matcher.quoteReplacement("\\\\")); //Duplica a quantidade de barras invertidas (Uma barra é substituída por duas. Duas barras são substituídas por quatro e assim sucessivamente) 
	}																			  // Se quisesse quadruplicar, substitur-se-ia apenas o trecho "Matcher.quoteReplacement("\\\\")" por "Matcher.quoteReplacement("\\\\\\\\")"
	
	public String divideBackSlash(String str) {
		//return str.replaceAll("\\\\", Matcher.quoteReplacement("\\"));
		return str.replace("\\\\\\\\","\\\\");
	}

}
