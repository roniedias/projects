package br.com.totvs.java3C.deprecated;

//import java.net.MalformedURLException;
//import java.rmi.Naming;
//import java.rmi.NotBoundException;
//import java.rmi.RemoteException;
//import java.text.DecimalFormat;
//import java.util.ArrayList;
//import java.util.List;
//
//import br.com.totvs.java3C.bean.ItemAmbiente;
//import br.com.totvs.java3C.bean.datasul.AmbienteFull;
//import br.com.totvs.java3C.bean.datasul.AtalhoInfo;
//import br.com.totvs.java3C.bean.datasul.Banco;
//import br.com.totvs.java3C.bean.datasul.StorageDirItems;
//import br.com.totvs.java3C.util.ConverteMedidasDados;
//
//import com.totvs.cloud.message.PingMessage;
//import com.totvs.cloud.message.items.AvailabilityAppServerItem;
//import com.totvs.cloud.message.items.AvailabilityDatabaseItem;
//import com.totvs.cloud.message.items.AvailabilityEAI2Item;
//import com.totvs.cloud.message.items.AvailabilityJbossItem;
//import com.totvs.cloud.message.items.AvailabilityLoginDatasulItem;
//import com.totvs.cloud.message.items.AvailabilityLoginEMSItem;
//import com.totvs.cloud.message.items.AvailabilityRPWItem;
//import com.totvs.cloud.message.items.StorageDirItem;
//import com.totvs.cloud.message.params.AvailabilityParamsDatasul;
//import com.totvs.cloud.message.params.AvailabilityParamsDatasulDatabases;
//import com.totvs.cloud.message.params.AvailabilityParamsEAI2;
//import com.totvs.cloud.message.params.AvailabilityParamsEMSDatabases;
//import com.totvs.cloud.message.params.AvailabilityParamsJboss;
//import com.totvs.cloud.message.params.AvailabilityParamsLoginDatasul;
//import com.totvs.cloud.message.params.AvailabilityParamsLoginEMS;
//import com.totvs.cloud.message.params.AvailabilityParamsRPWEMS;
//import com.totvs.cloud.message.params.AvailabilityParamsSQL;
//import com.totvs.cloud.message.params.StorageParamsDirItem;
//import com.totvs.cloud.service.RMICloudService;



