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

function localeSelect() {
    var select = $('#locales');

    var langParam = Cookies.get("lang");
    var currentLang = "";
    console.log(langParam);
    if (langParam == "us") {
        currentLang = "us";
    }
    if (langParam == "rs") {
        currentLang = "rs";
    }
    console.log(langParam);
    select.val(currentLang).attr('selected', true);

    $("#locales").change(function () {
        var selectedOption = select.val();
        if (selectedOption != '') {
            var currentLocation = window.location.href;
            var urlStripped = currentLocation.split("?")[0];;
            window.location = urlStripped + "?lang=" + selectedOption;
        }
    });
}

function dataTableWithLocale(dataTableId, langParam) {
    var lengthMenu = "Showing _MENU_ records per page";
    var zeroRecords = "Nothing found";
    var info = "Showing page _PAGE_ of _PAGES_";
    var infoEmpty = "No records available";
    var infoFiltered = "(filtered from _MAX_ total records)";
    var search = "Search";
    var previous = "Previous";
    var next = "Next";
    var first = "First";
    var last = "Last";

    if (langParam == "rs") {
        lengthMenu = "Prikaži _MENU_ unosa po strani";
        zeroRecords = "Nema rezultata";
        info = "Prikaz strane _PAGE_ od _PAGES_";
        infoEmpty = "Nema dostupnih unosa";
        infoFiltered = "(izdvojeno id _MAX_ unosa)";
        search = "Pretraži";
        previous = "Prethodna";
        next = "Sledeća";
        first = "Prva";
        last = "Poslednja";
    }

    $(dataTableId).DataTable({

        "language": {
            "lengthMenu": lengthMenu,
            "zeroRecords": zeroRecords,
            "info": info,
            "infoEmpty": infoEmpty,
            "infoFiltered": infoFiltered,
            "search": search,
            "paginate": {
                "first": first,
                "last": last,
                "next": next,
                "previous": previous
            }
        }
    });
}

