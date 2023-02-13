$(document).ready(

    function () {
    
        var workerId = $('#workerIdInput').val();

        $.ajax({
            type: 'GET',
            contentType: 'application/json',
            url: "/worker/edit-worker/" + workerId,
            dataType: 'json',
            cache: false,
            timeout: 50000,
            success: function (data) {
                console.log(data);
                $("#first-name-input").val(data.firstName);
                $("#last-name-input").val(data.lastName);
                $("#email-input").val(data.email);
                $("#address-input").val(data.homeAddress);
                $("#workerId").val(data.workerId);

                $("#worker-position-select option[value='" + data.positionId + "']").prop('selected', true);
                $("#worker-store-select option[value='" + data.storeId + "']").prop('selected', true);
                
            },
            error: function (e) {
                alert(response.prettyErrorMessage);
            }
        });
        
        
        $('.request-button').prop('disabled', true);
        $('.navbar-brand').append('New Worker');
        $('.navbar-text').css('visibility', "visible");
        $('.reference-button').css('visibility', "hidden");
        $('.navbar-text').append("Enter Worker Information");
        
        $('.submit-button').prop('disabled', true);
        $('.finish-button').prop('disabled', true);

        $('.first-alignment-div').on('input', function () {
            var inputFirstName = $("#first-name-input").val();
            var inputLastName = $('#last-name-input').val();
            var inputEmail = $('#email-input').val();
            var inputHomeAddress = $('#address-input').val();
            var selectDiv = $('#worker-position-select');
            var positionId = selectDiv.value;

            if (inputFirstName != null && inputLastName != null && inputHomeAddress != null && inputEmail != null) {
                $('.submit-button').prop('disabled', false);
            }
        });
        $('.submit-button').on('click', function () {
            var firstName = $('#first-name-input').val();
            var lastName = $('#last-name-input').val();
            var email = $('#email-input').val();
            var homeAddress = $('#address-input').val();
            var selectPositionDiv = $('#worker-position-select');
            var positionId = $(selectPositionDiv).val();
            var selectStoreDiv = $('#worker-store-select');
            var storeId = $(selectStoreDiv).val();
            var workerId = $("#workerId").val();
            

            var fullName = firstName + ' ' + lastName;

            var body = {};

            body['firstName'] = firstName;
            body['lastName'] = lastName;
            body['email'] = email;
            body['homeAddress'] = homeAddress;
            body['positionId'] = +positionId;
            body['storeId'] = +storeId;
            if (workerId != null && workerId != "") {
                body['workerId'] = +workerId;
            }
            var bodyAsJson = JSON.stringify(body);
            console.log(bodyAsJson);

            $.ajax({
                type: 'POST',
                contentType: 'application/json',
                url: "/worker/new-worker",
                data: bodyAsJson,
                cache: false,
                timeout: 50000,
                success: function () {
                    var headerText = 'Successfully added a new Worker';
                    drawModal(headerText, fullName, "/worker/staffdetails")
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