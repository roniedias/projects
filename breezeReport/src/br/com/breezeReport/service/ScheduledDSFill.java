package br.com.breezeReport.service;


import org.springframework.scheduling.annotation.Scheduled;

import java.util.ArrayList;
import java.util.Calendar;

import br.com.breezeReport.model.RoomInfo;
import br.com.breezeReport.util.ConverteTimestamp;
import br.com.breezeReport.util.RegistrosBD;


public class ScheduledDSFill {

	 @Scheduled(fixedDelay = 60000)  
	 public void service()
	 {

			System.out.println(ConverteTimestamp.toBzFormat(Calendar.getInstance()) + " Executando agendamento... ");

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
			
			System.out.println(ConverteTimestamp.toBzFormat(Calendar.getInstance()) + " Atualiza��o dos dados realizada com sucesso.");

	 }
	 
	 
	 
	 
	 


	 			
	 				


	 
	 
	 
	 
	 
	 
	 

}
