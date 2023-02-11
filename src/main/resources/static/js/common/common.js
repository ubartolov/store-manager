function drawModal(headerText, bodyText, redirectTo) {
    $('#addModal').modal('show');
    $('#addModalLabel').append(headerText);
    $('.modal-body').append(bodyText);
    $('.close-modal-button-add').on('click', function () {
        if (redirectTo != null && redirectTo != "") {
            window.location.href = redirectTo;
        }
    });
}

function drawModalWithRetrurnedData(headerText, data, redirectTo) {
    $('#addModal').modal('show');
    $('#addModalLabel').append(headerText);
    var nameModal = $('.modal-body').children('.name-modal');
    var priceModal = $('.modal-body').children('.price-modal');
    var quantityModal = $('.modal-body').children('.quantity-modal');

    $.each(data, function (index, item) {
        var nameModalClone = nameModal.clone();
        var priceModalClone = priceModal.clone();
        var quantityModalClone = quantityModal.clone();

        nameModalClone.append("Product Name : " + item.productName);
        priceModalClone.append("Price : " + item.productPrice);
        quantityModalClone.append("Quantity : " + item.requestAmount);

        nameModalClone.appendTo('.modal-body');
        priceModalClone.appendTo('.modal-body');
        quantityModalClone.appendTo('.modal-body');
        $('.modal-body').append('<hr class="hr" />');
    });

    $('.close-modal-button-add').on('click', function () {
        window.location = redirectTo;
    });
}
