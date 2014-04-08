<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>
<html>
	<head>
		<title>Connect Dashboard 1.0</title>
		<meta charset="utf-8">
		
		<link rel="stylesheet" href="css/estilos.css">
		<script type="text/javascript" src="js/jquery-1.9.1.js"></script>
		<script type="text/javascript" src="js/jquery-ui-1.10.3.custom.js"></script>		
	    <link rel="stylesheet" href="css/jquery-ui-1.10.3.custom.css">
	    <link rel="stylesheet" href="css/bootstrap.min.css">
		<link rel="stylesheet" href="css/bootstrap-theme.min.css">
		<script type="text/javascript" src="js/bootstrap.min.js"></script>
		<script type="text/javascript" src="js/script.js"></script>		
 
	</head>
	<body id="body-login">
				
		<form id="form-login" action="efetuaLogin" method="post">		
			<div class="panel panel-default" id="panel-login">
  			
  				<div class="panel-body">
  				
					<table id="tbl-login">
					
						<tr>
							<td class="td-image-logo"><img src="images/logo.png"/></td>
						</tr>	
						<tr>
							<td class="td-input-usr"><input type="text" placeholder="Usuário" class="form-control" id="input-login-usuario" name="input-login-usuario" onclick="hideLblLoginFail()"></td>					
						</tr>
						<tr>
							<td class="td-input-pass"><input type="password" placeholder="Senha" class="form-control" id="input-login-senha" name="input-login-senha" onclick="hideLblLoginFail()"></td>
						</tr>
						<tr>
							<td class="td-btn-sbmt"><button type="submit" class="btn btn-primary" id="btn-login">Acessar Connect Dashboard </button></td>
						</tr>	
						<tr>	
    						<td class="td-lbl-login"><span class="label label-danger" id="lbl-login-fail" style="visibility: hidden">Usuário e/ou senha inválido(s)</span></td>
						</tr>
					</table>
				</div>
			</div>	
		</form>
	</body>
	
	
	
</html>