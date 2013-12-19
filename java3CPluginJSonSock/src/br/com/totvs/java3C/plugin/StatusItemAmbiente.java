package br.com.totvs.java3C.plugin;

//import java.math.BigInteger;
import java.util.ArrayList;

import br.com.totvs.java3C.JSonParser.read.ParserRead;
//import br.com.totvs.java3C.JSonParser.write.ParserWrite;
import br.com.totvs.java3C.JSonParser.write.ParserWriteStatusItemAmb;
import br.com.totvs.java3C.bean.ServicosItemTipoAmb;
//import br.com.totvs.java3C.util.GrvMonitoramento;
import br.com.totvs.java3C.util.ValidacaoStatusAmb;



/*
 * Classe criada para que o 3C tenha a informação de quantas vezes o nagios efetuou checagens 
 * em um determinado período de tempo, com objetivo de controlar o tempo em que um ambiente 
 * ficou com status suspenso, em manutenção, etc). Para isso, se faz necessária a chamada ao 
 * método de gravação de dados no 3C (grvmonitoramento) sem passar nenhuma informação. Todo 
 * o tratamento é feito pelo próprio 3C
 */

public class StatusItemAmbiente {
	
	private String ZCA_CODAMB;
	private String ZCA_ITEM;
	private String ZCA_PARAM;	
	private String ZCA_TIPAMB;
	
	private String[] zbhAllTipSrvs;
	private ArrayList<ServicosItemTipoAmb> servicosItemTipoAmb = new ArrayList<ServicosItemTipoAmb>();


	
	public StatusItemAmbiente(String codAmbiente, String codTipoAmbiente, String codMonitoramento, String codProduto) {
				

		this.ZCA_PARAM = codMonitoramento; 
		this.ZCA_CODAMB = codAmbiente;
		this.ZCA_TIPAMB = codTipoAmbiente;
		
		ParserRead parserRead = new ParserRead("GENERIC_READ", codAmbiente, codTipoAmbiente, codProduto);
		
		if(parserRead.getZbbStatus().replaceAll("\\s", "").equals("-")) {
			System.out.println("Retorno JSON (leitura) invalido. Verifique o cadastro dos dados do ambiente no Nagios/3C");
			System.exit(3);
		}

		
		zbhAllTipSrvs = parserRead.getZbhTipSrvs();
		
		servicosItemTipoAmb = parserRead.getServicosItemTipoAmb(zbhAllTipSrvs[0]);
		
		this.ZCA_ITEM = servicosItemTipoAmb.get(0).getZbcItem();
		
		// Validação se o ambiente encontra-se com status ATIVO, EM MANUTENÇÃO, CADASTRO, SUSPENSO ou DESATIVADO. 
		// Ocorre neste ponto pela necessidade de passar-se para ValidacaoStatusAmb também o valor de ZCA_ITEM
		new ValidacaoStatusAmb(parserRead.getZbbStatus().replaceAll("\\s", ""), ZCA_CODAMB, ZCA_ITEM, ZCA_PARAM, ZCA_TIPAMB);

		
		
	   	ParserWriteStatusItemAmb parserWrite = new ParserWriteStatusItemAmb("STATUSITEMAMB_WRITE", ZCA_CODAMB, ZCA_TIPAMB, ZCA_ITEM, ZCA_PARAM);
	   		   	 
	   	
	   	if(parserWrite.getStatus().equals("0")) { // Se correu tudo bem com a gravação
	   		System.out.println("Resultado: " + parserWrite.getZcaResult() + ".");
	   		System.exit(Integer.parseInt(parserWrite.getLimiar()));
	   	}
	   	else {
	   		System.out.println(parserWrite.getMessage());
	   		System.exit(Integer.parseInt(parserWrite.getLimiar()));
	   	}

	
		
	}

}
