function formatPrice(val, row) {
    return '<p style="color: red;">¥ ' + val.toFixed(2) + '</p>';
}

function formatAllPrice(val, row) {
    return '<b style="color: red;">¥ ' + val.toFixed(2) + '</b>';
}

function formatStatus(val, row) {
    if (val == 0) {
        return '<b style="color: #b27000;">审核中</b>';
    } else if (val == 1) {
        return '<b style="color: #10b255;">审核通过</b>';
    } else if (val == 2) {
        return '<b style="color: red;">已拒绝</b>';
    }
}

function inventoryAuditorDialog(flag) {
    event.stopPropagation();
    var row;
    if (flag != null) {
        row = $('#inventoryAuditorTable').datagrid('getChecked');
        if (row == null || row.length == 0) {
            $.messager.alert('出错', '请先选择要审核的记录', 'error');
            return;
        } else if (row.length > 1) {
            $.messager.alert('出错', '只允许单条审核', 'error');
            return;
        } else {
            row = row[0];
        }
    } else {
        row = $('#inventoryAuditorTable').datagrid('getSelected');
    }
    if (row != null) {
        if (row.status != 0) {
            $.messager.alert('出错', '已审核', 'info');
            return;
        }
        $.ajax({
            url: BASE_HREF + 'manager/product/getProduct',
            data: {
                pid: row.productId
            },
            type: 'POST',
            beforeSend: function () {
                ajaxLoading('正在查询请稍候...');
            },
            success: function (data) {
                ajaxLoadEnd();
                if (data == null || data == '') {
                    $.messager.alert('错误', '产品不存在！');
                } else {
                    $('#inventoryAuditorForm #inventory').val(data.allNumber);
                    if (Number($('#inventoryAuditorForm #outNumber').val()) > data.allNumber) {
                        $.messager.alert('失败', '库存不足！');
                        return;
                    }
                }
            }
        });
        $('form').form('load', row);
        $('#dlg_m_inventoryAuditor').dialog({
            closed: true,
            inline: true,
            modal: true,
            onMove: onMoveDialog,
            buttons: '#dlg_buttons_m_inventoryAuditor',
            onBeforeOpen: function () {
                if (row != null) {
                    CHECK_DATALOCK_FLAG = true;
                    ajaxLoading('正在查询请稍候。。。');
                    var flag = checkLock(row.id);
                    ajaxLoadEnd();
                    return flag;
                } else {
                    return true;
                }
            },
            onOpen: function () {

            },
            onClose: function () {
                CHECK_DATALOCK_FLAG = false;
            }
        });

        $('#dlg_m_inventoryAuditor').dialog('open').dialog('setTitle', '审核');
        $('#inventoryAuditorForm').form('resetValidation');
        $('#inventoryAuditorForm').form('disableValidation');
    } else {
        $.messager.alert('出错', '请先选择要审核的记录', 'error');
        return;
    }
}

function inventoryAuditor (flag) {
    if (flag) {
        $('#status').val(1);
    } else {
        $('#status').val(2);
    }

    $.ajax({
        url: BASE_HREF + 'manager/inventoryauditor/auditor',
        data: $('form').serialize(),
        type: 'POST',
        beforeSend: function () {
            $('#dlg_m_inventoryAuditor').dialog('close');
            ajaxLoading('正在提交请稍候。。。');
        },
        success: function (data) {
            ajaxLoadEnd();
            switch (data.state) {
                case 0:
                    $.messager.alert('失败', data.message, 'error', function () {
                        $('#dlg_m_inventoryAuditor').dialog('open');
                    });
                    break;
                case 1:
                    $.messager.alert('成功', '操作成功');
                    $('#inventoryAuditorTable').datagrid('reload');
                    break;
                case 2:
                    $.messager.alert('失败', '验证不通过', 'error', function () {
                        $('#dlg_m_inventoryAuditor').dialog('open');
                    });
                    break;
                default:
                    break;
            }
            $('#inventoryAuditorTable').datagrid('clearChecked');
        }
    });
}
