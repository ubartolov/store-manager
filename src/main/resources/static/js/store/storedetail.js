$(document).ready(

    function () {

        localeSelect();

        var langParam = Cookies.get("lang");
        var buttonText = "Request New Product";
        if (langParam == "rs") {
            buttonText = "Zatraži Nov Proizvod";
        }
        $('.navbar-text').css('visibility', 'visible');
        $('.reference-button').append(buttonText);

        $('.reference-button').click(function () {
            var storeId = $('#storeIdInput').val();
            var referanceButton = this;
            var link = '/store/requestproduct/' + storeId;
            referanceButton.href = link;
        })




        $(".dropdownMenuLink").click(function () {
            var submitButton = $('.submit-button');
            var insertAmount = $('.request-amount');
            var quantityText = $('.quantity-text');

            submitButton.prop('disabled', true);
            insertAmount.prop('disabled', true);
            quantityText.text('');

            $('.request-form').trigger("reset");
        });
        $('.return-dropdown-menu').click(function () {
            var returnButton = $('.submit-return-button');
            returnButton.prop('disabled', true);

            $('.return-form').trigger("reset");
        });



        $('.warehouse-select').on('change', function () {
            var warehouseSelect = this;
            var warehouseId = warehouseSelect.value;
            var productId = $(warehouseSelect).siblings('.productIdInput').val();
            var insertAmount = $(warehouseSelect).siblings('.insert-div').children('.request-amount');

            insertAmount.prop('disabled', false);

            var path = "/storestock/current-stock/" + warehouseId + "/" + productId;

            $.ajax({
                type: "GET",
                contentType: "application/json",
                url: path,
                dataType: 'json',
                cache: false,
                timeout: 600000,
                success: function (data) {

                    var quantityDiv = $(warehouseSelect).siblings('.quantity-div');
                    var quantityText = quantityDiv.children('.quantity-text');

                    if ($(quantityText).is(':empty')) {
                        quantityText.append(data);
                        quantityDiv.css('visibility', 'visible');
                    } else {
                        quantityText.text('');
                        quantityText.append(data);
                        quantityDiv.css('visibility', 'visible');
                    }

                    var inStorage = quantityDiv.children('.currently-in-storage');
                    inStorage.val(data)


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

        })

        $('.submit-button').on('click', function () {
            var buttonId = this;
            var storeId = $(buttonId).siblings('.storeIdInput').val();

            var warehouseSelect = $(buttonId).siblings('.warehouse-select');
            var warehouseId = warehouseSelect.val();
            var productId = $(buttonId).siblings('.productIdInput').val();
            var productName = $(buttonId).siblings('.productNameInput').val();
            var requestAmount = $(buttonId).siblings(".insert-div").children(".request-amount").val();
            var body = {};

            body['storeId'] = +storeId;
            body['warehouseId'] = +warehouseId;
            body['productId'] = +productId;
            body['requestAmount'] = +requestAmount;

            console.log(body);

            var bodyAsJson = JSON.stringify(body);

            $.ajax({
                type: 'PATCH',
                contentType: 'application/json',
                url: "/storestock/reallocate-stock",
                data: bodyAsJson,
                dataType: 'json',
                cache: false,
                timeout: 60000,
                success: function (data) {
                    var headerText = "Successfully added more units of an article"
                    if(langParam == "rs") {
                        headerText = "Uspešno dodato jos jedinica artikla"
                    }
                    var bodyText = productName;
                    drawModal(headerText, bodyText, '/store/storedetails/' + storeId);
                },
                error: function (e) {
                    var response = JSON.parse(e.responseText);
                    alert(response.prettyErrorMessage);
                }
            })

        })

        $('.return-amount').on('input', function () {
            var returnAmount = this;
            var sumbitButton = $(returnAmount).parent().siblings('.submit-return-button');
            var quantity = $(returnAmount).val();
            var currentlyInStore = $(returnAmount).parent().siblings('.quantityInput').val();


            if (+quantity <= 0 || +quantity > +currentlyInStore) {
                returnAmount.style.border = '3px solid #FF0000';
                sumbitButton.prop('disabled', true);
            } else if (+quantity > 0 || +quantity <= +currentlyInStore) {
                returnAmount.style.border = '3px solid #0000FF';
                sumbitButton.prop('disabled', false);
            }

        })

        $('.submit-return-button').on('click', function () {
            var returnButton = this;
            var productId = $(returnButton).siblings('.productIdInput').val();
            var storeId = $(returnButton).siblings('.storeIdInput').val();
            var productName = $(returnButton).siblings('.productNameInput').val();
            var warehouseSelect = $(returnButton).siblings('.return-warehouse-select');
            var warehouseId = warehouseSelect.val();
            var returnAmount = $(returnButton).siblings(".insert-return-div").children(".return-amount").val();

            var body = {};
            body["storeId"] = +storeId;
            body["productId"] = +productId;
            body["warehouseId"] = +warehouseId;
            body["requestAmount"] = +returnAmount;
            console.log(body);

            var bodyAsJson = JSON.stringify(body);

            $.ajax({
                type: 'PATCH',
                contentType: 'application/json',
                url: "/storestock/return-product-store",
                data: bodyAsJson,
                dataType: 'json',
                cache: false,
                timeout: 60000,
                success: function (data) {
                    var headerText = "Successfully returned a product to warehouse"
                    if(langParam == "rs") {
                        headerText = "Uspešno vraćen proizvod u magacin"
                    }
                    var bodyText = productName;
                    drawModal(headerText, bodyText, '/store/storedetails/' + storeId);
                },
                error: function (e) {
                    var response = JSON.parse(e.responseText);
                    alert(response.prettyErrorMessage);
                }
            })
        });

        dataTableWithLocale('#main-table', langParam);
        dataTableWithLocale('#worker-table', langParam);
    }
);