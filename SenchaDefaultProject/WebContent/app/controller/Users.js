Ext.define('AM.controller.Users', {
    extend: 'Ext.app.Controller',
    
    stores: ['Users'],
    
    models: ['User'],
    
    views: [
            'user.List', // Linhas necessárias, pois precisa-se informar à aplicação para carregar
            'user.Edit' // esses arquivos automaticamente, para serem utilizados quando referenciados      
    ],  

    init: function() {
    	
        this.control({ // Configura listeners nas views da aplicação
//            'viewport > panel': { // Todos os Componentes filhos diretos de viewport que correspondem a panel (Localiza cada Panel que é filho direto de um Viewport)
//                render: this.onPanelRendered
//            }
        	
        	'userlist': {
        		itemdblclick: this.editUser
        	},
        
        	'useredit button[action=save]': { // Configurando o listener para clicks efetuados em botões cuja action seja igual a "save", pertencente(s) à view (xtype) "useredit"  
        		click: this.updateUser
        	}
        
        });
        
    },
    
    editUser: function(grid, record) {
    	//console.log('Duplo clique em ' + record.get('name'));
    	
    	var view = Ext.widget('useredit'); // Mesmo que Ext.create('widget.useredit')
    	
    	view.down('form').loadRecord(record); // Todo componente no Ext JS 4 possui uma função down, que aceita um seletor ComponentQuery para rapidamente localizar qualquer componente filho
    },
    
    updateUser: function(button) {     // Neste ponto, o evento click, nos deu uma referência ao botão 
        							   // no qual o usuário clicou
    	
    	var win = button.up('window'), // ComponentQuery utilizado aqui, primeiramente para obter uma 
    								   // referência à janela Edit User...
    	
        form   = win.down('form'),     // ... depois, para o formulário
        							   
        record = form.getRecord(),     // Obtendo os registros atualmente carregados no formulário
        values = form.getValues();     // Obtendo os valores que foram (ou não) digitados no formulário
    	
    	record.set(values);            // Atualizando as informações
    	win.close();				   // Fechando a janela
    	
        this.getUsersStore().sync();   // sincronizando a store após a edição do registro
    	 
    	
    }
    
    
//	onPanelRendered: function() {
//		console.log('O painel foi renderizado');
//	}

});