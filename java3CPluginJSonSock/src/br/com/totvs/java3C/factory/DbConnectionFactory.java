package br.com.totvs.java3C.factory;

import java.io.FileInputStream;
import java.io.IOException;
import java.sql.Connection;
//import java.sql.ResultSet;
import java.sql.SQLException;
//import java.sql.Statement;
import java.util.Properties;

import javax.sql.DataSource;

import org.apache.commons.dbcp2.ConnectionFactory;
import org.apache.commons.dbcp2.DriverManagerConnectionFactory;
import org.apache.commons.dbcp2.PoolableConnection;
import org.apache.commons.dbcp2.PoolableConnectionFactory;
import org.apache.commons.dbcp2.PoolingDataSource;
import org.apache.commons.pool2.ObjectPool;
import org.apache.commons.pool2.impl.GenericObjectPool;
       


/* Para o correto funcionamento desta classe, criar a pasta de projeto "resources" da seguinte maneira:
 * Project > Properties > Java Build Path. Na aba "Source", clicar em "Add Folder" e depois em 
 * "Create New Folder". Criar uma pasta de nome "resources".
 * Adicionar também as dependências (JARS externos) commons-dcp2-2.0.jar, commons-pool2-2.2.jar e db2jcc4.jar
 * ao projeto. 
 */



public class DbConnectionFactory {
	
	private static String DB_DRIVER_CLASS;
	private static String DB_URL;
	private static String DB_NAME;
	private static String DB_PORT;
	private static String DB_USERNAME;
	private static String DB_PASSWORD;
	
	
	public Connection getConnection() {
		
		// Rotina de leitura do arquivo db.properties para obter as informações de conexão com o banco de dados 
		Properties props = new Properties();
		FileInputStream fis = null;
		
		try {
//			fis = new FileInputStream("resources/db.properties");
			fis = new FileInputStream("/usr/lib/nagios/plugins/db.properties");
			props.load(fis);
			DB_DRIVER_CLASS = props.getProperty("DB_DRIVER_CLASS");
			DB_URL = props.getProperty("DB_URL");
			DB_PORT = props.getProperty("DB_PORT");
			DB_NAME = props.getProperty("DB_NAME");
			DB_USERNAME = props.getProperty("DB_USERNAME");
			DB_PASSWORD = props.getProperty("DB_PASSWORD");
		}
		catch (IOException e) {
			e.printStackTrace();
		}
		

		
		//Carregando o driver JDBC basico		
		try {
			Class.forName(DB_DRIVER_CLASS);
		}                  
		catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
		
		
		//Configurando o dataSource
		DataSource datasource = setupDataSource(DB_URL + ":" + DB_PORT + "/" + DB_NAME, DB_USERNAME, DB_PASSWORD);
		Connection conn = null;
		
		try {
			//Criando a conexao
			conn = datasource.getConnection();			
		}
		catch (SQLException e) {
			e.printStackTrace();
		} 
		
		return conn;
		
	}
	
	
	
	
	public static DataSource setupDataSource(String connectURI, String uname, String passwd) {
		
		// Criando a ConnectionFactory que o pool irá utilizar para criar conexões.
		//ConnectionFactory connectionFactory = new DriverManagerConnectionFactory(connectURI, null);
		ConnectionFactory connectionFactory = new DriverManagerConnectionFactory(connectURI, uname, passwd);
		
		// Criando a PoolableConnectionFactory, que contém as conexões "reais" criadas pela ConnectionFactory
		// com as classes que implementam a funcionalidade de pooling
		PoolableConnectionFactory poolableConnectionFactory = new PoolableConnectionFactory(connectionFactory, null);
			
		// Agora, é necessário um ObjectPool que servirá como o pool de conexões corrente. Foi utilizada uma
		// instância de GenericObjectPool, desta forma, qualquer implementação de ObjectPool poderá ser 
		// utilizada
		ObjectPool<PoolableConnection> connectionPool = new GenericObjectPool<PoolableConnection>(poolableConnectionFactory);
				
//		System.out.println("Active: " + connectionPool.getNumActive());
//		System.out.println("Idle: " + connectionPool.getNumIdle());
		
		// Configura as propriedades do pool para o pool dono
		poolableConnectionFactory.setPool(connectionPool);
		
		//Finalmente, criado o PoolingDriver passando o objeto pool criado
		PoolingDataSource<PoolableConnection> dataSource = new PoolingDataSource<PoolableConnection>(connectionPool);
		
		return dataSource;
		
	}
										
			
}
		
