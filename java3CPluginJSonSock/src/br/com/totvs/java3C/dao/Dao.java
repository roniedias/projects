package br.com.totvs.java3C.dao;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

import br.com.totvs.java3C.bean.Cliente;
import br.com.totvs.java3C.bean.ItemAmbiente;
import br.com.totvs.java3C.bean.Servico;
import br.com.totvs.java3C.bean.TipoAmbiente;
import br.com.totvs.java3C.bean.TipoServico;
import br.com.totvs.java3C.bean.datasul.AppServer;
import br.com.totvs.java3C.bean.datasul.AtalhoInfo;
import br.com.totvs.java3C.bean.datasul.Banco;
import br.com.totvs.java3C.factory.DbConnectionFactory;


public class Dao {
	
	private Connection connection;
	
	public Dao() {
		connection = new DbConnectionFactory().getConnection();
	}
	
	// Este método retorna o código do cliente, o nome do cliente e a loja, a partir do código do ambiente informado como parâmetro  
	public Cliente getCliente(String codAmbiente) {
//		Connection connection = new DbConnectionFactory().getConnection();
		Cliente cliente = new Cliente();
		Statement stmt;
		ResultSet rs;
		try {			
			//String sql = "SELECT ZBA_CLIENT, ZBA_DESCR, ZBA_LOJA FROM ZBA000 WHERE D_E_L_E_T_ = ' ' AND ZBA_CODAMB = '" + codAmbiente + "'";
			String sql = "SELECT ZBA_CLIENT, ZBA_DESCR, ZBA_LOJA FROM ZBA000 WHERE ZBA_FILIAL = '  ' AND D_E_L_E_T_ = ' ' AND ZBA_CODAMB = '" + codAmbiente + "'";
			stmt = connection.createStatement();
			rs = stmt.executeQuery(sql);
			if(rs.next()) {
				cliente.setCodigo(rs.getString("ZBA_CLIENT"));
				cliente.setNome(rs.getString("ZBA_DESCR").trim());
				cliente.setLoja(rs.getString("ZBA_LOJA").trim());
			}
			rs.close();
			stmt.close();			
		}
		catch(SQLException e) {
			throw new RuntimeException(e);
		}	
		return cliente;
	}
	
	
	// Este método retorna o código/nome dos tipos de ambiente (Produção, Homologação, Teste) cadastrados no ambiente 
	//do cliente e seu status (A = ATIVO, C = CADASTRO, S = SUSPENSO, etc.), a partir do código do ambiente informado 
	//como parâmetro
	public ArrayList<TipoAmbiente> getTiposAmbiente(String codAmbiente) {
		//Connection connection = new DbConnectionFactory().getConnection();
		Statement stmt;
		ResultSet rs;
		TipoAmbiente tipoAmbiente;
		ArrayList<TipoAmbiente> tiposAmbiente = new ArrayList<TipoAmbiente>();
		try {			
			//String sql = "SELECT ZBE_TIPAMB, ZB1_DESCR, ZBE_STATUS FROM ZBE000 ZBE INNER JOIN ZB1000 ZB1 ON ZBE.ZBE_TIPAMB = ZB1.ZB1_CODIGO WHERE ZBE_CODAMB = '" + codAmbiente + "' AND ZBE.D_E_L_E_T_ = ' ' AND ZB1.D_E_L_E_T_ = ' '";
			String sql = "SELECT ZBE.ZBE_TIPAMB, ZB1.ZB1_DESCR, ZBE.ZBE_STATUS FROM ZBE000 ZBE INNER JOIN ZB1000 ZB1 ON ZBE.ZBE_FILIAL = ZB1.ZB1_FILIAL AND ZBE.ZBE_TIPAMB = ZB1.ZB1_CODIGO AND ZB1.D_E_L_E_T_ = ' ' WHERE ZBE.ZBE_FILIAL = '  ' AND ZBE_CODAMB = '" + codAmbiente + "' AND ZBE.D_E_L_E_T_ = ' '";
			stmt = connection.createStatement();
			rs = stmt.executeQuery(sql);
			while(rs.next()) {
				tipoAmbiente = new TipoAmbiente();
				tipoAmbiente.setCodigo(rs.getString("ZBE_TIPAMB"));
				tipoAmbiente.setNome(rs.getString("ZB1_DESCR").trim());
				tipoAmbiente.setStatus(rs.getString("ZBE_STATUS"));
				tiposAmbiente.add(tipoAmbiente);
			}
			rs.close();
			stmt.close();			
		}
		catch(SQLException e) {
			throw new RuntimeException(e);
		}	
		return tiposAmbiente;
	}
	
	
	// Este método retorna as informações dos itens do ambiente, como por exemplo o produto (PROTHEUS, PROTHEUS TSS, 
	// AUDIT TRAIL, etc.), a partir do código do ambiente e do código do tipo de ambiente (01 = PRODUÇÃO, 
	// 04 = HOMOLOGAÇÃO, etc.) informados como parâmetros
	public ArrayList<ItemAmbiente> getItensAmbiente(String codAmbiente, String codTipoAmbiente) {
		//Connection connection = new DbConnectionFactory().getConnection();
		Statement stmt;
		ResultSet rs;
		ItemAmbiente itemAmbiente;
		ArrayList<ItemAmbiente> itensAmbiente = new ArrayList<ItemAmbiente>();
		try {			
			//String sql = "SELECT ZBB_ITEM, ZBB_PRODUT, ZB3_NREDUZ, ZB3_VERSAO, ZBB_PARNAG, ZBB_AMBPAI, ZBB_STATUS FROM ZBB000 AS ZBB INNER JOIN ZB3000 AS ZB3 ON ZBB.ZBB_PRODUT = ZB3.ZB3_CODIGO WHERE ZBB_CODAMB = '" + codAmbiente + "' AND ZBB_TIPAMB = '" + codTipoAmbiente + "' AND ZBB.D_E_L_E_T_ = ' ' AND ZB3.D_E_L_E_T_ = ' '";
			String sql = "SELECT ZBB_ITEM, ZBB_PRODUT, ZB3_NREDUZ, ZB3_VERSAO, ZBB_PARNAG, ZBB_AMBPAI, ZBB_STATUS FROM ZBB000 AS ZBB INNER JOIN ZB3000 AS ZB3 ON ZB3.ZB3_FILIAL = ZBB.ZBB_FILIAL AND ZBB.ZBB_PRODUT = ZB3.ZB3_CODIGO AND ZB3.D_E_L_E_T_ = ' ' WHERE ZBB.ZBB_FILIAL = '  ' AND ZBB_CODAMB = '" + codAmbiente + "' AND ZBB_TIPAMB = '" + codTipoAmbiente + "' AND ZBB.D_E_L_E_T_ = ' '";
			stmt = connection.createStatement();
			rs = stmt.executeQuery(sql);
			while(rs.next()) {
				itemAmbiente = new ItemAmbiente();
				itemAmbiente.setCodItem(rs.getString("ZBB_ITEM"));
				itemAmbiente.setCodProduto(rs.getString("ZBB_PRODUT"));
				itemAmbiente.setNomeProduto(rs.getString("ZB3_NREDUZ"));
				itemAmbiente.setVersaoProduto(rs.getString("ZB3_VERSAO"));
				itemAmbiente.setEmpresaFilial(rs.getString("ZBB_PARNAG"));
				itemAmbiente.setAmbientePai(rs.getString("ZBB_AMBPAI").trim());
				itemAmbiente.setStatus(rs.getString("ZBB_STATUS"));				
				itensAmbiente.add(itemAmbiente);
			}
			rs.close();
			stmt.close();			
		}
		catch(SQLException e) {
			throw new RuntimeException(e);
		}	
		return itensAmbiente;
	}
	
	
	/*
	 * ========== Métodos Específicos Produto Protheus =============================================	
	 */

	
		// Este método retorna o código/nome dos Tipos de Serviços e o campo informando se monitora (S/N), a
	// partir do código do ambiente, código do tipo de ambiente e código do item, informados como parâmetros
	public ArrayList<TipoServico> getTiposServico(String codAmbiente, String codTipoAmbiente, String codProduto) {
		//Connection connection = new DbConnectionFactory().getConnection();
		Statement stmt;
		ResultSet rs;
		TipoServico tipoServico;
		ArrayList<TipoServico> tiposServico = new ArrayList<TipoServico>();
		try {			
			//String sql = "SELECT ZBH_TIPSRV, ZB4_DESCR, ZBH_MONITO FROM ZBH000 AS ZBH INNER JOIN ZB4000 AS ZB4 ON ZBH.ZBH_TIPSRV = ZB4.ZB4_CODIGO WHERE ZBH_CODAMB = '" + codAmbiente + "' AND ZBH_TIPAMB = '" + codTipoAmbiente + "' AND ZBH_ITEM = '" + codItem + "' AND ZBH.D_E_L_E_T_ = ' ' AND ZB4.D_E_L_E_T_ = ' '";
			String sql = "Select zbh.ZBH_TIPSRV, zb4.ZB4_DESCR, zbb.ZBB_PRODUT From ZBA000 zba Inner Join ZBE000 zbe On ( zbe.ZBE_FILIAL = zba.ZBA_FILIAL And zbe.ZBE_CODAMB = zba.ZBA_CODAMB And zbe.D_E_L_E_T_ = ' ' ) Inner Join ZB1000 zb1 On ( zb1.ZB1_FILIAL = zbe.ZBE_FILIAL And zb1.ZB1_CODIGO = zbe.ZBE_TIPAMB And zb1.D_E_L_E_T_ = ' ' ) Inner Join ZBB000 zbb On ( zbb.ZBB_FILIAL = zba.ZBA_FILIAL And zbb.ZBB_CODAMB = zba.ZBA_CODAMB And zbb.ZBB_TIPAMB = zbe.ZBE_TIPAMB And zbb.D_E_L_E_T_ = ' ' ) Inner Join ZBH000 zbh On ( zbh.ZBH_FILIAL = zbb.ZBB_FILIAL And zbh.ZBH_CODAMB = zba.ZBA_CODAMB And zbh.ZBH_TIPAMB = zbe.ZBE_TIPAMB And zbh.ZBH_ITEM = zbb.ZBB_ITEM And zbh.D_E_L_E_T_ = ' ' ) Inner Join ZB4000 zb4 On ( zb4.ZB4_FILIAL = zbh.ZBH_FILIAL And zb4.ZB4_CODIGO = zbh.ZBH_TIPSRV And zb4.D_E_L_E_T_ = ' ' ) Where zba.ZBA_FILIAL = '  ' And zba.ZBA_CODAMB = '" + codAmbiente + "' And zbe.ZBE_TIPAMB = '" + codTipoAmbiente + "' And zbb.ZBB_PRODUT = '" + codProduto + "' And zba.D_E_L_E_T_ = ' ' AND Zbh.ZBH_MONITO = 'S' Order By zba.ZBA_CODAMB, zbe.ZBE_TIPAMB, zbb.ZBB_ITEM";
			stmt = connection.createStatement();
			rs = stmt.executeQuery(sql);
			while(rs.next()) {
				tipoServico = new TipoServico();
				tipoServico.setCodigo(rs.getString("ZBH_TIPSRV"));
				tipoServico.setNome(rs.getString("ZB4_DESCR"));
				tiposServico.add(tipoServico);
			}
			rs.close();
			stmt.close();			
		}
		catch(SQLException e) {
			throw new RuntimeException(e);
		}	
		return tiposServico;		
	}
	
	
	// Este método retorna informações dos serviços, a partir do código do ambiente, código do tipo do ambiente, 
	// código do item e código do tipo de serviço informados como parâmetros
	
