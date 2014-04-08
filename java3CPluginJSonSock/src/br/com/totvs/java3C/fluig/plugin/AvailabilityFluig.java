package br.com.totvs.java3C.fluig.plugin;

import java.util.ArrayList;

import br.com.totvs.java3C.JSonParser.read.ParserRead;
import br.com.totvs.java3C.JSonParser.write.ParserWriteAvail;
import br.com.totvs.java3C.bean.ParamServicosMonit;
import br.com.totvs.java3C.bean.ServicosItemTipoAmb;
import br.com.totvs.java3C.util.Telnet;
import br.com.totvs.java3C.util.ValidacaoStatusAmb;


public class AvailabilityFluig {
	
	private String ZCA_PARAM; 
	private String ZCA_CODAMB;     
	private String ZCA_TIPAMB;
	
	private String[] zbhAllTipSrvs;
	
	private ArrayList<String> zbhTipSrvs = new ArrayList<String>();
	
	private ArrayList<ServicosItemTipoAmb> servicosItemTipoAmb;
	
	private String ZCA_ITEM;
	
	private ArrayList<String> auxListZCA_MEMO = new ArrayList<String>();
	
	
	
	private ArrayList<String> auxSERVICOSListZCC_OBS = new ArrayList<String>();
	private ArrayList<String> auxSERVICOSListZCC_SEQSRV = new ArrayList<String>(); 
	private ArrayList<String> auxSERVICOSListZCC_STATUS = new ArrayList<String>(); 
	private ArrayList<String> auxSERVICOSListZCC_TIPSRV = new ArrayList<String>();
	
	private ParamServicosMonit[] SERVICOSARRAY;
	
	private String ZCA_MEMO;


	

