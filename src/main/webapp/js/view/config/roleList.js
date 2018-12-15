/*															
 * FileName：roleList.js
 *
 * Description：roleList.jsp
 *
 * History：
 * 版本号 		作者 			日期       			简介
 * 1.0       	chenchen		2015-3-23		Create
 */

function searchData(value, name) {
    if ($.trim(value) == '') {
        return;
    }
    $('#roleTable').datagrid('load', {'type': name, 'value': $.trim(value)});
}
function addRoleDialog() {
    $("#roleForm").form('clear')

    openDlgRole('新增', 'add');
}
function addORUpdateRole() {
    $('#roleForm').form('enableValidation');
    if ($('#roleForm').form('validate')) {
        var method = 'add';
        if ($('#roleForm #id').val()) {
            method = 'update';
        }
        $.ajax({
            url: BASE_HREF + 'manager/role/' + method,
            data: $('form').serialize(),
            type: 'POST',
            beforeSend: function () {
                $('#dlg_m_role').dialog('close');
                ajaxLoading('正在提交请稍候。。。');
            },
            success: function (data) {
                ajaxLoadEnd();
                switch (data.state) {
                    case 0:
                        $.messager.alert('失败', data.message, 'error', function () {
                            $('#dlg_m_role').dialog('open');
                        });
                        break;
                    case 1:
                        $.messager.alert('成功', '操作成功');
                        $('#roleTable').datagrid('reload');
                        break;
                    case 2:
                        $.messager.alert('失败', '验证不通过', 'error', function () {
                            $('#dlg_m_role').dialog('open');
                        });

                        break;
                    default:
                        break;
                }
                $('#rolerTable').datagrid('clearChecked');
            }
        });
    }
}


function updateRoleDialog(flag) {
    var row;
    if (flag != null) {
        row = $('#roleTable').datagrid('getChecked');
        if (row == null || row.length == 0) {
            $.messager.alert('出错', '请先选择要修改的角色', 'error');
            return;
        } else if (row.length > 1) {
            $.messager.alert('出错', '只允许选择一个角色行修改', 'error');
            return;
        } else {
            row = row[0];
        }
    } else {
        row = $('#roleTable').datagrid('getSelected');
    }
    if (row != null) {
        $('form').form('load', row);
        openDlgRole('修改', 'update', row.id);
    } else {
        $.messager.alert('出错', '请先选择要修改的角色', 'error');
        return;
    }

}


