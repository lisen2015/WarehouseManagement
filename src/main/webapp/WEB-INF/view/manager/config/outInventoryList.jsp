<%@ page contentType="text/html;charset=UTF-8" language="java" pageEncoding="utf-8" %>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<jsp:include page="../../include/tag.jsp"/>
<title>WarehouseManagement-管理系统</title>
<jsp:include page="../../include/head.jsp"/>
</head>
<body>

<table id="outinventoryTable" class="easyui-datagrid"
       data-options="url:'${basePath}manager/outinventory/list',singleSelect:false,rownumbers:'true',idField:'id',
            fit:true,fitColumns:true,pagination:true,pageSize:20,pageList:[20,50,100,200],toolbar:'#tb_m_outinventory'">
    <thead>
    <tr>
        <%--<th data-options="field:'id'" align="left"></th>--%>
        <th data-options="field:'productName'" width="200" align="center">产品名称</th>
        <th data-options="field:'outPersion'" width="200" align="center">提货人</th>
        <th data-options="field:'departmentName'" width="200" align="center">所属部门</th>
        <th data-options="field:'outNumber'" width="200" align="center">提取总量</th>
        <c:if test="${user.loginName eq 'admin' || functionMap.global_show_price }">
            <th data-options="field:'price',formatter: formatPrice" width="200" align="center">产品单价</th>
            <th data-options="field:'allPrice',formatter: formatAllPrice" width="200" align="center">产品总价</th>
        </c:if>
        <th data-options="field:'commit'" width="400" align="center">产品用途</th>
        <th data-options="field:'status',formatter: formatStatus" width="200" align="center">提取状态</th>
        <th data-options="field:'inputTimeStr'" width="200" align="center">提取时间</th>
        <th data-options="field:'userName'" width="200" align="center">操作员</th>
        <th data-options="field:'description'" width="200" align="center">审核备注</th>
    </tr>
    </thead>
</table>
<div id="tb_m_outinventory" style="padding:5px;height:auto">
    <table width="100%">
        <tr>
            <td align="left">
                <c:if test="${user.loginName eq 'admin' || functionMap.btn_outinventory_add }">
                    <a href="javascript:void(0)" class="easyui-linkbutton" iconCls="icon-add" plain="true"
                       onclick="addOutInventoryDialog()">扫码出库</a>
                </c:if>
            </td>
        </tr>
    </table>
</div>
<div id="dlg_m_outinventory" style="width:500px;height:470px;padding:10px 10px;">
    <div class="ftitle" id="errorMessage" style="color: red"></div>
    <form id="outinventoryForm" method="post">
        <table width="460">
            <tr>
                <td width="90" class="font_td">产品ID:</td>
                <td colspan="2">
                    <input type="text" id="productId" name="productId" style="width:100%" class="easyui-validatebox"
                           data-options="required:true,validType:'length[0,50]'">
                </td>
                <td width="60">
                    <a href="javascript:void(0)" class="easyui-linkbutton" iconCls="icon-search"
                       onclick="searchProductById()">查询</a>
                </td>
            </tr>
            <tr>
                <td width="90" class="font_td">产品名称:</td>
                <td colspan="3">
                    <input type="text" id="productName" name="productName" style="width:100%" class="easyui-validatebox"
                           readonly="readonly">
                </td>
            </tr>
            <tr>
                <td width="90" class="font_td">产品库存:</td>
                <td colspan="3">
                    <input type="text" id="inventorys" name="inventorys" style="width:100%" class="easyui-validatebox"
                           readonly="readonly">
                </td>
            </tr>
            <tr>
                <td width="90" class="font_td">提取数量:</td>
                <td colspan="3">
                    <input type="text" id="outNumber" name="outNumber" style="width:100%" class="easyui-numberbox"
                           data-options="required:true,validType:'length[0,20]',min:1">
                </td>
            </tr>
            <tr>
                <td width="90" class="font_td">提取人:</td>
                <td colspan="3">
                    <input type="text" id="outPersion" name="outPersion" style="width:100%" class="easyui-validatebox"
                           data-options="required:true,validType:'length[0,50]'">
                </td>
            </tr>
            <tr>
                <td class="font_td">提取部门:</td>
                <td colspan="3">
                    <select class="easyui-validatebox" name="departmentId" id="departmentId" style="width:100%"
                            data-options="editable:false"></select>
                </td>
            </tr>
            <tr>
                <td colspan="4" class="font_td">产品用途:</td>
            </tr>
            <tr>
                <td colspan="4">
                    <textarea type="text" id="commit" name="commit" style="width:100%;height: 190px"></textarea>
                </td>
            </tr>
        </table>
    </form>
</div>
<div id="dlg_buttons_m_outinventory_add" style="text-align: center;">
    <div id="dlg_buttons_m_channel_addOutInventory">
        <c:if test="${user.loginName eq 'admin' || functionMap.btn_outinventory_add }">
            <a href="javascript:void(0)" class="easyui-linkbutton" iconCls="icon-ok" onclick="addOutInventory()">确定</a>&nbsp;&nbsp;&nbsp;&nbsp;
        </c:if>
        <a href="javascript:void(0)" class="easyui-linkbutton" iconCls="icon-cancel"
           onclick="$('#dlg_m_outinventory').dialog('close')">关闭</a>
    </div>
</div>
<div id="win_user_config"/>
<script type="text/javascript" charset="utf-8" src="${basePath}js/view/config/outInventoryList.js"></script>
</body>
</html>
