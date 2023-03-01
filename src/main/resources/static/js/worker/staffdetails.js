$(document).ready(

    function () {
        localeSelect();
        var langParam = Cookies.get("lang");
        var buttonText = "Add new worker";
        if(langParam == "rs") {
            buttonText = "Dodaj Radnika"
        }


        $('.navbar-text').css('visibility', 'visible')
        $('.reference-button').append(buttonText);
        $('.reference-button').click(function() {
            var referanceButton = this;
            var link = '/worker/add-new-worker'; 
            referanceButton.href = link;
        });

        $('.transfer-button').on('click', function () {
            var button = this;
            var workerId = $(button).siblings('.dropdown-menu').children('.transfer-form').children(".workerIdInput").val();
    
            $.ajax({
                type: 'GET',
                contentType: 'application/json',
                url: "/worker/get-worker/" + workerId,
                dataType: 'json',
                cache: false,
                timeout: 50000,
                success: function (data) {
                    $(".transfer-select option[value='" + data.storeId + "']").prop('selected', true);
                },
                error: function (e) {
                    var response = JSON.parse(e.responseText);
                    alert(response.prettyErrorMessage);
                }
            }); 
        })
    

        $('.request-button').on('click', function() {
            var button = this;
            var workerId = $(button).siblings('.workerIdInput').val();
            var firstName = $(button).siblings('.firstNameInput').val();
            var lastName = $(button).siblings('.lastNameInput').val();
            var storeSelect = $(button).siblings('.transfer-to-div').children('.transfer-select');
            var storeId = storeSelect.val();
            var storeAddress = $(storeSelect).find("option:selected").text();
            

            var body = {};
            body["workerId"] = +workerId;
            body["storeId"] = +storeId;

            var bodyAsJson = JSON.stringify(body);

            $.ajax({
                type: 'PATCH',
                contentType: 'application/json',
                url: "/worker/transfer-worker",
                data: bodyAsJson,
                cache: false,
                timeout: 50000,
                success: function () {
                    var headerText = 'Successfully transfered ' + firstName + ' ' + lastName;
                    if(langParam = "rs") {
                        headerText = "Uspešno prebačen/a" +  firstName + ' ' + lastName; 
                    }
                    drawModal(headerText, storeAddress, "/worker/staffdetails");
                },
                error: function (e) {
                    var response = JSON.parse(e.responseText);
                    alert(response.prettyErrorMessage);
                }
            }); 
        });

        $('.delete-button').on('click', function() {
            
            var workerId = $(this).parent('.delete-dropdown').siblings('.transfer-dropdown').children('.dropdown-menu').children('.transfer-form').children('.workerIdInput').val();
            var firstName = $(this).parent('.delete-dropdown').siblings('.transfer-dropdown').children('.dropdown-menu').children('.transfer-form').children('.firstNameInput').val();
            var lastName = $(this).parent('.delete-dropdown').siblings('.transfer-dropdown').children('.dropdown-menu').children('.transfer-form').children('.lastNameInput').val();
            var fullName = firstName + ' ' + lastName;
            
            console.log(workerId);
            $.ajax({
                type: 'PATCH',
                contentType: 'application/json',
                url: "/worker/delete-worker/" + workerId,
                cache: false,
                timeout: 50000,
                success: function () {
                    var headerText = 'Successfully deleted a worker';
                    if(langParam == "rs") {
                        headerText = "Uspešno obrisan radnik"
                    }
                    drawModal(headerText, fullName, "/worker/staffdetails");
                },
                error: function (e) {
                    var response = JSON.parse(e.responseText);
                    alert(response.prettyErrorMessage);
                }
            }); 
        })


        dataTableWithLocale('#main-table', langParam);
        
    }
);