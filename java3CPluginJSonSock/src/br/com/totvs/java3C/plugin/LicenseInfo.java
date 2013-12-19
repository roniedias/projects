package br.com.totvs.java3C.plugin;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;


import br.com.totvs.java3C.JSonParser.monit.LicenseInfoParserMon;
import br.com.totvs.java3C.JSonParser.read.ParserRead;
import br.com.totvs.java3C.JSonParser.write.ParserWrite;
import br.com.totvs.java3C.bean.ServicosItemTipoAmb;
import br.com.totvs.java3C.util.PathConverter;
import br.com.totvs.java3C.util.ValidacaoStatusAmb;



/**
 * @author Ronie Dias Pinto
 */


public class LicenseInfo {
	
	private String ZCA_PARAM;
	private String ZCA_CODAMB;
	private String ZCA_TIPAMB;
	private String ZCA_ITEM;
	private float ZCA_RESULT;
	private String ZCA_MEMO;
//	private String ZCA_OBS = "SEM INFORMACAO";
//	private String ZCA_USER = ""; 
	
	private String tipoServico = "13"; // License
	
	private Date expirationDate;
	private Date currentDate = new Date();
	
	
//	private String ZCA_STMONI = "E"; // (A)berto ou (E)ncerrado
//	private String ZCA_DATLAT = ""; // Campos obrigatórios por causa de grvMonitoramento.
//	private String ZCA_HORLAT = ""; // Úteis apenas para Latency
	
	private LicenseInfoParserMon licenseInfoParser;
	private String [] licenceInfoParserReturn;
	
	private ArrayList<ServicosItemTipoAmb> servicosItemTipoAmb = new ArrayList<ServicosItemTipoAmb>();


	
	
