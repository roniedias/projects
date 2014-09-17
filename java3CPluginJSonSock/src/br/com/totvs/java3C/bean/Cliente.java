package br.com.totvs.java3C.bean;

public class Cliente {
	
	public String codigo;
	public String nome;
	public String loja;
	public String portaMonit; // Adicionado em 17/09/2014

	public String getCodigo() {
		return codigo;
	}
	public void setCodigo(String codigo) {
		this.codigo = codigo;
	}
	
	public String getNome() {
		return nome;
	}
	public void setNome(String nome) {
		this.nome = nome;
	}
	
	public String getLoja() {
		return loja;
	}
	public void setLoja(String loja) {
		this.loja = loja;
	}
	
	public String getPortaMonit() {
		return portaMonit;
	}
	public void setPortaMonit(String portaMonit) {
		this.portaMonit = portaMonit;
	}

		

}
