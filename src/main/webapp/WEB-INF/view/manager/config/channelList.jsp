<%@ page contentType="text/html;charset=UTF-8" language="java" pageEncoding="utf-8" %>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<jsp:include page="../../include/tag.jsp"/>
<title>WarehouseManagement-管理系统</title>
<jsp:include page="../../include/head.jsp"/>
</head>
<body>
<input type="hidden" id="zeroChannelId" value="${zeroChannelId}">
<table id="channelTable" class="easyui-treegrid"
       style="width:800px;height:250px"
       data-options="url:'${basePath}manager/channel/channelList',singleSelect:true,selectOnCheck:true,checkOnSelect:true,rownumbers:'true',idField:'id',
            treeField:'channelName',fit:true,fitColumns: true,onClickRow:clickRow,toolbar:'#tb_m_channel',onDblClickRow:dblClickRow">
    <thead>
    <tr>
        <th data-options="field:'channelName'" width="160" align="left">栏目名称</th>
        <th data-options="field:'channelIndex'" width="120" align="center">栏目索引</th>
        <c:if test="${user.loginName eq 'admin' || functionMap.btn_functon_manager }">
            <th data-options="field:'channelUrl2',formatter:formatFunction"
                width="100" align="center">功能
            </th>
        </c:if>
        <th data-options="field:'channelUrl'" width="400" align="left">栏目URL</th>
    </tr>
    </thead>
</table>

<div id="dlg_m_channel" style="width:430px;height:230px;padding:10px 10px;">
    <form id="channelForm" name="channelForm" method="post">
        <input type="hidden" id="id" name="id">
        <table width="390">
            <td width="70" class="font_td">栏目名称:</td>
            <td width="170" colspan="3">
                <input type="text" id="channelName" name="channelName" style="width:100%;" class="easyui-validatebox"
                       data-options="required:true,validType:'length[0,20]'">

            </td>

            </tr>
            <tr>
                <td width="70" class="font_td">栏目地址:</td>
                <td colspan="3" width="320">
                    <input type="text" id="channelUrl" name="channelUrl" style="width:100%;"
                           class="easyui-validatebox"
                           data-options="required:false,delay:1000,validType:{length:[0,200],remoteByException:['${basePath}manager/channel/exist','channelUrl',{id:'#id'}]}">
                </td>
            </tr>
            <tr>
                <td width="70" class="font_td">栏目索引:</td>
                <td colspan="3" width="320">
                    <input type="text" id="channelIndex" name="channelIndex"
                           style="width:100%;" class="easyui-validatebox"
                           data-options="required:false,validType:{range:[0,1000],integer:true}">
                </td>
            </tr>
            <tr>
                <td width="70" class="font_td">栏目等级:</td>
                <td width="80">
                    <input type="text" id="level" name="level" readonly="readonly" style="width:100%;;">
                    <input id="channelLevel" name="channelLevel" type="hidden">
                </td>
                <td width="70" class="font_td" align="right">隶属于:</td>
                <td width="170">
                    <input name="parentName" id="parentName" type="text" readonly="readonly" style="width:100%;;">
                    <input name="parentId" id="parentId" type="hidden">
                </td>
            </tr>
        </table>


    </form>
</div>
<div id="dlg_buttons_m_channel" style="text-align: center;">
    <div id="dlg_buttons_m_channel_add">
        <c:if test="${user.loginName eq 'admin' || functionMap.btn_channel_add }">
            <a href="javascript:void(0)" class="easyui-linkbutton" iconCls="icon-ok"
               onclick="addORUpdateChannel()">确定</a>&nbsp;&nbsp;&nbsp;&nbsp;
        </c:if>
        <a href="javascript:void(0)" class="easyui-linkbutton" iconCls="icon-cancel"
           onclick="$('#dlg_m_channel').dialog('close')">关闭</a>
    </div>
    <div id="dlg_buttons_m_channel_update">
        <c:if test="${user.loginName eq 'admin' || functionMap.btn_channel_update }">
            <a href="javascript:void(0)" class="easyui-linkbutton" iconCls="icon-ok"
               onclick="addORUpdateChannel()">确定</a>&nbsp;&nbsp;&nbsp;&nbsp;
        </c:if>
        <a href="javascript:void(0)" class="easyui-linkbutton" iconCls="icon-cancel"
           onclick="$('#dlg_m_channel').dialog('close')">关闭</a>
    </div>
</div>
<div id="tb_m_channel" style="padding:5px;height:auto">
    <table width="100%">
        <tr>
            <td align="left">
                <c:if test="${user.loginName eq 'admin' || functionMap.btn_channel_add }">
                    <a href="javascript:void(0)" class="easyui-linkbutton" iconCls="icon-add" plain="true"
                       onclick="addChannelDialog()">新增</a>
                </c:if>
                <c:if test="${user.loginName eq 'admin' || functionMap.btn_channel_update }">
                    <a href="javascript:void(0)" class="easyui-linkbutton" iconCls="icon-edit" plain="true"
                       onclick="updateChannelDialog()">修改</a>
                </c:if>
                <c:if test="${user.loginName eq 'admin' || functionMap.btn_channel_delete }">
                    <a href="javascript:void(0)" class="easyui-linkbutton" iconCls="icon-remove" plain="true"
                       onclick="deleteChannelDialog()">删除</a>
                </c:if>
            </td>
        </tr>
    </table>
</div>
<div id="win_user_config"/>
<script type="text/javascript" src="${basePath}js/view/config/channelList.js"></script>


</body>
</html>
