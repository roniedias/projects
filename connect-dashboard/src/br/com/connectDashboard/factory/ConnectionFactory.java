package br.com.connectDashboard.factory;

import java.sql.Connection;
import java.sql.SQLException;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.sql.DataSource;

public class ConnectionFactory {
	
	public Connection getConnection() {
	
		Connection result = null;
	
	    try {
	    	Context initialContext = new InitialContext();
	    	
	    	DataSource datasource = (DataSource)initialContext.lookup("java:comp/env/jdbc/breeze");
	    
	    	if (datasource != null) {
	    		result = datasource.getConnection();
	    	}
	    	else {
	    		System.out.println("Falha durante o datasource lookup.");
	    	}
	    }
	    catch ( NamingException e ) {
	    	e.printStackTrace();
	    }
	    catch(SQLException e){
	    	e.printStackTrace();
	    }
	    return result;
	  }

}