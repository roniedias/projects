package br.com.debugger3c.factory;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class ConnectionFactory {
	
	public Connection get3CConnection() {

		try {
			
			try {
				
				Class.forName("com.ibm.db2.jcc.DB2Driver");
			}
			catch (ClassNotFoundException e) {
				e.printStackTrace();
			}
			
			return DriverManager.getConnection("jdbc:db2://172.16.93.168:50000/DB3CPROD", "db2", "manager");
		}
		catch(SQLException e) {
			throw new RuntimeException(e);
		}
		
		
		
	}

}
