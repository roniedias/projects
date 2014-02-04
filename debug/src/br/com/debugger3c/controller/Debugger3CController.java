package br.com.debugger3c.controller;

import java.io.BufferedReader;
import java.io.IOException;
//import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashSet;


import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;


import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import br.com.debugger3c.dao.Dao3C;
import br.com.debugger3c.model.Client3C;

import br.com.debugger3c.util.JSonMaker;

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
	
	
	
//	private static final String PATH_TO_JAVA = "C:\\Program Files\\Java\\jdk1.7.0_17\\jre\\bin\\java.exe";
//	private static final String JAR_FILE = "C:\\Users\\ronie.dias\\workspace\\debugger3c-spring\\WebContent\\WEB-INF\\lib\\3CNagiosPlugin.jar";
	
//	private static final String PATH_TO_JAVA = "C:\\Program Files (x86)\\Java\\jre7\\bin\\java.exe"; // 172.18.0.150
	private static final String PATH_TO_JAVA = "C:\\Program Files\\Java\\jdk1.7.0_45\\jre\\bin\\java.exe"; // 172.18.0.149
	
	private static final String JAR_FILE = "C:\\apache-tomcat-7.0.47\\webapps\\debug\\WEB-INF\\lib\\3CNagiosPlugin.jar";
	
	
	public Debugger3CController() {
		
		// Instanciados apenas uma vez
		
		dao3c = new Dao3C();
		tiposAmbiente = new HashSet<String>();
		codigosProdutos = new HashSet<String>();
	}
	
	
	
	@RequestMapping("monit")
	public String indexForm() {		
		return "index";
	}

	
	@RequestMapping("allClients.json")
	public void getAllClients(HttpServletRequest request, HttpServletResponse response) {
		
		Dao3C dao = new Dao3C();
		
		ArrayList<Client3C> clients3C = new ArrayList<Client3C>();
		clients3C = dao.getTodosOsClientes3C();
		
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
	
	
	
	
	@RequestMapping("executeMonit")
	public void executeMonit(HttpServletRequest request, HttpServletResponse response) {
		
		codTipoServico = request.getParameter("codTipoServico");
		
		monit();					
	
	}
	
	
	
	@RequestMapping("executeMonitWithUrlParams")
	public void executeMonitWithUrlParams(HttpServletRequest request, HttpServletResponse response) {
		
		codigoCliente = request.getParameter("codigoCliente"); 
		codigoEmpresa = request.getParameter("codigoEmpresa"); 
		codigoAmbiente = request.getParameter("codigoAmbiente"); 
		codigoTipoAmb = request.getParameter("codigoTipoAmb"); 
		codTipoServico = request.getParameter("codTipoServico"); 
		codigoProduto = request.getParameter("codigoProduto");
		
		monit();
		
		
	}

	
	
	
	// Método que efetua o monitoramento, propriamente dito
	private void monit() {
		
		
		new Thread(new Runnable() {
			public void run() {
				
				try {
					
					String[] comando = {PATH_TO_JAVA, "-jar", JAR_FILE, codigoCliente, codigoEmpresa, codigoAmbiente, codigoTipoAmb, codTipoServico, codigoProduto};
					
					Runtime rt = Runtime.getRuntime();
					Process proc = null;
				
					proc = rt.exec(comando);
					
					BufferedReader stdInput = new BufferedReader(new InputStreamReader(proc.getInputStream()));
			
				    BufferedReader stdError = new BufferedReader(new InputStreamReader(proc.getErrorStream()));
			
				    // lê a saída do comando (Saida padrao do comando)
				    
				    String s = null;
				    while ((s = stdInput.readLine()) != null) {
				        System.out.println(s);
				    }
				    
				    // lê erros a partir do comando executado (Erro padrao do comando, se houver)
				    
				    while ((s = stdError.readLine()) != null) {
				        System.out.println(s);
				    }
				}
				catch (IOException e) {
					e.printStackTrace();
				}	
				
			}
		}).start();

	}
	
	
	
	
}

