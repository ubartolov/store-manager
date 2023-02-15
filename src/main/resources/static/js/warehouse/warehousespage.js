$(document).ready(
    
    function() {
        
        $('.reference-button').append('Add New Store');
        $('.navbar-text').css('visibility', 'visible');
        
        $('.reference-button').click(function() {
            var referanceButton = this;
            var link = '/common/new-store/warehousedetails'; 
            referanceButton.href = link;
        });

        $('.delete-button').on('click', function () {
            var address = $(this).parent('.list-group-item').parent('.list-group').siblings('.warehouseInputAddress').val();
            var storeId = $(this).parent('.list-group-item').parent('.list-group').siblings('.warehouseInputId').val();

            console.log(address);
            console.log(storeId);

            $.ajax({
                type: 'PATCH',
                contentType: 'application/json',
                url: "/delete-store/" + storeId,
                cache: false,
                timeout: 60000,
                success: function () {
                    var headerText = "Successfully removed a Warehouse";
                    var bodyText = address;
                    drawModal(headerText, bodyText, '/warehouse/warehousedetails');

                },
                error: function (e) {
                    var response = JSON.parse(e.responseText);
                    alert(response.prettyErrorMessage);
                }
            })
        });
    }
);