var base = base || {};

base.stockController = function() {
    'use strict' // add this to avoid some potential bugs

    // List of all foo data, will be useful to have when update functionality is added in lab 2.
    let model = [];
    let table =  $("#dataTable").DataTable({
        lengthChange: false,
        searching: false,
        "pagingType": "numbers"
    });

    const StockModel = function(_stock) {

        this.stock = _stock;

        const viewModel = this;

        this.render = function(template) {
            this.update(template.content.querySelector('tr'));
            const clone = document.importNode(template.content, true);
            template.parentElement.appendChild(clone);
        };

        this.update = function(trElement) {
            const tds = trElement.children;
            tds[0].textContent = viewModel.stock.locationName;
            tds[1].textContent = viewModel.stock.productName;
            tds[2].textContent = viewModel.stock.amount;
            const d = viewModel.stock.date;
            tds[3].textContent = d.toLocaleDateString();
        };

        this.drawTable = function () {
            let data = [viewModel.stock.locationName,
                        viewModel.stock.productName,
                        viewModel.stock.amount,
                        viewModel.stock.date.toDateString()];
            table.row.add(data).draw();
        }

    };

    const view = {

        render: function() {
            const t = this.template();
            model.forEach(d => d.render(t));
        },

        draw: function(){
            model.forEach(d => d.drawTable())  ;
        },

        template: function() {
            return document.getElementById('stock-template');
        }
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
                    vm.drawTable();

                    alert("It has been updated now");
                });
        }
    };

    // $("#dataTable").DataTable({
    //     lengthChange: false,
    //     searching: false,
    //
    //     data:stocks,
    //     columns: [
    //         { data: "locationName" },
    //         { data: "productName" },
    //         { data: "amount" },
    //         { data: "date",render:function (data) {
    //                 return data.toLocaleString();
    //             }}
    //     ]
    // });

    return controller;
};
