package br.com.totvs.java3C.datasul.plugin;

import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.List;

import br.com.totvs.java3C.JSonParser.write.ParserWrite;
//import br.com.totvs.java3C.JSonParser.write.ParserWrite;
import br.com.totvs.java3C.bean.ItemAmbiente;
import br.com.totvs.java3C.bean.datasul.RpwLegado;
import br.com.totvs.java3C.dao.Dao;
import br.com.totvs.java3C.util.ValidacaoStatusAmb;

import com.totvs.cloud.message.items.AvailabilityRPWItem;
import com.totvs.cloud.message.params.AvailabilityParamsRPWEMS;
import com.totvs.cloud.service.RMICloudService;

public class DispRPWEMS {
	
	private String ZBB_STATUS;
	private String ZCA_PARAM;
	private String ZCA_CODAMB;
	private String ZCA_TIPAMB;
	private String ZCA_ITEM;
	private float ZCA_RESULT = 100;
	private String ZCA_MEMO;
	
	private RMICloudService cloudService;
	private ArrayList<ItemAmbiente> itensAmbiente;
	private RpwLegado rpwLegado;
	private String bancoFoundation;
	
	private String monitSrv;
	private String dirProwin32;
	private String dirArquivoPf;
	private String dirArquivoIni;
	


	public DispRPWEMS(String codAmbiente, String codTipoAmbiente, String codMonitoramento, String codProduto) {

		this.ZCA_PARAM = codMonitoramento; 
		this.ZCA_CODAMB = codAmbiente;
		this.ZCA_TIPAMB = codTipoAmbiente;

	
		Dao dao = new Dao();
		itensAmbiente = dao.getItensAmbiente(codAmbiente, codTipoAmbiente);
		//atalhoInfo = dao.getAtalhoInfo(codAmbiente, codTipoAmbiente, codProduto);
		rpwLegado = dao.getRpwLegado(codAmbiente, codTipoAmbiente, codProduto);
		bancoFoundation = dao.getBancoFoundation(codAmbiente, codTipoAmbiente, codProduto);
		dao.closeConnection();
		
		for(ItemAmbiente i : itensAmbiente) {
			if(i.getCodProduto().trim().equals(codProduto)) {
				ZCA_ITEM = i.getCodItem().trim();
				ZBB_STATUS = i.getStatus().trim();				
			}
		}

		
		// Validação se o ambiente encontra-se com status ATIVO, EM MANUTENÇÃO, CADASTRO, SUSPENSO ou DESATIVADO. 
		new ValidacaoStatusAmb(ZBB_STATUS, ZCA_CODAMB, ZCA_ITEM, ZCA_PARAM, ZCA_TIPAMB);
		
		if(bancoFoundation.isEmpty()) {
			System.out.println("Nao foi possivel obter o nome do Banco do Foundation. Por favor, verifique o cadastro do 3C. Obs: Pelo menos um banco deve estar com o checkbox \"Monit login\" marcado, em Datasul > Bancos > Bancos.");
		}
		
		
		monitSrv = rpwLegado.getIp(); // IP do servidor de monitoramento = IP cadastrado em Datasul > Atalhos > Atalho
		dirProwin32 = rpwLegado.getDirProwin32();
		dirArquivoPf = rpwLegado.getDirArquivoPf();
		dirArquivoIni = rpwLegado.getDirArquivoIni();
		
		if(dirProwin32.isEmpty() || dirArquivoPf.isEmpty() || dirArquivoIni.isEmpty()) {
			System.out.println("Uma ou mais informacoes nao localizada(s) em Datasul > Atalhos > Atalho (\"Dir. Progress\", \"PFs\", \"INI\"). Verifique o cadastro do 3C.");
			System.exit(1);
		}
		

		StringBuilder rmiStrConnection = new StringBuilder();
		
	    try
	    {
	    
	    	rmiStrConnection.append("rmi://");
	    	rmiStrConnection.append(monitSrv);
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
	    
        AvailabilityParamsRPWEMS paramsRPWEMS = new AvailabilityParamsRPWEMS();
        
        paramsRPWEMS.setDlcPath(dirProwin32);
        paramsRPWEMS.setPfPath(dirArquivoPf);
        paramsRPWEMS.setIniPath(dirArquivoIni);
        paramsRPWEMS.setFndDbName(bancoFoundation);
        
        try
        {
        	List<AvailabilityRPWItem> avalRPWList = this.cloudService.availabilityRPWEMS(paramsRPWEMS);	
          
        	ZCA_MEMO = "RPW EMS: ";
        	
        	for (AvailabilityRPWItem item : avalRPWList)
        	{
		   		if(item.getState().trim().toUpperCase().equals("INATIVO"))
		   			ZCA_RESULT = 0;
		   		
		   		ZCA_MEMO += item.getServerDesc() + " = " + item.getState() + "; ";
		   		
            	if (item.getState().trim().toUpperCase().equals("ATIVO")) {
            		int index = ZCA_MEMO.lastIndexOf(";");
            		ZCA_MEMO = ZCA_MEMO.substring(0, index);
            		ZCA_MEMO += ", ";
	            	String execution = item.getExecution().booleanValue() ? "Sim" : "Nao";            		
            		ZCA_MEMO += "Executando = " + execution + ". ";
            	}

        	}
        }
        catch (RemoteException e)
        {
	    	System.out.println(e.getMessage());
    		System.exit(1);
        }
        
        if(ZCA_MEMO.equals("RPW EMS: ")) {
        	System.out.println("Retorno vazio, ao executar o metodo \"availabilityRPWEMS\". Verifique se o RPW encontra-se ativo no ambiente.");
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
