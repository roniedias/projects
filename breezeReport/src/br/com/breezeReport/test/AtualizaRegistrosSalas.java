package br.com.breezeReport.test;

import java.util.ArrayList;

import br.com.breezeReport.model.RoomInfo;
import br.com.breezeReport.util.RegistrosBD;

public class AtualizaRegistrosSalas {
	
	public static void main(String[] args) {
			
		RegistrosBD rbd = new RegistrosBD();
		
		// Desabilita a sala, adicionando à coluna "desabilitada" a data em que a mesma foi desativada
		rbd.checaEatualizaCampoDesabilitaSalas();
		
		// Adiciona na tabela room as salas que ainda não foram adicionadas (novas)
		rbd.adicionaSalasCasoAindaNaoExistamNaBase();
		
		// Adiciona na tabela roomInfo os registros novos, que ainda não foram  
		// adicionados para a data atual (de hoje)  
		ArrayList<RoomInfo> registrosAindaNaoEfetuadosHoje = new ArrayList<RoomInfo>();		
		registrosAindaNaoEfetuadosHoje = rbd.getDadosAindaNaoGravadosHoje();
		rbd.setRoomInfoRegistrosNaBase(registrosAindaNaoEfetuadosHoje);		
		
		// Atualiza os dados dos participantes, caso a query online traga valores maiores
		rbd.checaEAtualizaQuantidadeEDataParticipantes();
		
		// Fecha as conexões
		rbd.closeBreezeConnection();
		rbd.closeBreezeReportConnection();
			
	}
	
}
