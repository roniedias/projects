package br.com.breezeReport.servlet;

import java.io.IOException;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import br.com.breezeReport.dao.PersistenceDao;
import br.com.breezeReport.model.Room;


@WebServlet(urlPatterns={"/allRooms.json"})
public class AllRoomsJsonServlet extends HttpServlet{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private PersistenceDao persistenceDao;
	
	public AllRoomsJsonServlet() {
		persistenceDao = new PersistenceDao();
	}
	
	@Override
	public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		try {
						
			List<Room> rooms = persistenceDao.getAllRooms();
			 

			String allRoomsJson = "["; 

			for(int r = 0; r < rooms.size(); r++) {
			    if(r > 0 ) 
			    	allRoomsJson += " , "; 	
			    allRoomsJson += "{\"scoId\":" + "\"" + rooms.get(r).getScoId() + "\"" + " , \"nome\":" +  "\"" + rooms.get(r).getNome() + "\"" + " , \"desabilitada\":" + "\"" + rooms.get(r).getDesabilitada() + "\"" + " , \"dataInicial\":" + "\"" + persistenceDao.getRoomBeginDate(rooms.get(r).getScoId()) + "\"" + " , \"dataFinal\":" + "\"" + persistenceDao.getRoomEndDate(rooms.get(r).getScoId()) +  "\"}";
			}	
			
			allRoomsJson += "]";
				
			
		  response.setContentType("application/json");
          response.setCharacterEncoding("UTF-8");
          response.getWriter().write(allRoomsJson);

		}		
		catch (Exception e) {
			e.printStackTrace();
		}	
	}

}