	//Obs: Na primeira subselect:
	
	//	SELECT ZAE.ZAE_IP AS ZBC_IPHOST 
	//	FROM ZAE000 AS ZAE WHERE ZAE.ZAE_HOST = "codigo do host" 
	//	AND ZAE.ZAE_FILIAL = '  ' AND ZAE.ZAE_PRINCI IN ('S','C') AND ZAE.D_E_L_E_T_ = ' ' 
	//	ORDER BY ZAE.ZAE_PRINCI, ZAE.R_E_C_N_O_ FETCH FIRST 1 ROW ONLY
	
	//  Extrai-se o IP da tabela ZAE, respeitando o seguinte critério: 
	//  1º O IP que tiver o valor "C" (cluster) na coluna ZAE_PRINCI, será retornado
	//  2º Senão, será retornado o IP que tiver o valor "S" ("SIM") na coluna ZAE_PRINCI
	//  3º Se nenhum IP estiver cadastrado com pelo menos uma das opções acima, nada é retornado.
	
	//    Obs: A própria aplicação se comporta desta maneira, ou seja, não exibe nada no campo
	//    "I.P. Host" de um determinado serviço (em "Serviços") caso, para aquele host, todos os IP´s 
	//    cadastrados estiverem com valor igual a "N" na coluna ZAE_PRINCI
	
