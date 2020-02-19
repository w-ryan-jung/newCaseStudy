var base = base || {};
base.userAdminController = function() {
    'use strict'

    const model = {
        selectedUser: {},
        users: [],
        roles: [],
        roleNames: [],
        products:[],
        locations:[]
    };

    let productTable =  $("#ProductTable").DataTable({
        lengthChange: false,
        searching: false,
        paging: false,
        ordering: false,
        info: false
    });

    let locationTable =  $("#LocationTable").DataTable({
        lengthChange: false,
        searching: false,
        paging: false,
        ordering: false,
        info: false
    });

    const ProductModel = function(_product) {

        this.product = _product;

        const viewModel = this;

        this.drawTable = function () {
            let data = [viewModel.product.productName,
                viewModel.product.price];
            productTable.row.add(data).draw();
        }

    };

    const LocationModel = function(_location) {

        this.location = _location;

        const viewModel = this;

        this.drawTable = function () {
            let data = [viewModel.location.locationName];
            locationTable.row.add(data).draw();
        }
    };

    const view = {

        drawProduct: function(){
            model.products.forEach(p => p.drawTable());
        },

        drawLocaiton: function () {
            model.locations.forEach(l => l.drawTable());
        }

    };

    // Every user has one of these
    const UserViewModel = function(_user) {

        this.user = _user;

        const viewModel = this;

        this.renderListElement = function() {
            const t = document.getElementById('user-template');
            let button = t.content.querySelector('button');
            button.textContent = viewModel.user.username;
            const clone = document.importNode(t.content, true);
            button = clone.querySelector('button');
            button.onclick = viewModel.select;
            viewModel.listElement = button;
            t.parentElement.appendChild(clone);
        };

        this.remove = function() {
            base.rest.deleteUser(model.selectedUser.user.id).then(function() {
                const le = model.selectedUser.listElement;
                le.parentElement.removeChild(le);
                const ix = model.users.indexOf(model.selectedUser);
                model.users.splice(ix, 1);
                model.users[0].select();
            });
        };

        this.select = function() {
            model.selectedUser = viewModel;

            // Set appropriate user-view class to either add or edit.
            const userView = document.getElementById('user-view');
            if (viewModel.user.username === '') {
                userView.classList.remove('edit');
                userView.classList.add('add');
                controller.editPassword(true);
            } else {
                userView.classList.add('edit');
                userView.classList.remove('add');
                controller.editPassword(false);
            }

            // Set active link on the left-hand side menu.
            document.getElementById('user-list')
                .querySelectorAll('.active')
                .forEach(activeEl => activeEl.classList.remove('active'));
            viewModel.listElement.classList.add('active');

            document.getElementById('user-data').querySelector('a').href = '/rest/stock/user/'+viewModel.user.id;

            // Set defaults of form values. This will allow the HTML reset button to work by default HTML behaviour.
            document.getElementById('user-id').defaultValue = viewModel.user.id;
            document.getElementById('set-username').defaultValue = viewModel.user.username;
            document.getElementById('set-password').defaultValue = '';
            const roleIx = model.roleNames.indexOf(viewModel.user.role.name);
            const options = document.getElementById('set-role').querySelectorAll('option');
            options.forEach(o => o.defaultSelected = false);
            options[roleIx].defaultSelected = true;

            // Since we have specified reset values for all fields, we can use the reset function to populate the form
            document.getElementById('user-form').reset();
        }

        this.submit = function(submitEvent) {
            submitEvent.preventDefault;
            const password = document.getElementById('set-password').value;
            const username = document.getElementById('set-username').value;
            const role = document.getElementById('set-role').value;
            const id = document.getElementById('user-id').value;
            const credentials = {username, password, role};
            if (password === '') {
                // This makes it so we don't send an empty password, instead we send nothing on password field
                delete credentials.password;
            }
            if (id !== '') {
                // old user
                base.rest.putUser(id, credentials).then(function(user) {
                    viewModel.user = user;
                    viewModel.listElement.textContent = user.username;
                    // This will fix the new reset state
                    viewModel.select();
                });
            } else {
                base.rest.addUser(credentials).then(function(user) {
                    const addedUserController = new UserViewModel(user);
                    addedUserController.renderListElement();
                    model.users.push(addedUserController);
                    addedUserController.select();
                });
            }
            return false;
        };

    };

    const controller = {
        resetEdit: function() {
            // Most of the reset functionality is handled by default HTML behaviour
            // We just disable the password editing
            const userView = document.getElementById('user-view');
            const isEdit = userView.classList.contains('edit');
            controller.editPassword(!isEdit);
        },
        editPassword: function(enabled) {
            document.getElementById('set-password').disabled = !enabled;
        },
        load: function() {
            // Wire up all buttons
            document.getElementById('change-password').onclick = () => controller.editPassword(true);
            document.getElementById('user-form').onsubmit = (e) => model.selectedUser.submit(e);
            document.getElementById('reset-user').onclick = controller.resetEdit;
            document.getElementById('delete-user').onclick = () => model.selectedUser.remove();
            document.getElementById('new-user').onclick = function(event) {
                const contr = new UserViewModel({username: '', role: model.roles[0], id: ''});
                contr.listElement = event.target;
                contr.select();
            };


            document.getElementById("add-product").onclick = function(event){
                const product = document.getElementById("product").value;
                const price = document.getElementById("price").value;

                if(product <= 0 || price <= 0){
                    alert("Enter a product name and price!");
                    return;
                }

                base.rest.addProduct({"productName": product, "price": price})
                    .then(function (product) {
                        const p = new ProductModel(product);
                        model.products.push(p);
                        p.drawTable();
                    });
            };

            document.getElementById("add-location").onclick = function(event){
                const location = document.getElementById("location").value;
                if(location.length <= 0){
                    alert("Please enter a location name!");
                    return;
                }
                base.rest.addLocation({"locationName": location})
                    .then(function (location) {
                        const l = new LocationModel(location);
                        model.products.push(l);
                        l.drawTable();
                    });
            };
            document.getElementById("delete-product").onclick = function(event){
                const product = document.getElementById("product").value;
                base.rest.deleteProduct(product)
                    .then(function () {
                        productTable.clear().draw();
                        base.rest.getProducts()
                            .then(function(products) {
                                model.products = products.map(product => new ProductModel(product));
                                view.drawProduct();
                            });
                    });

            };

            document.getElementById("delete-location").onclick = function(event){
                const location = document.getElementById("location").value;
                base.rest.deleteLocation(location)
                    .then(function () {
                        locationTable.clear().draw();
                        base.rest.getLocations()
                            .then(function(locations) {
                                model.locations = locations.map(location => new LocationModel(location));
                                view.drawLocaiton();
                            });
                    });
            };

            // Promise.all joins two promises so they can fetch in parallell but pause until both are done.
            Promise.all([base.rest.getUsers(), base.rest.getRoles()])
            .then(function(values) {
                model.users = values[0].map((user, i) => new UserViewModel(user, i));
                model.roles = values[1];
                model.roleNames = model.roles.map(role => role.name);

                // Roles will not change once set so we do it inline here without possibility of change
                const roleTemplate = document.getElementById('role-template');
                model.roles.forEach(function(role) {
                    const o = roleTemplate.content.querySelector('option');
                    o.textContent = role.label;
                    o.value = role.name;
                    const clone = document.importNode(roleTemplate.content, true);
                    roleTemplate.parentElement.appendChild(clone);
                });

                model.users.forEach(user => user.renderListElement());

                model.users[0].select();
            });



            base.rest.getProducts()
                .then(function(products) {
                    model.products = products.map(product => new ProductModel(product));
                    view.drawProduct();
                });

            base.rest.getLocations()
                .then(function(locations) {
                    model.locations = locations.map(location => new LocationModel(location));
                    view.drawLocaiton();
                });


        }

    };

    return controller;
};
