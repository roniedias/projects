package br.com.totvs.java3C.bean;

public class ItemAmbiente {
	
	private String coditem;
	private String codProduto;
	private String nomeProduto;
	private String versaoProduto;
	private String empresaFilial;
	private String ambientePai;
	private String status;
	private String usuario;
	private String senha;
	
	
	public String getCodItem() {
		return coditem;
	}
	public void setCodItem(String item) {
		this.coditem = item;
	}
	
	public String getCodProduto() {
		return codProduto;
	}
	public void setCodProduto(String codProduto) {
		this.codProduto = codProduto;
	}
	
	public String getNomeProduto() {
		return nomeProduto;
	}
	public void setNomeProduto(String nomeProduto) {
		this.nomeProduto = nomeProduto;
	}
	
	public String getVersaoProduto() {
		return versaoProduto;
	}
	public void setVersaoProduto(String versaoProduto) {
		this.versaoProduto = versaoProduto;
	}
	
	public String getEmpresaFilial() {
		return empresaFilial;
	}
	public void setEmpresaFilial(String empresaFilial) {
		this.empresaFilial = empresaFilial;
	}
	
	public String getAmbientePai() {
		return ambientePai;
	}
	public void setAmbientePai(String ambientePai) {
		this.ambientePai = ambientePai;
	}
	
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	
	public String getUsuario() {
		return usuario;
	}
	public void setUsuario(String usuario) {
		this.usuario = usuario;
	}
	
	public String getSenha() {
		return senha;
	}
	public void setSenha(String senha) {
		this.senha = senha;
	}
	

}
