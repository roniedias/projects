package br.com.totvs.java3C.datasul.plugin;

import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.util.ArrayList;

import br.com.totvs.java3C.JSonParser.write.ParserWrite;
import br.com.totvs.java3C.bean.ItemAmbiente;
import br.com.totvs.java3C.dao.Dao;
import br.com.totvs.java3C.datasul.bean.AtalhoInfo;
//import br.com.totvs.java3C.datasul.bean.RpwLegado;
import br.com.totvs.java3C.util.ValidacaoStatusAmb;

import com.totvs.cloud.message.items.AvailabilityLoginEMSItem;
import com.totvs.cloud.message.params.AvailabilityParamsLoginEMS;
import com.totvs.cloud.service.RMICloudService;

public class DispLoginEMS {
	
	private String ZBB_STATUS;
	private String ZCA_PARAM;
	private String ZCA_CODAMB;
	private String ZCA_TIPAMB;
	private String ZCA_ITEM;
	private float ZCA_RESULT;
	private String ZCA_MEMO;
	
	private RMICloudService cloudService;
	private ArrayList<ItemAmbiente> itensAmbiente;
	private AtalhoInfo atalhoInfo;
	//private RpwLegado rpwLegado;
	private String bancoFoundation;
	private String usuarioItemAmbiente;
	private String senhaItemAmbiente;
	
	private String dirProwin32;
	private String dirArquivoPf;
	private String dirArquivoIni;
	
	private String monitSrv;
	private String portaMonit;
	
	

	
	
	public DispLoginEMS(String codAmbiente, String codTipoAmbiente, String codMonitoramento, String codProduto) {
	
		this.ZCA_PARAM = codMonitoramento; 
		this.ZCA_CODAMB = codAmbiente;
		this.ZCA_TIPAMB = codTipoAmbiente;

	
		Dao dao = new Dao();
		itensAmbiente = dao.getItensAmbiente(codAmbiente, codTipoAmbiente);
		atalhoInfo = dao.getAtalhoInfo(codAmbiente, codTipoAmbiente, codProduto);
		bancoFoundation = dao.getBancoFoundation(codAmbiente, codTipoAmbiente, codProduto);
		portaMonit = dao.getCliente(codAmbiente).getPortaMonit();
		
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
		
		if(bancoFoundation.isEmpty()) {
			System.out.println("Nao foi possivel obter o nome do Banco do Foundation. Por favor, verifique o cadastro do 3C. Obs: Pelo menos um banco deve estar com o checkbox \"Monit login\" marcado, em Datasul > Bancos > Bancos.");
		}
		
		if(usuarioItemAmbiente.isEmpty() || senhaItemAmbiente.isEmpty()) {
			System.out.println("Usuario e/ou senha invalido(s). Verifique essas informações no cadastro do Item de Ambiente, no 3C.");
			System.exit(1);
		}
		
		monitSrv = atalhoInfo.getIp(); // IP do servidor de monitoramento = IP cadastrado em Datasul > Atalhos > Atalho 
		dirProwin32 = atalhoInfo.getDirProwin32();
		dirArquivoPf = atalhoInfo.getDirArquivoPf();
		dirArquivoIni = atalhoInfo.getDirArquivoIni();
		
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
				
		
	    AvailabilityParamsLoginEMS paramsLoginEMS = new AvailabilityParamsLoginEMS();
	    
	    	    
	    paramsLoginEMS.setDlcPath(dirProwin32);   
	    paramsLoginEMS.setPfPath(dirArquivoPf);   
	    paramsLoginEMS.setIniPath(dirArquivoIni); 
	    	    
	    paramsLoginEMS.setFndDbName(bancoFoundation); 
	     
	    paramsLoginEMS.setUserName(usuarioItemAmbiente); 
	    paramsLoginEMS.setPassWord(senhaItemAmbiente);
	    	    	    	
	    try
	    {
	      AvailabilityLoginEMSItem loginEMSItem = this.cloudService.availabilityLoginEMS(paramsLoginEMS);
	      
	      ZCA_MEMO = "Status Login EMS: " + loginEMSItem.getStatus();
          
          if(loginEMSItem.getStatus().toUpperCase().equals("ATIVO")) 
          	ZCA_RESULT = 100;
          else 
          	ZCA_RESULT = 0;
	      
	    }
	    catch (RemoteException e)
	    {
	    	System.out.println(e.getMessage());
    		System.exit(1);
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
