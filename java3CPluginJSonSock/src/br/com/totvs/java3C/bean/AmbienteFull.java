package br.com.totvs.java3C.bean;

import java.util.ArrayList;

import br.com.totvs.java3C.dao.Dao;

public class AmbienteFull {
	
	private Cliente cliente;
	private ArrayList<TipoAmbiente> tiposAmbiente;
	private ArrayList<ItemAmbiente> itensAmbiente;
	private ArrayList<TipoServico> tiposServico;
	private ArrayList<Servico> servicos;
	
	private AmbienteFull envFullBean;
	
	// Extra
	private String codItemAmbiente;
	private String statusItemAmbiente;
	private ArrayList<Servico> servicosDeUmTipo = new ArrayList<Servico>();
	
	
	public AmbienteFull(String codAmbiente, String codTipoAmbiente, String codProduto, String codTipoServico) {
		Dao dao = new Dao();		
		envFullBean = setEnvFullBean(dao.getCliente(codAmbiente), dao.getTiposAmbiente(codAmbiente), dao.getItensAmbiente(codAmbiente, codTipoAmbiente), dao.getTiposServico(codAmbiente, codTipoAmbiente, codProduto), dao.getServicos(codAmbiente, codTipoAmbiente, codProduto, codTipoServico));
		dao.closeConnection();
	}
	
	
	public AmbienteFull(String codAmbiente, String codTipoAmbiente, String codProduto) {
		Dao dao = new Dao();		
		envFullBean = setEnvFullBean(dao.getCliente(codAmbiente), dao.getTiposAmbiente(codAmbiente), dao.getItensAmbiente(codAmbiente, codTipoAmbiente), dao.getTiposServico(codAmbiente, codTipoAmbiente, codProduto), dao.getTodosOsServicos(codAmbiente, codTipoAmbiente, codProduto));
		dao.closeConnection();
	}

		
	private AmbienteFull setEnvFullBean(Cliente cliente, ArrayList<TipoAmbiente> tiposAmbiente, ArrayList<ItemAmbiente> itensAmbiente, ArrayList<TipoServico> tiposServico, ArrayList<Servico> servicos) {
		this.cliente = cliente;
		this.tiposAmbiente = tiposAmbiente;
		this.itensAmbiente = itensAmbiente;
		this.tiposServico = tiposServico;
		this.servicos = servicos;
		return this;
	}
	
	
	public Cliente getCliente() {
		return cliente;
	}
	public void setCliente(Cliente cliente) {
		this.cliente = cliente;
	}
	
	public ArrayList<TipoAmbiente> getTiposAmbiente() {
		return tiposAmbiente;
	}
	public void setTiposAmbiente(ArrayList<TipoAmbiente> tiposAmbiente) {
		this.tiposAmbiente = tiposAmbiente;
	}
	
	public ArrayList<ItemAmbiente> getItensAmbiente() {
		return itensAmbiente;
	}
	public void setItensAmbiente(ArrayList<ItemAmbiente> itensAmbiente) {
		this.itensAmbiente = itensAmbiente;
	}
	
	public ArrayList<TipoServico> getTiposServico() {
		return tiposServico;
	}
	public void setTiposServico(ArrayList<TipoServico> tiposServico) {
		this.tiposServico = tiposServico;
	}
	
	public ArrayList<Servico> getServicos() {
		return servicos;
	}
	public void setServico(ArrayList<Servico> servicos) {
		this.servicos = servicos;
	}
	
	public AmbienteFull getEnvFullBean() {
		return envFullBean;
	}
	
	
	
	//Extra getters
	
	public String getCodItemAmbiente(String codProduto) {
		for(int z = 0; z < this.getItensAmbiente().size(); z++) {
			if(this.getItensAmbiente().get(z).getCodProduto().equals(codProduto)) 
				codItemAmbiente = this.getItensAmbiente().get(z).getCodItem();
		}
		return codItemAmbiente;
	}
	
	public String getStatusItemAmbiente(String codProduto) {
		for(int s = 0; s < this.getItensAmbiente().size(); s++) {
			if(this.getItensAmbiente().get(s).getCodProduto().equals(codProduto)) 
				statusItemAmbiente = this.getItensAmbiente().get(s).getStatus();
		}
		return statusItemAmbiente;
	}
	
	public ArrayList<Servico> getServicosDeUmTipo(String codTipoServico) {
		for(Servico s : this.servicos) {
			if(s.getCodTipoServico().equals(codTipoServico)) {
				servicosDeUmTipo.add(s);
			}
		}
		return servicosDeUmTipo;
	}

	
	
//	public static void main(String[] args) {
//		
//		// Rotina para teste
//		//AmbienteFull ambienteFull = new AmbienteFull("000070", "01", "000030", "10");
//		AmbienteFull ambienteFull = new AmbienteFull("000070", "04", "000030");
//		
//		Cliente cliente = ambienteFull.getCliente();
//		ArrayList<TipoAmbiente> tiposAmbiente = ambienteFull.getTiposAmbiente();
//		ArrayList<ItemAmbiente> itensAmbiente = ambienteFull.getItensAmbiente();
//		ArrayList<TipoServico> tiposServico = ambienteFull.getTiposServico();
//		ArrayList<Servico> servicos = ambienteFull.getServicos();
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
//			System.out.println("==================");
//		}
//		System.out.println("XXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXX");
//
//		for(TipoServico ts : tiposServico) {
//			System.out.println(ts.getCodigo());
//			System.out.println(ts.getNome());
//			System.out.println("==================");
//		}
//		System.out.println("XXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXX");
//		
//		for(Servico s : servicos) {
//			System.out.println(s.getSequencia());
//			System.out.println(s.getEnvironment());
//			System.out.println(s.getBalance());
//			System.out.println(s.getIpHost());
//			System.out.println(s.getIpExterno());
//			System.out.println(s.getDns());
//			System.out.println("LENGTH: " + s.getDns().trim().length());
//			System.out.println(s.getPorta());
//			System.out.println(s.getPathIni());
//			System.out.println(s.getPathInstal());
//			System.out.println(s.getDbAlias());
//			System.out.println("==================");
//		}
//		System.out.println("XXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXX");		
//				
//	}
	
	
}
