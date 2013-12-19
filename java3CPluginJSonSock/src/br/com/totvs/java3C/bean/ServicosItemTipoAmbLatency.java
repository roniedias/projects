package br.com.totvs.java3C.bean;

public class ServicosItemTipoAmbLatency {
	
    private String zbdDtMoni;				
    private String zbdHrMoni;				
    private String zbcEnviro;
    private String zbcIpHost;
    private String zbcPorta;
    private String zbcItem;
	private String zbcBalanc;
    
    
    public ServicosItemTipoAmbLatency(String zbdDtMoni, String zbdHrMoni, String zbcEnviro, String zbcIpHost, String zbcPorta, String zbcItem, 	String zbcBalanc) {
   
        this.zbdDtMoni = zbdDtMoni;				
        this.zbdHrMoni = zbdHrMoni;				
        this.zbcEnviro = zbcEnviro;
        this.zbcIpHost = zbcIpHost;
        this.zbcPorta = zbcPorta;
        this.zbcItem = zbcItem;
    	this.zbcBalanc = zbcBalanc;

    }
    
    
    

	public String getZbdDtMoni() {
		return zbdDtMoni;
	}
	
	public void setZbdDtMoni(String zbdDtMoni) {
		this.zbdDtMoni = zbdDtMoni;
	}
	
	
	public String getZbdHrMoni() {
		return zbdHrMoni;
	}
	public void setZbdHrMoni(String zbdHrMoni) {
		this.zbdHrMoni = zbdHrMoni;
	}
	
	
	public String getZbcEnviro() {
		return zbcEnviro;
	}
	
	public void setZbcEnviro(String zbcEnviro) {
		this.zbcEnviro = zbcEnviro;
	}
	
	
	public String getZbcIpHost() {
		return zbcIpHost;
	}
	
	public void setZbcIpHost(String zbcIpHost) {
		this.zbcIpHost = zbcIpHost;
	}
	
	
	public String getZbcPorta() {
		return zbcPorta;
	}
	
	public void setZbcPorta(String zbcPorta) {
		this.zbcPorta = zbcPorta;
	}
	
	
	public String getZbcItem() {
		return zbcItem;
	}
	
	public void setZbcItem(String zbcItem) {
		this.zbcItem = zbcItem;
	}
	
	
	public String getZbcBalanc() {
		return zbcBalanc;
	}

	public void setZbcBalanc(String zbcBalanc) {
		this.zbcBalanc = zbcBalanc;
	}

    
}
