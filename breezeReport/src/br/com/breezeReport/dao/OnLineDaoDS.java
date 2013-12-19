package br.com.breezeReport.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import java.sql.SQLException;
import java.util.ArrayList;




import br.com.breezeReport.factory.ConnectionFactoryDS;
import br.com.breezeReport.model.FixedRoom;
import br.com.breezeReport.model.FixedRoomHost;
import br.com.breezeReport.model.HostRoom;
import br.com.breezeReport.model.OnlineInfo;
import br.com.breezeReport.model.Room;



public class OnLineDaoDS {
	
	private static Connection breezeConnection;
	
	
	public OnLineDaoDS() {
		breezeConnection = new ConnectionFactoryDS().getBreezeConnection();
	}
	
	
	
	public ArrayList<OnlineInfo> generateRoomsInfo() {
		try {
			ArrayList<OnlineInfo> onlineInfos = new ArrayList<OnlineInfo>();
			
			PreparedStatement stmt = breezeConnection.prepareStatement(
					
					"SELECT QUERY1.SCO_ID as scoId, QUERY1.name as nome, QUERY1.url_path AS url, total, hosts, apresentadores, convidados, (select GETDATE()) as dataHora, (select GETDATE()) as horaHost, (select GETDATE()) as horaApresentador, (select GETDATE()) as horaConvidado FROM (SELECT S.SCO_ID, NAME, URL_PATH, COUNT( T.PERMISSION_ID ) AS TOTAL FROM PPS_SCOS S INNER JOIN PPS_TRANSCRIPTS T ON S.SCO_ID = T.SCO_ID WHERE T.STATUS='I' and S.ICON = 3 AND S.DISABLED IS NULL AND S.NAME NOT LIKE '{default%' GROUP BY S.SCO_ID, URL_PATH, NAME) QUERY1 " +
					"FULL OUTER JOIN" +
					"(SELECT S.SCO_ID, NAME, URL_PATH, COUNT( T.PERMISSION_ID ) AS HOSTS FROM PPS_SCOS S INNER JOIN PPS_TRANSCRIPTS T ON S.SCO_ID = T.SCO_ID WHERE T.STATUS='I' and S.ICON = 3 AND S.DISABLED IS NULL AND T.PERMISSION_ID=10 AND S.NAME NOT LIKE '{default%' GROUP BY S.SCO_ID, URL_PATH, NAME) QUERY2 " + 
					"ON QUERY1.SCO_ID = QUERY2.SCO_ID " +       
	                "FULL OUTER JOIN " +
					"(SELECT S.SCO_ID, NAME, URL_PATH, COUNT( T.PERMISSION_ID ) AS APRESENTADORES FROM PPS_SCOS S INNER JOIN PPS_TRANSCRIPTS T ON S.SCO_ID = T.SCO_ID WHERE T.STATUS='I' and S.ICON = 3 AND S.DISABLED IS NULL AND T.PERMISSION_ID=22 AND S.NAME NOT LIKE '{default%' GROUP BY S.SCO_ID, URL_PATH, NAME) QUERY3 " +
	                "ON QUERY1.SCO_ID = QUERY3.SCO_ID " + 
	                "FULL OUTER JOIN " + 
	                "(SELECT S.SCO_ID, NAME, URL_PATH, COUNT( T.PERMISSION_ID ) AS CONVIDADOS FROM PPS_SCOS S INNER JOIN PPS_TRANSCRIPTS T ON S.SCO_ID = T.SCO_ID WHERE T.STATUS='I' and S.ICON = 3 AND S.DISABLED IS NULL AND T.PERMISSION_ID=2 AND S.NAME NOT LIKE '{default%' GROUP BY S.SCO_ID, URL_PATH, NAME) QUERY4 ON QUERY1.SCO_ID = QUERY4.SCO_ID ");
			
			ResultSet rs = stmt.executeQuery();
			
			while(rs.next()) {
	
				OnlineInfo onlineInfo = new OnlineInfo();
				onlineInfo.setScoId(rs.getLong("scoId"));
				onlineInfo.setNome(rs.getString("nome"));
				onlineInfo.setUrl(rs.getString("url"));
				onlineInfo.setHosts(rs.getInt("hosts"));
				onlineInfo.setApresentadores(rs.getInt("apresentadores"));
				onlineInfo.setConvidados(rs.getInt("convidados"));
				onlineInfo.setTotal(rs.getInt("total"));
				onlineInfo.setDataHora(rs.getString("dataHora"));
				onlineInfo.setHoraHost(rs.getString("horaHost"));
				onlineInfo.setHoraApresentador(rs.getString("horaApresentador"));
				onlineInfo.setHoraConvidado(rs.getString("horaConvidado"));
				
				onlineInfos.add(onlineInfo);
			}
			rs.close();
			stmt.close();
			return onlineInfos;
		}
		catch(SQLException e) {
			throw new RuntimeException(e);
		}
	}
	
	
	
