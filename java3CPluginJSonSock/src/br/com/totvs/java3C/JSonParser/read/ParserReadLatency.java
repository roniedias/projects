package br.com.totvs.java3C.JSonParser.read;

import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import br.com.totvs.java3C.bean.ServicosItemTipoAmbLatency;
import br.com.totvs.java3C.factory.TcpConnFactory;


public class ParserReadLatency {
	
		
	private String[] zbhTipSrvs;
	private String zbbStatus;
	
	private ArrayList<ServicosItemTipoAmbLatency> servicosItemTipoAmbs;
	private ArrayList<ServicosItemTipoAmbLatency> todosServicosItemTipoAmb = new ArrayList<ServicosItemTipoAmbLatency>();
	
	private boolean isTcpSrvConn; 
	private String tcpSrvReturn;


	
	public ParserReadLatency(String nomeOperacao, String codAmbiente, String codTipoAmbiente , String codProduto) {
		
		String readLatencyStr = nomeOperacao + "#" + codAmbiente + "#" + codTipoAmbiente + "#" + codProduto + "#\0";
		
		TcpConnFactory tcpConnFactory = new TcpConnFactory(readLatencyStr, "tcpSrvRead");
		tcpConnFactory.execute();
		isTcpSrvConn = tcpConnFactory.tcpSrvConn();
		tcpSrvReturn = tcpConnFactory.getTcpSrvReturn();
		
		if(!isTcpSrvConn) {
			System.out.println("Nao foi possível conectar-se ao servico TCP de leitura.");
			System.exit(3);
		}			
					
	}
	
	
	
	public String[] getZbhTipSrvs() {
		try {
			JSONObject jsonObject = new JSONObject(tcpSrvReturn);		
			zbhTipSrvs = new String[jsonObject.length()];
			
			for(int o = 0; o < jsonObject.length(); o++) 
				zbhTipSrvs[o] = jsonObject.names().get(o).toString(); 			
		}
    	catch(NullPointerException e) {
    		System.out.println("Nao foi possivel obter resposta JSON a partir do TCP de leitura (null).");
    		System.exit(3);
    	}
		catch(JSONException j) {
			System.out.println("JSON invalido, retornado pelo servico TCP de leitura: " + tcpSrvReturn);
			System.exit(3);
		}
		return zbhTipSrvs;
	}
	

	public ArrayList<ServicosItemTipoAmbLatency> getServicosItemTipoAmb(String zbhTipSrv) {
		try {
			
			JSONObject jsonObject = new JSONObject(tcpSrvReturn);
			
			boolean exists = false;
			
			for(int j = 0; j < jsonObject.length(); j++) {
				if(jsonObject.names().get(j).equals(zbhTipSrv)) {
					exists = true;
				}
			}
			
			if(!exists) {
				System.out.println("Nenhum servico cadastrado para o tipo " + zbhTipSrv + ".");
				System.exit(3);				
			}
			
			
			JSONArray jSonArray = jsonObject.getJSONArray(zbhTipSrv);
			
			servicosItemTipoAmbs = new ArrayList<ServicosItemTipoAmbLatency>();

			for(int s = 0; s < jSonArray.length(); s++) {
				

			    String zbdDtMoni = (String) jSonArray.getJSONObject(s).get("ZBD_DTMONI");				
			    String zbdHrMoni = (String) jSonArray.getJSONObject(s).get("ZBD_HRMONI");				
			    String zbcEnviro = (String) jSonArray.getJSONObject(s).get("ZBC_ENVIRO");
			    String zbcIpHost = (String) jSonArray.getJSONObject(s).get("ZBC_IPHOST");
			    String zbcPorta = (String) jSonArray.getJSONObject(s).get("ZBC_PORTA");
			    String zbcItem = (String) jSonArray.getJSONObject(s).get("ZBC_ITEM");
			    String zbcBalanc = (String) jSonArray.getJSONObject(s).get("ZBC_BALANC");
			    
			    
			    servicosItemTipoAmbs.add(new ServicosItemTipoAmbLatency(zbdDtMoni, zbdHrMoni, zbcEnviro, zbcIpHost, zbcPorta, zbcItem, zbcBalanc));
			}
		}
    	catch(NullPointerException e) {
    		System.out.println("Nao foi possivel obter resposta JSON a partir do TCP de leitura (null).");
    		System.exit(3);
    	}
		catch(JSONException j) {
			System.out.println("JSON invalido, retornado pelo servico TCP de leitura: " + tcpSrvReturn);
			System.exit(3);
		}
		return servicosItemTipoAmbs;
    }


	
	public ArrayList<ServicosItemTipoAmbLatency> getTodosServicosItemTipoAmb() {
		try {
			
			JSONObject jsonObject = new JSONObject(tcpSrvReturn);
			
			for(int o = 0; o < jsonObject.length(); o++) {
				
				JSONArray jSonArray = jsonObject.getJSONArray(jsonObject.names().get(o).toString());
				
				for(int s = 0; s < jSonArray.length(); s++) {
					
				    String zbdDtMoni = (String) jSonArray.getJSONObject(s).get("ZBD_DTMONI");				
				    String zbdHrMoni = (String) jSonArray.getJSONObject(s).get("ZBD_HRMONI");									
				    String zbcEnviro = (String) jSonArray.getJSONObject(s).get("ZBC_ENVIRO");
				    String zbcIpHost = (String) jSonArray.getJSONObject(s).get("ZBC_IPHOST");
				    String zbcPorta =  (String) jSonArray.getJSONObject(s).get("ZBC_PORTA");
				    String zbcItem =   (String) jSonArray.getJSONObject(s).get("ZBC_ITEM");
				    String zbcBalanc =   (String) jSonArray.getJSONObject(s).get("ZBC_BALANC");
				    
				    todosServicosItemTipoAmb.add(new ServicosItemTipoAmbLatency(zbdDtMoni, zbdHrMoni, zbcEnviro, zbcIpHost, zbcPorta, zbcItem, zbcBalanc)); 
				}
				
			}
			
			return todosServicosItemTipoAmb;
		}
    	catch(NullPointerException e) {
    		System.out.println("Nao foi possivel obter resposta JSON a partir do TCP de leitura (null).");
    		System.exit(3);
    	}
		catch(JSONException j) {
			System.out.println("JSON invalido, retornado pelo servico TCP de leitura: " + tcpSrvReturn);
			System.exit(3);
		}
		return servicosItemTipoAmbs;
    }

	
	
