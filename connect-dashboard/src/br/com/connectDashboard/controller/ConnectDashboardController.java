package br.com.connectDashboard.controller;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;
import org.w3c.dom.CharacterData;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import br.com.connectDashboard.dao.Dao;
import br.com.connectDashboard.model.MaximoUsuariosPorData;
import br.com.connectDashboard.model.ResponsavelSala;
import br.com.connectDashboard.model.Sala;
import br.com.connectDashboard.model.UsuariosOnlineSala;
import br.com.connectDashboard.util.Init;

@Controller
public class ConnectDashboardController {

	private static String CONFIG_FILE_PATH; 
	
	
	public ConnectDashboardController() {
		CONFIG_FILE_PATH = Init.getWebInfPath() + "/app-config.xml"; // Caminho do arquivo app-config que se encontra dentro da pasta WEB-INF
	}
	
	
	
	@RequestMapping("login")
	public String loginForm() {		
		return "formulario-login";
	}
	
	@RequestMapping("index")
	public String indexForm() {		
		return "index";
	}
	
	@RequestMapping("logout")
	public String logout(HttpSession session) {
		session.invalidate();
		return "redirect:login";
	}

	
		
	@RequestMapping("efetuaLogin")
	public ModelAndView efetuaLogin(HttpServletRequest request, HttpSession session) {
		
		String usr = request.getParameter("input-login-usuario");
		String pass = request.getParameter("input-login-senha");

		HttpClient httpClient = new DefaultHttpClient();
		HttpPost httpPost = new HttpPost(getUrlConnect().trim() + "/api/xml?action=login&login=" + usr + "&password=" + pass);
		
		String statusCode = new String();
		
		try {
			
			HttpResponse res = httpClient.execute(httpPost);
			HttpEntity respEntity = res.getEntity();
			
			if(respEntity != null) {
				
				String xml = EntityUtils.toString(respEntity); // Atribuindo o XML resultante à variável do tipo String "xml"
				
				try {
					// Parse do valor contido no atributo code da tag status, filha da tag results no XML retornado pela requisição ao Servidor Adobe Connect Pro
					ByteArrayInputStream stream = new ByteArrayInputStream(xml.getBytes());
					DocumentBuilder builder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
					Document doc = builder.parse(stream);
					doc.getDocumentElement().normalize();				
					NodeList resultsNodes = doc.getElementsByTagName("results");						
					Element elementResource = (Element) resultsNodes.item(0);
					NodeList status = elementResource.getElementsByTagName("status");
					statusCode = status.item(0).getAttributes().getNamedItem("code").getNodeValue();						
				}
				catch (SAXException e) {
			        Exception x = e.getException ();
			        ((x == null) ? e : x).printStackTrace ();
				}
				catch (ParserConfigurationException pe) {
					pe.printStackTrace();
				}
				
			}
			
			
		}
		catch (ClientProtocolException e) {
			e.printStackTrace();
		} 
		catch (IOException e) {
			//System.out.println("uri invalida: " + uri);
			e.printStackTrace();
		}

		ModelAndView mav = new ModelAndView();
		mav.addObject("statusCode", statusCode);
		
		
        if(statusCode.equals("ok")) {   	
        	
        	session.setAttribute("usuarioLogado", usr);
       
            mav.setViewName("index");
            
        }
        else {
        	
            mav.setViewName("redirect:login");
        	
        }

        return mav;
		
	}
	
		
	
	
	@RequestMapping("todasAsSalas.json")
	public void getTodasAsSalasJson(HttpServletRequest request, HttpServletResponse response) {
		
		Dao dao = new Dao();
		
		ArrayList<Sala> salas = new ArrayList<Sala>();
		salas = dao.getTodasAsSalas();
		
		String todasAsSalasJson = "["; 

		for(int ts = 0; ts < salas.size(); ts++) {
		    
			if(ts > 0 ) 
				todasAsSalasJson += " , "; 	
		    
			todasAsSalasJson += "{\"scoId\":" + "\"" + salas.get(ts).getScoId() + "\"" + " , \"nome\":" + "\"" + salas.get(ts).getNome() + "\"}";
		}	
		
		todasAsSalasJson += "]";

		
	    response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        
        try {
			response.getWriter().write(todasAsSalasJson);
		} catch (IOException e) {
			e.printStackTrace();
		}
		
	}
	
	

	
	@RequestMapping("usuariosOnlineSala.json")
	public void getusuariosOnlineSalaJson(HttpServletRequest request, HttpServletResponse response) {
		
		Dao dao = new Dao();
		
		ArrayList<UsuariosOnlineSala> uos = new ArrayList<UsuariosOnlineSala>();
		uos = dao.getUsuariosOnlineSala();
		
		String usuariosOnlineSalaJson = "["; 

		for(int us = 0; us < uos.size(); us++) {
		    
			if(us > 0 ) 
				usuariosOnlineSalaJson += " , "; 	
		    
			usuariosOnlineSalaJson += "{\"scoId\":" + "\"" + uos.get(us).getScoId() + "\"" + " , \"hosts\":" + "\"" + uos.get(us).getHosts() + "\"" + " , \"apresentadores\":" + "\"" + uos.get(us).getApresentadores() + "\"" + " , \"convidados\":" + "\"" + uos.get(us).getConvidados() + "\"" + " , \"total\":" + "\"" + uos.get(us).getTotal() + "\"" + " , \"nomeSala\":" + "\"" + uos.get(us).getNomeSala() + "\" }";
		}	
		
		usuariosOnlineSalaJson += "]";

		
	    response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        
        try {
			response.getWriter().write(usuariosOnlineSalaJson);
		} catch (IOException e) {
			e.printStackTrace();
		}
		
	}
	


	
	@RequestMapping("salasEmUsoNoMesAtual.json")
	public void getSalasEmUsoNoMesAtualJson(HttpServletRequest request, HttpServletResponse response) {
		
		Dao dao = new Dao();
		
		ArrayList<Sala> salasEmUsoNoMesAtual = new ArrayList<Sala>();
		salasEmUsoNoMesAtual = dao.getSalasEmUsoNoMesAtual();
		
		String salasEmUsoNoMesAtualJson = "["; 

		for(int ts = 0; ts < salasEmUsoNoMesAtual.size(); ts++) {
		    
			if(ts > 0 ) 
				salasEmUsoNoMesAtualJson += " , "; 	
		    
			salasEmUsoNoMesAtualJson += "{\"scoId\":" + "\"" + salasEmUsoNoMesAtual.get(ts).getScoId() + "\"" + " , \"nome\":" + "\"" + salasEmUsoNoMesAtual.get(ts).getNome() + "\"}";
		}	
		
		salasEmUsoNoMesAtualJson += "]";

		
	    response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        
        try {
			response.getWriter().write(salasEmUsoNoMesAtualJson);
		} catch (IOException e) {
			e.printStackTrace();
		}
		
	}
	
	
	
	
	
//	@RequestMapping("maximoUsuariosPorScoId.json")
	@RequestMapping("maximoUsuariosDataMesCorrente.json")
	public void getmaximoUsuariosPorScoIdJson(HttpServletRequest request, HttpServletResponse response) {
		
		Dao dao = new Dao();
		
		
		ArrayList<MaximoUsuariosPorData> maximoUsuariosPorScoId = new ArrayList<MaximoUsuariosPorData>();
		
		maximoUsuariosPorScoId = dao.getMaximoUsuariosDataMesCorrente(request.getParameter("scoId"));
		
		String maximoUsuariosPorScoIdJson = "["; 

		for(int m = 0; m < maximoUsuariosPorScoId.size(); m++) {
		    
			if(m > 0 ) 
				maximoUsuariosPorScoIdJson += " , "; 	
		    
			maximoUsuariosPorScoIdJson += "{\"maxUsuarios\":" + "\"" + maximoUsuariosPorScoId.get(m).getMaxUsuarios() + "\"" + " , \"data\":" + "\"" + maximoUsuariosPorScoId.get(m).getData() + "\"}";
		}	
		
		maximoUsuariosPorScoIdJson += "]";

		
	    response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        
        try {
			response.getWriter().write(maximoUsuariosPorScoIdJson);
		} catch (IOException e) {
			e.printStackTrace();
		}
		
	}
	
	
	
