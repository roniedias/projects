package br.com.totvs.java3C.datasul.plugin;

//Verificar qual campo irá corresponder ao IP do processo de monitoramento. 
//Para esta classe, esta informação está sendo obtida através do campo Datasul > Atalhos > Atalho > I.P. Host
//Verificar se não deve-se obter o IP do AppServer


import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.List;

import br.com.totvs.java3C.JSonParser.write.ParserWrite;
import br.com.totvs.java3C.bean.ItemAmbiente;
import br.com.totvs.java3C.dao.Dao;
import br.com.totvs.java3C.datasul.bean.AppServer;
import br.com.totvs.java3C.datasul.bean.AtalhoInfo;
import br.com.totvs.java3C.datasul.bean.Banco;
import br.com.totvs.java3C.util.ValidacaoStatusAmb;

import com.totvs.cloud.message.items.AvailabilityDatabaseItem;
import com.totvs.cloud.message.params.AvailabilityParamsEMSDatabases;
import com.totvs.cloud.service.RMICloudService;

public class DispEMSDatabases {
	
	private RMICloudService cloudService;
	private ArrayList<Banco> bancos;
	private ArrayList<ItemAmbiente> itensAmbiente;
	private List<AvailabilityDatabaseItem> avalItems = new ArrayList<AvailabilityDatabaseItem>();
	private ArrayList<AppServer> appServInfo;	
	private AtalhoInfo atalhoInfo;
	private String dirProwin32;
	private String dirArquivoPf;
	private String dirArquivoIni;
	private String ZBB_STATUS;
	private String ZCA_PARAM;
	private String ZCA_CODAMB;
	private String ZCA_TIPAMB;
	private String ZCA_ITEM;
	private float ZCA_RESULT = 100;
	private String ZCA_MEMO;
	private String appServerIp = "no_info";
	private String portaMonit;

	// Todas as informações, com exceção dos nomes dos bancos (presentes em Datasul > BANCOS > Nome Fisico), 
    //	serão obtidas a partir de Datasul > Atalhos > Atalho, inclusive o IP do servidor de monitoramento. 

	// TODOS os bancos devem estar DISPONÍVEIS. Deve-se retornar o status INDISPONÍVEL, caso haja apenas um 
    // banco indisponível	


	
	public DispEMSDatabases(String codAmbiente, String codTipoAmbiente, String codMonitoramento, String codProduto) {
		
		this.ZCA_PARAM = codMonitoramento; 
		this.ZCA_CODAMB = codAmbiente;
		this.ZCA_TIPAMB = codTipoAmbiente;
		
		Dao dao = new Dao();
		itensAmbiente = dao.getItensAmbiente(codAmbiente, codTipoAmbiente);
		atalhoInfo = dao.getAtalhoInfo(codAmbiente, codTipoAmbiente, codProduto);
		appServInfo = dao.getAppServers(codAmbiente, codTipoAmbiente, codProduto);		
		bancos = dao.getBancos(codAmbiente, codTipoAmbiente, codProduto);
		portaMonit = dao.getCliente(codAmbiente).getPortaMonit();
		dao.closeConnection();
		
		for(ItemAmbiente i : itensAmbiente) {
			if(i.getCodProduto().trim().equals(codProduto)) {
				ZCA_ITEM = i.getCodItem().trim();
				ZBB_STATUS = i.getStatus().trim();
			}
		}
		
		
		// Validação se o ambiente encontra-se com status ATIVO, EM MANUTENÇÃO, CADASTRO, SUSPENSO ou DESATIVADO. 
		new ValidacaoStatusAmb(ZBB_STATUS, ZCA_CODAMB, ZCA_ITEM, ZCA_PARAM, ZCA_TIPAMB);
		

		for(AppServer a : appServInfo) { // Checando se há pelo menos um checkbox selecionado e obtendo o IP  
			if(a.getMonitora().trim().equals("T")) { 
				appServerIp = a.getIp().trim();
				break;
			}
		}
		
		if(appServerIp.equals("no_info")) {
			System.out.println("Nenhum App Server configurado para ser monitorado. Verifique no 3C, o checkbox \"Monitora?\" em Datasul > AppServer/WebSpeed");
			System.exit(1);
		}
		

		try {
			dirProwin32 = atalhoInfo.getDirProwin32().trim();
			dirArquivoPf = atalhoInfo.getDirArquivoPf().trim();
			dirArquivoIni = atalhoInfo.getDirArquivoIni().trim();			
		}
		catch (NullPointerException n) {	// Nenhum Atalho foi cadastrado no 3C
			System.out.println("Nenhuma informacao localizada em Datasul > Atalhos > Atalho. Verifique o cadastro do 3C.");
			System.exit(1);
		}
		
		
		StringBuilder rmiStrConnection = new StringBuilder();
		
	    try
	    {
	    	rmiStrConnection.append("rmi://");
	    	rmiStrConnection.append(appServerIp);
	    	rmiStrConnection.append(":");
	    	rmiStrConnection.append(portaMonit);
	    	rmiStrConnection.append("/DatasulCloudMonitor");
	      
	    	this.cloudService = ((RMICloudService)Naming.lookup(rmiStrConnection.toString()));
	    	
	    }
	    catch (RemoteException e)
	    {
	    	System.out.println("Nao foi possivel efetuar conexao RMI com o servidor de monitoramento: " + rmiStrConnection.toString());
	    	System.exit(1);
	    }
	    catch (MalformedURLException e)
	    {
	    	System.out.println("Url INCORRETA para conexao RMI com o servidor de monitoramento: " + rmiStrConnection.toString());
	        System.exit(1);
	    }
	    catch (NotBoundException e)
	    {
	    	System.out.println("Erro durante tentativa de procura (lookup) ou desvinculo (unbind). Nome nao possui qualquer ligacao associada ao registro (registry): " + rmiStrConnection.toString());
	        System.exit(1);
	    }
		
        
        AvailabilityParamsEMSDatabases paramsEMSDatabases = new AvailabilityParamsEMSDatabases();
        paramsEMSDatabases.setDlcPath(dirProwin32); // Dir. Progress (D:\dlc102b\bin\prowin32.exe, para o Item DATASUL 11)
        paramsEMSDatabases.setPfPath(dirArquivoPf); // PFs (D:\datasul\clientes\totvs-cloud\atalhos\dts11.pf, para o Item DATASUL 11) 
        paramsEMSDatabases.setIniPath(dirArquivoIni); // INI (D:\datasul\clientes\totvs-cloud\atalhos\dts11.ini, para o Item DATASUL 11)
        
        String dbNames[] = new String[bancos.size()]; 
	      
	        for(int n = 0; n < bancos.size(); n++) {
	        	dbNames[n] = bancos.get(n).getNomeFisico().trim();
	        }
	        	
	        
        paramsEMSDatabases.setDbNames(dbNames);
        
        try
        {

        	avalItems = this.cloudService.availabilityEMSDatabases(paramsEMSDatabases);
		    
			ZCA_MEMO = "BANCOS EMS: ";
			
		   	for (AvailabilityDatabaseItem item : avalItems) {
		   		
		   		if(item.getStatus().trim().toUpperCase().equals("INATIVO"))
		   			ZCA_RESULT = 0;
		   		
			   	ZCA_MEMO += item.getItemName() + " = " + item.getStatus() + "; ";
		    }
		 
        }   		
        catch (RemoteException e)
        {
	    	System.out.println(e.getMessage());
    		System.exit(1);
        }

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
