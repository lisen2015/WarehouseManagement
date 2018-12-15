$(function () {

    $('#manager').datagrid({
        url : LOCALHOST+'rule/getAllAdmin',
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
        toolbar : '#manager_tool',
        columns : [[
            {
                field : 'id',
                title : '自动编号',
                width : 100,
                checkbox : true,
            },
            {
                field : 'manager',
                title : '管理员帐号',
                width : 100,
            },
            {
                field : 'auth',
                title : '拥有权限',
                width : 100,
            },
            {
                field : 'date',
                title : '创建日期',
                width : 100,
            },
        ]],
    });

    $('#manager_add').dialog({
        width : 350,
        title : '新增管理',
        modal : true,
        closed : true,
        iconCls : 'icon-user-add',
        buttons : [{
            text : '提交',
            iconCls : 'icon-add-new',
            handler : function () {
                if ($('#manager_add').form('validate')) {
                    var user = {
                        manager : $('input[name="manager"]').val(),
                        password : $('input[name="password"]').val(),
                        auth : $('#auth').combotree('getText'),
                        date : 111
                    };

                    $.ajax({
                        url : LOCALHOST+'rule/addAdmin',
                        type : 'post',
                        data : user,
                        beforeSend : function () {
                            $.messager.progress({
                                text : '正在新增中...',
                            });
                        },
                        success : function (data, response, status) {
                            $.messager.progress('close');

                            if (data > 0) {
                                $.messager.show({
                                    title : '提示',
                                    msg : '新增管理成功',
                                });
                                $('#manager_add').dialog('close').form('reset');
                                $('#manager').datagrid('reload');
                            } else {
                                $.messager.alert('新增失败！', '未知错误导致失败，请重试！', 'warning');
                            }
                        }
                    });
                }
            },
        },{
            text : '取消',
            iconCls : 'icon-redo',
            handler : function () {
                $('#manager_add').dialog('close').form('reset');
            },
        }],
    });

    $('#manager_edit').dialog({
        width : 350,
        title : '修改管理',
        modal : true,
        closed : true,
        iconCls : 'icon-user-add',
        buttons : [{
            text : '提交',
            iconCls : 'icon-edit-new',
            handler : function () {
                if ($('#manager_edit').form('validate')) {
                    var user = {
                        id : $('input[name="id"]').val(),
                        password : $('input[name="password_edit"]').val(),
                        manager:$('input[name="manager_edit"]').val(),
                        auth : $('#auth_edit').combotree('getText'),
                        date:111
                    }
                    $.ajax({
                        url : LOCALHOST+'rule/updateAdmin',
                        type : 'post',
                        data : user,
                        beforeSend : function () {
                            $.messager.progress({
                                text : '正在修改中...',
                            });
                        },
                        success : function (data, response, status) {
                            $.messager.progress('close');

                            if (data > 0) {
                                $.messager.show({
                                    title : '提示',
                                    msg : '修改管理成功',
                                });
                                $('#manager_edit').dialog('close').form('reset');
                                $('#manager').datagrid('reload');
                            } else {
                                $.messager.alert('修改失败！', '未知错误或没有任何修改，请重试！', 'warning');
                            }
                        }
                    });
                }
            },
        },{
            text : '取消',
            iconCls : 'icon-redo',
            handler : function () {
                $('#manager_edit').dialog('close').form('reset');
            },
        }],
    });

    //管理帐号
    $('input[name="manager"]').validatebox({
        required : true,
        validType : 'length[2,20]',
        missingMessage : '请输入管理名称',
        invalidMessage : '管理名称在 2-20 位',
    });

    //管理密码
    $('input[name="password"]').validatebox({
        required : true,
        validType : 'length[6,30]',
        missingMessage : '请输入管理密码',
        invalidMessage : '管理密码在 6-30 位',
    });

    //修改管理密码
    $('input[name="password_edit"]').validatebox({
        //required : true,
        validType : 'length[6,30]',
        missingMessage : '请输入管理密码',
        invalidMessage : '管理密码在 6-30 位',
    });

    //分配权限
    $('#auth').combotree({
        url :LOCALHOST+ 'rule/getNav',
        required : true,
        lines : true,
        multiple : true,
        checkbox : true,
        onlyLeafCheck : true,
        onLoadSuccess : function (node, data) {
            var _this = this;
            if (data) {
                $(data).each(function (index, value) {
                    if (this.state == 'closed') {
                        $(_this).tree('expandAll');
                    }
                });
            }
        },
    });


    manager_tool = {
        reload : function () {
            $('#manager').datagrid('reload');
        },
        redo : function () {
            $('#manager').datagrid('unselectAll');
        },
        add : function () {
            $('#manager_add').dialog('open');
            $('input[name="manager"]').focus();
        },
        remove : function () {
            var rows = $('#manager').datagrid('getSelections');
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
                            url :LOCALHOST+ 'rule/deleteAdmin',
                            data : {
                                ids : ids,
                            },
                            beforeSend : function () {
                                $('#manager').datagrid('loading');
                            },
                            success : function (data) {
                                if (data) {
                                    $('#manager').datagrid('loaded');
                                    $('#manager').datagrid('load');
                                    $('#manager').datagrid('unselectAll');
                                    $.messager.show({
                                        title : '提示',
                                        msg : data + '个管理被删除成功！',
                                    });
                                }
                            },
                        });
                    }
                });
            } else {
                $.messager.alert('提示', '请选择要删除的记录！', 'info');
            }
        },
        edit : function () {
            var rows = $('#manager').datagrid('getSelections');
            if (rows.length > 1) {
                $.messager.alert('警告操作！', '编辑记录只能选定一条数据！', 'warning');
            } else if (rows.length == 1) {

                $.ajax({
                    url :LOCALHOST+ 'rule/getAdminById',
                    type : 'post',
                    data :{
                        id:rows[0].id,
                    },
                    beforeSend : function () {
                        $.messager.progress({
                            text : '正在获取中...',
                        });
                    },
                    success : function (data, response, status) {
                        $.messager.progress('close');

                        if (data) {
                            var obj = data;
                            var auth = obj.auth.split(',');

                            $('#manager_edit').form('load', {
                                id : obj.id,
                                manager_edit : obj.manager,
                                password_edit:obj.password,
                            }).dialog('open');

                            //分配权限
                            $('#auth_edit').combotree({
                                url : LOCALHOST+'rule/getNav',
                                required : true,
                                lines : true,
                                multiple : true,
                                checkbox : true,
                                onlyLeafCheck : true,
                                onLoadSuccess : function (node, data) {
                                    var _this = this;
                                    if (data) {
                                        $(data).each(function (index, value) {
                                            if ($.inArray(value.text, auth) != -1) {
                                                $(_this).tree('check', value.target);
                                            }
                                            if (this.state == 'closed') {
                                                $(_this).tree('expandAll');
                                            }
                                        });
                                    }
                                },
                            });

                        } else {
                            $.messager.alert('获取失败！', '未知错误导致失败，请重试！', 'warning');
                        }
                    }
                });
            } else if (rows.length == 0) {
                $.messager.alert('警告操作！', '编辑记录至少选定一条数据！', 'warning');
            }
        },
    };



});