package br.com.totvs.java3C.deprecated;

//import java.util.ArrayList;
//import br.com.totvs.java3C.bean.Cliente;
//import br.com.totvs.java3C.bean.TipoAmbiente;
//import br.com.totvs.java3C.bean.ItemAmbiente;
//
//import br.com.totvs.java3C.dao.Dao;

public class AmbienteFull {
	
//	private Cliente cliente;
//	private ArrayList<TipoAmbiente> tiposAmbiente;
//	private ArrayList<ItemAmbiente> itensAmbiente;
//	private ArrayList<AppServer> appServers;
//	
//	private AmbienteFull envFullBean;
//	private ArrayList<Banco> bancos = new ArrayList<Banco>();
//	private AtalhoInfo atalhoInfo;
//	private ArrayList<JBossInfo> jBossInfoArr = new ArrayList<JBossInfo>();
//	
//	private Eai2Info eai2Info;
//	private String bancoFoundation;
//	
//	private StorageDirItems storageDirItems = new StorageDirItems();
//	
//		
//	public AmbienteFull(String codAmbiente, String codTipoAmbiente, String codProduto) {
//		Dao dao = new Dao();		
//		envFullBean = setEnvFullBean(dao.getCliente(codAmbiente), dao.getTiposAmbiente(codAmbiente), dao.getItensAmbiente(codAmbiente, codTipoAmbiente), dao.getAppServers(codAmbiente, codTipoAmbiente, codProduto), dao.getBancos(codAmbiente, codTipoAmbiente, codProduto), dao.getAtalhoInfo(codAmbiente, codTipoAmbiente, codProduto), dao.getJBossInfo(codAmbiente, codTipoAmbiente, codProduto), dao.getEai2Info(codAmbiente, codTipoAmbiente, codProduto), dao.getBancoFoundation(codAmbiente, codTipoAmbiente, codProduto), dao.getStorageDirItems(codAmbiente, codTipoAmbiente, codProduto));
//		dao.closeConnection();
//	}
//
//		
//	private AmbienteFull setEnvFullBean(Cliente cliente, ArrayList<TipoAmbiente> tiposAmbiente, ArrayList<ItemAmbiente> itensAmbiente, ArrayList<AppServer> appServers, ArrayList<Banco> bancos, AtalhoInfo atalhoInfo, ArrayList<JBossInfo> jBossInfoArr, Eai2Info eai2Info, String bancoFoundation, StorageDirItems storageDirItems) {
//		this.cliente = cliente;
//		this.tiposAmbiente = tiposAmbiente;
//		this.itensAmbiente = itensAmbiente;
//		this.appServers = appServers;
//		this.bancos = bancos;
//		this.atalhoInfo = atalhoInfo;
//		this.jBossInfoArr = jBossInfoArr;
//		this.eai2Info = eai2Info;
//		this.bancoFoundation = bancoFoundation;
//		this.storageDirItems = storageDirItems;
//		return this;
//	}
//	
//	
//	public Cliente getCliente() {
//		return cliente;
//	}
//	public void setCliente(Cliente cliente) {
//		this.cliente = cliente;
//	}
//	
//	public ArrayList<TipoAmbiente> getTiposAmbiente() {
//		return tiposAmbiente;
//	}
//	public void setTiposAmbiente(ArrayList<TipoAmbiente> tiposAmbiente) {
//		this.tiposAmbiente = tiposAmbiente;
//	}
//	
//	public ArrayList<ItemAmbiente> getItensAmbiente() {
//		return itensAmbiente;
//	}
//	public void setItensAmbiente(ArrayList<ItemAmbiente> itensAmbiente) {
//		this.itensAmbiente = itensAmbiente;
//	}	
//
//	public ArrayList<AppServer> getAppServers() {
//		return appServers;
//	}
//	public void setAppServer(ArrayList<AppServer> appServers) {
//		this.appServers = appServers;
//	}	
//	
//	public ArrayList<Banco> getBancos() {
//		return bancos;
//	}
//	public void setBancos(ArrayList<Banco> bancos) {
//		this.bancos = bancos;
//	}
//	
//	public AtalhoInfo getAtalhoInfo() {
//		return atalhoInfo;
//	}
//	public void setAtalhoInfo(AtalhoInfo atalhoInfo) {
//		this.atalhoInfo = atalhoInfo;
//	}
//	
//	public ArrayList<JBossInfo> getJBossInfo() {
//		return jBossInfoArr;
//	}
//	public void setJBossInfo(ArrayList<JBossInfo> jBossInfoArr) {
//		this.jBossInfoArr = jBossInfoArr;
//	}
//	
//	public Eai2Info getEai2Info() {
//		return eai2Info;
//	}
//	public void setEai2Info(Eai2Info eai2Info) {
//		this.eai2Info = eai2Info;
//	}
//	
//	public String getBancoFoundation() {
//		return bancoFoundation;
//	}
//	public void setBancoFoundation(String bancoFoundation) {
//		this.bancoFoundation = bancoFoundation;
//	}
//	
//	public StorageDirItems getStorageDirItems() {
//		return storageDirItems;
//	}
//	public void setStorageDirItems(StorageDirItems storageDirItems) {
//		this.storageDirItems = storageDirItems;
//	}
//
//
//	public AmbienteFull getEnvFullBean() {
//		return envFullBean;
//	}
//	
//	
//
//
//	public static void main(String[] args) {
//		
//		AmbienteFull ambienteFull = new AmbienteFull("000125", "09", "000019");
//		
//		Cliente cliente = ambienteFull.getCliente();
//		ArrayList<TipoAmbiente> tiposAmbiente = ambienteFull.getTiposAmbiente();
//		ArrayList<ItemAmbiente> itensAmbiente = ambienteFull.getItensAmbiente();
//		ArrayList<AppServer> appServers = ambienteFull.getAppServers();
//		ArrayList<Banco> bancos = ambienteFull.getBancos();
//		AtalhoInfo atalhoInfo = ambienteFull.getAtalhoInfo();
//		ArrayList<JBossInfo> jBossInfoArray = ambienteFull.getJBossInfo();
//		Eai2Info eai2Info = ambienteFull.getEai2Info();
//		StorageDirItems storageDirItems = ambienteFull.getStorageDirItems();
//		
//		System.out.println(cliente.getCodigo());
//		System.out.println(cliente.getNome());
//		System.out.println(cliente.getLoja());	
//		System.out.println("XXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXX");
//		
//		for(TipoAmbiente t : tiposAmbiente) {
//			System.out.println(t.getCodigo());
//			System.out.println(t.getNome());
//			System.out.println(t.getStatus());
//		}
//		System.out.println("XXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXX");		
//		
//		for(ItemAmbiente i : itensAmbiente) {
//			System.out.println(i.getCodItem());
//			System.out.println(i.getCodProduto());
//			System.out.println(i.getNomeProduto());
//			System.out.println(i.getVersaoProduto());
//			System.out.println(i.getEmpresaFilial());
//			System.out.println(i.getAmbientePai());
//			System.out.println(i.getStatus());
//			System.out.println(i.getUsuario());
//			System.out.println(i.getSenha());
//			System.out.println("==================");
//		}
//		System.out.println("XXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXX");
//		
//		for(AppServer a : appServers) {
//			System.out.println(a.getIp());
//			System.out.println(a.getPortaNs());
//			System.out.println(a.getNomeHost());
//			System.out.println(a.getInstanciaApp());
////			System.out.println(a.getUsuario());
////			System.out.println(a.getSenha());
//			System.out.println(a.getProtocolo());
//			System.out.println(a.getPortaApp());
//		}
//		
//		
//
//		System.out.println("XXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXX");
//		
//		for(Banco b : bancos) {
//			System.out.println(b.getNomeFisico());
//			System.out.println(b.getNomeLogico());
//			System.out.println(b.getMonitLogin());
////			System.out.println(b.getAlias());
//			System.out.println(b.getIp());
//			System.out.println(b.getPorta());
//			System.out.println(b.getUsuario());
//			System.out.println(b.getSenha());
//			System.out.println(b.getUrlConexao());
//			System.out.println(b.getTipoBanco());
//			
//			System.out.println("===================");
//			
//		}
//		
//		System.out.println("XXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXX");
//		
//		System.out.println(atalhoInfo.getDirProwin32());
//		System.out.println(atalhoInfo.getDirArquivoIni());
//		System.out.println(atalhoInfo.getDirArquivoPf());
//		System.out.println(atalhoInfo.getDirRaizCliente());
//		
//		System.out.println("XXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXX");
//		
//		for(JBossInfo j : jBossInfoArray) {
//			System.out.println(j.getPorta());
//			System.out.println(j.getProtocolo());
//			System.out.println(j.getNomeHost());
//		}
//		
//		System.out.println("XXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXX");
//		
//		System.out.println(eai2Info.getProtocolo());
//		System.out.println(eai2Info.getIp());
//		System.out.println(eai2Info.getPorta());
//		
//		System.out.println("XXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXX");
//		
//		System.out.println(storageDirItems.getEspeciais());
//		System.out.println(storageDirItems.getEspecificos());
//		System.out.println(storageDirItems.getSpool());
//		System.out.println(storageDirItems.getDirBanco()); // Campo Dir. Banco
//		System.out.println(storageDirItems.getBkpAiLog());
//		System.out.println(storageDirItems.getBkpFull());
//		
//	}
	
	
	
}
