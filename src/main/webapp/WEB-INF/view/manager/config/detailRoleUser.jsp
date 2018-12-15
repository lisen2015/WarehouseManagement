<%@ page contentType="text/html;charset=UTF-8" language="java" pageEncoding="utf-8" %>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<jsp:include page="../../include/tag.jsp"/>
<title>WarehouseManagement-管理系统</title>
<jsp:include page="../../include/head.jsp"/>
</head>
<body>

<table id="roleConfigTable" class="easyui-datagrid"
       data-options="url:'${basePath}manager/user/role/detailList?id=${id}',rownumbers:'true',idField:'id',
            fit:true,fitColumns: true,toolbar:'#tb_m_role'">
    <thead>
    <tr>
        <th data-options="field:'id'" align="center"
            checkbox=true>ID
        </th>
        <th data-options="field:'roleName'" width="120" align="center">组织名称</th>
    </tr>
    </thead>
</table>
<div id="tb_m_role" style="padding:5px;height:auto">
    <table width="100%">
        <tr>
            <td><a href="javaScript:void(0)" onclick="delRoleUser();"
                   class="easyui-linkbutton"
                   data-options="plain:true,iconCls:'icon-cancel'"
                   style="padding-left: 5px">删除</a></td>
            <td align="right">&nbsp;</td>
        </tr>
    </table>
</div>
<script type="text/javascript" charset="utf-8" src="${basePath}js/view/config/detailRoleUser.js"></script>
</body>
</html>
