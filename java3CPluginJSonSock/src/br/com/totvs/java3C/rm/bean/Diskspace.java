package br.com.totvs.java3C.rm.bean;

public class Diskspace {
	
    public String databaseSize;
    public String elapTime;
    public String filesSize;
    public String message;
    public String status;
    
    
        
	public Diskspace(String databaseSize, String elapTime, String filesSize, String message, String status) {
		this.databaseSize = databaseSize;
		this.elapTime = elapTime;
		this.filesSize = filesSize;
		this.message = message;
		this.status = status;
	}
	
	
	public String getDatabaseSize() {
		return databaseSize;
	}
	public void setDatabaseSize(String databaseSize) {
		this.databaseSize = databaseSize;
	}
	
	public String getElapTime() {
		return elapTime;
	}
	public void setElapTime(String elapTime) {
		this.elapTime = elapTime;
	}
	
	public String getFilesSize() {
		return filesSize;
	}
	public void setFilesSize(String filesSize) {
		this.filesSize = filesSize;
	}
	
	public String getMessage() {
		return message;
	}
	public void setMessage(String message) {
		this.message = message;
	}
	
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
    

}
