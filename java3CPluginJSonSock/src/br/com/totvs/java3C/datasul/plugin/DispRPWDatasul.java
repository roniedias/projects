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
import br.com.totvs.java3C.dao.Dao;
import br.com.totvs.java3C.util.ValidacaoStatusAmb;

import com.totvs.cloud.message.items.AvailabilityRPWItem;
import com.totvs.cloud.message.params.AvailabilityParamsDatasul;
import com.totvs.cloud.service.RMICloudService;

public class DispRPWDatasul {

	private RMICloudService cloudService;
	ArrayList<AppServer> appServInfo;
	private ArrayList<ItemAmbiente> itensAmbiente;
	private List<AvailabilityRPWItem> avalRPWList = new ArrayList<AvailabilityRPWItem>();
	private String ZBB_STATUS;
	private String ZCA_PARAM;
	private String ZCA_CODAMB;
	private String ZCA_TIPAMB;
	private String ZCA_ITEM;
	private float ZCA_RESULT = 0;
	private String ZCA_MEMO = new String();
	private String portaMonit;
	
	
	public DispRPWDatasul(String codAmbiente, String codTipoAmbiente, String codMonitoramento, String codProduto) {
		
		this.ZCA_PARAM = codMonitoramento; 
		this.ZCA_CODAMB = codAmbiente;
		this.ZCA_TIPAMB = codTipoAmbiente;
		
		Dao dao = new Dao();
		appServInfo = dao.getAppServers(codAmbiente, codTipoAmbiente, codProduto);
		itensAmbiente = dao.getItensAmbiente(codAmbiente, codTipoAmbiente);
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


		
		// Se nenhuma informação de AppSrv foi cadastrada no 3C
		if(appServInfo.size() == 0) {
			System.out.println("Informacoes do servidor de aplicacao nao cadastradas ou incompletas. Verifique o cadastro do 3C.");
			System.exit(1);
		}
		else {
			for(int a = 0; a < appServInfo.size(); a++) {
				
				
		    	// Monitorar apenas a primeira linha, pois este método faz uma consulta 
		    	// no banco de dados e retorna as informações de todos os servidores

		    	// OK se tiver pelo menos um servidor ativo (traz vários resultados)
		    	
		    	// Dos resultados retornados (servidores), se tiver pelo menos um ativo, considerar o status final como ATIVO

				
		    	// Datasul > AppServer/WebSpeed				
				String hostMonit = appServInfo.get(a).getIp().trim();
				String nomeAppSrv = appServInfo.get(a).getNomeHost().trim();
				String portaNsSrv = appServInfo.get(a).getPortaNs().trim();
				String instanciaAppSrv = appServInfo.get(a).getInstanciaApp().trim();

								
				StringBuilder rmiStrConnection = new StringBuilder();
				
			    try
			    {
			    	rmiStrConnection.append("rmi://");
			    	rmiStrConnection.append(hostMonit);
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
			    
			    
		        AvailabilityParamsDatasul paramsRPWDatasul = new AvailabilityParamsDatasul();
		        paramsRPWDatasul.setAppServerName(nomeAppSrv);     
		        paramsRPWDatasul.setAppServerPort(portaNsSrv);     
		        paramsRPWDatasul.setAppServerApp(instanciaAppSrv); 
		        	        
		        try 
		        {
		        	avalRPWList = this.cloudService.availabilityRPWDatasul(paramsRPWDatasul);
		        	
		        	ZCA_MEMO = "Servidores RPW: ";
		        	
		            for (int i = 0; i < avalRPWList.size(); i++) {
		            	
		            	ZCA_MEMO += "Codigo = " + avalRPWList.get(i).getServerCode() + ", Descricao = " + avalRPWList.get(i).getServerDesc() + ", Estatus = " + avalRPWList.get(i).getState() + "; ";
		            
		            	if (avalRPWList.get(i).getState().equals("ativo")) {
		            		int index = ZCA_MEMO.lastIndexOf(";");
		            		ZCA_MEMO = ZCA_MEMO.substring(0, index);
		            		ZCA_MEMO += ", ";
		            		ZCA_MEMO += "Executando = " + avalRPWList.get(i).getExecution() + "; ";
		            	}
		            	
		            }
		        }
		        catch (RemoteException e)
		        {
			    	System.out.println(e.getMessage());
		    		System.exit(1);
		        }
		        
		        if(avalRPWList.size() > 0)
		        	break; 	// 	Se foi possível obter as informações do(s) servidor(es) RPW, sai do loop 
  
			}
			
		}
		
		if(avalRPWList.size() == 0) {    
			System.out.println("Nenhum servidor localizado. Por favor, verifique as parametrizacoes dos servidores no(s) arquivo(s) de configuracao do sistema RPW.");
			System.exit(1);
		}
		
		// Checagem, se pelo menos um servidor RPW encontra-se com status ATIVO. Em caso afirmativo, o resultados será 100%
		for(AvailabilityRPWItem a : avalRPWList) {
			if(a.getState().trim().toUpperCase().equals("ATIVO")) {				
				ZCA_RESULT = 100;
				break;
			}
		}
		
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
