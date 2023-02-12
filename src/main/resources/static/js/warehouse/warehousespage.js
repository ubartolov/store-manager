$(document).ready(
    
    function() {

        $('.navbar-brand').append('All Warehouse Locations');
        

        $('.reference-button').append('Add New Store');
        $('.navbar-text').css('visibility', 'visible');
        
        $('.reference-button').click(function() {
            var referanceButton = this;
            var link = '/common/new-store/warehousedetails'; 
            referanceButton.href = link;
        });
    }
);