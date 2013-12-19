package br.com.totvs.java3C.bean;

public class LatencyLog {
	
	private String environment;
	private String ip;
	private String message;
	private String port;
	private String status;
	private LogInfo[] logInfo;
	
	
	public 	LatencyLog() {}
	
	
	public LatencyLog(String environment, String ip, String message, String port, String status, LogInfo[] logInfo) {
		
		this.environment = environment;
		this.ip = ip;
		this.message = message;
		this.port = port;
		this.status = status;
		this.logInfo = logInfo;
	}
	

	public String getEnvironment() {
		return environment;
	}

	public void setEnvironment(String environment) {
		this.environment = environment;
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


	
	public LogInfo[] getLogInfo() {
		return logInfo;
	}

	public void setLogInfo(LogInfo[] logInfo) {
		this.logInfo = logInfo;
	}
	

}