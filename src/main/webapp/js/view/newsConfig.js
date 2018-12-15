/**
 * Created by thinkpad on 2016/8/24.
 */
$(function () {
    $('input[name="codeValue1"]').validatebox({
        required : true,
    });
    $('input[name="id"]').validatebox({
        required : true,
        validType:{
            integer:true,
            delay:1000,
            range: [1, 100000000],
            remoteByException:[''+BASE_HREF+'manager/code/exist','id',{id:'#ids'}]}
    });
    $('input[name="codeIndex"]').validatebox({
        required : true,
        validType:{
            range:[0,1000],
            integer:true
        }
    });
})
$('#newsConfig').treegrid({
    url : BASE_HREF+"manager/newsConfig/list",
    method:"post",
    fit : true,
    fitColumns : true,
    striped : true,
    rownumbers : true,
    idField : 'id',
    treeField : 'id',
    onlyLeafCheck : true,
    border : false,
    toolbar : '#manager_tool_newsConfig',
    columns : [[
        {
            field : 'id',
            title : '消息通道ID',
            width:50,
        },
        {
            field : 'codeIndex',
            title : '索引',
            width:50,
        },
        {
            field : 'codeValue1',
            title : '标题',
            width:200,
        }
    ]],
    onLoadSuccess:function () {
        $('#newsConfig').treegrid('expandAll');
    }
});

$('#newsConfig_dialog_add').dialog({
    width : 430,
    title : '新增通道',
    modal : true,
    inline : true,
    closed : true,
    iconCls : 'icon-user-add',
    buttons : [{
        text : '新增',
        iconCls : 'icon-add-new',
        handler : function () {
            $('#newsConfig_add').form('enableValidation');
            if ($('#newsConfig_add').form('validate')) {
                $.ajax({
                    url : BASE_HREF+"manager/newsConfig/add",
                    type : 'post',
                    data : $("#newsConfig_add").serialize(),
                    beforeSend : function () {
                        $.messager.progress({
                            text : '正在新增中...',
                        });
                        $("#newsConfig_dialog_add").dialog("close");
                    },
                    success : function (data, response, status) {
                        $.messager.progress('close');
                        ajaxLoadEnd();
                        switch (data.state) {
                            case 0:
                                $.messager.alert("新增失败！", data.message, "warning", function() {
                                    $("#newsConfig_dialog_add").dialog("open");
                                });
                                break;
                            case 1:
                                $.messager.show({
                                    title : '提示',
                                    msg : '新增通道成功',
                                });
                                $('#newsConfig_dialog_add').dialog('close')
                                $('#newsConfig').treegrid('unselectAll');
                                oldSelectedRow=null;
                                $("#newsConfig").treegrid("reload");
                                break;
                            case 2:
                                $.messager.alert("新增失败！", "验证不通过", "warning", function() {
                                    $("#newsConfig_dialog_add").dialog("open");
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
            $('#newsConfig_dialog_add').dialog('close')
            $('#newsConfig_add').form('reset');
        },
    }],
});




manager_tool_newsConfig = {

    add : function () {

        //打开对话框前 重置表单
        $('#newsConfig_add').form('reset');
        //重置表单验证
        $("#newsConfig_add").validate().resetForm();
        $('#newsConfig_add').form('resetValidation');
        $('#newsConfig_add').form('disableValidation');
        $("input").removeClass("error");

        var row = $('#newsConfig').treegrid('getSelected');
        if (row != null) {
            if(row.codeLevel=="3"){
                $.messager.alert("新增失败！", "请先选择类型", "warning");
            }else {
                $('#parentId').val(row.id);
                $('#codeLevel').val(row.codeLevel + 1);
                $('#level').val(row.codeLevel + 1);
                $('#parentName').val(row.codeValue1);
                $('#newsConfig_dialog_add').dialog('open');
                $('input[name="codeValue1"]').focus();
            }
        } else {
            $.messager.alert("新增失败！", "请先选择类型", "warning");
        }

    },
    //新增类型
    addType : function () {
        //打开对话框前 重置表单
        $('#newsConfig_add').form('reset');
        //重置表单验证
        $("#newsConfig_add").validate().resetForm();
        $('#newsConfig_add').form('resetValidation');
        $('#newsConfig_add').form('disableValidation');
        $("input").removeClass("error");
        $('#parentId').val(1005);
        $('#codeLevel').val(2);
        $('#level').val(2);
        $('#parentName').val(null);
        $('#newsConfig_dialog_add').dialog('open');
        $('input[name="codeValue1"]').focus();
    },

    remove : function () {
        var rows = $('#newsConfig').treegrid('getSelections');
        if (rows.length > 0) {
            $.messager.confirm('确定操作', '您正在要删除所选的记录吗？', function (flag) {
                if (flag) {
                    var ids = "";
                    for (var i = 0; i < rows.length; i ++) {
                        ids += rows[i].id+",";
                    }
                    ids = ids.substring(0,ids.length-1);
                    $.ajax({
                        type : 'POST',
                        url :BASE_HREF+"manager/newsConfig/del",
                        data : {
                            id : ids,
                        },
                        beforeSend : function () {
                            $('#newsConfig').treegrid('loading');
                        },
                        success : function (data) {
                            ajaxLoadEnd();
                            switch (data.state) {
                                case 0:
                                    $.messager.alert('失败', data.message, 'error');
                                    break;
                                case 1:
                                    $.messager.show({
                                        title : '提示',
                                        msg :'删除成功！',
                                    });
                                    $('#newsConfig').treegrid('unselectAll');
                                    oldSelectedRow=null;
                                    $('#newsConfig').treegrid('reload');
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