	public ArrayList<Room> generateDisabledRoomsInfo() {
		try {
			ArrayList<Room> onlineDisabledRooms = new ArrayList<Room>();
			
			PreparedStatement stmt = breezeConnection.prepareStatement("SELECT SCO_ID AS scoId, NAME AS nome, DISABLED as desabilitada FROM PPS_SCOS WHERE icon = 3 AND DISABLED IS NOT NULL AND NAME NOT LIKE '{default%'");
			
			ResultSet rs = stmt.executeQuery();
			
			while(rs.next()) {
				
				Room room = new Room();
				room.setScoId(rs.getLong("scoId"));
				room.setNome(rs.getString("nome"));
				room.setDesabilitada(rs.getString("desabilitada"));
				
				onlineDisabledRooms.add(room);
				
			}
			rs.close();
			stmt.close();
			return onlineDisabledRooms;
		}
		catch(SQLException e) {
			throw new RuntimeException(e);
		}
		
	}
	
	
	public ArrayList<HostRoom> generateHostRoomsInfo() {
		try {

			ArrayList<HostRoom> onlineHostRoomsInfo = new ArrayList<HostRoom>();
			
			PreparedStatement stmt = breezeConnection.prepareStatement(
							
			"SELECT QUERY2.NAME AS nome, QUERY2.LOGIN, QUERY1.SCO_ID AS roomScoId, QUERY2.RecordCreated AS dataHora, QUERY2.DISABLED AS desabilitado FROM " + 

			"(SELECT S.SCO_ID, S.NAME, T.PRINCIPAL_ID " +  
			"FROM PPS_SCOS S INNER JOIN PPS_TRANSCRIPTS T " +
			"ON S.SCO_ID = T.SCO_ID " +
			"WHERE T.STATUS='I' and S.ICON = 3 " +
			"AND T.PERMISSION_ID = 10 AND S.NAME NOT LIKE '{default%' ) " + 

			"AS QUERY1 " +

			"INNER JOIN " + 

			"(SELECT PRINCIPAL_ID, NAME, LOGIN, EXT_LOGIN, DISABLED, RecordCreated " +
			"FROM PPS_PRINCIPALS) AS QUERY2 " +

			"ON QUERY1.PRINCIPAL_ID = QUERY2.PRINCIPAL_ID "); 
			
							
			ResultSet rs = stmt.executeQuery();
			
			while(rs.next()) {
	
				HostRoom onlineHostRoomInfo = new HostRoom();
				onlineHostRoomInfo.setNome(rs.getString("nome"));
				onlineHostRoomInfo.setLogin(rs.getString("login"));
				onlineHostRoomInfo.setRoomScoId(rs.getLong("roomScoId"));
				onlineHostRoomInfo.setDataHora(rs.getString("dataHora"));
				onlineHostRoomInfo.setDesabilitado(rs.getString("desabilitado"));
				
				onlineHostRoomsInfo.add(onlineHostRoomInfo);
			}
			rs.close();
			stmt.close();
			return onlineHostRoomsInfo;
		}
		catch(SQLException e) {
		throw new RuntimeException(e);
		}

		
	}
	
	
	
	public ArrayList<FixedRoom> getFixedRooms() {
		
		// Retorna uma lista contendo o scoId e nome de todas as salas de reunião que começam com 
		// "EU - " ou "ISR - " (Salas fixas)
		
		try {

			ArrayList<FixedRoom> fixedRooms = new ArrayList<FixedRoom>();
			PreparedStatement stmt = breezeConnection.prepareStatement("SELECT SCO_ID AS scoId, NAME AS nome FROM PPS_SCOS WHERE DISABLED IS NULL AND (NAME LIKE 'EU - %' OR NAME LIKE 'ISR - %') ORDER BY NAME");			
			ResultSet rs = stmt.executeQuery();
			
			while(rs.next()) {
				
				FixedRoom fixedRoom = new FixedRoom();
				fixedRoom.setScoId(rs.getString("scoId"));
				fixedRoom.setNome(rs.getString("nome"));

				fixedRooms.add(fixedRoom);
			}
			rs.close();
			stmt.close();
			return fixedRooms;
		}
		catch(SQLException e) {
			throw new RuntimeException(e);
		}
	}




	// Retorna a lista de hosts nomeados (cadastrados através do connect como hosts de uma determinada sala)
	// + os hosts ativos no momento, para uma dada sala de reunião (scoId)
	// Obs: É possível através deste método saber qual foi a sessão mais recente de cada host
	
