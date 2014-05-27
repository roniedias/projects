<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>

<!DOCTYPE HTML>
<html>

	<head>
		<title>Cadastro</title>		
 	 		<link href="<c:url value="/css/jquery-ui.css"/>" rel="stylesheet" type="text/css" media="screen" />
   			<script type="text/javascript" src="<c:url value="/js/jquery-ui.js"/>"></script>
			<script type="text/javascript" src="<c:url value="/js/script.js"/>"></script>
			<script type="text/javascript" src="<c:url value="/js/bootstrap.min.js"/>"></script>
			<link href="<c:url value="/css/bootstrap.min.css"/>" rel="stylesheet" type="text/css" media="screen" />
			<link href="<c:url value="/css/bootstrap-theme.min.css"/>" rel="stylesheet" type="text/css" media="screen" />
			<link rel="stylesheet" href="css/estilos.css">			  		    			  
	</head>

	
	
		<c:import url="cabecalho.jsp" />
    



		<form class="form-horizontal" id="registerHere" method='post' action=''>

						
			<div class="container" id="container-cadastro">
			
			<div class="text-center">
			    <h2><span class="label label-primary">Cadastro do Apostador</span></h2>
		    </div>
		    
		    <br/>
			
			
			<ul class="nav nav-pills nav-justified"  id="tab-principal">
	 			<li class="active"><a href="#dp-tab" data-toggle="tab">Dados Pessoais</a></li>
			  	<li><a href="#dc-tab" data-toggle="tab">Dados para contato</a></li>
			</ul>
			
			

					
				<div class="jumbotron" id="jumbo-cadastro">
				
				
					<div class="tab-content" id="cadastro-tab-content">
							
						<div class="tab-pane active" id="dp-tab">
						
		 		            <div class="form-group">
				                <h3>Nome</h3>
				                <div class="col-xs-9"><input type="text" placeholder="Informe aqui o seu nome" class="form-control"></div>
					            <div class="btn-group">
								  <button type="button" class="btn btn-primary dropdown-toggle" data-toggle="dropdown">
								  	Sexo <span class="caret"></span>
								  </button>
								  <ul class="dropdown-menu" >
								    <li><a href="#">Masculino</a></li>
								    <li><a href="#">Feminino</a></li>
								  </ul>
								</div>		            
				            </div>
				            
		   		            <div class="form-group">
				                <h3>CPF</h3>
			                	<div class="col-xs-5"><input type="text" placeholder="xxx.xxx.xxx-xx" class="form-control"></div>
			                </div>
			                
		   		            <div class="form-group">
				                <h3>E-mail</h3>
			                	<div class="col-xs-6"><input type="text" placeholder="Informe seu e-mail" class="form-control"></div>
			                </div>
			                
			                <div class="form-group">
				                <h3>Confirme seu e-mail</h3>
			                	<div class="col-xs-6"><input type="text" placeholder="Informe novamente seu e-mail" class="form-control"></div>
			                </div>
			                	                
		   		            <div class="form-group">
				                <h3>Data de nascimento</h3>
			                	<div class="col-xs-3"><input type="text" placeholder="dd/mm/yyyy" class="form-control"></div>
			                </div>
			                
		   		            <div class="form-group">
				                <h3>Telefone para contato</h3>
			                	<div class="col-xs-6"><input type="text" placeholder="+55 (xx) xxxx-xxxx" class="form-control"></div>
			                	<div class="text-center">	 
									<button class="btn btn-primary" >Seguinte &raquo;</button>
								</div>	
			                </div>
			                
			            </div>
			            
			            <div class="tab-pane" id="dc-tab">
			            
					        <div class="form-group">
				                <h3>CEP</h3>
			                	<div class="col-xs-5"><input type="text" placeholder="_____-___" class="form-control"></div>
			                </div>
			                
			                
			                <div class="form-group">
				                <h3>Confirme seu e-mail</h3>
			                	<div class="col-xs-6"><input type="text" placeholder="Informe novamente seu e-mail" class="form-control"></div>
			                </div>
			                
			                
		   		            <div class="form-group">
				                <h3>Cidade</h3>
			                	<div class="col-xs-6"><input type="text" placeholder="Informe o nome da sua cidade" class="form-control"></div>
			                </div>
			                
			                <div class="form-group">
				                <h3>Estado</h3>
			                	<div class="col-xs-5"><input type="text" placeholder="Informe aqui o nome do estado onde reside" class="form-control"></div>
			                </div>
			                
			                <div class="form-group">
				                <h3>Senha</h3>
			                	<div class="col-xs-6"><input type="password" placeholder="Senha" class="form-control"></div>
			                </div>
			                
			                <div class="form-group">
				                <h3>Confirme a senha</h3>
			                	<div class="col-xs-6"><input type="password" placeholder="Informe novamente sua senha" class="form-control"></div>
			                	<div class="text-center">	 
									<button class="btn btn-primary" >Criar Conta</button>
								</div>				                				                	
			                </div>
			                
			                
			                			            			            
			            </div>
		                
		        	</div>
	             
	            </div>
	                
					
	
			
		</form>    

</html>
