<%@ page contentType="text/html;charset=UTF-8" language="java" pageEncoding="utf-8" %>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<jsp:include page="../../include/tag.jsp"/>
<title>WarehouseManagement-管理系统</title>
<jsp:include page="../../include/head.jsp"/>
</head>
<body>
<script type="text/javascript">
    function addRoleConfig(){
        var rows = $('#roleConfigTable').datagrid('getChecked');
        if(rows==null || rows.length<=0){
            $.messager.alert('失败','请先选角色！！！', "error");
            return;
        }
        var ids="";
        $.each(rows,function(i,n){
            if(i==rows.length-1){
                ids+=n.id;
            }else{
                ids+=n.id+',';
            }
        });
        $.ajax({
            url : '${basePath}manager/user/role/add',
            data : 'userId=${id}&ids='+ids,
            type : 'POST',
            beforeSend : function() {
                $("#win_role_config").window("close");
                ajaxLoading("正在提交请稍候。。。");
            },
            success : function(data) {
                ajaxLoadEnd();
                switch (data.state) {
                    case 0:
                        $.messager.alert('失败', data.message, "error",function(){
                            $("#win_user_config").window("open");
                        });
                        break;
                    case 1:
                        $.messager.alert('成功', '操作成功');
                        $("#win_user_config").window("close");
                        break;
                    case 2:
                        $.messager.alert('失败', data.message, "error",function(){
                            $("#win_user_config").window("open");
                        });
                        break;
                    default:
                        break;
                }
            }
        });
    }
</script>
<table id="roleConfigTable"  class="easyui-datagrid"
       data-options="url:'${basePath}manager/user/role/list?id=${id}',rownumbers:'true',idField:'id',
            fit:true,fitColumns: true,toolbar:'#tb_m_role'">
    <thead>
    <tr>
        <th data-options="field:'id'"  align="center"
            checkbox=true>ID</th>
        <th data-options="field:'roleName'" width="120" align="center" >角色名称</th>
    </tr>
    </thead>
</table>
<div id="tb_m_role" style="padding:5px;height:auto">
    <table width="100%">
        <tr>
            <td align="left"><a href="javaScript:void(0)"onclick="addRoleConfig();"
                                class="easyui-linkbutton"
                                data-options="plain:true,iconCls:'icon-add'"
                                style="padding-left: 5px;">加入</a></td>
            <td align="right">&nbsp;</td>
        </tr>
    </table>
</div>

</body>
</html>
