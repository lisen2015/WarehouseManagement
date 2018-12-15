<%@ page contentType="text/html;charset=UTF-8" language="java" pageEncoding="utf-8" %>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<jsp:include page="./include/tag.jsp"/>
<title>WarehouseManagement-管理系统</title>
<jsp:include page="./include/head.jsp"/>
</head>
<body class="easyui-layout" style="text-align:left">
<div data-options="region:'north',border:false,cache:true"
     style="text-align:center;background: #2767af  url(${basePath}images/bg.png) center right no-repeat">
    <table cellpadding="0" cellspacing="0" style="width:100%;">
        <tr>
            <td id="titlelogo"></td>
            <td style="width: 20%;padding-right:5px;text-align:right;vertical-align:bottom;padding-bottom:10px;color:#fff;font-size:14px;">
                <div id="topmenu">
                    <span>欢迎您,${user.loginName }</span>
                    <a href="${basePath}mLogin/loginOut">退出</a>
                </div>
            </td>
        </tr>
    </table>
</div>
<div data-options="region:'west',border:false,split:true,title:'菜单',href:'${basePath}manager/index/left',cache:true"
     style="width:250px;padding:5px;">
</div>

<div id="index_center"
     data-options="region:'center',border:false,title:'首页',href:'${basePath}manager/index/welcome',cache:true">
</div>


<div id="win_loading" title="My Window" style="width:300px;height:100px"
     data-options="modal:true,title:'',collapsible:false,minimizable:false,maximizable:false,closable:false,resizable:false"
     align="center">
    <div style="width: 100%;text-align: center"></div>
</div>

</body>
</html>
