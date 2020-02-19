var base = base || {};

base.rest = (function() {
    'use strict'

    // The available classes are defined here, feel free to move them to other files when this gets unwieldy.
    // Note the pattern where the class constructor takes an object that is parsed from JSON, and extends
    // itself using the JSON object with Object.assign.
    // In this way, we don't have to write: this.id = json.id; this.payload = json.payload etc.

    const Location = function (json) {
        Object.assign(this,json);
    };

    const Product = function (json) {
        Object.assign(this,json);
    };

    const Total = function (json) {
        Object.assign(this,json);
    };

    const Stock = function (json) {
        Object.assign(this, json);
        this.date = new Date(this.date);
    };

    const Role = function(role) {
        this.name = role;
        this.label = this.name[0] + this.name.toLowerCase().slice(1);
    };

    const User = function(json) {
        Object.assign(this, json);
        this.role = new Role(json.role);
        this.json = json;

        this.isAdmin = function() {
            return this.role.name === 'ADMIN';
        };
        this.isNone = function() {
            return this.role.name === 'NONE';
        };
    };

    // Expose the classes to base module, they are primarily used by the tests.
    base.Stock = Stock;
    base.Total = Total;
    base.User = User;
    base.Role = Role;
    base.Product = Product;
    base.Location = Location;

    // This method extends the functionality of fetch by adding default error handling.
    // Using it is entirely optional.
    const baseFetch = function(url, config) {

         // We create config if it does not already exist
        config = config || {};
         // Setting 'same-origin' make sure that cookies are sent to the server (which it would not otherwise)
        config.credentials = 'same-origin';
        return fetch(url, config)
            .then(function(response) {
                if (!response.ok) {
                    return new Promise((resolve) => resolve(response.json()))
                        .then(function(errorJson) {
                            const status = errorJson.status;
                            throw Error(`${errorJson.status} ${errorJson.error}\n${errorJson.message}`);
                        });
                } else {
                    return response;
                }
            }).catch(function(error) {
                alert(error);
                throw error;
            });
    };

    const jsonHeader = {'Content-Type': 'application/json;charset=utf-8'};

    return {
        /*
         * Fetches the currently logged in user
         *
         * example: const me = base.rest.getUser();
         */
        getUser: function() {
            return baseFetch('/rest/user')
                .then(response => response.json())
                .then(u => new User(u));
        },

        /*
         * Login with given credentials.
         * username: name of the user
         * password: password in plaintext
         * rememberMe: boolean flag, whether the user
         *
         * example: base.rest.login('test', 'password', true);
         */
        login: function(username, password, rememberMe) {
            var loginObj = {username: username, password: password};
            return baseFetch('/rest/user/login?remember=' + rememberMe, {
                    method: 'POST',
                    body: JSON.stringify(loginObj),
                    headers: jsonHeader});
        },

        /*
         * Logout the current user.
         *
         * example: base.rest.logout();
         */
        logout: function() {
            return baseFetch('/rest/user/logout', {method: 'POST'});
        },

        /*
         * Gets the users available (admin only).
         * returns: A list of User
         *
         * example: const userList = base.rest.getUsers();
         */
        getUsers: function() {
            return baseFetch('/rest/user/all')
                .then(response => response.json())
                .then(users => users.map(u => new User(u)));
        },

        /*
         * Gets the roles available (admin only).
         * returns: A list of Role
         *
         * example: const availableRoles = base.rest.getRoles();
         */
        getRoles: function() {
            return baseFetch('/rest/user/roles')
                .then(response => response.json())
                .then(roles => roles.map(r => new Role(r)));
        },

        /*
         * Add a new user (admin only).
         * credentials: object with username, password, and role
         * returns: the updated User
         *
         * example: let user = base.rest.addUser(2, {'username': 'Test2', 'password': 'password2', 'role': 'USER');
         */
        addUser: function(credentials) {
            return baseFetch('/rest/user', {
                    method: 'POST',
                    body: JSON.stringify(credentials),
                    headers: jsonHeader})
                .then(response => response.json())
                .then(u => new User(u));
        },

        /*
         * Replace a specific user with a given userId (admin only).
         * id: user to replace
         * credentials: object with username, password (optional), and role
         * returns: the updated user
         *
         * example: let user = base.rest.putUser(2, {'username': 'Test2', 'role': 'USER');
         */
        putUser: function(id, credentials) {
            return baseFetch('/rest/user/'+id, {
                    method: 'PUT',
                    body: JSON.stringify(credentials),
                    headers: jsonHeader})
                .then(response => response.json())
                .then(u => new User(u));
        },

        /*
         * Delete a specific user with a given userId (admin only).
         * id: user to delete
         *
         * example: base.rest.deleteUser(2);
         */
        deleteUser: function(userId) {
            return baseFetch('/rest/user/'+userId, {method: 'DELETE'});
        },

        /*
         * Fetches the stocks of the user, either the currently logged in one, or of a specific user (admin only).
         * userId (optional): a specific user OR if userId is not specified.
         * returns: an array of Stocks
         */
        getStocks: function(userId) {
            var postfix = "";
            if (typeof userId !== "undefined") postfix = "/user/" + userId;
            return baseFetch('/rest/stock' + postfix)
                .then(response => response.json())
                .then(stocks => stocks.map(s => new Stock(s)));
        },

        addStock: function(stock) {
            let result = baseFetch('/rest/stock', {
                method: 'POST',
                body: JSON.stringify(stock),
                headers: jsonHeader})
                .then(response => response.json())
                .then(s => new Stock(s));

            return result;
        },

        getTotal: function(){
            return baseFetch('/rest/total')
                .then(response => response.json())
                .then(total => total.map(t => new Total(t)));
        },


        getStocksBy: function(item){
            return baseFetch('/rest/total',{
                method:'POST',
                body: JSON.stringify(item),
                headers: jsonHeader})
                .then(response => response.json())
                .then(total => total.map(t => new Total(t)));
        },

        getTotalByLocation: function(location){
            return baseFetch('/rest/total/location/' + location)
                .then(response => response.json())
                .then(total => total.map(t => new Total(t)));
        },

        getTotalByProduct: function(product){
            return baseFetch('/rest/total/product/' + product)
                .then(response => response.json())
                .then(total => total.map(t => new Total(t)));
        },

        getProducts: function(){
            return baseFetch('/rest/product')
                .then(response => response.json())
                .then(products => products.map(p => new Product(p)));
        },

        deleteProduct: function(product){
            return baseFetch('/rest/product/'+product, {method: 'DELETE'});
        },


        addProduct: function(product){
            let result = baseFetch('/rest/product', {
                method: 'POST',
                body: JSON.stringify(product),
                headers: jsonHeader})
                .then(response => response.json())
                .then(p => new Product(p));
            return result;
        },


        getLocations: function(){
            return baseFetch('/rest/location')
                .then(response => response.json())
                .then(locations => locations.map(l => new Location(l)));
        },


        addLocation: function(location){
            let result = baseFetch('/rest/location', {
                method: 'POST',
                body: JSON.stringify(location),
                headers: jsonHeader})
                .then(response => response.json())
                .then(l => new Location(l));
            return result;
        },

        deleteLocation: function(location){
            return baseFetch('/rest/location/'+location, {method: 'DELETE'});

        },

        deleteStock: function(fooId) {
            return baseFetch('/rest/stock/'+fooId, {method: 'DELETE'});
        },

        updateStock: function(fooId, total) {
            return baseFetch('/rest/stock/'+fooId+'/total/'+total, {method: 'POST'})
                .then(function() {
                    return total;
                });
        }
    };
})();

