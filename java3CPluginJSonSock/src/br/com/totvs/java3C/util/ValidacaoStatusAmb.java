package br.com.totvs.java3C.util;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;


import br.com.totvs.java3C.JSonParser.write.ParserWriteValidStatusAmb;


public class ValidacaoStatusAmb {
	
	public ValidacaoStatusAmb(String status, String ZCA_CODAMB, String ZCA_ITEM, String ZCA_PARAM, String ZCA_TIPAMB) {
		
		/*
		 *  Verifica o status do item de Ambiente a ser monitorado 
		 *  (campo "Status" em "Itens de Ambiente"). Os status possíveis são:
		 * 
		 *  M = Manutencao;
		 *  C = Em Cadastro;
		 *  S = Suspenso;
		 *  D = Desativado;
		 *  
		 *  Obs: Se o Status for diferente de "A (ativo) o processo irá se encerrar e a gravação do status 
		 *  	 efetuada no 3C.    
		 *  	
		 */
		
		
		HashMap<String , String> hashStatusItemTipoAmbiente = new HashMap<String, String>();
		//hashStatusItemTipoAmbiente.put("A", "Ativo");
		hashStatusItemTipoAmbiente.put("M", "Em manutencao");
		hashStatusItemTipoAmbiente.put("C", "Em cadastro");
		hashStatusItemTipoAmbiente.put("S", "Suspenso");
		hashStatusItemTipoAmbiente.put("D", "Desativado");
		
		
		if(!status.equals("A")) { // Se o item de ambiente não estiver ativo  
																				  
			// Apenas para não passar vazio, evitando a geração de uma exception			


			
			DateFormat dateFormat = new SimpleDateFormat("yyyyMMdd");
			DateFormat timeFormat = new SimpleDateFormat("HH:mm:ss");
			Date date = new Date();
			
			String DATA = dateFormat.format(date); 
			String HORA = timeFormat.format(date);
			
			String ZCA_MEMO = "Item de ambiente encontra-se com status: " + hashStatusItemTipoAmbiente.get(status);
			
			float ZCA_RESULT = 0;
			
			
			// Chamada a para gravar o status do ambiente
			// Obs: Para este caso em específico, ZCA_TPLIMI terá retorno sempre igual a 1 (warning)
			
			
			
			ParserWriteValidStatusAmb parserWriteValidStatusAmb = new ParserWriteValidStatusAmb("STATUS_WRITE", ZCA_CODAMB, ZCA_TIPAMB, ZCA_ITEM, ZCA_PARAM, DATA, HORA, ZCA_MEMO);
			
		   	if(parserWriteValidStatusAmb.getStatus().equals("0")) { // Se correu tudo bem com a gravação
		   		System.out.println("Resultado: " + ZCA_RESULT + ". "+ ZCA_MEMO);
		   		System.exit(Integer.parseInt(parserWriteValidStatusAmb.getLimiar()));
		   	}
		   	else {
		   		System.out.println(parserWriteValidStatusAmb.getMessage());
		   		System.exit(Integer.parseInt(parserWriteValidStatusAmb.getLimiar()));
		   	}

			
		}

		
		
		
	}

}
