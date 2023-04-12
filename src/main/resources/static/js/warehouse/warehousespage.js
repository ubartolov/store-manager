$(document).ready(

    function () {
        localeSelect();

        var buttonText = "Add New Warehouse";
        var langParam = Cookies.get("lang");
        if (langParam == "rs") {
            buttonText = "Dodaj Nov Magacin";
        }

        $('.reference-button').append(buttonText);
        $('.navbar-text').css('visibility', 'visible');

        $('.reference-button').click(function () {
            var referanceButton = this;
            var link = '/common/new-store/warehousedetails';
            referanceButton.href = link;
        });

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
                    var headerText = "Successfully returned all products and removed a Warehouse";
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