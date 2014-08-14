Ext.define('AM.store.Users', {
    extend: 'Ext.data.Store',
    
    model: 'AM.model.User',
    
    autoLoad: true, // A Store irá pedir para o Proxy carregar os dados imediatamente
    
    proxy: {
    		type: 'ajax',
    		
    		api: {
    			read: 'users.json',
    			update: 'updateUsers.json'
    		},
    		
    		reader: {
    			type: 'json',
    			root: 'users',
    			successProperty: 'success'
    		}
    }
    
//    data: [
//        {name: 'Ed',    email: 'ed@sencha.com'},
//        {name: 'Tommy', email: 'tommy@sencha.com'}
//    ]
});