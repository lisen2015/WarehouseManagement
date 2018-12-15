/*
 * FileName：codeList.js
 *
 * Description：
 *
 * History：
 * 版本号 			作者 			日期       			简介
 *  1.0   			chenchen		16/7/6			Create
 */

function addCodeDialog() {
    $("#codeForm").form('clear')
    var row = $('#codeTable').treegrid('getSelected');
    $('#id').prop('readonly', false);
    $('#ids').val('-1');
    if (row != null) {
        $('#parentId').val(row.id);
        $('#level').val(row.codeLevel + 1);
        $('#codeLevel').val(row.codeLevel + 1);
        $('#parentName').val(row.codeDesc);
    } else {
        $('#parentId').val(0);
        $('#level').val(1);
        $('#codeLevel').val(null);
        $('#parentName').val(null);
    }
    openDlgChannel('新增', 'add');

}
function addORUpdateCode() {
    $('#codeForm').form('enableValidation');
    if ($('#codeForm').form('validate')) {
        var method = 'add';
        if ($('#ids').val() != -1) {
            method = 'update';
        }
        $.ajax({
            url: BASE_HREF + 'manager/code/' + method,
            data: $('form').serialize(),
            type: 'POST',
            beforeSend: function () {
                $('#dlg_m_code').dialog('close');
                ajaxLoading('正在提交请稍候。。。');
            },
            success: function (data) {
                ajaxLoadEnd();
                switch (data.state) {
                    case 0:
                        $.messager.alert('失败', data.message, 'error', function () {
                            $('#dlg_m_code').dialog('open');
                        });
                        break;
                    case 1:
                        $.messager.alert('成功', '操作成功');
                        parentRow = $('#codeTable').treegrid('find',
                            $('#parentId').val());
                        if (parentRow != null) {
                            parentRow.state = 'closed';
                            $('#codeTable')
                                .treegrid('refresh', $('#parentId').val());
                            $('#codeTable').treegrid('reload', $('#parentId').val());
                        } else {
                            $('#codeTable').treegrid('reload');
                        }
                        oldSelectedRow = null;
                        $('#codeTable').treegrid('unselectAll');
                        break;
                    case 2:
                        $.messager.alert('失败', '验证不通过', 'error', function () {
                            $('#dlg_m_code').dialog('open');
                        });

                        break;
                    default:
                        break;
                }
            }
        });
    }
}
function updateCodeDialog() {
    $('#id').prop('readonly', true);
    var row= $('#codeTable').treegrid('getSelected');
    if (row != null) {
        $('form').form('load', row);
        $('#ids').val($('#id').val());
        $('#level').val(row.codeLevel);
        $('#codeLevel').val(row.codeLevel);
        if (row.parentId != null && row.parentId != '0') {
            var parentRow = $('#codeTable').treegrid('find', row.parentId);
            $('#parentName').val(parentRow.codeDesc);
            $('#parentId').val(row.parentId);
        } else {
            $('#parentName').val(null);
            $('#parentId').val(0);
        }

    } else {
        $.messager.alert('出错', '请先选择要修改的数据', 'error');
        return;
    }
    openDlgChannel('修改', 'update',row.id);
}
function deleteCodeDialog() {
    var row= $('#codeTable').treegrid('getSelected');
    if (row != null) {
        row = row.id;
        $.messager.confirm('确认', '您是否确认要删除这些数据，如果数据包含子节点，将同步删除子节点数据?', function (t) {
            if (t) {
                $.ajax({
                    url: BASE_HREF + 'manager/code/delCode',
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
                                if (row.parentId != null) {
                                    $('#codeTable').treegrid('reload', row.parentId);
                                } else {
                                    $('#codeTable').treegrid('reload');
                                }
                                oldSelectedRow = null;
                                $('#codeTable').treegrid('unselectAll');
                                break;
                            case 2:
                                $('#codeTable').treegrid('reload');
                                $.messager.alert('提示', "当前数据已被用户:"+data.lockedUser+"锁定,请稍候再试!", "info");
                                break;
                            default:
                                break;
                        }
                    }
                });
            }
        });
    } else {
        $.messager.alert('错误', '请选择要删除的数据！', 'error');
    }

}
var oldSelectedRow = null;
function clickRow(row) {
    if (oldSelectedRow == null) {
        oldSelectedRow = row;
        return;
    } else if (row.id == oldSelectedRow.id) {
        oldSelectedRow = null;
        $('#codeTable').treegrid('unselectAll');
    } else {
        oldSelectedRow = row;
    }
}
function searchData(value, name) {
    if ($.trim(value) == '') {
        return;
    }
    $('#codeTable').treegrid('options').treeField = null;
    $('#codeTable').treegrid('load', {'type': name, 'value': $.trim(value)});

}
function dblClickRow(data) {
    if (data != null) {
        $('form').form('load', data);
        $('#ids').val($('#id').val());
        $('#level').val(data.codeLevel);
        $('#codeLevel').val(data.codeLevel);
        if (data.parentId != null && data.parentId != '0') {
            var parentRow = $('#codeTable').treegrid('find', data.parentId);
            $('#parentName').val(parentRow.codeDesc);
            $('#parentId').val(data.parentId);
        } else {
            $('#parentName').val(null);
            $('#parentId').val(0);
        }

    } else {
        $.messager.alert('出错', '请先选择要修改的数据', 'error');
        return;
    }
    openDlgChannel('修改', 'update',data.id);
}
function openDlgChannel(title, type,id) {
    if (type == 'add') {
        $('#dlg_buttons_m_code_add').show();
        $('#dlg_buttons_m_code_update').hide();
    } else {
        $('#dlg_buttons_m_code_add').hide();
        $('#dlg_buttons_m_code_update').show();
    }
    $('#dlg_m_code').dialog({
        closed: true,
        inline: true,
        modal: true,
        buttons: '#dlg_buttons_m_code',
        onMove: onMoveDialog,
        onOpen: function () {
            $('#codeDesc').focus();
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
    $('#codeForm').form('resetValidation');
    $('#codeForm').form('disableValidation');
    $('#dlg_m_code').dialog('open').dialog('setTitle', title);
}