function deleteRoleDialog(flag) {
    var row;
    if (flag != null) {
        row = $('#roleTable').datagrid('getChecked');
        if (row == null || row.length == 0) {
            $.messager.alert('出错', '请先选择要删除的角色', 'error');
            return;
        } else {
            var rowids = '';
            $.each(row, function (i, n) {
                rowids += n.id + ',';
            });
            row = rowids.substr(0, rowids.length - 1);
        }
    } else {
        row = $('#roleTable').datagrid('getSelected').id;
    }
    if (row != null) {
        if (row == '402881895b352012015b3526e2070001') {
            $.messager.alert('出错', '请勿删除此条记录！', 'error');
            return;
        }
        $.messager.confirm('确认', '您是否确认要删除这些数据?', function (t) {
            if (t) {
                $.ajax({
                    url: BASE_HREF + 'manager/role/delRole',
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
                                $('#roleTable').datagrid('reload');
                                $('#roleTable').datagrid('clearChecked');
                                break;
                            case 2:
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
function formatUser(value, row, index) {
    return '<a href="javaScript:void(0)" onclick="roleUserList(\'' + row.id
        + '\')">管理</a>';
}

function formatUserAdd(value, row, index) {
    return '<a href="javaScript:void(0)" onclick="userList(\'' + row.id
        + '\')">添加</a>';
}

function roleUserList(value) {
    $('#win_user_config').window({
        title: '用户列表',
        width: 450,
        height: 400,
        draggable: true,
        modal: true,
        resizable: true,
        collapsible: false,
        minimizable: false,
        maximizable: false,
        inline: true,
        onMove: onMoveWindow,
        href: BASE_HREF + 'manager/role/user/detailUser?id=' + value,
        onBeforeOpen: function () {
            if (value != null) {
                CHECK_DATALOCK_FLAG = true;
                ajaxLoading('正在查询请稍候。。。');
                var flag = checkLock(value);
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
function userList(value) {
    $('#win_user_config').window({
        title: '用户列表',
        width: 450,
        height: 400,
        draggable: true,
        modal: true,
        resizable: true,
        collapsible: false,
        minimizable: false,
        maximizable: false,
        inline: true,
        onMove: onMoveWindow,
        href: BASE_HREF + 'manager/role/user/list?id=' + value,
        onBeforeOpen: function () {
            if (value != null) {
                CHECK_DATALOCK_FLAG = true;
                ajaxLoading('正在查询请稍候。。。');
                var flag = checkLock(value);
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

function defaultRole(value, row, index) {
    if (value == 1) {
        return '是';
    } else {
        return '';
    }
}

function formatFunctionAuth(value, row, index) {
    return '<a href="javaScript:void(0)" onclick="functionAuthList(\'' + row.id + '\')">管理</a>';
}
function functionAuthList(value) {
    $("#win_user_config").window({
        title: "权限列表",
        width: 600,
        height: 400,
        draggable: true,
        resizable: true,
        modal: true,
        collapsible: false,
        minimizable: false,
        maximizable: false,
        inline: true,
        onMove: onMoveWindow,
        href: BASE_HREF + "manager/role/authority?id=" + value,
        onBeforeOpen: function () {
            if (value != null) {
                CHECK_DATALOCK_FLAG = true;
                ajaxLoading('正在查询请稍候。。。');
                var flag = checkLock(value);
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
/**
 * 表格行拖动排序回调方法，用于将结果保存到服务端
 * @param targetRow 目标对象
 * @param sourceRow 被拖动对象
 * @param point        位置
 * @version
 *    2014-9-23        chenchen    create
 */
function dropConfig(targetRow, sourceRow, point) {
    $.ajax({
        url: BASE_HREF + 'manager/role/move',
        data: {
            sId: sourceRow.id,
            sIndex: sourceRow.roleIndex,
            tIndex: targetRow.roleIndex,
            point: point,
        },
        type: 'POST',
        beforeSend: function () {
            ajaxLoading('正在提交请稍候。。。');
        },
        success: function (data) {
            ajaxLoadEnd();
            switch (data.state) {
                case 0:
                    $.messager.alert('失败', data.message, 'error');
                    $('#roleTable').datagrid('reload');
                    break;
                case 1:
                    $('#roleTable').datagrid('reload');
                    break;
                default:
                    break;
            }
        }
    });
}
/**
 * 表格行拖动排序之前的回调方法，用于检查是否可以拖动
 * @param targetRow 目标对象
 * @param sourceRow 被拖动对象
 * @param point        位置
 * @version
 *    2014-9-23        chenchen    create
 */
function checkDrop(targetRow, sourceRow, point) {
    if (!hasAuthority()) {
        return false;
    }
    if (point == 'top') {
        if (targetRow.roleIndex - 1 == sourceRow.roleIndex) {
            return false;
        }
    } else {
        if (targetRow.roleIndex + 1 == sourceRow.roleIndex) {
            return false;
        }
    }

}
function dblClickRow(rowIndex, rowData) {
    if (rowData != null) {
        $('form').form('load', rowData);
    } else {
        $.messager.alert('出错', '请先选择要修改的角色', 'error');
        return;
    }
    openDlgRole('修改', 'update', rowData.id);
}
function openDlgRole(title, type, id) {

    if (type == 'add') {
        $('#dlg_buttons_m_channel_addRole').show();
        $('#dlg_buttons_m_channel_update').hide();
    } else {
        $('#dlg_buttons_m_channel_addRole').hide();
        $('#dlg_buttons_m_channel_update').show();
    }

    $('#dlg_m_role').dialog({
        closed: true,
        inline: true,
        modal: true,
        onMove: onMoveDialog,
        buttons: '#dlg_buttons_m_role_add',
        onBeforeOpen:function () {
            if (id!=null){
                CHECK_DATALOCK_FLAG=true;
                ajaxLoading('正在查询请稍候。。。');
                var flag=checkLock(id);
                ajaxLoadEnd();
                return　flag;
            }else{
                return　true;
            }
        },
        onOpen: function () {
            $('#roleName').focus();
        },
        onClose:function(){
            CHECK_DATALOCK_FLAG=false;
        }
    });
    $('#dlg_m_role').dialog('open').dialog('setTitle', title);
    $('#roleForm').form('resetValidation');
    $('#roleForm').form('disableValidation');
    event.stopPropagation();
}