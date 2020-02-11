var base = base || {};

base.totalController = function() {
    'use strict' // add this to avoid some potential bugs

    let model = [];
    let table =  $("#dataTable").DataTable({
        lengthChange: false,
        searching: false,
        "pagingType": "numbers"
    });

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

        load: function() {
            base.rest.getTotal()
                .then(function(stocks) {
                    model = stocks.map(balance => new BalanceModel(balance));
                    view.draw();
                });
        },

    };

    return controller;
};
