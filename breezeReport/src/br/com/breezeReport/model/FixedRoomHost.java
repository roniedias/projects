package br.com.breezeReport.model;

public class FixedRoomHost {
	
	String id;
	String login;
	String primeiroNome;
	String sobrenome;
	String email;
	String ulimaSessao;
	String statusSessao;
	
	
	public FixedRoomHost() {}
	
	public FixedRoomHost(String id, String login, String primeiroNome, String sobrenome, String email, String ulimaSessao, String statusSessao) {
		this.id = id;
		this.login = login;
		this.primeiroNome = primeiroNome;
		this.sobrenome = sobrenome;
		this.email = email;
		this.ulimaSessao = ulimaSessao;
		this.statusSessao = statusSessao;
	}
	
	
	
	public String getId() {
		return id;
	}
	
	public void setId(String id) {
		this.id = id;
	}
	
	
	public String getLogin() {
		return login;
	}
	
	public void setLogin(String login) {
		this.login = login;
	}
	
	
	public String getPrimeiroNome() {
		return primeiroNome;
	}
	
	public void setPrimeiroNome(String primeiroNome) {
		this.primeiroNome = primeiroNome;
	}
	
	
	public String getSobrenome() {
		return sobrenome;
	}
	
	public void setSobrenome(String sobrenome) {
		this.sobrenome = sobrenome;
	}
	
	
	public String getEmail() {
		return email;
	}
	
	public void setEmail(String email) {
		this.email = email;
	}
	
	
	public String getUlimaSessao() {
		return ulimaSessao;
	}
	
	public void setUlimaSessao(String ulimaSessao) {
		this.ulimaSessao = ulimaSessao;
	}
		

	public String getStatusSessao() {
		return statusSessao;
	}

	public void setStatusSessao(String statusSessao) {
		this.statusSessao = statusSessao;
	}
	

}
