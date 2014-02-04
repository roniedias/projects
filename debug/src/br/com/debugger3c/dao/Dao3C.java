package br.com.debugger3c.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashSet;

import br.com.debugger3c.factory.ConnectionFactory;
import br.com.debugger3c.model.Client3C;
import br.com.debugger3c.model.ClientEnvironmentInfo;



public class Dao3C {
	
	private static final String ERROR_MSG = "Informações não disponíveis! Para executar esta rotina, é necessário que o ambiente esteja ATIVO ou PARCIALMENTE ATIVO e esteja com pelo menos uma opção de Monitoramento/Nagios = Sim.";
	
	private static Connection connection3C;
	
	public Dao3C() {
		connection3C = new ConnectionFactory().get3CConnection();
	}

	
	public ArrayList<Client3C> getTodosOsClientes3C() {
		
		try {
			ArrayList<Client3C> clients3C = new ArrayList<Client3C>();
			String sql = "SELECT ZBA_CLIENT, ZBA_LOJA, ZBA_DESCR FROM ZBA000 WHERE D_E_L_E_T_ = ' ' ";
			PreparedStatement stmt = connection3C.prepareStatement(sql);
			ResultSet rs = stmt.executeQuery();
			
			while(rs.next()) {
				
				Client3C client3C = new Client3C();
				client3C.setCodigo(rs.getString("ZBA_CLIENT"));
				client3C.setDescricao(rs.getString("ZBA_DESCR").trim());
				
				clients3C.add(client3C);
			}
			rs.close();
			stmt.close();
			return clients3C;			
		}
		catch(SQLException e) {
			throw new RuntimeException(e);
		}	
		
	}
	
	
	
	
	public ArrayList<ClientEnvironmentInfo> getInfoGeralAmbientesCliente(String codigoCliente) {

		try {
			ArrayList<ClientEnvironmentInfo> clientEnvironmentInfos = new ArrayList<ClientEnvironmentInfo>();		
			String sql = "Select zba.ZBA_CODAMB, zba.ZBA_DESCR, zbe.ZBE_TIPAMB, zb1.ZB1_DESCR, zbb.ZBB_PRODUT, zb3.ZB3_NREDUZ, zbd.ZBD_PARAM, zc1.ZC1_DESCR From ZBA000 zba Inner Join ZBE000 zbe On ( zbe.ZBE_FILIAL = zba.ZBA_FILIAL And zbe.ZBE_CODAMB = zba.ZBA_CODAMB And zbe.D_E_L_E_T_ = ' ' ) Inner Join ZB1000 zb1 On ( zb1.ZB1_FILIAL = zbe.ZBE_FILIAL And zb1.ZB1_CODIGO = zbe.ZBE_TIPAMB And zb1.D_E_L_E_T_ = ' ' ) Inner Join ZBB000 zbb On ( zbb.ZBB_FILIAL = zba.ZBA_FILIAL And zbb.ZBB_CODAMB = zba.ZBA_CODAMB And zbb.ZBB_TIPAMB = zbe.ZBE_TIPAMB And zbb.D_E_L_E_T_ = ' ' ) Inner Join ZB3000 zb3 On ( zb3.ZB3_FILIAL = zbb.ZBB_FILIAL And zb3.ZB3_CODIGO = zbb.ZBB_PRODUT And zb3.D_E_L_E_T_ = ' ' ) Inner Join ZBD000 zbd On ( zbd.ZBD_FILIAL = zba.ZBA_FILIAL And zbd.ZBD_CODAMB = zba.ZBA_CODAMB And zbd.ZBD_TIPAMB = zbe.ZBE_TIPAMB And zbd.ZBD_ITEM = zbb.ZBB_ITEM And zbd.D_E_L_E_T_ = ' ' ) Inner Join ZC1000 zc1 On ( zc1.ZC1_FILIAL = zbd.ZBD_FILIAL And zc1.ZC1_CODIGO = zbd.ZBD_PARAM  And zc1.D_E_L_E_T_ = ' ' ) Where zba.ZBA_FILIAL = '  ' And zba.ZBA_CLIENT = '" + codigoCliente +  "' And zbe.ZBE_STATUS Not In ('C','S','D','M','G') And zbd.ZBD_NAGIOS = 'S' And zba.D_E_L_E_T_ = ' ' Order By zba.ZBA_CODAMB, zbe.ZBE_TIPAMB, zbb.ZBB_ITEM, zbd.ZBD_PARAM";
			PreparedStatement stmt = connection3C.prepareStatement(sql);
			ResultSet rs = stmt.executeQuery();
			
			while(rs.next()) {
								
				ClientEnvironmentInfo clientEnvironmentInfo = new ClientEnvironmentInfo();
				clientEnvironmentInfo.setZbaCodAmb(rs.getString("ZBA_CODAMB"));
				clientEnvironmentInfo.setZbeTipAmb(rs.getString("ZBE_TIPAMB"));
				clientEnvironmentInfo.setZb1Descr(rs.getString("ZB1_DESCR"));
				clientEnvironmentInfo.setZbbProdut(rs.getString("ZBB_PRODUT"));
				clientEnvironmentInfo.setZbdParam(rs.getString("ZBD_PARAM"));
				clientEnvironmentInfo.setZc1Descr(rs.getString("ZC1_DESCR"));
				
				clientEnvironmentInfos.add(clientEnvironmentInfo);
				
			}
			rs.close();
			stmt.close();
			return clientEnvironmentInfos;			

		}
		catch(SQLException e) {
			throw new RuntimeException(e);
		}
		
	}
	
	

	
	public HashSet<String> getCodigosTiposAmbiente(String codigoCliente) {

		try {
			ArrayList<ClientEnvironmentInfo> clientEnvironmentInfos = new ArrayList<ClientEnvironmentInfo>();		
			String sql = "Select zba.ZBA_CODAMB, zba.ZBA_DESCR, zbe.ZBE_TIPAMB, zb1.ZB1_DESCR, zbb.ZBB_PRODUT, zb3.ZB3_NREDUZ, zbd.ZBD_PARAM, zc1.ZC1_DESCR From ZBA000 zba Inner Join ZBE000 zbe On ( zbe.ZBE_FILIAL = zba.ZBA_FILIAL And zbe.ZBE_CODAMB = zba.ZBA_CODAMB And zbe.D_E_L_E_T_ = ' ' ) Inner Join ZB1000 zb1 On ( zb1.ZB1_FILIAL = zbe.ZBE_FILIAL And zb1.ZB1_CODIGO = zbe.ZBE_TIPAMB And zb1.D_E_L_E_T_ = ' ' ) Inner Join ZBB000 zbb On ( zbb.ZBB_FILIAL = zba.ZBA_FILIAL And zbb.ZBB_CODAMB = zba.ZBA_CODAMB And zbb.ZBB_TIPAMB = zbe.ZBE_TIPAMB And zbb.D_E_L_E_T_ = ' ' ) Inner Join ZB3000 zb3 On ( zb3.ZB3_FILIAL = zbb.ZBB_FILIAL And zb3.ZB3_CODIGO = zbb.ZBB_PRODUT And zb3.D_E_L_E_T_ = ' ' ) Inner Join ZBD000 zbd On ( zbd.ZBD_FILIAL = zba.ZBA_FILIAL And zbd.ZBD_CODAMB = zba.ZBA_CODAMB And zbd.ZBD_TIPAMB = zbe.ZBE_TIPAMB And zbd.ZBD_ITEM = zbb.ZBB_ITEM And zbd.D_E_L_E_T_ = ' ' ) Inner Join ZC1000 zc1 On ( zc1.ZC1_FILIAL = zbd.ZBD_FILIAL And zc1.ZC1_CODIGO = zbd.ZBD_PARAM  And zc1.D_E_L_E_T_ = ' ' ) Where zba.ZBA_FILIAL = '  ' And zba.ZBA_CLIENT = '" + codigoCliente +  "' And zbe.ZBE_STATUS Not In ('C','S','D','M','G') And zbd.ZBD_NAGIOS = 'S' And zba.D_E_L_E_T_ = ' ' Order By zba.ZBA_CODAMB, zbe.ZBE_TIPAMB, zbb.ZBB_ITEM, zbd.ZBD_PARAM";
			PreparedStatement stmt = connection3C.prepareStatement(sql);
			ResultSet rs = stmt.executeQuery();
			
			while(rs.next()) {
								
				ClientEnvironmentInfo clientEnvironmentInfo = new ClientEnvironmentInfo();
				clientEnvironmentInfo.setZbaCodAmb(rs.getString("ZBA_CODAMB"));
				clientEnvironmentInfo.setZbeTipAmb(rs.getString("ZBE_TIPAMB"));
				clientEnvironmentInfo.setZb1Descr(rs.getString("ZB1_DESCR"));
				clientEnvironmentInfo.setZbbProdut(rs.getString("ZBB_PRODUT"));
				clientEnvironmentInfo.setZbdParam(rs.getString("ZBD_PARAM"));
				clientEnvironmentInfo.setZc1Descr(rs.getString("ZC1_DESCR"));
				
				clientEnvironmentInfos.add(clientEnvironmentInfo);
				
			}
			rs.close();
			stmt.close();
			
			
			HashSet<String> envTypes = new HashSet<String>();
			
			for(ClientEnvironmentInfo c : clientEnvironmentInfos) {
				envTypes.add(c.getZbeTipAmb() + " " + c.getZb1Descr().trim()); // 01 - PRODUCAO, 04 - HOMOLOGACAO
			}
			
			if(envTypes.size() == 0) {
				envTypes.add(ERROR_MSG);
			}
			
			return envTypes;			

		}
		catch(SQLException e) {
			throw new RuntimeException(e);
		}
		
	}
	
	
	
	
	public HashSet<String> getCodigosProdutos(String codigoCliente, String codigoTipoAmb) {
		
		try {

			ArrayList<ClientEnvironmentInfo> clientEnvironmentInfos = new ArrayList<ClientEnvironmentInfo>();		
			String sql = "Select zba.ZBA_CODAMB, zba.ZBA_DESCR, zbe.ZBE_TIPAMB, zb1.ZB1_DESCR, zbb.ZBB_PRODUT, zb3.ZB3_NREDUZ, zbd.ZBD_PARAM, zc1.ZC1_DESCR From ZBA000 zba Inner Join ZBE000 zbe On ( zbe.ZBE_FILIAL = zba.ZBA_FILIAL And zbe.ZBE_CODAMB = zba.ZBA_CODAMB And zbe.D_E_L_E_T_ = ' ' ) Inner Join ZB1000 zb1 On ( zb1.ZB1_FILIAL = zbe.ZBE_FILIAL And zb1.ZB1_CODIGO = zbe.ZBE_TIPAMB And zb1.D_E_L_E_T_ = ' ' ) Inner Join ZBB000 zbb On ( zbb.ZBB_FILIAL = zba.ZBA_FILIAL And zbb.ZBB_CODAMB = zba.ZBA_CODAMB And zbb.ZBB_TIPAMB = zbe.ZBE_TIPAMB And zbb.D_E_L_E_T_ = ' ' ) Inner Join ZB3000 zb3 On ( zb3.ZB3_FILIAL = zbb.ZBB_FILIAL And zb3.ZB3_CODIGO = zbb.ZBB_PRODUT And zb3.D_E_L_E_T_ = ' ' ) Inner Join ZBD000 zbd On ( zbd.ZBD_FILIAL = zba.ZBA_FILIAL And zbd.ZBD_CODAMB = zba.ZBA_CODAMB And zbd.ZBD_TIPAMB = zbe.ZBE_TIPAMB And zbd.ZBD_ITEM = zbb.ZBB_ITEM And zbd.D_E_L_E_T_ = ' ' ) Inner Join ZC1000 zc1 On ( zc1.ZC1_FILIAL = zbd.ZBD_FILIAL And zc1.ZC1_CODIGO = zbd.ZBD_PARAM  And zc1.D_E_L_E_T_ = ' ' ) Where zba.ZBA_FILIAL = '  ' And zba.ZBA_CLIENT = '" + codigoCliente +  "' And zbe.ZBE_TIPAMB = '" + codigoTipoAmb + "' And zbe.ZBE_STATUS Not In ('C','S','D','M','G') And zbd.ZBD_NAGIOS = 'S' And zba.D_E_L_E_T_ = ' ' Order By zba.ZBA_CODAMB, zbe.ZBE_TIPAMB, zbb.ZBB_ITEM, zbd.ZBD_PARAM";
			PreparedStatement stmt = connection3C.prepareStatement(sql);
			ResultSet rs = stmt.executeQuery();
			
			while(rs.next()) {
								
				ClientEnvironmentInfo clientEnvironmentInfo = new ClientEnvironmentInfo();
				clientEnvironmentInfo.setZbaCodAmb(rs.getString("ZBA_CODAMB"));
				clientEnvironmentInfo.setZbeTipAmb(rs.getString("ZBE_TIPAMB"));
				clientEnvironmentInfo.setZb1Descr(rs.getString("ZB1_DESCR"));
				clientEnvironmentInfo.setZbbProdut(rs.getString("ZBB_PRODUT"));
				clientEnvironmentInfo.setZbdParam(rs.getString("ZBD_PARAM"));
				clientEnvironmentInfo.setZc1Descr(rs.getString("ZC1_DESCR"));
				
				clientEnvironmentInfos.add(clientEnvironmentInfo);
				
			}
			rs.close();
			stmt.close();
			
			HashSet<String> productCodes = new HashSet<String>();
			
			for(ClientEnvironmentInfo c : clientEnvironmentInfos) {
				productCodes.add(c.getZbbProdut()); 
			}
			
			if(productCodes.size() == 0) {
				productCodes.add(ERROR_MSG);
			}
			
			return productCodes;			


		}
		catch(SQLException e) {
			throw new RuntimeException(e);
		}
		
	}
	
	
	
	
	public ArrayList<String> getTiposServicos(String codigoCliente, String codigoTipoAmb, String codigoProduto) {
		ArrayList<String> tiposServicos = new ArrayList<String>();
		String sql = "Select zbd.ZBD_PARAM, zc1.ZC1_DESCR From ZBA000 zba Inner Join ZBE000 zbe On ( zbe.ZBE_FILIAL = zba.ZBA_FILIAL And zbe.ZBE_CODAMB = zba.ZBA_CODAMB And zbe.D_E_L_E_T_ = ' ' ) Inner Join ZB1000 zb1 On ( zb1.ZB1_FILIAL = zbe.ZBE_FILIAL And zb1.ZB1_CODIGO = zbe.ZBE_TIPAMB And zb1.D_E_L_E_T_ = ' ' ) Inner Join ZBB000 zbb On ( zbb.ZBB_FILIAL = zba.ZBA_FILIAL And zbb.ZBB_CODAMB = zba.ZBA_CODAMB And zbb.ZBB_TIPAMB = zbe.ZBE_TIPAMB And zbb.D_E_L_E_T_ = ' ' ) Inner Join ZB3000 zb3 On ( zb3.ZB3_FILIAL = zbb.ZBB_FILIAL And zb3.ZB3_CODIGO = zbb.ZBB_PRODUT And zb3.D_E_L_E_T_ = ' ' ) Inner Join ZBD000 zbd On ( zbd.ZBD_FILIAL = zba.ZBA_FILIAL And zbd.ZBD_CODAMB = zba.ZBA_CODAMB And zbd.ZBD_TIPAMB = zbe.ZBE_TIPAMB And zbd.ZBD_ITEM = zbb.ZBB_ITEM And zbd.D_E_L_E_T_ = ' ' ) Inner Join ZC1000 zc1 On ( zc1.ZC1_FILIAL = zbd.ZBD_FILIAL And zc1.ZC1_CODIGO = zbd.ZBD_PARAM  And zc1.D_E_L_E_T_ = ' ' ) Where zba.ZBA_FILIAL = '  ' And zba.ZBA_CLIENT = '" + codigoCliente +  "' And zbe.ZBE_TIPAMB = '" + codigoTipoAmb +  "' And zbb.ZBB_PRODUT = '" + codigoProduto +  "' And zbe.ZBE_STATUS Not In ('C','S','D','M','G') And zbd.ZBD_NAGIOS = 'S' And zba.D_E_L_E_T_ = ' ' Order By zba.ZBA_CODAMB, zbe.ZBE_TIPAMB, zbb.ZBB_ITEM, zbd.ZBD_PARAM";
		
		try {
			
			PreparedStatement stmt = connection3C.prepareStatement(sql);
			ResultSet rs = stmt.executeQuery();
			
			while(rs.next()) {
				tiposServicos.add(rs.getString("ZBD_PARAM") + " " + rs.getString("ZC1_DESCR"));
				
			}
			rs.close();
			stmt.close();

			return tiposServicos;
		}
		catch(SQLException e) {
			throw new RuntimeException(e);
		}

	}


	
	
	
	public String getCodigoAmbiente(String codigoCliente) {

		try {
			String codigoAmbiente = new String();
			String sql = "Select zba.ZBA_CODAMB, zba.ZBA_DESCR, zbe.ZBE_TIPAMB, zb1.ZB1_DESCR, zbb.ZBB_PRODUT, zb3.ZB3_NREDUZ, zbd.ZBD_PARAM, zc1.ZC1_DESCR From ZBA000 zba Inner Join ZBE000 zbe On ( zbe.ZBE_FILIAL = zba.ZBA_FILIAL And zbe.ZBE_CODAMB = zba.ZBA_CODAMB And zbe.D_E_L_E_T_ = ' ' ) Inner Join ZB1000 zb1 On ( zb1.ZB1_FILIAL = zbe.ZBE_FILIAL And zb1.ZB1_CODIGO = zbe.ZBE_TIPAMB And zb1.D_E_L_E_T_ = ' ' ) Inner Join ZBB000 zbb On ( zbb.ZBB_FILIAL = zba.ZBA_FILIAL And zbb.ZBB_CODAMB = zba.ZBA_CODAMB And zbb.ZBB_TIPAMB = zbe.ZBE_TIPAMB And zbb.D_E_L_E_T_ = ' ' ) Inner Join ZB3000 zb3 On ( zb3.ZB3_FILIAL = zbb.ZBB_FILIAL And zb3.ZB3_CODIGO = zbb.ZBB_PRODUT And zb3.D_E_L_E_T_ = ' ' ) Inner Join ZBD000 zbd On ( zbd.ZBD_FILIAL = zba.ZBA_FILIAL And zbd.ZBD_CODAMB = zba.ZBA_CODAMB And zbd.ZBD_TIPAMB = zbe.ZBE_TIPAMB And zbd.ZBD_ITEM = zbb.ZBB_ITEM And zbd.D_E_L_E_T_ = ' ' ) Inner Join ZC1000 zc1 On ( zc1.ZC1_FILIAL = zbd.ZBD_FILIAL And zc1.ZC1_CODIGO = zbd.ZBD_PARAM  And zc1.D_E_L_E_T_ = ' ' ) Where zba.ZBA_FILIAL = '  ' And zba.ZBA_CLIENT = '" + codigoCliente +  "' And zbe.ZBE_STATUS Not In ('C','S','D','M','G') And zbd.ZBD_NAGIOS = 'S' And zba.D_E_L_E_T_ = ' ' Order By zba.ZBA_CODAMB, zbe.ZBE_TIPAMB, zbb.ZBB_ITEM, zbd.ZBD_PARAM";
			PreparedStatement stmt = connection3C.prepareStatement(sql);
			ResultSet rs = stmt.executeQuery();
			
			while(rs.next()) {

				codigoAmbiente = rs.getString("ZBA_CODAMB");
			}
			rs.close();
			stmt.close();
			
			if(codigoAmbiente.isEmpty()) {
				codigoAmbiente = ERROR_MSG; 
			}
			
			return codigoAmbiente;			

		}
		catch(SQLException e) {
			throw new RuntimeException(e);
		}
		
	}
	
	
	

