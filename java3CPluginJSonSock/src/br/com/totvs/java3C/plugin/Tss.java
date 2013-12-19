package br.com.totvs.java3C.plugin;

import java.util.ArrayList;
import java.util.Random;


import br.com.totvs.java3C.JSonParser.monit.TssParserMon;

import br.com.totvs.java3C.JSonParser.read.ParserReadTss;

import br.com.totvs.java3C.JSonParser.write.ParserWriteTss;
import br.com.totvs.java3C.bean.ParamQtdNfeCnpj;
import br.com.totvs.java3C.bean.ZbcTopService;
import br.com.totvs.java3C.util.ValidacaoStatusAmb;

import java.math.BigInteger;



/**
 * @author Ronie Dias Pinto
 */



public class Tss {
	
	
	private ParamQtdNfeCnpj[] QTDNFECNPJ;
	
	private ParamQtdNfeCnpj auxQtdNfeCnpj;

		
	private String ZCA_CODAMB; 
	private String ZCA_TIPAMB; 
	private String ZCA_ITEM; 
	private String ZCA_PARAM;
	private float ZCA_RESULT = 0;  
	private String ZCA_MEMO;

		
	private ArrayList<String> cnpjList = new ArrayList<String>();
	
	private ArrayList<String> auxListZCA_MEMO = new ArrayList<String>();
	
	
	private String topServer;
	private String topPort;
	private String topAlias;
	private String topDatabase;
	 
		

	private TssParserMon tssParser;
	private String [] tssParserReturn;
	
	private ArrayList<ZbcTopService> zbcTopServices = new ArrayList<ZbcTopService>();
	
	private int totalCnpj;

	
		