	public LicenseInfo(String codAmbiente, String codTipoAmbiente, String codMonitoramento, String codProduto) {
		
		
		this.ZCA_PARAM = codMonitoramento; 
		this.ZCA_CODAMB = codAmbiente;
		this.ZCA_TIPAMB = codTipoAmbiente;

		
		ParserRead parserRead = new ParserRead("GENERIC_READ", codAmbiente, codTipoAmbiente, codProduto);
		
		
		if(parserRead.getZbbStatus().replaceAll("\\s", "").equals("-")) {
			System.out.println("Retorno JSON (leitura) invalido. Verifique o cadastro dos dados do ambiente no Nagios/3C");
			System.exit(3);
		}

		
		servicosItemTipoAmb = parserRead.getServicosItemTipoAmb(tipoServico);
		
		this.ZCA_ITEM = servicosItemTipoAmb.get(0).getZbcItem();
		
		// Validação se o ambiente encontra-se com status ATIVO, EM MANUTENÇÃO, CADASTRO, SUSPENSO ou DESATIVADO. 
		// Ocorre neste ponto pela necessidade de passar-se para ValidacaoStatusAmb também o valor de ZCA_ITEM
		new ValidacaoStatusAmb(parserRead.getZbbStatus().replaceAll("\\s", ""), ZCA_CODAMB, ZCA_ITEM, ZCA_PARAM, ZCA_TIPAMB);

		
	
		
		if(servicosItemTipoAmb.size() == 0) {
			System.out.println("Nenhum servico localizado, para o tipo License. Verifique o cadastro no 3C.");
			System.exit(3);
		}
		

		
		// A chamada à função de monitoramento LICENSEINFO (Emerson) será feita utilizando informações obtidas a partir do primeiro 
		// SERVICO localizado abaixo do TIPO DE SERVICO "LICENSE" 
					


			String str = new PathConverter(servicosItemTipoAmb.get(0).getZbcIni(), servicosItemTipoAmb.get(0).getZbcIpHost()).convertToRelative();	
			String path = str.replaceAll("\\s", "");
			//path = new PathConverter().duplicateBackSlash(path);
			
			licenseInfoParser = new LicenseInfoParserMon(servicosItemTipoAmb.get(0).getZbcIpHost(), servicosItemTipoAmb.get(0).getZbcPorta(), servicosItemTipoAmb.get(0).getZbcEnviro(), path);
			this.licenceInfoParserReturn = licenseInfoParser.getReturnArray();       // licenceInfoParserReturn[0] = DATE
																		             // licenceInfoParserReturn[1] = STATUS
																		             // licenceInfoParserReturn[2] = MESSAGE
        															                 // licenceInfoParserReturn[3] = ELAPTIME
        	
			
//				System.out.println("IP: " + servicosItemTipoAmb.get(0).getZbcIpHost());
//				System.out.println("PORTA: " + servicosItemTipoAmb.get(0).getZbcPorta()); 
//				System.out.println("ENVIRONMENT: " + servicosItemTipoAmb.get(0).getZbcEnviro());
//				System.out.println("PATH: " + path);
			

			
		
		
		if(licenceInfoParserReturn[1].equals("1")) { // Se um erro ocorrer
			System.out.println(licenceInfoParserReturn[2]);
			System.exit(3);
		}
		
							
		DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
				
		try {
			expirationDate = dateFormat.parse(licenceInfoParserReturn[0]);

		} catch (ParseException e) {
			e.printStackTrace();
		}
		
					
		long DAY_IN_MILLIS = 1000 * 60 * 60 * 24;
		this.ZCA_RESULT = (float) ((expirationDate.getTime() - currentDate.getTime()) / DAY_IN_MILLIS );
		this.ZCA_MEMO = licenceInfoParserReturn[2] + ".";

		

		
//		System.out.println("LATENCIA: " + LATENCIA);
//		System.out.println("QTDNFECNPJ: " + QTDNFECNPJ);
//		
//		for(int s = 0; s < SERVICOSARRAY.length; s++) {
//			System.out.println("SERVICOSARRAY[" + s + "].getZCC_OBS(): " + SERVICOSARRAY[s].getZCC_OBS());
//			System.out.println("SERVICOSARRAY[" + s + "].getZCC_SEQSRV(): " + SERVICOSARRAY[s].getZCC_SEQSRV());
//			System.out.println("SERVICOSARRAY[" + s + "].getZCC_STATUS(): " + SERVICOSARRAY[s].getZCC_STATUS());
//			System.out.println("SERVICOSARRAY[" + s + "].getZCC_TIPSRV(): " + SERVICOSARRAY[s].getZCC_TIPSRV());
//		}
//		
//		System.out.println("ZCA_CODAMB: " + ZCA_CODAMB);
//		System.out.println("ZCA_DATLAT: " + ZCA_DATLAT);
//		System.out.println("ZCA_HORLAT: " + ZCA_HORLAT);
//		System.out.println("ZCA_ITEM: " + ZCA_ITEM);
//		System.out.println("ZCA_MEMO: " + ZCA_MEMO);
//		System.out.println("ZCA_OBS: " + ZCA_OBS); 
//		System.out.println("ZCA_PARAM: " + ZCA_PARAM); 
//		System.out.println("ZCA_RESULT: " + ZCA_RESULT); 
//		System.out.println("ZCA_STMONI: " + ZCA_STMONI);
//		System.out.println("ZCA_TIPAMB: " + ZCA_TIPAMB);
//		System.out.println("ZCA_USER: " + ZCA_USER);
		
		
		
	   	ParserWrite parserWrite = new ParserWrite("GENERIC_WRITE", ZCA_CODAMB, ZCA_TIPAMB, ZCA_ITEM, ZCA_PARAM, String.valueOf(ZCA_RESULT), ZCA_MEMO);
	   	
	   	if(parserWrite.getStatus().equals("0")) { // Se correu tudo bem com a gravação
	   		System.out.println("Resultado: " + ZCA_RESULT + ". "+ ZCA_MEMO);
	   		System.exit(Integer.parseInt(parserWrite.getLimiar()));
	   	}
	   	else {
	   		System.out.println(parserWrite.getMessage());
	   		System.exit(Integer.parseInt(parserWrite.getLimiar()));
	   	}

		
		
//		new GrvMonitoramento(LATENCIA, QTDNFECNPJ, SERVICOSARRAY, ZCA_CODAMB, ZCA_DATLAT, ZCA_HORLAT, ZCA_ITEM, ZCA_MEMO, ZCA_OBS, ZCA_PARAM, ZCA_RESULT, ZCA_STMONI, ZCA_TIPAMB, ZCA_USER);
		
		        
	}

}

