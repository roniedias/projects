package br.com.totvs.java3C.bean;

public class ServerLatency {

    private String ip;
    private String porta;
    private String ambiente;
	private String data;
    private String hora;

    
	public ServerLatency() {}
    
    
	public ServerLatency(String ip, String porta, String ambiente, String data, String hora) {

		this.ip = ip;
		this.porta = porta;
		this.ambiente = ambiente;
		this.data = data;
		this.hora = hora;
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


	
	public String getAmbiente() {
		return ambiente;
	}

	public void setAmbiente(String ambiente) {
		this.ambiente = ambiente;
	}
	
	

	public String getData() {
		return data;
	}

	public void setData(String data) {
		this.data = data;
	}



	public String getHora() {
		return hora;
	}

	public void setHora(String hora) {
		this.hora = hora;
	}


	
		
}
