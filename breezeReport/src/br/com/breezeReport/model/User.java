package br.com.breezeReport.model;

import br.com.breezeReport.security.SHA2;

public class User {
	
	private Long id;
	private String nome;	
	private String senha;
	private String email;
	
	
	public User(){}
	
	public User(Long id, String nome, String senha, String email) {
		this.id = id;
		this.nome = nome;
		this.senha = senha;
		this.email = email;
	}
	
	
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	
	public String getNome() {
		return nome;
	}
	public void setNome(String nome) {
		this.nome = nome;
	}
	
	public String getSenha() {
		return senha;
	}
	public void setSenha(String senha) {
		this.senha = new SHA2(senha).genEncryptedPassword();
	}
	
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	} 


}
