package br.com.breezeReport.deprecated;

import java.util.ArrayList;

import br.com.breezeReport.dao.PersistenceDaoDS;
import br.com.breezeReport.model.RoomInfo;

public class BreezeReportDBFill implements Runnable {
	
	volatile static boolean running = true;
	
	
	@Override
	public void run() {
		
			
		ArrayList<RoomInfo> roomsInfo = new ArrayList<RoomInfo>();
		
		PersistenceDaoDS persistenceDao = new PersistenceDaoDS();
		roomsInfo = persistenceDao.getAllRoomsInfo();
				
		for(RoomInfo r : roomsInfo) {
			persistenceDao.addRoomInfo(r);
		}
		
		persistenceDao.closeBreezeReportConnection();
	}
	

	public static void main(String[] args) {
		
		while(running) {
			
			new Thread(new BreezeReportDBFill()).start();

			try {
				Thread.sleep(260000); // Espera 5 minutos, aproximadamente
			} catch (InterruptedException e) { 
				e.printStackTrace();
			}

		}
		
	}

	
}
