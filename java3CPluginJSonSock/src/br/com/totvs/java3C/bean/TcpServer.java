package br.com.totvs.java3C.bean;

public class TcpServer {
	
	private String ip;
	private int port;
	
	public TcpServer(String ip, int port) {
		this.ip = ip;
		this.port = port;
	}
	
	
	public String getIp() {
		return ip;
	}
	
	public void setIp(String ip) {
		this.ip = ip;
	}
	
	
	public int getPort() {
		return port;
	}
	
	public void setPort(int port) {
		this.port = port;
	}
	

}
