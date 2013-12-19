  
	 function preencheAutocomplete(items) {	 
		 $(" #nomeSala ").autocomplete({
			source: items
		 });
	 };
	 
  	 
	function getNomesJSON() {
		var items = [];
	  		$.getJSON("allRooms.json", function(data) {
				$.each(data, function(key, val) {
					items.push(val.nome);
				});
				preencheAutocomplete(items);
	  		});
	};  
	
	getNomesJSON();
	
	
	

	function ativaDatePickers(dataInicial, dataFinal) {
		
		
		
		document.getElementById("btn-selecionar-sala").style.visibility="hidden";
		
		$( "#inputDataInicial" ).val("");
		$( "#inputDataFinal" ).val("");

		
		
		var beginDay = dataInicial.substring(8, 10);
		var beginMonth = dataInicial.substring(5, 7);
		var beginYear = dataInicial.substring(0, 4);
		
		var endDay = dataFinal.substring(8, 10);
		var endMonth = dataFinal.substring(5, 7);
		var endYear = dataFinal.substring(0, 4);
				
		

		
		$.datepicker.setDefaults( $.datepicker.regional["pt-BR"]);
		$.datepicker.setDefaults({showOn: "both", buttonImageOnly: true, buttonImage: "css/images/calendar.gif", minDate: new Date(beginYear, beginMonth - 1, beginDay), maxDate: new Date(endYear, endMonth - 1, endDay) });
	
		
		$( "#inputDataInicial" ).datepicker({dateFormat: "dd/mm/yy", defaultDate: "+1w", changeMonth: true, numberOfMonths: 2, onClose: 
			function( selectedDate ) {
				$( "#inputDataFinal" ).datepicker( "option", "minDate", selectedDate );
			}
		});

		$( "#inputDataFinal" ).datepicker({dateFormat: "dd/mm/yy", defaultDate: "+1w", changeMonth: true, numberOfMonths: 2, onClose: 
			function( selectedDate ) {
				$( "#inputDataInicial" ).datepicker( "option", "maxDate", selectedDate );
			}
		});	
		
	}
	


	
	function verificaExistenciaSala(nome) {
			
			var retorno = false;
			var dataInicial = null;
			var dataFinal = null;
			var scoId = 0;
			
			
	  		$.getJSON("allRooms.json", function(data) {
				$.each(data, function(key, val) {
					if(val.nome == nome) {
						retorno = true;
						dataInicial = val.dataInicial;
						dataFinal = val.dataFinal;
						scoId = val.scoId;
					}
				});
				if(retorno) {
					//Executado quando a sala existir
					document.getElementById('scoId').value = scoId; 
					document.getElementById("btn-submeter-calendario").style.visibility="visible";
					document.getElementById("nomeSala").setAttribute('readonly', true);
					ativaDatePickers(dataInicial, dataFinal);
					
				}
				else {
					// Executado quando a sala não existir
					alert("Sala inexistente.");
				}
	  		});
	  		
		}
	
	
	function configElementosInit() {
		// Executado logo que a página é carregada
		
		
		$( "#inputDataInicial" ).datepicker("disable");
		$( "#inputDataFinal" ).datepicker("disable");
		document.getElementById("btn-submeter-calendario").style.visibility="hidden";
		document.getElementById("inputDataInicial.buttonImage").style.visibility="hidden";
		document.getElementById("inputDataFinal.buttonImage").style.visibility="hidden";

	}
	
	
	function validaDatas() {
		// As informações somente serão submetidas se ambos os inputs de data contiverem dados
		if($( "#inputDataInicial" ).val().length == 0 || $( "#inputDataFinal" ).val().length == 0) {
			alert("Data(s) invalida(s). Verifique o(s) campo(s) data inicial e/ou data final");
			
		}
		else {
			document.getElementById("preencheDadosSala").submit();
		}
	}

	
	
