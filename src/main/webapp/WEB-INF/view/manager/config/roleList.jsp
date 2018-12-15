<%@ page contentType="text/html;charset=UTF-8" language="java" pageEncoding="utf-8" %>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<jsp:include page="../../include/tag.jsp"/>
<title>WarehouseManagement-管理系统</title>
<jsp:include page="../../include/head.jsp"/>
</head>
<body>

<table id="roleTable" class="easyui-datagrid"
       data-options="url:'${basePath}manager/role/roleList',singleSelect:false,rownumbers:'true',idField:'id',
            fit:true,fitColumns:true,toolbar:'#tb_m_role',onDblClickRow:dblClickRow
            <c:if test="${user.loginName eq 'admin' || functionMap.btn_role_sort }">
                ,onDrop:dropConfig,onBeforeDrop:checkDrop,onLoadSuccess:function(){$(this).datagrid('enableDnd');}
            </c:if>">
    <thead>
    <tr>
        <th data-options="field:'id',checkbox:true" align="left"></th>
        <th data-options="field:'roleName'" width="200" align="center">角色名称</th>
        <th data-options="field:'roleDesc'" width="400" align="center">角色说明</th>
        <c:if test="${user.loginName eq 'admin' || functionMap.btn_role_user_manager }">
            <th data-options="field:'parentId1',formatter:formatUser" width="80" align="center">用户管理</th>
        </c:if>
        <c:if test="${user.loginName eq 'admin' || functionMap.btn_role_user_add }">
            <th data-options="field:'parentId2',formatter:formatUserAdd" width="80" align="center">添加用户</th>
        </c:if>
        <c:if test="${user.loginName eq 'admin' || functionMap.btn_role_function_authity_manager }">
            <th data-options="field:'inputTime',formatter:formatFunctionAuth" width="100" align="center">功能权限</th>
        </c:if>
    </tr>
    </thead>
</table>
<div id="tb_m_role" style="padding:5px;height:auto">
    <table width="100%">
        <tr>
            <td align="left">
                <c:if test="${user.loginName eq 'admin' || functionMap.btn_role_add }">
                    <a href="javascript:void(0)" class="easyui-linkbutton" iconCls="icon-add" plain="true"
                       onclick="addRoleDialog()">新增</a>
                </c:if>

                <c:if test="${user.loginName eq 'admin' || functionMap.btn_role_update }">
                    <a href="javascript:void(0)" class="easyui-linkbutton" iconCls="icon-edit" plain="true"
                       onclick="updateRoleDialog(true)">修改</a>
                </c:if>

                <c:if test="${user.loginName eq 'admin' || functionMap.btn_role_delete }">
                    <a href="javascript:void(0)" class="easyui-linkbutton" iconCls="icon-remove" plain="true"
                       onclick="deleteRoleDialog(true)">删除</a>
                </c:if>
            </td>
            <td align="right">
                <input class="easyui-searchbox" data-options="searcher:searchData,prompt:'请输入内容',menu:'#mm_tb'"
                       style="width:280px">
                <div id="mm_tb" style="width:120px">
                    <div name="all">所有</div>
                    <div name="roleName">角色名称</div>
                    <div name="roleDesc">角色说明</div>
                </div>
            </td>
        </tr>
    </table>
</div>
<div id="dlg_m_role" style="width:700px;height:350px;padding:10px 10px;">
    <div class="ftitle" id="errorMessage" style="color: red"></div>
    <form id="roleForm" method="post">
        <input type="hidden" id="id" name="id">
        <table width="660">
            <tr>
                <td width="90" class="font_td">角色名称:</td>
                <td colspan="3" >
                    <input type="text" id="roleName" name="roleName" style="width:100%" class="easyui-validatebox"
                           data-options="required:true,validType:'length[0,20]'">
                </td>
            </tr>
            <tr>
                <td colspan="4" class="font_td">角色说明:</td>
            </tr>
            <tr>
                <td colspan="4">
                    <textarea type="text" id="roleDesc" name="roleDesc" style="width:100%;height: 190px"></textarea>
                </td>
            </tr>
        </table>
    </form>
</div>
<div id="dlg_buttons_m_role_add" style="text-align: center;">
    <div id="dlg_buttons_m_channel_addRole">
        <c:if test="${user.loginName eq 'admin' || functionMap.btn_role_add }">
            <a href="javascript:void(0)" class="easyui-linkbutton" iconCls="icon-ok" onclick="addORUpdateRole()">确定</a>&nbsp;&nbsp;&nbsp;&nbsp;
        </c:if>
        <a href="javascript:void(0)" class="easyui-linkbutton" iconCls="icon-cancel"
           onclick="$('#dlg_m_role').dialog('close')">关闭</a>
    </div>

    <div id="dlg_buttons_m_channel_update">
        <c:if test="${user.loginName eq 'admin' || functionMap.btn_role_update }">
            <a href="javascript:void(0)" class="easyui-linkbutton" iconCls="icon-ok" onclick="addORUpdateRole()">确定</a>&nbsp;&nbsp;&nbsp;&nbsp;
        </c:if>
        <a href="javascript:void(0)" class="easyui-linkbutton" iconCls="icon-cancel"
           onclick="$('#dlg_m_role').dialog('close')">关闭</a>
    </div>
</div>
<div id="win_user_config"/>
<script type="text/javascript" charset="utf-8" src="${basePath}js/view/config/roleList.js"></script>
</body>
</html>
