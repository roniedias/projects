package br.com.totvs.java3C.bean;

public class ZbcTopService {

	private String zbcIpHost;
	private String zbcPorta;
	private String zbcEnviro;
	private String zbcBalanc;
	
	public ZbcTopService(String zbcIpHost, String zbcPorta, String zbcEnviro, String zbcBalanc) {
		this.zbcIpHost = zbcIpHost;
		this.zbcPorta = zbcPorta;
		this.zbcEnviro = zbcEnviro;
		this.zbcBalanc = zbcBalanc;
	}
	
	
	public String getZbcIpHost() {
		return zbcIpHost;
	}
	
	public void setZbcIpHost(String zbcIpHost) {
		this.zbcIpHost = zbcIpHost;
	}
	
	
	public String getZbcPorta() {
		return zbcPorta;
	}
	
	public void setZbcPorta(String zbcPorta) {
		this.zbcPorta = zbcPorta;
	}
	
	
	public String getZbcEnviro() {
		return zbcEnviro;
	}
	
	public void setZbcEnviro(String zbcEnviro) {
		this.zbcEnviro = zbcEnviro;
	}
	

	public String getZbcBalanc() {
		return zbcBalanc;
	}

	public void setZbcBalanc(String zbcBalanc) {
		this.zbcBalanc = zbcBalanc;
	}

	

}
