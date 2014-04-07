// Variáveis globais ===========================================================================

	var scoIdNomeSelecionadoInputTodasSalas;
	var emailResponsaveis = []; // Array global que contém os e-mails de todos os responsáveis pela sala selecionada, ao clicar-se no ícone e-mail 
	var tpRelatorio; // Variável global em uso pela função geraRelatorioSalas().
	var tipoRelatorioGlobal; // Variável global em uso pela função enviarEmailHostsSala().
	var relatNomeSalaGlobal; // Variável global em uso pela função enviarEmailHostsSala().

	
	
	
// =============================================================================================	
	
	
// Funções executadas na inicialização =========================================================
	
	$( document ).ready(function() {
		$("#imgEmailGeralRelat").hide();
		getTodasAsSalasJSON();
		getUsuariosOnlineSalaJSON();
		getUsrLogadoJSON();
		labelLoginFail();
			
	});
	

	
	
	
	
// =============================================================================================	

	
	
	
	
	function getUsrLogadoJSON() {
		
		var usuarioLogado = [];
		
		$.getJSON("usuarioSessao.json", function(data) {
	  		$.each(data, function(key, val) {
	  			usuarioLogado.push(val.usuario);	
			});
	  		
	  		createLogout(usuarioLogado);
		});
		
	}
	
	
	function createLogout(usuarioLogado) {
			
	  var formLogin = document.getElementById("login-group-div");
	  formLogin.innerHTML = '';
	  formLogin.style.paddingRight ="10%";

	  var a = document.createElement('a');
      a.setAttribute('href', 'logout');
      a.appendChild(document.createTextNode("Log-out  (" + usuarioLogado[0] + ")"));
      a.style.color = 'white';
   	  formLogin.appendChild(a);
	}
	

	// Rotina de preenchimento do input (autocomplete) com os nomes de todas as salas virtuais ativas
	function getTodasAsSalasJSON() {
		
		var nomes  = [];
		var scoIds = [];

		$.getJSON("todasAsSalas.json", function(data) {
				
	  		$.each(data, function(key, val) {
				nomes.push(val.nome);
				scoIds.push(val.scoId);
			});

			$(" #input-todas-salas ").autocomplete({
					source: nomes,
					
					select: function(event, ui) {
						scoIdNomeSelecionadoInputTodasSalas = scoIds[nomes.indexOf(ui.item.value)];         
					},
					
					
					focus: function( event, ui ) {
						scoIdNomeSelecionadoInputTodasSalas = scoIds[nomes.indexOf(ui.item.value)];
					}
						
			});

	  	});
	};
	
	// Fim da rotina de preenchimento do input (autocomplete) com os nomes de todas as salas virtuais ativas
		
	
	
	// Rotina de preenchimento da tab online com os graficos de todas as salas online (em uso)
	function getUsuariosOnlineSalaJSON() {
			
		var usuariosOnlineSalaArray = [];
  		
		$.getJSON("usuariosOnlineSala.json", function(data) {
			$.each(data, function(key, val) {
				usuariosOnlineSalaArray.push(val);
			});	
			criaDivsDasSalasOnlineDinamicamente(usuariosOnlineSalaArray);
  		});
		  		
	}
	
		
	function criaDivsDasSalasOnlineDinamicamente(usuariosOnlineSalaArray) {
		
		if(usuariosOnlineSalaArray.length > 0) {
			
			jQuery('#panel-body-online').html('');
			
			var novaDiv;
				
			for(var a = 0; a < usuariosOnlineSalaArray.length; a++) {
			     
				novaDiv = document.createElement("div");
				novaDiv.setAttribute("id", usuariosOnlineSalaArray[a].scoId);
				novaDiv.style.border = 'thick solid #CDC9C9'; 
				novaDiv.style.display = 'inline-block';
				novaDiv.style.margin ="30px 10px 10px 10px";
				novaDiv.style.padding ="10px 10px 10px 10px";
			        
				document.getElementById('panel-body-online').appendChild(novaDiv);
			        
		        desenhaEmDivGraficoPizzaUsuariosSala(usuariosOnlineSalaArray[a].scoId, usuariosOnlineSalaArray[a].hosts, usuariosOnlineSalaArray[a].apresentadores, usuariosOnlineSalaArray[a].convidados , usuariosOnlineSalaArray[a].total, usuariosOnlineSalaArray[a].nomeSala);
		        
		    }
	
		}
		else {
			document.getElementById('sem-sala-online-lbl').style.visibility="visible";
		}
			
					
	}

	
 	
    function desenhaEmDivGraficoPizzaUsuariosSala(scoId, totalHosts, totalApresentadores, totalConvidados, totalGeral, nomeSala) {
    	
	    var hosts = parseInt(totalHosts);
	    var apresentadores = parseInt(totalApresentadores);
	    var convidados = parseInt(totalConvidados);

        // Cria a tabela de dados.
        var data = new google.visualization.DataTable();
        data.addColumn('string', 'Topping');
        data.addColumn('number', 'Slices');
        
        data.addRows([ ['Hosts ('+ totalHosts + ')', hosts], ['Apresentadores (' + totalApresentadores + ')', apresentadores], ['Convidados (' + totalConvidados + ')', convidados] ]);
        
        // Configura opcoes do grafico
        var options = {'title':nomeSala + ". Total: " + totalGeral, 'width':500, 'height':225, is3D: true};
        
        // Instancia e desenha o grafico, passando algumas opcoes        
        var chart = new google.visualization.PieChart(document.getElementById(scoId));
        chart.draw(data, options);

      }
      // Fim da rotina de preenchimento da tab online com os graficos de todas as salas online (em uso)
    
    
    
    // Execucao a cada 2 minutos da rotina de preenchimento da tab online com os graficos de todas as salas online (em uso) 
    setInterval(function(){
    	getUsuariosOnlineSalaJSON();
    }, 120000);
    
      
    
    
    
    //Início da rotina de criação do gráfico da sala selecionada (a partir da tab Salas), na tab Gráficos
	function desenhaGraficoSala(scoId, nomeSala) {
			
		var maximoUsuariosPorScoIdArray = [];
		
		$.getJSON("maximoUsuariosPorScoId.json", {scoId : scoId}, function(data) {
			$.each(data, function(key, val) {
				maximoUsuariosPorScoIdArray.push(val);
			});	
			desenha(maximoUsuariosPorScoIdArray, nomeSala);
  		});		
	}	
	//Fim da rotina de criação do gráfico da sala selecionada (a partir da tab Salas), na tab Gráficos
	
	
	
		
		
	
	function desenhaGraficoSalaEspecifica(dtInicial, dtFinal, nomeSala) {		
		
		if(dtInicial.length == 0 || dtFinal.length == 0) { 
			alert("Por favor, preencha todos os campos: DATA INICIAL, DATA FINAL e NOME DO CLIENTE");
		}
		else {
				
			
			var dataInicial = converteData(dtInicial, "00:00:00");
			var dataFinal = converteData(dtFinal, "23:59:59");
			
		    //Rotina de criação do gráfico da sala informada a partir da pesquisa (data início, data fim, nome sala), na tab Gráfico
					
				var maximoUsuariosPorNomeArray = [];
				
				$.getJSON("maximoUsuariosPorData.json", {dataInicial : dataInicial, dataFinal : dataFinal, scoId : scoIdNomeSelecionadoInputTodasSalas}, function(data) {
					$.each(data, function(key, val) {
						maximoUsuariosPorNomeArray.push(val);
					});	
					
					if(maximoUsuariosPorNomeArray.length == 0) {
						alert("Nao foi possivel gerar grafico, com base no periodo informado!");
					}
					else {
						desenha(maximoUsuariosPorNomeArray, nomeSala);
					
					}
		  		});		
			}
				
	}
	
	
	
	// Gera o gráfico
	function desenha(arr, nomeSala) {
		
		if(arr.length > 0) {
		
			$("#tab-principal li.active").next().find("a[data-toggle='tab']").tab('show'); 
			
			var info = [];
			
			info.push(['Dia', 'Usuarios']);
			
			for(var a = 0; a < arr.length; a++) {
				info.push([arr[a].data, parseInt(arr[a].maxUsuarios)]);
			}
				
				
	        var data = google.visualization.arrayToDataTable(info);
	        	        
	        var options = {title: nomeSala.toUpperCase(), hAxis: {title: 'Data(s)', titleTextStyle: {color: '#8C8C8C'}}};
	
	        var chart = new google.visualization.ColumnChart(document.getElementById('panel-body-apresentacao-graficos'));
		       
	        chart.draw(data, options);
	        
		}
		
	}

	
	
	$(function() {
		
        $('#input-data-inicio').datepicker({
        	format: 'dd/mm/yyyy',
            language: 'pt-BR',
            endDate: new Date(),
            autoclose: true	
        	}).on('changeDate', function(selectedDate) {

				var dataInicioTmp = $('#input-data-inicio').val();
				dataInicioTmp = dataInicioTmp.replace( /(\d{2})\/(\d{2})\/(\d{4})/, "$2/$1/$3");
				var dataInicio = new Date(dataInicioTmp);
				$('#input-data-fim').datepicker("setStartDate", dataInicio);
								        	        	        	
        	});
        
        
		$('#input-data-fim').datepicker({
        	format: 'dd/mm/yyyy',
        	language: 'pt-BR',
        	endDate: new Date(),
        	autoclose: true,	
			}).on('changeDate', function(selectedDate) {
				
							
			});
        					
	});
	
	
	
	
	// Função auxiliar de conversão de data. Do formato dd/mm/yyyy para 
	//mm/dd/yyyy xx:xx:xx (onde a string xx:xx:xx é passada como parâmetro)
	function converteData(data, strAddAposData) {
		
		var novaData = data.replace('\/', '-');
		novaData = novaData.replace('\/', '-');
		novaData = novaData.replace( /(\d{2})-(\d{2})-(\d{4})/, "$3-$2-$1");
		novaData += " " + strAddAposData;
		
		return novaData;
	}
	
	
	function limpaInputTodasSalas() {
		$("#input-todas-salas").val('');
	}

	
	
	function geraRelatorioSalas(tipoRelatorio) {
		
		tipoRelatorioGlobal = tipoRelatorio;
		
		
		$.when( $('#panel-body-relatorios').empty() ).then(
				
				function( data, textStatus, jqXHR ) {
			
				    //Rotina de preenchimento das linhas na tablea table-relatorios (que se encontra na tab Relatórios)    		
					var salasArray = [];
					
					if(tipoRelatorio === "salasFixas") {
						
						tpRelatorio = "Salas Fixas";

						$.getJSON("salasFixas.json", function(data) {
							$.each(data, function(key, val) {
								salasArray.push(val);
							});
							preencheTabelaSalasTipoSelecionado(salasArray, "SALAS FIXAS");
						});

					}
					else if (tipoRelatorio === "todasSalas"){
						
						tpRelatorio = "Todas as Salas";
						
						$.getJSON("todasAsSalas.json", function(data) {
							$.each(data, function(key, val) {
								salasArray.push(val);
							});
								preencheTabelaSalasTipoSelecionado(salasArray, "TODAS AS SALAS");
						});
					}
					else {
						
						$.getJSON("salasEmUsoNoMesAtual.json", function(data) {
							
							tpRelatorio = "Salas em Uso (Mes Corrente)";
							
							$.each(data, function(key, val) {
								salasArray.push(val);
							});
								preencheTabelaSalasTipoSelecionado(salasArray, "SALAS EM USO (MES CORRENTE)");
						});

					}
						
				});
		
	}
	
	
	
	function preencheTabelaSalasTipoSelecionado(salasArray, tipoRelatorio) {
		
		  
		var tabela = document.createElement('table'); // Criando tabela
		$("#panel-body-relatorios").append(tabela); // Colocando-a dentro de panel-body-relatorios
		
		
		// Rotina para criação do cabeçalho
		// ------------------------------------
		var header = tabela.createTHead();		
		var divHeader = document.createElement('div');
		divHeader.style.padding = "8px 0px 8px 8px";
		var lbl = document.createElement("label");
		lbl.appendChild(document.createTextNode(tipoRelatorio));
		divHeader.appendChild(lbl);		
		var headerRow = header.insertRow(0);
		headerRow.style.borderTop="#DDDDDD solid 1px";
		var headerCell = headerRow.insertCell(0);
		headerCell.appendChild(divHeader);
		// ------------------------------------

				
		var rows = [];
		var imgMail = [];
		
		
		for(var s = 0; s < salasArray.length; s++) {
			
			var t = s + 1;
			
			rows[s] = tabela.insertRow(t);
			rows[s].style.borderTop="#DDDDDD solid 1px";
			
			if(s % 2 == 0) {
				rows[s].style.backgroundColor = '#FFFFFF';
			}
			else {
				rows[s].style.backgroundColor = '#F6F4F0';
			}
			
			var nomeSala = salasArray[s].nome;
			var scoId = salasArray[s].scoId;
			
			
			// Procedimento (A) ** não em uso hoje. Pode-se criar um link dinamicamente em cada linha da 
			// tabela (com o nome da sala) que ao ser clicado, executará uma função desejada
			
			//var a = document.createElement('a');
			//a.href = "javascript:funcaoDesejada('" + scoId + "','" + nomeSala + "')";
			//a.innerHTML = nomeSala;
			
			
			//Criando duas células
			var celula1 = rows[s].insertCell(0);
			var celula2 = rows[s].insertCell(1);


			// Adicionando as células à linha atual
			rows[s].appendChild(celula1);
			rows[s].appendChild(celula2);

			
			// Criando uma div, que irá conter o nome da sala
			var div = document.createElement('div');
			div.style.padding = "8px 0px 8px 8px";
			
			// Adicionando o nome da sala à div 
//			div.appendChild(a); // Descomentar essa linha e comentar a proxima caso queira utilizar 
			                    // o procedimento (A) **
			div.innerHTML = nomeSala;
			
			// Adicionando a div à primeira célula
			celula1.appendChild(div);
			
			// Criando a imagem para envio de e-mail
			imgMail[s] = document.createElement("img");
			imgMail[s] = "<img src='images/mail.png' id='"+ scoId + "' onmouseover='' style='cursor: pointer' title='Enviar e-mail para os hosts desta sala'>";
			
						
			// Adicionando a imagem para envio de e-mail à segunda célula
			celula2.innerHTML = imgMail[s];			

			clickImgMailSala(scoId, nomeSala);
									
		}
	}
	
	
	
	
 
	
	function clickImgMailSala(scoId, nomeSala) {

		document.getElementById(scoId).onclick = function() {
					
			var responsaveisSalaArray = [];
					
			$.getJSON("responsaveisSala.json", {scoId : scoId}, function(data) {
				$.each(data, function(key, val) {
					responsaveisSalaArray.push(val);
					emailResponsaveis.push(val.email);
				});
					preencheTblResponsaveisSala(responsaveisSalaArray, nomeSala);
			});			
		};
	}
	
	
	
	
	function preencheTblResponsaveisSala(responsaveisSalaArray, nomeSala) {
		
		relatNomeSalaGlobal = nomeSala;
			
		if(responsaveisSalaArray.length === 0) {
			alert("Nenhum HOST atualmente ativo para a sala  \"" + nomeSala.toUpperCase() + "\"");
		}
		else {
			
			$.when( $('#panel-body-relatorios').empty() ).then(
					
				function( data, textStatus, jqXHR ) {
					
					$("#imgEmailGeralRelat").show();

					var tabela = document.createElement('table');
					tabela.setAttribute("id", "table-responsaveis-sala"); // Atribuindo um ID à tabela
					$("#panel-body-relatorios").append(tabela); 
			
					// Rotina para criação do cabeçalho
					var header = tabela.createTHead();		
					var divHeader = document.createElement('div');
					divHeader.style.padding = "8px 0px 8px 8px";
					var lbl = document.createElement("label");
					lbl.appendChild(document.createTextNode("HOSTS DA SALA VIRTUAL:  " + nomeSala.toUpperCase()));
					divHeader.appendChild(lbl);		
					var headerRow = header.insertRow(0);
					headerRow.style.borderTop="#DDDDDD solid 1px";
					var headerCell = headerRow.insertCell(0);
					headerCell.appendChild(divHeader);
					// ------------------------------------
					
					var rows = [];
					var imgExcluir = [];
					
					for(var r = 0; r < responsaveisSalaArray.length; r++) {
						
						var t = r + 1;
						
						rows[r] = tabela.insertRow(t); 
						rows[r].style.borderTop="#DDDDDD solid 1px";
						
						if(r % 2 == 0) {
							rows[r].style.backgroundColor = '#F6F4F0';
						}
						else {
							rows[r].style.backgroundColor = '#FFFFFF';
						}
			
			
						var principalId = responsaveisSalaArray[r].principalId;
						var nomeResponsavel = responsaveisSalaArray[r].nome;
						var emailResponsavel = responsaveisSalaArray[r].email;
						
						//Criando três células
						var celula1 = rows[r].insertCell(0);
						var celula2 = rows[r].insertCell(1);
						var celula3 = rows[r].insertCell(2);
						
						celula2.id = "cel" + t;
						
						// Adicionando as células à linha atual
						rows[r].appendChild(celula1);
						rows[r].appendChild(celula2);
						rows[r].appendChild(celula3);
						
						rows[r].id = "row" + t;
						
						// Criando uma div, que irá conter o nome da responsável pela sala
						var div = document.createElement('div');
						div.style.padding = "8px 0px 8px 8px";
						
						div.innerHTML = nomeResponsavel;
						
						// Adicionando a div à primeira célula
						celula1.appendChild(div);
						
						celula2.innerHTML = emailResponsavel;
						
						// Criando a imagem para envio de e-mail
						imgExcluir[r] = document.createElement("img");
						
						imgExcluir[r] = "<img src='images/excluir.png' id='"+ principalId + "' onmouseover='' style='cursor: pointer' title='Excluir' onclick='clickImgExcluirResponsavelSala(row" + t + ")'>";
				
						celula3.innerHTML = imgExcluir[r];
						
					}
					
				}
			);

		}
		
		
		 
				
	}
	
	
	
	
	
	function clickImgExcluirResponsavelSala(rId) {
				
		// Rotina de exclusão do e-mail selecionado no array global "emailResponsaveis". Esta exclusão ocorre após click na imagem dinâmica "excluir.png", presente na tabela "table-responsaveis-sala"      
		var idCel = document.getElementById(rId.id).childNodes[1].id; // Pegando o ID da celula2, que contém o e-mail do responsável
		var indxMailResponsavel = emailResponsaveis.indexOf(document.getElementById(idCel).innerHTML); // Pegando o índice, para poder excluir o e-mail do array "emailResponsaveis"		
		emailResponsaveis.splice(indxMailResponsavel, 1); // Removendo o e-mail do array "emailResponsaveis"
		// ---------------------------------------------------------------------------------------------------------		
		document.getElementById("table-responsaveis-sala").deleteRow(rId.rowIndex); // Excluindo visualmente (da página HTML) a linha da tabela 		
	}
	
	
	
	function enviarEmailHostsSala() {
		
		if(emailResponsaveis.length === 0) { // Se todos os e-mails dos hosts foram excluídos (array vazio)
			alert("Lista de e-mails vazia.");
			geraRelatorioSalas(tipoRelatorioGlobal);
		}
		else {
			
			var emailsParaEnviar = getStringParaEnvioDeEmail(emailResponsaveis);
			window.location.href = "mailto:" + emailsParaEnviar + "?subject=Mensagem para o(s) host(s) da sala virtual: " + relatNomeSalaGlobal;
			emailResponsaveis.length = 0;
			emailsParaEnviar.length = 0;
			
			$.when( $('#panel-body-relatorios').empty() ).then(
				function( data, textStatus, jqXHR ) {
					$("#relatorios-lbl").show();
					$( "#panel-body-relatorios" ).append( "<span class='label label-primary' id='relatorios-lbl'>ROTINA PARA ENVIO DE E-MAIL EXECUTADA COM SUCESSO!</span>");
				}
			);

			
		}
		
		$("#imgEmailGeralRelat").hide();
		

		
		
	}
	
	
	// Função auxiliar
	function getStringParaEnvioDeEmail(arrayEmails) {
		
		var retStr = "";
		
		for(var i = 0; i < arrayEmails.length; i++) {
			retStr = retStr + arrayEmails[i] + "; ";
		}
		
		return retStr;
	}
	
	
	
	function recarregar() {
       
		location.reload();
	}
	
	
	function labelLoginFail() {
		
		var status = GetURLParameter('statusCode');
		
		if(status === "no-data") {
			document.getElementById('lbl-login-fail').style.visibility="visible";
		}
		
	}

	
	
	function GetURLParameter(sParam) {
		
	    var sPageURL = window.location.search.substring(1);
	    var sURLVariables = sPageURL.split('&');
	    
	    for (var i = 0; i < sURLVariables.length; i++) {
	        
	    	var sParameterName = sURLVariables[i].split('=');
	        
	        if (sParameterName[0] == sParam) {
	            return sParameterName[1];
	        }
	    }
	}
	
	
	function hideLblLoginFail() {
		document.getElementById('lbl-login-fail').style.visibility="hidden";
	}