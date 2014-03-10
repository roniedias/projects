package br.com.totvs.java3C.JSonParser.read;

import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;


import br.com.totvs.java3C.bean.ZbcTopService;
import br.com.totvs.java3C.factory.TcpConnFactory;


public class ParserReadTss {
	
	
	private String zcaItem;
	private ArrayList<String> zbgCnpj = new ArrayList<String>();
	private String zbbAmbPai;
	private String za6TopNic;
	private ArrayList<ZbcTopService> zbcTopServices = new ArrayList<ZbcTopService>();
	private String zbbStatus;

	private boolean isTcpSrvConn; 
	private String tcpSrvReturn;


	
	public ParserReadTss(String nomeOperacao, String codAmbiente, String codTipoAmbiente, String codProduto) {
		
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
	
	
	
	
	public String getZcaItem() {
		try {
			JSONObject jsonObject = new JSONObject(tcpSrvReturn);
			zcaItem = jsonObject.getString("ZCA_ITEM");
		}
    	catch(NullPointerException e) {
    		System.out.println("Nao foi possivel obter resposta JSON a partir do TCP de leitura (null).");
    		System.exit(3);
    	}
		catch(JSONException j) {
			System.out.println("JSON invalido, retornado pelo servico TCP de leitura: " + tcpSrvReturn);
			System.exit(3);
		}
		return zcaItem;
	}
	
	
	
	public ArrayList<String> getZbgCnpj() {
		try {
			JSONObject jsonObject = new JSONObject(tcpSrvReturn);
			JSONArray jSonArray = jsonObject.getJSONArray("ZBG_CNPJ");
			
			for(int s = 0; s < jSonArray.length(); s++) {
				zbgCnpj.add(jSonArray.getString(s));
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
		return zbgCnpj;
	}

	
	
	public String getZbbAmbPai() {
		try {
			JSONObject jsonObject = new JSONObject(tcpSrvReturn);
			zbbAmbPai = jsonObject.getString("ZBB_AMBPAI");
		}
    	catch(NullPointerException e) {
    		System.out.println("Nao foi possivel obter resposta JSON a partir do TCP de leitura (null).");
    		System.exit(3);
    	}
		catch(JSONException j) {
			System.out.println("JSON invalido, retornado pelo servico TCP de leitura: " + tcpSrvReturn);
			System.exit(3);
		}
		return zbbAmbPai;
	}

	

	public String getZa6TopNic() {
		try {
			JSONObject jsonObject = new JSONObject(tcpSrvReturn);
			za6TopNic = jsonObject.getString("ZA6_TOPNIC");
		}
    	catch(NullPointerException e) {
    		System.out.println("Nao foi possivel obter resposta JSON a partir do TCP de leitura (null).");
    		System.exit(3);
    	}
		catch(JSONException j) {
			System.out.println("JSON invalido, retornado pelo servico TCP de leitura: " + tcpSrvReturn);
			System.exit(3);
		}
		return za6TopNic;
	}
	
	
	
	public ArrayList<ZbcTopService> getZbcTopServices() {
		
		try {
			JSONObject jsonObject = new JSONObject(tcpSrvReturn);
			JSONArray jSonArray = jsonObject.getJSONArray("ZBC_TOPSERVICE");
			
			for(int j = 0; j < jSonArray.length(); j++) {
				String zbcIpHost = (String) jSonArray.getJSONObject(j).get("ZBC_IPHOST");
				String zbcPorta = (String) jSonArray.getJSONObject(j).get("ZBC_PORTA");
				String zbcEnviro = (String) jSonArray.getJSONObject(j).get("ZBC_ENVIRO");
				String zbcBalanc = (String) jSonArray.getJSONObject(j).get("ZBC_BALANC");
				
				this.zbcTopServices.add(new ZbcTopService(zbcIpHost, zbcPorta, zbcEnviro, zbcBalanc));
				
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

		return zbcTopServices;
		
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
//		ParserReadTss parserRead = new ParserReadTss("TSS_READ", "000051", "01", "000036");
//		System.out.println(parserRead.getZbbStatus());
//   }
		
	
	
	
	
//	public static void main(String[] args) {
//
//
//		for(int i = 0; i < 100; i++) {
//		
//			
//			new Thread(new Runnable() {
//				
//				public void run() {
//			
//					ParserReadTss parserRead = new ParserReadTss("TSS_READ", "000051", "01", "000036");
//						
//                    
//					System.out.println(parserRead.getJSonIn());
//					System.out.println(parserRead.getJSonOut());
//						
//				}
//	
//			}).start();
//
//		}
		
		
		
		
		
//		ArrayList<ZbcTopService> zbcTopServices = new ArrayList<ZbcTopService>();
		
//		zbcTopServices = parserRead.getZbcTopServices();
//		
//		for(ZbcTopService s : zbcTopServices) {
//			System.out.println(s.getZbcIpHost());
//			System.out.println(s.getZbcPorta());
//			System.out.println(s.getZbcEnviro());
//			System.out.println(s.getZbcBalanc());
//			System.out.println("================");
//		}
		
		
//		System.out.println(parserRead.getJSonIn());
//		System.out.println(parserRead.getJSonOut());
		
//		ArrayList<String> zbgCnpj = new ArrayList<String>();
//		zbgCnpj = parserRead.getZbgCnpj();
//		
//		for(String z : zbgCnpj) {
//			System.out.println(z);
//		}
		
		
		
//	}


}


