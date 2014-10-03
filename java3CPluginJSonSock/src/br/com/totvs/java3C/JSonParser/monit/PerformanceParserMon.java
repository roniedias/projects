package br.com.totvs.java3C.JSonParser.monit;


import org.json.JSONException;
import org.json.JSONObject;

import br.com.totvs.java3C.factory.TcpConnFactory;


public class PerformanceParserMon {
	
	JSONObject jsonObject;
  
    private String totaltime;
    private String timedescription;
    private String status;
    private String message;
    private String elaptime;
	
	private boolean isTcpSrvConn; 
	private String tcpSrvReturn;
	
	
	public PerformanceParserMon(String ip, String porta, String ambiente, String empresaFilial) {
		
		String performanceConnStr = "PERFORMANCE#" + ip + "#" + porta + "#" + ambiente + "#" + empresaFilial + "#\0";
		
		TcpConnFactory tcpConnFactory = new TcpConnFactory(performanceConnStr, "tcpSrvMonit");
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

            totaltime       = jsonObject.getString("TOTALTIME");
        	timedescription = jsonObject.getString("TIMEDESCRIPTION");
        	status          = jsonObject.getString("STATUS");
        	message         = jsonObject.getString("MESSAGE");
        	elaptime        = jsonObject.getString("ELAPTIME");
        	
        	        	
		} catch (JSONException e) {
			e.printStackTrace();
		}	
		
	}


	public String getTotaltime() {
		return totaltime;
	}

	public String getTimedescription() {
		return timedescription;
	}

	public String getStatus() {
		return status;
	}

	public String getMessage() {
		return message;
	}

	public String getElaptime() {
		return elaptime;
	}

	
	
//	public static void main(String[] args) {
//		PerformanceParcerMon performanceParserMon = new PerformanceParcerMon("172.16.93.100", "7001", "MAXIME", "00/01"); 
//		System.out.println(performanceParserMon.getTotaltime());
//		System.out.println(performanceParserMon.getTimedescription());
//		System.out.println(performanceParserMon.getStatus());
//		System.out.println(performanceParserMon.getMessage());
//		System.out.println(performanceParserMon.getElaptime());
//	}
	

}

