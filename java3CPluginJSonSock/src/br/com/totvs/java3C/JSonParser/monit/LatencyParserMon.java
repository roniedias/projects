package br.com.totvs.java3C.JSonParser.monit;

import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import br.com.totvs.java3C.bean.LatencyLog;
import br.com.totvs.java3C.bean.LogInfo;
import br.com.totvs.java3C.bean.ServerLatency;
import br.com.totvs.java3C.factory.TcpConnFactory;
//import br.com.totvs.java3C.factory.TcpSpecificConnFactory;

import br.com.totvs.java3C.util.SrvsLatencyStrMaker;

/**
 * @author Ronie Dias Pinto
 */


public class LatencyParserMon {
			    
    
    private JSONObject jsonObject;
    
    private String elapTime;
	private String lastDate;
	private String lastHour;
	private String message; 
	private String status;
	
	private ArrayList<LatencyLog> latencyLog = new ArrayList<LatencyLog>();
			
	private boolean isTcpSrvConn; 
	private String tcpSrvReturn;

	
	public LatencyParserMon(ArrayList<ServerLatency> servers) {
		  	
    	
		SrvsLatencyStrMaker tsl = new SrvsLatencyStrMaker(servers);
		String srvs = tsl.getSrvsLatency();
		
		String latencyStr = "LATENCY#" + servers.size() + srvs + "#\0";
		

		TcpConnFactory tcpConnFactory = new TcpConnFactory(latencyStr, "tcpSrvMonit");
		tcpConnFactory.execute();
		isTcpSrvConn = tcpConnFactory.tcpSrvConn();
		tcpSrvReturn = tcpConnFactory.getTcpSrvReturn();
		
		if(!isTcpSrvConn) {
			System.out.println("Nao foi possível conectar-se ao servico TCP de monitoramento.");
			System.exit(3);
		}
		
		
//		TcpSpecificConnFactory tcpSpecificConnFactory = new TcpSpecificConnFactory(latencyStr, "172.16.102.37", 9130);
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
    	
        	
        	this.elapTime    = jsonObject.getString("ELAPTIME");
        	this.lastDate    = jsonObject.getString("LASTDATE");
        	this.lastHour    = jsonObject.getString("LASTHOUR");        	
        	this.message     = jsonObject.getString("MESSAGE"); 
        	this.status      = jsonObject.getString("STATUS");
        	
        	JSONArray jSonLatencyLog = jsonObject.getJSONArray("LATENCYLOG");
        	
        	
        	for(int j = 0; j < jSonLatencyLog.length(); j++) {
        		
        		JSONArray jsa = (JSONArray)jSonLatencyLog.getJSONObject(j).get("LOGINFO");
        		
        		LogInfo[] li = new LogInfo[jsa.length()];
        		
        		for(int k = 0; k < jsa.length(); k++) {
        			
            		String activityTime = (String) jsa.getJSONObject(k).get("ACTIVITYTIME");
            		String environment = (String) jsa.getJSONObject(k).get("ENVIRONMENT");
    				String avg = (String) jsa.getJSONObject(k).get("AVG");
    				String logDate = (String) jsa.getJSONObject(k).get("LOGDATE");
    				String logHour = (String) jsa.getJSONObject(k).get("LOGHOUR");
    				String lost = (String) jsa.getJSONObject(k).get("LOST");
    				String max = (String) jsa.getJSONObject(k).get("MAX");
    				String min = (String) jsa.getJSONObject(k).get("MIN");
    				String pingDate = (String) jsa.getJSONObject(k).get("PINGDATE");
    				String pingHour = (String) jsa.getJSONObject(k).get("PINGHOUR");
    				String remoteIp = (String) jsa.getJSONObject(k).get("REMOTEIP");
    				String remoteMachine = (String) jsa.getJSONObject(k).get("REMOTEMACHINE");
    				String remoteUser = (String) jsa.getJSONObject(k).get("REMOTEUSER");
    				String serverThread = (String) jsa.getJSONObject(k).get("SERVERTHREAD");
    				
    				li[k] = new LogInfo(activityTime, avg, environment, logDate, logHour, lost, max, min, pingDate, pingHour, remoteIp, remoteMachine, remoteUser, serverThread); 
        		}
        		
        		latencyLog.add(new LatencyLog((String) jSonLatencyLog.getJSONObject(j).get("ENVIRONMENT"), (String) jSonLatencyLog.getJSONObject(j).get("IP"), (String) jSonLatencyLog.getJSONObject(j).get("MESSAGE"), (String) jSonLatencyLog.getJSONObject(j).get("PORT"), (String) jSonLatencyLog.getJSONObject(j).get("STATUS"), li));      	        			        		
        	}
        	
        	        	
        	        	
		} catch (JSONException e) {
			e.printStackTrace();
		}
         
	}



	
	public String getElapTime() {
		return elapTime;
	}

	public String getLastDate() {
		return lastDate;
	}

	public String getLastHour() {
		return lastHour;
	}

	public String getMessage() {
		return message;
	}

	public String getStatus() {
		return status;
	}
		          	
	public ArrayList<LatencyLog> getLatencyLog() {
		return latencyLog;
	}
	

	
