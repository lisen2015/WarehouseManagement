<%@ page contentType="text/html;charset=UTF-8" language="java" pageEncoding="utf-8" %>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<jsp:include page="../../include/tag.jsp"/>
<title>WarehouseManagement-管理系统</title>
<jsp:include page="../../include/head.jsp"/>
</head>
<body>
<br/> <br/>
<form id="userPasswordForm" name="userPasswordForm" method="post">
    <input type="hidden" id="id" name="id">
    <table width="390" align="center">
        <tr>
            <td class="font_td">原 密 码:</td>
            <td colspan="3" width="320">
                <input type="password" id="loginPassword" name="loginPassword" class="easyui-validatebox"
                                               data-options="required:true"  style="width:315px">
            </td>
        </tr>
        <tr id="passtd">
            <td class="font_td">密　　码:</td>
            <td colspan="3" width="320">
                <input type="password" id="newLoginPassword" name="newLoginPassword" style="width:315px" class="easyui-validatebox"
                       data-options="required:true,validType:'length[6,20]'" >
            </td>
        </tr>
        <tr id="passtd2">
            <td class="font_td">确认密码:</td>
            <td colspan="3" width="320">
                <input type="password" id="secondPassword" name="secondPassword" style="width:315px" class="easyui-validatebox"
                       required=true validType="equals['#newLoginPassword']">
            </td>
        </tr>
    </table>

    <div id="dlg_buttons_m_user_add" style="text-align: center;padding-top: 10px">
        <a href="javascript:void(0)" class="easyui-linkbutton" iconCls="icon-ok" onclick="resetPassword();">修改</a>&nbsp;&nbsp;&nbsp;&nbsp;
        <a href="javascript:void(0)" class="easyui-linkbutton" iconCls="icon-cancel" onclick="resetUserForm();">重置</a>
    </div>
</form>

<div id="win_user_config"/>
<script type="text/javascript" src="${basePath}js/view/config/resetPassword.js"></script>

</body>
</html>
