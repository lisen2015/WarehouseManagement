<%@ page contentType="text/html;charset=UTF-8" language="java" pageEncoding="utf-8" %>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<jsp:include page="../../include/tag.jsp"/>
<title>WarehouseManagement-管理系统</title>
<jsp:include page="../../include/head.jsp"/>
</head>
<body>
<div class="easyui-layout" style="height:auto" data-options="fit:true">
    <div id="channelDiv" data-options="region:'west',border:false" style="width:350px;">
        <table id="authorityTable" class="easyui-treegrid"
               data-options="url:'${basePath}manager/${authority}/authority/list?${typeId}=${id}',
					singleSelect:true,rownumbers:'true',idField:'CHANNELID',toolbar:'#tb_m_authority',fitColumns: true,onClickRow:clickChannelRow,
		            treeField:'CHANNELNAME',fit:true,onLoadSuccess:expendChannelNodeAll">
            <thead>
            <th data-options="field:'CHANNELNAME'" width="50" align="left">栏目名称</th>
            <th data-options="field:'ISAUTH',formatter:formatChannelAuthor" width="20" align="center">访问</th>
            </thead>
        </table>
        <div id="tb_m_authority" style="padding:5px;height:auto">
            <div><a href="javaScript:void(0)" onclick="submitChannelAuth();" class="easyui-linkbutton"
                    data-options="plain:true,iconCls:'icon-save'">　保存　</a>
            </div>
        </div>
    </div>
    <div id="functionDiv" data-options="region:'center',border:false">
        <table id="functionCofigTable" class="easyui-datagrid"
               data-options="url:'${basePath}manager/${authority}/authority/functionList?${typeId}=${id}',
				idField:'FUNCTIONID',scrollbarSize:0,fit:true,onClickRow:clickFunctionRow,fitColumns: true,toolbar:'#tb_m_function',
				onLoadSuccess:functionLoadSuccess">
            <thead>
            <tr>
                <th data-options="field:'FUNCTIONNAME'" width="40" align="left">名称</th>
                <th data-options="field:'ISAUTH',formatter:formatFunctionAuthor" width="20" align="center">操作</th>
            </tr>
            </thead>
        </table>
        <div id="tb_m_function" style="padding:5px;height:auto">
            <div>
                <a href="javaScript:void(0)" onclick="submitFunctionAuth();" class="easyui-linkbutton"
                   data-options="plain:true,iconCls:'icon-save'">　保存　</a>
            </div>
        </div>
    </div>
