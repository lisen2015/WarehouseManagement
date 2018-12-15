<%@ page contentType="text/html;charset=UTF-8" language="java" pageEncoding="utf-8" %>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<jsp:include page="../../include/tag.jsp"/>
<title>WarehouseManagement-管理系统</title>
<jsp:include page="../../include/head.jsp"/>
</head>
<body>
<div class="easyui-layout" style="height:auto" data-options="fit:true">
    <div id="channelDiv" data-options="region:'west',border:false" style="width:350px;">
        <table id="authorityTable" class="easyui-treegrid"
               data-options="url:'${basePath}manager/${authority}/authority/list?${typeId}=${id}',
					singleSelect:true,rownumbers:'true',idField:'CHANNELID',toolbar:'#tb_m_authority',fitColumns: true,onClickRow:clickChannelRow,
		            treeField:'CHANNELNAME',fit:true,onLoadSuccess:expendChannelNodeAll">
            <thead>
            <th data-options="field:'CHANNELNAME'" width="50" align="left">栏目名称</th>
            <th data-options="field:'ISAUTHORITY',formatter:formatChannelAuthor" width="20" align="center">访问</th>
            </thead>
        </table>
        <div id="tb_m_authority" style="padding:5px;height:auto">
            <div><a href="javaScript:void(0)" onclick="submitChannelAuth();" class="easyui-linkbutton"
                    data-options="plain:true,iconCls:'icon-save'">　保存　</a>
            </div>
        </div>
    </div>
    <div id="functionDiv" data-options="region:'center',border:false">
        <table id="functionCofigTable" class="easyui-datagrid"
               data-options="url:'${basePath}manager/${authority}/authority/functionList?${typeId}=${id}',
				idField:'HTMLID',singleSelect:true,selectOnCheck:false,checkOnSelect:false,scrollbarSize:0,fit:true,onClickRow:clickFunctionRow,fitColumns: true,toolbar:'#tb_m_function',
				onLoadSuccess:functionLoadSuccess">
            <thead>
            <tr>
                <th data-options="field:'FUNCTIONNAME'" width="40" align="left">名称</th>
                <th data-options="field:'ISAUTH',formatter:formatFunctionAuthor" width="20" align="center">操作</th>
            </tr>
            </thead>
        </table>
        <div id="tb_m_function" style="padding:5px;height:auto">
            <div>
                <a href="javaScript:void(0)" onclick="submitFunctionAuth();" class="easyui-linkbutton"
                   data-options="plain:true,iconCls:'icon-save'">　保存　</a>
            </div>
        </div>
    </div>
</div>
<script type="text/javascript">
    var AUTH = "${authority }";
    var ID = "${id }";
    var TYPE_ID = "${typeId }";
    var LOGIN_NAME = "${user.loginName}";
</script>
<script type="text/javascript" charset="utf-8" src="${basePath}js/view/config/roleAuthority.js"></script>
</body>
</html>
