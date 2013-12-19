<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<html>
		<head>
			<link href="<c:url value="/css/jquery-ui.css"/>" rel="stylesheet" type="text/css" media="screen" />
			<script type="text/javascript" src="<c:url value="/js/jquery-1.9.1.js"/>"></script>
			<script type="text/javascript" src="<c:url value="/js/jquery-ui.js"/>"></script>
			<script type="text/javascript" src="<c:url value="/js/breezeReport.js"/>"></script>
			<script type="text/javascript" src="<c:url value="/js/bootstrap.min.js"/>"></script>
			<link href="<c:url value="/css/bootstrap.min.css"/>" rel="stylesheet" type="text/css" media="screen" />
			<link href="<c:url value="/css/bootstrap-theme.min.css"/>" rel="stylesheet" type="text/css" media="screen" />
			<script type="text/javascript" src="<c:url value="/js/jquery-ui-i18n.js"/>"></script>
			<script type="text/javascript" src="<c:url value="/js/bootstrap-inputmask.min.js"/>"></script>		  		    			  
		</head>
		
		
	
	
	<jsp:useBean id="persistenceDao" class="br.com.breezeReport.dao.PersistenceDaoDS"/>


 	<body onload="ativaDatePickers('${persistenceDao.breezeReportBeginDate}')" >
 
	
		
	<form action="graficosControl" method="post">	
	
		
	<div class="row">
  		
  		<div class="col-lg-2">
    		<div class="input-group">
    		 	<input type="text" placeholder="Selecione a data inicial" id="inputDataInicial" class="form-control" name="inputDataInicial" disabled="disabled">
    		
    		</div>
  		</div>
 
  		<div class="col-lg-2">
 		   <div class="input-group">
    			<input type="text" placeholder="Selecione a data final" id="inputDataFinal" class="form-control" name="inputDataFinal" disabled="disabled">
   			</div>
  		</div>
  		
  		<div>
			<button class="btn btn-default" type="submit" id="btn-pesquisar-sala">Submeter</button>
		</div>	
  		
	</div>
		
	
	</form>


<%-- ${param.busca} --%>
	
<%--  		<jsp:useBean id="persistenceDao" class="br.com.breezeReport.dao.PersistenceDao"/> --%>
<!-- 		 <table>												   -->
<%-- 			<c:forEach var="room" items="${persistenceDao.getRoomInfoByName(param.busca)}" varStatus="id"> --%>
<!-- 		    	<tr bgcolor="#${id.count % 2 == 0 ? 'aaee88' : 'ffffff' }"> -->
<%-- 		      		<td>${room.scoId}</td> --%>
<%-- 		      		<td>${room.nome}</td> --%>
<%-- 		      		<td>${room.url}</td> --%>
<%-- 		      		<td>${room.hosts}</td> --%>
<%-- 		      		<td>${room.apresentadores}</td> --%>
<%-- 		      		<td>${room.convidados}</td> --%>
<%-- 		      		<td>${room.total}</td> --%>
<%-- 		      		<td>${room.dataHora}</td> --%>
<%-- 		      		<td>${room.horaHost}</td> --%>
<%-- 		      		<td>${room.horaApresentador}</td> --%>
<%-- 		      		<td>${room.horaConvidado}</td> --%>
<!-- 		    	</tr> -->
<%-- 		 	</c:forEach> --%>
<!-- 	     </table> -->




	</body>
</html>