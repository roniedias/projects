<!DOCTYPE html>
<html>
	<head>
		<title>Debugger 3C 1.1</title>
		<meta charset="utf-8">


		<script type="text/javascript" src="js/jquery-1.9.1.js"></script>
		<script type="text/javascript" src="js/jquery-ui-1.10.3.custom.js"></script>
		
		<script type="text/javascript" src="js/script.js"></script>
		<link rel="stylesheet" href="css/estilos.css">
		
	    <link rel="stylesheet" href="css/jquery-ui-1.10.3.custom.css">
	    
	    <link rel="stylesheet" href="css/bootstrap.min.css">
		<link rel="stylesheet" href="css/bootstrap-theme.min.css">
		<script src="js/bootstrap.min.js"></script>
		
	    
	    
	    
	</head>
	
	<body onload="load()">
	
		<header class="header-class" id="header">
		
	    	<table>

	  	    	<tr>
			    	<td>
			    		<div id="div-img-totvs">
			    			<input type="image" src="img/logo-totvs.png">
			    		</div>
			    	 </td>
					<td>
						<div id="div-img-3c">
							<input type="image" src="img/logo-3c.png">
						</div>
					</td>
					<td>
			  			<div class="col-lg-12" id="div-pesquisar">
		    				<div class="input-group">
		      					<input type="text" class="form-control" id="input-cliente" name="input-cliente" onclick="limparSelects()" placeholder="Digite o código ou nome do cliente">
		      					<span class="input-group-btn">
		        					<button class="btn btn-primary" type="button" id="btn-pesquisa-cliente" onclick="getClientEnvTypes(document.getElementById('input-cliente').value)">Pesquisar</button>
		      					</span>
		    				</div>
		  				</div>
					</td>
					
				</tr>
			</table>
		
		</header>
		
		
		<section class="section-class" id="section">
			
			<table>
				<tr>
					<td id="select-tipos-amb-td">
						<select class="form-control" id="select-tipos-amb" onchange="selectTiposAmbChange()"></select>
					</td>
					<td id="select-produtos-td">
						<select class="form-control" id="select-produtos" onchange="selectProdutosChange()"></select>
					</td>
					<td id="select-tipos-serv-td">
						<select class="form-control" id="select-tipos-serv" onchange="selectTiposServChange()"></select>
					</td>
					<td id="submit-btn-td">
						<button class="btn btn-primary" type="button" id="submit-btn" onclick="executeMonit()">Executar monitoramento</button>
					</td>
				</tr>
			</table>				
			
				
		
		
			<ul class="nav nav-pills nav-justified"  id="tab-principal">
	 			<li class="active"><a href="#leitura-tab-pane" data-toggle="tab">Leitura</a></li>
			  	<li><a href="#monitoramento-tab-pane" data-toggle="tab">Monitoramento</a></li>
			  	<li><a href="#escrita-tab-pane" data-toggle="tab">Escrita</a></li>
 			  	<li><a href="#javaplugin-tab-pane" data-toggle="tab">Java Plug-in</a></li>
			</ul>
						
			
			<div class="tab-content" id="logs-tab-content">
			
			  <div class="tab-pane active" id="leitura-tab-pane">
			      <textarea rows="23" cols="150" id="txt-area-leitura" readonly></textarea>
			  </div>
			  
			  <div class="tab-pane" id="monitoramento-tab-pane">
			      <textarea rows="23" cols="150" id="txt-area-monit" readonly></textarea>
			  </div>
			  
			  <div class="tab-pane" id="escrita-tab-pane">
			  	  <textarea rows="23" cols="150" id="txt-area-escrita" readonly></textarea>
			  </div>
			  
			  <div class="tab-pane" id="javaplugin-tab-pane">
			  	  <textarea rows="23" cols="150" id="txt-area-javaplugin" readonly></textarea>
			  </div>
			  
			  
			</div>
			
		</section>
		
		
		<div id="div-btn-primary">
			<button type="button" class="btn btn-primary" id="reload-btn" onclick="limparLogs()">Limpar Logs</button>
		</div>
	
		<footer class="footer-class" id="footer"></footer>
		
	</body>
	
	
	
	
</html>
	