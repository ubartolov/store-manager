$(document).ready(

    function () {

        localeSelect();

        var langParam = Cookies.get("lang");
        var buttonText = "Select Desired Products";
        if(langParam == "rs") {
            buttonText = "Izaberite proizvode"
        }
    
        $('.request-button').prop('disabled', true);
        
        $('.navbar-text').css('visibility', "visible");
        $('.reference-button').css('visibility', "hidden");
        $('.navbar-text').append(buttonText);

        $(".dropdownMenuLink").click(function () {
            var submitButton = $('.submit-button');
            var insertAmount = $('.request-amount');
            submitButton.prop('disabled', true);
            insertAmount.prop('disabled', true);

        });

        $('.warehouse-select').on('change', function () {
            var warehouseSelect = this;
            var warehouseId = warehouseSelect.value;
            var insertAmount = $(warehouseSelect).siblings('.insert-div').children('.request-amount');
            var productId = $(warehouseSelect).siblings('.productIdInput').val();
            var quantityLabel = $(warehouseSelect).siblings('.quantity-div').children('.quantity-label');
            var quantityDiv = $(warehouseSelect).siblings('.quantity-div');
            var inStorage = quantityDiv.children('.currently-in-storage');

            insertAmount.prop('disabled', false);

            $.ajax({
                type: "GET",
                contentType: "application/json",
                url: "/storestock/current-stock/" + warehouseId + "/" + productId,
                dataType: 'json',
                cache: false,
                timeout: 600000,
                success: function (data) {
                    var availableQuantity = 0;
                    availableQuantity = +data;

                    $("#requestListTable tr.table-detail-row").each(function () {
                        var requestAmount = $(this).find(".requestAmountData").text();
                        var requestProductId = $(this).find("input.productIdData").val();
                        var requestWarehouseId = $(this).find("input.warehouseIdData").val();


                        if (+requestProductId == +productId && +requestWarehouseId == +warehouseId) {
                            availableQuantity = +availableQuantity - +requestAmount;
                        }

                    })

                    quantityLabel.text('Currently in Storage: ' + availableQuantity);
                    quantityDiv.css('visibility', 'visible');
                    inStorage.val(availableQuantity);

                },
                error: function (e) {
                    console.log("ERROR : ", e);
                }
            });



        });
        $('.request-amount').on('input', function () {
            var requestAmount = this;
            var sumbitButton = $(requestAmount).parent().siblings('.submit-button');
            var quantity = $(requestAmount).val();
            var currentlyInStorage = $(requestAmount).parent().siblings('.quantity-div').children('.currently-in-storage');


            if (+quantity > +$(currentlyInStorage).val() || +quantity <= 0) {
                requestAmount.style.border = '3px solid #FF0000';
                sumbitButton.prop('disabled', true);
            } else if (+quantity <= +$(currentlyInStorage).val()) {
                requestAmount.style.border = '3px solid #0000FF';
                sumbitButton.prop('disabled', false);
            }
        });

        $('.submit-button').on('click', function () {
            var button = this;
            var storeId = $('#storeIdInput').val();
            var warehouseSelect = $(button).siblings('.warehouse-select');
            var warehouseId = warehouseSelect.val();
            var warehouseAddress = warehouseSelect.find('option:selected').text();
            var productId = $(button).siblings('.productIdInput').val();
            var productName = $(button).siblings('.productNameInput').val();
            var requestAmount = $(button).siblings(".insert-div").children(".request-amount").val();

            if (+requestAmount >= 0) {
                var tableRow = '<tr class="table-detail-row">'
                    + '<td  class="productNameData" value="">' + productName + '</td>'
                    + '<td class="requestAmountData">' + requestAmount + '</td>'
                    + '<td>' + warehouseAddress + '</td>'
                    + '<td><button type="button" class="btn btn-success remove-button">Remove</button></td>'
                    + '<input type="hidden" class="warehouseIdData" value="' + warehouseId + '">'
                    + '<input type="hidden" class="productIdData" value="' + productId + '">'
                    + '</tr>'

                $('.requestListTable').append(tableRow);
                $('.request-button').prop('disabled', false);

                $(this).siblings('.quantity-div').children('.quantity-label').text('');
                $('.request-form').trigger("reset");
            } else {
                alert('Insert Amount Must Be Higher Than 0');
            }

        });


        $('.request-button').on('click', function () {
            var button = this;
            var storeId = $('#storeIdInput').val();
            var link = $('#link-back').val
            
            var requestList = [];

            $(".requestListTable tr.table-detail-row").each(function () {
                var productId = $(this).find("input.productIdData").val();
                var warehouseId = $(this).find("input.warehouseIdData").val();
                var requestAmount = $(this).find(".requestAmountData").text();

                body = {}

                body['storeId'] = +storeId;
                body['productId'] = +productId;
                body['warehouseId'] = +warehouseId;
                body['requestAmount'] = +requestAmount;

                requestList.push(body);

            });

            var bodyAsJson = JSON.stringify(requestList);

            $.ajax({
                type:'POST',
                contentType: 'application/json',
                url: "/storestock/store-new-product",
                data: bodyAsJson,
                dataType: 'json',
                cache: false,
                timeout: 50000,
                success: function (data) {
                    var headerText = 'Successfully added'
                    if(langParam == "rs") {
                        headerText = "Uspešno dodat"
                    }
                   drawModalWithRetrurnedData(headerText, data, "/store/storedetails/" + storeId);

                },
                error: function (e) {
                    var response = JSON.parse(e.responseText);
                    alert(response.prettyErrorMessage);
                }
            });

        });
        $('.requestListTable').on('click', '.remove-button', function () {
            $(this).parent().parent().remove();

        });

        dataTableWithLocale('#main-table', langParam);
    }


);