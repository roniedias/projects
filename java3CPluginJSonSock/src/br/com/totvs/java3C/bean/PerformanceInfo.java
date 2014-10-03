package br.com.totvs.java3C.bean;

public class PerformanceInfo {
	
	private String ip;
	private String porta;
	private String environment;
	private String tipoServico;
	private String tempo;
	
	
	public PerformanceInfo(String ip, String porta, String environment, String tipoServico, String tempo) {
		this.ip = ip;
		this.porta = porta;
		this.environment = environment;
		this.tipoServico = tipoServico;
		this.tempo = tempo;
	}

	
	
	public String getIp() {
		return ip;
	}
	public void setIp(String ip) {
		this.ip = ip;
	}
	
	public String getPorta() {
		return porta;
	}
	public void setPorta(String porta) {
		this.porta = porta;
	}
	
	public String getEnvironment() {
		return environment;
	}
	public void setEnvironment(String environment) {
		this.environment = environment;
	}
	
	public String getTipoServico() {
		return tipoServico;
	}
	public void setTipoServico(String tipoServico) {
		this.tipoServico = tipoServico;
	}
	
	public String getTempo() {
		return tempo;
	}
	public void setTempo(String tempo) {
		this.tempo = tempo;
	}
	

}