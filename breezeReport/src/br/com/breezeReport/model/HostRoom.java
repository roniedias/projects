package br.com.breezeReport.model;

public class HostRoom {
	
	private String nome;
	private String login; 
	private Long roomScoId;
	private String dataHora;
	private String desabilitado;
	
	
	public HostRoom() {}
	
	public HostRoom(String nome, String login, Long roomScoId, String dataHora, String desabilitado) {
		this.nome = nome;
		this.login = login;
		this.roomScoId = roomScoId;
		this.dataHora = dataHora;
		this.desabilitado = desabilitado;
	}


	
	public String getNome() {
		return nome;
	}
	public void setNome(String nome) {
		this.nome = nome;
	}

	public String getLogin() {
		return login;
	}
	public void setLogin(String login) {
		this.login = login;
	}

	public Long getRoomScoId() {
		return roomScoId;
	}
	public void setRoomScoId(Long roomScoId) {
		this.roomScoId = roomScoId;
	}
	
	public String getDataHora() {
		return dataHora;
	}
	public void setDataHora(String dataHora) {
		this.dataHora = dataHora;
	}

	public String getDesabilitado() {
		return desabilitado;
	}
	public void setDesabilitado(String desabilitado) {
		this.desabilitado = desabilitado;
	}
	



}
