package br.com.totvs.java3C.datasul.plugin;

import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.util.ArrayList;

import com.totvs.cloud.message.items.AvailabilityEAI2Item;
import com.totvs.cloud.message.params.AvailabilityParamsEAI2;
import com.totvs.cloud.service.RMICloudService;

import br.com.totvs.java3C.JSonParser.write.ParserWrite;
import br.com.totvs.java3C.bean.ItemAmbiente;
import br.com.totvs.java3C.bean.datasul.Eai2Info;
import br.com.totvs.java3C.dao.Dao;
import br.com.totvs.java3C.util.ValidacaoStatusAmb;


public class DispEAI2 {
	
	private String ZBB_STATUS;
	private String ZCA_PARAM;
	private String ZCA_CODAMB;
	private String ZCA_TIPAMB;
	private String ZCA_ITEM;
	private float ZCA_RESULT;
	private String ZCA_MEMO;
	
	private RMICloudService cloudService;
	private ArrayList<ItemAmbiente> itensAmbiente;
	Eai2Info eai2Info;
	
	private String protocoloEai2;
	private String ipEai2;
	private String portaEai2;
	private String portaMonit;
	
	

	public DispEAI2(String codAmbiente, String codTipoAmbiente, String codMonitoramento, String codProduto) {

		this.ZCA_PARAM = codMonitoramento; 
		this.ZCA_CODAMB = codAmbiente;
		this.ZCA_TIPAMB = codTipoAmbiente;

	
		Dao dao = new Dao();
		itensAmbiente = dao.getItensAmbiente(codAmbiente, codTipoAmbiente);
		eai2Info = dao.getEai2Info(codAmbiente, codTipoAmbiente, codProduto);
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
		
    	protocoloEai2 = eai2Info.getProtocolo();     
    	ipEai2 = eai2Info.getIp(); 
    	portaEai2 = eai2Info.getPorta();
		
		if(protocoloEai2.isEmpty() || ipEai2.isEmpty() || portaEai2.isEmpty()) {
	    	System.out.println("Uma ou mais informacoes nao localizada(s) em Datasul > Integrator/EAI > EAI2 (\"Protocolo\", \"I.P. Host\", \"Porta\"). Verifique o cadastro do 3C.");
			System.exit(1);
		}
		else {

			StringBuilder rmiStrConnection = new StringBuilder();
			
		    try
		    {
		    	rmiStrConnection.append("rmi://");
		    	rmiStrConnection.append(ipEai2);
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
		    
	        AvailabilityParamsEAI2 paramsEAI2 = new AvailabilityParamsEAI2();
	        paramsEAI2.setProtocol(protocoloEai2); 
	        paramsEAI2.setServer(ipEai2); 
	        paramsEAI2.setPort(portaEai2); 
	        
	        AvailabilityEAI2Item eai2Item = null;
	        
	        try
	        {
	        	eai2Item = this.cloudService.availabilityEAI2(paramsEAI2);
	            ZCA_MEMO = "Status Servidor EAI2: " + eai2Item.getStatus();
	            
	            if(eai2Item.getStatus().trim().toUpperCase().equals("ATIVO")) 
	            	ZCA_RESULT = 100;
	            else 
	            	ZCA_RESULT = 0;
	        }
	        catch (RemoteException e)
	        {
		    	System.out.println(e.getMessage());
	    		System.exit(1);
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
