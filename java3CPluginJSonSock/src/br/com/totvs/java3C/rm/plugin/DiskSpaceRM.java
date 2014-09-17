package br.com.totvs.java3C.rm.plugin;

import java.util.ArrayList;

import br.com.totvs.java3C.JSonParser.read.ParserRead;
import br.com.totvs.java3C.JSonParser.write.ParserWrite;
import br.com.totvs.java3C.bean.ServicosItemTipoAmb;
import br.com.totvs.java3C.rm.JSonParser.RmSrvRetDskSpcParser;
import br.com.totvs.java3C.util.StrToUTF8;
import br.com.totvs.java3C.util.TcpConnection;
import br.com.totvs.java3C.util.ValidacaoStatusAmb;

public class DiskSpaceRM {
	
	private String ZCA_PARAM;
	private String ZCA_CODAMB;
	private String ZCA_TIPAMB;
	private String ZCA_ITEM;
	
	private String[] zbhAllTipSrvs;
	private ArrayList<String> zbhTipSrvs = new ArrayList<String>();
	
	private String codTipoServicoLicense = "13";
	
	private ArrayList<ServicosItemTipoAmb> servicosItemTipoAmb = new ArrayList<ServicosItemTipoAmb>();
	
	private String serverReturn;
	
	private float filesSize;
	private float databaseSize;
	
	private ArrayList<String> auxListZCA_MEMO = new ArrayList<String>();
	
	private float ZCA_RESULT;
	private String ZCA_MEMO;
	
	
	public DiskSpaceRM(String codAmbiente, String codTipoAmbiente, String codMonitoramento, String codProduto) {
		
		this.ZCA_PARAM = codMonitoramento; 
		this.ZCA_CODAMB = codAmbiente;     
		this.ZCA_TIPAMB = codTipoAmbiente; 

		ParserRead parserRead = new ParserRead("GENERIC_READ", codAmbiente, codTipoAmbiente, codProduto);
	
		
		if(parserRead.getZbbStatus().replaceAll("\\s", "").equals("-")) {
			System.out.println("Retorno JSON (leitura) invalido. Verifique o cadastro dos dados do ambiente no Nagios/3C");
			System.exit(3);
		}
		

		zbhAllTipSrvs = parserRead.getZbhTipSrvs();
		
		// Adicionando os tipos de serviço a zbhTipSrvs, excluindo os Tipos LICENSE, TOP e CTREE
		for(int z = 0; z < zbhAllTipSrvs.length; z++) {
			if(!(zbhAllTipSrvs[z].equals(codTipoServicoLicense))) {
				zbhTipSrvs.add(zbhAllTipSrvs[z]);  
			}									  
		}
		
		
		for(int lst = 0; lst < zbhTipSrvs.size(); lst++) {
			
			servicosItemTipoAmb = parserRead.getServicosItemTipoAmb(zbhTipSrvs.get(lst));
		}
			
		
		
		this.ZCA_ITEM = servicosItemTipoAmb.get(0).getZbcItem(); // ZBC_ITEM, do JSON. Mesmo valor, qualquer que seja a posição do ArrayList escolhida
		
		// Validação se o ambiente encontra-se com status ATIVO, EM MANUTENÇÃO, CADASTRO, SUSPENSO ou DESATIVADO. 
		// Ocorre neste ponto pela necessidade de passar-se para ValidacaoStatusAmb também o valor de ZCA_ITEM
		new ValidacaoStatusAmb(parserRead.getZbbStatus().replaceAll("\\s", ""), ZCA_CODAMB, ZCA_ITEM, ZCA_PARAM, ZCA_TIPAMB);


		// Verificando se há serviços cadastrados
		if(servicosItemTipoAmb.size() == 0) {
			System.out.println("Nenhum servico localizado.");
			System.exit(3);
		}

		
		
		// Monitoramento de storage para o produto RM, propriamente dito. O consumo de disco deve ser 
		// somado, caso haja mais de uma aplicação cadastrada no ambiente. O consumo de 
		//banco de dados, não precisa  
		for(int s = 0; s < servicosItemTipoAmb.size(); s++) {
			
			TcpConnection tcpConnection = new TcpConnection(servicosItemTipoAmb.get(s).getZbcIpHost(), 4000, "DISKSPACE#" + servicosItemTipoAmb.get(s).getZbcIpHost() + "#8051#" + servicosItemTipoAmb.get(s).getZbcEnviro() + "#");
			serverReturn = new StrToUTF8().convert(tcpConnection.getSrvReturn());
			serverReturn = serverReturn.substring(1).replaceAll("Value cannot be null.", ""); // Retirando a "?" que aparece no início do JSON e a String "Value cannot be null." que aparece no final 
			
			RmSrvRetDskSpcParser r = new RmSrvRetDskSpcParser(serverReturn);
			
			
//			filesSize += Float.valueOf(r.getDiskspace().getFilesSize().replaceAll(",", "."));
			filesSize += Float.valueOf(r.getDiskspace().getFilesSize().replaceAll(",", ".")) / 1024; // Para retornar o resultado em GB
//			databaseSize = Float.valueOf(r.getDiskspace().getDatabaseSize().replaceAll(",", ".")); // Não soma... irá pegar o último
			databaseSize = Float.valueOf(r.getDiskspace().getDatabaseSize().replaceAll(",", ".")) / 1024; // Não soma... irá pegar o último
						
			if(r.getDiskspace().getStatus().replaceAll("\\s", "").equals("0")) {
//				auxListZCA_MEMO.add("IP " + servicosItemTipoAmb.get(s).getZbcIpHost() + " - Consumo disco " + r.getDiskspace().getFilesSize().replaceAll(",", ".") + " MB; ");
				auxListZCA_MEMO.add("IP " + servicosItemTipoAmb.get(s).getZbcIpHost() + " - Consumo disco " + String.valueOf(Float.valueOf(r.getDiskspace().getFilesSize()) / 1024) + " GB; ");
			}
			else {
				auxListZCA_MEMO.add("IP " + servicosItemTipoAmb.get(s).getZbcIpHost() + " - " + r.getDiskspace().getMessage() + ". ");  
			}
			
		}
		
		//auxListZCA_MEMO.add("Consumo de banco: " + databaseSize + " MB");
		auxListZCA_MEMO.add("Consumo de banco: " + databaseSize + " GB");
		
		ZCA_RESULT += filesSize + databaseSize;
		
	  	StringBuilder sb = new StringBuilder();    
		 
	   	for (String s1 : auxListZCA_MEMO)
	   	{
	   	    sb.append(s1);
	   	}
	   	
	   	ZCA_MEMO = sb.toString();
	   	
		// Apenas para não passar vazio, evitando a geração de uma exception
	   	
	   	
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
