package br.com.totvs.java3C.datasul.plugin;

import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.util.ArrayList;

import com.totvs.cloud.message.items.AvailabilityLoginDatasulItem;
import com.totvs.cloud.message.params.AvailabilityParamsLoginDatasul;
import com.totvs.cloud.service.RMICloudService;

import br.com.totvs.java3C.JSonParser.write.ParserWrite;
import br.com.totvs.java3C.bean.ItemAmbiente;
import br.com.totvs.java3C.bean.datasul.AppServer;
import br.com.totvs.java3C.bean.datasul.JBossInfo;
import br.com.totvs.java3C.dao.Dao;
import br.com.totvs.java3C.util.ValidacaoStatusAmb;

public class DispLoginDatasul {
	
	private RMICloudService cloudService;
	private ArrayList<ItemAmbiente> itensAmbiente;
	private String ZBB_STATUS;
	private String ZCA_PARAM;
	private String ZCA_CODAMB;
	private String ZCA_TIPAMB;
	private String ZCA_ITEM;
	private float ZCA_RESULT = 0;
	private String ZCA_MEMO = "Login Datasul Inativo.";
	
	private ArrayList<AppServer> appServInfo;
	private ArrayList<JBossInfo> jBossInfo;
    private String usuarioItemAmbiente;
    private String senhaItemAmbiente;   
	
	

