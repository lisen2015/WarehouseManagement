<%@ page contentType="text/html;charset=UTF-8" language="java" pageEncoding="utf-8" %>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<jsp:include page="../../include/tag.jsp"/>
<title>WarehouseManagement-管理系统</title>
<jsp:include page="../../include/head.jsp"/>
</head>
<body>

<table id="departmentTable" class="easyui-datagrid"
       data-options="url:'${basePath}manager/department/list',singleSelect:false,rownumbers:'true',idField:'id',
            fit:true,fitColumns:true,toolbar:'#tb_m_department',onDblClickRow:dblClickRow">
    <thead>
    <tr>
        <th data-options="field:'id',checkbox:true" align="left"></th>
        <th data-options="field:'departmentName'" width="200" align="center">部门名称</th>
        <th data-options="field:'departmentDesc'" width="400" align="center">部门说明</th>
        <th data-options="field:'inputTimeStr'" width="400" align="center">创建时间</th>
    </tr>
    </thead>
</table>
<div id="tb_m_department" style="padding:5px;height:auto">
    <table width="100%">
        <tr>
            <td align="left">
                <c:if test="${user.loginName eq 'admin' || functionMap.btn_department_add }">
                    <a href="javascript:void(0)" class="easyui-linkbutton" iconCls="icon-add" plain="true"
                       onclick="addDepartmentDialog()">新增</a>
                </c:if>

                <c:if test="${user.loginName eq 'admin' || functionMap.btn_department_update }">
                    <a href="javascript:void(0)" class="easyui-linkbutton" iconCls="icon-edit" plain="true"
                       onclick="updateDepartmentDialog(true)">修改</a>
                </c:if>

                <c:if test="${user.loginName eq 'admin' || functionMap.btn_department_delete }">
                    <a href="javascript:void(0)" class="easyui-linkbutton" iconCls="icon-remove" plain="true"
                       onclick="deleteDepartmentDialog(true)">删除</a>
                </c:if>
            </td>
        </tr>
    </table>
</div>
<div id="dlg_m_department" style="width:700px;height:350px;padding:10px 10px;">
    <div class="ftitle" id="errorMessage" style="color: red"></div>
    <form id="departmentForm" method="post">
        <input type="hidden" id="id" name="id">
        <table width="660">
            <tr>
                <td width="90" class="font_td">部门名称:</td>
                <td colspan="3" >
                    <input type="text" id="departmentName" name="departmentName" style="width:100%" class="easyui-validatebox"
                           data-options="required:true,validType:'length[0,20]'">
                </td>
            </tr>
            <tr>
                <td colspan="4" class="font_td">部门说明:</td>
            </tr>
            <tr>
                <td colspan="4">
                    <textarea type="text" id="departmentDesc" name="departmentDesc" style="width:100%;height: 190px"></textarea>
                </td>
            </tr>
        </table>
    </form>
</div>
<div id="dlg_buttons_m_department_add" style="text-align: center;">
    <div id="dlg_buttons_m_channel_addDepartment">
        <c:if test="${user.loginName eq 'admin' || functionMap.btn_department_add }">
            <a href="javascript:void(0)" class="easyui-linkbutton" iconCls="icon-ok" onclick="addORUpdateDepartment()">确定</a>&nbsp;&nbsp;&nbsp;&nbsp;
        </c:if>
        <a href="javascript:void(0)" class="easyui-linkbutton" iconCls="icon-cancel"
           onclick="$('#dlg_m_department').dialog('close')">关闭</a>
    </div>

    <div id="dlg_buttons_m_channel_update">
        <c:if test="${user.loginName eq 'admin' || functionMap.btn_department_update }">
            <a href="javascript:void(0)" class="easyui-linkbutton" iconCls="icon-ok" onclick="addORUpdateDepartment()">确定</a>&nbsp;&nbsp;&nbsp;&nbsp;
        </c:if>
        <a href="javascript:void(0)" class="easyui-linkbutton" iconCls="icon-cancel"
           onclick="$('#dlg_m_department').dialog('close')">关闭</a>
    </div>
</div>
<div id="win_user_config"/>
<script type="text/javascript" charset="utf-8" src="${basePath}js/view/config/departmentList.js"></script>
</body>
</html>
