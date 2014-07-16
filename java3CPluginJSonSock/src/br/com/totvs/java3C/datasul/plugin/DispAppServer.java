package br.com.totvs.java3C.datasul.plugin;

import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.util.ArrayList;

import com.totvs.cloud.message.items.AvailabilityAppServerItem;
import com.totvs.cloud.message.params.AvailabilityParamsDatasul;
import com.totvs.cloud.service.RMICloudService;
import br.com.totvs.java3C.JSonParser.write.ParserWrite;
import br.com.totvs.java3C.bean.ItemAmbiente;
import br.com.totvs.java3C.bean.datasul.AppServer;
import br.com.totvs.java3C.dao.Dao;
import br.com.totvs.java3C.util.ValidacaoStatusAmb;



public class DispAppServer {
	
	private RMICloudService cloudService;
	private ArrayList<AppServer> appServInfo;
	private ArrayList<ItemAmbiente> itensAmbiente;
	private ArrayList<AvailabilityAppServerItem> resultados = new ArrayList<AvailabilityAppServerItem>();
	private boolean isUmMonitoraSelec = false;
	private boolean isAppServerAtivo = false;
	private String ZBB_STATUS;
	private String ZCA_PARAM;
	private String ZCA_CODAMB;
	private String ZCA_TIPAMB;
	private String ZCA_ITEM;
	private float ZCA_RESULT;
	private String ZCA_MEMO = new String();
	

	

	public DispAppServer(String codAmbiente, String codTipoAmbiente, String codMonitoramento, String codProduto) {
		
		this.ZCA_PARAM = codMonitoramento; 
		this.ZCA_CODAMB = codAmbiente;
		this.ZCA_TIPAMB = codTipoAmbiente;

	
		Dao dao = new Dao();
		appServInfo = dao.getAppServers(codAmbiente, codTipoAmbiente, codProduto);
		itensAmbiente = dao.getItensAmbiente(codAmbiente, codTipoAmbiente);
		dao.closeConnection();
		
		 
		for(ItemAmbiente i : itensAmbiente) {
			if(i.getCodProduto().trim().equals(codProduto)) {
				ZCA_ITEM = i.getCodItem().trim();
				ZBB_STATUS = i.getStatus().trim();
			}
		}
		
		
		// Validação se o ambiente encontra-se com status ATIVO, EM MANUTENÇÃO, CADASTRO, SUSPENSO ou DESATIVADO. 
		new ValidacaoStatusAmb(ZBB_STATUS, ZCA_CODAMB, ZCA_ITEM, ZCA_PARAM, ZCA_TIPAMB);


		for(AppServer a : appServInfo) { // Checando se há pelo menos um checkbox selecionado 
			if(a.getMonitora().trim().equals("T")) 
				isUmMonitoraSelec = true;
		}
		
		if(!isUmMonitoraSelec) {
			System.out.println("Nenhum App Server configurado para ser monitorado. Verifique no 3C, o checkbox \"Monitora?\" em Datasul > AppServer/WebSpeed");
			System.exit(1);
		}
		
		
		// Se nenhuma informação de AppSrv foi cadastrada no 3C
		if(appServInfo.size() == 0) {
			System.out.println("Informacoes do servidor de aplicacao nao cadastradas. Verifique o cadastro do 3C.");
			System.exit(1);
		}
		else {
			for(AppServer a : appServInfo) {

				if(a.getMonitora().trim().equals("T")) {

					// Monitorar todas as linhas que estiverem com o check box marcado (ZBR_MONITO = T)
			    	
			    	// Se de todos os que forem analisados, houver apenas um que esteja disponível, 
			    	//o retorno deverá ser apresentado como DISPONÍVEL 
			    	
			    	// Datasul > AppServer/WebSpeed
					String hostMonit = a.getIp().trim();
					String nomeAppSrv = a.getNomeHost().trim();
					String portaNsSrv = a.getPortaNs().trim();
					String instanciaAppSrv = a.getInstanciaApp().trim();
									
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
				    
			    	AvailabilityParamsDatasul paramsDatasul = new AvailabilityParamsDatasul();
			    	paramsDatasul.setAppServerName(nomeAppSrv);     // Nm. Host (JCHIBMA685, para o Item DATASUL 11)
			    	paramsDatasul.setAppServerPort(portaNsSrv);     // Porta NS (5162, para o Item DATASUL 11)
			    	paramsDatasul.setAppServerApp(instanciaAppSrv); // Nome (datasul-11510-totvs-cloud, para o Item DATASUL 11)
			    	AvailabilityAppServerItem appServerItem;
			      
			    	try {	    	  
			    		appServerItem = this.cloudService.availabilityAppServer(paramsDatasul);	      
			    		resultados.add(appServerItem);
			    	}
			    	catch (RemoteException e)
			    	{
				    	System.out.println(e.getMessage());
			    		System.exit(1);
			    	}
					
				} 
			
			}
						
		}
	
				
		for(AvailabilityAppServerItem r : resultados) {
			if(r.getAppServerStatus().trim().toUpperCase().equals("ATIVO")) 
				isAppServerAtivo = true;
			
			ZCA_MEMO += "App Server " + r.getAppServerName() + " porta " + r.getAppServerPort() + " instancia " + r.getAppServerApp() + ": Status = " + r.getAppServerStatus() + "; ";			
		}
		
		if(isAppServerAtivo) {
			ZCA_RESULT = 100;
		}
		else {
			ZCA_RESULT = 0;
		}
		
		
//		System.out.println("ZCA_CODAMB: " + ZCA_CODAMB); 
//		System.out.println("ZCA_TIPAMB: " + ZCA_TIPAMB); 
//		System.out.println("ZCA_ITEM: " + ZCA_ITEM);     
//		System.out.println("ZCA_PARAM: " + ZCA_PARAM);   
//		System.out.println("ZCA_RESULT: " + ZCA_RESULT); 
//		System.out.println("ZCA_MEMO: " + ZCA_MEMO);
//		System.out.println("ZBB_STATUS: " + ZBB_STATUS);
		
		
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