	//    Obs2: Caso haja mais de um campo cadastrado com valor "C" (o que é permitido pela aplicação), 
	//          será retornado o primeiro que foi cadastrado no sistema (ordenado de acordo com o R_E_C_N_O_)
	
	public ArrayList<Servico> getServicos(String codAmbiente, String codTipoAmbiente, String codProduto, String codTipoServico) {
		//Connection connection = new DbConnectionFactory().getConnection();
		Statement stmt;
		ResultSet rs;
		Servico servico;
		ArrayList<Servico> servicos = new ArrayList<Servico>();
		try {			
//			String sql = "SELECT ZBC_SEQ, ZBC_ENVIRO, ZBC_BALANC, (SELECT ZAE.ZAE_IP AS ZBC_IPHOST FROM ZAE000 AS ZAE WHERE ZAE.ZAE_HOST IN (SELECT ZBC_HOST FROM ZBC000 WHERE ZBC_CODAMB = '" + codAmbiente + "' AND ZBC_TIPAMB = '" + codTipoAmbiente + "' AND ZBC_ITEM = '" + codItem + "' AND ZBC_TIPSRV = '" + codTipoServico + "' AND D_E_L_E_T_ = ' ' FETCH FIRST 1 ROW ONLY) AND ZAE.ZAE_FILIAL = '  ' AND ZAE.ZAE_PRINCI IN ('S','C') AND ZAE.D_E_L_E_T_ = ' ' ORDER BY ZAE.ZAE_PRINCI, ZAE.R_E_C_N_O_ FETCH FIRST 1 ROW ONLY), ZBC_IPEXT, ZBC_DNS, ZBC_PORTA, ZBC_INI, ZBC_INST FROM ZBC000 WHERE ZBC_CODAMB = '" + codAmbiente + "' AND ZBC_TIPAMB = '" + codTipoAmbiente + "' AND ZBC_ITEM = '" + codItem + "' AND ZBC_TIPSRV = '" + codTipoServico + "' AND D_E_L_E_T_ = ' '";
			String sql = "Select zbc.ZBC_SEQ, zbc.ZBC_ENVIRO, zbc.ZBC_BALANC, (Select zae1.ZAE_IP As ZBC_IPHOST From ZAE000 zae1 Where zae1.ZAE_HOST = zbc.ZBC_HOST And zae1.ZAE_FILIAL = '  ' And zae1.ZAE_PRINCI In ('S','C') And zae1.D_E_L_E_T_ = ' ' Order By zae1.ZAE_PRINCI, zae1.R_E_C_N_O_ FETCH FIRST 1 ROW ONLY), zbc.ZBC_IPEXT, zbc.ZBC_DNS, zbc.ZBC_PORTA, zbc.ZBC_INI, zbc.ZBC_INST, za6.ZA6_TOPNIC From ZBA000 zba Inner Join ZBE000 zbe On ( zbe.ZBE_FILIAL = zba.ZBA_FILIAL And zbe.ZBE_CODAMB = zba.ZBA_CODAMB And zbe.D_E_L_E_T_ = ' ' ) Inner Join ZBB000 zbb On ( zbb.ZBB_FILIAL = zba.ZBA_FILIAL And zbb.ZBB_CODAMB = zba.ZBA_CODAMB And zbb.ZBB_TIPAMB = zbe.ZBE_TIPAMB And zbb.D_E_L_E_T_ = ' ' ) Inner Join ZBH000 zbh On ( zbh.ZBH_FILIAL = zbh.ZBH_FILIAL And zbh.ZBH_CODAMB = zba.ZBA_CODAMB And zbh.ZBH_TIPAMB = zbe.ZBE_TIPAMB And zbh.ZBH_ITEM = zbb.ZBB_ITEM And zbh.D_E_L_E_T_ = ' ' ) Inner Join ZBC000 zbc On ( zbc.ZBC_FILIAL = zbh.ZBH_FILIAL And zbc.ZBC_CODAMB = zba.ZBA_CODAMB And zbc.ZBC_TIPAMB = zbe.ZBE_TIPAMB And zbC.ZBC_ITEM = zbb.ZBB_ITEM And zbc.ZBC_TIPSRV = zbh.ZBH_TIPSRV And zbc.D_E_L_E_T_ = ' ' ) Inner Join ZBL000 zbl On ( zbl.ZBL_FILIAL = zbh.ZBH_FILIAL And zbl.ZBL_CODAMB = zbb.ZBB_CODAMB And zbl.ZBL_TIPAMB = zbb.ZBB_TIPAMB And zbl.ZBL_ITEM = zbb.ZBB_ITEM And zbl.D_E_L_E_T_ = ' ' ) Inner Join ZAB000 zab On ( zab.ZAB_FILIAL = zbl.ZBL_FILIAL And zab.ZAB_HOST = zbl.ZBL_HOST And zab.ZAB_ITEM = zbl.ZBL_INSTAN And zab.D_E_L_E_T_ = ' ' ) Inner Join ZA6000 za6 On ( za6.ZA6_FILIAL = zab.ZAB_FILIAL And za6.ZA6_CODIGO = zab.ZAB_SGBD And za6.D_E_L_E_T_ = ' ' ) Where zba.ZBA_CODAMB = '" + codAmbiente + "' And zbe.ZBE_TIPAMB = '" + codTipoAmbiente + "' And zba.ZBA_FILIAL = '  ' And zbb.ZBB_PRODUT = '" + codProduto + "' And zbh.ZBH_TIPSRV = '" + codTipoServico + "' And zba.D_E_L_E_T_ = ' ' Order By zbc.ZBC_TIPAMB, zbc.ZBC_TIPSRV, zbc.ZBC_SEQ"; 
			stmt = connection.createStatement();
			rs = stmt.executeQuery(sql);
			while(rs.next()) {
				servico = new Servico();
				servico.setCodTipoServico(codTipoServico);
				servico.setSequencia(rs.getString("ZBC_SEQ"));
				servico.setEnvironment(rs.getString("ZBC_ENVIRO"));
				servico.setBalance(rs.getString("ZBC_BALANC"));
				servico.setIpHost(rs.getString("ZBC_IPHOST"));
				servico.setIpExterno(rs.getString("ZBC_IPEXT"));
				servico.setDns(rs.getString("ZBC_DNS"));
				servico.setPorta(rs.getString("ZBC_PORTA"));
				servico.setPathIni(rs.getString("ZBC_INI"));
				servico.setPathInstal(rs.getString("ZBC_INST"));
				servico.setDbAlias(rs.getString("ZA6_TOPNIC"));
				servicos.add(servico);
			}
			rs.close();
			stmt.close();			
		}
		catch(SQLException e) {
			throw new RuntimeException(e);
		}	
		return servicos;				
	}
	
	
	public ArrayList<Servico> getTodosOsServicos(String codAmbiente, String codTipoAmbiente, String codProduto) {
		//Connection connection = new DbConnectionFactory().getConnection();
		Statement stmt;
		ResultSet rs;
		Servico servico;
		ArrayList<Servico> servicos = new ArrayList<Servico>();
		try {			
			String sql = "Select zbh.ZBH_TIPSRV, zbc.ZBC_SEQ, zbc.ZBC_ENVIRO, zbc.ZBC_BALANC, (Select zae1.ZAE_IP As ZBC_IPHOST From ZAE000 zae1 Where zae1.ZAE_HOST = zbc.ZBC_HOST And zae1.ZAE_FILIAL = '  ' And zae1.ZAE_PRINCI In ('S','C') And zae1.D_E_L_E_T_ = ' ' Order By zae1.ZAE_PRINCI, zae1.R_E_C_N_O_ FETCH FIRST 1 ROW ONLY), zbc.ZBC_IPEXT, zbc.ZBC_DNS, zbc.ZBC_PORTA, zbc.ZBC_INI, zbc.ZBC_INST, za6.ZA6_TOPNIC From ZBA000 zba Inner Join ZBE000 zbe On ( zbe.ZBE_FILIAL = zba.ZBA_FILIAL And zbe.ZBE_CODAMB = zba.ZBA_CODAMB And zbe.D_E_L_E_T_ = ' ' ) Inner Join ZBB000 zbb On ( zbb.ZBB_FILIAL = zba.ZBA_FILIAL And zbb.ZBB_CODAMB = zba.ZBA_CODAMB And zbb.ZBB_TIPAMB = zbe.ZBE_TIPAMB And zbb.D_E_L_E_T_ = ' ' ) Inner Join ZBH000 zbh On ( zbh.ZBH_FILIAL = zbh.ZBH_FILIAL And zbh.ZBH_CODAMB = zba.ZBA_CODAMB And zbh.ZBH_TIPAMB = zbe.ZBE_TIPAMB And zbh.ZBH_ITEM = zbb.ZBB_ITEM And zbh.D_E_L_E_T_ = ' ' ) Inner Join ZBC000 zbc On ( zbc.ZBC_FILIAL = zbh.ZBH_FILIAL And zbc.ZBC_CODAMB = zba.ZBA_CODAMB And zbc.ZBC_TIPAMB = zbe.ZBE_TIPAMB And zbC.ZBC_ITEM = zbb.ZBB_ITEM And zbc.ZBC_TIPSRV = zbh.ZBH_TIPSRV And zbc.D_E_L_E_T_ = ' ' ) Inner Join ZBL000 zbl On ( zbl.ZBL_FILIAL = zbh.ZBH_FILIAL And zbl.ZBL_CODAMB = zbb.ZBB_CODAMB And zbl.ZBL_TIPAMB = zbb.ZBB_TIPAMB And zbl.ZBL_ITEM = zbb.ZBB_ITEM And zbl.D_E_L_E_T_ = ' ' ) Inner Join ZAB000 zab On ( zab.ZAB_FILIAL = zbl.ZBL_FILIAL And zab.ZAB_HOST = zbl.ZBL_HOST And zab.ZAB_ITEM = zbl.ZBL_INSTAN And zab.D_E_L_E_T_ = ' ' ) Inner Join ZA6000 za6 On ( za6.ZA6_FILIAL = zab.ZAB_FILIAL And za6.ZA6_CODIGO = zab.ZAB_SGBD And za6.D_E_L_E_T_ = ' ' ) Where zba.ZBA_CODAMB = '" + codAmbiente + "' And zbe.ZBE_TIPAMB = '" + codTipoAmbiente + "' And zba.ZBA_FILIAL = '  ' And zbb.ZBB_PRODUT = '" + codProduto + "'  And zba.D_E_L_E_T_ = ' ' Order By zbc.ZBC_TIPAMB, zbc.ZBC_TIPSRV, zbc.ZBC_SEQ";	
			stmt = connection.createStatement();
			rs = stmt.executeQuery(sql);
			while(rs.next()) {
				servico = new Servico();
				servico.setCodTipoServico(rs.getString("ZBH_TIPSRV"));
				servico.setSequencia(rs.getString("ZBC_SEQ"));
				servico.setEnvironment(rs.getString("ZBC_ENVIRO"));
				servico.setBalance(rs.getString("ZBC_BALANC"));
				servico.setIpHost(rs.getString("ZBC_IPHOST"));
				servico.setIpExterno(rs.getString("ZBC_IPEXT"));
				servico.setDns(rs.getString("ZBC_DNS"));
				servico.setPorta(rs.getString("ZBC_PORTA"));
				servico.setPathIni(rs.getString("ZBC_INI"));
				servico.setPathInstal(rs.getString("ZBC_INST"));
				servico.setDbAlias(rs.getString("ZA6_TOPNIC"));
				servicos.add(servico);
			}
			rs.close();
			stmt.close();			
		}
		catch(SQLException e) {
			throw new RuntimeException(e);
		}	
		return servicos;				
	}
	
	
/*
 * ========== Métodos Específicos Produto Datasul =============================================	
 */
	
