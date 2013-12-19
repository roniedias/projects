package br.com.breezeReport.deprecated;

import java.util.ArrayList;
import java.util.Calendar;

import br.com.breezeReport.model.RoomInfo;
import br.com.breezeReport.util.ConverteTimestamp;
import br.com.breezeReport.util.RegistrosBD;

public class DSFill {
	
	public DSFill() {
		
		while (true) {
			
			
			System.out.println(ConverteTimestamp.toBzFormat(Calendar.getInstance()) + "  Rotina \"Breeze Report DB Fill\" executada");

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
			
			try {
				Thread.sleep(60000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			} 
			
		}
				
	}

}

