package br.com.breezeReport.test;

import java.util.ArrayList;

import br.com.breezeReport.model.RoomInfo;
import br.com.breezeReport.util.RegistrosBD;

public class AtualizaRegistrosSalas {
	
	public static void main(String[] args) {
			
		RegistrosBD rbd = new RegistrosBD();
		
		// Desabilita a sala, adicionando � coluna "desabilitada" a data em que a mesma foi desativada
		rbd.checaEatualizaCampoDesabilitaSalas();
		
		// Adiciona na tabela room as salas que ainda n�o foram adicionadas (novas)
		rbd.adicionaSalasCasoAindaNaoExistamNaBase();
		
		// Adiciona na tabela roomInfo os registros novos, que ainda n�o foram  
		// adicionados para a data atual (de hoje)  
		ArrayList<RoomInfo> registrosAindaNaoEfetuadosHoje = new ArrayList<RoomInfo>();		
		registrosAindaNaoEfetuadosHoje = rbd.getDadosAindaNaoGravadosHoje();
		rbd.setRoomInfoRegistrosNaBase(registrosAindaNaoEfetuadosHoje);		
		
		// Atualiza os dados dos participantes, caso a query online traga valores maiores
		rbd.checaEAtualizaQuantidadeEDataParticipantes();
		
		// Fecha as conex�es
		rbd.closeBreezeConnection();
		rbd.closeBreezeReportConnection();
			
	}
	
}
