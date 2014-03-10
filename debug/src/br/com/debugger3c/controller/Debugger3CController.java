package br.com.debugger3c.controller;


import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import br.com.debugger3c.dao.Dao3C;
import br.com.debugger3c.model.Client3C;
import br.com.debugger3c.util.ConfigXmlParser;
import br.com.debugger3c.util.JSonMaker;
import br.com.debugger3c.util.JSonMakerMulti;


@Controller
public class Debugger3CController {
	
	protected String codigoCliente;
	protected String codigoAmbiente;
	protected String codigoEmpresa;
	protected HashSet<String> tiposAmbiente;

	protected String nomeProduto;
	
	protected String codigoTipoAmb;
	protected HashSet<String> codigosProdutos;
	protected ArrayList<String> produtos;
	
	protected String codigoProduto;
	protected ArrayList<String> tiposServicos;
	
	protected String codTipoServico;

	protected Dao3C dao3c;
	
	private ConfigXmlParser cxp;
	private String portaTomcat;
	private String websocksMonitIp;
	private String websocksPlugin3cIp;
	private String websocksReadIp;
	private String websocksWriteIp;

	
	
	
	public Debugger3CController() {
		
		// Instanciados apenas uma vez
		
		dao3c = new Dao3C();
		tiposAmbiente = new HashSet<String>();
		codigosProdutos = new HashSet<String>();
		
		cxp = new ConfigXmlParser();
	
		portaTomcat = cxp.getPortaTomcat();
		websocksMonitIp = cxp.getWebSockMonitIp();
		websocksPlugin3cIp = cxp.getWebsockPlugin3cIp();
		websocksReadIp = cxp.getWebSockReadIp();
		websocksWriteIp = cxp.getWebSockWriteIp();
		
	}
	
	
	
	@RequestMapping("monit")
	public String indexForm() {		
		return "index";
	}

	
	@RequestMapping("websocketIPsAndTomcatPort.json")
	public void getWbsocketIPsAndTomcatPort(HttpServletRequest request, HttpServletResponse response) {
		
		HashMap<String, String> hs = new HashMap<String, String>();
		hs.put("portaTomcat", portaTomcat);
		hs.put("websocksMonitIp", websocksMonitIp);
		hs.put("websocksPlugin3cIp", websocksPlugin3cIp);
		hs.put("websocksReadIp", websocksReadIp);
		hs.put("websocksWriteIp", websocksWriteIp);
		
		JSonMakerMulti jSonMakerMulti = new JSonMakerMulti(hs);
		
		String json = jSonMakerMulti.getJSon();
		
	    response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        
        try {
			response.getWriter().write(json);
		} catch (IOException e) {
			e.printStackTrace();
		}

		
	}
	
	
	@RequestMapping("allClients.json")
	public void getAllClients(HttpServletRequest request, HttpServletResponse response) {
		
		//Dao3C dao = new Dao3C();
		
		ArrayList<Client3C> clients3C = new ArrayList<Client3C>();
		//clients3C = dao.getTodosOsClientes3C();
		clients3C = dao3c.getTodosOsClientes3C();
		
		String allClientsJson = "["; 

		for(int c = 0; c < clients3C.size(); c++) {
		    
			if(c > 0 ) 
		    	allClientsJson += " , "; 	
		    
		    allClientsJson += "{\"codigo\":" + "\"" + clients3C.get(c).getCodigo() + "\"" + " , \"descricao\":" + "\"" + clients3C.get(c).getDescricao() + "\"}";
		}	
		
		allClientsJson += "]";

		
	    response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        
        try {
			response.getWriter().write(allClientsJson);
		} catch (IOException e) {
			e.printStackTrace();
		}
		
	}
	
	
	
	
	@RequestMapping("clientEnvTypes.json")
	public void getClientEnvTypes(HttpServletRequest request, HttpServletResponse response) {
		
		
		
		codigoCliente = request.getParameter("codCliente");
		
		codigoAmbiente = dao3c.getCodigoAmbiente(codigoCliente);   
		codigoEmpresa = dao3c.getCodigoEmpresa(codigoCliente);
		
		
		tiposAmbiente = dao3c.getCodigosTiposAmbiente(codigoCliente);
		
		JSonMaker jSonMaker = new JSonMaker("tipoAmbiente", tiposAmbiente);
		
		String clientEnvTypesJson = jSonMaker.getHashSetJSon();
		
	    response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        
        try {
			response.getWriter().write(clientEnvTypesJson);
		} catch (IOException e) {
			e.printStackTrace();
		}
			
	}
	
	
	
	
	@RequestMapping("products.json")
	public void getProducts(HttpServletRequest request, HttpServletResponse response) {
		
		produtos = new ArrayList<String>();
		
		codigoTipoAmb = request.getParameter("codigoTipoAmb");
		codigosProdutos = dao3c.getCodigosProdutos(codigoCliente, codigoTipoAmb); // 000030, 000024, etc.
		
		for(String p : codigosProdutos) {
			produtos.add(dao3c.getNomeProduto(p)); // Adicionando nomes aos códigos dos produtos
		}                                          // Ex: 000030 PROTHEUS 11, 000024 PROTHEUS 10 etc. 
		
		JSonMaker jSonMaker = new JSonMaker("produtos", produtos);		
		
		String produtosJson = jSonMaker.getArrayListJSon();
		
	    response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        
        try {
			response.getWriter().write(produtosJson);
		} catch (IOException e) {
			e.printStackTrace();
		}
		
	
	}
	
	
	
	
	@RequestMapping("serviceTypes.json")
	public void getTiposServicos(HttpServletRequest request, HttpServletResponse response) {
		
		tiposServicos = new ArrayList<String>();
		
		codigoProduto = request.getParameter("codigoProduto");
		
		tiposServicos = dao3c.getTiposServicos(codigoCliente, codigoTipoAmb, codigoProduto); 
		
		JSonMaker jSonMaker = new JSonMaker("tiposServicos", tiposServicos);
		
		String tiposServicosJson = jSonMaker.getArrayListJSon();
		
	    response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        
        try {
			response.getWriter().write(tiposServicosJson);
		} catch (IOException e) {
			e.printStackTrace();
		}	
		
	}
	
	
	
