$(document).ready(

    function() {

        localeSelect();
        var langParam = Cookies.get("lang");
        var buttonText = "Request New Product";
        if (langParam == "rs") {
            buttonText = "Zatraži Nov Proizvod";
        }
    
        $('.navbar-text').css('visibility', 'visible')
        $('.reference-button').append(buttonText);

        $('.reference-button').click(function() {
            var warehouseId = $('#warehouseIdInput').val();
            var referanceButton = this;
            var link = '/warehouse/addnewproduct/' + warehouseId; 
            referanceButton.href = link;
        })

        $(".dropdownMenuLink").click(function() {
            var submitButton = $('.submit-button');
            submitButton.prop('disabled', true);
       });

        $('.request-amount').on('input', function() {
            var requestAmount = this;
            var sumbitButton = $(requestAmount).parent().siblings('.submit-button');
            var quantity = $(requestAmount).val()
            if(+quantity <= 0) {
                requestAmount.style.border = '3px solid #FF0000';
                sumbitButton.prop('disabled', true);
            }else if(+quantity > 0) {
                requestAmount.style.border = '3px solid #0000FF';
                sumbitButton.prop('disabled', false);
            }
        })

        $('.submit-button').on('click', function() {
            var buttonId = this;
            var requestAmount = $(buttonId).siblings('.insert-div').children('.request-amount').val();
            var warehouseId = $(buttonId).siblings('.warehouseIdInput').val();
            var productId = $(buttonId).siblings('.productIdInput').val();
            var productName = $(buttonId).siblings('.productNameInput').val();
            var body = {};

            body['requestAmount'] = +requestAmount;
            body['warehouseId'] = +warehouseId;
            body['productId'] = +productId;

            var bodyAsJson = JSON.stringify(body);

            $.ajax({
                type:'PATCH',
                contentType: 'application/json',
                url: "/storestock/update-warehouse-stock",
                data: bodyAsJson,
                dataType: 'json',
                cache: false,
                timeout: 60000,
                success: function(data) {
                    var headerText = "Successfully added more units of an article"
                    if(langParam == "rs") {
                        headerText = "Uspešno dodato jos jedinica artikla"
                    }
                    var bodyText = productName;
                    drawModal(headerText, bodyText, '/warehouse/warehousedetails/' + warehouseId);
                },
                error: function(e) {
                    var response = JSON.parse(e.responseText);
                    alert(response.prettyErrorMessage);
                }
            })
        })

        $('.delete-button').on('click', function() {
            var deleteButton = this;
            var productId = $(deleteButton).parent('.button-td').siblings('.td-dropdown').children('.dropdown-menu').children('.request-form')
                            .children('.insert-div').children('.productIdInput').val();
            var warehouseId = $('#warehouseIdInput').val();
            var productName = $(deleteButton).parent('.button-td').siblings('.td-dropdown').children('.dropdown-menu').children('.request-form')
            .children('.insert-div').children('.productNameInput').val();

            var body = {};
            body["storeId"] = +warehouseId;
            body["productId"] = +productId;

            var bodyAsJson = JSON.stringify(body);

            $.ajax({
                type:'PATCH',
                contentType: 'application/json',
                url: "/storestock/delete-product-warehouse",
                data: bodyAsJson,
                dataType: 'json',
                cache: false,
                timeout: 60000,
                success: function(data) {
                    var headerText = 'Successfully deleted a product';
                    if(langParam == "rs") {
                        headerText = "Uspešno obrisan proizvod"    
                    }
                    var bodyText = productName;
                    drawModal(headerText, bodyText, '/warehouse/warehousedetails/' + warehouseId);
                    
                },
                error: function(e) {
                    var response = JSON.parse(e.responseText);
                    alert(response.prettyErrorMessage);
                }
            });

        });
        dataTableWithLocale('#main-table', langParam);
        dataTableWithLocale('#worker-table', langParam);
    }
);