/*
 * FileName：channelConfig.js
 *
 * Description：栏目管理模块 js
 *
 * History：
 * 版本号 			作者 			日期       			简介
 *  1.0   			chenchen		16/7/5			Create
 */


function addChannelDialog() {
    $("#channelForm").form('clear')
    var row = $('#channelTable').treegrid('getSelected');
    if (row != null) {
        $('#parentId').val(row.id);
        $('#level').val(row.channelLevel + 1);
        $('#channelLevel').val(row.channelLevel + 1);
        $('#parentName').val(row.channelName);
    } else {
        $('#parentId').val($('#zeroChannelId').val());
        $('#level').val(1);
        $('#channelLevel').val(1);
        $('#parentName').val(null);
    }

    openDlg('新增栏目', 'add');

}
function addORUpdateChannel() {
    $('#channelForm').form('enableValidation');
    if($('#channelForm').form('validate')){
        var method = 'addChannel';
        if ($('#channelForm #id').val()) {
            method = 'updateChannel';
        }
        $.ajax({
            url: BASE_HREF+'manager/channel/' + method,
            data: $('form').serialize(),
            type: 'POST',
            beforeSend: function () {
                $('#dlg_m_channel').dialog('close');
                ajaxLoading('正在提交请稍候。。。');
            },
            success: function (data) {
                ajaxLoadEnd();
                switch (data.state) {
                    case 0:
                        $.messager.alert('失败', data.message, 'error', function () {
                            $('#dlg_m_channel').dialog('open');
                        });
                        break;
                    case 1:
                        $.messager.alert('成功', '操作成功');
                        $('#channelTable').treegrid('unselectAll');
                        oldSelectedRow=null;
                        $('#channelTable').treegrid('reload');
                        break;
                    case 2:
                        $.messager.alert('失败', '验证不通过', 'error', function () {
                            $('#dlg_m_channel').dialog('open');
                        });
                        break;
                    default:
                        break;
                }
            }
        });
    }

}
function updateChannelDialog() {
    var row = $('#channelTable').treegrid('getSelected');
    if (row == null) {
        $.messager.alert('出错', '请先选择要修改的数据', 'error');
        return;
    }
    $('#channelForm').form('load', row);
    $('#id').val(row.id);
    $('#level').val(row.channelLevel);
    $('#channelLevel').val(row.channelLevel);
    if (row.parentId != null ) {
        var parentRow = $('#channelTable').treegrid('find', row.parentId);
        if (parentRow==undefined){
            parentRow={
                channelName:'WarehouseManagement 管理系统'
            }
        }
        $('#parentName').val(parentRow.channelName);
        $('#parentId').val(row.parentId);
    } else {
        $('#parentName').val(null);
        $('#parentId').val($('#zeroChannelId').val());
    }
    openDlg('修改栏目', 'update',row.id);
}
function deleteChannelDialog() {
    var row= $('#channelTable').treegrid('getSelected');
    if (row == null) {
        $.messager.alert('出错', '请先选择要删除的数据', 'error');
        return;
    }
    row = row.id;
    $.messager.confirm('确认', '您是否确认要删除数据，如果数据包含子节点，将同步删除子节点数据?', function (t) {
        if (t) {
            $.ajax({
                url: BASE_HREF+'manager/channel/delChannel',
                data: 'id=' + row,
                type: 'POST',
                beforeSend: function () {
                    ajaxLoading('正在提交请稍候。。。');
                },
                success: function (data) {
                    ajaxLoadEnd();
                    switch (data.state) {
                        case 0:
                            $.messager.alert('失败', data.message, 'error');
                            break;
                        case 1:
                            $.messager.alert('成功', '操作成功');
                            $('#channelTable').treegrid('unselectAll');
                            oldSelectedRow=null;
                            $('#channelTable').treegrid('reload');
                            break;
                        case 2:
                            $('#channelTable').treegrid('reload');
                            $.messager.alert('提示', "当前数据已被用户:"+data.lockedUser+"锁定,请稍候再试!", "info");
                            break;
                        default:
                            break;
                    }
                }
            });
        }
    });


}
var oldSelectedRow=null;
function clickRow(row) {
    if (oldSelectedRow==null){
        oldSelectedRow=row;
        return;
    }else if (row.id==oldSelectedRow.id){
        oldSelectedRow=null;
        $('#channelTable').treegrid('unselectAll');
    }else{
        oldSelectedRow=row;
    }
}
function dblClickRow(data) {
    if (data != null) {
        $('form').form('load', data);
        $('#level').val(data.channelLevel);
        $('#channelLevel').val(data.channelLevel);
        if (data.parentId != null ) {
            var parentRow = $('#channelTable').treegrid('find', data.parentId);
            if (parentRow==undefined){
                parentRow={
                    channelName:'WarehouseManagement 管理系统'
                }
            }
            $('#parentName').val(parentRow.channelName);
            $('#parentId').val(data.parentId);
        } else {
            $('#parentName').val(null);
            $('#parentId').val($('#zeroChannelId').val());
        }

    } else {
        $.messager.alert('出错', '请先选择要修改的数据', 'error');
        return;
    }
    openDlg('修改栏目', 'update',data.id);
}
function formatFunction(value, row, index) {
    return '<a href="javaScript:void(0)" onclick="roleFunctionListList(\'' + row.id + '\')">管理</a>';
}
function roleFunctionListList(id) {
    $('#win_user_config').window({
        title: '功能管理',
        width: 800,
        height: 600,
        modal: true,
        draggable: true,
        resizable: true,
        collapsible: false,
        minimizable: false,
        maximizable: false,
        inline: true,
        onMove: onMoveWindow,
        href: BASE_HREF+'manager/function',
        queryParams: {channelId: id, channelName: $('#channelTable').treegrid('find', id).channelName},
        method: 'post',
        onBeforeOpen: function () {
            if (id != null) {
                CHECK_DATALOCK_FLAG = true;
                ajaxLoading('正在查询请稍候。。。');
                var flag = checkLock(id);
                ajaxLoadEnd();
                return flag;
            } else {
                return true;
            }
        },
        onClose: function () {
            CHECK_DATALOCK_FLAG = false;
        }
    });
    event.stopPropagation();
}
function openDlg(title, type,id) {
    if (type == 'add') {
        $('#dlg_buttons_m_channel_add').show();
        $('#dlg_buttons_m_channel_update').hide();
    } else {
        $('#dlg_buttons_m_channel_add').hide();
        $('#dlg_buttons_m_channel_update').show();
    }
    $('#dlg_m_channel').dialog({
        closed: true,
        inline: true,
        modal: true,
        buttons: '#dlg_buttons_m_channel',
        onMove: onMoveDialog,
        onOpen: function () {
            $('#channelName').focus();
        },
        onBeforeOpen: function () {
            if (id != null) {
                CHECK_DATALOCK_FLAG = true;
                ajaxLoading('正在查询请稍候。。。');
                var flag = checkLock(id);
                ajaxLoadEnd();
                return flag;
            } else {
                return true;
            }
        },
        onClose: function () {
            CHECK_DATALOCK_FLAG = false;
        }
    });
    $('#dlg_m_channel').dialog('open').dialog('setTitle', title);
    $('#channelForm').form('resetValidation');
    $('#channelForm').form('disableValidation');
}
function expendChannelNodeAll() {
    $('#channelTable').treegrid('expandAll');
}