	public AvailabilityFluig(String codAmbiente, String codTipoAmbiente, String codMonitoramento, String codProduto) {
			
		this.ZCA_PARAM = codMonitoramento; 
		this.ZCA_CODAMB = codAmbiente;     
		this.ZCA_TIPAMB = codTipoAmbiente; 

		ParserRead parserRead = new ParserRead("GENERIC_READ", codAmbiente, codTipoAmbiente, codProduto);
		
		if(parserRead.getZbbStatus().replaceAll("\\s", "").equals("-")) {
			System.out.println("Retorno JSON (leitura) invalido. Verifique o cadastro dos dados do ambiente no Nagios/3C");
			System.exit(3);
		}
		
		
		zbhAllTipSrvs = parserRead.getZbhTipSrvs();

		// Adicionando apenas os tipos de serviço retornados pelo json de leitura.
		// Obs: Foi criado um campo novo no 3C "monitora?" em 07/04/2013 na aba "Tipos de Servicos" 
		// onde pode-se informar qual tipo de serviço se quer monitorar. Por consequência, somente
		// aqueles que tiverem este atributo configurado como "Sim" serão enviados no json de 
		// leitura (não somente o GENERIC_READ, mas todos)
		for(int z = 0; z < zbhAllTipSrvs.length; z++) {
//			if(zbhAllTipSrvs[z].equals("38")) {
				zbhTipSrvs.add(zbhAllTipSrvs[z]);  
			}									  
//		}
		
		
	    servicosItemTipoAmb = new ArrayList<ServicosItemTipoAmb>();

		for(int lst = 0; lst < zbhTipSrvs.size(); lst++) {			
			servicosItemTipoAmb = parserRead.getServicosItemTipoAmb(zbhTipSrvs.get(lst));
		}

		
		this.ZCA_ITEM = servicosItemTipoAmb.get(0).getZbcItem(); // ZBC_ITEM, do JSON. Mesmo valor, qualquer que seja a posição do ArrayList escolhida
		
		// Validação se o ambiente encontra-se com status ATIVO, EM MANUTENÇÃO, CADASTRO, SUSPENSO ou DESATIVADO. 
		new ValidacaoStatusAmb(parserRead.getZbbStatus().replaceAll("\\s", ""), ZCA_CODAMB, ZCA_ITEM, ZCA_PARAM, ZCA_TIPAMB);



		// Execução do monitoramento
		for(int s = 0; s < servicosItemTipoAmb.size(); s++) {
			
			// Os dados dos serviços devem ser armazenados em uma lista auxiliar, para ao final serem gravados no 3C 
			auxSERVICOSListZCC_OBS.add("Servico localizado");
			auxSERVICOSListZCC_SEQSRV.add(servicosItemTipoAmb.get(s).getZbcSeq()); 			 
			auxSERVICOSListZCC_TIPSRV.add(servicosItemTipoAmb.get(s).getZbcTipSrv()); // Variável que armazena o código do tipo de serviço 
																					  // (JBOSS, CHAT FLUIG, ETC.)
			
			String dns = servicosItemTipoAmb.get(s).getZbcDns();
			String porta = servicosItemTipoAmb.get(s).getZbcPorta();
			
			Telnet telnet = new Telnet(dns, Integer.valueOf(porta));
			String result[] = telnet.getResult();
			
			auxSERVICOSListZCC_STATUS.add(result[0]);
			auxListZCA_MEMO.add(result[1] + "; ");
			
		}
		
		
		SERVICOSARRAY = new ParamServicosMonit[auxSERVICOSListZCC_SEQSRV.size()]; 
		  
		for(int slst = 0; slst < auxSERVICOSListZCC_SEQSRV.size(); slst++) {
			ParamServicosMonit SERVICOS = new ParamServicosMonit(auxSERVICOSListZCC_OBS.get(slst), auxSERVICOSListZCC_SEQSRV.get(slst), auxSERVICOSListZCC_STATUS.get(slst), auxSERVICOSListZCC_TIPSRV.get(slst));
			SERVICOSARRAY[slst] = SERVICOS;
		}
	   	
	   	
	   	StringBuilder sb = new StringBuilder();  // Procedimento necessário para armazenar em ZBC_MEMO as informações de  
				   											 // todos os servidores, retornadas pelo método de disponibilidade
	   	for (String s : auxListZCA_MEMO) {
	   	    sb.append(s);
	   	}
	   	
	   	
	   	ZCA_MEMO = sb.toString();
	   	
//	   	System.out.println("ZCA_CODAMB: " + ZCA_CODAMB);
//	   	System.out.println("ZCA_TIPAMB: " + ZCA_TIPAMB);
//	   	System.out.println("ZCA_ITEM: " + ZCA_ITEM);
//	   	System.out.println("ZCA_PARAM: " + ZCA_PARAM);
//	   	System.out.println("ZCA_MEMO: " + ZCA_MEMO);
//	   	
//	   	for(int t = 0; t < SERVICOSARRAY.length; t++) {
//	   		System.out.println("ZccObs: " + SERVICOSARRAY[t].getZccObs());
//	   		System.out.println("zccSeqSrv: " + SERVICOSARRAY[t].getZccSeqSrv());
//	   		System.out.println("zccStatus: " + SERVICOSARRAY[t].getZccStatus());
//	   		System.out.println("zccTipSrv: " + SERVICOSARRAY[t].getZccTipSrv());
//	   	}

	   	ParserWriteAvail parserWriteAvail = new ParserWriteAvail("AVAILABILITY_WRITE", ZCA_CODAMB, ZCA_TIPAMB, ZCA_ITEM, ZCA_PARAM, ZCA_MEMO, SERVICOSARRAY);
		
	   	if(parserWriteAvail.getStatus().equals("0")) { // Se correu tudo bem com a gravação
	   		System.out.println("Resultado: " + parserWriteAvail.getZcaResult() + ". "+ ZCA_MEMO);
	   		System.exit(Integer.parseInt(parserWriteAvail.getLimiar()));
	   	}
	   	else {
	   		System.out.println(parserWriteAvail.getMessage());
	   		System.exit(Integer.parseInt(parserWriteAvail.getLimiar()));
	   	}

		
	}
	
	
}
