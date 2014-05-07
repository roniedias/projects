<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

<!DOCTYPE html>
<html>
	<head>
		<script type="text/javascript" src="js/jquery-1.9.1.js"></script>
		<script type="text/javascript" src="js/jquery-ui-1.10.3.custom.js"></script>
		
		<script type="text/javascript" src="js/script.js"></script>
		<link rel="stylesheet" href="css/estilos.css">
		
	    <link rel="stylesheet" href="css/jquery-ui-1.10.3.custom.css">
	    	    
	</head>
	
	<body>
	
		<c:import url="cabecalho.jsp" />
				
		<div id="div-section">
		
			<section class="section-class" id="section">
			
				<ul class="nav nav-pills" id="tab-principal">
		 			<li class="active"><a href="#online-tab-pane" data-toggle="tab">Online</a></li>
		 			<li><a href="#relatorios-tab-pane" data-toggle="tab">Relatórios</a></li>
				  	<li><a href="#graficos-tab-pane" data-toggle="tab">Gráficos</a></li>
				  	<li><a href="#apresentacao-graficos-tab-pane" data-toggle="tab">Apresentação Gráficos</a></li>				  	
				</ul>
				
				<div class="tab-content" id="tab-content">
								
				  <!-- Início tab online -->
				  <div class="tab-pane active" id="online-tab-pane">
				    
 			  		<div class="panel panel-default" id="panel-online">
	  					<div class="panel-heading">SALAS ON-LINE</div>
	  					
	  					<div class="div-btn-atualizar-salas-online"><button class="btn btn-primary" type="button" id="btn-atualizar-salas-online" onclick="getUsuariosOnlineSalaJSON()" class="right">Atualizar</button></div>
	  					  									
	  					<div class="panel-body" id="panel-body-online"><span class="label label-primary" id="sem-sala-online-lbl" style="visibility: hidden">NENHUMA SALA EM USO NO MOMENTO</span></div>

					</div>
				  </div>
				  <!-- Fim da tab online -->
								  
				  <!-- Início tab Relatórios -->								  
				  <div class="tab-pane" id="relatorios-tab-pane">
				  	<div class="panel panel-default" id="panel-relatorios">
				  		<div class="panel-heading" id="heading-relatorios">
				  			<table>
					  			<tr>
					  				<td class="relatorios-nome">RELATÓRIOS</td>
<!-- Não em uso atualmente			<td class="relatorios-print-img"><img src="images/printer.png" alt="Imprimir Relatório" id="imgImpressora" onClick="imprimeRelatorioSelecionado()" title="Imprimir" onmouseover="" style="cursor: pointer"/></td> -->
					  			</tr>
				  			</table>
				  		</div>
				  		
				  			<div class="div-dropdown-btn-relatorios">
				  				<table>
				  					<tr>
				  						<td class="relat-dropdown-td">
											<div class="btn-group">
											    <button type="button" class="btn btn-primary dropdown-toggle" data-toggle="dropdown" id="dropdown-btn-relatorios">Tipo de Relatório<span class="caret"></span></button>
											    <ul class="dropdown-menu">
			
											      <li><a href="javascript:geraRelatorioSalas('salasFixas')">Salas Fixas</a></li>
											      <li><a href="javascript:geraRelatorioSalas('todasSalas')">Todas as Salas</a></li>
											      <li><a href="javascript:geraRelatorioSalas('salasMesCorrente')">Salas em Uso (Mês Corrente)</a></li>
			 								    </ul> 
											  </div>
										</td>
								  		<td class="relat-imgmail-td">
										    <figure class="fig-mail-geral-relat">
										      <img src="images/mail.png" id="imgEmailGeralRelat" onmouseover="" title="Enviar e-mail" onclick="enviarEmailHostsSala()">
										    </figure>
										</td>
										<td class="relat-imgfile-td">
											<figure class="fig-arquivo-geral-relat">
										      <img src="images/file.png" id="imgArquivoGeralRelat" onmouseover="" title="Gerar arquivo CSV" onclick="geraCSVHosts()">
										    </figure>
										</td>
								  	</tr>
								</table>
							</div>				  		
				  		
				  		<div class="panel-body" id="panel-body-relatorios"><span class="label label-primary" id="relatorios-lbl">NENHUM RELATÓRIO GERADO ATÉ O MOMENTO</span>
				  		
				  			<table id="table-relatorios"></table>
				  		
				  		</div>
				  	</div>
				  </div>
				  
				  <!-- Fim tab Relatórios -->				  
				  				  
				  <jsp:useBean id="dao" class="br.com.connectDashboard.dao.Dao"/>
				  
				  
				  <!-- Início da tab Gráficos -->
				  <div class="tab-pane" id="graficos-tab-pane">
				  	<div class="panel panel-default" id="panel-graficos">				  		
	  					<div class="panel-heading">GERAÇÃO DE GRÁFICOS (PICOS DE USO)</div>
	  						<div class="div-tbl-pesquisa-salas">
		  						<table id="tbl-pesquisa-salas" id="table-panel-graficos">
		  							<tr>
	 	  								<td class="pesquisa-salas-nome"><h4>Pesquisar outras salas:</h4></td>
		  								<td class="pesquisa-salas-data-inicio">
									    	<input type='text' class="form-control" id='input-data-inicio' placeholder="Data inicial (dd/mm/yyyy)" style="cursor :default" readonly="readonly"/>
		  								</td>
		  								<td class="pesquisa-salas-data-fim">
		  									<input type='text' class="form-control" id='input-data-fim' placeholder="Data final (dd/mm/yyyy)" style="cursor :default" readonly="readonly"/>
		  								</td>
		  								<td class="pesquisa-salas-input">
	
	 							  			<div class="col-lg-12" id="div-executar">
	 						    				<div class="input-group">
