package br.com.totvs.java3C.rm.JSonParser;


import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import br.com.totvs.java3C.rm.bean.Diskspace;


public class RmSrvRetDskSpcParser {
	
	private Diskspace diskspace;
	
	public RmSrvRetDskSpcParser(String serverReturn) {
		
		try {
		
			JSONObject jsonObject = new JSONObject(serverReturn);			
			JSONArray jsonArrayServers = (JSONArray) jsonObject.get("DISKSPACES");
				
				String databaseSize = (String) jsonArrayServers.getJSONObject(0).get("DATABASESIZE");
				String elapTime = (String) jsonArrayServers.getJSONObject(0).get("ELAPTIME");
				String filesSize = (String) jsonArrayServers.getJSONObject(0).get("FILESSIZE");
				String message = (String) jsonArrayServers.getJSONObject(0).get("MESSAGE");
				String status = (String) jsonArrayServers.getJSONObject(0).get("STATUS");
				
				diskspace = new Diskspace(databaseSize, elapTime, filesSize, message, status);


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
	

	public Diskspace getDiskspace() {
		return diskspace;
	}
	
	
	
	
	
	
	
	
	

}
