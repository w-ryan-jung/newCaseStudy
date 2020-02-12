var base = base || {};

base.stockController = function() {

    'use strict' // add this to avoid some potential bugs

    let model = [];

    let table =  $("#dataTable").DataTable({
        lengthChange: false,
        searching: false,
        "pagingType": "numbers"
    });

    const StockModel = function(_stock) {

        this.stock = _stock;

        const viewModel = this;

        this.drawTable = function (callback) {
            let data = [viewModel.stock.locationName,
                        viewModel.stock.productName,
                        viewModel.stock.amount,
                        viewModel.stock.date.toDateString()];
            table.row.add(data).draw();
            if(typeof callback === 'function'){
                table.page('last').draw(false);
                table.order([3,desc]).draw();
            }
        }

    };

    const view = {

        draw: function(callback){
            model.forEach(d => d.drawTable(callback))  ;
        },

    };

    const controller = {

        load: function() {

            document.getElementById('form').onsubmit = function(event) {
                event.preventDefault();
                controller.postStock();
                return false;
            };

            base.rest.getStocks()
                .then(function(stocks) {
                    model = stocks.map(stock => new StockModel(stock));
                    view.draw();
                });
        },

        postStock: function() {

            const location = document.getElementById('location');
            const product = document.getElementById('product');
            const inOut = document.getElementById('in-out');
            const amount = document.getElementById('amount');
            if(inOut.value === '-1'){
                amount.value = (amount.value * -1);
            }

            const postData = {"locationName":location.value, "productName":product.value, "amount":amount.value};

            base.rest.addStock(postData)
                .then(function(stock) {
                    if(inOut.value === '-1'){
                        amount.value = (amount.value * -1);
                    }
                    const vm = new StockModel(stock);
                    model.push(vm);          // append the foo to the end of the model array
                    vm.drawTable(function () {});
                });
        }
    };

    return controller;
};
