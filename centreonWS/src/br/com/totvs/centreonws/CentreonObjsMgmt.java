package br.com.totvs.centreonws;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

public class CentreonObjsMgmt {

	private static final String PATH_CLAPI = "/usr/local/centreon/www/modules/centreon-clapi/core/centreon";
	//private static final String CENTREON_USER = "3c";
	//private static final String CENTREON_PASSWORD = "totvs@123";
	//private static final String POLLER = "infra-nagios-sp06"; 
	
	//private static final String HOST_GROUP = "3C-GH";
	//private static final String POLLER_NUMBER = "12"; 
	//private static final String CONTACT_GROUP = "3c-GC";
	//tamplateName -> generic-service: de 5 em 5 min, 3c-valid-env, 12 em 12h
	
	
	private String hostCreationReturn;
	private String hostDeletionReturn;
	private String serviceCreationReturn;
	private String serviceDeletionReturn;
	
	
	
	public String hostCreation(String hostName, String centreonUser, String centreonPassword, String poller, String hostGroup, String pollerNumber, String contactGroup) {

		if(hostName == null | centreonUser == null || centreonPassword == null || poller == null || hostGroup == null || pollerNumber == null || contactGroup == null) {
			return "Dado(s) invalido(s)!";
		}

		DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
		Date date = new Date();
		
		try {
			Process p = Runtime.getRuntime().exec(PATH_CLAPI + " -u " + centreonUser + " -p " + centreonPassword + " -o host -a add -v " + hostName + ";" + dateFormat.format(date) + ";localhost;generic-host;" + poller + ";" + hostGroup);
			BufferedReader stdout = new BufferedReader(new InputStreamReader(p.getInputStream())); 		
			String line = stdout.readLine();			

			while(line != null) {
				hostCreationReturn = line.toString() + " ";
				line = stdout.readLine();
			}

			Process p1 = Runtime.getRuntime().exec(PATH_CLAPI + " -u " + centreonUser + " -p " + centreonPassword + " -o host -a addcontactgroup -v " + hostName + ";" + contactGroup);
			
			BufferedReader stdout1 = new BufferedReader(new InputStreamReader(p1.getInputStream())); 		
			String line1 = stdout1.readLine();			

			while(line != null) {
				hostCreationReturn = line1.toString() + " ";
				line1 = stdout1.readLine();
			}
			
		}
		catch (IOException e) {
			e.printStackTrace();
		}
		
		if(hostCreationReturn == null) {
			System.out.println("Host criado com sucesso: " + hostName + ".");
			return "Host criado com sucesso: " + hostName + ".";
		}
		else {
			System.out.println(hostCreationReturn);
			return hostCreationReturn;
		}

		
	}
	
	
	
	public String hostDeletion(String hostName, String centreonUser, String centreonPassword) {

		if(hostName == null || centreonUser == null || centreonPassword == null) {
			return "Nome do host/usuario/password invalido(s).";
		}
		
		try {			
			Process p = Runtime.getRuntime().exec(PATH_CLAPI + " -u " + centreonUser + " -p " + centreonPassword+ " -o HOST -a DEL -v " + hostName);
			BufferedReader stdout = new BufferedReader(new InputStreamReader(p.getInputStream())); 		
			String line = stdout.readLine();			
	
			while(line != null) {
				hostDeletionReturn = line.toString() + " ";
				line = stdout.readLine();
			}
		}
		catch(IOException e) {
			e.printStackTrace();
		}
		
		if(hostDeletionReturn == null) {
			System.out.println("Host deletado com sucesso: " + hostName + ".");
			return "Host deletado com sucesso: " + hostName + ".";
		}
		else {
			System.out.println(hostDeletionReturn);
			return hostDeletionReturn;
		}

		
	}
	
	
	
