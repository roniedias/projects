package br.com.breezeReport.deprecated;



import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;



public class DSFillServlet extends HttpServlet {
	

	volatile static boolean running = true;
	
	private static final long serialVersionUID = 1L;

	public void init() throws ServletException {
		
		
		new Thread(new Runnable() {
			
			public void run() {
				new DSFill();
			}

		}).start();
				
		
	}
	
	
	
	
}
