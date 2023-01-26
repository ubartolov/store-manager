$(document).ready(

    function() {

        $(".dropdownMenuLink").click(function() {
            var submitButton = $('.submit-button');
            submitButton.prop('disabled', true);
       });

        $('.request-amount').on('input', function() {
            var requestAmount = this;
            var sumbitButton = $(requestAmount).parent().siblings('.submit-button');
            var quantity = $(requestAmount).val()
            if(+quantity <= 0) {
                requestAmount.style.border = '3px solid #FF0000';
                sumbitButton.prop('disabled', true);
            }else if(+quantity > 0) {
                requestAmount.style.border = '3px solid #0000FF';
                sumbitButton.prop('disabled', false);
            }
        })

        $('.submit-button').on('click', function() {
            var buttonId = this;
            var requestAmount = $(buttonId).siblings('.insert-div').children('.request-amount').val();
            var warehouseId = $(buttonId).siblings('.warehouseIdInput').val();
            var productId = $(buttonId).siblings('.productIdInput').val();
            var productName = $(buttonId).siblings('.productNameInput').val();
            var body = {};

            body['requestAmount'] = +requestAmount;
            body['warehouseId'] = +warehouseId;
            body['productId'] = +productId;

            var bodyAsJson = JSON.stringify(body);

            $.ajax({
                type:'PATCH',
                contentType: 'application/json',
                url: "/storestock/update-warehouse-stock",
                data: bodyAsJson,
                dataType: 'json',
                cache: false,
                timeout: 60000,
                success: function(data) {
                    alert("Successfully added " + +requestAmount + " more copies of " + productName);
                    location.reload(true);
                },
                error: function(e) {
                    var response = JSON.parse(e.responseText);
                    alert(response.prettyErrorMessage);
                }
            })
        })
    }
)