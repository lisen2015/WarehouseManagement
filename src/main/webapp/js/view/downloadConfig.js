/**
 * Created by thinkpad on 2016/8/24.
 */
$(function () {
    $('input[name="dataTitle"]').validatebox({
        required : true,
    });
    $('input[name="dataUrl"]').validatebox({
        required : true,
    });
})
$('#downloadConfig').datagrid({
    url : BASE_HREF+"manager/downloadConfig/list",
    method:"post",
    fit : true,
    fitColumns : true,
    striped : true,
    rownumbers : true,
    border : false,
    pagination : true,
    pageSize : 20,
    pageList : [10, 20, 30, 40, 50],
    pageNumber : 1,
    sortName : 'date',
    sortOrder : 'desc',
    toolbar : '#manager_tool_downloadConfig',
    columns : [[
        {
            field : 'id',
            title : '自动编号',
            checkbox : true,
            width:10
        },
        {
            field : 'dataTitle',
            title : '栏目名称',
            width:10
        },
        {
            field : 'inputTimeStr',
            title : '日期',
            width:10
        },

        {
            field : 'dataUrl',
            title : '下载页面地址',
            width:40
        },
    ]],
    onDblClickRow: function(rowIndex, rowData){
        if (rowData != null) {
            $("#downloadConfig_edit").form("load",rowData);
        }else{
            $.messager.alert("出错", "请先选择要修改的下载配置", "error");
            return;
        }
        $("#downloadConfig_edit").validate().resetForm();
        $('#downloadConfig_edit').form('resetValidation');
        $('#downloadConfig_edit').form('disableValidation');
        $("input").removeClass("error");
        openDlgEdit(rowData.id)
        $('input[name="channelname_add"]').focus();
    }
});
$('#downloadConfig_dialog_add').dialog({
    width: 580,
    height: 400,
    title: '新增下载栏目',
    modal: true,
    closed: true,
    inline: true,
    iconCls: 'icon-user-add',
    buttons: [{
        text: '提交',
        iconCls: 'icon-add-new',
        handler: function () {
            //开启验证
            $('#downloadConfig_add').form('enableValidation');
            if ($('#downloadConfig_add').form('validate')) {
                var regu =/(http|ftp|https):\/\/[\w\-_]+(\.[\w\-_]+)+([\w\-\.,@?^=%&amp;:/~\+#]*[\w\-\@?^=%&amp;/~\+#])?/;
                var re = new RegExp(regu);
                if(re.test($("#dataUrl1").val()))
                {
                    $.ajax({
                        url: BASE_HREF + "manager/downloadConfig/add",
                        type: 'post',
                        data: $("#downloadConfig_add").serialize(),
                        beforeSend: function () {
                            $.messager.progress({
                                text: '正在新增中...',
                            });
                            $("#downloadConfig_dialog_add").dialog("close");
                        },
                        success: function (data, response, status) {
                            $.messager.progress('close');
                            ajaxLoadEnd();
                            switch (data.state) {
                                case 0:
                                    $.messager.alert("新增失败！", data.message, "warning", function () {
                                        $("#downloadConfig_dialog_add").dialog("open");
                                    });
                                    break;
                                case 1:
                                    $.messager.show({
                                        title: '提示',
                                        msg: '新增下载配置成功',
                                    });
                                    $('#downloadConfig_dialog_add').dialog('close')
                                    $('#downloadConfig_add').form('reset');
                                    $("#downloadConfig").datagrid("reload");
                                    break;
                                case 2:
                                    $.messager.alert("新增失败！", "验证不通过", "warning", function () {
                                        $("#downloadConfig_dialog_add").dialog("open");
                                    });
                                    break;
                                default:
                                    break;
                            }
                            //清空checkbox
                            $("#downloadConfig").datagrid("clearChecked");
                        }
                    });
                }else{
                    $.messager.alert("新增失败！", "请输入正确的链接地址！", "warning");
                }
            }
        },
    }
        , {
            text: '取消',
            iconCls: 'icon-redo',
            handler: function () {
                $('#downloadConfig_dialog_add').dialog('close')
                $('#downloadConfig_add').form('reset');
            },
        }],
});
function downloadUpdate(){
    $('#downloadConfig_edit').form('enableValidation');
    if ($('#downloadConfig_edit').form('validate')) {
        //开启验证
        var regu =/(http|ftp|https):\/\/[\w\-_]+(\.[\w\-_]+)+([\w\-\.,@?^=%&amp;:/~\+#]*[\w\-\@?^=%&amp;/~\+#])?/;
        var re = new RegExp(regu);
        if(re.test($("#dataUrl2").val())){
        $.ajax({
            url: BASE_HREF + "manager/downloadConfig/update",
            type: 'post',
            data: $("#downloadConfig_edit").serialize(),
            beforeSend: function () {
                $.messager.progress({
                    text: '正在修改中...',
                });
                $("#downloadConfig_dialog_edit").dialog("close");
            },
            success: function (data, response, status) {
                $.messager.progress('close');
                ajaxLoadEnd();
                switch (data.state) {
                    case 0:
                        $.messager.alert("修改失败！", data.message, "warning", function () {
                            $("#downloadConfig_dialog_edit").dialog("open");
                        });
                        break;
                    case 1:
                        $.messager.show({
                            title: '提示',
                            msg: '修改下载配置成功',
                        });
                        $('#downloadConfig_dialog_edit').dialog('close')
                        $('#downloadConfig_edit').form('reset');
                        $("#downloadConfig").datagrid("reload");
                        break;
                    case 2:
                        $.messager.alert("修改失败！", "验证不通过", "warning", function () {
                            $("#downloadConfig_dialog_edit").dialog("open");
                        });
                        break;
                    default:
                        break;
                }
                //清空checkbox
                $("#downloadConfig").datagrid("clearChecked");
            }
        });
        }else{
            $.messager.alert("修改失败！", "请输入正确的链接地址！", "warning");
        }
    }
}
function openDlgEdit(id) {
    $("#dlg_buttons_m_download").show();
    $('#downloadConfig_dialog_edit').dialog({
        width: 580,
        height: 400,
        title: '修改下载栏目',
        modal: true,
        inline: true,
        closed: true,
        iconCls: 'icon-user-add',
        buttons : "#dlg_buttons_m_download",
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
    $('#downloadConfig_dialog_edit').dialog('open');
}

manager_tool_downloadConfig = {
    reload : function () {
        $('#downloadConfig').datagrid('reload');
    },
    redo : function () {
        $('#downloadConfig').datagrid('unselectAll');
    },
    add : function () {
        //打开对话框前 重置表单
        $('#downloadConfig_add').form('reset');
        ////重置表单验证
        $("#downloadConfig_add").validate().resetForm();
        $('#downloadConfig_add').form('resetValidation');
        $('#downloadConfig_add').form('disableValidation');
        $("input").removeClass("error");
        $('#downloadConfig_dialog_add').dialog('open');
        $('input[name="channelname_add"]').focus();
    },
    edit : function () {
        var rows = $('#downloadConfig').datagrid('getSelections');
        if (rows.length > 1) {
            $.messager.alert('警告操作！', '编辑记录只能选定一条数据！', 'warning');
        } else if (rows.length == 1) {
            var row=rows[0];
            //重置表单验证
            $("#downloadConfig_edit").form("load",row);
            $("#downloadConfig_edit").validate().resetForm();
            $('#downloadConfig_edit').form('resetValidation');
            $('#downloadConfig_edit').form('disableValidation');
            $("input").removeClass("error");
            openDlgEdit(row.id)
            $('input[name="channelname_add"]').focus();
        } else if (rows.length == 0) {
            $.messager.alert('警告操作！', '编辑记录至少选定一条数据！', 'warning');
        }
    },
    remove : function () {
        //查询是否有记录被调用
        var rows = $('#downloadConfig').datagrid('getSelections');
        //遍历id 若有id被调用 则设置flag为false
        if (rows.length > 0) {
            $.messager.confirm('确定操作', '您正在要删除所选的记录吗？', function (flag) {
                if (flag) {
                    var ids = "";
                    for (var i = 0; i < rows.length; i++) {
                        ids += rows[i].id + ",";
                    }
                    ids = ids.substring(0, ids.length - 1);

                    $.ajax({
                        type: 'POST',
                        url: BASE_HREF + "manager/downloadConfig/del",
                        data: {
                            id: ids,
                        },
                        beforeSend: function () {
                            $('#downloadConfig').datagrid('loading');
                        },
                        success: function (data) {
                            ajaxLoadEnd();
                            switch (data.state) {
                                case 0:
                                    $('#downloadConfig').datagrid('loaded');
                                    $('#downloadConfig').datagrid('load');
                                    $.messager.alert("删除失败！", data.message, "warning");
                                    break;
                                case 1:
                                    $('#downloadConfig').datagrid('loaded');
                                    $('#downloadConfig').datagrid('load');
                                    $('#downloadConfig').datagrid('unselectAll');
                                    $.messager.show({
                                        title: '提示',
                                        msg: data.number + '个下载配置被删除成功！',
                                    });
                                    break;
                                case 2:
                                    $('#downloadConfig').datagrid('loaded');
                                    $('#downloadConfig').datagrid('load');
                                    $.messager.alert('提示', "当前数据已被用户:"+data.lockedUser+"锁定,请稍候再试!", "info");
                                    break;
                                default:
                                    break;
                            }
                        },
                    });
                }
            });
        } else {
            $.messager.alert('提示', '请选择要删除的记录！', 'info');
        }
    },
};