	public ArrayList<FixedRoomHost> getFixedRoomHosts(String scoId) {

		try {
			
			ArrayList<FixedRoomHost> fixedRoomHosts = new ArrayList<FixedRoomHost>();
			

			PreparedStatement stmt = breezeConnection.prepareStatement(							

					"SELECT user_id as id, login, first_name as primeiroNome, last_name as sobrenome, email, most_recent_session as ultimaSessao, session_status as statusSessao FROM " + 			
					         "(SELECT " +  
					            "z.principal_id as user_id, " + 
					            "z.login, " + 
					            "MAX(z.first_name) AS first_name, " + 
					            "MAX(z.last_name) AS last_name,  " +
					            "MAX(z.email) AS email, " +
					            "z.description AS user_description, " + 
					            "z.name AS user_type, " +
					            "z.most_recent_session, " +
					            "z.session_status, " +
					            "z.manager_name, " +
					            "z.disabled, " +
					            "z.account_id, " +
					            "MAX(z.email) AS custom_field_1, " +
					            "MAX(z.custom_field_2) AS custom_field_2, " + 
					            "MAX(z.custom_field_3) AS custom_field_3, " + 
					            "MAX(z.custom_field_4) AS custom_field_4, " + 
					            "MAX(z.custom_field_5) AS custom_field_5, " + 
					            "MAX(z.custom_field_6) AS custom_field_6, " + 
					            "MAX(z.custom_field_7) AS custom_field_7, " + 
					            "MAX(z.custom_field_8) AS custom_field_8, " + 
					            "MAX(z.custom_field_9) AS custom_field_9, " + 
					            "MAX(z.custom_field_10) AS custom_field_10 " +
					        "FROM " + 
					            "(SELECT " + 
					                "y.principal_id, " + 
					                "y.login,  " +
					                "y.description, " + 
					                "y.name, " +
					                "y.most_recent_session, " +
					                "y.session_status, " +
					                "y.manager_name, " +
					                "y.disabled, " +
					                "y.account_id, " +
					                "CASE f.display_seq WHEN 1 THEN f.value END AS first_name, " + 
					                "CASE f.display_seq WHEN 2 THEN f.value END AS last_name,  " +
					                "CASE f.display_seq WHEN 3 THEN f.value END AS email,  " +
					                "CASE f.display_seq WHEN 4 THEN (CASE f.field_id WHEN 131 THEN (SELECT value FROM pps_acl_multi_fields WHERE df_id = f.value) ELSE f.value END) END AS custom_field_2, " + 
					                "CASE f.display_seq WHEN 5 THEN (CASE f.field_id WHEN 131 THEN (SELECT value FROM pps_acl_multi_fields WHERE df_id = f.value) ELSE f.value END) END AS custom_field_3, " +
					                "CASE f.display_seq WHEN 6 THEN (CASE f.field_id WHEN 131 THEN (SELECT value FROM pps_acl_multi_fields WHERE df_id = f.value) ELSE f.value END) END AS custom_field_4, " +
					                "CASE f.display_seq WHEN 7 THEN (CASE f.field_id WHEN 131 THEN (SELECT value FROM pps_acl_multi_fields WHERE df_id = f.value) ELSE f.value END) END AS custom_field_5, " +
					                "CASE f.display_seq WHEN 8 THEN (CASE f.field_id WHEN 131 THEN (SELECT value FROM pps_acl_multi_fields WHERE df_id = f.value) ELSE f.value END) END AS custom_field_6, " +
					                "CASE f.display_seq WHEN 9 THEN (CASE f.field_id WHEN 131 THEN (SELECT value FROM pps_acl_multi_fields WHERE df_id = f.value) ELSE f.value END) END AS custom_field_7, " + 
					                "CASE f.display_seq WHEN 10 THEN (CASE f.field_id WHEN 131 THEN (SELECT value FROM pps_acl_multi_fields WHERE df_id = f.value) ELSE f.value END) END AS custom_field_8, " + 
					                "CASE f.display_seq WHEN 11 THEN (CASE f.field_id WHEN 131 THEN (SELECT value FROM pps_acl_multi_fields WHERE df_id = f.value) ELSE f.value END) END AS custom_field_9, " + 
					                "CASE f.display_seq WHEN 12 THEN (CASE f.field_id WHEN 131 THEN (SELECT value FROM pps_acl_multi_fields WHERE df_id = f.value) ELSE f.value END) END AS custom_field_10 " +
					            "FROM " + 
					                "(SELECT " +
					                    "p1.principal_id, " + 
					                    "p1.login, " +
					                    "p1.description, " + 
					                    "p1.name, " +
					                    "us.date_created AS most_recent_session, " +
					                    "CASE " +
					                        "WHEN us.date_created IS NULL THEN '{inactive}' " +
					                        "WHEN (DATEDIFF(hh, MAX(us.date_created), GETUTCDATE()) < 16) THEN '{active}' " +
					                        "ELSE '{inactive}' " +
					                    "END AS session_status, " +
					                    "(SELECT p.name FROM pps_principals p, pps_acls a WHERE p.principal_id = a.parent_acl_id AND p1.principal_id = a.acl_id) manager_name, " +
					                    "p1.disabled, " +
					                    "p1.account_id " +
					                "FROM ( " +
					                    "SELECT " + 
					                        "p.principal_id, " + 
					                        "p.login, " + 
					                        "p.description, " + 
					                        "e.name, " +
					                        "m.login AS manager_login, " +
					                        "p.disabled, " +
					                        "p.account_id " +
					                    "FROM " + 
					                        "pps_principals p, " + 
					                        "pps_ext_enum_utype e, " +
					                        "pps_acls a " +
					                    "LEFT OUTER JOIN " + 
					                        "pps_principals m " + 
					                    "ON " + 
					                        "m.principal_id = a.parent_acl_id " +    
					                    "WHERE " + 
					                        "p.has_children = 0 AND " + 
					                        "p.type = e.type AND " +
					                        "a.acl_id = p.principal_id AND " +
					                        "p.principal_id > 0 and " + 
					                        "p.PRINCIPAL_ID IN (SELECT DISTINCT P.PRINCIPAL_ID FROM PPS_PERMISSION_ACTIVITIES PA INNER JOIN PPS_PRINCIPALS P ON PA.PRINCIPAL_ID = P.PRINCIPAL_ID WHERE SCO_ID = " + scoId + " AND NAME <> 'All Users' AND DISABLED IS NULL AND ACTIVITY_TYPE_ID = 1) " +                            
					                ") p1 " +
					                "LEFT OUTER JOIN " +
					                "( " +
					                    "SELECT " + 
					                        "user_id, MAX(date_created) AS date_created " +
					                    "FROM " + 
					                        "pps_user_sessions " +
					                    "WHERE " +
					                        "user_id IS NOT NULL " +
					                    "GROUP BY " + 
					                        "user_id " +
					                ") us " +
					                "ON " +
					                    "p1.principal_id = us.user_id " +
					                "GROUP BY " +
					                    "p1.principal_id, p1.login, p1.description, p1.name, p1.login, p1.disabled, p1.account_id, us.date_created) y " + 
					            "LEFT OUTER JOIN " + 
					                "(SELECT " + 
					                    "a.account_id, " +
					                    "af.acl_id, " + 
					                    "af.value, " + 
					                    "a.display_seq, " +
					                    "af.field_id " +
					                "FROM " + 
					                    "pps_acl_fields af, " + 
					                    "pps_fields a, " +
					                    "pps_principals p " +
					                "WHERE " + 
					                    "af.acl_id = p.principal_id AND " +
					                    "p.account_id = a.account_id AND " +
					                    "a.object_type = 0 AND " + 
					                    "a.disabled IS NULL AND " + 
					                    "a.field_id = af.field_id AND " + 
					                    "a.display_seq < 13 AND " +
					                    "a.name NOT like '{x-tel%password}' AND " +
					                    "a.name != '{x-manager}') f " + 
					            "ON " + 
					                "f.acl_id = y.principal_id AND " +
					                "f.account_id = y.account_id ) z " + 
					        "GROUP BY " + 
					            "z.principal_id, " + 
					            "z.login, " + 
					            "z.description, " + 
					            "z.name, " +
					            "z.most_recent_session, " +
					            "z.session_status, " +
					            "z.manager_name, " +
					            "z.disabled, " +
					            "z.account_id) AS QUERY1");
					
							
			ResultSet rs = stmt.executeQuery();
			
			while(rs.next()) {
	
				FixedRoomHost fixedRoomHost = new FixedRoomHost();
				fixedRoomHost.setId(rs.getString("id"));
				fixedRoomHost.setLogin(rs.getString("login"));
				fixedRoomHost.setPrimeiroNome(rs.getString("primeiroNome"));
				fixedRoomHost.setSobrenome(rs.getString("sobrenome"));
				fixedRoomHost.setEmail(rs.getString("email"));
				fixedRoomHost.setUlimaSessao(rs.getString("ultimaSessao"));
				fixedRoomHost.setStatusSessao(rs.getString("statusSessao"));
				
				fixedRoomHosts.add(fixedRoomHost);
			}
			rs.close();
			stmt.close();
			return fixedRoomHosts;
		}
		catch(SQLException e) {
		throw new RuntimeException(e);
		}

		
	}
	
	
	

	
	
	public void closeBreezeConnection() {
		try {
			breezeConnection.close();
		} 
		catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	
}