package br.com.connectDashboard.util;

import java.text.SimpleDateFormat;
import java.util.Calendar;



public class Datas {
	
	private Calendar calendar;
	private SimpleDateFormat sdf;
	
	
	public Datas() {
		calendar = Calendar.getInstance();
		sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");		
	}
	
	
	
	public String getDateFirstDayOfCurrentMonth() {
		calendar.set(Calendar.DAY_OF_MONTH, Calendar.getInstance().getActualMinimum(Calendar.DAY_OF_MONTH));
		calendar.set(Calendar.HOUR_OF_DAY, 0);
		calendar.set(Calendar.MINUTE, 0);
		calendar.set(Calendar.SECOND, 0);
		return (sdf.format(calendar.getTime()));
	}
	
	
	public String getDateLastDayOfCurrentMonth() {
		calendar.set(Calendar.DAY_OF_MONTH, calendar.getActualMaximum(Calendar.DAY_OF_MONTH));
		calendar.set(Calendar.HOUR_OF_DAY, 23);
		calendar.set(Calendar.MINUTE, 59);
		calendar.set(Calendar.SECOND, 59);
		return (sdf.format(calendar.getTime()));
	}


	
//	public static void main(String[] args) {
//		Datas datas = new Datas();
//		System.out.println(datas.getDateLastDayOfCurrentMonth());
//	}

	
	

}






	








