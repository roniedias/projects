package br.com.totvs.java3C.datasul.util;

import br.com.totvs.java3C.datasul.plugin.DispLoginDatasul;

public class DispLoginDatasulNaoGrava extends DispLoginDatasul {
	
	public DispLoginDatasulNaoGrava(String codAmbiente, String codTipoAmbiente, String codMonitoramento, String codProduto) {
		super(codAmbiente, codTipoAmbiente, codMonitoramento, codProduto);
	}
	
	public void grava() {}
}
