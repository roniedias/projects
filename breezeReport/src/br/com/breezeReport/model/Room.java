package br.com.breezeReport.model;

public class Room {
	
	private Long scoId;
	private String nome;	
	private String desabilitada;
	
	
	public Room(){}
	
	public Room(Long scoId, String nome, String desabilitada) {
		this.scoId = scoId;
		this.nome = nome;
		this.desabilitada = desabilitada;
		
	}
	

	public Long getScoId() {
		return scoId;
	}
	
	public void setScoId(Long scoId) {
		this.scoId = scoId;
	}
	
	
	public String getNome() {
		return nome;
	}

	public void setNome(String nome) {
		this.nome = nome;
	}


	public String getDesabilitada() {
		return desabilitada;
	}

	public void setDesabilitada(String desabilitada) {
		this.desabilitada = desabilitada;
	}


	
}
