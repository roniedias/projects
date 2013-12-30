package br.com.totvs.java3C.bean;

import java.math.BigInteger;

public class ParamQtdNfeCnpj {
	
    private BigInteger zcgAcumMe;
    private BigInteger zcgAcumSr;
    private BigInteger zcgAcumTo;
    private String zcgAcumCnpj;
    private String zcgColaboracao;
    
    
    public ParamQtdNfeCnpj(BigInteger zcgAcumMe, BigInteger zcgAcumSr, BigInteger zcgAcumTo, String zcgAcumCnpj, String zcgColaboracao) {
    	this.zcgAcumMe = zcgAcumMe;
    	this.zcgAcumSr = zcgAcumSr;
    	this.zcgAcumTo = zcgAcumTo;
    	this.zcgAcumCnpj = zcgAcumCnpj;
    	this.zcgColaboracao = zcgColaboracao;
    }



	public BigInteger getZcgAcumMe() {
		return zcgAcumMe;
	}

	public void setZcgAcumMe(BigInteger zcgAcumMe) {
		this.zcgAcumMe = zcgAcumMe;
	}


	
	public BigInteger getZcgAcumSr() {
		return zcgAcumSr;
	}

	public void setZcgAcumSr(BigInteger zcgAcumSr) {
		this.zcgAcumSr = zcgAcumSr;
	}

	

	public BigInteger getZcgAcumTo() {
		return zcgAcumTo;
	}

	public void setZcgAcumTo(BigInteger zcgAcumTo) {
		this.zcgAcumTo = zcgAcumTo;
	}

	

	public String getZcgAcumCnpj() {
		return zcgAcumCnpj;
	}

	public void setZcgAcumCnpj(String zcgAcumCnpj) {
		this.zcgAcumCnpj = zcgAcumCnpj;
	}
	
	

	public String getZcgColaboracao() {
		return zcgColaboracao;
	}

	public void setZcgColaboracao(String zcgColaboracao) {
		this.zcgColaboracao = zcgColaboracao;
	}

    
    
}