	// Obtém-se apenas dados contidos na primeira linha de: Datasul > AppServer/WebSpeed > AppServer
	public ArrayList<AppServer> getAppServers(String codAmbiente, String codTipoAmbiente, String codProduto) {
		Statement stmt;
		ResultSet rs;
		ArrayList<AppServer> appServers = new ArrayList<AppServer>();
		try {			
			
			//String sql = "SELECT ZAE_IP, ZBR_PORTA, ZBR_NOME, ZAA_NREDUZ FROM ZBR000 AS ZBR INNER JOIN ZAE000 AS ZAE ON ZBR.ZBR_HOST = ZAE.ZAE_HOST INNER JOIN ZAA000 AS ZAA ON ZAE.ZAE_HOST = ZAA.ZAA_CODIGO WHERE ZBR.ZBR_CODAMB = '" + codAmbiente + "'  AND ZBR.D_E_L_E_T_ = ' ' AND ZAE.D_E_L_E_T_ = ' ' AND ZAA.D_E_L_E_T_ = ' ' FETCH FIRST ROW ONLY";
			String sql = "SELECT ZAE_IP, ZBR_PORTA, ZBR_NOME, ZAA_NREDUZ FROM ZBR000 AS ZBR INNER JOIN ZAE000 AS ZAE ON ZBR_HOST = ZAE_HOST INNER JOIN ZAA000 AS ZAA ON ZAE_HOST = ZAA_CODIGO INNER JOIN ZBB000 AS ZBB ON  ZBR_ITEM = ZBB_ITEM WHERE ZBR_CODAMB = '" + codAmbiente + "' AND ZBR_TIPAMB = '" + codTipoAmbiente + "' AND ZBB_PRODUT = '" + codProduto + "' AND ZBR.D_E_L_E_T_ = ' ' AND ZAE.D_E_L_E_T_ = ' ' AND ZAA.D_E_L_E_T_ = ' ' AND ZBB.D_E_L_E_T_ = ' ' FETCH FIRST ROW ONLY";
			stmt = connection.createStatement();
			rs = stmt.executeQuery(sql);
			while(rs.next()) {
				AppServer appServer = new AppServer();
				appServer.setIp(rs.getString("ZAE_IP"));
				appServer.setPorta(rs.getString("ZBR_PORTA"));
				appServer.setInstanciaApp(rs.getString("ZBR_NOME"));
				appServer.setNomeHost(rs.getString("ZAA_NREDUZ"));
				appServers.add(appServer);
			}
			rs.close();
			stmt.close();			
		}
		catch(SQLException e) {
			throw new RuntimeException(e);
		}	
		return appServers;				
	}
	
	
	// Obtém-se os Nomes Fisicos de todos os bancos em: Datasul > Bancos > Nome Fisico
	public ArrayList<Banco> getBancos(String codAmbiente, String codTipoAmbiente, String codProduto) {
		Statement stmt;
		ResultSet rs;
		ArrayList<Banco> bancos = new ArrayList<Banco>();
		String sql = "SELECT ZBO_NMFISI, ZBO_ALIAS, ZAE_IP, (SELECT ZBP_PORTA FROM ZBP000 WHERE ZBO_SEQ = ZBP_SEQBCO AND D_E_L_E_T_ = ' '), (SELECT ZBP_USER FROM ZBP000 WHERE ZBO_SEQ = ZBP_SEQBCO AND D_E_L_E_T_ = ' '), (SELECT ZBP_SENHA FROM ZBP000 WHERE ZBO_SEQ = ZBP_SEQBCO AND D_E_L_E_T_ = ' ') FROM ZBO000 AS ZBO INNER JOIN ZAE000 AS ZAE ON ZBO_HOST = ZAE_HOST INNER JOIN ZBB000 AS ZBB ON ZBO_CODAMB = ZBB_CODAMB WHERE ZBO_CODAMB = '" + codAmbiente + "' AND ZBO_TIPAMB = '" + codTipoAmbiente + "' AND ZBB_PRODUT = '" + codProduto + "' AND ZBO.D_E_L_E_T_ = ' ' AND ZAE.D_E_L_E_T_ = ' ' AND ZBB.D_E_L_E_T_ = ' '";
		try {
			stmt = connection.createStatement();
			rs = stmt.executeQuery(sql);
			while(rs.next()) {
				Banco banco = new Banco();
				banco.setNomeFisico(rs.getString("ZBO_NMFISI"));
				banco.setAlias(rs.getString("ZBO_ALIAS"));
				banco.setIp(rs.getString("ZAE_IP"));
				
				// Remover lógica após alteração do Jonas no 3C, para o campo porta em Datasul > Bancos > porta
				int limit = rs.getString("ZBP_PORTA").trim().lastIndexOf("0");
				banco.setPorta(rs.getString("ZBP_PORTA").replaceAll("\\.", " ").trim().substring(0, limit));  
				// =================================================================
				
				banco.setUsuario(rs.getString("ZBP_USER"));
				banco.setSenha(rs.getString("ZBP_SENHA"));
			
				// Remover código estático (extrair do banco) após alteração do Jonas no 3C 
				// Deve ser criado um campo Url Conexao em Datasul > Bancos > Bancos
				banco.setUrlConexao("jdbc:datadirect:openedge://");
				
				bancos.add(banco);
			}
			rs.close();
			stmt.close();
		}
		catch(SQLException e) {
			throw new RuntimeException(e);
		}	
		return bancos;
	}
	
	
	public AtalhoInfo getAtalhoInfo(String codAmbiente, String codTipoAmbiente, String codProduto) {
		Statement stmt;
		ResultSet rs;
		AtalhoInfo atalhoInfo = new AtalhoInfo();	
		String sql = "SELECT ZBQ_DIRPRO, ZBQ_PARCLI, ZBQ_PF, ZBQ_RAIZCL FROM ZBQ000 AS ZBQ INNER JOIN ZBB000 AS ZBB ON ZBQ_ITEM = ZBB_ITEM WHERE ZBQ_CODAMB = '" + codAmbiente + "'  AND ZBQ_TIPAMB = '" + codTipoAmbiente + "' AND ZBB_PRODUT = '" + codProduto + "' AND ZBQ.D_E_L_E_T_ = ' ' AND ZBB.D_E_L_E_T_ = ' ' FETCH FIRST ROW ONLY";
		try {
			stmt = connection.createStatement();
			rs = stmt.executeQuery(sql);
			if(rs.next()) {
				atalhoInfo.setDirProwin32(rs.getString("ZBQ_DIRPRO"));
				atalhoInfo.setDirArquivoIni(rs.getString("ZBQ_PARCLI"));
				atalhoInfo.setDirArquivoPf(rs.getString("ZBQ_PF"));
				atalhoInfo.setDirRaizCliente(rs.getString("ZBQ_RAIZCL"));
			}	
			rs.close();
			stmt.close();			
		}
		catch(SQLException e) {
			throw new RuntimeException(e);
		}	
		return atalhoInfo;
	}
	
	
	public String getPortaJBoss(String codAmbiente, String codTipoAmbiente, String codProduto) {
		Statement stmt;
		ResultSet rs;
		String portaJBoss = new String();
		String sql = "SELECT ZBT_URLPOR FROM ZBT000 AS ZBT INNER JOIN ZBB000 AS ZBB ON ZBT_ITEM = ZBB_ITEM WHERE ZBT_CODAMB = '" + codAmbiente + "' AND ZBT_TIPAMB = '" + codTipoAmbiente + "' AND ZBB_PRODUT = '" + codProduto + "' AND  ZBT.D_E_L_E_T_ = ' ' AND ZBB.D_E_L_E_T_ = ' ' FETCH FIRST ROW ONLY";
		try {
			stmt = connection.createStatement();
			rs = stmt.executeQuery(sql);
			if(rs.next()) {
				portaJBoss = rs.getString("ZBT_URLPOR");
			}	
		}
		catch(SQLException e) {
			throw new RuntimeException(e);
		}	
		return portaJBoss;
	}
	
	
	
	
	public void closeConnection() {
		try {
			connection.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	

}
