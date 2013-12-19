package br.com.totvs.java3C.bean;

public class ParamServicosMonit {

    private String zccObs;
    private String zccSeqSrv;
    private String zccStatus;
    private String zccTipSrv;
    
    
    public ParamServicosMonit() {}
    
    
    public ParamServicosMonit(String zccObs, String zccSeqSrv, String zccStatus, String zccTipSrv) {
    	this.zccObs = zccObs;
    	this.zccSeqSrv = zccSeqSrv;
    	this.zccStatus = zccStatus;
    	this.zccTipSrv = zccTipSrv;
    }
    
    
    
	public String getZccObs() {
		return zccObs;
	}
	
	public void setZccObs(String zccObs) {
		this.zccObs = zccObs;
	}
	
	
	public String getZccSeqSrv() {
		return zccSeqSrv;
	}
	
	public void setZccSeqSrv(String zccSeqSrv) {
		this.zccSeqSrv = zccSeqSrv;
	}
	
	
	public String getZccStatus() {
		return zccStatus;
	}
	
	public void setZccStatus(String zccStatus) {
		this.zccStatus = zccStatus;
	}
	
	
	public String getZccTipSrv() {
		return zccTipSrv;
	}
	
	public void setZccTipSrv(String zccTipSrv) {
		this.zccTipSrv = zccTipSrv;
	}
    
   
	
}
