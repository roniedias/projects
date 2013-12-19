package br.com.breezeReport.test;

import br.com.breezeReport.dao.PersistenceDao;
import br.com.breezeReport.model.User;


public class AdicionaUsuario {
	
	

		public static void main(String[] args) {
			
			User user = new User();
			
			user.setNome("admin");
			user.setSenha("admin");
			user.setEmail("admin@breezereport.com.br");
			
			PersistenceDao persistenceDao = new PersistenceDao();
			persistenceDao.addUser(user);
			
			System.out.println("Usuário " + user.getNome() + "adicionado com sucesso.");
			

		}

	

}
