<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>


<html>
  <head>
	<script type="text/javascript" src="<c:url value="/js/jsapi.js"/>"></script>
	<script type="text/javascript" src="<c:url value="/js/graficos.js"/>"></script>
	<script type="text/javascript" src="<c:url value="/js/grafico-pie.js"/>"></script>
	
    <script type="text/javascript">
		google.load('visualization', '1.0', {'packages':['corechart']}); // Carrega a API de visualização de o pacote piechart.
	</script>
  </head>
  <body onload="desenhaPieChart('${totalHosts}', '${totalApresentadores}', '${totalConvidados}', '${totalGeral}', '${nome}')" > 
    <div id="chart_div"></div> <!-- Div que retém o gráfico -->
    <%-- ${dataInicial} ${dataFinal} ${scoId} ${nome} ${totalHosts} ${totalApresentadores} ${totalConvidados} ${totalGeral} --%>
  </body>
</html>







	