	@RequestMapping("maximoUsuariosPorData.json")
	public void getmaximoUsuariosPorNomeJson(HttpServletRequest request, HttpServletResponse response) {
		
		Dao dao = new Dao();
		
		
		ArrayList<MaximoUsuariosPorData> maximoUsuariosPorNome = new ArrayList<MaximoUsuariosPorData>();
			
		String dataInicial = request.getParameter("dataInicial");
		String dataFinal = request.getParameter("dataFinal");
		//String nomeSala = request.getParameter("nomeSala").replaceAll("Ã¡", "á").replaceAll("Ã©", "é").replaceAll("Ã­", "í").replaceAll("Ã­", "Í").replaceAll("Ã³", "ó").replaceAll("Ãº", "ú").replaceAll("Ã£", "ã").replaceAll("Ãµ", "õ").replaceAll("Ã§", "ç").replaceAll("Ã´", "ô").replaceAll("Ãª", "ê").replaceAll("Ã¢", "â");
		String scoId = request.getParameter("scoId");

		
		maximoUsuariosPorNome = dao.getMaximoUsuariosData(dataInicial, dataFinal, scoId);
		
		String maximoUsuariosPorScoIdJson = "["; 

		for(int m = 0; m < maximoUsuariosPorNome.size(); m++) {
		    
			if(m > 0 ) 
				maximoUsuariosPorScoIdJson += " , "; 	
		    
			maximoUsuariosPorScoIdJson += "{\"maxUsuarios\":" + "\"" + maximoUsuariosPorNome.get(m).getMaxUsuarios() + "\"" + " , \"data\":" + "\"" + maximoUsuariosPorNome.get(m).getData() + "\"}";
		}	
		
		maximoUsuariosPorScoIdJson += "]";

		
	    response.setContentType("application/json");
        
        
        try {
			response.getWriter().write(maximoUsuariosPorScoIdJson);
		} catch (IOException e) {
			e.printStackTrace();
		}
		
	}

	
	
