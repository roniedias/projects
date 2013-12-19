package br.com.breezeReport.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;



//import br.com.breezeReport.factory.ConnectionFactory;
import br.com.breezeReport.factory.ConnectionFactoryDS;
import br.com.breezeReport.model.HostRoom;
import br.com.breezeReport.model.Room;
import br.com.breezeReport.model.RoomInfo;
import br.com.breezeReport.util.ConverteTimestamp;



public class PersistenceDaoDS {
	
	private static Connection breezeReportConnection;
	
	
	public PersistenceDaoDS() {
		breezeReportConnection = new ConnectionFactoryDS().getBreezeReportConnection();
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
			PreparedStatement stmt = breezeReportConnection.prepareStatement("SELECT min(dataHora) AS dataHora FROM roomInfo ri INNER JOIN room r ON ri.scoId = r.scoId WHERE ri.scoId = '" +  scoId + "'");
			
			ResultSet rs = stmt.executeQuery();
			
			while(rs.next()) {
				beginDate = rs.getString("dataHora");
			}
	
			rs.close();
			stmt.close();
			return beginDate;
		}
		catch(SQLException e) {
			throw new RuntimeException(e);
		}
	}
	
	
	
	public String getRoomEndDate(long scoId) {
		
		String endDate = new String();
		
		try {
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

	
	
	
	public HostRoom getHostByRoomScoId(long roomScoId) {
		
		try {
			HostRoom hostRoom = new HostRoom();
			PreparedStatement stmt = breezeReportConnection.prepareStatement("SELECT * FROM hostRoom where roomScoId = '" + roomScoId + "'");
			ResultSet rs = stmt.executeQuery();
			
			while(rs.next()) {
				
				hostRoom.setDataHora(rs.getString("dataHora"));
				hostRoom.setLogin(rs.getString("login"));
				hostRoom.setRoomScoId(rs.getLong("roomScoId"));
				hostRoom.setDataHora(rs.getString("dataHora"));
				hostRoom.setDesabilitado(rs.getString("desabilitado"));
			}
			rs.close();
			stmt.close();
			return hostRoom;
		}
		catch(SQLException e) {
			throw new RuntimeException(e);
		}
	}
		
		
	public ArrayList<HostRoom> getAllHostRooms() {
			
		try {
				
			ArrayList<HostRoom> hostRooms = new ArrayList<HostRoom>();
				
			PreparedStatement stmt = breezeReportConnection.prepareStatement("SELECT * FROM hostRoom WHERE desabilitado IS NULL");
			ResultSet rs = stmt.executeQuery();
				
			while(rs.next()) {
					
				HostRoom hostRoom = new HostRoom();
				hostRoom.setDataHora(rs.getString("dataHora"));
				hostRoom.setLogin(rs.getString("login"));
				hostRoom.setRoomScoId(rs.getLong("roomScoId"));
				hostRoom.setDataHora(rs.getString("dataHora"));
				hostRoom.setDesabilitado(rs.getString("desabilitado"));
					
				hostRooms.add(hostRoom);
					
			}
			rs.close();
			stmt.close();
			return hostRooms;
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
	

}