	public DispLoginDatasul(String codAmbiente, String codTipoAmbiente, String codMonitoramento, String codProduto) {
		
		// Deve-se executar o monitoramento de todos os JBoss´s vinculados a cada servidor de aplicação, cujo  
		// checkbox "Monitora ?" esteja marcado (T = true). Todos os servidores de aplicação (Datasul > AppServer/WebSpeed > AppServer) 
		// têm Jboss´s vinculados a eles, através do campo "AppServer" em Datasul > Web > JBoss > ZBT_APPSER.
		
		// O retorno será O.K. (100%) caso apenas um JBoss retorne status "Ativo"
		

		this.ZCA_PARAM = codMonitoramento; 
		this.ZCA_CODAMB = codAmbiente;
		this.ZCA_TIPAMB = codTipoAmbiente;
		
		Dao dao = new Dao();
		itensAmbiente = dao.getItensAmbiente(codAmbiente, codTipoAmbiente);
		appServInfo = dao.getAppServers(codAmbiente, codTipoAmbiente, codProduto);
		jBossInfo = dao.getJBossInfo(codAmbiente, codTipoAmbiente, codProduto);
		dao.closeConnection();

		for(ItemAmbiente i : itensAmbiente) {
			if(i.getCodProduto().trim().equals(codProduto)) {
				ZCA_ITEM = i.getCodItem().trim();
				ZBB_STATUS = i.getStatus().trim();
				usuarioItemAmbiente = i.getUsuario();
				senhaItemAmbiente = i.getSenha();
			}
		}
		
		// Validação se o ambiente encontra-se com status ATIVO, EM MANUTENÇÃO, CADASTRO, SUSPENSO ou DESATIVADO. 
		new ValidacaoStatusAmb(ZBB_STATUS, ZCA_CODAMB, ZCA_ITEM, ZCA_PARAM, ZCA_TIPAMB);
		
		// Se nenhuma informação de AppSrv foi cadastrada no 3C
		if(appServInfo.size() == 0) {
			System.out.println("Informacoes do servidor de aplicacao nao cadastradas. Verifique o cadastro do 3C.");
			System.exit(1);
		}

		if(jBossInfo.size() == 0) { // Checando se não há informações cadastradas em Datasul > Web > JBoss 
			System.out.println("Informacoes do JBoss nao cadastradas. Verifique o cadastro do 3C.");
			System.exit(1);
		}
		
		for(AppServer a : appServInfo) {  
					
			
			if(a.getMonitora().trim().equals("T")) { // Se o checkbox "Monitora ?" estiver marcado
				
				String hostMonit = a.getIp().trim(); // Pressupõe-se o serviço de monitoramento estar executando no servidor de aplicação
				String nomeAppSrv = a.getNomeHost();
				String portaAppSrv = a.getPortaApp();
				String seq = a.getSeq(); // Datasul > AppServer/WebSpeed > AppServer > Seq
						
				for(JBossInfo j : jBossInfo) {
					
					String protocoloJBoss = j.getProtocolo();
					String nomeHostJBoss = j.getNomeHost();
					String portaJBoss = j.getPorta();
					String jBossSeqAppSer = j.getSeqAppSer(); // Valor atribuido a jBossSeqAppSer está em: Datasul > Web > JBoss > AppServer 
																			 
					if(jBossSeqAppSer.equals(seq)) {
												
						StringBuilder rmiStrConnection = new StringBuilder();
						
					    try
					    {
					    	rmiStrConnection.append("rmi://");
					    	rmiStrConnection.append(hostMonit);
					    	rmiStrConnection.append(":");
					    	rmiStrConnection.append("1099");
					    	rmiStrConnection.append("/DatasulCloudMonitor");
					      
					    	this.cloudService = ((RMICloudService)Naming.lookup(rmiStrConnection.toString()));
					    	
					    }
					    catch (RemoteException e)
					    {
					    	System.out.println("Nao foi possivel efetuar conexao RMI com o servidor de monitoramento: " + rmiStrConnection.toString());
					    	System.exit(1);
					    }
					    catch (MalformedURLException e1)
					    {
					    	System.out.println("Url INCORRETA para conexao RMI com o servidor de monitoramento: " + rmiStrConnection.toString());
					        System.exit(1);
					    }
					    catch (NotBoundException e1)
					    {
					    	System.out.println("Erro durante tentativa de procura (lookup) ou desvinculo (unbind). Nome nao possui qualquer ligacao associada ao registro (registry): " + rmiStrConnection.toString());
					        System.exit(1);
					    }
						
					    
				    	AvailabilityParamsLoginDatasul paramsLoginDatasul = new AvailabilityParamsLoginDatasul();
				    	 
				        paramsLoginDatasul.setProtocol(protocoloJBoss.trim()); 
				        paramsLoginDatasul.setHost(nomeHostJBoss.trim()); 
				        paramsLoginDatasul.setPort(portaJBoss.trim()); 
				        paramsLoginDatasul.setUserName(usuarioItemAmbiente.trim());
				        paramsLoginDatasul.setPassWord(senhaItemAmbiente.trim());
				        paramsLoginDatasul.setRemoteServerName(nomeAppSrv.trim());
				        paramsLoginDatasul.setRemoteServerPort(Integer.valueOf(portaAppSrv.trim()));  
				        
				        try
				        {
				          AvailabilityLoginDatasulItem loginDatasulItem = this.cloudService.availabilityLoginDatasul(paramsLoginDatasul);
				          
				          if(loginDatasulItem.getStatus().trim().toUpperCase().equals("ATIVO")) {
				        	  ZCA_RESULT = 100;
				          	  ZCA_MEMO = "Login Datasul Ativo.";
				          }
				          
				        }
				        catch (RemoteException e)
				        {
					    	System.out.println(e.getMessage());
				    		System.exit(1);
				        }
														
					}
				}
			}
			else {
				System.out.println("Nenhum App Server configurado para ser monitorado. Verifique no 3C, o checkbox \"Monitora?\" em Datasul > AppServer/WebSpeed");
				System.exit(1);				
			}					
		}
		

		grava();
		
				
	}
	
	public float getZCA_RESULT() {
		return ZCA_RESULT;
	}
	
	public String getZCA_MEMO() {
		return ZCA_MEMO;
	}
	
	public void grava() {
		
//	   	ParserWrite parserWrite = new ParserWrite("GENERIC_WRITE", ZCA_CODAMB, ZCA_TIPAMB, ZCA_ITEM, ZCA_PARAM, String.valueOf(ZCA_RESULT), ZCA_MEMO);
		ParserWrite parserWrite = new ParserWrite("AVAILABILITY_DATASUL", ZCA_CODAMB, ZCA_TIPAMB, ZCA_ITEM, ZCA_PARAM, String.valueOf(ZCA_RESULT), ZCA_MEMO);
	   	
	   	
	   	if(parserWrite.getStatus().equals("0")) { // Se correu tudo bem com a gravação
	   		System.out.println("Resultado: " + ZCA_RESULT + ". "+ ZCA_MEMO);
	   		System.exit(Integer.parseInt(parserWrite.getLimiar()));
	   	}
	   	else {
	   		System.out.println(parserWrite.getMessage());
	   		System.exit(Integer.parseInt(parserWrite.getLimiar()));
	   	}
	
	}
	
	
}
