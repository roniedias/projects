package br.com.totvs.java3C.util;

import java.util.ArrayList;
import java.util.Random;

import br.com.totvs.java3C.bean.TcpServer;

public class RandomTcpSrv {
	
	private TcpServer tcpServer;	
	private String ip;
	private int porta; 
	private int attempts;
	
	
		
	private Random geradorAleatorioTcpSrv;
	
	
	public RandomTcpSrv(String nodeName) {
		
		geradorAleatorioTcpSrv = new Random();
		
//		TcpSrvsXmlParser tsxp = new TcpSrvsXmlParser("src\\br\\com\\totvs\\java3C\\util\\tcpServersTst.xml", nodeName); 	
		TcpSrvsXmlParser tsxp = new TcpSrvsXmlParser("C:\\apache-tomcat-7.0.47\\webapps\\debug\\WEB-INF\\tcpServers.xml", nodeName); // Configuração para o debug, nos servidores 172.18.0.149 e 150
				
	
		
		
		// Producao
//		TcpSrvsXmlParser tsxp = new TcpSrvsXmlParser("/usr/lib/nagios/plugins/tcpServers.xml", nodeName); // Configuração para deploy nos pollers

		
		// Nao em funcionamento hoje
//		String currentDir = System.getProperty("user.dir"); // Obtém o diretório atual
//		TcpSrvsXmlParser tsxp = new TcpSrvsXmlParser(currentDir + "/tcpServers.xml", nodeName);
		
		
		
		
		ArrayList<TcpServer> tcpServers = tsxp.getTcpServers();
		
		this.attempts = Integer.parseInt(tsxp.getTcpConnAtempts());
		
		int indice = geradorAleatorioTcpSrv.nextInt(tcpServers.size());
		this.tcpServer = tcpServers.get(indice);
		this.ip = tcpServer.getIp();
		this.porta = tcpServer.getPort();
		
	}
	
	
	public TcpServer getTcpServer() {
		return this.tcpServer;
	}
	
	public String getIp() {
		return this.ip;
	}
	
	public int getPorta() {
		return this.porta;
	}
	
	public int getAttempts() {
		return this.attempts;
	}
	
	

		
	
//	public static void main(String[] args) {
//		RandomTcpSrv rts = new RandomTcpSrv();
//		TcpServer tcpServer = rts.getTcpServer();
//		System.out.println("Ip: " + tcpServer.getIp());
//		System.out.println("Porta: " + tcpServer.getPort());
//	}

	

}
