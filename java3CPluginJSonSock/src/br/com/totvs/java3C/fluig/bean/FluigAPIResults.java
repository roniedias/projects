package br.com.totvs.java3C.fluig.bean;

public class FluigAPIResults {
	
	private String goodDataAvailability;
	private String memcachedAvailability;
	private String openOfficeServerAvailability;
	private String solrServerAvailability;
	private String identityAvailability;
	private String databaseAvailability;
	private String licenseServerAvailability;
	
	
	public FluigAPIResults(String goodDataAvailability, String memcachedAvailability, String openOfficeServerAvailability, String solrServerAvailability, String identityAvailability, String databaseAvailability, String licenseServerAvailability) {
		this.goodDataAvailability = goodDataAvailability;
		this.memcachedAvailability = memcachedAvailability;
		this.openOfficeServerAvailability = openOfficeServerAvailability;
		this.solrServerAvailability = solrServerAvailability;
		this.identityAvailability = identityAvailability;
		this.databaseAvailability = databaseAvailability;
		this.licenseServerAvailability = licenseServerAvailability;
	}
	
	
	public String getGoodDataAvailability() {
		return goodDataAvailability;
	}
	
	public String getMemcachedAvailability() {
		return memcachedAvailability;
	}
	
	public String getOpenOfficeServerAvailability() {
		return openOfficeServerAvailability;
	}
	
	public String getSolrServerAvailability() {
		return solrServerAvailability;
	}
	
	public String getIdentityAvailability() {
		return identityAvailability;
	}
	
	public String getDatabaseAvailability() {
		return databaseAvailability;
	}
	
	public String getLicenseServerAvailability() {
		return licenseServerAvailability;
	}
	
}
