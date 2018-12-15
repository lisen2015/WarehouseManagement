function formatPrice(val, row) {
    return '<p style="color: red;">¥ ' + val.toFixed(2) + '</p>';
}

function formatAllPrice(val, row) {
    return '<b style="color: red;">¥ ' + val.toFixed(2) + '</b>';
}

function searchProductById() {
    $.ajax({
        url: BASE_HREF + 'manager/product/getProduct',
        data: {
            pid: $('#ininventoryForm #productId').val().trim()
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
                $('#ininventoryForm #productName').val(data.productName);
                $('#ininventoryForm #inNumber').focus();
                $('#dlg_buttons_m_ininventory_add').show();
            }
        }
    });
}

function addInInventoryDialog() {
    $("#ininventoryForm").form('clear');
    openDlgInInventory('新增', 'add');
}

function addInInventory() {
    event.stopPropagation();
    $('#ininventoryForm').form('enableValidation');
    if ($('#ininventoryForm').form('validate')) {
        $.ajax({
            url: BASE_HREF + 'manager/ininventory/add',
            data: $('form').serialize(),
            type: 'POST',
            beforeSend: function () {
                $('#dlg_m_ininventory').dialog('close');
                ajaxLoading('正在提交请稍候。。。');
            },
            success: function (data) {
                ajaxLoadEnd();
                switch (data.state) {
                    case 0:
                        $.messager.alert('失败', data.message, 'error', function () {
                            $('#dlg_m_ininventory').dialog('open');
                        });
                        break;
                    case 1:
                        $.messager.alert('成功', '操作成功');
                        $('#ininventoryTable').datagrid('reload');
                        break;
                    case 2:
                        $.messager.alert('失败', '验证不通过', 'error', function () {
                            $('#dlg_m_ininventory').dialog('open');
                        });

                        break;
                    default:
                        break;
                }
            }
        });
    }
}

function openDlgInInventory(title, type, id) {
    // $.ajax({
    //     url: BASE_HREF + 'manager/department/departmentList',
    //     type: 'GET',
    //     success: function (data) {
    //         var oplArr = [];
    //         $.each(data, function (idx, item) {
    //             var selected = '';
    //             if (id == item.id){
    //                 selected = 'selected';
    //             }
    //             oplArr.push('<option name="department" value="' + item.id + '" '+selected+'>' + item.departmentName + '</option>');
    //         });
    //         $('#ininventoryForm #department').html(oplArr.join(''));
    //     }
    // });
    if (type == 'add') {
        $('#dlg_buttons_m_channel_addInInventory').show();
        $('#dlg_buttons_m_channel_update').hide();
    }

    $('#dlg_m_ininventory').dialog({
        closed: true,
        inline: true,
        modal: true,
        onMove: onMoveDialog,
        buttons: '#dlg_buttons_m_ininventory_add',
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
            $('#ininventoryForm #productId').focus();
            $('#dlg_buttons_m_ininventory_add').hide();
        },
        onClose: function () {
            CHECK_DATALOCK_FLAG = false;
        }
    });

    $('#dlg_m_ininventory').dialog('open').dialog('setTitle', title);
    $('#ininventoryForm').form('resetValidation');
    $('#ininventoryForm').form('disableValidation');
    event.stopPropagation();
}