package br.com.breezeReport.controller;


import java.net.BindException;
import java.util.ArrayList;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;


import br.com.breezeReport.dao.PersistenceDao;


import br.com.breezeReport.model.RoomInfo;
import br.com.breezeReport.util.ConverteTimestamp;



@Controller
public class BreezeReportController {
	
	private PersistenceDao persistenceDao;
	
	public BreezeReportController() {
		persistenceDao = new PersistenceDao();
	}
	
	
	@RequestMapping("loginForm")
	public String loginForm() {		
		return "formulario-login";
	}
	
	
	@RequestMapping("efetuaLogin")
	public String efetuaLogin(HttpServletRequest request, HttpServletResponse response, HttpSession session) {
		String nome = request.getParameter("login");
		String senha = request.getParameter("senha");
			
		if(persistenceDao.isLoginOK(nome, senha)) {
			session.setAttribute("usuarioLogado", nome);
		    return "index-page";
		}
		
		return "redirect:loginForm";
	}
	
	

	
	@RequestMapping("index")
	public ModelAndView execute() {
	//public String execute() {
				
		ModelAndView mv = new ModelAndView("index-page");
		mv.addObject("breezeReportBeginDate", ConverteTimestamp.addSlashesToDate(persistenceDao.getBreezeReportBeginDate()));
		return mv;
		
	    //return "index-page";
	}
		
	
		
	
	
	@RequestMapping("totalGraficosController")
	public ModelAndView graficos(HttpServletRequest request, HttpServletResponse response, BindException errors) {

		String strScoId = request.getParameter("scoId");
		long scoId = Long.parseLong(strScoId);
		
		String inputDataInicial = request.getParameter("inputDataInicial");
		String inputDataFinal = request.getParameter("inputDataFinal");
		String nome = persistenceDao.getRoomName(scoId);
		
		// Necessário adicionar mais um dia a inputDataFinal, para ser utilizado por getRoomInfoByScoFromPeriod
		inputDataFinal = ConverteTimestamp.addDiasToOnlyDate(inputDataFinal, 1);
		
		// Necessario alterar o formato da data, de dd/MM/yyyy para yyyy-MM-dd 00:00:00
		inputDataInicial = ConverteTimestamp.onlyDateToDBDateTimeFormat(inputDataInicial);
		inputDataFinal =   ConverteTimestamp.onlyDateToDBDateTimeFormat(inputDataFinal);
			
		ArrayList<RoomInfo> roomInfo = new ArrayList<RoomInfo>();
		
		roomInfo = persistenceDao.getRoomInfoByScoFromPeriod(scoId, inputDataInicial, inputDataFinal);
		
		
		// Após recuperar-se os dados na variável roomInfo, pode-se retornar os valores de inputDataInicial
		// e inputDataFinal
		inputDataInicial = ConverteTimestamp.dBDateTimeFormatToOnlyDate(inputDataInicial);
		inputDataFinal = ConverteTimestamp.dBDateTimeFormatToOnlyDate(inputDataFinal);
		inputDataFinal = ConverteTimestamp.addDiasToOnlyDate(inputDataFinal, -1);
		

		int totalHosts = 0;
		int totalApresentadores = 0;
		int totalConvidados = 0;
		int totalGeral = 0;
		
		for(RoomInfo r : roomInfo) {
			 totalHosts += r.getHosts();
			 totalApresentadores += r.getApresentadores();
			 totalConvidados += r.getConvidados();
			 totalGeral += r.getTotal();
		}
			
		
		ModelAndView mv = new ModelAndView("grafico-pie");
		
		
		mv.addObject("dataInicial", inputDataInicial);
		mv.addObject("dataFinal", inputDataFinal);
		mv.addObject("scoId", strScoId);
		mv.addObject("nome", nome);
		
		mv.addObject("totalHosts", totalHosts);
		mv.addObject("totalApresentadores", totalApresentadores);
		mv.addObject("totalConvidados", totalConvidados);
		mv.addObject("totalGeral", totalGeral);
		
		return mv;
	  
	}

	


	@RequestMapping("logout")
	public String logout(HttpSession session) {
	  session.invalidate();
	  return "redirect:loginForm";
	}

	

	
}	
	
	





















//	private PersistenceDao persistenceDao;

//	public BreezeReportController() {
//		persistenceDao = new PersistenceDao();  
//	}



	
	
// Funcionando, porem com problema na acentuacao
//@RequestMapping("allRooms2.json")
//@ResponseBody
//public String allRoomsJson2() {
//	List<RoomInfo> roomInfo = persistenceDao.getDistinctAllRoomsInfo(); 
//
//	String allRoomsJsonReturn = "["; 
//
//	for(int r = 0; r < roomInfo.size(); r++) {
//	    if(r > 0 ) 
//	    	allRoomsJsonReturn += " , ";
//		allRoomsJsonReturn += "{\"scoId\":" + "\"" + roomInfo.get(r).getScoId() + "\"" + " , \"nome\":" +  "\"" + roomInfo.get(r).getNome() + "\"" + " , \"url\":" + "\"" + roomInfo.get(r).getUrl() + "\"" + " , \"hosts\":" + "\"" + roomInfo.get(r).getHosts() + "\"" + " , \"apresentadores\":" + "\"" + roomInfo.get(r).getApresentadores() + "\"" + " , \"convidados\":" + "\"" + roomInfo.get(r).getConvidados() + "\"" + " , \"total\":" + "\"" + roomInfo.get(r).getTotal() + "\"" + " , \"dataHora\":" + "\"" + roomInfo.get(r).getDataHora() + "\"" + " , \"horaHost\":" + "\"" + roomInfo.get(r).getHoraHost() + "\"" + " , \"horaApresentador\":" + "\"" + roomInfo.get(r).getHoraApresentador() + "\"" + " , \"horaConvidado\":" + "\"" + roomInfo.get(r).getHoraConvidado() + "\"" + "}"; 	
//	}	
//	
//	allRoomsJsonReturn += "]";
//	
//	return allRoomsJsonReturn;
//
//}
	 
	

//@RequestMapping("graficosController")
//public ModelAndView graficos(HttpServletRequest request, HttpServletResponse response, BindException errors) {
//	ArrayList<RoomInfo> roomsByName = new ArrayList<RoomInfo>();
//	
//	String nome = request.getParameter("busca");
////System.out.println(nome);
//	roomsByName = persistenceDao.getRoomInfoByName(nome);
//	
//	ModelAndView mv = new ModelAndView("graficos-from-controller");
//	mv.addObject("roomsByName", roomsByName);
//	return mv;
//  
//}
