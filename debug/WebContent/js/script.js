/*
 * Funções úteis
 */


var urlCodCliente;
var urlCodLoja;
var urlCodAmbiente;
var urlCodTipoAmbiente;
var urlCodTipoServico;
var urlCodProduto;

var urlNomeTipoServico;
var urlNomeProduto;
var urlNomeCliente;
var urlNomeTipoAmbiente;





function load() {
	
	selectSet("hidden", 'select-tipos-amb');
	selectSet("hidden", 'select-produtos');
	selectSet("hidden", 'select-tipos-serv');
	
	document.getElementById('submit-btn').style.visibility="hidden";
	
	urlCodCliente = decodeURIComponent((new RegExp('[?|&]' + 'cli' + '=' + '([^&;]+?)(&|#|;|$)').exec(location.search)||[,""])[1].replace(/\+/g, '%20'))||null;
	urlCodLoja = decodeURIComponent((new RegExp('[?|&]' + 'loja' + '=' + '([^&;]+?)(&|#|;|$)').exec(location.search)||[,""])[1].replace(/\+/g, '%20'))||null;
	urlCodAmbiente = decodeURIComponent((new RegExp('[?|&]' + 'amb' + '=' + '([^&;]+?)(&|#|;|$)').exec(location.search)||[,""])[1].replace(/\+/g, '%20'))||null;
	urlCodTipoAmbiente = decodeURIComponent((new RegExp('[?|&]' + 'tip' + '=' + '([^&;]+?)(&|#|;|$)').exec(location.search)||[,""])[1].replace(/\+/g, '%20'))||null;
	urlCodTipoServico = decodeURIComponent((new RegExp('[?|&]' + 'param' + '=' + '([^&;]+?)(&|#|;|$)').exec(location.search)||[,""])[1].replace(/\+/g, '%20'))||null;
	urlCodProduto = decodeURIComponent((new RegExp('[?|&]' + 'prod' + '=' + '([^&;]+?)(&|#|;|$)').exec(location.search)||[,""])[1].replace(/\+/g, '%20'))||null;
	
	urlNomeCliente = decodeURIComponent((new RegExp('[?|&]' + 'nmamb' + '=' + '([^&;]+?)(&|#|;|$)').exec(location.search)||[,""])[1].replace(/\+/g, '%20'))||null;
	urlNomeTipoAmbiente = decodeURIComponent((new RegExp('[?|&]' + 'nmtip' + '=' + '([^&;]+?)(&|#|;|$)').exec(location.search)||[,""])[1].replace(/\+/g, '%20'))||null;
	urlNomeProduto = decodeURIComponent((new RegExp('[?|&]' + 'nmprod' + '=' + '([^&;]+?)(&|#|;|$)').exec(location.search)||[,""])[1].replace(/\+/g, '%20'))||null;
	urlNomeTipoServico = decodeURIComponent((new RegExp('[?|&]' + 'nmparam' + '=' + '([^&;]+?)(&|#|;|$)').exec(location.search)||[,""])[1].replace(/\+/g, '%20'))||null;

	
	
	if(!(urlCodCliente == null || urlCodLoja == null || urlCodAmbiente == null || urlCodTipoAmbiente == null || urlCodTipoServico == null || urlCodProduto == null || urlNomeTipoServico == null || urlNomeProduto == null || urlNomeCliente == null || urlNomeTipoAmbiente == null)) {
		executeMonitWithUrlParams();	
	}
	
}



function reiniciar() {
	$('#input-cliente').val("");
	location.reload();
}



//Função que envia as informações do ambiente a ser monitorado para o controller, com base nos 
//parâmetros passados via URL
function executeMonitWithUrlParams() {
	
	
	var inputCliente = $( "#input-cliente" );
	inputCliente.val( inputCliente.val() + "(" + urlCodCliente + ")" + " " + urlNomeCliente);
	
	
	var envTypes = [];
	$.getJSON("clientEnvTypes.json", { codCliente: urlCodCliente }, function(data){		
		$.each(data, function(key, val) {
			envTypes.push(data[key]);
		});
		preencheComboTiposDeAmbiente(envTypes);
		$('#select-tipos-amb').val(urlCodTipoAmbiente + " " + urlNomeTipoAmbiente);
	});
	
	
	var products = [];
	$.getJSON("products.json", { codigoTipoAmb: urlCodTipoAmbiente }, function(data){		
		$.each(data, function(key, val) {
			products.push(data[key]);
		});
		preencheComboProdutos(products);
		$('#select-produtos').val(urlCodProduto + " " + urlNomeProduto);
		
		preencheTipServCombo(); // Necessário adicionar em função separada, pois ambas as funções getJSON 
	});                         // products.json e serviceTypes.json estavam sendo requisitadas ao mesmo tempo
								// ocasionando problema de sincronismo no resultado final

}