<!-- 	 						      					<input type="text" class="form-control" id="input-todas-salas" name="input-todas-salas" placeholder="Digite o nome do cliente" onClick="limpaInputTodasSalas()"> -->
	 						      					<input type="text" class="form-control" id="input-todas-salas" name="input-todas-salas" placeholder="Digite o nome da sala virtual">
	 						      					<span class="input-group-btn">
	 						        					<button class="btn btn-primary" type="button" id="btn-executar" onclick="desenhaGraficoSalaEspecifica(document.getElementById('input-data-inicio').value, document.getElementById('input-data-fim').value, document.getElementById('input-todas-salas').value)">Executar</button>
	 						      					</span>
	 						    				</div> 
	 						  				</div> 
		  								  								
		  								</td>
		  							</tr>
		  						</table>
	  						</div>
	  					
	  					<div class="panel-body" id="panel-body-graficos">
						    	
						    	<table class="table" id="table-salas-em-uso-mes-atual">
									<tr>
										<th>SALAS EM USO (MÊS CORRENTE)</th>
									</tr>	
			  						<c:forEach var="salasEmUsoNoMesAtual" items="${dao.salasEmUsoNoMesAtual}" varStatus="id">
			    						<tr bgcolor="#${id.count % 2 == 0 ? 'f6f4f0' : 'ffffff' }" >
			      							<td>
			       								<a href="javascript:desenhaGraficoSala('${salasEmUsoNoMesAtual.scoId}', '${salasEmUsoNoMesAtual.nome}')">${salasEmUsoNoMesAtual.nome}</a>
			      							</td>
									    </tr>
			  						</c:forEach>
								</table>
																	
						</div>			           
				    </div>
	
				  </div>
				  <!-- Fim da tab Gráficos -->
				  
				  
				  <!-- Início da tab Apresentação -->
				  <div class="tab-pane" id="apresentacao-graficos-tab-pane">
				  	<div class="panel panel-default" id="panel-apresentacao-graficos">				  		
	  					<div class="panel-heading">UTILIZAÇÃO DA SALA</div>			
	  						<div class="panel-body" id="panel-body-apresentacao-graficos"><span class="label label-primary" id="sem-grafico-lbl">NENHUM GRÁFICO GERADO ATÉ O MOMENTO</span></div>
				    </div>
				  </div>
				  <!-- Fim da tab Apresentação -->
				  				  
								
			    </div>	
						
			</section>
		
		</div>
		
		
<!-- 		<div id="div-footer"> -->
		
<!-- 			<footer class="footer-class" id="footer"> -->
<!-- 	        </footer> -->
	        
<!-- 	    </div> -->
		
		
		
	</body>
	
</html>
	