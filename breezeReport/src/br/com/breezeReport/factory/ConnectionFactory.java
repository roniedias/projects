package br.com.breezeReport.factory;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;



public class ConnectionFactory {
	
//	public Connection getBreezeConnection() {
//		try {
//				try {
//					Class.forName("com.mysql.jdbc.Driver");
//				} catch (ClassNotFoundException e) {
//					e.printStackTrace();
//				}
//			return DriverManager.getConnection("jdbc:sqlserver://172.16.90.89:1433;user=connect;databaseName=breeze;password=totvs");
//	    } 
//		catch (SQLException e) {
//			throw new RuntimeException(e);
//	    }
//	}
	
	
	public Connection getBreezeReportConnection() {
		try {
			//return DriverManager.getConnection("jdbc:sqlserver://172.16.90.89:1433;user=connect;databaseName=breezeReport;password=totvs");
				try {
					Class.forName("com.mysql.jdbc.Driver");
				} catch (ClassNotFoundException e) {
					e.printStackTrace();
				}  
			return DriverManager.getConnection("jdbc:mysql://localhost/breezeReport", "root", "");
	    } 
		catch (SQLException e) {
			throw new RuntimeException(e);
	    }
	}
	

		
}

