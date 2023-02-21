$(document).ready(

    function () {
        localeSelect();

        var buttonText = "Add New Warehouse";
        var langParam = Cookies.get("lang");
        if (langParam == "rs") {
            buttonText = "Dodaj Magacin";
        }

        $('.reference-button').append(buttonText);
        $('.navbar-text').css('visibility', 'visible');

        $('.reference-button').click(function () {
            var referanceButton = this;
            var link = '/common/new-store/warehousedetails';
            referanceButton.href = link;
        });

        // $('.delete-button').on('click', function () {
        //     var address = $(this).parent('.list-group-item').parent('.list-group').siblings('.warehouseInputAddress').val();
        //     var storeId = $(this).parent('.list-group-item').parent('.list-group').siblings('.warehouseInputId').val();

        //     console.log(address);
        //     console.log(storeId);

        //     $.ajax({
        //         type: 'PATCH',
        //         contentType: 'application/json',
        //         url: "/delete-store/" + storeId,
        //         cache: false,
        //         timeout: 60000,
        //         success: function () {
        //             var headerText = "Successfully removed a Warehouse";
        //             var bodyText = address;
        //             drawModal(headerText, bodyText, '/warehouse/warehousedetails');

        //         },
        //         error: function (e) {
        //             var response = JSON.parse(e.responseText);
        //             alert(response.prettyErrorMessage);
        //         }
        //     })
        // });

        $('.request-button').on('click', function () {
            
            var existingWarehouseId = $(this).siblings('.warehouseIdInput').val();
            var existingWarehouseAddress = $(this).siblings('.warehouseAddressInput').val();
            var warehouseId = $(this).siblings('.delete-warehouse-select').find('option:selected').val();

            console.log(existingWarehouseId);
            console.log(warehouseId);

            $.ajax({
                type: 'PATCH',
                contentType: 'application/json',
                url: "/warehouse/delete-store/" + existingWarehouseId + "/"+ warehouseId,
                cache: false,
                timeout: 60000,
                success: function () {
                    var headerText = "Successfully returned all products and removed a warehouse";
                    if(langParam == "rs") {
                        headerText = "Uspešno vraćeni svi proizvodi i obrisan Magacin";
                    }
                    var bodyText = existingWarehouseAddress;
                    drawModal(headerText, bodyText, '/warehouse/warehousespage');

                },
                error: function (e) {
                    var response = JSON.parse(e.responseText);
                    alert(response.prettyErrorMessage);
                }
            })
        });
    }
);