	public String getCodigoEmpresa(String codigoCliente) {

		try {
			String codigoEmpresa = new String();
			String sql = "SELECT ZBA_LOJA FROM ZBA000 WHERE D_E_L_E_T_ = ' ' AND ZBA_CLIENT = '" + codigoCliente + "'";
			PreparedStatement stmt = connection3C.prepareStatement(sql);
			ResultSet rs = stmt.executeQuery();
			
			while(rs.next()) {
				codigoEmpresa = rs.getString("ZBA_LOJA");
			}
			rs.close();
			stmt.close();
			
			if(codigoEmpresa.isEmpty()) {
				codigoEmpresa = ERROR_MSG; 
			}
			
			return codigoEmpresa;			

		}
		catch(SQLException e) {
			throw new RuntimeException(e);
		}
		
	}

	
	
	
	public String getNomeProduto(String codigoProduto) {
		
		try {
		
			String zb3NReduz = new String();
			String zb3Versao = new String();
			
			String sql = "SELECT ZB3_NREDUZ, ZB3_VERSAO FROM ZB3000 WHERE ZB3_CODIGO = '" + codigoProduto + "'";
			PreparedStatement stmt = connection3C.prepareStatement(sql);
			ResultSet rs = stmt.executeQuery();
			
			while(rs.next()) {
				zb3NReduz = rs.getString("ZB3_NREDUZ").trim();
				zb3Versao = rs.getString("ZB3_VERSAO");
				
			}
			rs.close();
			stmt.close();

			
			return codigoProduto + " " + zb3NReduz + " " + zb3Versao;
		}
		catch(SQLException e) {
			throw new RuntimeException(e);
		}

		
	}	
	
	
	
	
	

	
	
	

}




	

	
	
