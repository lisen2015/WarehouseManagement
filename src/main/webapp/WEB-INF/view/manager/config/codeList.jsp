<%@ page contentType="text/html;charset=UTF-8" language="java" pageEncoding="utf-8" %>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<jsp:include page="../../include/tag.jsp"/>
<title>WarehouseManagement-管理系统</title>
<jsp:include page="../../include/head.jsp"/>
</head>
<body>
<table id="codeTable" class="easyui-treegrid" style="width:800px;height:250px"
       data-options="url:'${basePath}manager/code/codeList',singleSelect:true,selectOnCheck:true,checkOnSelect:true,rownumbers:'true',idField:'id',
            treeField:'id',fit:true,fitColumns: true,onClickRow:clickRow,toolbar:'#tb_m_code',onDblClickRow:dblClickRow">
    <thead>
    <tr>
        <th data-options="field:'id'" width="70">ID</th>
        <th data-options="field:'codeIndex'" width="50" align="center">索引</th>
        <th data-options="field:'codeDesc'" width="100" align="center">说明</th>
        <th data-options="field:'codeValue1'" width="275" align="center">值1</th>
        <th data-options="field:'codeValue2'" width="275" align="center">值2</th>
    </tr>
    </thead>
</table>


<div id="dlg_m_code" style="width:430px;height:370px;padding:10px 10px;">
    <form id="codeForm" name="codeForm" method="post">
        <table width="390">
            <tr>
                <td width="60" class="font_td">　　I　D:</td>
                <td width="80" align="left">
                    <input type="text" id="id" name="id" style="width:68px" class="easyui-validatebox"
                           data-options="required:true,delay:1000,validType:{integer:true,range: [1, 10000000],remoteByException:['${basePath}manager/code/exist','id',{id:'#ids'}]}">
                    <input id="ids" name="ids" type="hidden">
                </td>
                <td width="60" class="font_td">　　索引:</td>
                <td>
                    <input type="text" id="codeIndex" name="codeIndex" style="width:177px" class="easyui-validatebox"
                           data-options="required:false,validType:{range:[0,1000],integer:true}">
                </td>
            </tr>
            <tr>
                <td width="60" class="font_td" align="left">说　　明:</td>
                <td colspan="3" width="330">
                    <input type="text" id="codeDesc" name="codeDesc" style="width:325px"
                           class="easyui-validatebox"
                           data-options="required:true,validType:'length[0,200]'">
                </td>
            </tr>

            <tr>
                <td width="60" class="font_td">等　　级:</td>
                <td width="80" align="left">
                    <input disabled="disabled" type="text" id="level" name="level" style="width:68px">
                    <input id="codeLevel" name="codeLevel" type="hidden">
                </td>
                <td width="60" class="font_td">　隶属于:</td>
                <td>
                    <input type="text" disabled="disabled" id="parentName" name="parentName" style="width:177px">
                    <input name="parentId" id="parentId" type="hidden">
                </td>
            </tr>
            <tr>
                <td class="font_td" colspan="4"> 值　　1:</td>
            </tr>
            <tr>
                <td colspan="4">
                    <textarea id="codeValue1" name="codeValue1"
                                          style="height: 60px;width: 390px" class="easyui-validatebox"
                                          data-options="required:false,validType:'length[0,200]'" ></textarea>
                </td>
            </tr>
            <tr>
                <td class="font_td" colspan="4"> 值　　2:</td>
            </tr>
            <tr>
                <td colspan="4">
                    <textarea id="codeValue2" name="codeValue2" style="height: 60px;width: 390px" class="easyui-validatebox"
                                          data-options="required:false,validType:'length[0,800]'"></textarea>
                </td>
            </tr>

        </table>
    </form>
</div>
<div id="dlg_buttons_m_code" style="text-align: center;">
    <div id="dlg_buttons_m_code_add">
        <c:if test="${user.loginName eq 'admin' || functionMap.btn_code_add }">
            <a href="javascript:void(0)" class="easyui-linkbutton" iconCls="icon-ok" onclick="addORUpdateCode()">确定</a>&nbsp;&nbsp;&nbsp;&nbsp;
        </c:if>
        <a href="javascript:void(0)" class="easyui-linkbutton" iconCls="icon-cancel"
           onclick="$('#dlg_m_code').dialog('close')">关闭</a>
    </div>
    <div id="dlg_buttons_m_code_update">
        <c:if test="${user.loginName eq 'admin' || functionMap.btn_code_update }">
            <a href="javascript:void(0)" class="easyui-linkbutton" iconCls="icon-ok" onclick="addORUpdateCode()">确定</a>&nbsp;&nbsp;&nbsp;&nbsp;
        </c:if>
        <a href="javascript:void(0)" class="easyui-linkbutton" iconCls="icon-cancel"
           onclick="$('#dlg_m_code').dialog('close')">关闭</a>
    </div>
</div>
<div id="tb_m_code" style="padding:5px;height:auto">
    <table width="100%">
        <tr>
            <td align="left">
                <c:if test="${user.loginName eq 'admin' || functionMap.btn_code_add }">
                    <a href="javascript:void(0)" class="easyui-linkbutton" iconCls="icon-add" plain="true"
                       onclick="addCodeDialog()">新增</a>
                </c:if>
                <c:if test="${user.loginName eq 'admin' || functionMap.btn_code_update }">
                    <a href="javascript:void(0)" class="easyui-linkbutton" iconCls="icon-edit" plain="true"
                       onclick="updateCodeDialog()">修改</a>
                </c:if>
                <c:if test="${user.loginName eq 'admin' || functionMap.btn_code_delete }">
                    <a href="javascript:void(0)" class="easyui-linkbutton" iconCls="icon-remove" plain="true"
                       onclick="deleteCodeDialog()">删除</a>
                </c:if>
            </td>
        </tr>
    </table>
</div>
<div id="win_user_config"/>
<script type="text/javascript" src="${basePath}js/view/config/codeList.js"></script>
</body>
</html>
