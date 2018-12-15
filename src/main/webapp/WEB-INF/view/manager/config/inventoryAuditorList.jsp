<%@ page contentType="text/html;charset=UTF-8" language="java" pageEncoding="utf-8" %>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<jsp:include page="../../include/tag.jsp"/>
<title>WarehouseManagement-管理系统</title>
<jsp:include page="../../include/head.jsp"/>
</head>
<body>

<table id="inventoryAuditorTable" class="easyui-datagrid"
       data-options="url:'${basePath}manager/inventoryauditor/list',singleSelect:true,rownumbers:'true',idField:'id',
            fit:true,fitColumns:true,pagination:true,pageSize:20,pageList:[20,50,100,200],toolbar:'#tb_m_inventoryAuditor'">
    <thead>
    <tr>
        <th data-options="field:'id',checkbox:true" align="left"></th>
        <th data-options="field:'productId',hidden:true" width="200" align="center">产品ID</th>
        <th data-options="field:'productName'" width="200" align="center">产品名称</th>
        <th data-options="field:'outPersion'" width="200" align="center">提货人</th>
        <th data-options="field:'departmentName'" width="200" align="center">提取部门</th>
        <th data-options="field:'outNumber'" width="200" align="center">提取总量</th>
        <c:if test="${user.loginName eq 'admin' || functionMap.global_show_price }">
            <th data-options="field:'price',formatter: formatPrice" width="200" align="center">产品单价</th>
            <th data-options="field:'allPrice',formatter: formatAllPrice" width="200" align="center">产品总价</th>
        </c:if>
        <th data-options="field:'commit'" width="200" align="center">申请用途</th>
        <th data-options="field:'status',formatter: formatStatus" width="200" align="center">申请状态</th>
        <th data-options="field:'inputTimeStr'" width="200" align="center">申请时间</th>
        <th data-options="field:'userName'" width="200" align="center">申请人</th>
        <th data-options="field:'description'" width="200" align="center">备注</th>
        <th data-options="field:'auditorTimeStr'" width="200" align="center">审核时间</th>
        <th data-options="field:'auditorUser'" width="200" align="center">审核人</th>
    </tr>
    </thead>
</table>
<div id="tb_m_inventoryAuditor" style="padding:5px;height:auto">
    <table width="100%">
        <tr>
            <td align="left">
                <c:if test="${user.loginName eq 'admin' || functionMap.btn_inventoryAuditor }">
                    <a href="javascript:void(0)" class="easyui-linkbutton" iconCls="icon-search" plain="true"
                       onclick="inventoryAuditorDialog(true)">审核</a>
                </c:if>
            </td>
        </tr>
    </table>
</div>
<div id="dlg_m_inventoryAuditor" style="width:500px;height:450px;padding:10px 10px;">
    <div class="ftitle" id="errorMessage" style="color: red"></div>
    <form id="inventoryAuditorForm" method="post">
        <input type="hidden" id="id" name="id">
        <input type="hidden" id="productId" name="productId">
        <input type="hidden" id="status" name="status">
        <table width="460">
            <tr>
                <td width="90" class="font_td">产品名称:</td>
                <td colspan="3" >
                    <input type="text" id="productName" name="productName" style="width:100%" class="easyui-validatebox" disabled="disabled">
                </td>
            </tr>
            <tr>
                <td width="90" class="font_td">产品库存:</td>
                <td colspan="3" >
                    <input type="text" id="inventory" name="inventory" style="width:100%" class="easyui-validatebox" disabled="disabled">
                </td>
            </tr>
            <tr>
                <td width="90" class="font_td">提取数量:</td>
                <td colspan="3" >
                    <input type="text" id="outNumber" name="outNumber" style="width:100%" class="easyui-validatebox" readonly="readonly">
                </td>
            </tr>
            <tr>
                <td width="90" class="font_td">提取人:</td>
                <td colspan="3" >
                    <input type="text" id="outPersion" name="outPersion" style="width:100%" class="easyui-validatebox" disabled="disabled">
                </td>
            </tr>
            <tr>
                <td class="font_td">提取部门:</td>
                <td colspan="3">
                    <input type="text" id="departmentName" name="departmentName" style="width:100%" class="easyui-validatebox" disabled="disabled">
                </td>
            </tr>
            <tr>
                <td colspan="4" class="font_td">申请用途:</td>
            </tr>
            <tr>
                <td colspan="4">
                    <textarea type="text" id="commit" name="commit" style="width:100%;" disabled="disabled"></textarea>
                </td>
            </tr>
            <tr>
                <td colspan="4" class="font_td">备注说明:</td>
            </tr>
            <tr>
                <td colspan="4">
                    <textarea type="text" id="description" name="description" style="width:100%;height: 110px;"></textarea>
                </td>
            </tr>
        </table>
    </form>
</div>
<div id="dlg_buttons_m_inventoryAuditor" style="text-align: center;">
    <div id="dlg_buttons_m_channel_inventoryAuditor">
        <c:if test="${user.loginName eq 'admin' || functionMap.btn_inventoryAuditor }">
            <a href="javascript:void(0)" class="easyui-linkbutton" iconCls="icon-ok" onclick="inventoryAuditor(true)">通过</a>&nbsp;&nbsp;&nbsp;&nbsp;
            <a href="javascript:void(0)" class="easyui-linkbutton" iconCls="icon-cancel" onclick="inventoryAuditor(false)">拒绝</a>&nbsp;&nbsp;&nbsp;&nbsp;
        </c:if>
        <a href="javascript:void(0)" class="easyui-linkbutton" onclick="$('#dlg_m_inventoryAuditor').dialog('close')">关闭</a>
    </div>
</div>
<div id="win_user_config"/>
<script type="text/javascript" charset="utf-8" src="${basePath}js/view/config/inventoryAuditorList.js"></script>
</body>
</html>
