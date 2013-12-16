//used in create.xhtml oncomplete event for #newSourceSystemButton
function triggerEdit() {
    $('.ui-datatable-data tr:last').find('.ui-icon-pencil').trigger('click')
}

$(document).ready(function () {
    $('#treeForm').find('.ui-treenode-label').first().addClass('ui-state-highlight');
});