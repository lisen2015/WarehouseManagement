/*															
 * FileName：mUser.js						
 *			
 * Description：用于userList.jsp
 * 																
 * History：
 * 版本号 		作者 			日期       			简介
 *  1.0 	chenchen	2014-12-10		Create
 */

function dblClickRow(rowIndex, row) {
    if (row != null) {
        $('form').form('load', row);
        $('#passtd').hide();
        $('#passtd2').hide();
        $('#loginPassword').val('11111111');
    } else {
        $.messager.alert('出错', '请先选择要修改的用户', 'error');
        return;
    }
    openDlgUser('修改',row.id);
}

function addUserDialog() {
    $('#passtd').show();
    $('#passtd2').show();
    $('#userForm').form('clear');
    openDlgUser('新增');
}
function addORUpdateUser() {
    $('#userForm').form('enableValidation');
    if ($('#userForm').form('validate')) {
        var method = 'add';
        if ($('#userForm #id').val()) {
            method = 'update';
        }
        $.ajax({
            url: BASE_HREF + 'manager/user/' + method,
            data: $('form').serialize(),
            type: 'POST',
            beforeSend: function () {
                $('#dlg_m_user').dialog('close');
                ajaxLoading('正在提交请稍候。。。');
            },
            success: function (data) {
                ajaxLoadEnd();
                switch (data.state) {
                    case 0:
                        $.messager.alert('失败', data.message, 'error', function () {
                            $('#dlg_m_user').dialog('open');
                        });
                        break;
                    case 1:
                        $.messager.alert('成功', '操作成功');
                        $('#userTable').datagrid('reload');
                        break;
                    case 2:
                        $.messager.alert('失败', '验证不通过', 'error', function () {
                            $('#dlg_m_user').dialog('open');
                        });

                        break;
                    default:
                        break;
                }
                $('#userTable').datagrid('clearChecked');
            }
        });
    }
}
function updateUserDialog(flag) {
    var row;
    if (flag != null) {
        row = $('#userTable').datagrid('getChecked');
        if (row == null || row.length == 0) {
            $.messager.alert('出错', '请先选择要修改的用户', 'error');
            return;
        } else if (row.length > 1) {
            $.messager.alert('出错', '只允许选择一个用户进行修改', 'error');
            return;
        } else {
            row = row[0];
        }
    } else {
        row = $('#userTable').datagrid('getSelected');
    }
    if (row != null) {
        $('form').form('load', row);
        $('#passtd').hide();
        $('#passtd2').hide();
        $('#loginPassword').val('11111111');
    } else {
        $.messager.alert('出错', '请先选择要修改的用户', 'error');
        return;
    }
    openDlgUser('修改',row.id);
}
function deleteUserDialog(flag) {
    var row;
    if (flag != null) {
        row = $('#userTable').datagrid('getChecked');
        if (row == null || row.length == 0) {
            $.messager.alert('出错', '请先选择要删除的用户', 'error');
            return;
        } else {
            var rowids = '';
            $.each(row, function (i, n) {
                rowids += n.id + ',';
            });
            row = rowids.substr(0, rowids.length - 1);
        }
    } else {
        row = $('#userTable').datagrid('getSelected').id;
    }
    if (row != null) {
        $.messager.confirm('确认', '您是否确认要删除这些数据?', function (t) {
            if (t) {
                $.ajax({
                    url: BASE_HREF + 'manager/user/delUser',
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
                                $('#userTable').datagrid('reload');
                                $('#userTable').datagrid('clearChecked');
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
function resetPassWord(flag) {
    var row;
    if (flag != null) {
        row = $('#userTable').datagrid('getChecked');
        if (row == null || row.length == 0) {
            $.messager.alert('出错', '请先选择要重置密码的用户', 'error');
            return;
        } else {
            var rowids = '';
            $.each(row, function (i, n) {
                rowids += n.id + ',';
            });
            row = rowids.substr(0, rowids.length - 1);
        }
    } else {
        row = $('#userTable').datagrid('getSelected').id;
    }
    if (row != null) {
        $.messager.confirm('确认', '您是否确认要为这些用户重置密码?', function (t) {
            if (t) {
                $.ajax({
                    url: BASE_HREF + 'manager/user/resetPass',
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
                                $('#userTable').datagrid('clearChecked');
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
        $.messager.alert('错误', '请选择要重置密码的用户！', 'error');
    }
}





function formatRoleManager(value, row, index) {
    return '<a href="javaScript:void(0)" onclick="userRoleList(\'' + row.id + '\')">管理</a>';
}
function formatRoleAdd(value, row, index) {
    return '<a href="javaScript:void(0)" onclick="roleList(\'' + row.id + '\')">添加</a>';
}
function userRoleList(value) {
    $('#win_user_config').window({
        title: '所属角色',
        width: 400,
        height: 400,
        draggable: true,
        resizable: true,
        modal: true,
        collapsible: false,
        minimizable: false,
        maximizable: false,
        inline: true,
        onMove: onMoveWindow,
        href: BASE_HREF + 'manager/user/role/detail?id=' + value,
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
function roleList(value) {
    $('#win_user_config').window({
        title: '角色列表',
        width: 400,
        height: 400,
        draggable: true,
        resizable: true,
        modal: true,
        collapsible: false,
        minimizable: false,
        maximizable: false,
        inline: true,
        onMove: onMoveWindow,
        href: BASE_HREF + 'manager/user/role?id=' + value,
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
function searchData(value, name) {
    if ($.trim(value) == '') {
        return;
    }
    $('#userTable').datagrid('load', {'type': name, 'value': $.trim(value)});

}

function openDlgUser(title,id) {
    $('#dlg_m_user').dialog({
        closed: true,
        inline: true,
        modal: true,
        onMove: onMoveDialog,
        buttons: '#dlg_buttons_m_user_add',
        onOpen: function () {
            $('#loginName').focus();
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
    $('#dlg_m_user').dialog('open').dialog('setTitle', title);
    $('#userForm').form('resetValidation');
    $('#userForm').form('disableValidation');
    event.stopPropagation();
}