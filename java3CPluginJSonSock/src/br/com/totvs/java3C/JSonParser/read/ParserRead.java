package br.com.totvs.java3C.JSonParser.read;

import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import br.com.totvs.java3C.bean.ServicosItemTipoAmb;
import br.com.totvs.java3C.factory.TcpConnFactory;


public class ParserRead {
	
		
	private String[] zbhTipSrvs;
	private String zbbStatus;
	
	
	private ArrayList<ServicosItemTipoAmb> servicosItemTipoAmbs = new ArrayList<ServicosItemTipoAmb>();
	private ArrayList<ServicosItemTipoAmb> todosServicosItemTipoAmb = new ArrayList<ServicosItemTipoAmb>();
	
	private boolean isTcpSrvConn; 
	private String tcpSrvReturn;

	
	
	public ParserRead(String nomeOperacao, String codAmbiente, String codTipoAmbiente , String codProduto) {
		
		
		String readStr = nomeOperacao + "#" + codAmbiente + "#" + codTipoAmbiente + "#" + codProduto + "#\0";
		
//		System.out.println("Dados para leitura (String): " + readStr);
		
		TcpConnFactory tcpConnFactory = new TcpConnFactory(readStr, "tcpSrvRead");
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
			JSONObject jsonObject2 = (JSONObject) jsonObject.get("SERVICES");
			
			zbhTipSrvs = new String[jsonObject2.length()];
			
			for(int o = 0; o < jsonObject2.length(); o++) {
				zbhTipSrvs[o] = jsonObject2.names().get(o).toString(); 
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
		return zbhTipSrvs;
	}
	

	
	public ArrayList<ServicosItemTipoAmb> getServicosItemTipoAmb(String zbhTipSrv) {
		try {
			
			JSONObject jsonObject = new JSONObject(tcpSrvReturn);
			JSONObject jsonObject2 = (JSONObject) jsonObject.get("SERVICES");
			
			boolean exists = false;
			
			
			
			for(int j = 0; j < jsonObject2.length(); j++) {
			
				
				if(jsonObject2.names().get(j).equals(zbhTipSrv)) {
					exists = true;
				}
			}
			
			if(!exists) {
				System.out.println("Nenhum servico cadastrado para o tipo " + zbhTipSrv + ".");
				System.exit(3);				
			}
					
			JSONArray jsonArray = jsonObject2.getJSONArray(zbhTipSrv);
			
		
			for(int s = 0; s < jsonArray.length(); s++) {

				String zbbParNag = (String) jsonArray.getJSONObject(s).get("ZBB_PARNAG");
				String zbcBalanc = (String) jsonArray.getJSONObject(s).get("ZBC_BALANC");
			    String zbcDns = (String) jsonArray.getJSONObject(s).get("ZBC_DNS");
			    String zbcEnviro = (String) jsonArray.getJSONObject(s).get("ZBC_ENVIRO");
			    String zbcIni = (String) jsonArray.getJSONObject(s).get("ZBC_INI");
			    String zbcInst = (String) jsonArray.getJSONObject(s).get("ZBC_INST");
			    String zbcIpExt = (String) jsonArray.getJSONObject(s).get("ZBC_IPEXT");
			    String zbcIpHost = (String) jsonArray.getJSONObject(s).get("ZBC_IPHOST");
			    String zbcPorta = (String) jsonArray.getJSONObject(s).get("ZBC_PORTA");
			    String zbcSeq = (String) jsonArray.getJSONObject(s).get("ZBC_SEQ");
			    String zbcTipSrv = (String) jsonArray.getJSONObject(s).get("ZBC_TIPSRV");
			    String zbcItem = (String) jsonArray.getJSONObject(s).get("ZBC_ITEM");
			    
			    servicosItemTipoAmbs.add(new ServicosItemTipoAmb(zbbParNag, zbcBalanc, zbcDns, zbcEnviro, zbcIni, zbcInst, zbcIpExt, zbcIpHost, zbcPorta, zbcSeq, zbcTipSrv, zbcItem));
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


	
	public ArrayList<ServicosItemTipoAmb> getTodosServicosItemTipoAmb() {
		try {
			
			JSONObject jsonObject = new JSONObject(tcpSrvReturn);
			
			JSONObject jsonObject2 = (JSONObject) jsonObject.get("SERVICES");
			
			
			
			for(int s = 0; s < jsonObject2.length(); s++) {
			
				JSONArray jsonArray = jsonObject2.getJSONArray(jsonObject2.names().get(s).toString());
				
				
				
				for(int a = 0; a < jsonArray.length(); a++) {
				
				
					String zbbParNag = (String) jsonArray.getJSONObject(a).get("ZBB_PARNAG");
					String zbcBalanc = (String) jsonArray.getJSONObject(a).get("ZBC_BALANC");
				    String zbcDns = (String) jsonArray.getJSONObject(a).get("ZBC_DNS");				    
				    String zbcEnviro = (String) jsonArray.getJSONObject(a).get("ZBC_ENVIRO");
				    String zbcIni = (String) jsonArray.getJSONObject(a).get("ZBC_INI");
				    String zbcInst = (String) jsonArray.getJSONObject(a).get("ZBC_INST");
				    String zbcIpExt = (String) jsonArray.getJSONObject(a).get("ZBC_IPEXT");
				    String zbcIpHost = (String) jsonArray.getJSONObject(a).get("ZBC_IPHOST");
				    String zbcPorta = (String) jsonArray.getJSONObject(a).get("ZBC_PORTA");
				    String zbcSeq = (String) jsonArray.getJSONObject(a).get("ZBC_SEQ");
				    String zbcTipSrv = (String) jsonArray.getJSONObject(a).get("ZBC_TIPSRV");
				    String zbcItem = (String) jsonArray.getJSONObject(a).get("ZBC_ITEM");
					
				    todosServicosItemTipoAmb.add(new ServicosItemTipoAmb(zbbParNag, zbcBalanc, zbcDns, zbcEnviro, zbcIni, zbcInst, zbcIpExt, zbcIpHost, zbcPorta, zbcSeq, zbcTipSrv, zbcItem));
				}
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
		return todosServicosItemTipoAmb;
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
//		ParserRead parserRead = new ParserRead("GENERIC_READ", "000051", "01", "000030");
//		parserRead.getServicosItemTipoAmb("10");
		
//		
//		ArrayList<ServicosItemTipoAmb> todosServicosItemTipoAmb = new ArrayList<ServicosItemTipoAmb>();
//		
//		todosServicosItemTipoAmb = parserRead.getTodosServicosItemTipoAmb();
//
//		
//		for(int s = 0; s < todosServicosItemTipoAmb.size(); s++) {
//
//			System.out.println("ZbcBalanc: " + todosServicosItemTipoAmb.get(s).getZbcBalanc());
//			System.out.println("ZbcDns: " + todosServicosItemTipoAmb.get(s).getZbcDns());
//			System.out.println("ZbcEnviro: " + todosServicosItemTipoAmb.get(s).getZbcEnviro());
//			System.out.println("ZbcIni: " + todosServicosItemTipoAmb.get(s).getZbcIni());
//			System.out.println("ZbcInst: " + todosServicosItemTipoAmb.get(s).getZbcInst());
//			System.out.println("ZbcIpExt: " + todosServicosItemTipoAmb.get(s).getZbcIpExt());
//			System.out.println("ZbcIpHost: " + todosServicosItemTipoAmb.get(s).getZbcIpHost());
//			System.out.println("ZbcPorta: " + todosServicosItemTipoAmb.get(s).getZbcPorta());
//			System.out.println("ZbcSeq: " + todosServicosItemTipoAmb.get(s).getZbcSeq());
//			System.out.println("ZbcTipSrv: " + todosServicosItemTipoAmb.get(s).getZbcTipSrv());
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
		
//		ArrayList<ServicosItemTipoAmb> servicosItemTipoAmb = new ArrayList<ServicosItemTipoAmb>();
//		
//		servicosItemTipoAmb = parserRead.getServicosItemTipoAmb("10");
//		
//		for(int s = 0; s < servicosItemTipoAmb.size(); s++) {
//			System.out.println("ZbcBalanc: " + servicosItemTipoAmb.get(s).getZbcBalanc());
//			System.out.println("ZbcDns: " + servicosItemTipoAmb.get(s).getZbcDns());
//			System.out.println("ZbcEnviro: " + servicosItemTipoAmb.get(s).getZbcEnviro());
//			System.out.println("ZbcIni: " + servicosItemTipoAmb.get(s).getZbcIni());
//			System.out.println("ZbcInst: " + servicosItemTipoAmb.get(s).getZbcInst());
//			System.out.println("ZbcIpExt: " + servicosItemTipoAmb.get(s).getZbcIpExt());
//			System.out.println("ZbcIpHost: " + servicosItemTipoAmb.get(s).getZbcIpHost());
//			System.out.println("ZbcPorta: " + servicosItemTipoAmb.get(s).getZbcPorta());
//			System.out.println("ZbcSeq: " + servicosItemTipoAmb.get(s).getZbcSeq());
//			System.out.println("ZbcTipSrv: " + servicosItemTipoAmb.get(s).getZbcTipSrv());
//			System.out.println("ZbcItem: " + servicosItemTipoAmb.get(s).getZbcItem());
//			
//			System.out.println("=======================================");
//		}
//				
//	}


}

