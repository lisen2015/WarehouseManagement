<%@ page language="java" pageEncoding="UTF-8"%>
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
	<script type="text/javascript">
	var BASE_HREF="<%=basePath %>";
	</script>
  </head>
  
  <body >
	<table id="userTable"  class="easyui-datagrid" style="width:800px;height:250px"
		data-options="url:'<%=basePath%>manager/roleUser/userList',rownumbers:'true',idField:'id',pagination:true,pageSize:20,pageList:[20,50,100,200],
		toolbar:'#tb_roleUser_buttonArea',fit:true,fitColumns: true,onDblClickRow:dblClickRow,onRowContextMenu: contextMenu,onLoadSuccess:divContextMenu">
		<thead>
		    <tr>
				<th data-options="field:'id'"  align="center" checkbox=true>ID</th>
				<th data-options="field:'loginName'" width="120" align="center" >用户名</th>
				<th data-options="field:'userName'" width="120" align="center">真实姓名</th>
				<th data-options="field:'tel'" width="120" align="center">电话号码</th>
				<th data-options="field:'adminLevel',formatter:formatAdmin" width="120" align="center">等级</th>
				<c:if test="${user.loginName eq 'admin' || functionMap.btn_userRoleManager }">
					<th data-options="field:'aaa',formatter:formatRole" width="120" align="center">角色管理</th>
				</c:if>
				<c:if test="${user.loginName eq 'admin' || functionMap.btn_authority_query }">
					<th data-options="field:'bbb',formatter:formatAuth" width="120" align="center">权限查看</th>
				</c:if>
			</tr>
		</thead>
	</table>
    <div id="tb_roleUser_buttonArea" style="padding:5px;height:auto">
    	<table width="100%">
			<tr>
				<td align="left">
					<div id="userManagerArea">
						<c:if test="${user.loginName eq 'admin' || functionMap.btn_userManager_add }">
							<a href="javascript:void(0)" class="easyui-linkbutton" iconCls="icon-add" plain="true" onclick="userList()">新增</a>
						</c:if>
						<c:if test="${user.loginName eq 'admin' || functionMap.btn_userManager_update }">
		        		 	<a href="javascript:void(0)" class="easyui-linkbutton" iconCls="icon-edit" plain="true" onclick="updateUserDialog(true)">修改</a>
		        		</c:if>
		        		<c:if test="${user.loginName eq 'admin' || functionMap.btn_userManager_del }">
		      		  	 	<a href="javascript:void(0)" class="easyui-linkbutton" iconCls="icon-remove" plain="true" onclick="deleteUserDialog(true)">删除</a>
		      		  	</c:if>
		      		  	<c:if test="${user.loginName eq 'admin' || functionMap.btn_userManger_reset }">
		      		  	 	<a href="javascript:void(0)" class="easyui-linkbutton" iconCls="icon-redo" plain="true" onclick="resetPassWord(true)">重置密码</a>
		      		  	</c:if>
		      		  	<c:if test="${user.loginName eq 'admin' || functionMap.btn_userManger_setLevel }">
			      		  	<a href="javaScript:void(0)" class="easyui-menubutton"
								data-options="plain:true,iconCls:'icon-undo',menu:'#userLevel'" style="padding-left: 5px">等级</a>
							<div id="userLevel" style="width:100px;">
								<div onclick="setLevel(1)">普通用户</div>
							    <div onclick="setLevel(2)">组用户</div>
							    <div onclick="setLevel(3)">单室管理员</div>
							    <div onclick="setLevel(4)">超级管理员</div>
							</div>
					  	</c:if>
					</div>
				</td>
				<td align="right">
					<c:if test="${ userSelectRole.adminLevel==4 }">
						<input id="roleSelect" name="roleSelect" class="easyui-combotree" value="${userSelectRole.roleId }"  data-options="url:'<%=basePath %>mLogin/roleUserTree',
						method:'post',editable:false,onChange:selectRoleChange,panelHeight:'auto',panelMaxHeight:200" style="width:150px;"  >
					</c:if>
					<input class="easyui-searchbox"	data-options="searcher:searchData,prompt:'请输入内容'" style="width:280px"></input>
				</td>
			</tr>
		</table>
	</div>
	<div id="dlg_m_user" style="width:450px;height:250px;padding:10px 10px;" >	
	 	<div class="ftitle" id="errorMessage" style="color: red"></div>
		<form  id="userForm" method="post"  >
			<input type="hidden" id="id" name="id">
			<input type="hidden" id="userId" name="userId">
			<input type="hidden" id="roleId" name="roleId">
			<table width="390">
				<tr>
					<td width="70" class="font_td">用户名　:</td>
					<td colspan="3" width="320"><input type="text" id="loginName"  name="loginName"  style="width:315px"  ></td>
				</tr>
				<tr id="passtd">
					<td class="font_td">密　　码:</td>
					<td colspan="3" width="320"><input type="password" id="loginPassword"  name="loginPassword"   style="width:315px" ></td>
				</tr>
				<tr id="passtd2">
					<td class="font_td">确认密码:</td>
					<td colspan="3" width="320"><input type="password" id="secondPassword" name="secondPassword"   style="width:315px" ></td>
				</tr>
				<tr>
					<td width="70"  class="font_td">姓　　名:</td>
					<td colspan="3" width="320"><input id="userName" name="userName" type="text"  style="width:315px"></td>
				</tr>
				<tr>
					<td width="70"  class="font_td">邮　　箱:</td>
					<td colspan="3" width="320"><input id="email" name="email" type="text"  style="width:315px"></td>
				</tr>
				<tr>
					<td width="70"  class="font_td">电话号码:</td>
					<td colspan="3" width="320"><input id="tel" name="tel" type="text"  style="width:315px"></td>
				</tr>
			</table>
	    </form>        
    </div>
    <div id="dlg_buttons_m_user_add"  style="text-align: center">
    	<c:if test="${user.loginName eq 'admin' || functionMap.btn_userManager_update }">
    		<a href="javascript:void(0)" class="easyui-linkbutton" iconCls="icon-ok"  onclick="addOrUpdateUser();" >确定</a>&nbsp;&nbsp;&nbsp;&nbsp;
    	</c:if>
        <a href="javascript:void(0)" class="easyui-linkbutton" iconCls="icon-cancel" onclick="$('#dlg_m_user').dialog('close')">关闭</a>
	</div>
		
 	<div id="win_user_config"  ></div>
 	<script type="text/javascript">
		USER_ID=${user.id};
    	ORG_ID=${userSelectRole.roleId};
    	ORG_LEVEL=${userSelectRole.adminLevel};
   	</script>
	<script type="text/javascript" charset="utf-8" src="<%=basePath %>js/view/config/roleUserList.js"></script>
</body>
</html>