	@RequestMapping("salasFixas.json")
	public void getSalasFixasJson(HttpServletRequest request, HttpServletResponse response) {
		
		Dao dao = new Dao();
		
		ArrayList<Sala> salasFixas = new ArrayList<Sala>();
		salasFixas = dao.getSalasFixas();
		
		String salasFixasJson = "["; 

		for(int ts = 0; ts < salasFixas.size(); ts++) {
		    
			if(ts > 0 ) 
				salasFixasJson += " , "; 	
		    
			salasFixasJson += "{\"scoId\":" + "\"" + salasFixas.get(ts).getScoId() + "\"" + " , \"nome\":" + "\"" + salasFixas.get(ts).getNome().replaceAll("\\\\", "\\\\\\\\").replaceAll("\"","'") + "\"}";
		}	
		
		salasFixasJson += "]";

		
	    response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        
        try {
			response.getWriter().write(salasFixasJson);
		} catch (IOException e) {
			e.printStackTrace();
		}
		
	}
	
	
	
	@RequestMapping("responsaveisSala.json")
	public void getResponsaveisSalaJson(HttpServletRequest request, HttpServletResponse response) {
		
		Dao dao = new Dao();
		
		ArrayList<ResponsavelSala> responsaveisSala = new ArrayList<ResponsavelSala>();
		
		String scoIdSala = request.getParameter("scoId");
		
		responsaveisSala = dao.getResponsaveisSala(scoIdSala);
		

		String responsaveisSalaJson = "["; 

		for(int rs = 0; rs < responsaveisSala.size(); rs++) {
		    
			if(rs > 0 ) 
				responsaveisSalaJson += " , "; 	
		    
			responsaveisSalaJson += "{\"principalId\":" + "\"" + responsaveisSala.get(rs).getPrincipalId() + "\"" + " , \"nome\":" + "\"" + responsaveisSala.get(rs).getNome() + "\"" + " , \"email\":" + "\"" + responsaveisSala.get(rs).getEmail() + "\" }";
		}	
		
		responsaveisSalaJson += "]";

		
	    response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        
        try {
			response.getWriter().write(responsaveisSalaJson);
		} catch (IOException e) {
			e.printStackTrace();
		}
		

	}
	
	

	
	private String getUrlConnect() {
	
		
		String urlConnect = new String();
		
		
		File fXmlFile = new File(CONFIG_FILE_PATH);
		DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
		
		try {
			
			DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
			Document doc = dBuilder.parse(fXmlFile);
			doc.getDocumentElement().normalize();
			
			NodeList nodeList = doc.getElementsByTagName("app-config");
			
			Element elementResource = (Element) nodeList.item(0);
	
			
			NodeList urlConnectElement = elementResource.getElementsByTagName("url-connect");
			Element urlConnectLine = (Element) urlConnectElement.item(0);
			Node urlConnectChild = urlConnectLine.getFirstChild();
			CharacterData urlConnectCharacterData = (CharacterData) urlConnectChild;
			urlConnect = urlConnectCharacterData.getNodeValue();
			
		}	
		catch(ParserConfigurationException pce) {  
			pce.printStackTrace();
		}
		catch(SAXException se) {
			se.printStackTrace();
		}
		catch(IOException ioe) {
			ioe.printStackTrace();
		}
		
		return urlConnect;
	
	}
	
	
	
	@RequestMapping("usuarioSessao.json")
	public void getUsuarioSessaoJson(HttpServletRequest request, HttpServletResponse response, HttpSession session) {
		
		String usuario = (String) session.getAttribute("usuarioLogado");
		
		String usuarioJson = "[{\"usuario\":\"" + usuario + "\"}]";
	
	    response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        
        try {
			response.getWriter().write(usuarioJson);
		} catch (IOException e) {
			e.printStackTrace();
		}
		
	}
	
	
	
}
