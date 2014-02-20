package br.com.debugger3c.util;

import java.io.File;
import java.io.IOException;
//import java.util.regex.Matcher;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.CharacterData;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

public class ConfigXmlParser {
	
	private String jrePath;
	private String jarPath;
	private String portaTomcat;
	private String webSockMonitIp;
	private String webSockReadIp;
	private String webSockWriteIp;
	private String websockPlugin3cIp;
	private String websockReadFilePath;
	private String websockWriteFilePath;
	private String websockMonitFilePath;
	
	                              
	                              
	public ConfigXmlParser() {
		
		//String configXmlPath = "C:\\Users\\ronie.dias\\workspace\\debug\\WebContent\\WEB-INF\\debug-config.xml"; // local
		String configXmlPath = "C:\\apache-tomcat-7.0.47\\webapps\\debug\\WEB-INF\\debug-config.xml"; // deploy

		File fXmlFile = new File(configXmlPath);
		DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
		
		try {
			
			DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
			Document doc = dBuilder.parse(fXmlFile);
			doc.getDocumentElement().normalize();
			
			NodeList nodeList = doc.getElementsByTagName("debug-config");
			
			Element elementResource = (Element) nodeList.item(0);

			NodeList jrePathElement = elementResource.getElementsByTagName("jre-path");
			NodeList jarPathElement = elementResource.getElementsByTagName("jar-path");
			NodeList portaTomcatElement = elementResource.getElementsByTagName("porta-tomcat");
			NodeList webSockMonitIpElement = elementResource.getElementsByTagName("ip-websock-monit");
			NodeList webSockReadIpElement = elementResource.getElementsByTagName("ip-websock-read");
			NodeList webSockWriteIpElement = elementResource.getElementsByTagName("ip-websock-write");
			NodeList websockPlugin3cIpElement = elementResource.getElementsByTagName("ip-websock-plugin3c");
			NodeList websockReadFilePathElement = elementResource.getElementsByTagName("file-path-websock-read");
			NodeList websockWriteFilePathElement = elementResource.getElementsByTagName("file-path-websock-write");
			NodeList websockMonitFilePathElement = elementResource.getElementsByTagName("file-path-websock-monit");

				
			Element jrePathLine = (Element) jrePathElement.item(0);
			Element jarPathLine = (Element) jarPathElement.item(0);
			Element portaTomcatLine = (Element) portaTomcatElement.item(0);
			Element webSockMonitIpLine = (Element) webSockMonitIpElement.item(0);
			Element webSockReadIpLine = (Element) webSockReadIpElement.item(0);
			Element webSockWriteIpLine = (Element) webSockWriteIpElement.item(0);
			Element websockPlugin3cIpLine = (Element) websockPlugin3cIpElement.item(0);
			Element websockReadFilePathLine = (Element) websockReadFilePathElement.item(0);
			Element websockWriteFilePathLine = (Element) websockWriteFilePathElement.item(0);
			Element websockMonitFilePathLine = (Element) websockMonitFilePathElement.item(0);
			

			Node jrePathChild = jrePathLine.getFirstChild(); 
			Node jarPathChild = jarPathLine.getFirstChild();
			Node portaTomcatChild = portaTomcatLine.getFirstChild();
			Node webSockMonitIpChild = webSockMonitIpLine.getFirstChild();
			Node webSockReadIpChild = webSockReadIpLine.getFirstChild();
			Node webSockWriteIpChild = webSockWriteIpLine.getFirstChild();
			Node websockPlugin3cIpChild = websockPlugin3cIpLine.getFirstChild();
			Node websockReadFilePathChild = websockReadFilePathLine.getFirstChild();
			Node webSockWriteFilePathChild = websockWriteFilePathLine.getFirstChild();
			Node webSockMonitFilePathChild = websockMonitFilePathLine.getFirstChild();
			
			CharacterData characterDataJrePath = (CharacterData) jrePathChild;
			CharacterData characterDataJarPath = (CharacterData) jarPathChild;
			CharacterData characterDataPortaTomcat = (CharacterData) portaTomcatChild;
			CharacterData characterDataWebSockMonitIp = (CharacterData) webSockMonitIpChild;
			CharacterData characterDataWebSockReadIp = (CharacterData) webSockReadIpChild;
			CharacterData characterDataWebsockWriteIp = (CharacterData) webSockWriteIpChild;
			CharacterData characterDataWebsockPlugin3cIp = (CharacterData) websockPlugin3cIpChild;
			CharacterData characterDataWebsockReadFilePath = (CharacterData) websockReadFilePathChild;
			CharacterData characterDataWebsockWriteFilePath = (CharacterData) webSockWriteFilePathChild;
			CharacterData characterDataWebsockMonitFilePath = (CharacterData) webSockMonitFilePathChild;

			this.jrePath = characterDataJrePath.getNodeValue();
			this.jarPath = characterDataJarPath.getNodeValue();
			this.portaTomcat = characterDataPortaTomcat.getNodeValue();
			this.webSockMonitIp = characterDataWebSockMonitIp.getNodeValue();
			this.webSockReadIp = characterDataWebSockReadIp.getNodeValue();
			this.webSockWriteIp = characterDataWebsockWriteIp.getNodeValue();
			this.websockPlugin3cIp = characterDataWebsockPlugin3cIp.getNodeValue();
			this.websockReadFilePath = characterDataWebsockReadFilePath.getNodeValue();
			this.websockWriteFilePath = characterDataWebsockWriteFilePath.getNodeValue();
			this.websockMonitFilePath = characterDataWebsockMonitFilePath.getNodeValue();
			
		}
		catch (ParserConfigurationException | SAXException | IOException e) {
			e.printStackTrace();
		}
		
	}

	
	// Getters

	
	public String getJrePath() {
		return jrePath;
	}


	public String getJarPath() {
		return jarPath;
	}


	public String getPortaTomcat() {
		return portaTomcat;
	}


	public String getWebSockMonitIp() {
		return webSockMonitIp;
	}


	public String getWebSockReadIp() {
		return webSockReadIp;
	}


	public String getWebSockWriteIp() {
		return webSockWriteIp;
	}


	public String getWebsockPlugin3cIp() {
		return websockPlugin3cIp;
	}
	
	
	public String getWebsockReadFilePath() {
		return websockReadFilePath;
	}
	
	
	public String getWebsockWriteFilePath() {
		return websockWriteFilePath;
	}
	
	
	public String getWebsockMonitFilePath() {
		return websockMonitFilePath;
	}


	
	
	
//	public static void main(String[] args) {
//			
//		ConfigXmlParser cxp = new ConfigXmlParser();
//		
//		System.out.println(cxp.getJrePath().replaceAll("\\\\", Matcher.quoteReplacement("\\\\")));
//		System.out.println(cxp.getJarPath().replaceAll("\\\\", Matcher.quoteReplacement("\\\\")));
//		System.out.println(cxp.getPortaTomcat());
//		System.out.println(cxp.getWebSockMonitIp());
//		System.out.println(cxp.getWebSockReadIp());
//		System.out.println(cxp.getWebSockWriteIp());
//		System.out.println(cxp.getWebsockPlugin3cIp());
//		System.out.println(cxp.getWebsockReadFilePath().replaceAll("\\\\", Matcher.quoteReplacement("\\\\")));
//		System.out.println(cxp.getWebsockWriteFilePath().replaceAll("\\\\", Matcher.quoteReplacement("\\\\")));
//		System.out.println(cxp.getWebsockMonitFilePath().replaceAll("\\\\", Matcher.quoteReplacement("\\\\")));
//		
//		
//	}
	

}
