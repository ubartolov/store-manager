$(document).ready(

    function() {

        $('.navbar-brand').append('All Store Locations');
        $('.navbar-text').css('visibility', 'visible');

        $('.reference-button').append('Add New Store');

        $('.reference-button').click(function() {
            var referanceButton = this;
            var link = '/common/new-store/storespage'; 
            referanceButton.href = link;
        });

    }
);