function preencheTipServCombo() {
	
	var servTypes = [];
	$.getJSON("serviceTypes.json", { codigoProduto: urlCodProduto }, function(data){		
		$.each(data, function(key, val) {
			servTypes.push(data[key]);
		});
		preencheComboTiposServicos(servTypes);
		executaMonitViaAjax();
		$('#select-tipos-serv').val(urlCodTipoServico + " " + urlNomeTipoServico);
	});
		
}


function executaMonitViaAjax() {
	$.ajax({url: 'executeMonitWithUrlParams', type: 'POST', data: { codigoCliente : urlCodCliente, codigoEmpresa : urlCodLoja, codigoAmbiente : urlCodAmbiente, codigoTipoAmb : urlCodTipoAmbiente, codTipoServico : urlCodTipoServico, codigoProduto : urlCodProduto}});
}




function selectSet(status, selectId) {

	var select = document.getElementById(selectId);
	select.options.length = 0; // limpa itens existentes
	select.style.visibility=status;

}



//========================================================================================


/*
 *    Funções que têm por objetivo autocompletar o input com todos os clientes que contém
 *    a String digitada 
 */



function preencheAutocomplete(items) {	 
	 $(" #input-cliente ").autocomplete({
		source: items
	 });
};

	 
function getAllClientsJSON() {
	var items = [];
		$.getJSON("allClients.json", function(data) {
			$.each(data, function(key, val) {
				items.push("(" + val.codigo + ") " + val.descricao);
			});
			preencheAutocomplete(items);
		});
};  

getAllClientsJSON();


// ========================================================================================

/*
 * Funções: envio do código do cliente para o controller, retorno dos tipos de ambiente (PRODUÇÃO, TESTE, ETC.), 
 * preenchimento da tag Select "select-tipos-amb" com esses dados retornados
 * 
*/

function getClientEnvTypes(clientTxtInputInfo) {
	
	selectSet("hidden", 'select-produtos');
	selectSet("hidden", 'select-tipos-serv');
	document.getElementById('submit-btn').style.visibility="hidden";
	

	
	
	var codCliente = clientTxtInputInfo.substr(1, 6); // extraindo o código do cliente
	
	var items = [];
	$.getJSON("clientEnvTypes.json", { codCliente: codCliente }, function(data){		
		$.each(data, function(key, val) {
			items.push(data[key]);
		});
		preencheComboTiposDeAmbiente(items);
	});
}  

function preencheComboTiposDeAmbiente(items) {
	if(items.length == 1 && items[0].indexOf("veis! Para executar esta rotina") != -1) {
		alert(items[0]); // Se não houver dados, alerta
	}
	else {
		// Preenche o combo
		
		selectSet("visible", 'select-tipos-amb');		
		document.getElementById('select-tipos-amb').options.add(new Option("SELECIONE ..."));
		
		for (var i in items) {
			document.getElementById('select-tipos-amb').options.add(new Option(items[i]));
		}		
	
	}
}

function selectTiposAmbChange() {
    var selectBox = document.getElementById("select-tipos-amb");
    var tipoAmbiente = selectBox.options[selectBox.selectedIndex].value;
    
    if(tipoAmbiente != "SELECIONE ...") {
    	getProdutos(tipoAmbiente);
    }
    else {
    	selectSet("hidden", 'select-produtos');
    	selectSet("hidden", 'select-tipos-serv');
    	
    	document.getElementById('submit-btn').style.visibility="hidden";
    }
    
    
}





//========================================================================================

/*
 * Funções: envio do código do tipo de ambiente para o controller, retorno dos produtos 
 * (000030 PROTHEUS 11, 000036 PROTHEUS TSS N, ETC.) e preenchimento da tag 
 * "select-produtos" com esses dados retornados
 * 
*/



function getProdutos(tipoAmbiente) {
	
	
	var codigoTipoAmb = tipoAmbiente.substr(0, 2); // extraindo o código do tipo do ambiente
	
	var items = [];
	$.getJSON("products.json", { codigoTipoAmb: codigoTipoAmb }, function(data){		
		$.each(data, function(key, val) {
			items.push(data[key]);
		});
		preencheComboProdutos(items);
	});
		
}  



