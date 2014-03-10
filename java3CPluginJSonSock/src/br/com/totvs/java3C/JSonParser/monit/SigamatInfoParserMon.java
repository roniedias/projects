package br.com.totvs.java3C.JSonParser.monit;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import br.com.totvs.java3C.factory.TcpConnFactory;


public class SigamatInfoParserMon {
				
	private JSONObject jsonObject;
	private String[] returnArray;
	
	private boolean isTcpSrvConn; 
	private String tcpSrvReturn;
	
	
	public SigamatInfoParserMon(String ip, String porta, String ambiente) {
			
		String sigamatInfoStr = "SIGAMATINFO#" + ip + "#" + porta + "#" + ambiente + "#\0";
		
//		System.out.println("Dados para monitoramento (String): " + sigamatInfoStr);
		
		TcpConnFactory tcpConnFactory = new TcpConnFactory(sigamatInfoStr, "tcpSrvMonit");
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

        	
        	JSONArray jSonArray = jsonObject.getJSONArray("SIGAMATINFO");
        	
        	String[] returnArray = new String[4];
	        	
        	returnArray[0] = (String) jSonArray.getJSONObject(0).get("DATE");
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
//			SigamatInfoParserMon sipm = new SigamatInfoParserMon("10.10.3.38", "10317", "KPMG_PRODUCAO");
//			String[] returnArray = new String[4];
//			returnArray = sipm.getReturnArray();
//			
//			System.out.println("DATE: " + returnArray[0]);
//			System.out.println("STATUS: " + returnArray[1]);
//			System.out.println("MESSAGE: " + returnArray[2]);
//			System.out.println("ELAPTIME: " + returnArray[3]);
//	}
		

}

