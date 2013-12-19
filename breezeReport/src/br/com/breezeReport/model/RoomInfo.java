package br.com.breezeReport.model;


public class RoomInfo {
	
	private Long scoId;
	private int hosts;
	private int apresentadores;
	private int convidados;
	private int total;
	private String dataHora;	
	private String horaHost;
	private String horaApresentador;
	private String horaConvidado;

	
	public RoomInfo(){}
	

	public RoomInfo(Long scoId, int hosts, int apresentadores, int convidados, int total, String dataHora, String horaHost, String horaApresentador, String horaConvidado) {
	
		this.scoId = scoId;
		this.hosts = hosts;
		this.apresentadores = apresentadores;
		this.convidados = convidados;
		this.total = total;
		this.dataHora = dataHora;
		this.horaHost = horaHost;
		this.horaApresentador = horaApresentador;
		this.horaConvidado = horaConvidado;
	}
	
	
	
	public Long getScoId() {
		return scoId;
	}


	public void setScoId(Long scoId) {
		this.scoId = scoId;
	}
	
	
	public int getTotal() {
		return total;
	}
	
	public void setTotal(int total) {
		this.total = total;
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
	
	
	public String getDataHora() {
		return dataHora;
	}

	public void setDataHora(String date) {
		this.dataHora = date;
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
