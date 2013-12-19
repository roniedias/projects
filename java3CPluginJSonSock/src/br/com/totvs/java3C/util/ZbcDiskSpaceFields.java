package br.com.totvs.java3C.util;

/**
 * @author Ronie Dias Pinto
 */


public class ZbcDiskSpaceFields {
	
	private String ZBC_IPHOST;
	private String ZBC_PORTA;
	private String ZBC_ENVIRO;
	private String ZBC_INST;

	
	public ZbcDiskSpaceFields(String ZBC_IPHOST, String ZBC_PORTA, String ZBC_ENVIRO, String ZBC_INST) {
		this.ZBC_IPHOST = ZBC_IPHOST;
		this.ZBC_PORTA = ZBC_PORTA;
		this.ZBC_ENVIRO = ZBC_ENVIRO;
		this.ZBC_INST = ZBC_INST;
	}
	
	
	//getters	
	public String getZBC_IPHOST() {
		return ZBC_IPHOST;
	}

	public String getZBC_PORTA() {
		return ZBC_PORTA;
	}

	public String getZBC_ENVIRO() {
		return ZBC_ENVIRO;
	}

	public String getZBC_INST() {
		return ZBC_INST;
	}

	

}
