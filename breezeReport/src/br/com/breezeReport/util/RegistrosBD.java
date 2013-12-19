package br.com.breezeReport.util;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import br.com.breezeReport.dao.OnLineDaoDS;
import br.com.breezeReport.dao.PersistenceDaoDS;
import br.com.breezeReport.model.OnlineInfo;
import br.com.breezeReport.model.Room;
import br.com.breezeReport.model.RoomInfo;

public class RegistrosBD {
	
	
	private OnLineDaoDS onLineDaoDS;
	private ArrayList<OnlineInfo> onlineGeneratedInfo;
	private ArrayList<Room> onlineDisabledRooms;
	private PersistenceDaoDS persistenceDaoDS;
	
	private ArrayList<RoomInfo> persistenceRoomsInfo;
	
	private ArrayList<Room> persistenceRooms;
	
	
	private Date date;
	private DateFormat formatter;
	private String hoje;


	

	public RegistrosBD() {
		
		this.onLineDaoDS = new OnLineDaoDS();
		
		this.onlineGeneratedInfo = new ArrayList<OnlineInfo>();
		this.onlineGeneratedInfo = onLineDaoDS.generateRoomsInfo();
		
		this.onlineDisabledRooms = new ArrayList<Room>();
		this.onlineDisabledRooms = onLineDaoDS.generateDisabledRoomsInfo();
		
		this. persistenceDaoDS = new PersistenceDaoDS();
		
		this.persistenceRoomsInfo = new ArrayList<RoomInfo>();
		this.persistenceRoomsInfo = persistenceDaoDS.getAllRoomsInfo();
		
		this.persistenceRooms = new ArrayList<Room>();
		this.persistenceRooms = persistenceDaoDS.getAllRooms();
		
		this.date = Calendar.getInstance().getTime();
		this.formatter = new SimpleDateFormat("dd/MM/yyyy");
		this.hoje = formatter.format(date); // Data do dia de hoje

	}
	
	
	public void adicionaSalasCasoAindaNaoExistamNaBase() {
		for(OnlineInfo o : onlineGeneratedInfo) {
			if(!persistenceDaoDS.isRoomAlreadyRecorded(o.getScoId())) {
				//persistenceDao.addRoom(new Room(o.getScoId(), o.getNome(), ConverteTimestamp.toDbTimestamp(Calendar.getInstance()), null));
				persistenceDaoDS.addRoom(new Room(o.getScoId(), o.getNome(), null));
			}
		}
	}
	
	
		
	// Obtém-se um array contendo todos os registros que já existem na base, com a data de hoje
	public ArrayList<RoomInfo> getRegistrosJaEfetuadosHoje() {   
		
		
		ArrayList<RoomInfo> registrosJaEfetuadosHoje = new ArrayList<RoomInfo>();
		
		for(RoomInfo r : persistenceRoomsInfo) {
			if(isRegistroHojeEncontradoNaBase(r.getScoId()))
				registrosJaEfetuadosHoje.add(getRegistroHojeDaBase(r.getScoId()));
		}
		return registrosJaEfetuadosHoje;	
	}
	
	
	// Obtém-se um array contendo as informações de todas as salas que ainda não foram gravadas na base, na data de hoje 	
	public ArrayList<RoomInfo> getDadosAindaNaoGravadosHoje() {    
		
		ArrayList<RoomInfo> dadosAindaNaoGravadosHoje = new ArrayList<RoomInfo>();
		
		for(OnlineInfo o : onlineGeneratedInfo) {
			if(!isRegistroHojeEncontradoNaBase(o.getScoId())) {
				
				dadosAindaNaoGravadosHoje.add(new RoomInfo(o.getScoId(), o.getHosts(), o.getApresentadores(), o.getConvidados(), o.getTotal(), o.getDataHora(), o.getHoraHost(), o.getHoraApresentador(), o.getHoraConvidado()));
				System.out.println("Adicionado o registro: " + o.getScoId() + " " + o.getNome() + " hosts = " + o.getHosts() + " apresentadores = " + o.getApresentadores() + " convidados = " + o.getConvidados());
			}
				
		}
		return dadosAindaNaoGravadosHoje;	
	}	
	

	// Grava na base os registros (ArrayList) passados como parâmetro
	public void setRoomInfoRegistrosNaBase(ArrayList<RoomInfo> registros) { 
		for(RoomInfo r : registros) {
			persistenceDaoDS.addRoomInfo(r);
		}
	}
		
	
	// Para a data atual (data de hoje), este método verifica se os números de Hosts, Apresentadores e Convidados
	// obtidos a partir da base do Breeze (registros on-line) são maiores que os números já registrados no banco. 
	// Em caso afirmativo, os registos do banco são atualizados (quantidade adquirida online e data/hora atual) 
	// para cada um deles
	
