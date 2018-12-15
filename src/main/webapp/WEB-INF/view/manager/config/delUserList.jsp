<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%
String path = request.getContextPath();
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
  <head>
    <base href="<%=basePath%>">    
	<meta http-equiv="pragma" content="no-cache">
	<meta http-equiv="cache-control" content="no-cache">
	<meta http-equiv="expires" content="0">    
	<meta http-equiv="keywords" content="keyword1,keyword2,keyword3">
	<meta http-equiv="description" content="This is my page">
	<link rel="stylesheet" type="text/css" href="<%=basePath %>js/easyui/themes/default/easyui.css">
	<script type="text/javascript" src="<%=basePath %>js/jquery.min.js"></script>
	<script type="text/javascript" src="<%=basePath %>js/jquery.validate.min.js"></script>
	<script type="text/javascript" src="<%=basePath %>js/easyui/jquery.easyui.min.js"></script>
	<link rel="stylesheet" type="text/css" href="<%=basePath %>js/easyui/themes/icon.css">
	<script type="text/javascript" src="<%=basePath %>js/my97/WdatePicker.js"></script>
	<script type="text/javascript" src="<%=basePath %>js/index.js"></script>
	<link rel="stylesheet" type="text/css" href="<%=basePath %>images/style.css">
	<script type="text/javascript" src="<%=basePath %>js/easyui/local/easyui-lang-zh_CN.js"></script>
	<!--
	<link rel="stylesheet" type="text/css" href="styles.css">
	-->

  </head>
  
  <body >
	
	<table id="delUserTable"  class="easyui-datagrid" style="width:800px;height:250px"
            data-options="url:'<%=basePath%>manager/delUser/userList',rownumbers:'true',idField:'id',
         fit:true,onRowContextMenu: onContextMenu,fitColumns: true,selectOnCheck:false,checkOnSelect:false,singleSelect:true,pagination:true,pageSize:50,pageList:[50,100,200],
            toolbar:'#tb_m_delUser'">
	        <thead>
	            <tr>
	            	<th data-options="field:'id',checkbox:true"  align="left" ></th>
	                <th data-options="field:'loginName'"    width="130" align="left">用户名</th>
	                <th data-options="field:'userName'"  width="130" align="center" >真实姓名</th>
	                <th data-options="field:'deleteTimeString'" width="130" align="center">删除时间</th>
	                <c:if test="${user.loginName eq 'admin' || functionMap.btn_delUser_group }">
	                 <th data-options="field:'sex' ,formatter:formatRole" width="130" align="center">组织管理</th>
	                 </c:if>
	                 <c:if test="${user.loginName eq 'admin' || functionMap.btn_delUser_role }">
	                  <th data-options="field:'tel' ,formatter:formatRole" width="130" align="center">角色管理</th>
	                  </c:if>
	            </tr>
	        </thead>
    	</table>
		 
	  
	    <div id="tb_m_delUser" style="padding:5px;height:auto">
	    	<table width="100%">
				<tr>
					<td align="left">
					<c:if test="${user.loginName eq 'admin' || functionMap.btn_delUser_relDelete }">
		      		  	 <a href="javascript:void(0)" class="easyui-linkbutton" iconCls="icon-cancel" plain="true" onclick="deleteUserDialog()">物理删除</a>
		      		</c:if>
		      		<c:if test="${user.loginName eq 'admin' || functionMap.btn_delUser_back }">
		      		  	  <a href="javascript:void(0)" class="easyui-linkbutton" iconCls="icon-redo" plain="true" onclick="redoUser()">还原</a>
					</c:if>
					</td>
					<td align="right">
						<input class="easyui-searchbox"	data-options="searcher:searchData,prompt:'请输入内容',menu:'#mm_tb'" style="width:280px"></input>
						<div id="mm_tb" style="width:120px">
						<div name="all">所有</div>
							<div name="loginName">用户名</div>
							<div name="userName">真实姓名</div>
						</div>
					</td>
				</tr>
			</table>
		</div>
	    <div id="win_user_config"  />
	<script type="text/javascript">
	function deleteUserDialog() {
		var row;
			 row = $("#delUserTable").datagrid("getChecked");
			 if(row==null ||row.length==0){
				 $.messager.alert("出错", "请先选择要删除的用户", "error");
				 return;
			 }else{
				 var rowids="";
				 $.each(row,function(i,n){
					 rowids+=n.id+",";
				 });
				 row=rowids.substr(0,rowids.length-1);
			 }
		
		if (row != null) {
			$.messager.confirm("确认", "您是否确认要删除这些数据?",function(t){
				if(t){
					$.ajax({
						url : "<%=basePath%>manager/delUser/delUser",
						data :"id="+row, 
						type : "POST",
						beforeSend : function() {
							ajaxLoading("正在提交请稍候。。。");
						},
						success : function(data) {
							ajaxLoadEnd();
							switch (data.state) {
							case 0:
								$.messager.alert("失败", data.message, "error");
								break;
							case 1:
								$.messager.alert("成功", "操作成功");
								$("#delUserTable").datagrid("reload");
								$("#delUserTable").datagrid("clearChecked");
								break;
							default:
								break;
							}
						},
						error : function(jqXHR, textStatus, errorThrown) {
							ajaxLoadEnd();
							$.messager.alert("出错", "服务器出错啦！<br />错误类型：" + textStatus
									+ "<br />错误内容：" + errorThrown, "error");
							}
					});
				}
			});
		}else{
			$.messager.alert("错误","请选择要删除的数据！","error");
		}
		
	}
	function redoUser(flag) {
		var row;
			 row = $("#delUserTable").datagrid("getChecked");
			 if(row==null ||row.length==0){
				 $.messager.alert("出错", "请先选择要恢复的用户", "error");
				 return;
			 }else{
				 var rowids="";
				 $.each(row,function(i,n){
					 rowids+=n.id+",";
				 });
				 row=rowids.substr(0,rowids.length-1);
			 }
		
		if (row != null) {
			$.messager.confirm("确认", "您是否确认要恢复这些用户?",function(t){
				if(t){
					$.ajax({
						url : "<%=basePath%>manager/delUser/redoUser",
						data :"id="+row, 
						type : "POST",
						beforeSend : function() {
							ajaxLoading("正在提交请稍候。。。");
						},
						success : function(data) {
							ajaxLoadEnd();
							switch (data.state) {
							case 0:
								$.messager.alert("失败", data.message, "error");
								break;
							case 1:
								$.messager.alert("成功", "操作成功");
								$("#delUserTable").datagrid("reload");
								$("#delUserTable").datagrid("clearChecked");
								break;
							default:
								break;
							}
						},
						error : function(jqXHR, textStatus, errorThrown) {
							ajaxLoadEnd();
							$.messager.alert("出错", "服务器出错啦！<br />错误类型：" + textStatus
									+ "<br />错误内容：" + errorThrown, "error");
							}
					});
				}
			});
		}else{
			$.messager.alert("错误","请选择要恢复的用户！","error");
		}
	}
		
		function formatRole(value, row, index) {
			return "<a href='javaScript:void(0)'onclick='userRoleList(" + row.id
			+ ")'>管理</a>";
		}
		function userRoleList(value) {
			$("#win_user_config").window({
				title : "所属角色",
				width : 400,
				height : 400,
				draggable : true,
				resizable : true,
				collapsible : false,
				minimizable : false,
				maximizable : false,
				inline : true,
				onMove:onMoveWindow,
				href : '<%=basePath%>manager/delUser/role/detail?id=' + value
			});
		}
		
		function formatRole(value, row, index) {
			return "<a href='javaScript:void(0)'onclick='userRoleList(" + row.id
			+ ")'>管理</a>";
		}
		function userRoleList(value) {
			$("#win_user_config").window({
				title : "所属组织",
				width : 400,
				height : 400,
				draggable : true,
				resizable : true,
				collapsible : false,
				minimizable : false,
				maximizable : false,
				inline : true,
				onMove:onMoveWindow,
				href : '<%=basePath%>manager/delUser/role/detail?id=' + value
			});
		}

		function searchData(value, name) {
			if ($.trim(value) == '') {
				return;
			}
			$('#delUserTable').datagrid('load', {
				"type" : name,
				"value" : $.trim(value)
			});

		}
		
		function onContextMenu(e, rowIndex, row) {
			e.preventDefault();
			$(this).datagrid("selectRow",rowIndex);
			$("#menu_m_delUser").menu("show", {
				left : e.pageX,
				top : e.pageY
			});
			return false;
		}
	</script>
</body>
</html>
