package br.com.breezeReport.util;

import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

public class ConverteTimestamp {
	
	// Todos os métodos servem para converter a string obtida a partir banco de dados - coluna dataHora (datetime)

	public static Calendar toCalendar(String dataHora) {
		Timestamp dbSqlTimestamp = Timestamp.valueOf(dataHora);
		Date dbSqlConverted = new Date(dbSqlTimestamp.getTime());
		Calendar calendar = new GregorianCalendar();
		calendar.setTime(dbSqlConverted);
		return calendar;
	}
	

	public static Date toDate(String dataHora) {
		Timestamp dbSqlTimestamp = Timestamp.valueOf(dataHora);
		Date dbSqlConverted = new Date(dbSqlTimestamp.getTime());
		return dbSqlConverted;
	}
	
	
	public static String getDate(String dataHora) {
		Timestamp dbSqlTimestamp = Timestamp.valueOf(dataHora);
		Date dbSqlConverted = new Date(dbSqlTimestamp.getTime());
		DateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
        return formatter.format(dbSqlConverted); 
	}
	
	public static String getDate(Date dataHora) {
		DateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
		return formatter.format(dataHora);
	}
	
	public static String millisecondsToDate(long milliseconds) {
		Date date = new Date(milliseconds);
		DateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
		return formatter.format(date);
	}
	
	
	public static String getHour(String dataHora) {
		Timestamp dbSqlTimestamp = Timestamp.valueOf(dataHora);
		Date dbSqlConverted = new Date(dbSqlTimestamp.getTime());
		DateFormat formatter = new SimpleDateFormat("HH:mm:ss");
		return formatter.format(dbSqlConverted);
	}
	
	
	public static String toDbTimestamp(Calendar c) {
		DateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		return formatter.format(c.getTime());				
	}
	
	
	public static String getDateWithoutSlash(String dataHora) {
		Timestamp dbSqlTimestamp = Timestamp.valueOf(dataHora);
		Date dbSqlConverted = new Date(dbSqlTimestamp.getTime());
		DateFormat formatter = new SimpleDateFormat("ddMMyyyy");
        return formatter.format(dbSqlConverted); 
	}
	
	public static String addSlashesToDate(String dateWithoutSlashes) {
		String day = dateWithoutSlashes.substring(0, 2);
		String month = dateWithoutSlashes.substring(2, 4);
		String year = dateWithoutSlashes.substring(4, 8);
		String date = day + "/" + month + "/" + year;
		return date;
	}
	
	
	public static String addDiasToOnlyDate(String strData, int quantDias) {
		int dia = Integer.parseInt(strData.substring(0, 2));
		dia = dia + quantDias; 
		String inputRemainingBody = strData.substring(2);
		
		if(dia < 10)
			strData = "0" + String.valueOf(dia) + inputRemainingBody;
		else
			strData = String.valueOf(dia) + inputRemainingBody;
				
		return strData;
	}
	
	
	
	public static String onlyDateToDBDateTimeFormat(String strData) { // strData no formato dd/MM/yyyy
		String dia = strData.substring(0, 2);
		String mes = strData.substring(3, 5);
		String ano = strData.substring(6, 10);
		
		String dataFormatada = ano + "-" + mes + "-" + dia + " 00:00:00";
		
		return(dataFormatada);			
	}
	
	
	public static String dBDateTimeFormatToOnlyDate(String strDateTime) { // strDateTime no formato yyyy-MM-dd HH:mm:ss
		
		strDateTime = strDateTime.replaceAll("00:00:00", "");
		
		String dia = strDateTime.substring(8, 10);
		String mes = strDateTime.substring(5, 7);
		String ano = strDateTime.substring(0, 4);
		
		return dia + "/" + mes + "/" + ano; 		
	}
 

	public static String toBzFormat(Calendar c) {
		DateFormat formatter = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
		return formatter.format(c.getTime());
	}


	
	
	
	
	
//	public Static Date manipulaData(String dataSemFormato, int chave, int quantidade) {
//		
//	/*	Args possÌveis:
//      arg[0] = "20130906";
//	    arg[1] = 1;
//      arg[2] = 5; */
//		
//		String dia = dataSemFormato.substring(6, 8);
//		String mes = dataSemFormato.substring(4, 6);
//		String ano = dataSemFormato.substring(0, 4);
//		
//		String dataFormatada = ano + "-" + mes + "-" + dia + " 00:00:00";
//		
//		Timestamp dbSqlTimestamp = Timestamp.valueOf(dataFormatada);
//		Date dbSqlConverted = new Date(dbSqlTimestamp.getTime());
//		Calendar calendar = new GregorianCalendar();
//		calendar.setTime(dbSqlConverted);
//		
//		if(chave == 1) 
//			calendar.add(Calendar.DATE, quantidade);
//		
//		else if (chave == 2) 
//			calendar.add(Calendar.MONTH, quantidade);
//		
//		else if (chave == 3) 
//			calendar.add(Calendar.YEAR, quantidade);
//		
//		else {
//			System.out.println("Valor da chave inv·lido!");
//			System.exit(0);
//		}
//		
//		Date dataFormatoDate = calendar.getTime();
//		
//		return dataFormatoDate;
//	}

	
	
	
}
