package br.com.breezeReport.security;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class SHA2 {
	
	private String senha;
	private StringBuilder hexString = new StringBuilder();
	private byte messageDigest[];
	
	public SHA2(String senha) {
		this.senha = senha;
	}
	
	public String genEncryptedPassword() {

		try {
			MessageDigest algoritimo = MessageDigest.getInstance("SHA-256");
			
			try {
				messageDigest = algoritimo.digest(senha.getBytes("UTF-8"));
			} 
			catch (UnsupportedEncodingException e) {
				e.printStackTrace();
			}
			
			
			for(byte b : messageDigest) {
				hexString.append(String.format("%02X", 0xFF & b));
			}
			
		} 
		catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		}
		
		return hexString.toString();
	}

}
