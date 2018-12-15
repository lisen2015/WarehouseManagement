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

function searchProductById() {
    $('#dlg_buttons_m_outinventory_add').hide();
    $.ajax({
        url: BASE_HREF + 'manager/product/getProduct',
        data: {
            pid: $('#outinventoryForm #productId').val().trim()
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
                $('#outinventoryForm #inventorys').val(data.inventorys);
                $('#outinventoryForm #productName').val(data.productName);
                if (Number(data.inventorys) > 0){
                    $('#outinventoryForm #inNumber').focus();
                    $('#dlg_buttons_m_outinventory_add').show();
                } else {
                    $.messager.alert('失败', '库存不足！');
                    return;
                }
            }
        }
    });
}

function addOutInventoryDialog() {
    $("#outinventoryForm").form('clear');
    openDlgOutInventory('新增', 'add');
}

function addOutInventory() {
    event.stopPropagation();

    var outNumber = $('#outinventoryForm #outNumber').val();
    var inventorys = $('#outinventoryForm #inventorys').val();
    if (outNumber > inventorys) {
        $.messager.alert('失败', '库存不足！');
        return;
    }

    $('#outinventoryForm').form('enableValidation');
    if ($('#outinventoryForm').form('validate')) {
        $.ajax({
            url: BASE_HREF + 'manager/outinventory/add',
            data: $('form').serialize(),
            type: 'POST',
            beforeSend: function () {
                $('#dlg_m_outinventory').dialog('close');
                ajaxLoading('正在提交请稍候。。。');
            },
            success: function (data) {
                ajaxLoadEnd();
                switch (data.state) {
                    case 0:
                        $.messager.alert('失败', data.message, 'error', function () {
                            $('#dlg_m_outinventory').dialog('open');
                        });
                        break;
                    case 1:
                        $.messager.alert('成功', '操作成功');
                        $('#outinventoryTable').datagrid('reload');
                        break;
                    case 2:
                        $.messager.alert('失败', '验证不通过', 'error', function () {
                            $('#dlg_m_outinventory').dialog('open');
                        });
                        break;
                    default:
                        break;
                }
            }
        });
    }
}

function openDlgOutInventory(title, type, id) {
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
                oplArr.push('<option name="departmentId" value="' + item.id + '" '+selected+'>' + item.departmentName + '</option>');
            });
            $('#outinventoryForm #departmentId').html(oplArr.join(''));
        }
    });
    if (type == 'add') {
        $('#dlg_buttons_m_channel_addOutInventory').show();
        $('#dlg_buttons_m_channel_update').hide();
    }

    $('#dlg_m_outinventory').dialog({
        closed: true,
        inline: true,
        modal: true,
        onMove: onMoveDialog,
        buttons: '#dlg_buttons_m_outinventory_add',
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
            $('#outinventoryForm #productId').focus();
            $('#dlg_buttons_m_outinventory_add').hide();
        },
        onClose: function () {
            CHECK_DATALOCK_FLAG = false;
        }
    });

    $('#dlg_m_outinventory').dialog('open').dialog('setTitle', title);
    $('#outinventoryForm').form('resetValidation');
    $('#outinventoryForm').form('disableValidation');
    event.stopPropagation();
}