	public void checaEAtualizaQuantidadeEDataParticipantes() {
		
		ArrayList<RoomInfo> registrosJaEfetuadosHoje = new ArrayList<RoomInfo>();
		registrosJaEfetuadosHoje = getRegistrosJaEfetuadosHoje();
	
		
		
		for(OnlineInfo o: onlineGeneratedInfo) {
			for(RoomInfo r : registrosJaEfetuadosHoje) {
				
				boolean atualizaTotal = false;
				
				if(r.getScoId().equals(o.getScoId())) {
				
					if(r.getHosts() < o.getHosts()) {
						System.out.println("SCO ID " + r.getScoId() + ": Numero de hosts atualizado de " + r.getHosts() + " para " + o.getHosts());
						r.setHosts(o.getHosts());
						r.setHoraHost(ConverteTimestamp.toDbTimestamp(Calendar.getInstance()));
						atualizaTotal = true;
					}
	
					if(r.getApresentadores() < o.getApresentadores()) {
						System.out.println("SCO ID " + r.getScoId() + ": Numero de apresentadores atualizado de " + r.getApresentadores() + " para " + o.getApresentadores());
						r.setApresentadores(o.getApresentadores());
						r.setHoraApresentador(ConverteTimestamp.toDbTimestamp(Calendar.getInstance()));
						atualizaTotal = true;
					}
					
					if(r.getConvidados() < o.getConvidados()) {
						System.out.println("SCO ID " + r.getScoId() + ": Numero de convidados atualizado de " + r.getConvidados() + " para " + o.getConvidados());
						r.setConvidados(o.getConvidados());
						r.setHoraConvidado(ConverteTimestamp.toDbTimestamp(Calendar.getInstance()));
						atualizaTotal = true;
					}
				
					if(atualizaTotal) {	
						int totalOld;
						totalOld = r.getTotal();
						
						r.setTotal(r.getHosts() + r.getApresentadores() + r.getConvidados());
						persistenceDaoDS.updateRoomInfo(r);
						
						System.out.println("Total atualizado de " + totalOld + " para " + r.getTotal());
					}	
				}	
			}	
		}
	}
	
	
	// Rotina que desabilita as salas, adicionando a data em que as mesmas foram desativadas
	public void checaEatualizaCampoDesabilitaSalas() {
		for(Room o : onlineDisabledRooms) {
			for(Room p : persistenceRooms) {
				if(o.getScoId().equals(p.getScoId()) && o.getDesabilitada() != null) {
					p.setDesabilitada(o.getDesabilitada());
					persistenceDaoDS.updateRoom(p);
					System.out.println("Sala " + p.getNome() + "(scoId: " + p.getScoId() + " ) sendo desabilitada.");
				}
			}
		}
	}
	
	
	
	
//	public String getDatadeInicioEFimSala(String nome) {
//		String beginDate = persistenceDao.getRoomBeginDate(nome);
//		String endDate = persistenceDao.getRoomEndDate(nome);
//		
//		return beginDate + "_" + endDate; 	
//	}
	
	
	
	// Método auxiliar: Retorna um boolean informando se já foi gravado um registro com o scoId informado, 
	// na data atual (de hoje). 
	private boolean isRegistroHojeEncontradoNaBase(long scoId) {

		boolean localizado = false;
		
		ArrayList<RoomInfo> registersForSco = new ArrayList<RoomInfo>();
		registersForSco = persistenceDaoDS.getRoomInfoBySco(scoId);
		
		for(RoomInfo r : registersForSco) {
			if(hoje.equals(ConverteTimestamp.getDate(r.getDataHora()))) 
				localizado = true;
		}
	
		return localizado;
	}
	
	

	// Método auxiliar: Retorna o registro da data atual (de hoje), contendo o ScoId informado 
	private RoomInfo getRegistroHojeDaBase(long scoId) {
		
		ArrayList<RoomInfo> registersForSco = new ArrayList<RoomInfo>();
		registersForSco = persistenceDaoDS.getRoomInfoBySco(scoId);
		
		RoomInfo roomFound = new RoomInfo();
		
		for(RoomInfo r : registersForSco) {
			if(hoje.equals(ConverteTimestamp.getDate(r.getDataHora()))) {
				roomFound = r;
				break;
			}
		}
		
		return roomFound;
		
	}
		

	
	public void closeBreezeConnection() {
		onLineDaoDS.closeBreezeConnection();
	}
	

	public void closeBreezeReportConnection() {
		persistenceDaoDS.closeBreezeReportConnection();
	}
	
	
	
}




