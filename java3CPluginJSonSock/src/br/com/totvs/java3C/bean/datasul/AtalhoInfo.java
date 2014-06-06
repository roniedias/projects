package br.com.totvs.java3C.bean.datasul;

import br.com.totvs.java3C.util.PathConverter;

public class AtalhoInfo {
	
	private String dirProwin32;
	private String dirArquivoPf;
	private String dirArquivoIni;
	private String dirRaizCliente;
	
	private PathConverter pathConverter;
	
	public AtalhoInfo() {
		pathConverter = new PathConverter();
	}

	public String getDirProwin32() {
		//return pathConverter.duplicateBackSlash(dirProwin32);
		return dirProwin32;
	}
	public void setDirProwin32(String dirProwin32) {
		this.dirProwin32 = dirProwin32;
	}
	
	public String getDirArquivoPf() {
		//return pathConverter.duplicateBackSlash(dirArquivoPf);
		return dirArquivoPf;
	}
	public void setDirArquivoPf(String dirArquivoPf) {
		this.dirArquivoPf = dirArquivoPf;
	}
	
	public String getDirArquivoIni() {
		//return pathConverter.duplicateBackSlash(dirArquivoIni);
		return dirArquivoIni;
	}
	public void setDirArquivoIni(String dirArquivoIni) {
		this.dirArquivoIni = dirArquivoIni;
	}

	public String getDirRaizCliente() {
		//return pathConverter.duplicateBackSlash(dirRaizCliente);
		return dirRaizCliente;
	}
	public void setDirRaizCliente(String dirRaizCliente) {
		this.dirRaizCliente = dirRaizCliente;
	}
	
	
	

}
