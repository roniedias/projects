package br.com.breezeReport.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;



import br.com.breezeReport.factory.ConnectionFactory;
import br.com.breezeReport.model.Room;
import br.com.breezeReport.model.RoomInfo;
import br.com.breezeReport.model.User;
import br.com.breezeReport.util.ConverteTimestamp;
import br.com.breezeReport.security.SHA2;



public class PersistenceDao {
	
	private static Connection breezeReportConnection;
	
	
	public PersistenceDao() {
		breezeReportConnection = new ConnectionFactory().getBreezeReportConnection();
	}


	public void addRoomInfo(RoomInfo roomInfo) {
	
		String sql = "insert into roomInfo (scoId, hosts, apresentadores, convidados, total, dataHora, horaHost, horaApresentador, horaConvidado) values (?,?,?,?,?,?,?,?,?)";
	
		try {
			PreparedStatement stmt = breezeReportConnection.prepareStatement(sql);
			
			stmt.setLong(1, roomInfo.getScoId());
			stmt.setInt(2, roomInfo.getHosts());
			stmt.setInt(3, roomInfo.getApresentadores());
			stmt.setInt(4, roomInfo.getConvidados());
			stmt.setInt(5, roomInfo.getTotal());
			stmt.setString(6, roomInfo.getDataHora());
			stmt.setString(7, roomInfo.getHoraHost());
			stmt.setString(8, roomInfo.getHoraApresentador());
			stmt.setString(9, roomInfo.getHoraConvidado());
			stmt.execute();
			stmt.close();	
		} 
		catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	
	
	
	
	public void addRoom(Room room) {
		
		String sql = "insert into room (scoId, nome, desabilitada) values (?,?,?)";
	
		try {
			PreparedStatement stmt = breezeReportConnection.prepareStatement(sql);
			
			stmt.setLong(1, room.getScoId());
			stmt.setString(2, room.getNome());
			stmt.setString(3, room.getDesabilitada());
			stmt.execute();
			stmt.close();	
		} 
		catch (SQLException e) {
			e.printStackTrace();
		}
	}

	
	
	public ArrayList<RoomInfo> getRoomInfoBySco(long scoId) {
		try {
			ArrayList<RoomInfo> roomsInfo = new ArrayList<RoomInfo>();
			//PreparedStatement stmt = breezeReportConnection.prepareStatement("SELECT * from roomInfo where scoId = " + scoId + " order by " + scoId);
			PreparedStatement stmt = breezeReportConnection.prepareStatement("SELECT * from roomInfo where scoId = " + scoId); // Obs: Deu problema quando foi adicionada a cláusula order by
			ResultSet rs = stmt.executeQuery();
			
			while(rs.next()) {
				
				RoomInfo roomInfo = new RoomInfo();
				roomInfo.setScoId(rs.getLong("scoId"));
				roomInfo.setHosts(rs.getInt("hosts"));
				roomInfo.setApresentadores(rs.getInt("apresentadores"));
				roomInfo.setConvidados(rs.getInt("convidados"));
				roomInfo.setTotal(rs.getInt("total"));
				roomInfo.setDataHora(rs.getString("dataHora"));
				roomInfo.setHoraHost(rs.getString("horaHost"));
				roomInfo.setHoraApresentador(rs.getString("horaApresentador"));
				roomInfo.setHoraConvidado(rs.getString("horaConvidado"));
				
				roomsInfo.add(roomInfo);
			}
			rs.close();
			stmt.close();
			return roomsInfo;
		}
		catch(SQLException e) {
			throw new RuntimeException(e);
		}

	}
	
	
	public ArrayList<RoomInfo> getRoomInfoByScoFromPeriod(long scoId, String inputDataInicial, String inputDataFinal) {
		try {
			ArrayList<RoomInfo> roomsInfo = new ArrayList<RoomInfo>();
						
			PreparedStatement stmt = breezeReportConnection.prepareStatement("SELECT * FROM roomInfo WHERE scoId = " + scoId + " AND dataHora BETWEEN '" + inputDataInicial +"'" + " AND " + "'" + inputDataFinal + "'"); 
			
			ResultSet rs = stmt.executeQuery();
			
			while(rs.next()) {
				
				RoomInfo roomInfo = new RoomInfo();
				roomInfo.setScoId(rs.getLong("scoId"));
				roomInfo.setHosts(rs.getInt("hosts"));
				roomInfo.setApresentadores(rs.getInt("apresentadores"));
				roomInfo.setConvidados(rs.getInt("convidados"));
				roomInfo.setTotal(rs.getInt("total"));
				roomInfo.setDataHora(rs.getString("dataHora"));
				roomInfo.setHoraHost(rs.getString("horaHost"));
				roomInfo.setHoraApresentador(rs.getString("horaApresentador"));
				roomInfo.setHoraConvidado(rs.getString("horaConvidado"));
				
				roomsInfo.add(roomInfo);
			}
			rs.close();
			stmt.close();
			return roomsInfo;
		}
		catch(SQLException e) {
			throw new RuntimeException(e);
		}

	}
	
			
	
	public ArrayList<RoomInfo> getRoomInfoByName(String nome) {
		try {
			ArrayList<RoomInfo> roomsInfo = new ArrayList<RoomInfo>();
			PreparedStatement stmt = breezeReportConnection.prepareStatement("SELECT * FROM roomInfo ri inner join room r ON ri.scoId = r.scoId WHERE r.nome = '" + nome + "' ORDER BY r.scoId");
			ResultSet rs = stmt.executeQuery();
			
			while(rs.next()) {
				
				RoomInfo roomInfo = new RoomInfo();
				roomInfo.setScoId(rs.getLong("scoId"));
				roomInfo.setHosts(rs.getInt("hosts"));
				roomInfo.setApresentadores(rs.getInt("apresentadores"));
				roomInfo.setConvidados(rs.getInt("convidados"));
				roomInfo.setTotal(rs.getInt("total"));
				roomInfo.setDataHora(rs.getString("dataHora"));
				roomInfo.setHoraHost(rs.getString("horaHost"));
				roomInfo.setHoraApresentador(rs.getString("horaApresentador"));
				roomInfo.setHoraConvidado(rs.getString("horaConvidado"));

					
				roomsInfo.add(roomInfo);
			}
			rs.close();
			stmt.close();
			return roomsInfo;
		}
		catch(SQLException e) {
			throw new RuntimeException(e);
		}

	}
	
	
	public void updateRoomInfo(RoomInfo roomInfo) {
		
		String sql = "update roomInfo set hosts = ?, apresentadores = ?, convidados = ?, total = ?, dataHora = ?, horaHost = ?, horaApresentador = ?, horaConvidado = ? where scoId = ? and dataHora = ?";
		
		try {
			
			 PreparedStatement stmt = breezeReportConnection.prepareStatement(sql);
	         stmt.setInt(1, roomInfo.getHosts());
	         stmt.setInt(2, roomInfo.getApresentadores());
	         stmt.setInt(3, roomInfo.getConvidados());
	         stmt.setInt(4, roomInfo.getTotal());
	         stmt.setString(5, roomInfo.getDataHora());
			 stmt.setString(6, roomInfo.getHoraHost());
			 stmt.setString(7, roomInfo.getHoraApresentador());
			 stmt.setString(8, roomInfo.getHoraConvidado());

			 stmt.setLong(9, roomInfo.getScoId());
	         stmt.setString(10, roomInfo.getDataHora());
	         
	         stmt.execute();
	         stmt.close();
		}
		catch (SQLException e) {
	         throw new RuntimeException(e);
		}		
	}
	
	
	
	public void updateRoom(Room room) {
		
		String sql = "update room set nome = ?, desabilitada = ? where scoId = ?";
		
		try {
			
			 PreparedStatement stmt = breezeReportConnection.prepareStatement(sql);
	         stmt.setString(1, room.getNome());
	         stmt.setString(2, room.getDesabilitada());

			 stmt.setLong(3, room.getScoId());
			 
	         stmt.execute();
	         stmt.close();
		}
		catch (SQLException e) {
	         throw new RuntimeException(e);
		}		
	}

	
	
	
	public ArrayList<RoomInfo> getAllRoomsInfo() {
		try {
			ArrayList<RoomInfo> roomsInfo = new ArrayList<RoomInfo>();
			PreparedStatement stmt = breezeReportConnection.prepareStatement("SELECT * FROM roomInfo ORDER BY scoId");
			ResultSet rs = stmt.executeQuery();
			
			while(rs.next()) {
				
				RoomInfo roomInfo = new RoomInfo();
				roomInfo.setScoId(rs.getLong("scoId"));
				roomInfo.setHosts(rs.getInt("hosts"));
				roomInfo.setApresentadores(rs.getInt("apresentadores"));
				roomInfo.setConvidados(rs.getInt("convidados"));
				roomInfo.setTotal(rs.getInt("total"));
				roomInfo.setDataHora(rs.getString("dataHora")); 
				roomInfo.setHoraHost(rs.getString("horaHost"));
				roomInfo.setHoraApresentador(rs.getString("horaApresentador"));
				roomInfo.setHoraConvidado(rs.getString("horaConvidado"));

				
					
				roomsInfo.add(roomInfo);
			}
			rs.close();
			stmt.close();
			return roomsInfo;
		}
		catch(SQLException e) {
			throw new RuntimeException(e);
		}

	}
	

	public ArrayList<Room> getAllRooms() {
		try {
			ArrayList<Room> rooms = new ArrayList<Room>();
			PreparedStatement stmt = breezeReportConnection.prepareStatement("SELECT * from room WHERE desabilitada IS NULL");
			
			ResultSet rs = stmt.executeQuery();
			
			while(rs.next()) {
				Room room = new Room();
				room.setScoId(rs.getLong("scoId"));
				room.setNome(rs.getString("nome"));
				room.setDesabilitada(rs.getString("desabilitada"));
				
				
				rooms.add(room);				
			}
			
			rs.close();
			stmt.close();
			return rooms;
		}
		catch(SQLException e) {
			throw new RuntimeException(e);	
		}
	}
	
	
	
	
	
	public String getRoomName(long scoId) {
		
		String roomName = new String();
		
		try {
			PreparedStatement stmt = breezeReportConnection.prepareStatement("SELECT nome from room where desabilitada IS NULL and scoId = " + scoId);
			ResultSet rs = stmt.executeQuery();
			
			while(rs.next()) {
				roomName = rs.getString("nome");
			}

			rs.close();
			stmt.close();
			return roomName;
		}
		catch(SQLException e) {
			throw new RuntimeException(e);
		}
	}
	
	
	
	// Retorna true se a sala for localizada (pelo nome) na tabela room 
	public boolean isThereRoomWithName(String nome) {
		
		try {
			boolean exists = false;
			
			PreparedStatement stmt = breezeReportConnection.prepareStatement("SELECT nome from room where desabilitada IS NULL and nome = '" +  nome + "'");
			ResultSet rs = stmt.executeQuery();
				
			if (rs.next()){
				exists = true;
			}
				
			rs.close();
			stmt.close();
			return exists;
		}
		catch(SQLException e) {
			throw new RuntimeException(e);
		}
	}

	
	
	
	// Método auxiliar: Retorna true se a sala já tiver sido gravada na tabela room
	public boolean isRoomAlreadyRecorded(long scoId) {
		try {
			boolean exists = false;
			
			PreparedStatement stmt = breezeReportConnection.prepareStatement("SELECT scoId from room where desabilitada IS NULL and scoId = " + scoId);
			ResultSet rs = stmt.executeQuery();
			
			if (rs.next()){
				exists = true;
			}
			
			rs.close();
			stmt.close();
			return exists;
		}
		catch(SQLException e) {
			throw new RuntimeException(e);
		}

	}

		
	
	public String getBreezeReportBeginDate() {
	
		String beginDate = new String();
		
		try {
			PreparedStatement stmt = breezeReportConnection.prepareStatement("SELECT min(dataHora) AS dataHora FROM roomInfo");
			
			ResultSet rs = stmt.executeQuery();
			
			while(rs.next()) {
				beginDate = rs.getString("dataHora");
			}
	
			rs.close();
			stmt.close();
			return ConverteTimestamp.getDateWithoutSlash(beginDate);
		}
		catch(SQLException e) {
			throw new RuntimeException(e);
		}

	}
	
	
	public String getRoomBeginDate(long scoId) {
		
		String beginDate = new String();
		
		try {
			//PreparedStatement stmt = breezeReportConnection.prepareStatement("SELECT min(dataHora) AS dataHora FROM roomInfo WHERE nome = '" +  nome + "'" + "ORDER BY scoId");
			//PreparedStatement stmt = breezeReportConnection.prepareStatement("SELECT min(dataHora) AS dataHora FROM roomInfo ri INNER JOIN room r ON ri.scoId = r.scoId WHERE r.nome = '" +  nome + "'");
			PreparedStatement stmt = breezeReportConnection.prepareStatement("SELECT min(dataHora) AS dataHora FROM roomInfo ri INNER JOIN room r ON ri.scoId = r.scoId WHERE ri.scoId = '" +  scoId + "'");
			
			ResultSet rs = stmt.executeQuery();
			
			while(rs.next()) {
				beginDate = rs.getString("dataHora");
			}
	
			rs.close();
			stmt.close();
			//return ConverteTimestamp.getDateWithoutSlash(beginDate);
			return beginDate;
		}
		catch(SQLException e) {
			throw new RuntimeException(e);
		}
	}
	
	
	
	public String getRoomEndDate(long scoId) {
		
		String endDate = new String();
		
		try {
			//PreparedStatement stmt = breezeReportConnection.prepareStatement("SELECT max(dataHora) AS dataHora FROM roomInfo WHERE nome = '" +  nome + "'" + "ORDER BY scoId");
			//PreparedStatement stmt = breezeReportConnection.prepareStatement("SELECT max(dataHora) AS dataHora FROM roomInfo ri INNER JOIN room r ON ri.scoId = r.scoId WHERE r.nome = '" +  nome + "'");
			PreparedStatement stmt = breezeReportConnection.prepareStatement("SELECT max(dataHora) AS dataHora FROM roomInfo ri INNER JOIN room r ON ri.scoId = r.scoId WHERE ri.scoId = '" +  scoId + "'");
			
			ResultSet rs = stmt.executeQuery();
			
			while(rs.next()) {
				endDate = rs.getString("dataHora");
			}
	
			rs.close();
			stmt.close();
			//return ConverteTimestamp.getDateWithoutSlash(endDate);
			return endDate;
		}
		catch(SQLException e) {
			throw new RuntimeException(e);
		}
	}

	

		
	public void closeBreezeReportConnection() {
		try {
			breezeReportConnection.close();
		} 
		catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	
	
	
	public void addUser(User user) {
		
		String sql = "insert into user (nome, senha, email) values (?,?,?)";
	
		try {
			PreparedStatement stmt = breezeReportConnection.prepareStatement(sql);
			
			stmt.setString(1, user.getNome());
			stmt.setString(2, user.getSenha());
			stmt.setString(3, user.getEmail());
			stmt.execute();
			stmt.close();	
		} 
		catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	
	
	public boolean isLoginOK(String nome, String senha) {
	
		try {
			boolean nomeOK = false;
			boolean senhaOK = false;
			
			String senhaCript = new SHA2(senha).genEncryptedPassword();
			
			PreparedStatement stmt = breezeReportConnection.prepareStatement("SELECT nome, senha from user where nome = '" + nome + "'");
			ResultSet rs = stmt.executeQuery();
			
			if (rs.next()){
				nomeOK = true;
				senhaOK = senhaCript.equals(rs.getString("senha"));
			}
			
			rs.close();
			stmt.close();
			return nomeOK && senhaOK;
		}
		catch(SQLException e) {
			throw new RuntimeException(e);
		}

	}
	

	
	

	

}






































	
	
//	public String[] getAllRoomsArrayElements() {
//	try {		
//		
//		ArrayList<Room> rooms = new ArrayList<Room>();
//		PreparedStatement stmt = breezeReportConnection.prepareStatement("SELECT * from room");
//		
//		ResultSet rs = stmt.executeQuery();
//		
//		while(rs.next()) {
//			Room room = new Room();
//			room.setScoId(rs.getLong("scoId"));
//			room.setNome(rs.getString("nome"));
//			room.setUrl(rs.getString("url"));
//			room.setDataGravacao(rs.getString("dataGravacao"));
//			
//			rooms.add(room);
//		}
//		
//		rs.close();
//		stmt.close();
//		
//		String[] roomArray = new String[rooms.size()];
//		
//		for(int r = 0; r < rooms.size(); r++) {
//			roomArray[r] = rooms.get(r).getNome();
//		}
//		
//		return roomArray;
//
//	}
//	catch(SQLException e) {
//		throw new RuntimeException(e);	
//	}
//	
//}

//public ArrayList<RoomInfo> getDistinctAllRoomsInfo() {
//	try {
//		ArrayList<RoomInfo> roomsInfo = new ArrayList<RoomInfo>();
//		//PreparedStatement stmt = breezeReportConnection.prepareStatement("SELECT MAX(scoId) as scoId, nome, url, hosts, apresentadores, convidados, total, dataHora, horaHost, horaApresentador, horaConvidado FROM roomInfo GROUP BY nome");
//		PreparedStatement stmt = breezeReportConnection.prepareStatement("SELECT MAX(ri.scoId) as scoId, r.nome as nome, r.url as url, ri.hosts as hosts, ri.apresentadores as apresentadores, ri.convidados as convidados, ri.total as total, ri.dataHora as dataHora, ri.horaHost as horaHost, ri.horaApresentador as horaApresentador, ri.horaConvidado as horaConvidado FROM roomInfo ri inner join room r on ri.scoID = r.scoId GROUP BY nome");
//		
//		ResultSet rs = stmt.executeQuery();
//		
//		while(rs.next()) {
//			
//			RoomInfo roomInfo = new RoomInfo();
//			roomInfo.setScoId(rs.getLong("scoId"));
//			roomInfo.setHosts(rs.getInt("hosts"));
//			roomInfo.setApresentadores(rs.getInt("apresentadores"));
//			roomInfo.setConvidados(rs.getInt("convidados"));
//			roomInfo.setTotal(rs.getInt("total"));
//			roomInfo.setDataHora(rs.getString("dataHora")); 
//			roomInfo.setHoraHost(rs.getString("horaHost"));
//			roomInfo.setHoraApresentador(rs.getString("horaApresentador"));
//			roomInfo.setHoraConvidado(rs.getString("horaConvidado"));
//				
//			roomsInfo.add(roomInfo);
//		}
//		rs.close();
//		stmt.close();
//		return roomsInfo;
//	}
//	catch(SQLException e) {
//		throw new RuntimeException(e);
//	}
//
//}

	
	//	private boolean isRegisterFoundForDate(long scoId, String Date) {
//	
//	try {
//		PreparedStatement stmt = breezeReportConnection.prepareStatement("SELECT dataHora from roomInfo where scoId = " + scoId);
//		ResultSet rs = stmt.executeQuery();
//		
//		while(rs.next()) {
//			
//			if(ConverteTimestamp.getDate(rs.getString("dataHora")).equals(Date))
//				dateFound = true;
//		}
//		rs.close();
//		stmt.close();
//		return dateFound;
//	}
//	catch(SQLException e) {
//		throw new RuntimeException(e);
//	}
//
//}


//public String getRoomUrl(long scoId) {
//	
//	String roomUrl = new String();
//	
//	try {
//		PreparedStatement stmt = breezeReportConnection.prepareStatement("SELECT url from room where scoId = " + scoId);
//		ResultSet rs = stmt.executeQuery();
//		
//		while(rs.next()) {
//			roomUrl = rs.getString("url");
//		}
//		
//		rs.close();
//		stmt.close();
//		return roomUrl;
//	}
//	catch(SQLException e) {
//		throw new RuntimeException(e);
//	}
//}



