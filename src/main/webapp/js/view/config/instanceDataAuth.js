/*
 * FileName：roleDataAuth.js
 *
 * Description：
 *
 * History：
 * 版本号 			作者 			日期       			简介
 *  1.0   			chenchen		16/7/7			Create
 */

/**
 * 数据源搜索标签点击事件回调函数
 * @param obj
 */
function sourceClick(obj) {
    $(obj).toggleClass('li-selected');
    if ($(obj).text() != '更多')
       // searchInstance();
    searchDataBase();
}
/**
 * 数据库数据检索
 */
function searchDataBase(value) {
    var dataParam = {
        value: null, sourceIds: null
    }
    if (value != null && value != '') {
        dataParam.value = value;
    }
    var selectedSource = '';
    $("#sourceTd").find('.li-selected').each(function () {
        if ($(this).text() != '更多')
            selectedSource += $(this).attr('name') + ',';
    })

    if (selectedSource.length > 0) {
        selectedSource = selectedSource.substr(0, selectedSource.length - 1);
        dataParam.sourceIds = selectedSource;
    }

    $('#instanceTable').datagrid('uncheckAll');
    $('#instanceTable').datagrid('load', dataParam);
}
/**
 * 展开标签点击事件回掉函数,用于展开隐藏的数据
 * @param obj
 */
function slideToggleDiv(obj) {
    $(obj).toggleClass('li-selected');
    $(obj).parent().next('div').slideToggle('slow', function () {
        $('#instanceTable').datagrid('resize');
    });
}
