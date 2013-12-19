package br.com.breezeReport.model;

public class OnlineInfo {
	
	private Long scoId;
	private String nome;	
	private String url;
	private int hosts;
	private int apresentadores;
	private int convidados;
	private int total;
	private String dataHora;	
	private String horaHost;
	private String horaApresentador;
	private String horaConvidado;

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
	
	public String getUrl() {
		return url;
	}
	public void setUrl(String url) {
		this.url = url;
	}
	
	public int getHosts() {
		return hosts;
	}
	public void setHosts(int hosts) {
		this.hosts = hosts;
	}
	
	public int getApresentadores() {
		return apresentadores;
	}
	public void setApresentadores(int apresentadores) {
		this.apresentadores = apresentadores;
	}
	
	public int getConvidados() {
		return convidados;
	}
	public void setConvidados(int convidados) {
		this.convidados = convidados;
	}
	
	public int getTotal() {
		return total;
	}
	public void setTotal(int total) {
		this.total = total;
	}
	
	public String getDataHora() {
		return dataHora;
	}
	public void setDataHora(String dataHora) {
		this.dataHora = dataHora;
	}
	
	public String getHoraHost() {
		return horaHost;
	}
	public void setHoraHost(String horaHost) {
		this.horaHost = horaHost;
	}
	
	public String getHoraApresentador() {
		return horaApresentador;
	}
	public void setHoraApresentador(String horaApresentador) {
		this.horaApresentador = horaApresentador;
	}
	
	public String getHoraConvidado() {
		return horaConvidado;
	}
	public void setHoraConvidado(String horaConvidado) {
		this.horaConvidado = horaConvidado;
	}
	

}
