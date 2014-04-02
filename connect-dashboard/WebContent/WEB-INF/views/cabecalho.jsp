<!DOCTYPE html>
<html>
	<head>
		<title>Connect Dashboard 1.0</title>
		<meta charset="utf-8">
		
  		<script type="text/javascript" src="js/jsapi.js"></script> 

  	    <script type="text/javascript"> 
    			google.load('visualization', '1.0', {'packages':['corechart']}); // Carrega a API de visualização 
  	    </script>
 
		<script type="text/javascript" src="js/jquery-1.9.1.js"></script>
		<script type="text/javascript" src="js/jquery-ui-1.10.3.custom.js"></script>
		
		
		<link rel="stylesheet" href="css/estilos.css">
		
	    <link rel="stylesheet" href="css/jquery-ui-1.10.3.custom.css">
	    
	    <link rel="stylesheet" href="css/bootstrap.min.css">
		<link rel="stylesheet" href="css/bootstrap-theme.min.css">
		<link rel="stylesheet" href="css/datepicker3.css">
		<script type="text/javascript" src="js/bootstrap.min.js"></script>
  	    <script type="text/javascript" src="js/bootstrap-datepicker.js" charset="UTF-8"></script>
 		<script type="text/javascript" src="js/locales/bootstrap-datepicker.pt-BR.js" charset="UTF-8"></script>
 		
 		    
	</head>
	
	<body>
		<header class="header-class" id="header">
			<table>				
				<tr>		
					<td class="logo-img-td">
						<img src="images/logo-totvs.png" id="logo-img" onclick="recarregar()">
					</td>		
					<td class="header-login-td">
						<div class="input-group" id="login-group-div">				
							<form class="navbar-form navbar-right" id="form-login" action="efetuaLogin" method="post">
				    		     <div class="form-group">
	              					<input type="text" placeholder="Usuário" class="form-control" id="input-login-usuario" name="input-login-usuario">
	            				</div>
	            				<div class="form-group">
	              					<input type="password" placeholder="Senha" class="form-control" id="input-login-senha" name="input-login-senha">
	            				</div>
	            				<button type="submit" class="btn btn-primary" id="btn-login">Acessar</button>
	          				</form>							
						</div>
					</td>	
				</tr>
			</table>
		</header>
		
		<!-- <button type="button" class="btn btn-primary" id="btn-logout" onclick="logout()">Logout</button> -->
		
	</body>

</html>