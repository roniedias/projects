package br.com.breezeReport.test;


//import java.sql.Connection;
//import java.sql.DriverManager;
//import java.sql.SQLException;
import java.util.ArrayList;


import br.com.breezeReport.dao.OnLineDaoDS;
import br.com.breezeReport.model.FixedRoom;
import br.com.breezeReport.model.FixedRoomHost;


public class ImprimeHostsFixedRoom {
	
	private OnLineDaoDS onLineDaoDS;
	
		
//	public Connection getBreezeConnection() {
//		
//		try {
//				try {
//					Class.forName("com.mysql.jdbc.Driver");
//				} catch (ClassNotFoundException e) {
//					e.printStackTrace();
//				}
//			return DriverManager.getConnection("jdbc:sqlserver://172.16.90.89:1433;user=connect;databaseName=breeze;password=totvs");
//	    } 
//		catch (SQLException e) {
//			throw new RuntimeException(e);
//	    }
//		
//	}

	
	
	
	public ImprimeHostsFixedRoom() {
		this.onLineDaoDS = new OnLineDaoDS();		
	}
	
	
	public void go() {

		ArrayList<FixedRoom> fixedRooms = new ArrayList<FixedRoom>();
		ArrayList<FixedRoomHost> fixedRoomHosts = new ArrayList<FixedRoomHost>();
		
		fixedRooms = onLineDaoDS.getFixedRooms(); // Obtem as informações das salas fixas
		
		for(FixedRoom f : fixedRooms) {
			
			fixedRoomHosts = onLineDaoDS.getFixedRoomHosts(f.getScoId());
			
			System.out.println("**************************************");
			System.out.println("Sala: " + f.getNome());
			System.out.println("**************************************");
			
			for(FixedRoomHost frh : fixedRoomHosts) {
				System.out.println("Id: " + frh.getId());
				System.out.println("Login: " + frh.getLogin());
				System.out.println("Primeiro nome: " + frh.getPrimeiroNome());
				System.out.println("Sobrenome: " + frh.getSobrenome());
				System.out.println("E-mail: " + frh.getEmail());
				System.out.println("Ultima sessao: " + frh.getUlimaSessao());
				System.out.println("Status sessao: " + frh.getStatusSessao());
				System.out.println(" ------------------------------- ");
			}
			
			
			System.out.println(" ================================================================ ");
			System.out.println("\n");
			
		}

	}

		
				

	public static void main(String[] args) {
		
		ImprimeHostsFixedRoom imprime = new ImprimeHostsFixedRoom();
		
		imprime.go();
		
	}
		
	



		
		
		

	
}
