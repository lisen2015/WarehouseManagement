function searchData(value, name) {
    if ($.trim(value) == '') {
        return;
    }
    $('#departmentTable').datagrid('load', {'type': name, 'value': $.trim(value)});
}
function addDepartmentDialog() {
    $("#departmentForm").form('clear')

    openDlgDepartment('新增', 'add');
}
function addORUpdateDepartment() {
    $('#departmentForm').form('enableValidation');
    if ($('#departmentForm').form('validate')) {
        var method = 'add';
        if ($('#departmentForm #id').val()) {
            method = 'update';
        }
        $.ajax({
            url: BASE_HREF + 'manager/department/' + method,
            data: $('form').serialize(),
            type: 'POST',
            beforeSend: function () {
                $('#dlg_m_department').dialog('close');
                ajaxLoading('正在提交请稍候。。。');
            },
            success: function (data) {
                ajaxLoadEnd();
                switch (data.state) {
                    case 0:
                        $.messager.alert('失败', data.message, 'error', function () {
                            $('#dlg_m_department').dialog('open');
                        });
                        break;
                    case 1:
                        $.messager.alert('成功', '操作成功');
                        $('#departmentTable').datagrid('reload');
                        break;
                    case 2:
                        $.messager.alert('失败', '验证不通过', 'error', function () {
                            $('#dlg_m_department').dialog('open');
                        });

                        break;
                    default:
                        break;
                }
                $('#departmentTable').datagrid('clearChecked');
            }
        });
    }
}


function updateDepartmentDialog(flag) {
    var row;
    if (flag != null) {
        row = $('#departmentTable').datagrid('getChecked');
        if (row == null || row.length == 0) {
            $.messager.alert('出错', '请先选择要修改的部门', 'error');
            return;
        } else if (row.length > 1) {
            $.messager.alert('出错', '只允许选择一个部门行修改', 'error');
            return;
        } else {
            row = row[0];
        }
    } else {
        row = $('#departmentTable').datagrid('getSelected');
    }
    if (row != null) {
        $('form').form('load', row);
        openDlgDepartment('修改', 'update', row.id);
    } else {
        $.messager.alert('出错', '请先选择要修改的部门', 'error');
        return;
    }

}


function deleteDepartmentDialog(flag) {
    var row;
    if (flag != null) {
        row = $('#departmentTable').datagrid('getChecked');
        if (row == null || row.length == 0) {
            $.messager.alert('出错', '请先选择要删除的部门', 'error');
            return;
        } else {
            var rowids = '';
            $.each(row, function (i, n) {
                rowids += n.id + ',';
            });
            row = rowids.substr(0, rowids.length - 1);
        }
    } else {
        row = $('#departmentTable').datagrid('getSelected').id;
    }
    if (row != null) {
        $.messager.confirm('确认', '您是否确认要删除这些数据?', function (t) {
            if (t) {
                $.ajax({
                    url: BASE_HREF + 'manager/department/del',
                    data: 'ids=' + row,
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
                                $('#departmentTable').datagrid('reload');
                                $('#departmentTable').datagrid('clearChecked');
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
function dblClickRow(rowIndex, rowData) {
    if (rowData != null) {
        $('form').form('load', rowData);
    } else {
        $.messager.alert('出错', '请先选择要修改的部门', 'error');
        return;
    }
    openDlgDepartment('修改', 'update', rowData.id);
}
function openDlgDepartment(title, type, id) {

    if (type == 'add') {
        $('#dlg_buttons_m_channel_addDepartment').show();
        $('#dlg_buttons_m_channel_update').hide();
    } else {
        $('#dlg_buttons_m_channel_addDepartment').hide();
        $('#dlg_buttons_m_channel_update').show();
    }

    $('#dlg_m_department').dialog({
        closed: true,
        inline: true,
        modal: true,
        onMove: onMoveDialog,
        buttons: '#dlg_buttons_m_department_add',
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
            $('#departmentName').focus();
        },
        onClose:function(){
            CHECK_DATALOCK_FLAG=false;
        }
    });
    $('#dlg_m_department').dialog('open').dialog('setTitle', title);
    $('#departmentForm').form('resetValidation');
    $('#departmentForm').form('disableValidation');
    event.stopPropagation();
}