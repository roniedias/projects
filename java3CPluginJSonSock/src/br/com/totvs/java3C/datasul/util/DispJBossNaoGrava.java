package br.com.totvs.java3C.datasul.util;

import br.com.totvs.java3C.datasul.plugin.DispJBoss;


public class DispJBossNaoGrava extends DispJBoss{

	
	public DispJBossNaoGrava(String codAmbiente, String codTipoAmbiente, String codMonitoramento, String codProduto) {
		super(codAmbiente, codTipoAmbiente, codMonitoramento, codProduto);
		        			    
    }
	
	
	public void grava() {}

}