public class AvailabilityDatasulOld {
	
	
	/*
	 * *** MONITORAMENTOS DISPONÍVEIS ***
	 * ping (O.K.)
	 * availabilityAppServer (O.K.)
	 * availabilityDatasulDatabases (O.K.)
	 * availabilityEAI1
	 * availabilityEAI2
	 * availabilityEMSDatabases (O.K.)
	 * availabilityJboss (O.K.)
	 * availabilityLoginDatasul (O.K.)
	 * availabilityLoginEMS 
	 * availabilityRPWDatasul (O.K.)
	 * availabilityRPWEMS (Necessário um ambiente EMS para testar)
	 * availabilitySQL (O.K.) Obs: Os parâmetros de entrada para este tipo de monitoramento 
	 * 					      podem ser encontrados no servidor de testes, dentro do arquivo: 
	 * 					      D:\datasul\clientes\totvs-cloud\jboss\jboss-4.2.3.GA\server\instance-8080\deploy\progress-ds.xml)
	 * storageDirItems (O.K.)
	 * 
	 */

	
	
//	private RMICloudService cloudService;
//	
//	
//	private String hostMonit;
//	private String portaProcMonit = "1099";
//	private String nomeAppSrv;
//	private String portaNsSrv;
//	private String instanciaAppSrv;
////	private String usuarioAppSrv;
////	private String senhaAppSrv;
//	private String protocoloAppSrv;
//	private String portaAppSrv;
//	private ArrayList<Banco> bancos;
//	private AtalhoInfo atalhoInfo;
//	private String dirProwin32;
//	private String dirArquivoPf;
//	private String dirArquivoIni;
//	private String portaJBoss;
//	private String protocoloJBoss;
//	private String nomeHostJBoss;	
//	private String protocoloEai2;
//	private String ipEai2;
//	private String portaEai2;
//	private String bancoFoundation;
//	private String usuarioItemAmbiente;
//	private String senhaItemAmbiente;
//	private ArrayList<ItemAmbiente> itensAmbiente;
//
//	
//	
//	public AvailabilityDatasulOld(String codAmbiente, String codTipoAmbiente, String codMonitoramento, String codProduto) {
//		
//		StringBuilder rmiStrConnection = new StringBuilder();
//				
//		AmbienteFull ambienteFull = new AmbienteFull(codAmbiente, codTipoAmbiente, codProduto);
//		
//		atalhoInfo = ambienteFull.getAtalhoInfo();
//		dirProwin32 = atalhoInfo.getDirProwin32().trim();
//		dirArquivoPf = atalhoInfo.getDirArquivoPf().trim();
//		dirArquivoIni = atalhoInfo.getDirArquivoIni().trim();
//		bancos = ambienteFull.getBancos();
//		bancoFoundation = ambienteFull.getBancoFoundation().trim();
//		itensAmbiente = ambienteFull.getItensAmbiente();
//		
//
//		for(ItemAmbiente i : itensAmbiente) {
//			if(i.getCodProduto().trim().equals(codProduto)) {
//				usuarioItemAmbiente = i.getUsuario().trim();
//				senhaItemAmbiente = i.getSenha().trim();
//			}
//		}
//		
//		
//		if(codProduto.equals("000019")) { // Se o produto for DATASUL 11
//			hostMonit = ambienteFull.getAppServers().get(0).getIp().trim(); // Pegando os dados apenas do primeiro servidor de aplicação
//			nomeAppSrv = ambienteFull.getAppServers().get(0).getNomeHost().trim();
//			portaNsSrv = ambienteFull.getAppServers().get(0).getPortaNs().trim();
//			instanciaAppSrv = ambienteFull.getAppServers().get(0).getInstanciaApp().trim();
////			usuarioAppSrv = ambienteFull.getAppServers().get(0).getUsuario().trim();
////			senhaAppSrv = ambienteFull.getAppServers().get(0).getSenha().trim();
//			protocoloAppSrv = ambienteFull.getAppServers().get(0).getProtocolo().trim();
//			portaAppSrv = ambienteFull.getAppServers().get(0).getPortaApp().trim();
//			portaJBoss = ambienteFull.getJBossInfo().get(0).getPorta().trim();
//			protocoloJBoss = ambienteFull.getJBossInfo().get(0).getProtocolo();
//			nomeHostJBoss = ambienteFull.getJBossInfo().get(0).getNomeHost().trim();
//			protocoloEai2 = ambienteFull.getEai2Info().getProtocolo();
//			ipEai2 = ambienteFull.getEai2Info().getIp().trim();
//			portaEai2 = ambienteFull.getEai2Info().getPorta().trim();
//		}
//		else if (codProduto.equals("000020")) { // Se o produto for EMS2
//			hostMonit = atalhoInfo.getIp().trim();
//			
//		}
//		
//
//	    try
//	    {
//	    	rmiStrConnection.append("rmi://");
//	    	rmiStrConnection.append(hostMonit);
//	    	rmiStrConnection.append(":");
//	    	rmiStrConnection.append(portaProcMonit);
//	    	rmiStrConnection.append("/DatasulCloudMonitor");
//	      
//	    	this.cloudService = ((RMICloudService)Naming.lookup(rmiStrConnection.toString()));
//	      
//	      
//	      // ========== Ping ===============================================
//
////	    	PingMessage pingMessage = new PingMessage();
////	    	pingMessage.setMessage("ping");
////	    	
////	    	try
////	        {
////	    		System.out.println("Retorno ping: " + this.cloudService.ping(pingMessage).getMessage());
////	        }
////	    	catch (RemoteException e)
////	    	{
////	    		e.printStackTrace();
////	    	}
//	      
//	      
//		 // ========== Monitoramento availabilityAppServer ==================
//	      
//	    	
//	    	// Monitorar todas as linhas que estiverem com o check box marcado.
//	    	
//	    	// Se de todos os que forem analisados, houver apenas um que esteja disponível, 
//	    	//o retorno deverá ser apresentado como DISPONÍVEL 
//	    	
//	    	
//	    	// Datasul > AppServer/WebSpeed
//	    	
////	    	AvailabilityParamsDatasul paramsDatasul = new AvailabilityParamsDatasul();
////	    	paramsDatasul.setAppServerName(nomeAppSrv);     // Nm. Host (JCHIBMA685, para o Item DATASUL 11)
////	    	paramsDatasul.setAppServerPort(portaNsSrv);     // Porta NS (5162, para o Item DATASUL 11)
////	    	paramsDatasul.setAppServerApp(instanciaAppSrv); // Nome (datasul-11510-totvs-cloud, para o Item DATASUL 11)
////	      
////	    	try {	    	  
////	    		AvailabilityAppServerItem appServerItem = this.cloudService.availabilityAppServer(paramsDatasul);	      
////	    		System.out.println("App Server " + appServerItem.getAppServerName() + " porta " + appServerItem.getAppServerPort() + " instancia " + appServerItem.getAppServerApp() + ": Status = " + appServerItem.getAppServerStatus());
////	    	}
////	    	catch (RemoteException e)
////	    	{
////	    		e.printStackTrace();
////	    	}
//
//	      
//	   // ========== Monitoramento availabilityDatasulDatabases ===========
//	    	
//    	// Todas as informações, com exceção dos nomes dos bancos (presentes em Datasul > BANCOS > Nome Fisico), 
//	    //	serão obtidas a partir do servidor de aplicação, em Datasul > AppServer/WebSpeed > AppServer. Deve-se 
//	    //	obter esta informação a partir da primeira linha que estiver com o checkbox "Monitora" 
//	    //	selecionado (ZBR_MONITO = T)
//     	
//	    // TODOS os bancos devem estar DISPONÍVEIS. Deve-se retornar o status INDISPONÍVEL, caso haja apenas um 
//	    // banco indisponível	
//	    	
//	    	
//	      	      
////	      List<AvailabilityDatabaseItem> avalItems = null;
////	      
////	      AvailabilityParamsDatasulDatabases paramsDatasulDatabases = new AvailabilityParamsDatasulDatabases();
////	      paramsDatasulDatabases.setAppServerName(nomeAppSrv);     // Nm. Host (JCHIBMA685, para o Item DATASUL 11)
////	      paramsDatasulDatabases.setAppServerPort(portaNsSrv);     // Porta NS (5162, para o Item DATASUL 11)
////	      paramsDatasulDatabases.setAppServerApp(instanciaAppSrv); // Nome (datasul-11510-totvs-cloud, para o Item DATASUL 11)
////	      
////	      String dbNames[] = new String[bancos.size()]; 
////	      
////	      for(int n = 0; n < bancos.size(); n++) 
////	    	  dbNames[n] = bancos.get(n).getNomeFisico().trim(); // Datasul > Bancos > Bancos > Nome Fisico 
////	      														// Ex: ems2adt, ems2emp, finance, etc., para o Item DATASUL 11
////		
////	      paramsDatasulDatabases.setDbNames(dbNames);
////	      
////	      try
////	      {
////	        avalItems = this.cloudService.availabilityDatasulDatabases(paramsDatasulDatabases);
////	        if (avalItems != null) {
////	          for (AvailabilityDatabaseItem item : avalItems)
////	          {
////	            System.out.println("DatabaseName: " + item.getItemName());
////	            System.out.println("Status: " + item.getStatus() + "\n");
////	          }
////	        }
////	      }
////	      catch (RemoteException e)
////	      {
////	        e.printStackTrace();
////	      }
//	    	
//	    	
//	      // ========== Monitoramento availabilityEMSDatabases ===============
//
//	    	// O IP do servidor de monitoramento será obtido a partir do campo: Datasul > BANCOS > I.P. Host
//	    	// TODOS TEM QUE ESTAR DISPONÍVEIS. Retornar INDISPONÍVEL caso haja um indisponível
//	    	
//	    	// Datasul > Atalhos > Atalho
//	    	
////	    	List<AvailabilityDatabaseItem> avalItems1 = null;
////	        
////	        AvailabilityParamsEMSDatabases paramsEMSDatabases = new AvailabilityParamsEMSDatabases();
////	        paramsEMSDatabases.setDlcPath(dirProwin32); // Dir. Progress (D:\dlc102b\bin\prowin32.exe, para o Item DATASUL 11)
////	        paramsEMSDatabases.setPfPath(dirArquivoPf); // PFs (D:\datasul\clientes\totvs-cloud\atalhos\dts11.pf, para o Item DATASUL 11) 
////	        paramsEMSDatabases.setIniPath(dirArquivoIni); // INI (D:\datasul\clientes\totvs-cloud\atalhos\dts11.ini, para o Item DATASUL 11)
////	        
////	        String dbNames1[] = new String[bancos.size()]; 
////		      
//// 	        for(int n = 0; n < bancos.size(); n++) 
//// 	        	dbNames1[n] = bancos.get(n).getNomeFisico().trim();
//// 	        
////	        paramsEMSDatabases.setDbNames(dbNames1);
////	        
////	        try
////	        {
////	        	avalItems1 = this.cloudService.availabilityEMSDatabases(paramsEMSDatabases);
////	        	if (avalItems1 != null) {
////	        		for (AvailabilityDatabaseItem item : avalItems1) {         
////	        			System.out.println("DatabaseName: " + item.getItemName());
////	        			System.out.println("Status: " + item.getStatus() + "\n");
////	        		}
////	        	}
////	        }
////	        catch (RemoteException e)
////	        {
////	          e.printStackTrace();
////	        }
//	      
//
//	    // ========== Monitoramento availabilityJboss ======================
//	    	
//
//	    	// Monitorar todas as linhas, e todas as informações serão obtidas a partir de Datasul > Web > JBoss. 
//	    	// Inclusive o IP do servidor de monitoramento, que será o campo I.P. Host
//	    	
//	    	// Considerar que, se houver pelo menos um servidor jboss Ativo no ambiente, 
//	    	// esse monitoramento retornará 100%
//
//	    	
//	    	
////	        AvailabilityParamsJboss paramsJboss = new AvailabilityParamsJboss();
////	        paramsJboss.setProtocol(protocoloJBoss); // Protocolo (HTTP, para o Item DATASUL 11)
////	        paramsJboss.setHost(nomeHostJBoss); // Nm. Host (JCHIBMA685, para o Item DATASUL 11
////	        paramsJboss.setPort(portaJBoss); // URL Port (8080, para o Item DATASUL 11)
////	        
////	        try
////	        {
////	        	AvailabilityJbossItem jbossItem = this.cloudService.availabilityJboss(paramsJboss);
////	        	System.out.println("*** JBOSS ***");
////	        	System.out.println(jbossItem.getHost());
////	        	System.out.println(jbossItem.getPort());
////	        	System.out.println(jbossItem.getStatus());
////	        }
////	        catch (RemoteException e)
////	        {
////	        	e.printStackTrace();
////	        }
//	    	
//	    	
//			// ========== Monitoramento availabilityLoginDatasul =============== 
//	    	
//	    	
//	    	// Monitorar todas as linhas que tenham um app server "atrelado" a ele 
//	    	
//	    	// Dentro do universo de Jboss´s que serão monitorados, considerar que, se houver pelo menos um servidor 
//	    	// Jboss Ativo no ambiente, esse monitoramento retornará o status Ativo
// 
// 	    	
////	    	AvailabilityParamsLoginDatasul paramsLoginDatasul = new AvailabilityParamsLoginDatasul();
////	    	
////	    	// Datasul > Web > JBoss 
////	        paramsLoginDatasul.setProtocol(protocoloJBoss); // Protocolo (HTTP, para o Item DATASUL 11)
////	        paramsLoginDatasul.setHost(nomeHostJBoss); // Nm. Host (JCHIBMA685, para o Item DATASUL 11
////	        paramsLoginDatasul.setPort(portaJBoss); // URL Port (8080, para o Item DATASUL 11)
////	        	        
////	        //09 DEMONSTRACAO > Itens de Ambiente
////	        paramsLoginDatasul.setUserName(usuarioItemAmbiente); // Usuário (super, para o Item DATASUL 11)
////	        paramsLoginDatasul.setPassWord(senhaItemAmbiente); // Senha (super, para o Item DATASUL 11)
////	        
////	     // Datasul > AppServer/WebSpeed
////	        paramsLoginDatasul.setRemoteServerName(nomeAppSrv); // Nm. Host (JCHIBMA685, para o Item DATASUL 11)
////	        paramsLoginDatasul.setRemoteServerPort(Integer.valueOf(portaAppSrv)); // Porta App (14273, para o Item DATASUL 11) 
////	        
////	        try
////	        {
////	          AvailabilityLoginDatasulItem loginDatasulItem = this.cloudService.availabilityLoginDatasul(paramsLoginDatasul);
////	          
////	          System.out.println("<<Login Datasul>>");
////	          System.out.println("Protocolo: " + paramsLoginDatasul.getProtocol());  
////	          System.out.println("Host: " + paramsLoginDatasul.getHost());
////	          System.out.println("Porta: " + paramsLoginDatasul.getPort());
////	          System.out.println("Usuario: " + paramsLoginDatasul.getUserName());
////	          System.out.println("Senha: " + paramsLoginDatasul.getPassWord());
////	          System.out.println("Servidor remoto (WebEnabled): " + paramsLoginDatasul.getRemoteServerName());
////	          System.out.println("Port do servidor remoto (WebEnabled): " + paramsLoginDatasul.getRemoteServerPort());
////	          System.out.println("Status: " + loginDatasulItem.getStatus());
////	        }
////	        catch (RemoteException e)
////	        {
////	          e.printStackTrace();
////	        }
//	    	
//	    	
//			// ========== Monitoramento availabilityRPWDatasul =================
//
//	    	// Monitorar apenas a primeira linha, pois este método faz uma consulta 
//	    	// no banco de dados e retorna as informações de todos os servidores
//
//	    	// OK se tiver pelo menos um servidor ativo (traz vários resultados)
//	    	
//	    	// Dos resultados retornados, se tiver pelo menos um ativo, considerar o status final como ATIVO
//	    	
//	    	// Datasul > AppServer/WebSpeed
//	    	
////	        AvailabilityParamsDatasul paramsRPWDatasul = new AvailabilityParamsDatasul();
////	        paramsRPWDatasul.setAppServerName(nomeAppSrv);     // Nm. Host (JCHIBMA685, para o Item DATASUL 11)
////	        paramsRPWDatasul.setAppServerPort(portaNsSrv);     // Porta NS (5162, para o Item DATASUL 11)
////	        paramsRPWDatasul.setAppServerApp(instanciaAppSrv); // Nome (datasul-11510-totvs-cloud, para o Item DATASUL 11)
////	        	        
////	        try
////	        {
////	        	List<AvailabilityRPWItem> avalRPWList = this.cloudService.availabilityRPWDatasul(paramsRPWDatasul);
////	            for (AvailabilityRPWItem item : avalRPWList)
////	            { 
////	            	System.out.println("Cod. Servidor RPW: " + item.getServerCode());
////	            	System.out.println("Des. Servidor RPW: " + item.getServerDesc());
////	            	System.out.println("Estado: " + item.getState());
////	            	
////	            	if (item.getState().equals("ativo")) {
////	            		System.out.println("Executando? " + item.getExecution());
////	            	}
////	            	System.out.println("\n");
////	            }
////	        }
////	        catch (RemoteException e)
////	        {
////	        	e.printStackTrace();
////	        }
//	        
//	        
//			// ========== Monitoramento availabilitySQL ========================
//	    	
//	    	// TODOS TEM QUE ESTAR DISPONÍVEIS. Retornar INDISPONÍVEL caso haja um indisponível
//	    	
//	    	// Datasul > Bancos > Bancos
//
////	    	String[] dbConnStrs = new String[bancos.size()];
////	    	
////	    	for(int b = 0; b < bancos.size(); b++) {
////	    		dbConnStrs[b] = bancos.get(b).getUrlConexao().trim(); // URL Conexao (ex: jdbc:datadirect:openedge://172.18.107.31:23600;databaseName=ems2adt, para o Item DATASUL 11)
////	    	}
////	    	
////	        List<AvailabilityDatabaseItem> returnValue = null;
////	        List<AvailabilityParamsSQL> availabilityParamsSQLs = new ArrayList<AvailabilityParamsSQL>();	        
////	        AvailabilityParamsSQL availabilityParamsSQL = null;
////
////	        for (int i = 0; i < dbConnStrs.length; i++) {
////	        	
////	        	availabilityParamsSQL = new AvailabilityParamsSQL();
////	        	
////	            availabilityParamsSQL.setDatabaseType(bancos.get(i).getTipoBanco().trim()); // Tipo Banco (Open Edge, para o Item DATASUL 11)
////	            availabilityParamsSQL.setDbName(bancos.get(i).getNomeFisico().trim()); // Nome Fisico (Ex: ems2adt, ems2emp, finance, etc., para o Item DATASUL 11)
////	            availabilityParamsSQL.setUserName(bancos.get(i).getUsuario().trim()); // Posicionar em um campo de Datasul > Bancos > Bancos (ex: ems2adt) > Broker > campo Usuario (pub, para o Item DATASUL 11)  
////	            availabilityParamsSQL.setPassWord(bancos.get(i).getSenha().trim()); // Posicionar em um campo de Datasul > Bancos > Bancos (ex: ems2adt) > Broker > campo Senha (pub, para o Item DATASUL 11)
////	            availabilityParamsSQL.setUrlConnection(dbConnStrs[i]);
////                
////	            availabilityParamsSQLs.add(availabilityParamsSQL);
////            }
////	        
////            try
////	        {
////            	returnValue = this.cloudService.availabilitySQL(availabilityParamsSQLs);
////	            
////            	for (AvailabilityDatabaseItem item : returnValue)
////	            {
////            		System.out.println(item.getItemName());
////            		System.out.println(item.getStatus() + "\n");
////	            }
////	        }
////	        catch (RemoteException e)
////	        {
////	        	e.printStackTrace();
////	        }
//	    	
//	    	
//	    	// ========== Monitoramento getStorageDirItems =====================
//	    	
//	    	// IP processo de monitoramento obtido a partir das informações cadastradas na primeira linha
//	    	// de Datasul > Atalhos > Atalho
//	    	
//	    	// Caminhos físicos a serem checados:
//	    	
//	    				//ZBQ_ESPECI  ZBQ_ESPCIF   ZBQ_SPOOL (Essas informações não mudam. Portanto, 
//	    	 //Aba atalho (Especiais, Específicos, SPOOL)     será considerada somente a primeira linha)
//	    	
//	    	            //ZBO_DIRLOC  ZBO_BKPAI  ZBO_BKPFUL  (Essas informações não mudam. Portanto,
//	    	// Aba banco (Dir. Banco, Bkp AiLog, Bkp Full) -> será considerada somente a primeira linha) 
//	        
//	    	// Obs: Caso o campo Dir.Jboss (Datasul > Web > Jboss) esteja preenchido, ele será incluído
//	    	// na soma acima. Se houver mais de uma linha para este campo, todas serão incluídas e somadas  
//	        	        
//	    	// Ao final, todos os paths serão somados e gravados
//	    	
////	    	StorageDirItems storageDirItems = ambienteFull.getStorageDirItems();
////	    	
////	    	//Datasul > Atalhos > Atalho
////	    	String especiais = storageDirItems.getEspeciais().trim();     // Especiais (D:\datasul\clientes\totvs-cloud\especiais, para o Item DATASUL 11)
////	    	String especificos = storageDirItems.getEspecificos().trim(); // Especificos (D:\datasul\clientes\totvs-cloud\especificos, para o Item DATASUL 11)
////	    	String spool = storageDirItems.getSpool().trim(); // SPOOL (D:\temp\totvs-cloud, para o Item DATASUL 11)
////	    	
////	    	// Datasul > Bancos > Bancos
////	    	String dirBanco = storageDirItems.getDirBanco().trim(); // Dir. Banco (D:\datasul\clientes\totvs-cloud\bancos, para o Item DATASUL 11)
////	    	String bkpAiLog = storageDirItems.getBkpAiLog().trim(); // Bkp AILog (D:\DATASUL\CLIENTES\TOTVS-CLOUD\BANCOS , para o Item DATASUL 11)
////	    	String bkpFull = storageDirItems.getBkpFull().trim(); // Bkp FULL ("Vazio", para o Item DATASUL 11)
////	    	
////	    	double tamanhoTotal = 0.0;
////	    	
////	    	DecimalFormat df = new DecimalFormat("#.########");
////	    	
////	    		    	
////	    	List<StorageParamsDirItem> storageList = new ArrayList<StorageParamsDirItem>();
////	    	StorageParamsDirItem storageParamItem = new StorageParamsDirItem();
////	    	
////    		if(!especiais.equals("no_info")) {
////    			int limit = especiais.lastIndexOf("\\");
////    			String pasta = especiais.substring(limit + 1, especiais.length());
////    			storageParamItem.setDirName(pasta.trim());
////    			storageParamItem.setPathDir(especiais);
////    			storageList.add(storageParamItem);
////    		}
////    		
////    		if(!especificos.equals("no_info")) {
////    			storageParamItem = new StorageParamsDirItem();
////    			int limit = especificos.lastIndexOf("\\");
////    			String pasta = especificos.substring(limit + 1, especificos.length());
////    			storageParamItem.setDirName(pasta.trim());
////    			storageParamItem.setPathDir(especificos);
////    			storageList.add(storageParamItem);
////    		}
////    		
////    		if(!spool.equals("no_info")) {
////    			storageParamItem = new StorageParamsDirItem();
////    			int limit = spool.lastIndexOf("\\");
////    			String pasta = spool.substring(limit + 1, spool.length());
////    			storageParamItem.setDirName(pasta.trim());
////    			storageParamItem.setPathDir(spool);
////    			storageList.add(storageParamItem);
////    		}
////    		
////    		if(!dirBanco.equals("no_info")) {
////    			storageParamItem = new StorageParamsDirItem();
////    			int limit = dirBanco.lastIndexOf("\\");
////    			String pasta = dirBanco.substring(limit + 1, dirBanco.length());
////    			storageParamItem.setDirName(pasta.trim());
////    			storageParamItem.setPathDir(dirBanco);
////    			storageList.add(storageParamItem);
////    		}
////    		
////    		if(!bkpAiLog.equals("no_info")) {
////    			storageParamItem = new StorageParamsDirItem();
////    			int limit = bkpAiLog.lastIndexOf("\\");
////    			String pasta = bkpAiLog.substring(limit + 1, bkpAiLog.length());
////    			storageParamItem.setDirName(pasta.trim());
////    			storageParamItem.setPathDir(bkpAiLog);
////    			storageList.add(storageParamItem);
////    		}
////    		
////    		if(!bkpFull.equals("no_info")) {
////    			storageParamItem = new StorageParamsDirItem();
////    			int limit = bkpFull.lastIndexOf("\\");
////    			String pasta = bkpFull.substring(limit + 1, bkpFull.length());
////    			storageParamItem.setDirName(pasta.trim());
////    			storageParamItem.setPathDir(bkpFull);
////    			storageList.add(storageParamItem);
////    		}
////    		
////    		
////    		if(storageList.size() == 0) {
////    			System.out.println("Nenhum item de Storage a ser monitorado.");
////    			System.exit(1);
////    		}
////    		else {
////    			
////    	        try
////    		    {
////    	        	List<StorageDirItem> result = this.cloudService.getStorageDirItems(storageList);
////    	        	
////    	        	System.out.println("<<Diretorios>>");
////    	        	for (StorageDirItem item : result)
////    	        	{
////    			        System.out.println("Nome: " + item.getDirName());
////    			        System.out.println("Diretorio: " + item.getPathDir());
////    			        System.out.println("Tamanho: " + item.getSizeDir());
////    			        
////    			        double valorEmGiga = new ConverteMedidasDados(item.getSizeDir()).getValorEmGigaBytes();
////    			        
////    			        System.out.println("Tamanho convertido GB: " + valorEmGiga);
////    			        System.out.println("Tamanho convertido e formatado GB: " + df.format(valorEmGiga));
////    			        
////    			        tamanhoTotal += valorEmGiga;
////    			        
////    			        
////    			        System.out.println("---");
////    	        	}
////    		    }
////    		    catch (RemoteException e)
////    		    {
////    		      e.printStackTrace();
////    		    }
////    			
////    		}
////    		
////    		
////    		System.out.println("Tamanho total: " + tamanhoTotal);
////    		System.out.println("Tamanho total formatado: " + df.format(tamanhoTotal));
//    		
//    		
//	    	
//	    	
//	    	// ========== Monitoramento availabilityEAI1 ===================
//	    	
//	    	/*
//	    	- Protocolo
//	    	- Nome Servidor
//	    	- Porta
//	    	- Nome do contexto Axis
//	    	*/
//	    	
//	    	
//	    	// ========== Monitoramento availabilityEAI2 ===================
//	    	
//	    	//Server de monitoramento: Usar o IP do próprio EAI2
//	    	
//	        // Monitorar todas as linhas
//	    	
//	    	// Se um estiver fora, pára o monitoramento do EAI2, portanto todos devem estar
//	    	// disponíveis para que o retorno tenha status "DISPONÍVEL" 
//	        
//	    	// Datasul > Integrator/EAI > EAI2
////	        AvailabilityParamsEAI2 paramsEAI2 = new AvailabilityParamsEAI2();
////	        paramsEAI2.setProtocol(protocoloEai2); // Protocolo (HTTP, para o Item DATASUL 11)
////	        paramsEAI2.setServer(ipEai2); // I.P. Host (172.18.107.31, para o Item DATASUL 11)
////	        paramsEAI2.setPort(portaEai2); // Porta (8080, para o Item DATASUL 11)
////	        
////	        AvailabilityEAI2Item eai2Item = null;
////	        
////	        try
////	        {
////	        	eai2Item = this.cloudService.availabilityEAI2(paramsEAI2);
////	        	
////	        	System.out.println("<<EAI 2>>"); 
////	            System.out.println("Protocolo: " + eai2Item.getProtocol()); 
////	            System.out.println("Server: " + eai2Item.getServer()); 
////	            System.out.println("Port: " + eai2Item.getPort()); 
////	            System.out.println("Status: " + eai2Item.getStatus()); 
////	        }
////	        catch (RemoteException e)
////	        {
////	        	e.printStackTrace();
////	        }
//	      
//	    	
//	    	
//	    	// ========== Monitoramento availabilityLoginEMS ===============
//	    	
//	        // Monitorar apenas a primeira linha
//	    	// IP processo de monitoramento: Tentar o primeiro, se não conseguir, o segundo, e assim sucessivamente
//	        
////    	    AvailabilityParamsLoginEMS paramsLoginEMS = new AvailabilityParamsLoginEMS();
////    	    
////    	    // Datasul > Atalhos > Atalho
////    	    paramsLoginEMS.setDlcPath(dirProwin32);   // Dir. Progress (D:\dlc102b\bin\prowin32.exe, para o Item EMS2)
////    	    paramsLoginEMS.setPfPath(dirArquivoPf);   // PFs (D:\datasul\clientes\demo\atalhos\ems206b\ems206.pf, para o Item EMS2)
////    	    paramsLoginEMS.setIniPath(dirArquivoIni); // INI (D:\datasul\clientes\demo\atalhos\ems206b\ems206.ini, para o Item EMS2)
////    	    
////    	    // Datasul > Bancos > Bancos
////    	    paramsLoginEMS.setFndDbName(bancoFoundation); // Nome Logico, cuja opção "Monit Login" está marcada (mg, para o Item EMS2)
////    	    
////    	    // 09 DEMONSTRACAO > Itens de Ambiente 
////    	    paramsLoginEMS.setUserName(usuarioItemAmbiente); // Usuario (rpw, para o Item EMS2 / super, para o Item DATASUL 11)
////    	    paramsLoginEMS.setPassWord(senhaItemAmbiente); // Senha (rpw, para o Item EMS2 / super, para o Item DATASUL 11)
////    	    	    	
////    	    
////    	    try
////    	    {
////    	      AvailabilityLoginEMSItem loginEMSItem = this.cloudService.availabilityLoginEMS(paramsLoginEMS);
////    	      
////    	      System.out.println("<<Login EMS>>");
////    	      System.out.println("Banco FND: " + bancoFoundation);
////    	      System.out.println("Usuario: " + usuarioItemAmbiente);
////    	      System.out.println("Senha: " + senhaItemAmbiente);
////    	      System.out.println("Status: " + loginEMSItem.getStatus());
////    	    }
////    	    catch (RemoteException e)
////    	    {
////    	      e.printStackTrace();
////    	    }
//
//	    	
//	    	
//	    	// ========== Monitoramento availabilityRPWEMS =================
//	    	
//	    	// IP processo de monitoramento: Tentar o primeiro, se não conseguir, o segundo, e assim sucessivamente
//	    	// Monitorar apenas a primeira linha
//	    	
//	        AvailabilityParamsRPWEMS paramsRPWEMS = new AvailabilityParamsRPWEMS();
//	        
////    	    // Datasul > Atalhos > Atalho
//	        paramsRPWEMS.setDlcPath(dirProwin32); // Dir. Progress (D:\dlc102b\bin\prowin32.exe, para o Item EMS2)
//	        paramsRPWEMS.setPfPath(dirArquivoPf); // PFs (D:\datasul\clientes\demo\atalhos\ems206b\ems206.pf, para o Item EMS2)
//	        paramsRPWEMS.setIniPath(dirArquivoIni); // INI (D:\datasul\clientes\demo\atalhos\ems206b\ems206.ini, para o Item EMS2)
//	        
////    	    // Datasul > Bancos > Bancos
//	        paramsRPWEMS.setFndDbName(bancoFoundation); // Nome Logico, cuja opção "Monit Login" está marcada (mg, para o Item EMS2)
//	        	        	        
//	        try
//	        {
//	        	
//	        	List<AvailabilityRPWItem> avalRPWList = this.cloudService.availabilityRPWEMS(paramsRPWEMS);	
//	          
//	        	for (AvailabilityRPWItem item : avalRPWList)
//	        	{
//	        		System.out.println("Cod. Servidor RPW: " + item.getServerCode());
//	        		System.out.println("Des. Servidor RPW: " + item.getServerDesc());
//	        		System.out.println("Estado: " + item.getState());
//
//	        		if (item.getState().equals("ativo"))
//		            {
//		            	String execution = item.getExecution().booleanValue() ? "Sim" : "NÃ£o";
//		            	System.out.println("Executando? " + execution);
//		            }
//	        	}
//	        }
//	        catch (RemoteException e)
//	        {
//	        	e.printStackTrace();
//	        }
//	      
//    	
//	      
//	    }
//	    catch (RemoteException e)
//	    {
//	    	System.out.println("Nao foi possivel efetuar conexao RMI com o servidor de monitoramento.");
//	    	System.exit(1);
//	    }
//	    catch (MalformedURLException e1)
//	    {
//	    	System.out.println("Url INCORRETA para conexao RMI com o servidor de monitoramento.");
//	        System.exit(1);
//	    }
//	    catch (NotBoundException e1)
//	    {
//	    	System.out.println("Erro durante tentativa de procura (lookup) ou desvinculo (unbind). Nome nao possui qualquer ligacao associada ao registro (registry).");
//	        System.exit(1);
//	    }
//	    
//	    
//	}


		
}
