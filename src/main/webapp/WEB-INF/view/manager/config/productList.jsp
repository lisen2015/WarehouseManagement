<%@ page contentType="text/html;charset=UTF-8" language="java" pageEncoding="utf-8" %>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<jsp:include page="../../include/tag.jsp"/>
<title>WarehouseManagement-管理系统</title>
<jsp:include page="../../include/head.jsp"/>
</head>
<body>

<table id="productTable" class="easyui-datagrid"
       data-options="url:'${basePath}manager/product/list',singleSelect:false,rownumbers:'true',idField:'id',
            fit:true,fitColumns:true,pagination:true,pageSize:20,pageList:[20,50,100,200],toolbar:'#tb_m_product',onDblClickRow:dblClickRow">
    <thead>
    <tr>
        <th data-options="field:'id',checkbox:true" align="left">
            <input type="hidden" name="productName">
        </th>
        <th data-options="field:'productName'" width="400" align="center">产品名称</th>
        <th data-options="field:'departmentName'" width="200" align="center">所属部门</th>
        <th data-options="field:'outinventory'" width="200" align="center">已出库数量</th>
        <th data-options="field:'inventorys'" width="200" align="center">库存数量</th>
        <th data-options="field:'allNumber'" width="200" align="center">入库总量</th>
        <c:if test="${user.loginName eq 'admin' || functionMap.global_show_price }">
            <th data-options="field:'price',formatter: formatPrice" width="200" align="center">产品单价</th>
            <th data-options="field:'allPrice',formatter: formatAllPrice" width="200" align="center">库存总价</th>
        </c:if>
        <th data-options="field:'inputTimeStr'" width="400" align="center">入库时间</th>
        <th data-options="field:'lastUpdateUserName'" width="200" align="center">最后修改人</th>
    </tr>
    </thead>
</table>
<div id="tb_m_product" style="padding:5px;height:auto">
    <table width="100%">
        <tr>
            <td align="left">
                <c:if test="${user.loginName eq 'admin' || functionMap.btn_product_add }">
                    <a href="javascript:void(0)" class="easyui-linkbutton" iconCls="icon-add" plain="true"
                       onclick="addProductDialog()">新增</a>
                </c:if>

                <c:if test="${user.loginName eq 'admin' || functionMap.btn_productt_update }">
                    <a href="javascript:void(0)" class="easyui-linkbutton" iconCls="icon-edit" plain="true"
                       onclick="updateProductDialog(true)">修改</a>
                </c:if>

                <c:if test="${user.loginName eq 'admin' || functionMap.btn_product_delete }">
                    <a href="javascript:void(0)" class="easyui-linkbutton" iconCls="icon-remove" plain="true"
                       onclick="deleteProductDialog(true)">删除</a>
                </c:if>

                <c:if test="${user.loginName eq 'admin' || functionMap.btn_product_print }">
                    <a href="javascript:void(0)" class="easyui-linkbutton" iconCls="icon-print" plain="true"
                       onclick="printProductDialog(true)">打印条码</a>
                </c:if>
            </td>
        </tr>
    </table>
</div>
<div id="dlg_m_product_ecode" style="width:500px;height:250px;padding:10px 10px;display:none;">
    <canvas id="canvascode" style="width:95%;height:95%;padding:5px 5px;"></canvas>
</div>
<div id="dlg_buttons_m_product_print" style="text-align: center;display:none;">
    <div id="dlg_buttons_m_channel_print">
        <c:if test="${user.loginName eq 'admin' || functionMap.btn_product_print }">
            <a href="javascript:void(0)" class="easyui-linkbutton" iconCls="icon-ok" onclick="printProductCode()">打印</a>&nbsp;&nbsp;&nbsp;&nbsp;
        </c:if>
        <a href="javascript:void(0)" class="easyui-linkbutton" iconCls="icon-cancel"
           onclick="$('#dlg_m_product_ecode').dialog('close')">关闭</a>
    </div>
</div>
<div id="dlg_m_product" style="width:400px;height:200px;padding:10px 10px;">
    <div class="ftitle" id="errorMessage" style="color: red"></div>
    <form id="productForm" method="post">
        <input type="hidden" id="id" name="id">
        <table width="360">
            <tr>
                <td width="70" class="font_td">产品名称:</td>
                <td colspan="3">
                    <input type="text" id="productName" name="productName" style="width:100%" class="easyui-validatebox"
                           data-options="required:true,validType:'length[0,20]'">
                </td>
            </tr>
            <tr>
                <td class="font_td">产品单价:</td>
                <td colspan="3" >
                    <input type="text" id="productPrice" name="price" style="width:100%" class="easyui-validatebox"
                           data-options="required:true,validType:'length[0,8]'">
                </td>
            </tr>
            <tr>
                <td class="font_td">所属部门:</td>
                <td colspan="3">
                    <select class="easyui-validatebox" name="department" id="department" style="width:100%" data-options="editable:false"></select>
                </td>
            </tr>
        </table>
    </form>
</div>
<div id="dlg_buttons_m_product_add" style="text-align: center;">
    <div id="dlg_buttons_m_channel_addProduct">
        <c:if test="${user.loginName eq 'admin' || functionMap.btn_product_add }">
            <a href="javascript:void(0)" class="easyui-linkbutton" iconCls="icon-ok" onclick="addORUpdateProduct()">确定</a>&nbsp;&nbsp;&nbsp;&nbsp;
        </c:if>
        <a href="javascript:void(0)" class="easyui-linkbutton" iconCls="icon-cancel"
           onclick="$('#dlg_m_product').dialog('close')">关闭</a>
    </div>

    <div id="dlg_buttons_m_channel_update">
        <c:if test="${user.loginName eq 'admin' || functionMap.btn_product_update }">
            <a href="javascript:void(0)" class="easyui-linkbutton" iconCls="icon-ok" onclick="addORUpdateProduct()">确定</a>&nbsp;&nbsp;&nbsp;&nbsp;
        </c:if>
        <a href="javascript:void(0)" class="easyui-linkbutton" iconCls="icon-cancel"
           onclick="$('#dlg_m_product').dialog('close')">关闭</a>
    </div>
</div>
<div id="win_user_config"/>
<script type="text/javascript" charset="utf-8" src="${basePath}js/jquery-migrate-1.2.1.min.js"></script>
<script type="text/javascript" charset="utf-8" src="${basePath}js/jquery.jqprint-0.3.js"></script>
<script type="text/javascript" charset="utf-8" src="${basePath}js/JsBarcode.all.min.js"></script>
<script type="text/javascript" charset="utf-8" src="${basePath}js/view/config/productList.js"></script>
</body>
</html>