//	public static void main(String[] args) {
//
//		String ip = "10.10.3.38";
//		String porta1 = "10301";
//		String porta2 = "10302";
//		String porta3 = "10303";
//		String ambiente = "KPMG_PRODUCAO";
//		String data = "29/10/2013";
//		String hora1 = "13:00:00";
//		String hora2 = "14:38:40";
//		String hora3 = "15:45:53";
//
//	    ArrayList<ServerLatency> servers = new ArrayList<ServerLatency>();
//	    
//    	servers.add(new ServerLatency(ip, porta1, ambiente, data, hora1));
//    	servers.add(new ServerLatency(ip, porta2, ambiente, data, hora2));
//    	servers.add(new ServerLatency(ip, porta3, ambiente, data, hora3));
//    	
//    	LatencyParserMon lp = new LatencyParserMon(servers);
//    	
//    	System.out.println("jSonIn: " + lp.getjSonIn());
//    	System.out.println("jSonOut: " + lp.getjSonOut());
    	
//    	ArrayList<LatencyLog> latencyLog = new ArrayList<LatencyLog>();
//    	
//    	latencyLog = lp.getLatencyLog();
//    	
//    	
//    	for(int l = 0; l < latencyLog.size(); l++) {    		
//    		
//    		System.out.println(latencyLog.get(l).getEnvironment());
//    		System.out.println(latencyLog.get(l).getIp());
//    		System.out.println(latencyLog.get(l).getMessage());
//    		System.out.println(latencyLog.get(l).getPort());
//    		System.out.println(latencyLog.get(l).getStatus());
//    		
//    		System.out.println("======");
//    		System.out.println("logInfo");
//    		System.out.println("======");
//    		
//    		
//    		LogInfo[] logInfo = latencyLog.get(l).getLogInfo();
//    		
//    		for(int i = 0; i < logInfo.length; i++) {
//    		
//    			System.out.println(logInfo[i].getActivityTime());
//    			System.out.println(logInfo[i].getAvg());
//    			System.out.println(logInfo[i].getLogDate());
//    			System.out.println(logInfo[i].getLogHour());
//    			System.out.println(logInfo[i].getLost());
//    			System.out.println(logInfo[i].getMax());
//    			System.out.println(logInfo[i].getMin());
//    			System.out.println(logInfo[i].getPingDate());
//    			System.out.println(logInfo[i].getPingHour());
//    			System.out.println(logInfo[i].getRemoteIp());
//    			System.out.println(logInfo[i].getRemoteMachine());
//    			System.out.println(logInfo[i].getRemoteUser());
//    			System.out.println(logInfo[i].getServerThread());
//    			
//    			System.out.println(" --------------------");
//    		}
//    		
//    		
//    		System.out.println("=================================");
//    			
//    	}
//
//	}
	

	
}


