
$('#filters-select').click(function(){
    $(this).toggleClass('filters-selected')

    $('.filter-container').toggle(200);

    if($('#filters-flag').val() == "no") {
        $('#filters-flag').val("yes")
    }
    else {
        $('#filters-flag').val("no")
    }
});