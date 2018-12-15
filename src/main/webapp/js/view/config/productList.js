function addProductDialog() {
    $("#productForm").form('clear')
    openDlgProduct('新增', 'add');
}

function addORUpdateProduct() {
    $('#productForm').form('enableValidation');
    if ($('#productForm').form('validate')) {
        var method = 'add';
        if ($('#productForm #id').val()) {
            method = 'update';
        }
        $.ajax({
            url: BASE_HREF + 'manager/product/' + method,
            data: $('form').serialize(),
            type: 'POST',
            beforeSend: function () {
                $('#dlg_m_product').dialog('close');
                ajaxLoading('正在提交请稍候。。。');
            },
            success: function (data) {
                ajaxLoadEnd();
                switch (data.state) {
                    case 0:
                        $.messager.alert('失败', data.message, 'error', function () {
                            $('#dlg_m_product').dialog('open');
                        });
                        break;
                    case 1:
                        $.messager.alert('成功', '操作成功');
                        $('#productTable').datagrid('reload');
                        break;
                    case 2:
                        $.messager.alert('失败', '验证不通过', 'error', function () {
                            $('#dlg_m_product').dialog('open');
                        });

                        break;
                    default:
                        break;
                }
                $('#productTable').datagrid('clearChecked');
            }
        });
    }
}

function formatPrice(val, row) {
    return '<p style="color: red;">¥ ' + val.toFixed(2) + '</p>';
}

function formatAllPrice(val, row) {
    return '<b style="color: red;">¥ ' + val.toFixed(2) + '</b>';
}


function updateProductDialog(flag) {
    var row;
    if (flag != null) {
        row = $('#productTable').datagrid('getChecked');
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
        row = $('#productTable').datagrid('getSelected');
    }
    if (row != null) {
        $('form').form('load', row);
        openDlgProduct('修改', 'update', row.department);
    } else {
        $.messager.alert('出错', '请先选择要修改的部门', 'error');
        return;
    }

}

function printProductDialog(flag) {
    var row;
    if (flag != null) {
        row = $('#productTable').datagrid('getChecked');
        if (row == null || row.length == 0) {
            $.messager.alert('出错', '请先选择要删除的部门', 'error');
            return;
        } else if (row.length > 1) {
            $.messager.alert('出错', '只能单条记录条码打印', 'error');
            return;
        } else {
            var rowids = '';
            $.each(row, function (i, n) {
                rowids += n.id + ',';
            });
            row = rowids.substr(0, rowids.length - 1);
        }
    } else {
        row = $('#productTable').datagrid('getSelected').id;
    }
    if (row != null) {
        $('#dlg_m_product_ecode').dialog({
            closed: true,
            inline: true,
            modal: true,
            onMove: onMoveDialog,
            buttons: '#dlg_buttons_m_product_print',
            onBeforeOpen: function () {
                ajaxLoading('条码生成中...');
            },
            onOpen: function () {
                $("#dlg_m_product_ecode #canvascode").JsBarcode(row);
                ajaxLoadEnd();
            },
            onClose: function () {

            }
        });
        $('#dlg_m_product_ecode').dialog('open').dialog('setTitle', '打印条码');
        return;
    } else {
        $.messager.alert('错误', '请选择要打印条码的数据！', 'error');
    }
}

function printProductCode() {
    $("#dlg_m_product_ecode").jqprint({
        debug: false, //如果是true则可以显示iframe查看效果（iframe默认高和宽都很小，可以再源码中调大），默认是false
        importCSS: true, //true表示引进原来的页面的css，默认是true。（如果是true，先会找$("link[media=print]")，若没有会去找$("link")中的css文件）
        printContainer: true, //表示如果原来选择的对象必须被纳入打印（注意：设置为false可能会打破你的CSS规则）。
        operaSupport: true//表示如果插件也必须支持歌opera浏览器，在这种情况下，它提供了建立一个临时的打印选项卡。默认是true
    });
}


function deleteProductDialog(flag) {
    var row;
    if (flag != null) {
        row = $('#productTable').datagrid('getChecked');
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
        row = $('#productTable').datagrid('getSelected').id;
    }
    if (row != null) {
        $.messager.confirm('确认', '您是否确认要删除这些数据?', function (t) {
            if (t) {
                $.ajax({
                    url: BASE_HREF + 'manager/product/del',
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
                                $('#productTable').datagrid('reload');
                                $('#productTable').datagrid('clearChecked');
                                break;
                            case 2:
                                $.messager.alert('提示', "当前数据已被用户:" + data.lockedUser + "锁定,请稍候再试!", "info");
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
    openDlgProduct('修改', 'update', rowData.department);
}

function openDlgProduct(title, type, id) {
    $.ajax({
        url: BASE_HREF + 'manager/department/departmentList',
        type: 'GET',
        success: function (data) {
            var oplArr = [];
            $.each(data, function (idx, item) {
                var selected = '';
                if (id == item.id){
                    selected = 'selected';
                }
                oplArr.push('<option name="department" value="' + item.id + '" '+selected+'>' + item.departmentName + '</option>');
            });
            $('#productForm #department').html(oplArr.join(''));
        }
    });
    if (type == 'add') {
        $('#dlg_buttons_m_channel_addProduct').show();
        $('#dlg_buttons_m_channel_update').hide();
    } else {
        $('#dlg_buttons_m_channel_addProduct').hide();
        $('#dlg_buttons_m_channel_update').show();
    }

    $('#dlg_m_product').dialog({
        closed: true,
        inline: true,
        modal: true,
        onMove: onMoveDialog,
        buttons: '#dlg_buttons_m_product_add',
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
        onOpen: function () {
            $('#productName').focus();
        },
        onClose: function () {
            CHECK_DATALOCK_FLAG = false;
        }
    });
    $('#dlg_m_product').dialog('open').dialog('setTitle', title);
    $('#productForm').form('resetValidation');
    $('#productForm').form('disableValidation');
    event.stopPropagation();
}