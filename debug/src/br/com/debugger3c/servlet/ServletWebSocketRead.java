package br.com.debugger3c.servlet;

import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.util.ArrayList;
import java.util.regex.Matcher;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;

import org.apache.catalina.websocket.StreamInbound;
import org.apache.catalina.websocket.WebSocketServlet;
import org.apache.catalina.websocket.MessageInbound;
import org.apache.catalina.websocket.WsOutbound;

import br.com.debugger3c.util.ConfigXmlParser;


@WebServlet(urlPatterns="/webSockRead")
public class ServletWebSocketRead extends WebSocketServlet {

	private static final long serialVersionUID = 1L;
	
	private static ArrayList<MsgInbound> msgInboundList = new ArrayList<MsgInbound>();
	
	private ConfigXmlParser cxp;
	private static String FILE_PATH;
	private static File FILE; 
	private long length;
	private long pointer;
	private String webSockReadIp;
	

	
	public ServletWebSocketRead() {
		
		cxp = new ConfigXmlParser();
		FILE_PATH = cxp.getWebsockReadFilePath().replaceAll("\\\\", Matcher.quoteReplacement("\\\\"));
		FILE = new File(FILE_PATH);
		pointer = FILE.length();
		webSockReadIp = cxp.getWebSockReadIp();

		new Thread(new Runnable() {

			public void run() {
		
				while(true) {
					
					length = FILE.length();
					
					if(length < pointer) {
						
						// Envia para todos que est�o conectados, a informa��o de que o arquivo de log foi truncado
						sendStrToAll("*** Arquivo de log truncado ***");
						pointer = FILE.length();
		
					}
					else if(length > pointer) {
						
				        RandomAccessFile raf;
					       
						try {
								
							raf = new RandomAccessFile(FILE, "r");
							raf.seek(pointer);
								
							String line;
								
							while ((line = raf.readLine()) != null) {
								sendStrToAll(line);
							}
		
							pointer = raf.getFilePointer();
					        raf.close();
								
								
						} catch (Exception e) {
							e.printStackTrace();
						}
		
						
					}
					
					
					try {
						Thread.sleep(500);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
					
				}
		
			}
		}).start();

		
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
				outbound.writeTextMessage(CharBuffer.wrap("*** GERACAO DE LOG INICIADA. SERVIDOR: " + webSockReadIp + " ***"));
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








