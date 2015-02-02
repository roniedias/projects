package br.com.totvs.java3C.fluig.util;

import java.io.IOException;

import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.BasicResponseHandler;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONException;
import org.json.JSONObject;



import br.com.totvs.java3C.fluig.bean.FluigAPIResults;
import br.com.totvs.java3C.fluig.bean.ItensAmbienteFluig;

public class FluigAPI {
	
	private JSONObject jsonObject;
	private String goodDataAvailability;
	private String memcachedAvailability;
	private String openOfficeServerAvailability;
	private String solrServerAvailability;
	private String identityAvailability;
	private String databaseAvailability;
	private String licenseServerAvailability;
	
	private FluigAPIResults fluigAPIResults;
	private String result;
	
	
	// Construtor para obter-se todos os resultados retornados pela API, após sua execução
	public FluigAPI(ItensAmbienteFluig itensAmbienteFluig) {
		
		if(itensAmbienteFluig.getModalidade().equals("C")) { // Se o Fluig for "Compartilhado"
			
			HttpClient httpClient = new DefaultHttpClient();
			
			try {
	        	HttpGet httpGet = new HttpGet("http://" + itensAmbienteFluig.getIp() + ":" + itensAmbienteFluig.getPortaWeb() + "/monitor/report");
	            ResponseHandler<String> responseHandler = new BasicResponseHandler();
	            String responseBody;
				responseBody = httpClient.execute(httpGet, responseHandler);
				
				try {
	        		jsonObject = new JSONObject(responseBody);
	        		goodDataAvailability  = (String) jsonObject.getString("goodDataAvailability").trim();
	        		memcachedAvailability = (String) jsonObject.getString("memcachedAvailability").trim();
	        		openOfficeServerAvailability = (String) jsonObject.getString("openOfficeServerAvailability").trim();
	        		solrServerAvailability = (String) jsonObject.getString("solrServerAvailability").trim();
	        		identityAvailability = (String) jsonObject.getString("identityAvailability").trim();
	        		databaseAvailability = (String) jsonObject.getString("databaseAvailability").trim();
	        		licenseServerAvailability = (String) jsonObject.getString("licenseServerAvailability").trim();
	        		
	        		fluigAPIResults = new FluigAPIResults(goodDataAvailability, memcachedAvailability, openOfficeServerAvailability, solrServerAvailability, identityAvailability, databaseAvailability, licenseServerAvailability);	        		
	        	}
	        	catch(NullPointerException e) {
	        		System.out.println("Nao foi possivel obter resposta JSON a partir do Fluig (null).");
	        		System.exit(3);
	        	}
	        	catch(JSONException j) {
	        		System.out.println("JSON invalido: " + responseBody);
	        		System.exit(3);
	        	}
				
			}
			catch (ClientProtocolException e) {
				e.printStackTrace();
			} 
			catch (IOException e) {
				e.printStackTrace();
			}
			finally {
				httpClient.getConnectionManager().shutdown();
			}
				
		}		
		
	}
	
	
	// Construtor para obter-se apenas um resultado em específico, passado como parâmetro (tipo)	
	public FluigAPI(ItensAmbienteFluig itensAmbienteFluig, String tipo) {

		if(itensAmbienteFluig.getModalidade().equals("C")) { // Se o Fluig for "Compartilhado"
			
			HttpClient httpClient = new DefaultHttpClient();
			
			try {
				String urlMonit = "http://" + itensAmbienteFluig.getIp() + ":" + itensAmbienteFluig.getPortaWeb() + "/monitor/report/" + tipo;
				HttpGet httpGet = new HttpGet(urlMonit);
	            ResponseHandler<String> responseHandler = new BasicResponseHandler();
	            String responseBody;
				responseBody = httpClient.execute(httpGet, responseHandler);
				
				try {
	        		jsonObject = new JSONObject(responseBody);
	        		result = (String) jsonObject.getString(tipo).trim();	        		
	        	}
	        	catch(NullPointerException e) {
	        		System.out.println("Nao foi possivel obter resposta JSON a partir do Fluig (null).");
	        		System.exit(3);
	        	}
	        	catch(JSONException j) {
	        		System.out.println("JSON invalido: " + responseBody);
	        		System.exit(3);
	        	}
				
			}
			catch (ClientProtocolException e) {
				e.printStackTrace();
			} 
			catch (IOException e) {
				e.printStackTrace();
			}
			finally {
				httpClient.getConnectionManager().shutdown();
			}
				
		}		
		
	}
	
		
	
	public FluigAPIResults getFluigAPIResults() {
		return fluigAPIResults;
	}
	
	public String getResult() {
		return result;
	}

	

}

