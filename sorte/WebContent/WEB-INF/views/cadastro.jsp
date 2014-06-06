<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>

<!DOCTYPE HTML>
<html>

	<head>
		<title>Cadastro</title>
		<meta http-equiv="Content-Type" content="text/html;charset=utf-8">		
 	 		<link href="<c:url value="/css/jquery-ui.css"/>" rel="stylesheet" type="text/css" media="screen" />
   			<script type="text/javascript" src="<c:url value="/js/jquery-ui.js"/>"></script>
			<script type="text/javascript" src="<c:url value="/js/script.js"/>"></script>
			<script type="text/javascript" src="<c:url value="/js/bootstrap.min.js"/>"></script>
			<link href="<c:url value="/css/bootstrap.min.css"/>" rel="stylesheet" type="text/css" media="screen" />
			<link href="<c:url value="/css/bootstrap-theme.min.css"/>" rel="stylesheet" type="text/css" media="screen" />
			<link rel="stylesheet" href="css/cadastro.css">
			
									  		    			  
	</head>

		<c:import url="cabecalho.jsp" />
    
		<form class="form-horizontal" id="registerHere" method='post' action=''>
						
			<div class="container" id="container-cadastro">
			
				<div class="text-center">
				    <h2><span class="label label-primary">Cadastro</span></h2>
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
				                <h4>Nome</h4>
				                <div class="col-xs-9"><input type="text" placeholder="Digite aqui o seu nome" class="form-control input-sm"></div>
				            </div>
		
		   		            <div class="form-group">
				                <h4>Sexo</h4>
				                <div class="col-xs-5">
						            <select class="btn btn-default">
	    								<option>Masculino</option>
	    								<option>Feminino</option>
	  								</select>
  								</div>        	
			                </div>
					            
		   		            <div class="form-group">
				                <h4>CPF</h4>
			                	<div class="col-xs-5"><input type="text" data-mask="999.999.999-99" placeholder="xxx.xxx.xxx-xx" class="form-control input-sm"></div>
			                </div>
				                
		   		            <div class="form-group">
				                <h4>E-mail</h4>
			                	<div class="col-xs-6"><input type="text" placeholder="Informe seu e-mail" class="form-control input-sm"></div>
			                </div>
				                
			                <div class="form-group">
				                <h4>Confirma&ccedil;&atilde;o e-mail</h4>
			                	<div class="col-xs-6"><input type="text" placeholder="Confirme seu e-mail" class="form-control input-sm"></div>
			                </div>
				                	                
		   		            <div class="form-group">
				                <h4>Data de nascimento</h4>
			                	<div class="col-xs-3"><input type="text" placeholder="dd/mm/yyyy" class="form-control input-sm"></div>
			                	<div class="text-center">	 
									<button class="btn btn-primary" >Seguinte &raquo;</button>
								</div>	
			                	
			                </div>
				                
				                
			            </div>
				            
			            <div class="tab-pane" id="dc-tab">
			            
		   		            <div class="form-group">
				                <h4>Telefone para contato</h4>
			                	<div class="col-xs-6"><input type="text" placeholder="+55 (xx) xxxx-xxxx" class="form-control input-sm"></div>
			                </div>			            
				            
					        <div class="form-group">
				                <h4>CEP</h4>
			                	<div class="col-xs-5"><input type="text" placeholder="_____-___" class="form-control"></div>
			                	<div class="text-left">	 
									<button class="btn btn-primary" >Pesquisar</button>
								</div>				                				                	
			                	
			                </div>
				                				                
			                <div class="form-group">
				                <h4>Endere&ccedil;o</h4>
			                	<div class="col-xs-9"><input type="text" placeholder="Informe aqui o seu endere&ccedil;o" class="form-control"></div>
			                </div>
				                			                
		   		            <div class="form-group">
				                <h4>Cidade</h4>
			                	<div class="col-xs-6"><input type="text" placeholder="Informe o nome da sua cidade" class="form-control"></div>
			                </div>
				                
			                <div class="form-group">
				                <h4>Estado</h4>
			                	<div class="col-xs-6"><input type="text" placeholder="informe o estado onde reside" class="form-control"></div>
			                </div>
				                
			                <div class="form-group">
				                <h4>Senha</h4>
			                	<div class="col-xs-6"><input type="password" placeholder="Senha" class="form-control"></div>
			                </div>
				                
			                <div class="form-group">
				                <h4>Confirme a senha</h4>
			                	<div class="col-xs-6"><input type="password" placeholder="Informe novamente sua senha" class="form-control"></div>
			                	<div class="text-center">	 
									<button class="btn btn-primary" >Criar Conta</button>
								</div>				                				                	
			                </div>
				                			            			            
			            </div>
			                
		        	</div>
		             
	            </div>
	                
			</div>					
	
			
		</form>    

</html>
