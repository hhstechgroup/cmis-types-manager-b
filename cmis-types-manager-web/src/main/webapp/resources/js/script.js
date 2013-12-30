//used in create.xhtml oncomplete event for #newSourceSystemButton
function triggerEdit() {
    $('.ui-datatable-data tr:last').find('.ui-icon-pencil').trigger('click')
}

$(document).ready(function () {
    var html = '<div id="dfsgsfgsdfg" style="position: absolute;width: 100%;height: 100%;z-index: 100;top: 0;left: 0;"></div>';
    $("#leftMenuForm").find('nav').append(html);
});