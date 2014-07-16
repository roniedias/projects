package br.com.totvs.java3C.datasul.plugin;

import java.util.ArrayList;

import br.com.totvs.java3C.JSonParser.write.ParserWrite;
import br.com.totvs.java3C.bean.ItemAmbiente;
import br.com.totvs.java3C.dao.Dao;
import br.com.totvs.java3C.datasul.util.DispDatasulDatabasesNaoGrava;
import br.com.totvs.java3C.datasul.util.DispJBossNaoGrava;
import br.com.totvs.java3C.datasul.util.DispLoginDatasulNaoGrava;
import br.com.totvs.java3C.util.ValidacaoStatusAmb;


/**
* @author Ronie Dias Pinto
*/

// Serão executados três métodos de monitoramento desenvolvidos pelo frame do produto:
// availabilityDatasulDatabases, availabilityJboss e availabilityLoginDatasul  
public class DispDatasul {
	
	private ArrayList<ItemAmbiente> itensAmbiente;
	private String ZBB_STATUS;
	private String ZCA_PARAM;
	private String ZCA_CODAMB;
	private String ZCA_TIPAMB;
	private String ZCA_ITEM;
	private float ZCA_RESULT;
	private String ZCA_MEMO;
	
	private String dddZcaMemo;
	private float dddZcaResult;
	private String djbZcaMemo;
	private float djbZcaResult;
	private String dldZcaMemo;
	private float dldZcaResult;
	
	
	public DispDatasul(String codAmbiente, String codTipoAmbiente, String codMonitoramento, String codProduto) {
		
		this.ZCA_PARAM = codMonitoramento; 
		this.ZCA_CODAMB = codAmbiente;
		this.ZCA_TIPAMB = codTipoAmbiente;
		
		Dao dao = new Dao();
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
		
		DispDatasulDatabasesNaoGrava ddd = new DispDatasulDatabasesNaoGrava(codAmbiente, codTipoAmbiente, codMonitoramento, codProduto);
		dddZcaMemo = ddd.getZCA_MEMO();
		dddZcaResult = ddd.getZCA_RESULT();
		
		DispJBossNaoGrava djb = new DispJBossNaoGrava(codAmbiente, codTipoAmbiente, codMonitoramento, codProduto);
		djbZcaMemo = djb.getZCA_MEMO();
		djbZcaResult = djb.getZCA_RESULT();

		DispLoginDatasulNaoGrava dld = new DispLoginDatasulNaoGrava(codAmbiente, codTipoAmbiente, codMonitoramento, codProduto);
		dldZcaMemo = dld.getZCA_MEMO();
		dldZcaResult = dld.getZCA_RESULT();
		
		ZCA_MEMO = dddZcaMemo + djbZcaMemo + dldZcaMemo;
		

		if(dddZcaResult == 0 || djbZcaResult == 0 || dldZcaResult == 0)
			ZCA_RESULT = 0;
		else 
			ZCA_RESULT = 100;
		
		
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