</div>
<script type="text/javascript">
    function functionLoadSuccess(data) {
        $('#functionCofigTable').datagrid('clearChecked');
        $('input[type=checkbox]').click(function (event) {
            event.stopPropagation();
        });
    }

    function submitChannelAuth() {
        var authList=[];
        $('#channelDiv').find('input[type=hidden]').each(function (i, n) {
            if (n.value != null && n.value != '') {
                var id = $('#authorityTable').treegrid('find', n.name).ID;
                var json={
                    id:id,
                    channelId:n.name,
                    isAuth:n.value,
                    ${typeId}:'${id}'
                };
                authList.push(json);
            }
        });
        if (authList.length > 0) {
            $.ajax({
                url: '${basePath}manager/${authority}/authority/add',
                data: JSON.stringify(authList),
                dataType: 'json',
                contentType: 'application/json',
                type: 'POST',
                beforeSend: function () {
                    $('#win_user_config').window('close');
                    ajaxLoading('正在提交请稍候。。。');
                },
                success: function (data) {
                    ajaxLoadEnd();
                    switch (data.state) {
                        case 0:
                            $.messager.alert('失败', data.message, 'error', function () {
                                $('#win_user_config').window('open');
                            });
                            break;
                        case 1:
                            $.messager.alert('成功', '操作成功', 'success', function () {
                                $('#authorityTable').treegrid('reload');
                                $('#win_user_config').window('open');
                            });
                            break;
                        case 2:
                            $.messager.alert('失败', data.message, 'error', function () {
                                $('#win_user_config').window('open');
                            });
                            break;
                        default:
                            break;
                    }
                }
            });
        }

    }
    function submitFunctionAuth() {
        var channel = $('#authorityTable').datagrid('getSelected');
        if (channel == null || $('#functionDiv').find('input[type=hidden][name!=fun_id]').length == 0) {
            return;
        }
        var authList =[];
        $('#functionDiv').find('input[type=hidden][name!=fun_id]').each(function (i, n) {
            if (n.value != null && n.value != '') {
                var id = $(this).next('input').val();
                if (!id||id =='null' || id =='') {
                    id = null;
                }
                var json={
                    id:id,
                    channelId:channel.CHANNELID,
                    functionId:n.name,
                    isAuth:n.value,
                    ${typeId}:'${id}'
                };
                authList.push(json);
            }
        });
        if (authList.length > 0) {
            $.ajax({
                url: '${basePath}manager/${authority}/authority/addFunction',
                data: JSON.stringify(authList),
                dataType: 'json',
                contentType: 'application/json',
                type: 'POST',
                beforeSend: function () {
                    $('#win_user_config').window('close');
                    ajaxLoading('正在提交请稍候。。。');
                },
                success: function (data) {
                    ajaxLoadEnd();
                    switch (data.state) {
                        case 0:
                            $.messager.alert('失败', data.message, 'error', function () {
                                $('#win_user_config').window('open');
                            });
                            break;
                        case 1:
                            $.messager.alert('成功', '操作成功', 'success', function () {
                                $('#functionCofigTable').datagrid('reload');
                                $('#win_user_config').window('open');
                            });
                            break;
                        case 2:
                            $.messager.alert('失败', data.message, 'error', function () {
                                $('#win_user_config').window('open');
                            });
                            break;
                        default:
                            break;
                    }
                }
            });
        }
    }
    function formatChannelAuthor(value, row, index) {
        if (value == null || value == 0) {
            return '<input type="checkbox" id="chk_' + row.CHANNELID + '" name="' + row.CHANNELID + '" onchange="changeChannelCheck(this)" />'
                    + '<input id="val_cha_' + row.CHANNELID + '" name="' + row.CHANNELID + '" type="hidden" >';
        } else {
            return '<input type="checkbox" checked="checked" id="chk_' + row.CHANNELID + '" name="' + row.CHANNELID + '" onchange="changeChannelCheck(this)" />'
                    + '<input id="val_cha_' + row.CHANNELID + '" name="' + row.CHANNELID + '" type="hidden" >';
        }
    }
    function formatFunctionAuthor(value, row, index) {
        if (value == null || value == 0) {
            return '<input type="checkbox" id="chk_fun_' + row.FUNCTIONID + '" name="' + row.FUNCTIONID + '" onchange="changeFunctionCheck(this)" />'
                    + '<input id="val_fun_' + row.FUNCTIONID + '" name="' + row.FUNCTIONID + '" type="hidden" >'
                    + '<input name="fun_id" type="hidden" value="' + row.ID + '" >';
        } else {
            return '<input type="checkbox" checked="checked" id="chk_fun_' + row.FUNCTIONID + '"  name="' + row.FUNCTIONID + '" onchange="changeFunctionCheck(this)" />'
                    + '<input id="val_fun_' + row.FUNCTIONID + '" name="' + row.FUNCTIONID + '" type="hidden" >'
                    + '<input  name="fun_id" type="hidden" value="' + row.ID + '">';
        }
    }
    function changeChannelCheck(obj) {
        if ($(obj).prop('checked')) {
            $('#val_cha_' + $(obj).attr('name')).val(1);
        } else {
            $('#val_cha_' + $(obj).attr('name')).val(0);
        }
    }
    function changeFunctionCheck(obj) {
        if ($(obj).prop('checked')) {
            $('#val_fun_' + $(obj).attr('name')).val(1);
        } else {
            $('#val_fun_' + $(obj).attr('name')).val(0);
        }
    }
    function clickChannelRow(row) {
        $('#functionCofigTable').datagrid('load', {
            channelId: row.CHANNELID
        });
    }
    function clickFunctionRow(index, row) {
        var checked = $('#chk_fun_' + row.FUNCTIONID);
        $(checked).prop('checked') ? $(checked).prop('checked', false) : $(checked).prop('checked', true);
        changeFunctionCheck(checked);
    }
    function expendChannelNodeAll() {
        $('#authorityTable').treegrid('expandAll');
    }
</script>
</body>
</html>
