<%@ page contentType="text/html;charset=UTF-8" language="java" pageEncoding="utf-8" %>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<jsp:include page="../../include/tag.jsp"/>
<title>WarehouseManagement-管理系统</title>
<jsp:include page="../../include/head.jsp"/>
</head>
<body>
<table id="userTable" class="easyui-datagrid" style="width:800px;height:250px"
       data-options="url:'${basePath}manager/user/userList',singleSelect:false,rownumbers:'true',idField:'id',
         	fit:true,fitColumns: true,pagination:true,pageSize:20,pageList:[20,50,100,200],
            toolbar:'#tb_m_user',onDblClickRow:dblClickRow">
    <thead>
    <tr>
        <th data-options="field:'id',checkbox:true" align="left"></th>
        <th data-options="field:'loginName'" width="130" align="left">用户名</th>
        <th data-options="field:'userName'" width="130" align="center">真实姓名</th>
        <th data-options="field:'inputTimeStr'" width="130" align="center">注册时间</th>
        <th data-options="field:'isValidateStr'" width="130" align="center">状态</th>
        <c:if test="${user.loginName eq 'admin' || functionMap.btn_user_role_manager}">
            <th data-options="field:'a' ,formatter:formatRoleManager" width="130" align="center">角色管理</th>
        </c:if>
        <c:if test="${user.loginName eq 'admin' || functionMap.btn_user_role_add}">
            <th data-options="field:'b' ,formatter:formatRoleAdd" width="130" align="center">添加角色</th>
        </c:if>
    </tr>
    </thead>
</table>
<div id="dlg_m_user" style="width:450px;height:300px;padding:10px 10px;">
    <div class="ftitle" id="errorMessage" style="color: red"></div>
    <form id="userForm" method="post">
        <input type="hidden" id="id" name="id">
        <table width="390">
            <tr>
                <td width="70" class="font_td">用户名　:</td>
                <td colspan="3" width="320">
                    <input type="text" id="loginName" name="loginName" style="width:315px" class="easyui-validatebox"
                                                   data-options="required:true,delay:1000,validType:{length:[0,20],remoteByException:['${basePath}manager/user/exist','loginName',{id:'#id'}]}">
                </td>
            </tr>
            <tr id="passtd">
                <td class="font_td">密　　码:</td>
                <td colspan="3" width="320">
                    <input type="password" id="loginPassword" name="loginPassword" class="easyui-validatebox"
                                                   data-options="required:true,validType:'length[6,20]'" style="width:315px">
                </td>
            </tr>
            <tr id="passtd2">
                <td class="font_td">确认密码:</td>
                <td colspan="3" width="320">
                    <input type="password" id="secondPassword" name="secondPassword" class="easyui-validatebox"
                                                   required=true validType="equals['#loginPassword']" style="width:315px">
                </td>
            </tr>
            <tr>
                <td width="70" class="font_td">姓　　名:</td>
                <td colspan="3" width="320">
                    <input id="userName" name="userName" type="text" style="width:315px" class="easyui-validatebox"
                                                   data-options="required:true,validType:'length[0,20]'">
                </td>
            </tr>
            <tr>
                <td width="70" class="font_td">邮　　箱:</td>
                <td colspan="3" width="320">
                    <input id="email" name="email" type="text" style="width:315px" class="easyui-validatebox"
                                                   data-options="required:true,validType:{length:[0,50],email:true}">
                </td>
            </tr>
            <tr>
                <td width="70" class="font_td">状　　态:</td>
                <td colspan="3" width="320">
                    <select class="easyui-validatebox" name="isValidate" id="isValidate" style="width:315px" data-options="editable:false">
                        <option value="0">已删除</option>
                        <option value="1">已启用</option>
                        <option value="2">申请中</option>
                        <option value="3">已拒绝</option>
                    </select>
                </td>
            </tr>
        </table>
    </form>
</div>
<div id="dlg_buttons_m_user_add" style="text-align: center">
    <c:if test="${user.loginName eq 'admin' || functionMap.btn_user_add }">
        <a href="javascript:void(0)" class="easyui-linkbutton" iconCls="icon-ok" onclick="addORUpdateUser();">确定</a>&nbsp;&nbsp;&nbsp;&nbsp;
    </c:if>
    <a href="javascript:void(0)" class="easyui-linkbutton" iconCls="icon-cancel"
       onclick="$('#dlg_m_user').dialog('close')">关闭</a>
</div>

<div id="tb_m_user" style="padding:5px;height:auto">
    <table width="100%">
        <tr>
            <td align="left">
                <c:if test="${user.loginName eq 'admin' || functionMap.btn_user_add }">
                    <a href="javascript:void(0)" class="easyui-linkbutton" iconCls="icon-add" plain="true"
                       onclick="addUserDialog()">新增</a>
                </c:if>
                <c:if test="${user.loginName eq 'admin' || functionMap.btn_user_update }">
                    <a href="javascript:void(0)" class="easyui-linkbutton" iconCls="icon-edit" plain="true"
                       onclick="updateUserDialog(true)">修改</a>
                </c:if>
                <c:if test="${user.loginName eq 'admin' || functionMap.btn_user_delete }">
                    <a href="javascript:void(0)" class="easyui-linkbutton" iconCls="icon-remove" plain="true"
                       onclick="deleteUserDialog(true)">删除</a>
                </c:if>
                <c:if test="${user.loginName eq 'admin' || functionMap.btn_user_reset }">
                    <a href="javascript:void(0)" class="easyui-linkbutton" iconCls="icon-redo" plain="true"
                       onclick="resetPassWord(true)">重置密码</a>
                </c:if>
            </td>
            <td align="right">
                <input class="easyui-searchbox" data-options="searcher:searchData,prompt:'请输入内容',menu:'#mm_tb'"
                       style="width:280px">
                <div id="mm_tb" style="width:120px">
                    <div name="all">所有</div>
                    <div name="loginName">用户名</div>
                    <div name="userName">真实姓名</div>
                </div>
            </td>
        </tr>
    </table>
</div>
<div id="win_user_config"/>
<script type="text/javascript" charset="utf-8" src="${basePath}js/view/config/userList.js"></script>
</body>
</html>
