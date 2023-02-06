$(document).ready(

    function () {

        $('.navbar-brand').append('Staff Information');
        $('.navbar-text').css('visibility', 'visible')
        $('.reference-button').append('Add new worker');
        $('.reference-button').click(function() {
            var referanceButton = this;
            var link = '/worker/add-new-worker'; 
            referanceButton.href = link;
        });

    

        $('.request-button').on('click', function() {
            var button = this;
            var workerId = $(button).siblings('.workerIdInput').val();
            var firstName = $(button).siblings('.firstNameInput').val();
            var lastName = $(button).siblings('.lastNameInput').val();
            var storeSelect = $(button).siblings('.transfer-to-div').children('.transfer-select');
            var storeId = storeSelect.val();

            var body = {};
            body["workerId"] = +workerId;
            body["storeId"] = +storeId;

            var bodyAsJson = JSON.stringify(body);
            console.log(bodyAsJson);
            $.ajax({
                type: 'PATCH',
                contentType: 'application/json',
                url: "/worker/transfer-worker",
                data: bodyAsJson,
                cache: false,
                timeout: 50000,
                success: function () {
                    var headerText = 'Successfully transfered ' + firstName + ' ' + lastName;
                    drawModal(headerText, firstName, "/worker/staffdetails")
                },
                error: function (e) {
                    var response = JSON.parse(e.responseText);
                    alert(response.prettyErrorMessage);
                }
            }); 
        });


        $('#main-table').DataTable();
        
    }
);