	public String serviceCreation(String hostName, String serviceName, String clientCode, String environmentCode, String environmentTypeCode, String monitoringCode, String productCode, String templateName, String centreonUser, String centreonPassword) {

		if(hostName == null || serviceName == null || clientCode == null || environmentCode == null || environmentTypeCode == null || monitoringCode == null || productCode == null || templateName == null || centreonUser == null || centreonPassword == null) {
			return "10 argumentos requeridos: arg1: Nome do host; arg2: Nome do servico; arg3: Codigo do cliente; arg4: Codigo do Ambiente; arg5: Codigo do Tipo do Ambiente; arg6: Codigo do Monitoramento Desejado; arg7: Codigo do Produto; arg8: Nome do template a ser utilizado (generic-service: de 5 em 5 min, 3c-valid-env: de 12 em 12h), arg9: Usuario; arg10: Senha.";
		}
		
		try {
			
			// Criando o serviço
			Process p1 = Runtime.getRuntime().exec(PATH_CLAPI + " -u " + centreonUser + " -p " + centreonPassword + " -o service -a add -v " + hostName + ";" + serviceName + ";" + templateName);
			BufferedReader stdout1 = new BufferedReader(new InputStreamReader(p1.getInputStream())); 		
			String line1 = stdout1.readLine();			
	
			while(line1 != null) {
				serviceCreationReturn = line1.toString() + " ";
				line1 = stdout1.readLine();
			}	
			
			// Associando o serviço a um comando
			Process p2 = Runtime.getRuntime().exec(PATH_CLAPI + " -u " + centreonUser + " -p " + centreonPassword + " -o service -a setparam -v " + hostName + ";" + serviceName + ";check_command;3CNagiosPlugin");
			BufferedReader stdout2 = new BufferedReader(new InputStreamReader(p2.getInputStream())); 		
			String line2 = stdout2.readLine();			
	
			while(line2 != null) {
				serviceCreationReturn = line2.toString() + " ";
				line2 = stdout2.readLine();
			}
							
			// Gravando os argumentos para o serviço				
			Process p3 = Runtime.getRuntime().exec(PATH_CLAPI + " -u " + centreonUser + " -p " + centreonPassword + " -o service -a setparam -v " + hostName + ";" + serviceName + ";" + "check_command_arguments;!" + clientCode + "!00!" + environmentCode + "!" + environmentTypeCode + "!" + monitoringCode + "!" + productCode);
			BufferedReader stdout3 = new BufferedReader(new InputStreamReader(p3.getInputStream())); 		
			String line3 = stdout3.readLine();			
		
			while(line3 != null) {
				serviceCreationReturn = line3.toString() + " ";
				line3 = stdout3.readLine();
			}
				
		}
		catch(IOException e) {
			e.printStackTrace();
		}
		
		if(serviceCreationReturn == null) {
			System.out.println("Servico " + serviceName + " criado com sucesso. Adicionado ao host " + hostName + ".");
			return "Servico " + serviceName + " criado com sucesso. Adicionado ao host " + hostName + ".";
		}
		else {
			System.out.println(serviceCreationReturn);
			return serviceCreationReturn;
		}

		
	}
	
	
	
	public String serviceDeletion(String hostName, String serviceName, String centreonUser, String centreonPassword) {
		
		if(hostName == null || serviceName == null || centreonUser == null || centreonPassword == null) {
			return "Verifique as entradas: Nome do host / Servico / Usuario / Senha.";
		}
				
		try {			
			Process p = Runtime.getRuntime().exec(PATH_CLAPI + " -u " + centreonUser + " -p " + centreonPassword + " -o SERVICE -a DEL -v " + hostName + ";" + serviceName);
			BufferedReader stdout = new BufferedReader(new InputStreamReader(p.getInputStream())); 		
			String line = stdout.readLine();			
	
			while(line != null) {
				System.out.println(line);
				serviceDeletionReturn = line.toString() + " ";
				line = stdout.readLine();
			}
		}
		catch(IOException e) {
			e.printStackTrace();
		}
		
		if(serviceDeletionReturn == null) {
			System.out.println("Servico deletado com sucesso: " + serviceName);
			return "Servico deletado com sucesso: " + serviceName;
		}
		else {
			System.out.println(serviceDeletionReturn);
			return serviceDeletionReturn;
		}


	}
	
	
	
	public void poller3CRestart(String centreonUser, String centreonPassword, String pollerNumber) {
		
		try {
			execRestart(PATH_CLAPI + " -u " + centreonUser + " -p " + centreonPassword + " -a pollergenerate -v " + pollerNumber);
			Thread.sleep(2000); // Espera 2 segundos
			execRestart(PATH_CLAPI + " -u " + centreonUser + " -p " + centreonPassword + " -a pollertest -v " + pollerNumber);
			Thread.sleep(2000);
			execRestart(PATH_CLAPI + " -u " + centreonUser + " -p " + centreonPassword + " -a cfgmove -v " + pollerNumber);
			Thread.sleep(2000);
			execRestart(PATH_CLAPI + " -u " + centreonUser + " -p " + centreonPassword + " -a pollerreload -v " + pollerNumber);

		} 
		catch (InterruptedException e) {
			e.printStackTrace();
		}
		
	}
	
	private void execRestart(String command) {

		try {			
			Process p = Runtime.getRuntime().exec(command);
			BufferedReader stdout = new BufferedReader(new InputStreamReader(p.getInputStream())); 		
			String line1 = stdout.readLine();			
	
			while(line1 != null) {
				System.out.println(line1);
				line1 = stdout.readLine();
			}
		}
		catch(IOException e) {
			e.printStackTrace();
		}
		
	}
	
	
	
	public void aclRestart(String centreonUser, String centreonPassword) {
	
		try {			
			Process p = Runtime.getRuntime().exec(PATH_CLAPI + " -u " + centreonUser + " -p " + centreonPassword + " -o acl -a reload");
			BufferedReader stdout = new BufferedReader(new InputStreamReader(p.getInputStream())); 		
			String line1 = stdout.readLine();			
	
			while(line1 != null) {
				System.out.println(line1);
				line1 = stdout.readLine();
			}
		}
		catch(IOException e) {
			e.printStackTrace();
		}

	}
	
	
}
	
	