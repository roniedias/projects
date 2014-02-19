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
	
	private String jdkPath;
	private String jarPath;
	private String portaTomcat;
	private String webSockMonitIp;
	private String webSockReadIp;
	private String webSockWriteIp;
	private String websockPlugin3cIp;
	
	                              
	                              
	public ConfigXmlParser(String configXmlPath) {

		File fXmlFile = new File(configXmlPath);
		DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
		
		try {
			
			DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
			Document doc = dBuilder.parse(fXmlFile);
			doc.getDocumentElement().normalize();
			
			NodeList nodeList = doc.getElementsByTagName("debug-config");
			
			Element elementResource = (Element) nodeList.item(0);

			NodeList jdkPathElement = elementResource.getElementsByTagName("jdk-path");
			NodeList jarPathElement = elementResource.getElementsByTagName("jar-path");
			NodeList portaTomcatElement = elementResource.getElementsByTagName("porta-tomcat");
			NodeList webSockMonitIpElement = elementResource.getElementsByTagName("ip-websock-monit");
			NodeList webSockReadIpElement = elementResource.getElementsByTagName("ip-websock-read");
			NodeList webSockWriteIpElement = elementResource.getElementsByTagName("ip-websock-write");
			NodeList websockPlugin3cIpElement = elementResource.getElementsByTagName("ip-websock-plugin3c");
				
			Element jdkPathLine = (Element) jdkPathElement.item(0);
			Element jarPathLine = (Element) jarPathElement.item(0);
			Element portaTomcatLine = (Element) portaTomcatElement.item(0);
			Element webSockMonitIpLine = (Element) webSockMonitIpElement.item(0);
			Element webSockReadIpLine = (Element) webSockReadIpElement.item(0);
			Element webSockWriteIpLine = (Element) webSockWriteIpElement.item(0);
			Element websockPlugin3cIpLine = (Element) websockPlugin3cIpElement.item(0);

			Node jdkPathChild = jdkPathLine.getFirstChild(); 
			Node jarPathChild = jarPathLine.getFirstChild();
			Node portaTomcatChild = portaTomcatLine.getFirstChild();
			Node webSockMonitIpChild = webSockMonitIpLine.getFirstChild();
			Node webSockReadIpChild = webSockReadIpLine.getFirstChild();
			Node webSockWriteIpChild = webSockWriteIpLine.getFirstChild();
			Node websockPlugin3cIpChild = websockPlugin3cIpLine.getFirstChild();
			
			CharacterData characterDataJdkPath = (CharacterData) jdkPathChild;
			CharacterData characterDataJarPath = (CharacterData) jarPathChild;
			CharacterData characterDataPortaTomcat = (CharacterData) portaTomcatChild;
			CharacterData characterDataWebSockMonitIp = (CharacterData) webSockMonitIpChild;
			CharacterData characterDataWebSockReadIp = (CharacterData) webSockReadIpChild;
			CharacterData characterDataWebsockWriteIp = (CharacterData) webSockWriteIpChild;
			CharacterData characterDataWebsockPlugin3cIp = (CharacterData) websockPlugin3cIpChild;

			this.jdkPath = characterDataJdkPath.getNodeValue();
			this.jarPath = characterDataJarPath.getNodeValue();
			this.portaTomcat = characterDataPortaTomcat.getNodeValue();
			this.webSockMonitIp = characterDataWebSockMonitIp.getNodeValue();
			this.webSockReadIp = characterDataWebSockReadIp.getNodeValue();
			this.webSockWriteIp = characterDataWebsockWriteIp.getNodeValue();
			this.websockPlugin3cIp = characterDataWebsockPlugin3cIp.getNodeValue();
			
		}
		catch (ParserConfigurationException | SAXException | IOException e) {
			e.printStackTrace();
		}
		
	}

	
	// Getters

	
	public String getJdkPath() {
		return jdkPath;
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
	
	
	
	
//	public static void main(String[] args) {
//			
//		ConfigXmlParser cxp = new ConfigXmlParser("C:\\Users\\ronie.dias\\workspace\\debug\\WebContent\\WEB-INF\\config.xml");
//		
//		System.out.println(cxp.getJdkPath().replaceAll("\\\\", Matcher.quoteReplacement("\\\\")));
//		System.out.println(cxp.getJarPath().replaceAll("\\\\", Matcher.quoteReplacement("\\\\")));
//		System.out.println(cxp.getPortaTomcat());
//		System.out.println(cxp.getWebSockMonitIp());
//		System.out.println(cxp.getWebSockReadIp());
//		System.out.println(cxp.getWebSockWriteIp());
//		System.out.println(cxp.getWebsockPlugin3cIp());
//		
//		
//	}
	

}
