var base = base || {};

base.stockController = function() {

    'use strict' // add this to avoid some potential bugs

    let model = [];

    let table =  $("#dataTable").DataTable({
        lengthChange: false,
        searching: false,
        "pagingType": "numbers",
    });

    const Option = function (option,tagname) {
        this.makeOption = function () {
            // Create New Option.
            let newOption = $('<option>');
            newOption.attr('value', option).text(option);
            // Append that to the DropDownList.
            $(tagname).append(newOption);
        }
    };

    const StockModel = function(_stock) {

        this.stock = _stock;

        const viewModel = this;

        this.drawTable = function () {
            let data = [viewModel.stock.locationName,
                        viewModel.stock.productName,
                        viewModel.stock.total];

            table.row.add(data).draw();
        }

    };

    const view = {
        draw: function(){
            model.forEach(d => d.drawTable())  ;
        },
    };

    const controller = {

        request: function() {
            let location = document.getElementById('location').value;
            let product = document.getElementById('product').value;
            let item = {"locationName":location,"productName":product};
            base.rest.getStocksBy(item)
                .then(function (stocks) {
                    table.clear().draw();
                    controller.drawData(stocks);
                });
        },

        load: function() {

            // all
            base.rest.getTotal()
                .then(function (stocks) {controller.drawData(stocks)});

            // by location
            $("#location").change(function () {
                controller.request();
            });

            // by product
            $("#product").change(function () {
                controller.request();
            });


            // location select list
            base.rest.getLocations()
                .then((function (locations) {
                    locations.map(location => new Option(location.locationName, '#location').makeOption());
                }));

            // product select list
            base.rest.getProducts()
                .then((function (products) {
                    products.map(product => new Option(product.productName,'#product').makeOption());
                }));

            $("button").click(function(event){
                event.preventDefault();
                const location = document.getElementById('location');
                const product = document.getElementById('product');
                let amount = document.getElementById('amount');

                if(location.value === 'All Location'){
                    alert("Please select location");
                    return;
                }else if(product.value === 'All Product'){
                    alert("Please select product");
                    return;
                }else if(amount.value === ''){
                    alert("Please input amount");
                    return;
                }

                const postData = {"locationName":location.value, "productName":product.value, "amount":amount.value};
                if(this.id === 'delete'){
                    postData.amount = postData.amount * -1;
                }


                let total = 0;
                if(table.row().count() > 0){
                    total = table.row().data()[2];
                }


                if(this.id == 'delete' && amount.value > total){
                    alert("Amount should be less than total");
                    return;
                }else{

                    base.rest.addStock(postData)
                        .then(function(stock) {
                            controller.request();
                        });
                }

            });
        },

        drawData: function(data){
            model = data.map(d => new StockModel(d));
            view.draw();
        },

    };

    return controller;
};
