package br.com.totvs.java3C.JSonParser.monit;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import br.com.totvs.java3C.factory.TcpConnFactory;
//import br.com.totvs.java3C.factory.TcpSpecificConnFactory;


public class DiskSpaceParserMon {
	
	JSONObject jsonObject;	
	private String[] returnArray;
	
	private boolean isTcpSrvConn; 
	private String tcpSrvReturn;
	
	
	public DiskSpaceParserMon(String ip, String porta, String ambiente, String caminho) {
		

		String diskSpaceStr = "DISKSPACE#" + ip + "#" + porta + "#" + ambiente + "#" + caminho + "#\0";
		
//		System.out.println("Dados para monitoramento (String): " + diskSpaceStr);
		
		TcpConnFactory tcpConnFactory = new TcpConnFactory(diskSpaceStr, "tcpSrvMonit");
		tcpConnFactory.execute();
		isTcpSrvConn = tcpConnFactory.tcpSrvConn();
		tcpSrvReturn = tcpConnFactory.getTcpSrvReturn();
		
		
		if(!isTcpSrvConn) {
			System.out.println("Nao foi possível conectar-se ao servico TCP de monitoramento.");
			System.exit(3);
		}
		
		
		
//		TcpSpecificConnFactory tcpSpecificConnFactory = new TcpSpecificConnFactory(diskSpaceStr, "172.16.102.37", 9130);
//		tcpSpecificConnFactory.execute();
//		tcpSrvReturn = tcpSpecificConnFactory.getTcpSrvReturn();

        	
		
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

        	
        	JSONArray jSonArray = jsonObject.getJSONArray("DISKSPACE");
        												   
        	String[] returnArray = new String[5];
        	
        	returnArray[0] = (String) jSonArray.getJSONObject(0).get("DATABASESIZE");
        	returnArray[1] = (String) jSonArray.getJSONObject(0).get("FILESSIZE");
        	returnArray[2] = (String) jSonArray.getJSONObject(0).get("STATUS");
        	returnArray[3] = (String) jSonArray.getJSONObject(0).get("MESSAGE");
        	returnArray[4] = (String) jSonArray.getJSONObject(0).get("ELAPTIME");
        	
        	
        	this.returnArray = returnArray;
        	        	
		} catch (JSONException e) {
			e.printStackTrace();
		}
	
		
	}
	
	
	public String[] getReturnArray() {
		return this.returnArray;
	}
	


}

