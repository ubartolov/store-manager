$(document).ready(

    function () {

        $('.order-button').prop('disabled', true);
        $('.request-button').prop('disabled', true);
        $('.navbar-brand').append('New Product');

        $('.first-alignment-div').on('input', function () {
            var parentDiv = this;
            var inputName = $(parentDiv).children('.name-div').children('.name-input').val();
            var inputPrice = $(parentDiv).children('.price-div').children('.price-input').val();
            var inputQuantity = $(parentDiv).children('.quantity-div').children('.request-amount').val();

            if (inputName != null && +inputPrice > 0 && +inputQuantity > 0) {
                $('.order-button').prop('disabled', false);
            }
        })

        $('.order-button').on('click', function () {
            var button = this;
            var warehouseId = $('#warehouseIdInput').val();
            var productName = $(button).siblings('.name-div').children('.name-input').val();
            var productPrice = $(button).siblings('.price-div').children('.price-input').val();
            var requestAmount = $(button).siblings('.quantity-div').children('.request-amount').val();

            if (+requestAmount >= 0 && +productPrice >= 0) {
                var tableRow = '<tr class="table-detail-row">'
                    + '<td class="productNameData">' + productName + '</td>'
                    + '<td class="requestAmountData">' + requestAmount + '</td>'
                    + '<td class="productPriceData">' + productPrice + '</td>'
                    + '<td><button type="button" class="btn btn-danger remove-button">Remove</button></td>'
                    + '<input type="hidden" class="warehouseIdData" value="' + warehouseId + '">'
                    + '</tr>'

                $('#requestListTable').append(tableRow);
                $('.order-button').prop('disabled', true);
                $('.request-button').prop('disabled', false);
                $('.order-form').trigger("reset");

            } else {
                alert('Insert Amount Must Be Higher Than 0');
            }

        });
        $('#requestListTable').on('click', '.remove-button', function () {
            $(this).parent().parent().remove();

        });

        $('.request-button').on('click', function () {
            var button = this;
            var warehouseId = $('#warehouseIdInput').val();

            var requestList = [];

            $("#requestListTable tr.table-detail-row").each(function () {

                var productName = $(this).find(".productNameData").text();
                var productPrice = $(this).find(".productPriceData").text();
                var requestAmount = $(this).find(".requestAmountData").text();


                body = {}

                body['productName'] = productName;
                body['productPrice'] = +productPrice;
                body['warehouseId'] = +warehouseId;
                body['requestAmount'] = +requestAmount;

                requestList.push(body);
            });

            var bodyAsJson = JSON.stringify(requestList);

            console.log(bodyAsJson);

            $.ajax({
                type: 'POST',
                contentType: 'application/json',
                url: "/storestock/warehouse-new-product",
                data: bodyAsJson,
                dataType: 'json',
                cache: false,
                timeout: 50000,
                success: function (data) {
                    var headerText = 'Successfully added new products';
                    drawModalWithRetrurnedData(headerText, data, "/warehouse/warehousedetails/" + warehouseId)
                },
                error: function (e) {
                    var response = JSON.parse(e.responseText);
                    alert(response.prettyErrorMessage);
                }
            });
            $('.requestListTable').on('click', '.remove-button', function () {
                $(this).parent().parent().remove();
    
            });

        });
    }
);