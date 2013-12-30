package br.com.totvs.java3C.JSonParser.monit;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import br.com.totvs.java3C.factory.TcpConnFactory;


public class TssParserMon {
		
	private JSONObject jsonObject;		
	private String[] returnArray;
	
	private boolean isTcpSrvConn;
	private String tcpSrvReturn;
	
		
	public TssParserMon(String ip, String porta, String alias, String database, String cnpj) {
		
		
		String tssInfoStr = "TSSINFO#" + ip + "#" + porta + "#" + alias + "#" + database + "#" + cnpj + "#\0";
		
		TcpConnFactory tcpConnFactory = new TcpConnFactory(tssInfoStr, "tcpSrvMonit");
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
        		System.out.println("Nao foi possivel obter resposta JSON a partir do TCP de monitoramento (null)");
        		System.exit(3);
        	}
        	catch(JSONException j) {
        		System.out.println("JSON invalido: " + tcpSrvReturn);
        		System.exit(3);
        	}
        	
        	
        	JSONArray jSonArray = jsonObject.getJSONArray("TSSINFO");
        	
        	String[] returnArray = new String[9];
	        	
        	returnArray[0] = (String) jSonArray.getJSONObject(0).get("CNPJ");
        	returnArray[1] = (String) jSonArray.getJSONObject(0).get("PRODUTO_TESTE");
        	returnArray[2] = (String) jSonArray.getJSONObject(0).get("PRODUTO_PRODUCAO");
        	returnArray[3] = (String) jSonArray.getJSONObject(0).get("SERVICO_TESTE");
        	returnArray[4] = (String) jSonArray.getJSONObject(0).get("SERVICO_PRODUCAO");
        	returnArray[5] = (String) jSonArray.getJSONObject(0).get("STATUS");
        	returnArray[6] = (String) jSonArray.getJSONObject(0).get("MESSAGE");
        	returnArray[7] = (String) jSonArray.getJSONObject(0).get("ELAPTIME");
        	returnArray[8] = (String) jSonArray.getJSONObject(0).get("COLABORACAO");
        	
        	
        	this.returnArray = returnArray;
	        	        	
		} catch (JSONException e) {
			e.printStackTrace();
		}
		
			
	}
		
		
	public String[] getReturnArray() {
		return this.returnArray;
	}
		

}

