package br.com.debugger3c.servlet;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.util.ArrayList;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.catalina.websocket.MessageInbound;
import org.apache.catalina.websocket.StreamInbound;
import org.apache.catalina.websocket.WebSocketServlet;
import org.apache.catalina.websocket.WsOutbound;



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
	
	
//	private static final String PATH_TO_JAVA = "C:\\Program Files\\Java\\jdk1.7.0_17\\jre\\bin\\java.exe"; // localhost
//	private static final String PATH_TO_JAVA = "C:\\Program Files (x86)\\Java\\jre7\\bin\\java.exe"; // 172.18.0.150
	private static final String PATH_TO_JAVA = "C:\\Program Files\\Java\\jdk1.7.0_45\\jre\\bin\\java.exe"; // 172.18.0.149
//	private static final String PATH_TO_JAVA = "C:\\Program Files\\Java\\jdk1.7.0_51\\jre\\bin\\java.exe"; // 172.18.0.148 e 219

//	private static final String JAR_FILE = "C:\\Users\\ronie.dias\\workspace\\debug\\WebContent\\WEB-INF\\lib\\3CNagiosPlugin.jar"; // localhost
	private static final String JAR_FILE = "C:\\apache-tomcat-7.0.47\\webapps\\debug\\WEB-INF\\lib\\3CNagiosPlugin.jar"; // Servers 172.18.0.149 e 150


	
	
	
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
		
			if(!(codigoCliente.equals("vazio") || codigoEmpresa.equals("vazio") || codigoAmbiente.equals("vazio") || codigoTipoAmb.equals("vazio") || codTipoServico.equals("vazio") || codigoProduto.equals("vazio"))) {
				    	
						
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
				outbound.writeTextMessage(CharBuffer.wrap("*** GERACAO DE LOG INICIADA ***"));
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
