package br.com.totvs.java3C.datasul.plugin;

import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.util.ArrayList;

import com.totvs.cloud.service.DatasulCloudMonitorClient;
import com.totvs.cloud.service.RMICloudService;

import br.com.totvs.java3C.bean.datasul.AmbienteFull;
import br.com.totvs.java3C.bean.datasul.AppServer;
import br.com.totvs.java3C.bean.datasul.AtalhoInfo;
import br.com.totvs.java3C.bean.datasul.Banco;

public class AvailabilityDatasul {
	
	/*
	 * *** MONITORAMENTOS DISPONÍVEIS ***
	 * 
	 * availabilityAppServer (O.K.)
	 * availabilityDatasulDatabases (O.K.)
	 * 
	 * availabilityEAI1
	 * availabilityEAI2
	 * 
	 * availabilityEMSDatabases (O.K.)
	 * availabilityJboss (O.K.)
	 * availabilityLoginDatasul (O.K.)
	 * 
	 * availabilityLoginEMS 
	 * 
	 * availabilityRPWDatasul (O.K.)
	 * 
	 * availabilityRPWEMS (Necessário um ambiente EMS para testar)
	 * 
	 * availabilitySQL (O.K.) Obs: Os parâmetros de entrada para este tipo de monitoramento 
	 * 					      podem ser encontrados no servidor de testes, dentro do arquivo: 
	 * 					      D:\datasul\clientes\totvs-cloud\jboss\jboss-4.2.3.GA\server\instance-8080\deploy\progress-ds.xml)
	 * 
	 * ping
	 * storageDirItems (O.K.)
	 * 
	 */
	
	private String ZCA_PARAM;
	private String ZCA_CODAMB;
	private String ZCA_TIPAMB;

