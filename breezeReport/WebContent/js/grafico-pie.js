    function desenhaPieChart(totalHosts, totalApresentadores, totalConvidados, totalGeral, nomeSala) {
    	
	    var hosts = parseInt(totalHosts);
	    var apresentadores = parseInt(totalApresentadores);
	    var convidados = parseInt(totalConvidados);

        // Cria a tabela de dados.
        var data = new google.visualization.DataTable();
        data.addColumn('string', 'Topping');
        data.addColumn('number', 'Slices');
        
        data.addRows([ ['Hosts ('+ totalHosts + ')', hosts], ['Apresentadores (' + totalApresentadores + ')', apresentadores], ['Convidados (' + totalConvidados + ')', convidados] ]);
        
        // Configura opções do gráfico
        var options = {'title':nomeSala + ". Total: " + totalGeral, 'width':600, 'height':300};

        // Instancia e desenha o gráfico, passando algumas opções
        var chart = new google.visualization.PieChart(document.getElementById('chart_div'));
        chart.draw(data, options);
        
      }
