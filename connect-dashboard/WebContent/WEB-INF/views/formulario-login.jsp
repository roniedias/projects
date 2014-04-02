<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>
<html>
	<head>
		<title>Connect Dashboard 1.0</title>
		<meta charset="utf-8">
		<link rel="stylesheet" href="css/estilos.css">
	</head>
	<body>
		
		<c:import url="cabecalho.jsp"/>
			
			
			<div class="div-img-charts">
				<table>
					<tr>
						<td class="tdChart1"><img src="images/barChart.png" onmouseover="" title="Bar Chart"/></td>
						<td class="tdChart2"><img src="images/pieChart.png" onmouseover="" title="Pie Chart"/></td>
						<td class="tdChart3"><img src="images/vennChart.png" onmouseover="" title="Venn Chart"/></td>			
					</tr>
					<tr>
						<td class="tdChart4"><img src="images/lineChart.png" onmouseover="" title="Line Chart"/></td>
						<td class="tdChart5"><img src="images/scatterChart.png" onmouseover="" title="Scatter Chart"/></td>
						<td class="tdChart6"><img src="images/radarChart.png" onmouseover="" title="Radar Chart"/></td>					
					</tr>
					<tr>
						<td class="tdChart7"><img src="images/boxChart.png" onmouseover="" title="Box Chart"/></td>
						<td class="tdChart8"><img src="images/candlestickChart.png" onmouseover="" title="Candlestick Chart"/></td>
						<td class="tdChart9"><img src="images/compoundChart.png" onmouseover="" title="Compound Chart"/></td>					
					</tr>
					
				</table>
			</div>

		
	</body>
</html>