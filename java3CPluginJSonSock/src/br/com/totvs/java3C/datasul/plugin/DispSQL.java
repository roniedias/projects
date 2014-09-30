package br.com.totvs.java3C.datasul.plugin;
 
// Para esta classe, o IP do processo de monitoramento (servidor de monitoramento) está sendo obtido 
// a partir da primeira linha de Datasul > Bancos > Bancos > I.P. Host


import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.List;

import com.totvs.cloud.message.items.AvailabilityDatabaseItem;
import com.totvs.cloud.message.params.AvailabilityParamsSQL;
import com.totvs.cloud.service.RMICloudService;

import br.com.totvs.java3C.JSonParser.write.ParserWrite;
import br.com.totvs.java3C.bean.ItemAmbiente;
import br.com.totvs.java3C.dao.Dao;
import br.com.totvs.java3C.datasul.bean.Banco;
import br.com.totvs.java3C.util.ValidacaoStatusAmb;

public class DispSQL {

	private RMICloudService cloudService;
	private ArrayList<Banco> bancos;
	private ArrayList<ItemAmbiente> itensAmbiente;
	
	private String ZBB_STATUS;
	private String ZCA_PARAM;
	private String ZCA_CODAMB;
	private String ZCA_TIPAMB;
	private String ZCA_ITEM;
	private float ZCA_RESULT = 100;
	private String ZCA_MEMO;
	private String portaMonit;
	
	// TODOS os bancos devem estar disponíveis. O retorno da disponibilidade será 0%, caso haja um banco sequer indisponível
	public DispSQL(String codAmbiente, String codTipoAmbiente, String codMonitoramento, String codProduto) {
		
		this.ZCA_PARAM = codMonitoramento; 
		this.ZCA_CODAMB = codAmbiente;
		this.ZCA_TIPAMB = codTipoAmbiente;
		
		Dao dao = new Dao();
		itensAmbiente = dao.getItensAmbiente(codAmbiente, codTipoAmbiente);
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

		if(bancos.size() == 0) {
			System.out.println("Informacao(oes) de banco(s) de dado(s) nao cadastrada(s) no 3C. Verifique o cadastro.");
			System.exit(1);
		}
		

		StringBuilder rmiStrConnection = new StringBuilder();
		
	    try
	    {
	    	rmiStrConnection.append("rmi://");
	    	rmiStrConnection.append(bancos.get(0).getIp().trim()); // Pressupõe-se o serviço de monitoramento estar executando no servidor do banco
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
		
	    ZCA_MEMO = "Disponibilidade SQL. Bancos: ";
		
    	String[] dbConnStrs = new String[bancos.size()];
    	
    	for(int b = 0; b < bancos.size(); b++) {
    		dbConnStrs[b] = bancos.get(b).getUrlConexao().trim(); // URL Conexao (ex: jdbc:datadirect:openedge://172.18.107.31:23600;databaseName=ems2adt, para o Item DATASUL 11)
    	}
    	
        List<AvailabilityDatabaseItem> returnValue = null;
        List<AvailabilityParamsSQL> availabilityParamsSQLs = new ArrayList<AvailabilityParamsSQL>();	        
        AvailabilityParamsSQL availabilityParamsSQL = null;

        for (int i = 0; i < dbConnStrs.length; i++) {
        	
        	availabilityParamsSQL = new AvailabilityParamsSQL();
        	
            availabilityParamsSQL.setDatabaseType(bancos.get(i).getTipoBanco().trim()); // Tipo Banco (Open Edge, para o Item DATASUL 11)
            availabilityParamsSQL.setDbName(bancos.get(i).getNomeFisico().trim()); // Nome Fisico (Ex: ems2adt, ems2emp, finance, etc., para o Item DATASUL 11)
            availabilityParamsSQL.setUserName(bancos.get(i).getUsuario().trim()); // Posicionar em um campo de Datasul > Bancos > Bancos (ex: ems2adt) > Broker > campo Usuario (pub, para o Item DATASUL 11)  
            availabilityParamsSQL.setPassWord(bancos.get(i).getSenha().trim()); // Posicionar em um campo de Datasul > Bancos > Bancos (ex: ems2adt) > Broker > campo Senha (pub, para o Item DATASUL 11)
            availabilityParamsSQL.setUrlConnection(dbConnStrs[i]);
            
            availabilityParamsSQLs.add(availabilityParamsSQL);
        }
        
        try
        {
        	returnValue = this.cloudService.availabilitySQL(availabilityParamsSQLs);
            
        	for (AvailabilityDatabaseItem item : returnValue)
            {   
   				ZCA_MEMO += item.getItemName() + " = " + item.getStatus() + "; ";        		         		
            }
        }
        catch (RemoteException e)
        {
	    	System.out.println(e.getMessage());
    		System.exit(1);
        }
        
        if(ZCA_MEMO.contains("Inativo") || ZCA_MEMO.contains("null")) // Feito desta maneira para evitar exception   
        	ZCA_RESULT = 0;											  // do tipo "Null Pointer", pois há bancos que podem 
																	  // ter status de retorno = null
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
