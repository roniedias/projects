package br.com.totvs.java3C.datasul.plugin;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

import br.com.totvs.java3C.JSonParser.write.ParserWrite;
import br.com.totvs.java3C.bean.ItemAmbiente;

import br.com.totvs.java3C.dao.Dao;
//import br.com.totvs.java3C.datasul.bean.AppServer;
import br.com.totvs.java3C.datasul.bean.AtalhoInfo;
import br.com.totvs.java3C.datasul.bean.Banco;
import br.com.totvs.java3C.datasul.bean.JBossInfo;
import br.com.totvs.java3C.datasul.util.StorageUtil;
import br.com.totvs.java3C.util.ValidacaoStatusAmb;

import com.totvs.cloud.message.params.StorageParamsDirItem;


public class Storage {


	private ArrayList<ItemAmbiente> itensAmbiente;
	private String ZBB_STATUS;
	private String ZCA_PARAM;
	private String ZCA_CODAMB;
	private String ZCA_TIPAMB;
	private String ZCA_ITEM;
	private float ZCA_RESULT;
	private String ZCA_MEMO = "Diretorios: ";
	
	
//	private ArrayList<AppServer> appServInfo;
	private ArrayList<JBossInfo> jBossInfo;
	private ArrayList<Banco> bancos;
	
	private AtalhoInfo atalhoInfo;
//	private String appServerIp = "no_info";
	private String bancoServerIp;
	private String jBossServerIp;

	private String especiais;
	private String especificos;
	private String spool;
	private String dirBanco;
	private String bkpAiLog;
	private String bkpFull;
	private String jbossCliPath;
	
	private String atalhoIp;
	private String portaMonit;
	
	
	private double tamanhoTotal = 0.0;
	DecimalFormat df = new DecimalFormat("#.########");
	
	List<StorageParamsDirItem> storageList;
	StorageParamsDirItem storageParamItem;

	
	
	// IP processo de monitoramento obtido a partir das informações cadastradas na primeira linha
	// de Datasul > Atalhos > Atalho
	
	// Caminhos físicos a serem checados:
	
				//ZBQ_ESPECI  ZBQ_ESPCIF   ZBQ_SPOOL (Essas informações não mudam. Portanto, 
	 //Aba atalho (Especiais, Específicos, SPOOL)     será considerada somente a primeira linha)
	
	            //ZBO_DIRLOC  ZBO_BKPAI  ZBO_BKPFUL  (Essas informações não mudam. Portanto,
	// Aba banco (Dir. Banco, Bkp AiLog, Bkp Full) -> será considerada somente a primeira linha) 
    
	// Obs: Caso o campo D.JBoss Cli. (Datasul > Web > Jboss) esteja preenchido, ele será incluído
	// na soma acima. Se houver mais de uma linha para este campo, todas serão incluídas e somadas  
    	        
	// Ao final, todos os paths serão somados e gravados
	
