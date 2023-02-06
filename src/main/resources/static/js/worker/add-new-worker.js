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
            body['workerId'] = +workerId;

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

            // var tableRow = '<tr class="table-detail-row">'
            //     + '<td class="firstNameData">' + firstName + '</td>'
            //     + '<td class="lastNameData">' + lastName + '</td>'
            //     + '<td class="emailData">' + email + '</td>'
            //     + '<td class="addressData">' + homeAddress + '</td>'
            //     + '<td class="positionNameData">' + positionName + '</td>'
            //     + '<td class="positionNameData">' + storeAddress + '</td>'
            //     + '<td><button type="button" class="btn btn-danger remove-button">Remove</button></td>'
            //     + '<input type="hidden" class="positionIdData" value="' + positionId + '">'
            //     + '<input type="hidden" class="storeIdData" value="' + storeId + '">'
            //     + '</tr>'

            // $('#workerInfoTable').append(tableRow);
            // $('.submit-button').prop('disabled', true);
            // $('.finish-button').prop('disabled', false);
            // $('.worker-form').trigger("reset");


        
        // $('#requestListTable').on('click', '.remove-button', function () {
        //     $(this).parent().parent().remove();

        // });
        // $('.finish-button').on('click', function () {

        //     var fullName;
        //     var requestList = [];

        //     $("#workerInfoTable tr.table-detail-row").each(function () {

        //         var firstName = $(this).find(".firstNameData").text();
        //         var lastName = $(this).find(".lastNameData").text();
        //         var email = $(this).find(".emailData").text();
        //         var address = $(this).find(".addressData").text();
        //         var positionId = $(this).find(".positionIdData").val();
        //         var storeId = $(this).find(".storeIdData").val();
        //         fullName = firstName + ' ' + lastName;

        //         body = {}

        //         body['firstName'] = firstName;
        //         body['lastName'] = lastName;
        //         body['email'] = email;
        //         body['homeAddress'] = address;
        //         body['positionId'] = +positionId;
        //         body['storeId'] = +storeId;


        //         requestList.push(body);
        //     });

        //     var bodyAsJson = JSON.stringify(requestList);

        //     console.log(bodyAsJson);

        //     $.ajax({
        //         type: 'POST',
        //         contentType: 'application/json',
        //         url: "/worker/new-worker",
        //         data: bodyAsJson,
        //         dataType: 'json',
        //         cache: false,
        //         timeout: 50000,
        //         success: function (data) {
        //             var headerText = 'Successfully added a new Worker';
        //             drawModal(headerText, fullName, "/worker/staffdetails")
        //         },
        //         error: function (e) {
        //             var response = JSON.parse(e.responseText);
        //             alert(response.prettyErrorMessage);
        //         }
        //     });
        // });



        $('#main-table').DataTable();
    }
);