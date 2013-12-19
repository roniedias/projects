package br.com.breezeReport.deprecated;

//import java.sql.Connection;
//import java.sql.PreparedStatement;
//import java.sql.ResultSet;
//import java.sql.SQLException;
//import java.util.ArrayList;


//import br.com.breezeReport.factory.ConnectionFactory;
//import br.com.breezeReport.model.OnlineInfo;
//import br.com.breezeReport.model.Room;



public class OnLineDao {
	
//	private static Connection breezeConnection;
//	
//	
//	public OnLineDao() {
//		breezeConnection = new ConnectionFactory().getBreezeConnection();
//	}
//	
//	
//	
//	public ArrayList<OnlineInfo> generateRoomsInfo() {
//		try {
//			ArrayList<OnlineInfo> onlineInfos = new ArrayList<OnlineInfo>();
//			
//			PreparedStatement stmt = breezeConnection.prepareStatement(
//					
//					"SELECT QUERY1.SCO_ID as scoId, QUERY1.name as nome, QUERY1.url_path AS url, total, hosts, apresentadores, convidados, (select GETDATE()) as dataHora, (select GETDATE()) as horaHost, (select GETDATE()) as horaApresentador, (select GETDATE()) as horaConvidado FROM (SELECT S.SCO_ID, NAME, URL_PATH, COUNT( T.PERMISSION_ID ) AS TOTAL FROM PPS_SCOS S INNER JOIN PPS_TRANSCRIPTS T ON S.SCO_ID = T.SCO_ID WHERE T.STATUS='I' and S.ICON = 3 AND S.DISABLED IS NULL AND S.NAME NOT LIKE '{default%' GROUP BY S.SCO_ID, URL_PATH, NAME) QUERY1 " +
//					"FULL OUTER JOIN" +
//					"(SELECT S.SCO_ID, NAME, URL_PATH, COUNT( T.PERMISSION_ID ) AS HOSTS FROM PPS_SCOS S INNER JOIN PPS_TRANSCRIPTS T ON S.SCO_ID = T.SCO_ID WHERE T.STATUS='I' and S.ICON = 3 AND S.DISABLED IS NULL AND T.PERMISSION_ID=10 AND S.NAME NOT LIKE '{default%' GROUP BY S.SCO_ID, URL_PATH, NAME) QUERY2 " + 
//					"ON QUERY1.SCO_ID = QUERY2.SCO_ID " +       
//	                "FULL OUTER JOIN " +
//					"(SELECT S.SCO_ID, NAME, URL_PATH, COUNT( T.PERMISSION_ID ) AS APRESENTADORES FROM PPS_SCOS S INNER JOIN PPS_TRANSCRIPTS T ON S.SCO_ID = T.SCO_ID WHERE T.STATUS='I' and S.ICON = 3 AND S.DISABLED IS NULL AND T.PERMISSION_ID=22 AND S.NAME NOT LIKE '{default%' GROUP BY S.SCO_ID, URL_PATH, NAME) QUERY3 " +
//	                "ON QUERY1.SCO_ID = QUERY3.SCO_ID " + 
//	                "FULL OUTER JOIN " + 
//	                "(SELECT S.SCO_ID, NAME, URL_PATH, COUNT( T.PERMISSION_ID ) AS CONVIDADOS FROM PPS_SCOS S INNER JOIN PPS_TRANSCRIPTS T ON S.SCO_ID = T.SCO_ID WHERE T.STATUS='I' and S.ICON = 3 AND S.DISABLED IS NULL AND T.PERMISSION_ID=2 AND S.NAME NOT LIKE '{default%' GROUP BY S.SCO_ID, URL_PATH, NAME) QUERY4 ON QUERY1.SCO_ID = QUERY4.SCO_ID ");
//			
//			ResultSet rs = stmt.executeQuery();
//			
//			while(rs.next()) {
//	
//				OnlineInfo onlineInfo = new OnlineInfo();
//				onlineInfo.setScoId(rs.getLong("scoId"));
//				onlineInfo.setNome(rs.getString("nome"));
//				onlineInfo.setUrl(rs.getString("url"));
//				onlineInfo.setHosts(rs.getInt("hosts"));
//				onlineInfo.setApresentadores(rs.getInt("apresentadores"));
//				onlineInfo.setConvidados(rs.getInt("convidados"));
//				onlineInfo.setTotal(rs.getInt("total"));
//				onlineInfo.setDataHora(rs.getString("dataHora"));
//				onlineInfo.setHoraHost(rs.getString("horaHost"));
//				onlineInfo.setHoraApresentador(rs.getString("horaApresentador"));
//				onlineInfo.setHoraConvidado(rs.getString("horaConvidado"));
//				
//				onlineInfos.add(onlineInfo);
//			}
//			rs.close();
//			stmt.close();
//			return onlineInfos;
//		}
//		catch(SQLException e) {
//			throw new RuntimeException(e);
//		}
//	}
//	
//	
//	
//	public ArrayList<Room> generateDisabledRoomsInfo() {
//		try {
//			ArrayList<Room> onlineDisabledRooms = new ArrayList<Room>();
//			
//			PreparedStatement stmt = breezeConnection.prepareStatement("SELECT SCO_ID AS scoId, NAME AS nome, DISABLED as desabilitada FROM PPS_SCOS WHERE icon = 3 AND DISABLED IS NOT NULL AND NAME NOT LIKE '{default%'");
//			
//			ResultSet rs = stmt.executeQuery();
//			
//			while(rs.next()) {
//				
//				Room room = new Room();
//				room.setScoId(rs.getLong("scoId"));
//				room.setNome(rs.getString("nome"));
//				room.setDesabilitada(rs.getString("desabilitada"));
//				
//				onlineDisabledRooms.add(room);
//				
//			}
//			rs.close();
//			stmt.close();
//			return onlineDisabledRooms;
//		}
//		catch(SQLException e) {
//			throw new RuntimeException(e);
//		}
//		
//	}
//
//	
//	
//	
//	public void closeBreezeConnection() {
//		try {
//			breezeConnection.close();
//		} 
//		catch (SQLException e) {
//			e.printStackTrace();
//		}
//	}

			

}