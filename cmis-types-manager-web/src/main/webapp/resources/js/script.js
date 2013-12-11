$(document).ready(function () {
    $('#treeForm').find('.ui-treenode-label').first().addClass('ui-state-highlight');
    $('ul.ui-tree-container').find('li').click(function () {
        $('#treeForm-currentSelected').val($(this).attr('id'));
        return true;
    });
});