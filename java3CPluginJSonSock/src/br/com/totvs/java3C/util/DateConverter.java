package br.com.totvs.java3C.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * @author Ronie Dias Pinto
 */


public class DateConverter {
	
	
	private String dateIn;
	private String formatIn;
	private String formatOut;

	private Date auxDate;

	private String dateOut;

	public DateConverter(String dateIn, String formatIn, String formatOut) {
		this.dateIn = dateIn;
		this.formatIn = formatIn;
		this.formatOut = formatOut;
	}

	public void setdateIn(String dateIn) {
		this.dateIn = dateIn;
	}
	public String getdateIn() {
		return dateIn;
	}
	
	public void setformatIn(String formatIn) {
		this.formatIn = formatIn;
	}
	public String getformatIn() {
		return formatIn;
	}
	
	public void setformatOut(String formatOut) {
		this.formatOut = formatOut;
	}
	public String getformatOut() {
		return formatOut;
	}
	
	public void setAuxDate(Date auxDate) {
		this.auxDate = auxDate;
	}
	public Date getAuxDate() {
		return auxDate;
	}
	
	

	public String convert() {		
		
		try {
			auxDate = new SimpleDateFormat(formatIn).parse(dateIn);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		dateOut = new SimpleDateFormat(formatOut).format(auxDate);

		return dateOut;
	}
	
	

}
