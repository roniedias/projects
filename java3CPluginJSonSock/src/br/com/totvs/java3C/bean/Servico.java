package br.com.totvs.java3C.bean;

public class Servico {
	
	private String codTipoServico;
	private String sequencia;
	private String environment;
	private String balance; //(S/N)
	private String ipHost;
	private String ipExterno;
	private String dns;
	private String porta;
	private String pathIni;
	private String pathInstal;
	private String dbAlias;
	
	
	public String getCodTipoServico() {
		return codTipoServico;
	}
	public void setCodTipoServico(String codTipoServico) {
		this.codTipoServico = codTipoServico;
	}
	
	public String getSequencia() {
		return sequencia;
	}
	public void setSequencia(String sequencia) {
		this.sequencia = sequencia;
	}
	
	public String getEnvironment() {
		return environment;
	}
	public void setEnvironment(String environment) {
		this.environment = environment;
	}
	
	public String getBalance() {
		return balance;
	}
	public void setBalance(String balance) {
		this.balance = balance;
	}
	
	public String getIpHost() {
		return ipHost;
	}
	public void setIpHost(String ipHost) {
		this.ipHost = ipHost;
	}
	
	public String getIpExterno() {
		return ipExterno;
	}
	public void setIpExterno(String ipExterno) {
		this.ipExterno = ipExterno;
	}
	
	public String getDns() {
		return dns;
	}
	public void setDns(String dns) {
		this.dns = dns;
	}
	
	public String getPorta() {
		return porta;
	}
	public void setPorta(String porta) {
		this.porta = porta;
	}
	
	public String getPathIni() {
		return pathIni;
	}
	public void setPathIni(String pathIni) {
		this.pathIni = pathIni;
	}
	
	public String getPathInstal() {
		return pathInstal;
	}
	public void setPathInstal(String pathInstal) {
		this.pathInstal = pathInstal;
	}
	
	public String getDbAlias() {
		return dbAlias;
	}
	public void setDbAlias(String dbAlias) {
		this.dbAlias = dbAlias;
	}
	
	

}
