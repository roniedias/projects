package br.com.totvs.java3C.datasul.plugin;

import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.List;

import br.com.totvs.java3C.JSonParser.write.ParserWrite;
import br.com.totvs.java3C.bean.ItemAmbiente;
import br.com.totvs.java3C.bean.datasul.AppServer;
import br.com.totvs.java3C.bean.datasul.Banco;
import br.com.totvs.java3C.dao.Dao;
import br.com.totvs.java3C.util.ValidacaoStatusAmb;

import com.totvs.cloud.message.items.AvailabilityDatabaseItem;
import com.totvs.cloud.message.params.AvailabilityParamsDatasulDatabases;
import com.totvs.cloud.service.RMICloudService;

public class DispDatasulDatabases {
	
	private RMICloudService cloudService;
	private ArrayList<Banco> bancos;
	private ArrayList<ItemAmbiente> itensAmbiente;
	private ArrayList<AppServer> appServInfo;
	
	private String appServerIp = "no_info";
	private String appServerNome;
	private String appServerPortaNs;
	private String appServerInstancia;
	
	private String ZBB_STATUS;
	private String ZCA_PARAM;
	private String ZCA_CODAMB;
	private String ZCA_TIPAMB;
	private String ZCA_ITEM;
	private float ZCA_RESULT = 100;
	private String ZCA_MEMO;
	
	private List<AvailabilityDatabaseItem> avalItems = new ArrayList<AvailabilityDatabaseItem>();
	
	
	// Todas as informações, com exceção dos nomes dos bancos (presentes em Datasul > BANCOS > Nome Fisico), 
    //	serão obtidas a partir do servidor de aplicação, em Datasul > AppServer/WebSpeed > AppServer. Deve-se 
    //	obter esta informação a partir da primeira linha que estiver com o checkbox "Monitora" 
    //	selecionado (ZBR_MONITO = T)
 	
    // TODOS os bancos devem estar DISPONÍVEIS. Deve-se retornar o status INDISPONÍVEL, caso haja apenas um 
    // banco indisponível	

	
	public DispDatasulDatabases(String codAmbiente, String codTipoAmbiente, String codMonitoramento, String codProduto) {

		
		this.ZCA_PARAM = codMonitoramento; 
		this.ZCA_CODAMB = codAmbiente;
		this.ZCA_TIPAMB = codTipoAmbiente;
		
		Dao dao = new Dao();
		appServInfo = dao.getAppServers(codAmbiente, codTipoAmbiente, codProduto);
		itensAmbiente = dao.getItensAmbiente(codAmbiente, codTipoAmbiente);
		bancos = dao.getBancos(codAmbiente, codTipoAmbiente, codProduto);
		dao.closeConnection();
		
		for(ItemAmbiente i : itensAmbiente) {
			if(i.getCodProduto().trim().equals(codProduto)) {
				ZCA_ITEM = i.getCodItem().trim();
				ZBB_STATUS = i.getStatus().trim();
			}
		}
		
		// Validação se o ambiente encontra-se com status ATIVO, EM MANUTENÇÃO, CADASTRO, SUSPENSO ou DESATIVADO. 
		new ValidacaoStatusAmb(ZBB_STATUS, ZCA_CODAMB, ZCA_ITEM, ZCA_PARAM, ZCA_TIPAMB);
		
		// Validando se algum App Server foi cadastrado no 3C
		if(appServInfo.size() == 0) {
			System.out.println("Informacoes do servidor de aplicacao nao cadastradas. Verifique o cadastro do 3C.");
			System.exit(1);
		}
		
		for(AppServer appsrv : appServInfo) {
			if(appsrv.getMonitora().trim().equals("T")) {
				appServerIp = appsrv.getIp().trim();
				appServerNome = appsrv.getNomeHost().trim();
				appServerPortaNs = appsrv.getPortaNs().trim();
				appServerInstancia = appsrv.getInstanciaApp().trim();
				break;
			}
		}
				
		if(appServerIp.equals("no_info")) {
			System.out.println("Nenhum App Server configurado para ser monitorado. Verifique no 3C, o checkbox \"Monitora?\" em Datasul > AppServer/WebSpeed");
			System.exit(1);
		}
		
		
		// Nenhuma informação de banco(s) de dados foi cadastrada no 3C (em Datasul > Bancos > Bancos)
		if(bancos.size() == 0) { 
			System.out.println("Informacao(oes) de banco(s) de dado(s) nao cadastrada(s) no 3C. Verifique o cadastro.");
			System.exit(1);
		}
		else {
			
			// Esses dois valores irão ser repetir, qualquer que seja a linha de Datasul > Bancos > Bancos 

			StringBuilder rmiStrConnection = new StringBuilder();
			
		    try
		    {
		    	rmiStrConnection.append("rmi://");
		    	rmiStrConnection.append(appServerIp);
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
			
			      
			AvailabilityParamsDatasulDatabases paramsDatasulDatabases = new AvailabilityParamsDatasulDatabases();
			paramsDatasulDatabases.setAppServerName(appServerNome);     // Nm. Host (JCHIBMA685, para o Item DATASUL 11)
			paramsDatasulDatabases.setAppServerPort(appServerPortaNs);     // Porta NS (5162, para o Item DATASUL 11)
			paramsDatasulDatabases.setAppServerApp(appServerInstancia); // Nome (datasul-11510-totvs-cloud, para o Item DATASUL 11)
			      
			String dbNames[] = new String[bancos.size()]; 
			      
			for(int n = 0; n < bancos.size(); n++) 
				dbNames[n] = bancos.get(n).getNomeFisico().trim(); // Datasul > Bancos > Bancos > Nome Fisico 
			      														// Ex: ems2adt, ems2emp, finance, etc., para o Item DATASUL 11		
			paramsDatasulDatabases.setDbNames(dbNames);
			      
			try {
			
				avalItems = this.cloudService.availabilityDatasulDatabases(paramsDatasulDatabases);
			    
				ZCA_MEMO = "BANCOS DATASUL: ";
				
			   	for (AvailabilityDatabaseItem item : avalItems) {
			   		
			   		if(item.getStatus().trim().toUpperCase().equals("INATIVO"))
			   			ZCA_RESULT = 0;
			   		
				   	ZCA_MEMO += item.getItemName() + " = " + item.getStatus() + "; ";
			    }
			    
		    }
			catch (RemoteException e) {
		    	System.out.println(e.getMessage());
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

		
	   	//ParserWrite parserWrite = new ParserWrite("GENERIC_WRITE", ZCA_CODAMB, ZCA_TIPAMB, ZCA_ITEM, ZCA_PARAM, String.valueOf(ZCA_RESULT), ZCA_MEMO);
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
