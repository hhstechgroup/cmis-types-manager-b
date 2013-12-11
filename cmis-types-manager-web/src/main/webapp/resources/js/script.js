//used in create.xhtml oncomplete event for #newSourceSystemButton
function triggerEdit(){
    $('.ui-datatable-data tr:last').find('.ui-icon-pencil').trigger('click')
}

$(document).ready(function(){
//    var id = $('#treeForm-currentSelected').val();
//    $('#'+id).attr('aria-selected', true);
//    $('#'+id).find('.ui-treenode-label').addClass('ui-state-highlight');
//    console.log('#'+id);
    $('#treeForm').find('.ui-treenode-label').first().addClass('ui-state-highlight');
//    $('ul.ui-tree-container').find('li').click(function () {
//        $('#treeForm-currentSelected').val($(this).attr('id'));
//        return true;
//    });
});