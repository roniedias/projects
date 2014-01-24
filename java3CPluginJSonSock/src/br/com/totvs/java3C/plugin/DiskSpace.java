package br.com.totvs.java3C.plugin;


import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashSet;


import br.com.totvs.java3C.JSonParser.monit.DiskSpaceParserMon;
import br.com.totvs.java3C.JSonParser.read.ParserRead;
import br.com.totvs.java3C.JSonParser.write.ParserWrite;
import br.com.totvs.java3C.bean.ServicosItemTipoAmb;

import br.com.totvs.java3C.util.PathConverter;
import br.com.totvs.java3C.util.ValidacaoStatusAmb;

import br.com.totvs.java3C.util.ZbcDiskSpaceFields;




/**
 * @author Ronie Dias Pinto
 */


public class DiskSpace {
	
	private String ZCA_PARAM;
	private String ZCA_CODAMB;
	private String ZCA_TIPAMB;
	private String ZCA_ITEM;
	private float ZCA_RESULT;
	private String ZCA_MEMO;
	
	
	
	private ArrayList<ZbcDiskSpaceFields> lstZbcDiskSpaceFields = new ArrayList<ZbcDiskSpaceFields>();
	private ArrayList<ZbcDiskSpaceFields> finalLstZbcDiskSpaceFields = new ArrayList<ZbcDiskSpaceFields>();
	

	private ArrayList<String> auxListZCA_MEMO = new ArrayList<String>();
	
		
//	private String codTipoServicoLicense = "13";
//	private String codTipoServicoTopConnect = "16";
//	private String codTipoServicoCtree = "21"; 

	private DiskSpaceParserMon diskSpaceParser;
	private String [] diskSpaceParserReturn;
	
	private String[] zbhAllTipSrvs;
	private ArrayList<String> zbhTipSrvs = new ArrayList<String>();
	private ArrayList<ServicosItemTipoAmb> servicosItemTipoAmb;

	
	

