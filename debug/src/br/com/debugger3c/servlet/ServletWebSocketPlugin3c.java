package br.com.debugger3c.servlet;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.util.ArrayList;
import java.util.regex.Matcher;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.catalina.websocket.MessageInbound;
import org.apache.catalina.websocket.StreamInbound;
import org.apache.catalina.websocket.WebSocketServlet;
import org.apache.catalina.websocket.WsOutbound;

import br.com.debugger3c.util.ConfigXmlParser;



@WebServlet(urlPatterns="/websockPlugin3c")
public class ServletWebSocketPlugin3c extends WebSocketServlet {
	
	private static final long serialVersionUID = 1L;	
	private static ArrayList<MsgInbound> msgInboundList = new ArrayList<MsgInbound>();
	
	private String codigoCliente;
	private String codigoEmpresa;
	private String codigoAmbiente;
	private String codigoTipoAmb;
	private String codTipoServico;
	private String codigoProduto;
	
	private static String PATH_TO_JAVA;
	private static String JAR_FILE;
	
	private String websockPlugin3cIp;

	
	public ServletWebSocketPlugin3c() {
		
				
		ConfigXmlParser cxp = new ConfigXmlParser();
		
		PATH_TO_JAVA = cxp.getJrePath().replaceAll("\\\\", Matcher.quoteReplacement("\\\\"));
		JAR_FILE = cxp.getJarPath().replaceAll("\\\\", Matcher.quoteReplacement("\\\\"));
		
		websockPlugin3cIp = cxp.getWebsockPlugin3cIp();
		
	}
	
	
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
    {
		
		codigoCliente = request.getParameter("codigoCliente");
	    codigoEmpresa = request.getParameter("codigoEmpresa");
		codigoAmbiente = request.getParameter("codigoAmbiente");
		codigoTipoAmb = request.getParameter("codigoTipoAmb");
		codTipoServico = request.getParameter("codTipoServico");
		codigoProduto = request.getParameter("codigoProduto");
		
		executaJarFile();
		
		
    }
	
	
	
	private void executaJarFile() {		
				
		try {
				    					
			new Thread(new Runnable() {
				
				public void run() {
							
							
				    try {
					    
					    	
					    String s = null;
		
								
						String[] comando = {PATH_TO_JAVA, "-jar", JAR_FILE, codigoCliente, codigoEmpresa, codigoAmbiente, codigoTipoAmb, codTipoServico, codigoProduto};
								
						Runtime rt = Runtime.getRuntime();
						Process proc = null;
							
						proc = rt.exec(comando);
								
						BufferedReader stdInput = new BufferedReader(new InputStreamReader(proc.getInputStream()));
						
					    BufferedReader stdError = new BufferedReader(new InputStreamReader(proc.getErrorStream()));
		
					    // lê a saída do comando (Saida padrao do comando)				    
					    while ((s = stdInput.readLine()) != null) {
					    	sendStrToAll(s);
					    }
							    
					    // lê erros a partir do comando executado (Erro padrao do comando, se houver)
					    while ((s = stdError.readLine()) != null) {
					    	sendStrToAll(s);
					    }		
							
				    }
				    catch (IOException e) {
				    	e.printStackTrace();
				    }	
				   
					    
				}
			}).start();


		}
		catch(NullPointerException e) {
		//System.out.println("Valores vazios");
		}
			
		
	}
	
	
	
	
	
	private void sendStrToAll(String str) {
		for(MsgInbound m : msgInboundList) {
			CharBuffer buffer = CharBuffer.wrap(str);
			try {
				m.instanceOutbound.writeTextMessage(buffer);
				m.instanceOutbound.flush();
			} catch (IOException e) {
				e.printStackTrace();
			}
			
		}
	}
			
		
	

	protected StreamInbound createWebSocketInbound(String arg0, HttpServletRequest arg1) {
		return new MsgInbound();
	}
	
	
	private class MsgInbound extends MessageInbound {
		
		
		WsOutbound instanceOutbound;
		
		public void onOpen(WsOutbound outbound) {

			System.out.println("Conexao cliente efetuada.");

			this.instanceOutbound = outbound;
			msgInboundList.add(this);
			try {
				outbound.writeTextMessage(CharBuffer.wrap("*** GERACAO DE LOG INICIADA. SERVIDOR: " + websockPlugin3cIp + " ***"));
			}
			catch(IOException e) {
				e.printStackTrace();
			}			
		}
		
		
		public void onClose(int status) {
			System.out.println("Conexao cliente fechada.");
			msgInboundList.remove(this);
		}
		
		


		protected void onTextMessage(CharBuffer cb) throws IOException {

//			for(MsgInbound m : msgInboundList) {
//				 CharBuffer buffer = CharBuffer.wrap(cb);
//				 m.instanceOutbound.writeTextMessage(buffer);
//				 m.instanceOutbound.flush();		 
		}


		
		protected void onBinaryMessage(ByteBuffer arg0) throws IOException {	
		}


		
	}

	



}
