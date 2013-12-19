package br.com.totvs.java3C.plugin;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import br.com.totvs.java3C.JSonParser.monit.SigamatInfoParserMon;
import br.com.totvs.java3C.JSonParser.read.ParserRead;
import br.com.totvs.java3C.JSonParser.write.ParserWrite;
import br.com.totvs.java3C.bean.ServicosItemTipoAmb;
import br.com.totvs.java3C.util.ValidacaoStatusAmb;


/**
 * @author Ronie Dias Pinto
 */


public class Sigamat {
	
	
	private String ZCA_PARAM;
	private String ZCA_CODAMB;
	private String ZCA_TIPAMB;
	private String ZCA_ITEM;
	private String ZCA_MEMO;
	private float ZCA_RESULT;
//	private String ZCA_OBS = "SEM INFORMACAO";
//	private String ZCA_STMONI = "E"; // (A)berto ou (E)ncerrado
//	private String ZCA_USER = "";
	
	private String tipoServico; // Tipo de serviço ERP (10 ou 29)
	
		
//	private String ZCA_DATLAT = ""; // Campos obrigatórios por causa de grvMonitoramento.
//	private String ZCA_HORLAT = ""; // Úteis apenas para Latency
	
	private Date expirationDate;
	private Date currentDate = new Date();
	

	private SigamatInfoParserMon sigamatInfoParser;
	private String [] sigamatParserReturn;
	
	private String[] zbhAllTipSrvs;
	private ArrayList<ServicosItemTipoAmb> servicosItemTipoAmb = new ArrayList<ServicosItemTipoAmb>();

	
	
	public Sigamat(String codAmbiente, String codTipoAmbiente, String codMonitoramento, String codProduto) {
	
		
		this.ZCA_PARAM = codMonitoramento; 
		this.ZCA_CODAMB = codAmbiente;
		this.ZCA_TIPAMB = codTipoAmbiente;
		
		ParserRead parserRead = new ParserRead("GENERIC_READ", codAmbiente, codTipoAmbiente, codProduto);
		
		
		if(parserRead.getZbbStatus().replaceAll("\\s", "").equals("-")) {
			System.out.println("Retorno JSON (leitura) invalido. Verifique o cadastro dos dados do ambiente no Nagios/3C");
			System.exit(3);
		}

		
		
		zbhAllTipSrvs = parserRead.getZbhTipSrvs();
		
		
		// Procedimento para atribuir valor à variável "tipoServico"		
		for(int z = 0; z < zbhAllTipSrvs.length; z++) {
			if(zbhAllTipSrvs[z].equals("10") || zbhAllTipSrvs[z].equals("29")) {
				tipoServico = zbhAllTipSrvs[z]; 
			}
		}
		
			
		
		servicosItemTipoAmb = parserRead.getServicosItemTipoAmb(tipoServico);

		this.ZCA_ITEM = servicosItemTipoAmb.get(0).getZbcItem();
		
		
		// Validação se o ambiente encontra-se com status ATIVO, EM MANUTENÇÃO, CADASTRO, SUSPENSO ou DESATIVADO. 
		// Ocorre neste ponto pela necessidade de passar-se para ValidacaoStatusAmb também o valor de ZCA_ITEM
		new ValidacaoStatusAmb(parserRead.getZbbStatus().replaceAll("\\s", ""), ZCA_CODAMB, ZCA_ITEM, ZCA_PARAM, ZCA_TIPAMB);

		
		
		
		if(servicosItemTipoAmb.size() == 0) {
			System.out.println("Nenhum servico localizado. Verifique o cadastro no 3C.");
			System.exit(3);
		}
		

		
		// A chamada ao WS SIGAMAT (Emerson) será feita utilizando informações obtidas a partir do primeiro 
		// SERVICO localizado abaixo do TIPO DE SERVICO "ERP", Não importando se é um MASTER ou SLAVE 
		
						
			sigamatInfoParser = new SigamatInfoParserMon(servicosItemTipoAmb.get(0).getZbcIpHost(), servicosItemTipoAmb.get(0).getZbcPorta(), servicosItemTipoAmb.get(0).getZbcEnviro());
			this.sigamatParserReturn = sigamatInfoParser.getReturnArray();   // sigamatParserReturn[0] = DATE 
																	         // sigamatParserReturn[1] = STATUS
																			 // sigamatParserReturn[2] = MESSAGE
																	         // sigamatParserReturn[3] = ELAPTIME
						
			
//			System.out.println("IP: " + servicosItemTipoAmb.get(0).getZbcIpHost());
//			System.out.println("PORTA: " + servicosItemTipoAmb.get(0).getZbcPorta());
//			System.out.println("ENVIRONMENT: " + servicosItemTipoAmb.get(0).getZbcEnviro());
			
			
		
		if(sigamatParserReturn[1].equals("1")) { // Se um erro ocorrer
			System.out.println(sigamatParserReturn[2]);
			System.exit(3);
		}

		
		// Converter a data de expiração na quantidade de dias até a ela, a partir da data atual
		
		DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
		
		try {
			expirationDate = dateFormat.parse(sigamatParserReturn[0]);

		} catch (ParseException e) {
			e.printStackTrace();
		}

		long DAY_IN_MILLIS = 1000 * 60 * 60 * 24;
		this.ZCA_RESULT = (float) ((expirationDate.getTime() - currentDate.getTime()) / DAY_IN_MILLIS );
		//this.ZCA_MEMO = WS3CMonitorReturn.getSIGAMATINFO().getMESSAGE() + ". Data de expiracao: " + WS3CMonitorReturn.getSIGAMATINFO().getDATE() + ".";
		this.ZCA_MEMO = sigamatParserReturn[2] + ".";

			
		

		ParserWrite parserWrite = new ParserWrite("GENERIC_WRITE", ZCA_CODAMB, ZCA_TIPAMB, ZCA_ITEM, ZCA_PARAM, String.valueOf(ZCA_RESULT), ZCA_MEMO);
	   	
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
