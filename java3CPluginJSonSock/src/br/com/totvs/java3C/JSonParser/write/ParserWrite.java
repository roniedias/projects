package br.com.totvs.java3C.JSonParser.write;


import org.json.JSONException;
import org.json.JSONObject;

import br.com.totvs.java3C.factory.TcpConnFactory;


public class ParserWrite {
	
	
	private String status;
	private String message;
	private String limiar;
		
	private boolean isTcpSrvConn; 
	private String tcpSrvReturn;


	
	public ParserWrite(String nomeOperacao, String zcaCodAmb, String zcaTipAmb, String zcaItem, String zcaParam, String zcaResult, String zcaMemo) {
		
		
		String writeStr = nomeOperacao + "#" + zcaCodAmb + "#" + zcaTipAmb + "#" + zcaItem + "#" + zcaParam + "#" + zcaResult + "#" + zcaMemo + "#\0";
		
		TcpConnFactory tcpConnFactory = new TcpConnFactory(writeStr, "tcpSrvWrite");
		tcpConnFactory.execute();
		isTcpSrvConn = tcpConnFactory.tcpSrvConn();
		tcpSrvReturn = tcpConnFactory.getTcpSrvReturn();
		
		
		if(!isTcpSrvConn) {
			System.out.println("Nao foi possível conectar-se ao servico TCP de gravacao.");
			System.exit(3);
		}
		
		
	}
	
	
	
	public String getStatus() {
		try {
			JSONObject jsonObject = new JSONObject(tcpSrvReturn);	
			status = jsonObject.getString("STATUS");
		}
    	catch(NullPointerException e) {
    		System.out.println("Nao foi possivel obter resposta JSON a partir do TCP de gravacao (null).");
    		System.exit(3);
    	}
		catch(JSONException j) {
			System.out.println("JSON invalido, retornado pelo servico TCP de gravacao: " + tcpSrvReturn);
			System.exit(3);
		}
		return status;
	}
	
	
	
	public String getMessage() {
		try {
			JSONObject jsonObject = new JSONObject(tcpSrvReturn);	
			message = jsonObject.getString("MESSAGE");
		}
    	catch(NullPointerException e) {
    		System.out.println("Nao foi possivel obter resposta JSON a partir do TCP de gravacao (null).");
    		System.exit(3);
    	}
		catch(JSONException j) {
			System.out.println("JSON invalido, retornado pelo servico TCP de gravacao: " + tcpSrvReturn);
			System.exit(3);
		}
		return message;
	}
	
	
	
	public String getLimiar() {
		try {
			JSONObject jsonObject = new JSONObject(tcpSrvReturn);	
			limiar = jsonObject.getString("LIMIAR");
		}
    	catch(NullPointerException e) {
    		System.out.println("Nao foi possivel obter resposta JSON a partir do TCP de gravacao (null).");
    		System.exit(3);
    	}
		catch(JSONException j) {
			System.out.println("JSON invalido, retornado pelo servico TCP de gravacao: " + tcpSrvReturn);
			System.exit(3);
		}
		return limiar;
	}
	
	
	public String getTcpSrvReturn() {
		return this.tcpSrvReturn;
	}




	
	
//	public static void main(String[] args) {
//		
//		
//		ParserWrite parserWrite = new ParserWrite("LICENSEINFO_WRITE", "000051", "01", "001", "045", "97.0", "Validade do TOTVSLIC.KEY obtida com sucesso.");
//		
//		System.out.println("tcpSrvReturn: " + parserWrite.getTcpSrvReturn());
//		
//		System.out.println("Limiar: " + parserWrite.getLimiar());
//		System.out.println("Status: " + parserWrite.getStatus());
//		System.out.println("Message: " + parserWrite.getMessage());
//	}
	
	
//	public static void main(String[] args) {
//	
//
//	String zcaMemo = "Environment: KPMG_PRODUCAO, porta: 10301 = 6; Environment: KPMG_PRODUCAO, porta: 10302 = 6; Environment: KPMG_PRODUCAO, porta: 10303 = 7; Environment: KPMG_PRODUCAO, porta: 10304 = 6; Environment: KPMG_PRODUCAO_COMP, porta: 10318 = 0;";
//	
//	ParserWriteActiveConn parserWrite = new ParserWriteActiveConn("ACTIVECONNECTIONS_WRITE", "000051", "001", "01", "025", "26.0", zcaMemo);
//}

	

}