	public Tss(String codAmbiente, String codTipoAmbiente, String codMonitoramento, String codProduto) {
		
		this.ZCA_PARAM = codMonitoramento; 
		this.ZCA_CODAMB = codAmbiente;
		this.ZCA_TIPAMB = codTipoAmbiente;
		
		
		ParserReadTss parserRead = new ParserReadTss("TSS_READ", codAmbiente, codTipoAmbiente, codProduto);
		
		if(parserRead.getZbbStatus().replaceAll("\\s", "").equals("-")) {
			System.out.println("Retorno JSON (leitura) invalido. Verifique o cadastro dos dados do ambiente no Nagios/3C");
			System.exit(3);
		}

		
		this.ZCA_ITEM = parserRead.getZcaItem();
		
		
		// Validação se o ambiente encontra-se com status ATIVO, EM MANUTENÇÃO, CADASTRO, SUSPENSO ou DESATIVADO. 
		// Ocorre neste ponto pela necessidade de passar-se para ValidacaoStatusAmb também o valor de ZCA_ITEM
		new ValidacaoStatusAmb(parserRead.getZbbStatus().replaceAll("\\s", ""), ZCA_CODAMB, ZCA_ITEM, ZCA_PARAM, ZCA_TIPAMB);

				
		
	    // Primeiro passo: Obter a lista de CNPJ´s autorizados		   	
		cnpjList = parserRead.getZbgCnpj();
		
		if(cnpjList.size() == 1 && cnpjList.get(0).replaceAll("\\s", "").equals("-")) { // Não há cnpj´s cadastrados
			System.out.println("Nao ha CNPJs cadastrados para este cliente. Verifique o cadastro no 3C.");
			System.exit(3);
		}
		
    	    	
	    	
	    	
	    // Terceiro passo: Obter o alias do banco de dados	  
	    
		this.topDatabase = parserRead.getZa6TopNic();
		
		
	    
	    // Quarto passo: Obter os serviços cadastrados para TOPCONNECT 
		// (que se encontram abaixo do TIPO DE SERVICO "TOPCONNECT")
	    		
		
		zbcTopServices = parserRead.getZbcTopServices();
		
		if(zbcTopServices.size() == 1 && zbcTopServices.get(0).getZbcIpHost().replaceAll("\\s", "").equals("-")) { // Não há serviços cadastrados para o tipo TOPCONNECT 
			System.out.println("Nao ha servicos cadastrados para o tipo TOPCONNECT. Verifique o cadastro no 3C.");
			System.exit(3);
		}	   		   	
	   	
	   	
	   	
	    if(zbcTopServices.size() == 1) { 						  // Caso haja apenas um serviço cadastrado, as informações do TOP CONNECT 
	    	this.topServer = zbcTopServices.get(0).getZbcIpHost();  // serão obtidas a partir do primeiro e único SERVICO cadastrado para o  	    	
	    	this.topPort = zbcTopServices.get(0).getZbcPorta();     // TIPO "TOPCONNECT".
	    	this.topAlias = zbcTopServices.get(0).getZbcEnviro();
	    }
	    else {
			Random r = new Random();
			int i = r.nextInt(zbcTopServices.size() - 1) + 1;
																			// Antes, era obtido a partir do  
	    		this.topServer = zbcTopServices.get(i).getZbcIpHost();      // primeiro "serviço slave" as informações do top. 
	    		this.topPort = zbcTopServices.get(i).getZbcPorta();	        // Hoje, essas informações são obtidas randomicamente. 
	    		this.topAlias = zbcTopServices.get(i).getZbcEnviro();       // O primeiro registro, index 0 (que via de regra é o master) 
	    }																	// seguindo esta lógica nunca será selecionado (por causa do "+ 1" no final do trecho: r.nextInt(SERVICOSITEMTIPOAMB.length - 1) + 1)  
	    	    															// A mesma regra vale para o indice 13 que, por causa do "- 1", nunca aparece, evitando referencia a um elemento inexistente no array 

	    // Validando se os parâmetros do TOP foram preenchidos no 3C (validação adicional)
	   	if(topServer.isEmpty() || topPort.isEmpty() || topAlias.isEmpty()) {
	   		System.out.println("TOP Server, TOP Port ou TOP alias vazio(s). Verifique o cadastro no 3C.");
			System.exit(3);
	   	}


	   	
	   // Sexto passo: Efetuar a chamada ao WS do TSS para obter as informações de quantas notas 
	   // foram emitidas por CNPJ. O preenchimento da variável QTDNFECNPJ será efetuado com base no tipo de 
	   // ambiente informado em "codTipoAmbiente". Neste passo também será efetuado o preenchimento da lista 
	   // auxiliar "auxListZCA_MEMO"
		   
		   
        QTDNFECNPJ = new ParamQtdNfeCnpj[cnpjList.size()];
		      
		    
		
	    for(int c = 0; c < cnpjList.size(); c++) {
	    	
//	    	System.out.println("topServer: " + topServer);
//	    	System.out.println("topPort: " + topPort);
//	    	System.out.println("topAlias: " + topAlias);
//	    	System.out.println("topDatabase: " + topDatabase);
//	    	System.out.println("cnpj: " + cnpjList.get(c));
//	    	System.out.println("--------------");
	    	

		    	
		    	
				tssParser = new TssParserMon(topServer, topPort, topAlias, topDatabase, cnpjList.get(c));
				this.tssParserReturn = tssParser.getReturnArray();         // tssParserReturn[0] = CNPJ  
																		   // tssParserReturn[1] = PRODUTO_TESTE 
																		   // tssParserReturn[2] = PRODUTO_PRODUCAO 
  																		   // tssParserReturn[3] = SERVICO_TESTE 
																		   // tssParserReturn[4] = SERVICO_PRODUCAO 
																		   // tssParserReturn[5] = STATUS 
																		   // tssParserReturn[6] = MESSAGE 
																		   // tssParserReturn[7] = ELAPTIME 
		    	
		    	
		    
		    
		    
		    // Antes de executar o processo de envio de informações para o 3C, é preciso verificar se o status do 
		    // retorno da chamada ao WS que faz a contagem de NFE´s foi 1 (não O.K.). Se foi, exibe a mensagem de
		    // retorno 
		    
		    if(tssParserReturn[5].equals("1")) {
		    	
		    	System.out.println(tssParserReturn[6] + " topServer: " + topServer + "; topPort: " + topPort + "; topAlias: " + topAlias + "; topDatabase: " + topDatabase);

		    	
		    	System.exit(1); // warning
		    }
		    
			
		    
		    	
		    
		    int quantCnpjProdutos = Integer.parseInt(tssParserReturn[2]) + Integer.parseInt(tssParserReturn[1]);
		    int quantCnpjServicos = Integer.parseInt(tssParserReturn[4]) + Integer.parseInt(tssParserReturn[3]);
		    int totalCnpj = quantCnpjProdutos + quantCnpjServicos;
		      
	
		    BigInteger qtdCnpjProdutosBigInteger = BigInteger.valueOf(quantCnpjProdutos);
		    BigInteger qtdCnpjServicosBigInteger= BigInteger.valueOf(quantCnpjServicos);
		    BigInteger totalCnpjBigInteger = BigInteger.valueOf(totalCnpj);
		    
		    auxQtdNfeCnpj = new ParamQtdNfeCnpj(qtdCnpjProdutosBigInteger, qtdCnpjServicosBigInteger, totalCnpjBigInteger, tssParserReturn[0]);
		    QTDNFECNPJ[c] = auxQtdNfeCnpj;
		    auxListZCA_MEMO.add(tssParserReturn[0] + ": Prod " + quantCnpjProdutos + "; Serv " + quantCnpjServicos + ". ");
		    
		    this.totalCnpj += totalCnpj;
		    		    			  
	    }
						
					
	    StringBuilder sb = new StringBuilder();  // Procedimento necessário para armazenar as informações   
			   											 // de auxListZCA_MEMO em ZBA_MEMO
	    for (String s : auxListZCA_MEMO) {
	    	sb.append(s);
		}
			  
	    
		ZCA_MEMO = "Resultado: " + String.valueOf(totalCnpj) + ". " + sb.toString();

			 	  
	   // Sétimo passo: Preenchimento dos dados faltantes, que serão enviados para GRVMONITORAMENTO
 		

		ParserWriteTss parserWriteTss = new ParserWriteTss("TSS_WRITE", ZCA_CODAMB, ZCA_TIPAMB, ZCA_ITEM, ZCA_PARAM, String.valueOf(ZCA_RESULT), ZCA_MEMO, QTDNFECNPJ);
		
	   	if(parserWriteTss.getStatus().equals("0")) { // Se correu tudo bem com a gravação
	   		System.out.println(ZCA_MEMO);
	   		System.exit(Integer.parseInt(parserWriteTss.getLimiar()));
	   	}
	   	else {
	   		System.out.println(parserWriteTss.getMessage());
	   		System.exit(Integer.parseInt(parserWriteTss.getLimiar()));
	   	}

			  
	   // Oitavo passo: Gravação dos dados no 3C
		
		
		//new GrvMonitoramentoTss(LATENCIA, QTDNFECNPJ, SERVICOSARRAY, ZCA_CODAMB, ZCA_DATLAT, ZCA_HORLAT, ZCA_ITEM, ZCA_MEMO, ZCA_OBS, ZCA_PARAM, ZCA_RESULT, ZCA_STMONI, ZCA_TIPAMB, ZCA_USER);
						 
	}
		
}