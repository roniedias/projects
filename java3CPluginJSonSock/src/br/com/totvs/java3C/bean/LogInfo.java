package br.com.totvs.java3C.bean;

public class LogInfo {
	
	private String activityTime;
	private String avg;
	private String environment;
	private String logDate;
	private String logHour;
	private String lost;
	private String max;
	private String min;
	private String pingDate;
	private String pingHour;
	private String remoteIp;
	private String remoteMachine;
	private String remoteUser;
	private String serverThread;
	
	
	public LogInfo(){}
	
	
	public LogInfo(String activityTime, String avg, String environment, String logDate, String logHour, String lost, String max, String min, String pingDate, String pingHour, String remoteIp, String remoteMachine, String remoteUser, String serverThread) {
		
		this.activityTime = activityTime;
		this.avg = avg;
		this.environment = environment;
		this.logDate = logDate;
		this.logHour = logHour;
		this.lost = lost;
		this.max = max;
		this.min = min;
		this.pingDate = pingDate;
		this.pingHour = pingHour;
		this.remoteIp = remoteIp;
		this.remoteMachine = remoteMachine;
		this.remoteUser = remoteUser;
		this.serverThread = serverThread;		
		
	}


	public String getActivityTime() {
		return activityTime;
	}

	public void setActivityTime(String activityTime) {
		this.activityTime = activityTime;
	}


	
	public String getAvg() {
		return avg;
	}

	public void setAvg(String avg) {
		this.avg = avg;
	}

	

	public String getEnvironment() {
		return environment;
	}

	public void setEnvironment(String environment) {
		this.environment = environment;
	}


	
	public String getLogDate() {
		return logDate;
	}

	public void setLogDate(String logDate) {
		this.logDate = logDate;
	}


	
	public String getLogHour() {
		return logHour;
	}

	public void setLogHour(String logHour) {
		this.logHour = logHour;
	}


	
	public String getLost() {
		return lost;
	}

	public void setLost(String lost) {
		this.lost = lost;
	}


	
	public String getMax() {
		return max;
	}

	public void setMax(String max) {
		this.max = max;
	}


	
	public String getMin() {
		return min;
	}

	public void setMin(String min) {
		this.min = min;
	}


	
	public String getPingDate() {
		return pingDate;
	}

	public void setPingDate(String pingDate) {
		this.pingDate = pingDate;
	}


	
	public String getPingHour() {
		return pingHour;
	}

	public void setPingHour(String pingHour) {
		this.pingHour = pingHour;
	}


	
	public String getRemoteIp() {
		return remoteIp;
	}

	public void setRemoteIp(String remoteIp) {
		this.remoteIp = remoteIp;
	}


	
	public String getRemoteMachine() {
		return remoteMachine;
	}

	public void setRemoteMachine(String remoteMachine) {
		this.remoteMachine = remoteMachine;
	}


	
	public String getRemoteUser() {
		return remoteUser;
	}

	public void setRemoteUser(String remoteUser) {
		this.remoteUser = remoteUser;
	}


	
	public String getServerThread() {
		return serverThread;
	}

	public void setServerThread(String serverThread) {
		this.serverThread = serverThread;
	}
	

}
