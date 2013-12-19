package br.com.breezeReport.servlet;

import java.io.IOException;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import br.com.breezeReport.dao.PersistenceDao;
import br.com.breezeReport.model.RoomInfo;

@WebServlet(urlPatterns={"/allRoomsInfo.json"})
public class AllRoomsInfoJsonServlet extends HttpServlet{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private PersistenceDao persistenceDao;
	
	public AllRoomsInfoJsonServlet() {
		persistenceDao = new PersistenceDao();
	}
	
	@Override
	public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		try {
						
			List<RoomInfo> roomInfo = persistenceDao.getAllRoomsInfo();
			

			String allRoomsJson = "["; 

			for(int r = 0; r < roomInfo.size(); r++) {
			    if(r > 0 ) 
			    	allRoomsJson += " , ";
				allRoomsJson += "{\"scoId\":" + "\"" + roomInfo.get(r).getScoId() + "\"" + " , \"nome\":" +  "\"" + persistenceDao.getRoomName(roomInfo.get(r).getScoId()) + "\"" + " , \"hosts\":" + "\"" + roomInfo.get(r).getHosts() + "\"" + " , \"apresentadores\":" + "\"" + roomInfo.get(r).getApresentadores() + "\"" + " , \"convidados\":" + "\"" + roomInfo.get(r).getConvidados() + "\"" + " , \"total\":" + "\"" + roomInfo.get(r).getTotal() + "\"" + " , \"dataHora\":" + "\"" + roomInfo.get(r).getDataHora() + "\"" + " , \"horaHost\":" + "\"" + roomInfo.get(r).getHoraHost() + "\"" + " , \"horaApresentador\":" + "\"" + roomInfo.get(r).getHoraApresentador() + "\"" + " , \"horaConvidado\":" + "\"" + roomInfo.get(r).getHoraConvidado() + "\"" + "}"; 	
			}	
			
			allRoomsJson += "]";
				
			
		  response.setContentType("application/json");
          response.setCharacterEncoding("UTF-8");
          response.getWriter().write(allRoomsJson);

          //System.out.println(allRoomsJson);
		}		
		catch (Exception e) {
			e.printStackTrace();
		}	
	}

}