	public String getZbbStatus() {
		try {
			JSONObject jsonObject = new JSONObject(tcpSrvReturn);
			zbbStatus = jsonObject.getString("ZBB_STATUS");
		}
    	catch(NullPointerException e) {
    		System.out.println("Nao foi possivel obter resposta JSON a partir do TCP de leitura (null)");
    		System.exit(3);
    	}
		catch(JSONException j) {
			System.out.println("JSON invalido, retornado pelo servico TCP de leitura: " + tcpSrvReturn);
			System.exit(3);
		}
		return zbbStatus;
	}
	
	
	
	
//	public static void main(String[] args) {
//
//		ParserReadLatency parserRead = new ParserReadLatency("LATENCY_READ", "000070", "01", "000030");
//		
//		System.out.println(parserRead.getZbbStatus());
		
//		System.out.println(parserRead.getJSonIn());
//		System.out.println(parserRead.getJSonOut());
//		
//		ArrayList<ServicosItemTipoAmbLatency> todosServicosItemTipoAmb = new ArrayList<ServicosItemTipoAmbLatency>();
//		
//		todosServicosItemTipoAmb = parserRead.getTodosServicosItemTipoAmb();
//		
//		for(int s = 0; s < todosServicosItemTipoAmb.size(); s++) {
//
//					
//			System.out.println("zbdDtMoni: " + todosServicosItemTipoAmb.get(s).getZbdDtMoni());
//			System.out.println("zbdHrMoni: " + todosServicosItemTipoAmb.get(s).getZbdHrMoni());
//			System.out.println("ZbcEnviro: " + todosServicosItemTipoAmb.get(s).getZbcEnviro());
//			System.out.println("ZbcIpHost: " + todosServicosItemTipoAmb.get(s).getZbcIpHost());
//			System.out.println("ZbcPorta: " + todosServicosItemTipoAmb.get(s).getZbcPorta());
//			System.out.println("ZbcItem: " + todosServicosItemTipoAmb.get(s).getZbcItem());
//			
//			System.out.println("=======================================");
//
//		}
//		
//		
//		String[] zbhTipSrvs = parserRead.getZbhTipSrvs();
//		
//		for(int s = 0; s < zbhTipSrvs.length; s++) {
//			System.out.println(zbhTipSrvs[s]);
//		}
//		
//		ArrayList<ServicosItemTipoAmbLatency> servicosItemTipoAmb = new ArrayList<ServicosItemTipoAmbLatency>();
//		
//		servicosItemTipoAmb = parserRead.getServicosItemTipoAmb("16");
//		
//		for(int s = 0; s < servicosItemTipoAmb.size(); s++) {
//
//			System.out.println("zbdDtMoni: " + todosServicosItemTipoAmb.get(s).getZbdDtMoni());
//			System.out.println("zbdHrMoni: " + todosServicosItemTipoAmb.get(s).getZbdHrMoni());
//			System.out.println("ZbcEnviro: " + servicosItemTipoAmb.get(s).getZbcEnviro());
//			System.out.println("ZbcIpHost: " + servicosItemTipoAmb.get(s).getZbcIpHost());
//			System.out.println("ZbcPorta: " + servicosItemTipoAmb.get(s).getZbcPorta());
//			System.out.println("ZbcItem: " + servicosItemTipoAmb.get(s).getZbcItem());
//			
//			System.out.println("=======================================");
//		}
//				
//	}


}


