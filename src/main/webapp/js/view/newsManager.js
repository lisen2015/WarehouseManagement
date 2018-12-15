/**
 * Created by thinkpad on 2016/8/24.
 */
$(function () {
    $('input[name="messageTitle"]').validatebox({
        required : true,
    });
})
$('#messageSource').combotree({
    url: BASE_HREF+"manager/newsManager/codeList",
    //当选择触发事件，防止选择标题栏
    onBeforeSelect:function(node){
        if(node.attributes=="1"){
            return true
        }else {
            return false
        }
    }
});
$('#newsManager').datagrid({
    url : BASE_HREF+"manager/newsManager/list",
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
    toolbar : '#manager_tool_newsManager',
    columns : [[
        {
            field : 'id',
            title : '自动编号',
            width : 10,
            checkbox : true,
        },
        {
            field : 'codeValue1',
            title : '来源',
            width:10,
        },
        {
            field : 'messageTitle',
            title : '标题摘要',
            width:10,
        },
        {
            field : 'inputTimeStr',
            title : '发布日期',
            width : 10,
        },
        {
            field : 'messageContent',
            title : '链接',
            width:40,
        },
    ]],
});

$('#newsManager_dialog_add').dialog({
    width : 580,
    title : '新增全局消息',
    inline : true,
    modal : true,
    closed : true,
    iconCls : 'icon-user-add',
    buttons : [{
        text : '发送',
        iconCls : 'icon-send',
        handler : function () {
            $('#newsManager_add').form('enableValidation');
            if ($('#newsManager_add').form('validate')) {
                $.ajax({
                    url : BASE_HREF+"manager/newsManager/add",
                    type : 'post',
                    data : $("#newsManager_add").serialize(),
                    beforeSend : function () {
                        $.messager.progress({
                            text : '正在新增中...',
                        });
                        $("#newsManager_dialog_add").dialog("close");
                    },
                    success : function (data, response, status) {
                        $.messager.progress('close');
                        $.messager.progress('close');
                        ajaxLoadEnd();
                        switch (data.state) {
                            case 0:
                                $.messager.alert("新增失败！", data.message, "warning", function() {
                                    $("#newsManager_dialog_add").dialog("open");
                                });
                                break;
                            case 1:
                                $.messager.show({
                                    title : '提示',
                                    msg : '新增全局消息成功',
                                });
                                $('#newsManager_dialog_add').dialog('close')
                                $('#newsManager_add').form('reset');
                                $("#newsManager").datagrid("reload");
                                break;
                            case 2:
                                $.messager.alert("新增失败！", "验证不通过", "warning", function() {
                                    $("#newsManager_dialog_add").dialog("open");
                                });
                                break;
                            default:
                                break;
                        }
                    }
                });
             }
        },
    },{
        text : '取消',
        iconCls : 'icon-redo',
        handler : function () {
            $('#newsManager_dialog_add').dialog('close');
            $('#newsManager_add').form('reset');
        },
    }],
});




manager_tool_newsManager = {
    add : function () {
        //打开对话框前 重置表单
        $('#newsManager_add').form('reset');
        //重置表单 验证
        $("#newsManager_add").validate().resetForm();
        $('#newsManager_add').form('resetValidation');
        $('#newsManager_add').form('disableValidation');
        $("input").removeClass("error");
        $('#newsManager_dialog_add').dialog('open');
        $('input[name="messageTitle"]').focus();
    },
    remove : function () {
        var rows = $('#newsManager').datagrid('getSelections');
        if (rows.length > 0) {
            $.messager.confirm('确定操作', '您正在要删除所选的记录吗？', function (flag) {
                if (flag) {
                    $.messager.confirm('确定操作', '您的删除操作将会同时删除客户端此条消息，是否确认删除？', function (flag) {
                        if (flag) {
                            var ids = "";
                            for (var i = 0; i < rows.length; i ++) {
                                ids += rows[i].id+",";
                            }
                            ids = ids.substring(0,ids.length-1);

                            $.ajax({
                                type : 'POST',
                                url :BASE_HREF+"manager/newsManager/del",
                                data : {
                                    id : ids,
                                    type:null,
                                },
                                beforeSend : function () {
                                    $('#manager').datagrid('loading');
                                },
                                success : function (data) {
                                    ajaxLoadEnd();
                                    switch (data.state) {
                                        case 0:
                                            $.messager.alert("删除失败！", data.message, "warning");
                                            break;
                                        case 1:
                                            $('#newsManager').datagrid('loaded');
                                            $('#newsManager').datagrid('load');
                                            $('#newsManager').datagrid('unselectAll');
                                            $.messager.show({
                                                title : '提示',
                                                msg : data.number + '个全局消息被删除成功！',
                                            });
                                            break;
                                        default:
                                            break;
                                    }
                                },
                            });
                         }
                    });
                }
            });
        } else {
            $.messager.alert('提示', '请选择要删除的记录！', 'info');
        }
    },
    removeAll : function(){
        //删除所有
        $.messager.confirm('确定操作', '您正在要清空所有记录吗？', function (flag) {
            if (flag) {
                $.ajax({
                    type : 'POST',
                    url :BASE_HREF+"manager/newsManager/delAll",
                    data : {
                        id:null,
                        type : 'all',
                    },
                    beforeSend : function () {
                        $('#manager').datagrid('loading');
                    },
                    success : function (data) {
                        ajaxLoadEnd();
                        switch (data.state) {
                            case 0:
                                $.messager.alert("删除失败！", data.message, "warning");
                                break;
                            case 1:
                                $('#newsManager').datagrid('loaded');
                                $('#newsManager').datagrid('load');
                                $('#newsManager').datagrid('unselectAll');
                                $.messager.show({
                                    title : '提示',
                                    msg : '消息管理被清空成功！',
                                });
                                break;
                            default:
                                break;
                        }
                    },
                });
            }
        });
    }
};