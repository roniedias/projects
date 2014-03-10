package br.com.totvs.java3C.JSonParser.monit;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import br.com.totvs.java3C.factory.TcpConnFactory;
//import br.com.totvs.java3C.factory.TcpSpecificConnFactory;


public class ActiveConnParserMon {
	
	private JSONObject jsonObject;
	private String[] returnArray;
	
	private boolean isTcpSrvConn; 
	private String tcpSrvReturn;
	
	
	public ActiveConnParserMon(String ip, String porta, String ambiente) {
		
		String activeConnStr = "ACTIVECONNECTIONS#" + ip + "#" + porta + "#" + ambiente + "#\0";
		
//		System.out.println("Dados para monitoramento (String): " + activeConnStr);
		
		TcpConnFactory tcpConnFactory = new TcpConnFactory(activeConnStr, "tcpSrvMonit");
		tcpConnFactory.execute();
		isTcpSrvConn = tcpConnFactory.tcpSrvConn();
		tcpSrvReturn = tcpConnFactory.getTcpSrvReturn();
		
		if(!isTcpSrvConn) {
			System.out.println("Nao foi possível conectar-se ao servico TCP de monitoramento.");
			System.exit(3);
		}
		
		
		
//		TcpSpecificConnFactory tcpSpecificConnFactory = new TcpSpecificConnFactory(activeConnStr, "172.16.102.37", 9130);
//		tcpSpecificConnFactory.execute();
//		tcpSrvReturn = tcpSpecificConnFactory.getTcpSrvReturn();
		
				
		
		// Parse dos dados a partir do jSon, retornado após a execução do monitoramento e atribuição dos mesmos a 
		// um array de três posições
        try {
        	
        	try {
        		jsonObject = new JSONObject(tcpSrvReturn);
        	}
        	catch(NullPointerException e) {
        		System.out.println("Nao foi possivel obter resposta JSON a partir do servico TCP de monitoramento.");
        		System.exit(3);
        	}
        	catch(JSONException j) {
        		System.out.println("JSON invalido, retornado pelo servico TCP de monitoramento: " + tcpSrvReturn);
        		System.exit(3);
        	}


        	
        	JSONArray jSonArray = jsonObject.getJSONArray("ACTIVECONNECTIONS");
        	
        	String[] returnArray = new String[3];
        	
        	returnArray[0] = (String) jSonArray.getJSONObject(0).get("CONNECTIONS");
        	returnArray[1] = (String) jSonArray.getJSONObject(0).get("MESSAGE");
        	returnArray[2] = (String) jSonArray.getJSONObject(0).get("ELAPTIME");
        	
        	this.returnArray = returnArray;
        	        	
		} catch (JSONException e) {
			e.printStackTrace();
		}
	
		
	}
		
	
	
	public String[] getReturnArray() {
		return this.returnArray;
	}
	
	
//	public static void main(String[] args) {
////		ActiveConnParserMon acpm = new ActiveConnParserMon("10.10.1.73", "8901", "MP3QXH");
//		ActiveConnParserMon acpm = new ActiveConnParserMon("10.10.1.108", "6062", "AUTOVIX");
//
//		
//		String[] returnArray = new String[3];
//		returnArray = acpm.getReturnArray();
//		
//		System.out.println("CONNECTIONS: " + returnArray[0]);	
//		System.out.println("MESSAGE: " + returnArray[1]);
//		System.out.println("ELAPTIME: " + returnArray[2]);		
//	}


}