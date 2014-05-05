package br.com.totvs.java3C.rm.JSonParser;

import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import br.com.totvs.java3C.rm.bean.Server;

public class RmSrvRetAvbtyParser {
	
	private String elaptime;
	private String message;
	private String status;
	private ArrayList<Server> servers;
	

	public RmSrvRetAvbtyParser(String serverReturn) {
		
		servers = new ArrayList<Server>();
		
		try {
		
			JSONObject jsonObject = new JSONObject(serverReturn);
			
			this.elaptime = (String) jsonObject.get("ELAPTIME");
			this.message = (String) jsonObject.get("MESSAGE");
			this.status = (String) jsonObject.get("STATUS");
			
			JSONArray jsonArrayServers = (JSONArray) jsonObject.get("SERVERS");
			
			for(int j = 0; j < jsonArrayServers.length(); j++) {
				
				String ipTmp = (String) jsonArrayServers.getJSONObject(j).get("IP");
				String messageTmp = (String) jsonArrayServers.getJSONObject(j).get("MESSAGE");
				String nameTmp = (String) jsonArrayServers.getJSONObject(j).get("NAME");
				String portTmp = (String) jsonArrayServers.getJSONObject(j).get("PORT");
				String statusTmp = (String) jsonArrayServers.getJSONObject(j).get("STATUS");
				
				this.servers.add(new Server(ipTmp, messageTmp, nameTmp, portTmp, statusTmp));
			}
			
		}
    	catch(NullPointerException e) {
    		System.out.println("JSON retornado pelo servico RM de monitoramento incompleto: um ou mais parametros nulo(s).");
    		System.exit(3);
    	}
		catch(JSONException j) {
			System.out.println("JSON invalido, retornado pelo servidor: " + serverReturn);
			System.exit(3);
		}
	
		
		
	}
	
	
	
	public String getElaptime() {
		return elaptime;
	}
	
	public String getMessage() {
		return message;
	}
	
	public String getStatus() {
		return status;
	}
	
	public ArrayList<Server> getServers() {
		return servers;
	}
	
	
	
	
	
	

}
