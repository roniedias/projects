<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>

<!DOCTYPE HTML>
<html>
	<head>
	  <title>Breeze Report 1.0</title>
	  <meta name="viewport" content="width=device-width, initial-scale=1.0">
			

			<link href="<c:url value="/css/jquery-ui.css"/>" rel="stylesheet" type="text/css" media="screen" />
			<script type="text/javascript" src="<c:url value="/js/jquery-1.9.1.js"/>"></script>
			<script type="text/javascript" src="<c:url value="/js/jquery-ui.js"/>"></script>
			<script type="text/javascript" src="<c:url value="/js/index-page.js"/>"></script>
 			<script type="text/javascript" src="<c:url value="/js/bootstrap.min.js"/>"></script>
			<link href="<c:url value="/css/bootstrap.min.css"/>" rel="stylesheet" type="text/css" media="screen" />
			<link href="<c:url value="/css/bootstrap-theme.min.css"/>" rel="stylesheet" type="text/css" media="screen" />
			<script type="text/javascript" src="<c:url value="/js/jquery-ui-i18n.js"/>"></script>
			<script type="text/javascript" src="<c:url value="/js/bootstrap-inputmask.min.js"/>"></script>		  		    			  
			
			
	</head>
	


<body onload="configElementosInit()">

<!-- <form action="graficos.jsp" method="get" id="preencheDadosSala"> -->

	<nav class="navbar navbar-default" ><h2>Breeze Report 1.0</h2><span class="label label-default">Relatórios disponíveis a partir de: ${breezeReportBeginDate}</span></nav>
	<div class="row">
	  <div class="col-lg-6">
	    <div class="input-group">
	      <input type="text" placeholder="Digite o nome de uma sala virtual" id="nomeSala" class="form-control" name="nomeSala">
	      <span class="input-group-btn">
	      		  	<button class="btn btn-default" id="btn-selecionar-sala" onclick="verificaExistenciaSala(document.getElementById('nomeSala').value)">Selecionar</button>
	      </span>
	    </div>
	  </div>
	</div>
		


<form action="totalGraficosController" method="post" id="preencheDadosSala">

	<br/><br/><br/>

	<div class="row">
  		<div class="col-lg-2">
    		<div class="input-group">
<!--     		 	<input type="text" placeholder="Selecione a data inicial" id="inputDataInicial" class="form-control" name="inputDataInicial" disabled="disabled"> -->
				<input type="text" placeholder="Selecione a data inicial" id="inputDataInicial" class="form-control" name="inputDataInicial">
    		</div>
  		</div>
 
  		<div class="col-lg-2">
 		   <div class="input-group">
<!--     			<input type="text" placeholder="Selecione a data final" id="inputDataFinal" class="form-control" name="inputDataFinal" disabled="disabled"> -->
    			<input type="text" placeholder="Selecione a data final" id="inputDataFinal" class="form-control" name="inputDataFinal" >
   			</div>
  		</div>
  		  		
	</div>
	
	<input type="hidden" id="scoId" name="scoId"/>
			
</form>




	<br/><br/><br/>
  		<div>
			<button class="btn btn-default" id="btn-submeter-calendario" onClick="validaDatas()">Submeter</button>
		</div>	

 
</body>
</html>