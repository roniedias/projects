package br.com.totvs.java3C.rm.bean;

public class Server {
	
	private String ip;
	private String message;
	private String name;
	private String port;
	private String status;

	// Para gerar automaticamente este construtor: Alt+Shift+S e selecionar a opção "Generate Constructor Using Fields"  
	public Server(String ip, String message, String name, String port, String status) {
		this.ip = ip;
		this.message = message;
		this.name = name;
		this.port = port;
		this.status = status;
	}
	
	
	public String getIp() {
		return ip;
	}
	public void setIp(String ip) {
		this.ip = ip;
	}
	
	public String getMessage() {
		return message;
	}
	public void setMessage(String message) {
		this.message = message;
	}
	
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	
	public String getPort() {
		return port;
	}
	public void setPort(String port) {
		this.port = port;
	}
	
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	

}