	public DiskSpace(String codAmbiente, String codTipoAmbiente, String codMonitoramento, String codProduto) {
	
		this.ZCA_PARAM = codMonitoramento; // Sexto parâmetro passado para STRUPARAMGRVMONITORAMENTO
		this.ZCA_CODAMB = codAmbiente; // Quarto parâmetro passado para STRUPARAMGRVMONITORAMENTO
		this.ZCA_TIPAMB = codTipoAmbiente; // Decimo primeiro parâmetro passado para STRUPARAMGRVMONITORAMENTO
		
//		ParserRead parserRead = new ParserRead("GENERIC_READ", codAmbiente, codTipoAmbiente, codProduto);
		ParserRead parserRead = new ParserRead("DISKSPACE_READ", codAmbiente, codTipoAmbiente, codProduto);
		
		
		if(parserRead.getZbbStatus().replaceAll("\\s", "").equals("-")) {
			System.out.println("Retorno JSON (leitura) invalido. Verifique o cadastro dos dados do ambiente no Nagios/3C");
			System.exit(3);
		}

		
		zbhAllTipSrvs = parserRead.getZbhTipSrvs();
		
		// Adicionando os tipos de serviço a zbhTipSrvs, excluindo os Tipos LICENSE, TOP e CTREE
		for(int z = 0; z < zbhAllTipSrvs.length; z++) {
//			if(!(zbhAllTipSrvs[z].equals(codTipoServicoLicense) || zbhAllTipSrvs[z].equals(codTipoServicoTopConnect) || zbhAllTipSrvs[z].equals(codTipoServicoCtree))) {
				zbhTipSrvs.add(zbhAllTipSrvs[z]);  
//			}									  
		}
		
		
		
		

		
		// Primeiro passo: Adicionar à lista lstZbcDiskSpaceFields, apenas os campos IP, PORTA, ENVIRONMENT e PATH DE INSTALAÇÃO 
		// dos serviços cadastrados no 3C.  Obs: Para o método DiskSpace funcionar, os PATHS DE INSTALAÇÃO devem estar no 
		// formato correto (\\IP\caminho)
		
			
		for(int lst = 0; lst < zbhTipSrvs.size(); lst++) {
		
			servicosItemTipoAmb = new ArrayList<ServicosItemTipoAmb>();
			servicosItemTipoAmb = parserRead.getServicosItemTipoAmb(zbhTipSrvs.get(lst));

				

			for(int s = 0; s < servicosItemTipoAmb.size(); s++) {
				lstZbcDiskSpaceFields.add(new ZbcDiskSpaceFields(servicosItemTipoAmb.get(s).getZbcIpHost().replaceAll("\\s", ""), servicosItemTipoAmb.get(s).getZbcPorta().replaceAll("\\s", ""), servicosItemTipoAmb.get(s).getZbcEnviro().replaceAll("\\s", ""), servicosItemTipoAmb.get(s).getZbcInst().replaceAll("\\s", "")));											
			}
		}
		
		
		this.ZCA_ITEM = servicosItemTipoAmb.get(0).getZbcItem(); // ZBC_ITEM, do JSON. Mesmo valor, qualquer que seja a posição do ArrayList escolhida
		
		// Validação se o ambiente encontra-se com status ATIVO, EM MANUTENÇÃO, CADASTRO, SUSPENSO ou DESATIVADO. 
		// Ocorre neste ponto pela necessidade de passar-se para ValidacaoStatusAmb também o valor de ZCA_ITEM
		new ValidacaoStatusAmb(parserRead.getZbbStatus().replaceAll("\\s", ""), ZCA_CODAMB, ZCA_ITEM, ZCA_PARAM, ZCA_TIPAMB);

		
		
		// Verificando se há serviços cadastrados
		if(lstZbcDiskSpaceFields.size() == 0) {
			System.out.println("Nenhum servico localizado.");
			System.exit(3);
		}
		
	

		// Segundo passo: eliminar as repetições, evitando que o mesmo path de instalação seja verificado mais de uma 
		// vez. Isso é feito adicionando-se a finalZbcDiskSpaceFields apenas as informações de hosts cujo´s path´s de 
		// instalação não coincidem 
	
		
	
		// HashSet não permite elementos repetidos. Adiciona-se a ele apenas os paths de instalação distintos
		HashSet<String> hashSet = new HashSet<String>();
		
		for(ZbcDiskSpaceFields z : lstZbcDiskSpaceFields) {
			if(hashSet.add(z.getZBC_INST())) {
				// Adicionado a finalLstZbcDiskSpaceFields apenas as informações (aleatorias) de hosts 
				// cujo path de instalção são diferentes
				 
				
				finalLstZbcDiskSpaceFields.add(new ZbcDiskSpaceFields(z.getZBC_IPHOST(), z.getZBC_PORTA(), z.getZBC_ENVIRO(), new PathConverter(z.getZBC_INST(), z.getZBC_IPHOST()).convertToRelative()));
				
				
			}
			
		}
		
		
		
		
		
		DecimalFormat df = new DecimalFormat("#.########"); // Se não houver esta formatação, o resultado pode vir em 
															// notação de ponto flutuante, contendo letras. 
															// Ex: 0.00004387 ser apresentado como 4.387E-5
				
		for(int z = 0; z < finalLstZbcDiskSpaceFields.size(); z++) {
			
						
				
			//String path = new PathConverter().duplicateBackSlash(finalLstZbcDiskSpaceFields.get(z).getZBC_INST());
			String path = finalLstZbcDiskSpaceFields.get(z).getZBC_INST();
				
			diskSpaceParser = new DiskSpaceParserMon(finalLstZbcDiskSpaceFields.get(z).getZBC_IPHOST(), finalLstZbcDiskSpaceFields.get(z).getZBC_PORTA(), finalLstZbcDiskSpaceFields.get(z).getZBC_ENVIRO(), path);
			this.diskSpaceParserReturn = diskSpaceParser.getReturnArray();       // diskSpaceParserReturn[0] = DATABASESIZE
																			   	 // diskSpaceParserReturn[1] = FILESSIZE
																			   	 // diskSpaceParserReturn[2] = STATUS
	        															         // diskSpaceParserReturn[3] = MESSAGE
																			     // diskSpaceParserReturn[4] = ELAPTIME

				
								
				
//			System.out.println("IPHOST: " + finalLstZbcDiskSpaceFields.get(z).getZBC_IPHOST());
//			System.out.println("PORTA: " + finalLstZbcDiskSpaceFields.get(z).getZBC_PORTA()); 
//			System.out.println("ENVIRO: " + finalLstZbcDiskSpaceFields.get(z).getZBC_ENVIRO());
//			System.out.println("INST: " + path);
//			
//			System.out.println(" ----------------------------------");
//			
//		    System.out.println("DATABASESIZE: " + diskSpaceParserReturn[0]);
//		    System.out.println("FILESSIZE: " + diskSpaceParserReturn[1]);
//		   	System.out.println("STATUS: " + diskSpaceParserReturn[2]); 
//		   	System.out.println("MESSAGE: " + diskSpaceParserReturn[3]);
//		    System.out.println("ELAPTIME: " + diskSpaceParserReturn[4]);        	

			
								
			if(diskSpaceParserReturn[2].replaceAll("\\s", "").equals("1")) {
				System.out.println("Nao foi possivel obter o consumo de disco/banco. Verifique a(s) conexao(oes) com o(s) servidor(es) cadastrado(s) no 3C. MESSAGE: " + diskSpaceParserReturn[3]);
				System.exit(3);
			}
			else {
				auxListZCA_MEMO.add("IP " + finalLstZbcDiskSpaceFields.get(z).getZBC_IPHOST() + " - Consumo banco: " + df.format(Float.valueOf(diskSpaceParserReturn[0])) + " GB. Consumo disco " + new PathConverter(finalLstZbcDiskSpaceFields.get(z).getZBC_INST()).convertToAbsolute() + ": " + df.format(Float.valueOf(diskSpaceParserReturn[1])) + " GB. ");
				ZCA_RESULT += Float.valueOf(diskSpaceParserReturn[0]) + Float.valueOf(diskSpaceParserReturn[1]);
			}
				
					
		}
	
		
		

		// Necessário gravar somente se houve pelo menos um status = 0 (O.K.)
		
		
		
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
