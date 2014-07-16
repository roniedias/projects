package br.com.totvs.java3C.plugin.bdconn;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;



import br.com.totvs.java3C.JSonParser.monit.SigamatInfoParserMon;
//import br.com.totvs.java3C.JSonParser.read.ParserRead;
import br.com.totvs.java3C.JSonParser.write.ParserWrite;
import br.com.totvs.java3C.bean.AmbienteFull;
import br.com.totvs.java3C.bean.Servico;
//import br.com.totvs.java3C.bean.ServicosItemTipoAmb; // Eliminar futuramente esse bean
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

	private String tipoServico; // Tipo de serviço ERP (10 ou 29)
			
	private Date expirationDate;
	private Date currentDate = new Date();
	

	private SigamatInfoParserMon sigamatInfoParser;
	private String [] sigamatParserReturn;
	
	// private String[] zbhAllTipSrvs;
	private String[] codTodosTiposServicos;
	
	
	// Obs: BEAN ServicosItemTipoAmb deverá ser deprecated
	//private ArrayList<ServicosItemTipoAmb> servicosItemTipoAmb = new ArrayList<ServicosItemTipoAmb>();
	private ArrayList<Servico> servicosItemTipoAmb = new ArrayList<Servico>();
	private ArrayList<Servico> servicos = new ArrayList<Servico>();
	
	private String ZBB_STATUS;
	

	
	
	public Sigamat(String codAmbiente, String codTipoAmbiente, String codMonitoramento, String codProduto) {
	
		
		this.ZCA_PARAM = codMonitoramento; 
		this.ZCA_CODAMB = codAmbiente;
		this.ZCA_TIPAMB = codTipoAmbiente;
		
		
		
//		ParserRead parserRead = new ParserRead("GENERIC_READ", codAmbiente, codTipoAmbiente, codProduto);
//		
//		if(parserRead.getZbbStatus().replaceAll("\\s", "").equals("-")) {
//			System.out.println("Retorno JSON (leitura) invalido. Verifique o cadastro dos dados do ambiente no Nagios/3C");
//			System.exit(3);
//		}
		
		//zbhAllTipSrvs = parserRead.getZbhTipSrvs();
		
		AmbienteFull aFull = new AmbienteFull(codAmbiente, codTipoAmbiente, codProduto);

		codTodosTiposServicos = new String[aFull.getTiposServico().size()];
		
		for(int a = 0; a < aFull.getTiposServico().size(); a++) {
			codTodosTiposServicos[a] = aFull.getTiposServico().get(a).getCodigo();
		}
		
				
		// Procedimento para atribuir valor à variável "tipoServico"		
		for(int z = 0; z < codTodosTiposServicos.length; z++) {
			if(codTodosTiposServicos[z].equals("10") || codTodosTiposServicos[z].equals("29")) {
				tipoServico = codTodosTiposServicos[z]; 
			}
		}
		
		

		//servicosItemTipoAmb = parserRead.getServicosItemTipoAmb(tipoServico);
		
		servicosItemTipoAmb = aFull.getServicos();
		for(int s = 0; s < servicosItemTipoAmb.size(); s++) {
			if(servicosItemTipoAmb.get(s).getCodTipoServico().equals(tipoServico)) {
				servicos.add(servicosItemTipoAmb.get(s));
			}
		}
		
		
		
		//this.ZCA_ITEM = servicosItemTipoAmb.get(0).getZbcItem();
				
		this.ZCA_ITEM = aFull.getCodItemAmbiente(codProduto);
		
		
		
		// Validação se o ambiente encontra-se com status ATIVO, EM MANUTENÇÃO, CADASTRO, SUSPENSO ou DESATIVADO. 
		// Ocorre neste ponto pela necessidade de passar-se para ValidacaoStatusAmb também o valor de ZCA_ITEM
		
		
		this.ZBB_STATUS = aFull.getStatusItemAmbiente(codProduto);
		
		
//		new ValidacaoStatusAmb(parserRead.getZbbStatus().replaceAll("\\s", ""), ZCA_CODAMB, ZCA_ITEM, ZCA_PARAM, ZCA_TIPAMB);
		
		new ValidacaoStatusAmb(ZBB_STATUS, ZCA_CODAMB, ZCA_ITEM, ZCA_PARAM, ZCA_TIPAMB);
		
		if(servicos.size() == 0) {
			System.out.println("Nenhum servico localizado. Verifique o cadastro no 3C.");
			System.exit(3);
		}
		

		
		// A chamada ao WS SIGAMAT (Emerson) será feita utilizando informações obtidas a partir do primeiro 
		// SERVICO localizado abaixo do TIPO DE SERVICO "ERP", Não importando se é um MASTER ou SLAVE 
		
//			sigamatInfoParser = new SigamatInfoParserMon(servicosItemTipoAmb.get(0).getZbcIpHost(), servicosItemTipoAmb.get(0).getZbcPorta(), servicosItemTipoAmb.get(0).getZbcEnviro());
			
		    sigamatInfoParser = new SigamatInfoParserMon(servicos.get(0).getIpHost().trim(), servicos.get(0).getPorta().trim(), servicosItemTipoAmb.get(0).getEnvironment().trim());
	
			this.sigamatParserReturn = sigamatInfoParser.getReturnArray();   // sigamatParserReturn[0] = DATE 
																	         // sigamatParserReturn[1] = STATUS
																			 // sigamatParserReturn[2] = MESSAGE
																	         // sigamatParserReturn[3] = ELAPTIME
						
			
		
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
