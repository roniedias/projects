package br.com.connectDashboard.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

//import javax.naming.Context;
//import javax.naming.InitialContext;
//import javax.naming.NamingException;
//import org.apache.tomcat.jdbc.pool.DataSource;

import br.com.connectDashboard.model.MaximoUsuariosPorData;
import br.com.connectDashboard.model.ResponsavelSala;
import br.com.connectDashboard.model.Sala;
import br.com.connectDashboard.model.UsuariosOnlineSala;
import br.com.connectDashboard.factory.ConnectionFactory;


public class Dao {

	
	
//	private DataSource datasource;
	
	
//	public Dao() {
//		
//		
//		
//		
//        try {				
//        	// get dataSource
//        	Context initialContext = new InitialContext();
//	        datasource = (DataSource)initialContext.lookup("java:comp/env/jdbc/breeze");
//        }
//	    catch (NamingException ne) {
//	    	ne.printStackTrace();
//	    }
//
//	}
	
	
	public ArrayList<Sala> getTodasAsSalas() {
		
		
		ArrayList<Sala> salas = new ArrayList<Sala>();
		
		try {
			
			Connection connection = new ConnectionFactory().getConnection();
			
//			Connection connection = datasource.getConnection();
		
			PreparedStatement stmt = connection.prepareStatement("SELECT SCO_ID, NAME FROM PPS_SCOS WHERE icon = 3 AND DISABLED IS NULL AND NAME NOT LIKE '{default%' order by NAME");
			ResultSet rs = stmt.executeQuery();
			
			while(rs.next()) {
				Sala sala = new Sala();
				sala.setScoId(rs.getString("SCO_ID"));
				sala.setNome(rs.getString("NAME").replaceAll("\\\\", "\\\\\\\\").replaceAll("\""," ").replaceAll("–", "-"));
				salas.add(sala);
			}
			
			rs.close();
			stmt.close();
			if (connection != null) 
				try {
					connection.close();
				}
				catch (Exception ignore) {}
			return salas;	
		}
		catch(SQLException e) {
			throw new RuntimeException(e);
		}		
		
	}
	
	
		
	
	
	public ArrayList<Sala> getSalasEmUsoNoMesAtual() {
		
		ArrayList<Sala> salas = new ArrayList<Sala>();
		try {
			
			Connection connection = new ConnectionFactory().getConnection();
			
			//Connection connection = datasource.getConnection();
			
				
			PreparedStatement stmt = connection.prepareStatement("SELECT DISTINCT s.sco_id, s.name FROM pps_transcript_counts tc, pps_scos s WHERE tc.acl_id = s.sco_id AND tc.DATE_CREATED BETWEEN(SELECT DATEADD(month, DATEDIFF(month, 0, GETDATE()), 0) AS PRIMEIRO_DIA_DO_MES) AND (SELECT GETDATE() AS DATA_ATUAL) ORDER BY NAME");
			ResultSet rs = stmt.executeQuery();
			
			while(rs.next()) {
				Sala sala = new Sala();
				sala.setScoId(rs.getString("SCO_ID"));
				sala.setNome(rs.getString("NAME").replaceAll("\\\\", "\\\\\\\\").replaceAll("\""," ").replaceAll("–", "-"));
				salas.add(sala);
			}
			
			rs.close();
			stmt.close();
			if (connection != null) 
				try {
					connection.close();
				}
				catch (Exception ignore) {}
			return salas;
		}
		catch(SQLException e) {
			throw new RuntimeException(e);
		}
	}

	
	
	public ArrayList<UsuariosOnlineSala> getUsuariosOnlineSala() {
		
		try {
			
			Connection connection = new ConnectionFactory().getConnection();
			
//			Connection connection = datasource.getConnection();
			
			ArrayList<UsuariosOnlineSala> uos = new ArrayList<UsuariosOnlineSala>();
			
			PreparedStatement stmt = connection.prepareStatement(
					
					"SELECT QUERY1.SCO_ID as scoId, QUERY1.name as nomeSala, total, hosts, apresentadores, convidados FROM (SELECT S.SCO_ID, NAME, URL_PATH, COUNT( T.PERMISSION_ID ) AS TOTAL FROM PPS_SCOS S INNER JOIN PPS_TRANSCRIPTS T ON S.SCO_ID = T.SCO_ID WHERE T.STATUS='I' and S.ICON = 3 AND S.DISABLED IS NULL AND S.NAME NOT LIKE '{default%' GROUP BY S.SCO_ID, URL_PATH, NAME) QUERY1 " +
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
	
				UsuariosOnlineSala usuariosOnlineSala = new UsuariosOnlineSala();
				usuariosOnlineSala.setScoId(rs.getString("scoId"));
				usuariosOnlineSala.setNomeSala(rs.getString("nomeSala").replaceAll("\\\\", "\\\\\\\\").replaceAll("\""," ").replaceAll("–", "-"));
				
				if(rs.getString("hosts") != null) // Evitando valores nulos no resultado
					usuariosOnlineSala.setHosts(rs.getString("hosts"));
				else
					usuariosOnlineSala.setHosts("0");
					
				if(rs.getString("apresentadores") != null) // Evitando valores nulos no resultado
					usuariosOnlineSala.setApresentadores(rs.getString("apresentadores"));
				else
					usuariosOnlineSala.setApresentadores("0");
				
				if(rs.getString("convidados") != null) // Evitando valores nulos no resultado
					usuariosOnlineSala.setConvidados(rs.getString("convidados"));
				else
					usuariosOnlineSala.setConvidados("0");
	
			
				usuariosOnlineSala.setTotal(rs.getString("total"));
				
				uos.add(usuariosOnlineSala);
			}
			rs.close();
			stmt.close();
			if (connection != null) 
				try {
					connection.close();
				}
				catch (Exception ignore) {}
			return uos;
		}
		catch(SQLException e) {
			throw new RuntimeException(e);
		}
	}
	
		
	