	@RequestMapping("allEnvParamActiveInController.json")
	public void getTodosParamAmbienteAtivosNoController(HttpServletRequest request, HttpServletResponse response) {

		HashMap<String, String> hm = new HashMap<String, String>();
		hm.put("codigoCliente", codigoCliente);
		hm.put("codigoAmbiente", codigoAmbiente);
		hm.put("codigoEmpresa", codigoEmpresa);
		hm.put("codigoTipoAmb", codigoTipoAmb);
		hm.put("codTipoServico", codTipoServico);
		hm.put("codigoProduto", codigoProduto);
		
		JSonMakerMulti jSonMakerMulti = new JSonMakerMulti(hm);
		String json = jSonMakerMulti.getJSon();
		
	    response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        
        try {
			response.getWriter().write(json);
		} catch (IOException e) {
			e.printStackTrace();
		}			
		
	}
	
	
	
	
	@RequestMapping("executeMonit")
	public void executeMonit(HttpServletRequest request, HttpServletResponse response) {
		
		codTipoServico = request.getParameter("codTipoServico");
		
		//monit();
		monitExecutingJarFile();
		
	
	}
	
	
	
	@RequestMapping("executeMonitWithUrlParams")
	public void executeMonitWithUrlParams(HttpServletRequest request, HttpServletResponse response) {
		
		codigoCliente = request.getParameter("codigoCliente"); 
		codigoEmpresa = request.getParameter("codigoEmpresa"); 
		codigoAmbiente = request.getParameter("codigoAmbiente"); 
		codigoTipoAmb = request.getParameter("codigoTipoAmb"); 
		codTipoServico = request.getParameter("codTipoServico"); 
		codigoProduto = request.getParameter("codigoProduto");
		
		monitExecutingJarFile();
			
	}

	
		
	private void monitExecutingJarFile() {
				
				
		try {
					
			
			// KPMG http://localhost:8081/debug/websockPlugin3c?codigoCliente=TEZE60&codigoEmpresa=00&codigoAmbiente=000051&codigoTipoAmb=01&codTipoServico=046&codigoProduto=000030
			//AYMAN http://localhost:8081/debug/websockPlugin3c?codigoCliente=T87332&codigoEmpresa=00&codigoAmbiente=000070&codigoTipoAmb=01&codTipoServico=025&codigoProduto=000030
//			URL url = new URL("http://localhost:8081/debug/websockPlugin3c?codigoCliente=" + codigoCliente + "&codigoEmpresa=" + codigoEmpresa + "&codigoAmbiente=" + codigoAmbiente + "&codigoTipoAmb=" + codigoTipoAmb + "&codTipoServico=" + codTipoServico + "&codigoProduto=" + codigoProduto);
			
			
			String uri = "http://" + websocksPlugin3cIp + ":" + portaTomcat + "/debug/websockPlugin3c?codigoCliente=" + codigoCliente + "&codigoEmpresa=" + codigoEmpresa + "&codigoAmbiente=" + codigoAmbiente + "&codigoTipoAmb=" + codigoTipoAmb + "&codTipoServico=" + codTipoServico + "&codigoProduto=" + codigoProduto;
			URL url = new URL(uri.replaceAll("\\s", ""));    
		    HttpURLConnection conn = (HttpURLConnection) url.openConnection();
		    conn.setRequestMethod("POST");
		    conn.setRequestProperty("Content-Type", "text/xml");

				    
  		    //Rotina que obtém o retorno da ServletWebSocketPlugin3c
			// ===================================================================================
		    BufferedReader in = new BufferedReader(new InputStreamReader(conn.getInputStream()));
		    String inputLine;
		            		            
		    while ((inputLine = in.readLine()) != null) 
		    	System.out.println(inputLine);

		    in.close();
		    // ===================================================================================
				    
		            
		}
		catch (IOException e) {
			e.printStackTrace();
		}	
				

	}
	
	
	
	
}

