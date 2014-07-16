package br.com.totvs.java3C.datasul.plugin;

import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.util.ArrayList;

import com.totvs.cloud.message.items.AvailabilityJbossItem;
import com.totvs.cloud.message.params.AvailabilityParamsJboss;
import com.totvs.cloud.service.RMICloudService;

import br.com.totvs.java3C.JSonParser.write.ParserWrite;
import br.com.totvs.java3C.bean.ItemAmbiente;
import br.com.totvs.java3C.bean.datasul.JBossInfo;
import br.com.totvs.java3C.dao.Dao;
import br.com.totvs.java3C.util.ValidacaoStatusAmb;

public class DispJBoss {
	
	private RMICloudService cloudService;
	private ArrayList<ItemAmbiente> itensAmbiente;
	private ArrayList<JBossInfo> jBossInfo;
	private String ipSrvMonit;
	private String portaJBoss;
	private String protocoloJBoss;
	private String nomeHostJBoss;
	private String ZBB_STATUS;
	private String ZCA_PARAM;
	private String ZCA_CODAMB;
	private String ZCA_TIPAMB;
	private String ZCA_ITEM;
	private float ZCA_RESULT = 0;
	private String ZCA_MEMO;

	
	
	// Monitorar todas as linhas, e todas as informações serão obtidas a partir de Datasul > Web > JBoss. 
	// Inclusive o IP do servidor de monitoramento, que será o campo I.P. Host
	
	// Considerar que, se houver pelo menos um servidor jboss Ativo no ambiente, 
	// esse monitoramento retornará 100%

	public DispJBoss(String codAmbiente, String codTipoAmbiente, String codMonitoramento, String codProduto) {

		this.ZCA_PARAM = codMonitoramento; 
		this.ZCA_CODAMB = codAmbiente;
		this.ZCA_TIPAMB = codTipoAmbiente;
		
		
		Dao dao = new Dao();
		itensAmbiente = dao.getItensAmbiente(codAmbiente, codTipoAmbiente);
		jBossInfo = dao.getJBossInfo(codAmbiente, codTipoAmbiente, codProduto);
		dao.closeConnection();
		
		for(ItemAmbiente i : itensAmbiente) {
			if(i.getCodProduto().trim().equals(codProduto)) {
				ZCA_ITEM = i.getCodItem().trim();
				ZBB_STATUS = i.getStatus().trim();
			}
		}
		

		// Validação se o ambiente encontra-se com status ATIVO, EM MANUTENÇÃO, CADASTRO, SUSPENSO ou DESATIVADO. 
		new ValidacaoStatusAmb(ZBB_STATUS, ZCA_CODAMB, ZCA_ITEM, ZCA_PARAM, ZCA_TIPAMB);
		
		// Verificando se algum JBoss foi cadastrado no 3C
		if(jBossInfo.size() == 0) {
			System.out.println("Informacoes do JBoss nao cadastradas. Verifique o cadastro do 3C, em Datasul > Web > Jboss.");
			System.exit(1);
		}
		else {
			for(JBossInfo j : jBossInfo) {
				
				ipSrvMonit = j.getIpHost().trim();
				portaJBoss = j.getPorta().trim();
				protocoloJBoss = j.getProtocolo().trim();
				nomeHostJBoss = j.getNomeHost().trim();
				
		
				StringBuilder rmiStrConnection = new StringBuilder();
				
			    try
			    {
			    	rmiStrConnection.append("rmi://");
			    	rmiStrConnection.append(ipSrvMonit);
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
			    
			    
		        AvailabilityParamsJboss paramsJboss = new AvailabilityParamsJboss();
		        paramsJboss.setProtocol(protocoloJBoss); // Protocolo (HTTP, para o Item DATASUL 11)
		        paramsJboss.setHost(nomeHostJBoss); // Nm. Host (JCHIBMA685, para o Item DATASUL 11
		        paramsJboss.setPort(portaJBoss); // URL Port (8080, para o Item DATASUL 11)
		        
		        try
		        {	
		        	AvailabilityJbossItem jbossItem = this.cloudService.availabilityJboss(paramsJboss);
		        	
		        	ZCA_MEMO = "Servidores JBOSS: ";
		        	
		        	if(jbossItem.getStatus().trim().toUpperCase().equals("ATIVO"))
		        		ZCA_RESULT = 100;
		        	
		        	ZCA_MEMO += jbossItem.getHost() + " = " + jbossItem.getStatus() + "; "; 		        	
		        }
		        catch (RemoteException e)
		        {
			    	System.out.println(e.getMessage());
		    		System.exit(1);
		        }
			    			       
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
