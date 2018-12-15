<%@ page contentType="text/html;charset=UTF-8" language="java" pageEncoding="utf-8" %>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<jsp:include page="../../include/tag.jsp"/>
<title>WarehouseManagement-管理系统</title>
<jsp:include page="../../include/head.jsp"/>
</head>
<body>
<table id="userConfigTable" class="easyui-datagrid"
       data-options="url:'${basePath}manager/role/user/userList?id=${id}',rownumbers:'true',idField:'id',
            fit:true,fitColumns: true,toolbar:'#tb_m_user',
            pagination:true,pageSize:20,pageList:[20,50,100,200]">
    <thead>
    <tr>
        <th data-options="field:'id'" align="center"
            checkbox=true>ID
        </th>
        <th data-options="field:'loginName'" width="120" align="center">用户名</th>
        <th data-options="field:'userName'" width="120" align="center">真实姓名</th>
    </tr>
    </thead>
</table>
<div id="tb_m_user" style="padding:5px;height:auto">
    <table width="100%">
        <tr>
            <td><a href="javaScript:void(0)"
                   class="easyui-linkbutton"
                   data-options="plain:true,iconCls:'icon-add'"
                   style="padding-left: 5px" onclick="addUserConfig();">加入</a></td>
            <td align="right">
                <input class="easyui-searchbox" data-options="searcher:searchAddUserData,prompt:'请输入内容',"
                       style="width:150px"/>
            </td>
        </tr>
    </table>
</div>
<script type="text/javascript">
    function addUserConfig() {
        var rows = $('#userConfigTable').datagrid('getChecked');
        if (rows == null || rows.length <= 0) {
            $.messager.alert('失败', '请先选择用户！！！', "error");
            return;
        }
        var ids = "";
        $.each(rows, function (i, n) {
            if (i == rows.length - 1) {
                ids += n.id;
            } else {
                ids += n.id + ',';
            }
        });
        $.ajax({
            url: '${basePath}manager/role/user/add',
            data: 'roleId=${id}&ids=' + ids,
            type: 'POST',
            beforeSend: function () {
                $("#win_user_config").window("close");
                ajaxLoading("正在提交请稍候。。。");
            },
            success: function (data) {
                ajaxLoadEnd();
                switch (data.state) {
                    case 0:
                        $.messager.alert('失败', data.message, "error", function () {
                            $("#win_user_config").window("open");
                        });
                        break;
                    case 1:
                        $.messager.alert('成功', '操作成功');
                        IS_CHANGE = true;
                        $("#win_user_config").window("close");
                        break;
                    case 2:
                        $.messager.alert('失败', data.message, "error", function () {
                            $("#win_user_config").window("open");
                        });
                        break;
                    default:
                        break;
                }
            }
        });
    }
    function searchAddUserData(value) {
        $("#userConfigTable").datagrid("load", {"value": $.trim(value)});

    }
</script>
</body>
</html>
