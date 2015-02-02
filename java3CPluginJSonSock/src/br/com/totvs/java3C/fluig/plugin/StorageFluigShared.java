package br.com.totvs.java3C.fluig.plugin;

import br.com.totvs.java3C.JSonParser.write.ParserWrite;
import br.com.totvs.java3C.dao.Dao;
import br.com.totvs.java3C.fluig.bean.FluigVolumeDirInfo;
import br.com.totvs.java3C.fluig.bean.ItensAmbienteFluig;
import br.com.totvs.java3C.fluig.util.FluigAPI;
import br.com.totvs.java3C.fluig.util.ServerVolumeDir;
import br.com.totvs.java3C.fluig.util.VolumeParser;
import br.com.totvs.java3C.util.ValidacaoStatusAmb;

public class StorageFluigShared {
	
	private FluigVolumeDirInfo fluigVolumeDirInfo;
	private String usuario;
	private String senha;
	private String dir;
	private String ip;
	
	private String serverVolumeDirSize;
	private String volumeDirSizeFromAPI;
	private String databaseSizeFromAPI;
	
	private ItensAmbienteFluig itensAmbienteFluig;
	
	private double serverVolumeValue;
	private double volumeDirValue;
	private double databaseValue;
	private double totalVolume;
	
	private String ZCA_PARAM; 
	private String ZCA_CODAMB;
	private String ZCA_TIPAMB;
	private String ZCA_ITEM;
	private String ZBB_STATUS;
	private String ZCA_MEMO;
	private float ZCA_RESULT;

	

	public StorageFluigShared(String codAmbiente, String codTipoAmbiente, String codMonitoramento, String codProduto) {
		
		itensAmbienteFluig = new ItensAmbienteFluig();
		
		Dao dao = new Dao();
		fluigVolumeDirInfo = dao.getFluigVolumeDirInfo(codAmbiente, codTipoAmbiente, codProduto);
		itensAmbienteFluig = dao.getItensAmbienteFluig(codAmbiente, codTipoAmbiente, codProduto);
		dao.closeConnection();
		
		
		if(!itensAmbienteFluig.getModalidade().equals("C")) { // Se o Fluig não for "Compartilhado"
			System.out.println("Modalidade configurada nao corresponde ao modelo \"Compartilhado\". Verifique o cadastro do Item de Ambiente no 3C." );
			System.exit(3);
		}
	
		
		usuario = fluigVolumeDirInfo.getUsuario();
		senha = fluigVolumeDirInfo.getSenha(); 
		dir = fluigVolumeDirInfo.getDir();
		ip = fluigVolumeDirInfo.getIp();
		
		serverVolumeDirSize = new ServerVolumeDir(usuario, senha, dir, ip).getSize().trim();
		volumeDirSizeFromAPI = new FluigAPI(itensAmbienteFluig, "volumeDirSize").getResult().trim();
		databaseSizeFromAPI = new FluigAPI(itensAmbienteFluig, "databaseSize").getResult().trim();
		
		VolumeParser vp = new VolumeParser();
		
		if(vp.getMeasure(serverVolumeDirSize).equals("Megabyte")) 
			serverVolumeValue = Double.valueOf(vp.getValue(serverVolumeDirSize)) / 1024;
		else if(vp.getMeasure(serverVolumeDirSize).equals("Kilobyte")) 
			serverVolumeValue = Double.valueOf(vp.getValue(serverVolumeDirSize)) / 1048576;
		else 
			serverVolumeValue = Double.valueOf(vp.getValue(serverVolumeDirSize));
		
		if(vp.getMeasure(volumeDirSizeFromAPI).equals("Megabyte"))
			volumeDirValue = Double.valueOf(vp.getValue(volumeDirSizeFromAPI)) / 1024;
		else if(vp.getMeasure(volumeDirSizeFromAPI).equals("Kilobyte")) 
			volumeDirValue = Double.valueOf(vp.getValue(volumeDirSizeFromAPI)) / 1048576;
		else 
			volumeDirValue = Double.valueOf(vp.getValue(volumeDirSizeFromAPI));

		if(vp.getMeasure(databaseSizeFromAPI).equals("Megabyte")) 
			databaseValue = Double.valueOf(vp.getValue(databaseSizeFromAPI)) / 1024;
		else if(vp.getMeasure(databaseSizeFromAPI).equals("Kilobyte")) 
			databaseValue = Double.valueOf(vp.getValue(databaseSizeFromAPI)) / 1048576;
		else 
			databaseValue = Double.valueOf(vp.getValue(databaseSizeFromAPI));
		
		
		totalVolume = serverVolumeValue + volumeDirValue + databaseValue;
		

		this.ZCA_PARAM = codMonitoramento; 
		this.ZCA_CODAMB = codAmbiente;
		this.ZCA_TIPAMB = codTipoAmbiente;
		
		ZCA_ITEM = itensAmbienteFluig.getCodItem();
		ZBB_STATUS = itensAmbienteFluig.getStatus();
		
		// Validação se o ambiente encontra-se com status ATIVO, EM MANUTENÇÃO, CADASTRO, SUSPENSO ou DESATIVADO. 
		new ValidacaoStatusAmb(ZBB_STATUS, ZCA_CODAMB, ZCA_ITEM, ZCA_PARAM, ZCA_TIPAMB);
		
		
		ZCA_MEMO = "Unidade " + dir + ": " + serverVolumeValue + " GB; Volume cliente: " + volumeDirValue + " GB; Banco: " + databaseValue + " GB.";
		
		ZCA_RESULT = Float.valueOf(String.valueOf(totalVolume));
		
	
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
