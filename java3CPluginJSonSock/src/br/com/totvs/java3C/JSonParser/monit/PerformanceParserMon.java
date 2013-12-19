package br.com.totvs.java3C.JSonParser.monit;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import br.com.totvs.java3C.factory.TcpConnFactory;


/**
 * @author Ronie Dias Pinto
 */


public class PerformanceParserMon {
			
	private JSONObject jsonObject;
	private String[] returnArray;
	
	private boolean isTcpSrvConn; 
	private String tcpSrvReturn;
    	
		
	public PerformanceParserMon(String ip, String porta, String ambiente, String empresaFilial, String ipLicense, String portaLicense, String ambienteLicense) {
	      	  
		
		String performanceStr = "PERFORMANCE#" + ip + "#" + porta + "#" + ambiente + "#" + empresaFilial + "#" + ipLicense + "#" + portaLicense + "#" + ambienteLicense + "#\0";
		
		TcpConnFactory tcpConnFactory = new TcpConnFactory(performanceStr, "tcpSrvMonit");
		tcpConnFactory.execute();
		isTcpSrvConn = tcpConnFactory.tcpSrvConn();
		tcpSrvReturn = tcpConnFactory.getTcpSrvReturn();
		
		if(!isTcpSrvConn) {
			System.out.println("Nao foi possível conectar-se ao servico TCP de monitoramento.");
			System.exit(3);
		}
        
        
        try {
        	
        	try {
        		jsonObject = new JSONObject(tcpSrvReturn);
        	}
        	catch(NullPointerException e) {
        		System.out.println("Nao foi possivel obter resposta JSON a partir do TCP de monitoramento (null).");
        		System.exit(3);
        	}
        	catch(JSONException j) {
        		System.out.println("JSON invalido: " + tcpSrvReturn);
        		System.exit(3);
        	}

        	
        	JSONArray jSonArray = jsonObject.getJSONArray("PERFORMANCE");
        	
        	String[] returnArray = new String[4];
        	
        	returnArray[0] = (String) jSonArray.getJSONObject(0).get("TOTALTIME");
        	returnArray[1] = (String) jSonArray.getJSONObject(0).get("STATUS");
        	returnArray[2] = (String) jSonArray.getJSONObject(0).get("MESSAGE");
        	returnArray[3] = (String) jSonArray.getJSONObject(0).get("ELAPTIME");
        	
        	this.returnArray = returnArray;
        	        	
		} catch (JSONException e) {
			e.printStackTrace();
		}
        	        	        	
	}
	
	
	public String[] getReturnArray() {
		return this.returnArray;
	}
	
	
	

//	public static void main(String[] args) {
//
//		String ip = "10.10.3.38";
//		String porta = "10301";
//		String ambiente = "KPMG_PRODUCAO";
//		String empresaFilial = "01/01";
//
//		String ipLicense = "10.10.3.38";
//		String portaLicense = "3007";
//		String ambienteLicense = "ENVIRONMENT";
//		
//		String[] returnArray = new String[4];
//		
//		PerformanceParser pp = new PerformanceParser(ip, porta, ambiente, empresaFilial, ipLicense, portaLicense, ambienteLicense);
//		
////		System.out.println(pp.getjSonIn());
////		System.out.println(pp.getjSonOut());
//		
//		
//		returnArray = pp.getReturnArray();
//		
//		System.out.println("TOTALTIME: " + returnArray[0] + "; STATUS: " + returnArray[1] + "; MESSAGE: " + returnArray[2] + "; ELAPTIME: " + returnArray[3]);
//
//
//	}
	

	
}



