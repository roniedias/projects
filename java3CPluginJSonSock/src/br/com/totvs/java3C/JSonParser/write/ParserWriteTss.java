package br.com.totvs.java3C.JSonParser.write;


import org.json.JSONException;
import org.json.JSONObject;

import br.com.totvs.java3C.bean.ParamQtdNfeCnpj;
import br.com.totvs.java3C.factory.TcpConnFactory;
import br.com.totvs.java3C.util.QtdNfeCnpjsStrMaker;


public class ParserWriteTss {
	
	
	private String status;
	private String message;
	private String limiar;
		
	private boolean isTcpSrvConn; 
	private String tcpSrvReturn;


	
	public ParserWriteTss(String nomeOperacao, String zcaCodAmb, String zcaTipAmb, String zcaItem, String zcaParam, String zcaResult, String zcaMemo, ParamQtdNfeCnpj[] qtdNfeCnpj) {

		QtdNfeCnpjsStrMaker qncsm = new QtdNfeCnpjsStrMaker(qtdNfeCnpj);
		String qtdNfeCnpjStr = qncsm.getQtdNfeCnpjStr();
		
		String writeStr = nomeOperacao + "#" + zcaCodAmb + "#" + zcaTipAmb + "#" + zcaItem + "#" + zcaParam + "#" + zcaResult + "#" + zcaMemo + "#" + qtdNfeCnpjStr + "#\0";
		
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

	
}


