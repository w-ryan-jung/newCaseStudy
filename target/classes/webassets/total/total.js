var base = base || {};

base.totalController = function() {
    'use strict' // add this to avoid some potential bugs

    let model = [];
    let table =  $("#dataTable").DataTable({
        lengthChange: false,
        searching: false,
        paging: false,
        ordering: false,
        info: false
    });

    const Option = function (option,tagname) {
        this.makeOption = function () {
            // Create New Option.
            let newOption = $('<option>');
            newOption.attr('value', option).text(option);
            // Append that to the DropDownList.
            $(tagname).append(newOption);
        };
    };

    const BalanceModel = function(_balance) {

        this.balance = _balance;

        const viewModel = this;

        this.drawTable = function () {
            let data = [viewModel.balance.locationName,
                viewModel.balance.productName,
                viewModel.balance.total];
            table.row.add(data).draw();
        }

    };

    const view = {

        draw: function(){
            model.forEach(d => d.drawTable())  ;
        }

    };

    const controller = {

        load: function () {

            base.rest.getTotal()
                .then(function (stocks) {
                    model = stocks.map(balance => new BalanceModel(balance));
                    view.draw();
                });


            base.rest.getLocations()
                .then(function (locations) {
                    locations.map(location => new Option(location.locationName, '#location').makeOption())
                });

            base.rest.getProducts()
                .then(function (products) {
                    products.map(product => new Option(product.productName, '#product').makeOption())
                });

            $("#location").change(function () {
                if(this.value === 'Location'){
                    return
                }else if(this.value === 'All'){
                    base.rest.getTotal()
                        .then(function (stocks) {
                            model = stocks.map(balance => new BalanceModel(balance));
                            table.clear().draw();
                            view.draw();
                        });
                }else{
                    base.rest.getTotalByLocation(this.value)
                        .then(function (stocks) {
                            model = stocks.map(balance => new BalanceModel(balance));
                            table.clear().draw();
                            view.draw();
                        });
                }
            });

            $("#product").change(function () {
                console.log(this.value);

                if(this.value === 'Product'){
                    return
                }else if(this.value === 'All'){
                    base.rest.getTotal()
                        .then(function (stocks) {
                            model = stocks.map(balance => new BalanceModel(balance));
                            table.clear().draw();
                            view.draw();
                        });
                }else{
                    base.rest.getTotalByProduct(this.value)
                        .then(function (stocks) {
                            model = stocks.map(balance => new BalanceModel(balance));
                            table.clear().draw();
                            view.draw();
                        });
                }

            });


        }
    }

    return controller;

};
