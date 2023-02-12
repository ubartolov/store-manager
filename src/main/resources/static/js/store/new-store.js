$(document).ready(

    function () {

        $('.navbar-brand').append('New Store');

        
        $('.navbar-text').css('visibility', "visible");
        $('.reference-button').css('visibility', "hidden");
        $('.navbar-text').append("Enter Store Details");

        $('submit-button').prop('disabled', true);

        $('.submit-button').on('input', function() {
            var button = this;
            button.prop('disabled', false);
        });
        $('.submit-button').on('click', function () {

            var storeId = $('#storeIdInput').val();
            var address = $('#address-input').val();
            var storeDiv = $('#store-options');
            var storeType = $(storeDiv).val();
            var link = $('#origin').val();
            var body = {};

            body["storeId"] = storeId;
            body["address"] = address;
            body["storeType"] = storeType;

            var bodyAsJson = JSON.stringify(body);
            console.log(body);
            console.log(link);

            $.ajax({
                type: 'POST',
                contentType: 'application/json',
                url: "/store/add-new-store",
                data: bodyAsJson,
                cache: false,
                timeout: 50000,
                success: function () {
                    var headerText = 'Successfully added a new Store';

                    drawModal(headerText, address, link)
                },
                error: function (e) {
                    var response = JSON.parse(e.responseText);
                    alert(response.prettyErrorMessage);
                }
            });
        });

        var storeId = $('#storeIdInput').val();

        if (storeId != null && storeId != "") {
            $.ajax({
                type: 'GET',
                contentType: 'application/json',
                url: "/store/edit-store/" + storeId,
                dataType: 'json',
                cache: false,
                timeout: 50000,
                success: function (data) {
                    console.log(data);
                    $("#address-input").val(data.address);
                    $("#store-options option[value='" + data.storeType + "']").prop('selected', true);

                },
                error: function (e) {
                    alert(response.prettyErrorMessage);
                }
            });
        }


    }
);