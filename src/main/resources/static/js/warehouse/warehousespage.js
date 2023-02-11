$(document).ready(
    
    function() {

        $('.navbar-brand').append('All Warehouse Locations');
        
        $('.navbar-brand').css("color", "white");
        $('.reference-button').append('Add New Store');
        $('.navbar-text').css('visibility', 'visible');
        
        $('.reference-button').click(function() {
            var referanceButton = this;
            var link = '/common/new-store/warehousedetails'; 
            referanceButton.href = link;
        });
    }
);