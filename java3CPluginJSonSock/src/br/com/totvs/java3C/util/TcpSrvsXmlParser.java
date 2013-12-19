package br.com.totvs.java3C.util;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.w3c.dom.CharacterData;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;
import org.xml.sax.SAXParseException;

import br.com.totvs.java3C.bean.TcpServer;

public class TcpSrvsXmlParser {
				
	private ArrayList<TcpServer> tcpServers = new ArrayList<TcpServer>();
	
	
	private File fXmlFile;
	DocumentBuilderFactory dbFactory;
    DocumentBuilder dBuilder;
    Document doc;
   
	private String tcpConnAttempts;
	
	
	public TcpSrvsXmlParser(String xmlFullPath, String nodeName) { // tcpSrvMonit, tcpSrvRead, tcpSrvWrite
		
		try {
		
			fXmlFile = new File(xmlFullPath);
			dbFactory = DocumentBuilderFactory.newInstance();
	        dBuilder = dbFactory.newDocumentBuilder();
	        doc = dBuilder.parse(fXmlFile);
	        doc.getDocumentElement().normalize();
	        
			NodeList tcpSrvNodes = doc.getElementsByTagName(nodeName);
			
			for(int i = 0; i < tcpSrvNodes.getLength(); i++) {
				
				Element elementResource = (Element) tcpSrvNodes.item(i);
				
				NodeList ip = elementResource.getElementsByTagName("ip");
				NodeList port = elementResource.getElementsByTagName("port");
				NodeList active = elementResource.getElementsByTagName("active");
				
									
				Element ipLine = (Element) ip.item(0); 
				Element portLine = (Element) port.item(0);
				Element activeLine = (Element) active.item(0);
				
				Node ipChild = ipLine.getFirstChild(); 
				Node portChild = portLine.getFirstChild();
				Node activeChild = activeLine.getFirstChild();
				
				CharacterData characterDataIp = (CharacterData) ipChild;
				CharacterData characterDataPort = (CharacterData) portChild;
				CharacterData characterDataActive = (CharacterData) activeChild;
				
				if(characterDataActive.getNodeValue().equals("1")) 
					tcpServers.add(new TcpServer(characterDataIp.getNodeValue(), Integer.valueOf(characterDataPort.getNodeValue())));					
			}
			
			
			this.tcpConnAttempts = setTcpConnAtempts(nodeName);
			
	        
		}
		catch (IOException ioError) {
			System.out.println("Erro ao acessar o arquivo XML de configuracao dos servidores TCP");
		}
		catch (SAXParseException err) {
	        System.out.println ("Erro de Parsing" + ", linha " + err.getLineNumber() + ", uri " + err.getSystemId());
		}
		catch (SAXException e) {
	        Exception x = e.getException ();
	        ((x == null) ? e : x).printStackTrace ();
		}
		catch (Throwable t) {
	        t.printStackTrace ();
	    }
		
	}
	
	
	// Função auxiliar 
	
	private String setTcpConnAtempts(String nodeName) {
		
		if(nodeName.equals("tcpSrvMonit")) {
			
			NodeList tcpSrvNodes = doc.getElementsByTagName("tcpMonitConnAtempts");
			Element elementResource = (Element) tcpSrvNodes.item(0);
			NodeList attempts = elementResource.getElementsByTagName("attempts");
			Element ipLine = (Element) attempts.item(0); 
			Node attemptsChild = ipLine.getFirstChild(); 			
			CharacterData characterDataAttempts = (CharacterData) attemptsChild;
			return characterDataAttempts.getNodeValue();
			
		}
		else if(nodeName.equals("tcpSrvRead")) {			NodeList tcpSrvNodes = doc.getElementsByTagName("tcpReadConnAtempts");
			Element elementResource = (Element) tcpSrvNodes.item(0);
			NodeList attempts = elementResource.getElementsByTagName("attempts");
			Element ipLine = (Element) attempts.item(0); 
			Node attemptsChild = ipLine.getFirstChild(); 			
			CharacterData characterDataAttempts = (CharacterData) attemptsChild;
			return characterDataAttempts.getNodeValue();
		}
		else if(nodeName.equals("tcpSrvWrite")) {			NodeList tcpSrvNodes = doc.getElementsByTagName("tcpWriteConnAtempts");
			Element elementResource = (Element) tcpSrvNodes.item(0);
			NodeList attempts = elementResource.getElementsByTagName("attempts");
			Element ipLine = (Element) attempts.item(0); 
			Node attemptsChild = ipLine.getFirstChild(); 			
			CharacterData characterDataAttempts = (CharacterData) attemptsChild;
			return characterDataAttempts.getNodeValue();
		}
		
		else {
			return "Nao foi possivel obter o numero de re-tentativas de conexao com o servidor TCP.";
		}
	}
	
	
	
	
	// Getters
	
	public ArrayList<TcpServer> getTcpServers() {
		return tcpServers;
	}
	
	public String getTcpConnAtempts() {
		return tcpConnAttempts;
	}
	
	
	
	
	

	
	
	//	public static void main(String[] args) {	//		TcpSrvsXmlParser tsxp = new TcpSrvsXmlParser("src\\br\\com\\totvs\\java3C\\util\\tcpServers.xml", "tcpSrvMonit");//		ArrayList<TcpServer> tcpServers = tsxp.getTcpServers();				//		for(TcpServer m : tcpServers) {//			System.out.println(m.getIp());//			System.out.println(m.getPort());//			System.out.println("===================");//		}
		
//		System.out.println(tsxp.getTcpConnAtempts());				//	}
	
	

}
