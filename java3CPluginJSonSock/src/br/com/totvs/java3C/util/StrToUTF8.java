package br.com.totvs.java3C.util;

import java.io.UnsupportedEncodingException;

public class StrToUTF8 {
	
	private String strOut;
	
	public String convert(String strIn) {
		
		byte ptext[];
		
		try {
			
			ptext = strIn.getBytes("ISO-8859-1");
			strOut = new String(ptext, "UTF-8");
			
		} catch (UnsupportedEncodingException e) {
			strOut = e.toString();
		}
		
		return strOut; 

		
	}

}