function preencheComboProdutos(items) {
	if(items.length == 1 && items[0].indexOf("veis! Para executar esta rotina") != -1) {
		alert(items[0]); // Se não houver dados, alerta
	}
	else {
		// Preenche o combo
				
		selectSet("visible", 'select-produtos');
		document.getElementById('select-produtos').options.add(new Option("SELECIONE ..."));
		selectSet("hidden", 'select-tipos-serv');
		document.getElementById('select-tipos-serv').options.add(new Option("SELECIONE ..."));
				
		
		for (var i in items) {
			document.getElementById('select-produtos').options.add(new Option(items[i]));
		}		
	
	}
}


function selectProdutosChange() {
    var selectBox = document.getElementById("select-produtos");
    var produto = selectBox.options[selectBox.selectedIndex].value;
    
    if(produto != "SELECIONE ...") {
    	getTiposServicos(produto);
    }
    else {
    	selectSet("hidden", 'select-tipos-serv');
		document.getElementById('submit-btn').style.visibility="hidden";
    }

}






//========================================================================================

/*
 * Funções: envio do código do produto para o controller, retorno dos tipos de serviços de monitoramento 
 * (002 ESPACO EM DISCO AMB. PROTHEUS, 079 STATUS DE ITEM DE AMBIENTE, ETC.) e preenchimento da tag 
 * "select-tipos-serv" com esses dados retornados
 * 
*/



function getTiposServicos(produto) {
	
	var codigoProduto = produto.substr(0, 6); // extraindo o código do produto
	
	var items = [];
	$.getJSON("serviceTypes.json", { codigoProduto: codigoProduto }, function(data){		
		$.each(data, function(key, val) {
			items.push(data[key]);
		});
		preencheComboTiposServicos(items);
	});

}



function preencheComboTiposServicos(items) {
	
	if(items.length == 1 && items[0].indexOf("veis! Para executar esta rotina") != -1) {
		alert(items[0]); // Se não houver dados, alerta
	}
	else {
		// Preenche o combo
				
		selectSet("visible", 'select-tipos-serv');
		document.getElementById('select-tipos-serv').options.add(new Option("SELECIONE ..."));
		
		for (var i in items) {
			document.getElementById('select-tipos-serv').options.add(new Option(items[i]));
		}		
	
	}

}



function selectTiposServChange() {
    var selectBox = document.getElementById("select-tipos-serv");
    var tipoServico = selectBox.options[selectBox.selectedIndex].value;
    
    if(tipoServico != "SELECIONE ...") {
    	document.getElementById('submit-btn').style.visibility="visible";
    }
    else {
    	//alert("Selecione um tipo de serviço");
    	document.getElementById('submit-btn').style.visibility="hidden";
    }
}



//========================================================================================

/*
 * Execução do monitoramento
 */



function executeMonit() {
	
    var selectBox = document.getElementById("select-tipos-serv");
    var tipoServico = selectBox.options[selectBox.selectedIndex].value;	
	var codTipoServico = tipoServico.substr(0, 3); // extraindo o código do tipo de serviço
	
	$.ajax({url: 'executeMonit', type: 'POST', data: { codTipoServico: codTipoServico }});
	
}







//========================================================================================


/*
 * Funções que têm por objetivo conectar-se e administrar a conexão client com o servidor (via websocket)
 */


var webSockMonit = new WebSocket("ws://172.18.0.149:8081/debug/webSockMonit");
//var webSockMonit = new WebSocket("ws://172.18.0.149:8081/debug/servletWebSocket");
			
webSockMonit.onmessage = function(message) {
	document.getElementById("txt-area-monit").textContent += message.data + "\n";
};
			
function closeConnect() {
	webSockMonit.close();
}



var webSockRead = new WebSocket("ws://172.18.0.148:8081/debug/webSockRead");

webSockRead.onmessage = function(message) {
	document.getElementById("txt-area-leitura").textContent += message.data + "\n";
};
			
function closeConnect() {
	webSockRead.close();
}



var webSockWrite = new WebSocket("ws://172.18.0.148:8081/debug/webSockWrite");

webSockWrite.onmessage = function(message) {
	document.getElementById("txt-area-escrita").textContent += message.data + "\n";
};
			
function closeConnect() {
	webSockWrite.close();
}