	// Obs importante: Verificar com o pessoal do frame se este tipo de monitoramento funciona em Linux

	
	public Storage(String codAmbiente, String codTipoAmbiente, String codMonitoramento, String codProduto) {

		this.ZCA_PARAM = codMonitoramento; 
		this.ZCA_CODAMB = codAmbiente;
		this.ZCA_TIPAMB = codTipoAmbiente;
		
		Dao dao = new Dao();

		itensAmbiente = dao.getItensAmbiente(codAmbiente, codTipoAmbiente);
//		appServInfo = dao.getAppServers(codAmbiente, codTipoAmbiente, codProduto);
		atalhoInfo = dao.getAtalhoInfo(codAmbiente, codTipoAmbiente, codProduto); // Adicionado em 16/09/2014
		bancos = dao.getBancos(codAmbiente, codTipoAmbiente, codProduto);
		jBossInfo = dao.getJBossInfo(codAmbiente, codTipoAmbiente, codProduto);
		portaMonit = dao.getCliente(codAmbiente).getPortaMonit();
		dao.closeConnection();
		
		for(ItemAmbiente i : itensAmbiente) {
			if(i.getCodProduto().trim().equals(codProduto)) {
				ZCA_ITEM = i.getCodItem().trim();
				ZBB_STATUS = i.getStatus().trim();
			}
		}
		
		// Validação se o ambiente encontra-se com status ATIVO, EM MANUTENÇÃO, CADASTRO, SUSPENSO ou DESATIVADO. 
		new ValidacaoStatusAmb(ZBB_STATUS, ZCA_CODAMB, ZCA_ITEM, ZCA_PARAM, ZCA_TIPAMB);
		

		// =======
		// Atalhos
		// =======
/*				
		for(AppServer a : appServInfo) { // Checando se há pelo menos um checkbox selecionado e obtendo o IP  
			if(a.getMonitora().trim().equals("T")) { 
				appServerIp = a.getIp().trim();
				break;
			}
		}
		
		if(appServerIp.equals("no_info")) {
			System.out.println("Nenhum App Server configurado para ser monitorado. Verifique no 3C, o checkbox \"Monitora?\" em Datasul > AppServer/WebSpeed");
			System.exit(1);
		}
*/
		
		try {
			atalhoIp = atalhoInfo.getIp(); // Adicionado em 16/09/2014
	    	especiais = atalhoInfo.getEspeciais();     
	    	especificos = atalhoInfo.getEspecificos(); 
	    	spool = atalhoInfo.getSpool();
		}
		catch (NullPointerException e) { 
			System.out.println("Uma ou mais informacoes nao localizada(s) em Datasul > Atalhos > Atalho (Especiais, Especificos, Spool). Verifique o cadastro do 3C.");
			System.exit(1);			
		}
		
		// ======
		// Bancos
		// ======
		
		if(bancos.size() == 0) {
			System.out.println("Informacao(oes) de banco(s) de dado(s) nao cadastrada(s) no 3C. Verifique o cadastro.");
			System.exit(1);
		}
		
		if(bancos.get(0).getIp().isEmpty() || bancos.get(0).getDirBanco().isEmpty() || bancos.get(0).getBkpAiLog().isEmpty() || bancos.get(0).getBkpFull().isEmpty()) {
			System.out.println("Uma ou mais informacoes nao localizada(s) em Datasul > Bancos > Bancos (I.P. Host, Dir. Banco, Bkp AILog e Bkp FULL). Verifique o cadastro do 3C. Obs: Informacoes pelo menos devem estar preenchidas na primeira linha deste cadastro.");
			System.exit(1);
		}
		else {
			bancoServerIp = bancos.get(0).getIp();
			dirBanco = bancos.get(0).getDirBanco();     
			bkpAiLog = bancos.get(0).getBkpAiLog(); 
			bkpFull = bancos.get(0).getBkpFull();			
		}
		
		// =====
		// JBoss
		// =====

		if(jBossInfo.size() > 0) {
			jbossCliPath = jBossInfo.get(0).getDirJBossCliente();
			jBossServerIp = jBossInfo.get(0).getIpHost();
			if(jbossCliPath.isEmpty() || jBossServerIp.isEmpty()) {
				System.out.println("Campo \"D.JBoss Cli.\" nao preenchido, em Datasul > Web > JBoss. Verifique o cadastro do 3C. Obs: Informacao pelo menos deve estar preenchida na primeira linha deste cadastro.");
				System.exit(1);
			}
			else {
				StorageUtil storageJBoss = new StorageUtil(jBossServerIp, portaMonit, jbossCliPath);
				ZCA_MEMO += storageJBoss.getInfo();
				tamanhoTotal = storageJBoss.getTamanhoTotal();
			}
		}
		
				
		StorageUtil storageAtalhos = new StorageUtil(atalhoIp, portaMonit, especiais, especificos, spool); // Alterado de appServerIp para atalhoIp, em 16/09/2014 
		StorageUtil storageBancos = new StorageUtil(bancoServerIp, portaMonit, dirBanco, bkpAiLog, bkpFull);
		tamanhoTotal += storageAtalhos.getTamanhoTotal() + storageBancos.getTamanhoTotal();
		ZCA_MEMO +=  storageAtalhos.getInfo() + storageBancos.getInfo();
		
		ZCA_RESULT = Float.valueOf(String.valueOf(df.format(tamanhoTotal).replaceAll(",", ".")));
		
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
