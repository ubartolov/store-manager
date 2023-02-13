$(document).ready(

    function() {

        $('.navbar-brand').append('All Store Locations');
        $('.navbar-text').css('visibility', 'visible');

        $('.reference-button').append('Add New Store');

        $('.reference-button').click(function() {
            var referanceButton = this;
            var link = '/common/new-store/storespage'; 
            referanceButton.href = link;
        });


        $('.delete-button').on('click', function () {
            var address = $(this).siblings('.storeInputAddress').val();
            var storeId = $(this).siblings('.storeInputId').val();

            console.log(address);
            console.log(storeId);

            $.ajax({
                type: 'PATCH',
                contentType: 'application/json',
                url: "/delete-store/" + storeId,
                cache: false,
                timeout: 60000,
                success: function () {
                    var headerText = "Successfully removed a Store";
                    var bodyText = address;
                    drawModal(headerText, bodyText, '/store/storespage');

                },
                error: function (e) {
                    var response = JSON.parse(e.responseText);
                    alert(response.prettyErrorMessage);
                }
            })
        });
    }
);