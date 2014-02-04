<!DOCTYPE html>
<html>
	<head>
		<title>3C Debugger 1.0</title>
		<meta charset="utf-8">

 
		<script type="text/javascript" src="js/jquery-1.9.1.js"></script>
		<script type="text/javascript" src="js/jquery-ui-1.10.3.custom.js"></script>
		
		<script type="text/javascript" src="js/script.js"></script>
		
	    <link rel="stylesheet" href="css/estilos.css">
	    <link rel="stylesheet" href="css/jquery-ui-1.10.3.custom.css">
	</head>
	
	<body onload="load()">
	
		<header class="header-class">
			<h1>3C - LOG DEBUGGER v. 1.0</h1>
			<input type="text" id="input-cliente" name="input-cliente" placeholder="Digite o código ou o nome do cliente"/>
			<button id="btn-pesquisa-cliente" onclick="getClientEnvTypes(document.getElementById('input-cliente').value)">Pesquisar</button>
		</header>

		
		<section>
				 
			<div class="section-class">						

 				<table>
					<tr>
						<td>
							<select id="select-tipos-amb" onchange="selectTiposAmbChange()"></select>
						</td>
						<td>
							<select id="select-produtos" onchange="selectProdutosChange()"></select>
						</td>
						<td>
							<select id="select-tipos-serv" onchange="selectTiposServChange()"></select>
						</td>
						<td>
							<button type="button" id="submit-btn" onclick="executeMonit()">Executar monitoramento</button>
						</td>				
					</tr>
				</table>
 				
 				<textarea rows="35" cols="100" id="txtArea" readonly></textarea>
				<button type="button" id="clear-txt-area-btn" onclick="reiniciar()">Reiniciar Log</button> 				 								
			</div>
			
        </section>

		
        <footer>
			<div class="footer-class"></div>
        </footer>
		
	</body>
</html>
