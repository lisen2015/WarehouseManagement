<%@ page contentType="text/html;charset=UTF-8" language="java" pageEncoding="utf-8" %>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<jsp:include page="../../include/tag.jsp"/>
<title>WarehouseManagement-管理系统</title>
<jsp:include page="../../include/head.jsp"/>
</head>
<body>

<table id="functionCofigTable" class="easyui-datagrid"
       data-options="url:'${basePath}manager/function/list?channelId=${channelId}',rownumbers:'true',idField:'id',
            scrollbarSize:0,onDblClickRow:dblClickFunctionRow,
            fit:true,fitColumns: true,toolbar:'#tb_m_function' <c:if test="${user.loginName eq 'admin' || functionMap.btn_function_sort }">
                ,onDrop:dropConfig,onBeforeDrop:checkDrop,onLoadSuccess:function(){$(this).datagrid('enableDnd');}
            </c:if>">
    <thead>
    <tr>
        <th data-options="field:'id'" align="center"
            checkbox=true>ID
        </th>
        <th data-options="field:'functionName'" width="20" align="center">名称</th>
        <th data-options="field:'htmlId'" width="20" align="left">功能标签</th>
        <th data-options="field:'htmlUrl'" width="30" align="left">URL</th>
    </tr>
    </thead>
</table>
<div id="dlg_m_function" style="width:430px;height:310px;padding:10px 10px;">
    <input type="hidden" id="channelId" name="channelId" value="${channelId}">
    <input type="hidden" id="channelName" name="channelName" value="${channelName}">
    <form id="functionForm" method="post">
        <input type="hidden" id="id" name="id">
        <table width="390">
            <tr>
                <td width="70" class="font_td">功能名称:</td>
                <td width="320" colspan="3">
                    <input id="functionName" type="text" name="functionName" style="width:100%" class="easyui-validatebox"
                           data-options="required:true,validType:'length[0,20]'">
                </td>
            </tr>
            <tr>
                <td width="70" class="font_td">功能标签:</td>
                <td colspan="3">
                    <input id="htmlId" type="text" name="htmlId" style="width:100%" class="easyui-validatebox"
                           data-options="required:true,delay:1000,validType:{length:[0,50],remoteByException:['${basePath}manager/function/exist','htmlId',{id:'#functionForm>#id'}]}">
                </td>
            <tr>
                <td class="font_td" width="70">功能URL:</td>
                <td colspan="3" width="320">
                    <input id="htmlUrlSelect" class="easyui-textbox" style="width:100%" data-options="iconWidth: 22, icons: [{
	                				iconCls:'icon-add', handler: addHtmlValue},{iconCls:'icon-remove',handler: removeHtmlValue}]">
                </td>
            </tr>
            <tr>
                <td colspan="4" width="390">
                    <select id=htmlUrl name="htmlUrl" multiple="multiple" style="width:100%;height: 110px;"></select>
                </td>
            </tr>
        </table>
    </form>
</div>
<div id="dlg_buttons_m_function" style="text-align: center;">
    <div id="dlg_buttons_m_function_add">
        <c:if test="${user.loginName eq 'admin' || functionMap.btn_function_add }">
            <a href="javascript:void(0)" class="easyui-linkbutton" iconCls="icon-ok"
               onclick="addORUpdateFunction()">确定</a>&nbsp;&nbsp;&nbsp;&nbsp;
        </c:if>
        <a href="javascript:void(0)" class="easyui-linkbutton" iconCls="icon-cancel"
           onclick="$('#dlg_m_function').dialog('close')">关闭</a>
    </div>
    <div id="dlg_buttons_m_function_update">
        <c:if test="${user.loginName eq 'admin' || functionMap.btn_function_update }">
            <a href="javascript:void(0)" class="easyui-linkbutton" iconCls="icon-ok"
               onclick="addORUpdateFunction()">确定</a>&nbsp;&nbsp;&nbsp;&nbsp;
        </c:if>
        <a href="javascript:void(0)" class="easyui-linkbutton" iconCls="icon-cancel"
           onclick="$('#dlg_m_function').dialog('close')">关闭</a>
    </div>
</div>
<div id="tb_m_function" style="padding:5px;height:auto">
    <table width="100%">
        <tr>
            <td>
                <c:if test="${user.loginName eq 'admin' || functionMap.btn_function_add }">
                    <a href="javaScript:void(0)" onclick="addFunctionDialog();" class="easyui-linkbutton"
                       data-options="plain:true,iconCls:'icon-add'" style="padding-left: 5px">新增</a>
                </c:if>
                <c:if test="${user.loginName eq 'admin' || functionMap.btn_function_update }">
                    <a href="javaScript:void(0)" onclick="updateFunctionDialog(true);" class="easyui-linkbutton"
                       data-options="plain:true,iconCls:'icon-edit'" style="padding-left: 5px">修改</a>
                </c:if>
                <c:if test="${user.loginName eq 'admin' || functionMap.btn_function_delete }">
                    <a href="javaScript:void(0)" onclick="delFunctionDialog(true);" class="easyui-linkbutton"
                       data-options="plain:true,iconCls:'icon-remove'" style="padding-left: 5px">删除</a>
                </c:if>
            </td>
            <td align="right">&nbsp;</td>
        </tr>
    </table>
</div>
<script type="text/javascript" src="${basePath}js/view/config/functionList.js"></script>
</body>
</html>
