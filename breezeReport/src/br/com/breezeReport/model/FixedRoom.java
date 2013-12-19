package br.com.breezeReport.model;

public class FixedRoom {
	
	public String scoId;
	public String nome;
	
	public FixedRoom() {}
	
	public FixedRoom(String scoId, String nome) {
		this.scoId = scoId;
		this.nome = nome;
	}
	
	
	public String getScoId() {
		return scoId;
	}
	public void setScoId(String scoId) {
		this.scoId = scoId;
	}

	public String getNome() {
		return nome;
	}
	public void setNome(String nome) {
		this.nome = nome;
	}
	
	

}