	public AvailabilityDatasul(String codAmbiente, String codTipoAmbiente, String codMonitoramento, String codProduto) {
		
		this.ZCA_PARAM = codMonitoramento; 
		this.ZCA_CODAMB = codAmbiente;     
		this.ZCA_TIPAMB = codTipoAmbiente;
		
		AmbienteFull aFull = new AmbienteFull(codAmbiente, codTipoAmbiente, codProduto);
		
		String ipAppSrv = aFull.getAppServers().get(0).getIp().trim(); // Pegando os dados apenas do primeiro servidor de aplicação
		String portaProcMonit = "1099";
		String nomeAppSrv = aFull.getAppServers().get(0).getNomeHost().trim();
		String portaAppSrv = aFull.getAppServers().get(0).getPorta().trim();
		String instanciaAppSrv = aFull.getAppServers().get(0).getInstanciaApp().trim();
		
		ArrayList<Banco> bancos = aFull.getBancos();
		
		AtalhoInfo atalhoInfo = aFull.getAtalhoInfo();
		String dirProwin32 = atalhoInfo.getDirProwin32().trim();
		String dirArquivoPf = atalhoInfo.getDirArquivoPf().trim();
		String dirArquivoIni = atalhoInfo.getDirArquivoIni().trim();
		
		String portaJBoss = aFull.getPortaJBoss().trim();
		
		
		

		
		
		
		// ========== Monitoramento availabilityAppServer ==================

		// Os parâmetros de entrada para este tipo de monitoramento podem  
		// ser encontrados no servidor de testes, dentro do arquivo:
		// D:\datasul\clientes\totvs-cloud\jboss\jboss-4.2.3.GA\server\instance-8080\conf\datasul\datasul_framework.properties
		
		
		 //String [] argumentos = {ipAppSrv, portaProcMonit, "availabilityAppServer", nomeAppSrv, portaAppSrv, instanciaAppSrv};
		 
		 // JCHIBMA685, 5162, datasul-11510-totvs-cloud
		
		/* RESULTADO (executar DatasulCloudMonitorClient passando "argumentos" como parâmetro):
		 * 		
		 *	<<App Server>>
		 *	Nome: JCHIBMA685
		 *	Porta: 5162
		 *	Instancia: datasul-11510-totvs-cloud
		 *	Status: Ativo 		
		 */
		
		
		// ========== Monitoramento availabilityDatasulDatabases ===========
		
		// Este monitoramento será executado quando o ITEM DE AMBIENTE for "DATASUL 11", por exemplo
		// A execução deste método está amarrada ao nível acima ITEM DE AMBIENTE
			

//		String[] argumentos = new String[7];
//		
//		argumentos[0] = ipAppSrv;
//		argumentos[1] = portaProcMonit;
//		argumentos[2] = "availabilityDatasulDatabases";
//		argumentos[3] = nomeAppSrv;
//		argumentos[4] = portaAppSrv;
//		argumentos[5] = instanciaAppSrv;
//		
//		for(int n = 0; n < bancos.size(); n++) 
//			argumentos[6] += bancos.get(n).getNomeFisico().trim() + ",";
//		
//		argumentos[6] = argumentos[6].substring(4); // Removendo "null" do início da String 
//		
//		int limit = argumentos[6].lastIndexOf(","); // Removendo a última "," da String
//		argumentos[6] = argumentos[6].substring(0, limit);

				// EMS2 / Datasul 11
		
		
		/* RESULTADO (executar DatasulCloudMonitorClient passando "argumentos" como parâmetro):
		 * 
		 * DatabaseName: ems2adt
		 * Status: Inativo
		 *
		 * DatabaseName: ems2emp
		 * Status: Inativo
	     *
		 * DatabaseName: ems2mult
		 * Status: Inativo
		 * 
		 * DatabaseName: ems2sor
		 * Status: Inativo
		 * 
		 * DatabaseName: ems2uni
		 * Status: Inativo
		 * 
		 * DatabaseName: ems507
		 * Status: Ativo
		 * 
		 * DatabaseName: emsdev
		 * Status: Ativo
		 * 
		 * DatabaseName: finance
		 * Status: Ativo
		 * 
		 * DatabaseName: hr211
		 * Status: Ativo
		 * 
		 * DatabaseName: payroll
		 * Status: Ativo
		 * 
		 * DatabaseName: srcad
		 * Status: Ativo
		 * 
		 * DatabaseName: srmov
	     * Status: Ativo
		 * 
		 * DatabaseName: eai
		 * Status: Ativo
		 * 
		 * DatabaseName: mdtcrm
		 * Status: Ativo
		 * 
		 */
		
		
		// ========== Monitoramento availabilityEMSDatabases ===============
		
		// Este monitoramento será executado quando o ITEM DE AMBIENTE for "EMS 2", por exemplo
		// A execução deste método está amarrada ao nível acima ITEM DE AMBIENTE 

		
//		String[] argumentos = new String[7];
//		
//		argumentos[0] = ipAppSrv;
//		argumentos[1] = portaProcMonit;
//		argumentos[2] = "availabilityEMSDatabases";
//		argumentos[3] = dirProwin32; 
//		argumentos[4] = dirArquivoPf;
//		argumentos[5] = dirArquivoIni;
//		
//		for(int n = 0; n < bancos.size(); n++) 
//			argumentos[6] += bancos.get(n).getNomeFisico().trim() + ",";
//		
//		argumentos[6] = argumentos[6].substring(4); // Removendo "null" do início da String 
//		
//		// Removendo a última "," da String
//		int limit = argumentos[6].lastIndexOf(","); 
//		argumentos[6] = argumentos[6].substring(0, limit);


		/* RESULTADO (executar DatasulCloudMonitorClient passando "argumentos" como parâmetro)
		 * 
		 *
		 *	DatabaseName: ems2adt
		 *	Status: Inativo
	 	 *
		 *	DatabaseName: ems2emp
		 *	Status: Inativo
		 *
		 *	DatabaseName: ems2mult
		 *	Status: Inativo
		 *
		 *	DatabaseName: ems2sor
		 *	Status: Inativo
		 *
		 *	DatabaseName: ems2uni
		 *	Status: Inativo
		 *
		 *	DatabaseName: ems507
		 *	Status: Ativo
		 *
		 *	DatabaseName: emsdev
		 *	Status: Ativo
		 *
		 *	DatabaseName: finance
		 *	Status: Ativo
		 *
		 *	DatabaseName: hr211
		 *	Status: Ativo
		 *
		 *	DatabaseName: payroll
		 *	Status: Ativo
		 *
		 *	DatabaseName: srcad
		 *	Status: Ativo
		 *
		 *	DatabaseName: srmov
		 *	Status: Ativo
		 *
		 *	DatabaseName: eai
		 *	Status: Ativo
		 *
		 *	DatabaseName: mdtcrm
		 *	Status: Ativo
		 *
		 */
		
		
		// ========== Monitoramento availabilityJboss ======================
		                                    
		// String [] argumentos = {ipAppSrv, portaProcMonit, "availabilityJboss", "http", nomeAppSrv, portaJBoss};
		

		/* RESULTADO (executar DatasulCloudMonitorClient passando "argumentos" como parâmetro) 
		 * 
		 * << JBoss >> 
		 * 
		 * JCHIBMA685
		 *
		 *	8080                          
		 *
		 *	Ativo
		 *
		 */

		
		// ========== Monitoramento availabilityLoginDatasul ===============
		//java -jar datasul-cloud-monitor-client-1.0-SNAPSHOT.jar 172.18.107.31 1099 availabilityLoginDatasul http JCHIBMA685 8080 super super jchibma685 14273
				
		//String [] argumentos = {ipAppSrv, portaProcMonit, "availabilityLoginDatasul", "http", nomeAppSrv, portaJBoss, "super", "super", nomeAppSrv, "14273"};
		
		/* RESULTADO (executar DatasulCloudMonitorClient passando "argumentos" como parâmetro)
		 * 
		 * <<Login Datasul>>
		 *	Protocolo: http
		 *	Host: JCHIBMA685
		 *	Porta: 8080
		 *	Usuario: super
		 *	Senha: super
		 *	Servidor remoto (WebEnabled): JCHIBMA685
		 *	Port do servidor remoto (WebEnabled): 14273
		 *	Status: Ativo
		 *
		 */
		
		
		// ========== Monitoramento availabilityRPWDatasul =================

		//String [] argumentos = {ipAppSrv, portaProcMonit, "availabilityRPWDatasul", nomeAppSrv, portaAppSrv, instanciaAppSrv};
		
		/* RESULTADO (executar DatasulCloudMonitorClient passando "argumentos" como parâmetro)
		 * 
		 * Cod. Servidor RPW: adc
		 * Des. Servidor RPW: Servicor adc
		 * Estado: inativo
		 * Cod. Servidor RPW: adm-ems
		 * Des. Servidor RPW: Servidor Adm Datasul EMS
		 * Estado: ativo
		 * Executando? false
		 * Cod. Servidor RPW: AILTON
		 * Des. Servidor RPW: AILTON
		 * Estado: inativo
		 * Cod. Servidor RPW: ALFA
		 * Des. Servidor RPW: Alfa Anyware
		 * Estado: inativo
		 * Cod. Servidor RPW: byyou
		 * Des. Servidor RPW: byyou
		 * Estado: inativo
		 * Cod. Servidor RPW: ceiman
		 * Des. Servidor RPW: CEIMAN
		 * Estado: inativo
		 * Cod. Servidor RPW: crm
		 * Des. Servidor RPW: Servidor RPW CRM
		 * Estado: ativo
		 * Executando? false
		 * Cod. Servidor RPW: lou01
		 * Des. Servidor RPW: lou01
		 * Estado: inativo
		 * Cod. Servidor RPW: lourdes
		 * Des. Servidor RPW: lourdes
		 * Estado: inativo
		 * Cod. Servidor RPW: timeout
		 * Des. Servidor RPW: timeout
		 * Estado: inativo
		 * Cod. Servidor RPW: win
		 * Des. Servidor RPW: windwos
		 * Estado: ativo
		 * Executando? false
		 * 
		 */
		
		
		// ========== Monitoramento availabilityRPWEMS =====================		
		//java -jar datasul-cloud-monitor-client-1.0-SNAPSHOT.jar 172.18.107.31 1099 availabilityRPWEMS D:\dlc102b\bin\prowin32.exe D:\datasul\clientes\totvs-cloud\atalhos\dts11.pf D:\datasul\clientes\totvs-cloud\atalhos\dts11.ini EMS2
		
		// String [] argumentos = {ipAppSrv, portaProcMonit, "availabilityRPWEMS", dirProwin32, dirArquivoPf, dirArquivoIni, "EMS2"};
		
		/* 	RETORNO (Provavelmente por conta do último parâmetro (Produto), que não existe no ambiente. 
		 *  Analisar este método (não funcionando)
		 * 
		 *	RemoteException: E necessario alimentar corretamente os parametros: 
		 *	- Diretorio do prowin32.exe 
		 *	- Diretorio do arquivo .pf 
		 *	- Diretorio do arquivo .ini 
		 *	- Produto (EMS2 ou EMS5) 
		 * 
		 */


		// ========== Monitoramento availabilitySQL ========================
		//java -jar datasul-cloud-monitor-client-1.0-SNAPSHOT.jar 172.18.107.31 1099 availabilitySQL OpenEdge emsfnd pub pub jdbc:datadirect:openedge://172.18.107.31:23621;databaseName=emsfnd
			
		
//		ArrayList<String> strConnArr = new ArrayList<String>();
//		
//		for(Banco b : bancos) {
//			strConnArr.add(b.getNomeFisico().trim() + "," + b.getUrlConexao().trim() + b.getIp().trim() + ":" + b.getPorta().trim() + ";databaseName=" + b.getNomeFisico().trim());
//		}
//		
//		String strConn = new String();
//		
//		for(int s = 0; s < strConnArr.size(); s++) {
//			strConn += strConnArr.get(s) + ",";
//		}
//		
//		int limit = strConn.lastIndexOf(","); // Removendo a última "," da String
//		strConn = strConn.substring(0, limit);
//		
//		String [] argumentos = {ipAppSrv, portaProcMonit, "availabilitySQL", bancos.get(0).getAlias().trim(), bancos.get(0).getUsuario().trim(), bancos.get(0).getSenha().trim(), strConn};
				
		
		/* RESULTADO (executar DatasulCloudMonitorClient passando "argumentos" como parâmetro)
		 *
		 * ems2adt
		 * Inativo
		 *
		 * emsfnd
		 * Ativo
		 *
		 * mdtcrm
		 * Ativo
		 *
		 * eai
		 * Ativo
		 * 
		 * srmov
	     * Inativo
		 *
		 * srcad
		 * Inativo
		 * 
		 * payroll
		 * Ativo
		 * 
		 * hr211
		 * Inativo
		 * 
		 * finance
		 * Ativo
		 * 
		 * emsdev
		 * Ativo
		 * 
	 	 * ems507
		 * Inativo
		 * 
		 * ems2uni
		 * Ativo
		 * 
		 * ems2sor
		 * Ativo
		 * 
		 * ems2mult
		 * Inativo
		 * 
		 * ems2emp
		 * Ativo
		 *  
		 */
		
		
		// ========== Monitoramento getStorageDirItems =====================
		
		
		// Obs: A pasta do cliente NÃO precisa estar compartilhada
//		String dirRaizCliente = atalhoInfo.getDirRaizCliente().trim();
//		int limit = dirRaizCliente.lastIndexOf("\\\\");
//		String pastaCliente = dirRaizCliente.substring(limit + 2, dirRaizCliente.length());
//		
//		String [] argumentos = {ipAppSrv, portaProcMonit, "getStorageDirItems", pastaCliente + "," + dirRaizCliente};
		
		/* RESULTADO (executar DatasulCloudMonitorClient passando "argumentos" como parâmetro)
		 * 
		 * <<Diretorios>>
		 * ---
		 * Nome: :\datasul\clientes\totvs-cloud
		 * Diretorio: D:\datasul\clientes\totvs-cloud
		 * Tamanho: 7.56 GB
		 * 
		 */
		
//		try {
//			new DatasulCloudMonitorClient(argumentos);
//		} catch (RemoteException e) {
//			System.out.println("Erro durante execucao da chamada ao metodo remoto \"XYZ\"");
//		}

		
		

		
	}
}
