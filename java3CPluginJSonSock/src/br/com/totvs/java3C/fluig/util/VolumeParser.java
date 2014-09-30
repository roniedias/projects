package br.com.totvs.java3C.fluig.util;

public class VolumeParser {
	
	public String getMeasure(String val) {
		
		String measure;
		
		if(val.contains("G") || val.contains("GB")) { 
			measure = "Gigabyte";
		}
		else if (val.contains("M") || val.contains("MB")) {
			measure = "Megabyte";
		}
		else if (val.contains("K") || val.contains("KB")) {
			measure = "Kilobyte";
		}
		else {
			measure = "Unidade invalida";
		}
		
		return measure;
	}
	
	public String getValue(String val) {
		
		String value;

		if(val.contains("G") || val.contains("GB")) { 
			value = val.substring(0, val.indexOf("G")).trim();
		}
		else if (val.contains("M") || val.contains("MB")) {
			value = val.substring(0, val.indexOf("M")).trim();
		}
		else if (val.contains("K") || val.contains("KB")) {
			value = val.substring(0, val.indexOf("K")).trim();
		}
		else {
			value = "Valor invalido";
		}
		
		return value;

	}
	
	
	
}
