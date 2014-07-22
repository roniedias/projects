
package br.com.totvs.java3C.deprecated;

import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import br.com.totvs.java3C.bean.ServerAvailability;
import br.com.totvs.java3C.factory.TcpConnFactory;


public class AvailabilityParserMonCustomer {
	
	JSONObject jsonObject;
  
	private String elapTime;
	private String messageInfo;
	private String statusInfo;
	
	private ArrayList<ServerAvailability> servers = new ArrayList<ServerAvailability>();
	
	private boolean isTcpSrvConn; 
	private String tcpSrvReturn;
	
	
	public AvailabilityParserMonCustomer(String tipo, String ip, String porta, String ambiente, String empresaFilial) {
		
		String availabilityConnStr = "AVAILABILITY#" + ip + "#" + porta + "#" + ambiente + "#" + empresaFilial + "#" + tipo + "#\0";
		
//		System.out.println("Dados para monitoramento (String): " + availabilityConnStr);
		
		TcpConnFactory tcpConnFactory = new TcpConnFactory(availabilityConnStr, "tcpSrvMonit");
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


        	
        	elapTime    = jsonObject.getString("ELAPTIME");
        	messageInfo = jsonObject.getString("MESSAGE");
        	statusInfo  = jsonObject.getString("STATUS");
        	
			JSONArray jSonArray = jsonObject.getJSONArray("SERVERS");
        	
        	for(int s = 0; s < jSonArray.length(); s++) {
        		this.servers.add(new ServerAvailability((String) jSonArray.getJSONObject(s).get("IP"), (String) jSonArray.getJSONObject(s).get("MESSAGE"), (String) jSonArray.getJSONObject(s).get("NAME"), (String) jSonArray.getJSONObject(s).get("PORT"), (String) jSonArray.getJSONObject(s).get("STATUS")));
        	}
        	
        	        	
		} catch (JSONException e) {
			e.printStackTrace();
		}	
		
	}
	
	
	
	public String getElapTime() {
		return elapTime;
	}

	
	public String getMessageInfo() {
		return messageInfo;
	}

	
	public String getStatusInfo() {
		return statusInfo;
	}
	

	public ArrayList<ServerAvailability> getServers() {
		return servers;
	}
	
	

	
//	public static void main(String[] args) {
//		AvailabilityParserMon availabilityParserMon = new AvailabilityParserMon("1", "10.10.3.38", "10317", "KPMG_PRODUCAO", "01/01"); // KPMG
//	    AvailabilityParserMon availabilityParserMon = new AvailabilityParserMon("1", "10.10.1.73", "8990", "MP3QXH", "01/01"); // AYMAN
//		AvailabilityParserMon availabilityParserMon = new AvailabilityParserMon("1", "10.10.1.130", "6029", "AUTOVIX", "01/01"); //
//		ArrayList<ServerAvailability> servers = availabilityParserMon.getServers();
//		
//		
//		System.out.println(availabilityParserMon.getElapTime());
//		System.out.println(availabilityParserMon.getStatusInfo());
//		System.out.println(availabilityParserMon.getMessageInfo());
//		
//		for(ServerAvailability s : servers) {
//			System.out.println("============");
//			System.out.println(s.getIp());
//			System.out.println(s.getPort());
//			System.out.println(s.getName());
//			System.out.println(s.getMessage());
//			System.out.println(s.getStatus());
//		}
//		
//	}
	

}
