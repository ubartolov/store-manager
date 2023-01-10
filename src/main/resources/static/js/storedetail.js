$(document).ready(

    function () {
        $(".dropdownMenuLink").click(function() {
            var submitButton = $('.submit-button');
            var insertAmount = $('.request-amount');
            submitButton.prop('disabled', true);
            insertAmount.prop('disabled', true);
            
       });

        $('.warehouse-select').on('change', function () {
            var warehouseSelect = this;
            var warehouseId = warehouseSelect.value;
            var productId = $(warehouseSelect).siblings('.productIdInput').val();
            var insertAmount = $(warehouseSelect).siblings('.insert-div').children('.request-amount');

            insertAmount.prop('disabled', false);

            var path = "/storestock/current-stock/" + warehouseId + "/" + productId;

            $.ajax({
                type: "GET",
                contentType: "application/json",
                url: path ,
                dataType: 'json',
                cache: false,
                timeout: 600000,
                success: function (data) {
    
                    var quantityDiv = $(warehouseSelect).siblings('.quantity-div');
                    var quantityLabel = quantityDiv.children('.quantity-label');

                    quantityLabel.append(data);
                    quantityDiv.css('visibility', 'visible');

                    var inStorage = quantityDiv.children('.currently-in-storage');
                    inStorage.val(data)
                    

                },
                error: function (e) {
                    console.log("ERROR : ", e);
                    
                }


            });
        });

        $('.request-amount').on('input', function() {
            var requestAmount = this;
            var sumbitButton = $(requestAmount).parent().siblings('.submit-button');
            var quantity = $(requestAmount).val();
        
            var currentlyInStorage = $(requestAmount).parent().siblings('.quantity-div').children('.currently-in-storage');
            

            if(+quantity > +$(currentlyInStorage).val() || +quantity <= 0) {
                requestAmount.style.border = '3px solid #FF0000';
                sumbitButton.prop('disabled', true);
            } else if(+quantity <= +$(currentlyInStorage).val()){
                requestAmount.style.border = '3px solid #0000FF';
                sumbitButton.prop('disabled', false);
            }


            //  if(Number(quantity) >= Number($(currentlyInStorage).val())) {
            //     requestAmount.style.border = '3px solid #FF0000';
            //  } else if(Number(quantity) <= Number($(currentlyInStorage).val())){
            //     requestAmount.style.border = '3px solid #0000FF';
            //  }
         })

         $('.submit-button').on('click', function () {
            var buttonId = this;
            var storeId = $(buttonId).siblings('.storeIdInput').val();

            var warehouseSelect = $(buttonId).siblings('.warehouse-select');
            var warehouseId = warehouseSelect.val();
            var productId = $(buttonId).siblings('.productIdInput').val();
            var requestAmount = $(buttonId).siblings(".insert-div").children(".request-amount").val();
            var body = {}

            body['storeId'] = +storeId;
            body['warehouseId'] = +warehouseId;
            body['productId'] = +productId;
            body['requestAmount'] = +requestAmount;

            var bodyAsJson = JSON.stringify(body);

            $.ajax({
                type:'PATCH',
                contentType: 'application/json',
                url: "/storestock/reallocate-stock",
                data: bodyAsJson,
                dataType: 'json',
                cache: false,
                timeout: 60000,
                success: function(data) {
                    location.reload(true);
                },
                error: function(e) {
                    var response = JSON.parse(e.responseText);
                    alert(response.prettyErrorMessage);
                }
            })

         })
    }
);