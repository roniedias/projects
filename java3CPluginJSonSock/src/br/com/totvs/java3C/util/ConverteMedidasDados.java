package br.com.totvs.java3C.util;

public class ConverteMedidasDados {
	

	 double valorEmMegaBytes;
	 double valorEmGigaBytes;
	
	public ConverteMedidasDados(String str) {
		
		int espaco = str.indexOf(" ");
		String unidade = str.substring(espaco + 1, str.length()); // Unidade: TB, GB, MB, KB ou bytes
		double valor = Double.valueOf(str.substring(0, espaco));
		
		if(unidade.trim().equals("TB")) {
			valorEmMegaBytes = valor * 1048576;
			valorEmGigaBytes = valor * 1024;
		}
		else if(unidade.trim().equals("GB")) {
			valorEmMegaBytes = valor * 1024; 
			valorEmGigaBytes = valor;
		}
		else if(unidade.trim().equals("MB")) {
			valorEmMegaBytes = valor;
			valorEmGigaBytes = valor / 1024;
		}
		else if(unidade.trim().equals("KB")) {
			valorEmMegaBytes = valor / 1024;
			valorEmGigaBytes = valor / 1048576;
		}
		else { // bytes
			valorEmMegaBytes = valor / 1048576;
			valorEmGigaBytes = valor / 1073741824;
		}
		
	}
	
	public double getValorEmMegaBytes() {
		return valorEmMegaBytes;
	}
	
	public double getValorEmGigaBytes() {
		return valorEmGigaBytes;
	}

	
}