	// Retorna o maior número de participantes de uma sala de reunião, de CADA DIA, desde o primeiro dia do mês corrente até a data atual 
	public ArrayList<MaximoUsuariosPorData> getMaximoUsuariosData(String scoId) {
		
		ArrayList<MaximoUsuariosPorData> MaximoUsuariosPorDatas = new ArrayList<MaximoUsuariosPorData>();
		
		try {
			
			Connection connection = new ConnectionFactory().getConnection();
			
//			Connection connection = datasource.getConnection();
			
			PreparedStatement stmt = connection.prepareStatement("SELECT DISTINCT MAX(tc.transcript_count) OVER (PARTITION BY CONVERT(DATE, tc.date_created)) AS max_usuarios, CONVERT(DATE, tc.date_created) AS data FROM pps_transcript_counts tc, pps_scos s WHERE tc.acl_id = s.sco_id AND s.SCO_ID = " + scoId + "AND tc.DATE_CREATED BETWEEN(SELECT DATEADD(month, DATEDIFF(month, 0, GETDATE()), 0) AS PRIMEIRO_DIA_DO_MES) AND (SELECT GETDATE() AS DATA_ATUAL) ORDER BY data");	
			
			ResultSet rs = stmt.executeQuery();
			
			while(rs.next()) {
				MaximoUsuariosPorData maximoUsuariosPorData = new MaximoUsuariosPorData();
				maximoUsuariosPorData.setMaxUsuarios(rs.getString("max_usuarios"));
				
				String dataNaoFormatada = rs.getString("data");
				
				Timestamp dbSqlTimestamp = Timestamp.valueOf(dataNaoFormatada + " 00:00:00");
				Date dbSqlConverted = new Date(dbSqlTimestamp.getTime());
				DateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
				
				maximoUsuariosPorData.setData(formatter.format(dbSqlConverted));
				
				MaximoUsuariosPorDatas.add(maximoUsuariosPorData);
			}
			
			rs.close();
			stmt.close();
			if (connection != null) 
				try {
					connection.close();
				}
				catch (Exception ignore) {}
			return MaximoUsuariosPorDatas;
		}
		catch(SQLException e) {
			throw new RuntimeException(e);
		}
	}
	
	
	
	// Retorna o maior número de participantes de uma sala de reunião, de CADA DIA, baseando-se na data inicial, data final e nome da sala informados 
	public ArrayList<MaximoUsuariosPorData> getMaximoUsuariosData(String dataInicial, String dataFinal, String scoId) {
		
		ArrayList<MaximoUsuariosPorData> MaximoUsuariosPorDatas = new ArrayList<MaximoUsuariosPorData>();
		
		try {
			
			Connection connection = new ConnectionFactory().getConnection();
			
//			Connection connection = datasource.getConnection();
	
			
			PreparedStatement stmt = connection.prepareStatement("SELECT DISTINCT MAX(tc.transcript_count) OVER (PARTITION BY CONVERT(DATE, tc.date_created)) AS max_usuarios, CONVERT(DATE, tc.date_created) AS data FROM pps_transcript_counts tc, pps_scos s WHERE s.SCO_ID = " + scoId + " AND	tc.acl_id = s.sco_id AND tc.DATE_CREATED BETWEEN '" + dataInicial + "' AND '" + dataFinal + "' ORDER BY data");	
			
			ResultSet rs = stmt.executeQuery();
			
			while(rs.next()) {
				MaximoUsuariosPorData maximoUsuariosPorData = new MaximoUsuariosPorData();
				maximoUsuariosPorData.setMaxUsuarios(rs.getString("max_usuarios"));
				
				String dataNaoFormatada = rs.getString("data");
				
				Timestamp dbSqlTimestamp = Timestamp.valueOf(dataNaoFormatada + " 00:00:00");
				Date dbSqlConverted = new Date(dbSqlTimestamp.getTime());
				DateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
				
				maximoUsuariosPorData.setData(formatter.format(dbSqlConverted));
				
				MaximoUsuariosPorDatas.add(maximoUsuariosPorData);
			}
			
			rs.close();
			stmt.close();
			if (connection != null) 
				try {
					connection.close();
				}
				catch (Exception ignore) {}
			return MaximoUsuariosPorDatas;
		}
		catch(SQLException e) {
			throw new RuntimeException(e);
		}
	}


	
	
