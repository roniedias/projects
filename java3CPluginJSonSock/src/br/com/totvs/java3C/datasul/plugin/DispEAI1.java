package br.com.totvs.java3C.datasul.plugin;

// Verificar qual campo irá corresponder ao IP do processo de monitoramento. 
// Para esta classe, esta informação está sendo obtida através do campo Datasul > Atalhos > Atalho > I.P. Host
// Verificar se não deve-se obter o IP do AppServer

import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.util.ArrayList;

import br.com.totvs.java3C.JSonParser.write.ParserWrite;
import br.com.totvs.java3C.bean.ItemAmbiente;
import br.com.totvs.java3C.bean.datasul.Eai1Info;
import br.com.totvs.java3C.dao.Dao;
import br.com.totvs.java3C.util.ValidacaoStatusAmb;

import com.totvs.cloud.message.items.AvailabilityEAI1Item;
import com.totvs.cloud.message.params.AvailabilityParamsEAI1ProgressAgent;
import com.totvs.cloud.service.RMICloudService;

public class DispEAI1 {

	private String ZBB_STATUS;
	private String ZCA_PARAM;
	private String ZCA_CODAMB;
	private String ZCA_TIPAMB;
	private String ZCA_ITEM;
	private float ZCA_RESULT;
	private String ZCA_MEMO;
	
	private RMICloudService cloudService;
	private ArrayList<ItemAmbiente> itensAmbiente;

	private Eai1Info eai1Info;
	
	private String ip;
	private String dirProwin32;
	private String dirArquivoPf;
	private String dirArquivoIni;
	private String bancoFoundation;
	private String portaMonit;
	
	
	
	public DispEAI1(String codAmbiente, String codTipoAmbiente, String codMonitoramento, String codProduto) {

		this.ZCA_PARAM = codMonitoramento; 
		this.ZCA_CODAMB = codAmbiente;
		this.ZCA_TIPAMB = codTipoAmbiente;

	
		Dao dao = new Dao();
		itensAmbiente = dao.getItensAmbiente(codAmbiente, codTipoAmbiente);
		eai1Info = dao.getEai1Info(codAmbiente, codTipoAmbiente, codProduto);
		bancoFoundation = dao.getBancoFoundation(codAmbiente, codTipoAmbiente, codProduto);
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
		
		ip = eai1Info.getIp();
		dirProwin32 = eai1Info.getDirProwin32();
		dirArquivoPf = eai1Info.getDirArquivoPf();
		dirArquivoIni = eai1Info.getDirArquivoIni();
		
		if(dirProwin32.isEmpty() || dirArquivoPf.isEmpty() || dirArquivoIni.isEmpty()) {
	    	System.out.println("Uma ou mais informacoes nao localizada(s) em Datasul > Integrator/EAI > EAI1 (\"I.P. Host\", \"Dir.Progress\", \"INI\", \"PF\"). Verifique o cadastro do 3C.");
			System.exit(1);
		}
		
		if(bancoFoundation.isEmpty()) {
			System.out.println("Nao foi possivel obter o nome do Banco do Foundation. Por favor, verifique o cadastro do 3C. Obs: Pelo menos um banco deve estar com o checkbox \"Monit login\" marcado, em Datasul > Bancos > Bancos.");
		}
		 		

		StringBuilder rmiStrConnection = new StringBuilder();
		
	    try
	    {
	    	
	    	rmiStrConnection.append("rmi://");
	    	rmiStrConnection.append(ip);
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
	    
	    AvailabilityParamsEAI1ProgressAgent paramsEAI1 = new AvailabilityParamsEAI1ProgressAgent();
	    paramsEAI1.setDlcPath(dirProwin32);
	    paramsEAI1.setPfPath(dirArquivoPf);
	    paramsEAI1.setIniPath(dirArquivoIni);
	    paramsEAI1.setEaiDbName(bancoFoundation);
	    
	    AvailabilityEAI1Item eai1Item = null;
	    try
	    {
	      eai1Item = this.cloudService.availabilityEAI1ByProgressAgent(paramsEAI1);

	      ZCA_MEMO = "Status Servidor EAI1: " + eai1Item.getEai1Status();
          
          if(eai1Item.getEai1Status().trim().toUpperCase().equals("ATIVO")) 
          	ZCA_RESULT = 100;
          else 
          	ZCA_RESULT = 0;
	      
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
