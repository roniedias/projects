Ext.define('AM.controller.Users', {
    extend: 'Ext.app.Controller',
    
    stores: ['Users'],
    
    models: ['User'],
    
    views: [
            'user.List', // Linhas necess�rias, pois precisa-se informar � aplica��o para carregar
            'user.Edit' // esses arquivos automaticamente, para serem utilizados quando referenciados      
    ],  

    init: function() {
    	
        this.control({ // Configura listeners nas views da aplica��o
//            'viewport > panel': { // Todos os Componentes filhos diretos de viewport que correspondem a panel (Localiza cada Panel que � filho direto de um Viewport)
//                render: this.onPanelRendered
//            }
        	
        	'userlist': {
        		itemdblclick: this.editUser
        	},
        
        	'useredit button[action=save]': { // Configurando o listener para clicks efetuados em bot�es cuja action seja igual a "save", pertencente(s) � view (xtype) "useredit"  
        		click: this.updateUser
        	}
        
        });
        
    },
    
    editUser: function(grid, record) {
    	//console.log('Duplo clique em ' + record.get('name'));
    	
    	var view = Ext.widget('useredit'); // Mesmo que Ext.create('widget.useredit')
    	
    	view.down('form').loadRecord(record); // Todo componente no Ext JS 4 possui uma fun��o down, que aceita um seletor ComponentQuery para rapidamente localizar qualquer componente filho
    },
    
    updateUser: function(button) {     // Neste ponto, o evento click, nos deu uma refer�ncia ao bot�o 
        							   // no qual o usu�rio clicou
    	
    	var win = button.up('window'), // ComponentQuery utilizado aqui, primeiramente para obter uma 
    								   // refer�ncia � janela Edit User...
    	
        form   = win.down('form'),     // ... depois, para o formul�rio
        							   
        record = form.getRecord(),     // Obtendo os registros atualmente carregados no formul�rio
        values = form.getValues();     // Obtendo os valores que foram (ou n�o) digitados no formul�rio
    	
    	record.set(values);            // Atualizando as informa��es
    	win.close();				   // Fechando a janela
    	
        this.getUsersStore().sync();   // sincronizando a store ap�s a edi��o do registro
    	 
    	
    }
    
    
//	onPanelRendered: function() {
//		console.log('O painel foi renderizado');
//	}

});