	public ArrayList<Sala> getSalasFixas() {
		
		ArrayList<Sala> salas = new ArrayList<Sala>();
		
		try {
			
			Connection connection = new ConnectionFactory().getConnection();
			
//			Connection connection = datasource.getConnection();
			
			PreparedStatement stmt = connection.prepareStatement("SELECT SCO_ID, NAME FROM PPS_SCOS WHERE icon = 3 AND NAME like '%EU - %' OR NAME like '%ISR - %' AND DISABLED IS NULL ORDER BY NAME");
			ResultSet rs = stmt.executeQuery();
			
			while(rs.next()) {
				Sala sala = new Sala();
				sala.setScoId(rs.getString("SCO_ID"));
				sala.setNome(rs.getString("NAME").replaceAll("\\\\", "\\\\\\\\").replaceAll("\""," ").replaceAll("–", "-"));
				salas.add(sala);
			}
			
			rs.close();
			stmt.close();
			if (connection != null) 
				try {
					connection.close();
				}
				catch (Exception ignore) {}
			return salas;
		}
		catch(SQLException e) {
			throw new RuntimeException(e);
		}
	}
	
	
		

	public ArrayList<ResponsavelSala> getResponsaveisSala(String scoIdSala) {
		
		
		ArrayList<ResponsavelSala> responsaveisSala = new ArrayList<ResponsavelSala>();
		
		try {
				
			Connection connection = new ConnectionFactory().getConnection();
			
//			Connection connection = datasource.getConnection();
				
			PreparedStatement stmt = connection.prepareStatement("SELECT QUERY1.PRINCIPAL_ID, query1.nome, QUERY2.email FROM (SELECT  distinct p.principal_id, p.NAME as nome, LOGIN FROM pps_transcripts t, pps_scos s, pps_principals p WHERE t.sco_id = s.sco_id AND s.type = 3 AND t.permission_id = 10 AND t.status NOT IN ('X', 'N', 'R') AND t.principal_id is not null AND t.principal_id = p.principal_id AND s.sco_id = " + scoIdSala + " AND p.disabled is null) AS QUERY1 INNER JOIN (SELECT DISTINCT VALUE AS email, LOGIN FROM DBO.PPS_PRINCIPALS P, DBO.PPS_FIELDS C, DBO.PPS_ACL_FIELDS F WHERE P.ACCOUNT_ID = 7 AND P.DISABLED IS NULL AND P.HAS_CHILDREN = 0 AND F.ACL_ID = P.PRINCIPAL_ID AND C.FIELD_ID = F.FIELD_ID AND VALUE LIKE ('%@%')) AS QUERY2 ON QUERY1.LOGIN = QUERY2.LOGIN ORDER BY QUERY1.nome"); 

			ResultSet rs = stmt.executeQuery();
				
			while(rs.next()) {
				
				ResponsavelSala responsavelSala = new ResponsavelSala();
				
				responsavelSala.setPrincipalId(rs.getString("PRINCIPAL_ID"));
				responsavelSala.setNome(rs.getString("nome").toUpperCase());
				responsavelSala.setEmail(rs.getString("email"));
				
				responsaveisSala.add(responsavelSala);
			}
				
			rs.close();
			stmt.close();
			if (connection != null) 
				try {
					connection.close();
				}
				catch (Exception ignore) {}
			
			// Rotina de remoção de elementos repetidos -------------------
			String valAtual;
			for(int i = 0; i < responsaveisSala.size(); i++) {
				valAtual = responsaveisSala.get(i).getNome().replaceAll("\\s", "");
				for(int j = 0; j < responsaveisSala.size(); j++) 
					if(valAtual.equals(responsaveisSala.get(j).getNome().replaceAll("\\s", "")) && i != j)
							responsaveisSala.remove(j);
			}
			// ------------------------------------------------------------
			
			return responsaveisSala;
		}
		catch(SQLException e) {
			throw new RuntimeException(e);
		}
	}
	
	
	
}
