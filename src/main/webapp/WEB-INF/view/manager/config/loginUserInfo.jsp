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
  <br /> <br /><br /> <br /><br /> <br />
	<form  id="userForm" method="post"  >
		<input type="hidden" id="id" name="id" value="${user.id }">
		<input id="loginPassword" name="loginPassword" type="hidden"  value="111111">
		<table width="390" align="center" >
			<tr >
				<td width="70" class="font_td">用户名　:</td>
				<td colspan="3" width="320">
					<input type="text" id="loginName"  name="loginName"  style="width:315px" value="${user.loginName }" class="easyui-validatebox" <c:if test="${user.loginName eq 'admin'}">readonly="readonly"</c:if>
                                                   data-options="required:true,delay:1000,validType:{length:[0,20],remoteByException:['${basePath}manager/loginUser/exist','loginName',{id:'#id'}]}">
                </td>
			</tr>
			<tr >
				<td width="70"  class="font_td">姓　　名:</td>
				<td  width="320"  colspan="3">
                    <input id="userName" name="userName" type="text"  style="width:315px" value="${user.userName }" class="easyui-validatebox"
                                                     data-options="required:true,validType:{length:[0,20]}">
                </td>
				
			</tr>
			<tr >
				<td width="70"  class="font_td">邮　　箱:</td>
				<td  width="320"  colspan="3">
                    <input id="email" name="email" type="text"  style="width:315px" value="${user.email }" class="easyui-validatebox"
                                                     data-options="required:false,validType:{length:[0,50],email:true}">
                </td>
				
			</tr>
		</table>
		
		<div id="dlg_buttons_m_user_add"  style="text-align: center;padding-top: 10px">
			<a href="javascript:void(0)" class="easyui-linkbutton" iconCls="icon-ok"  onclick="addORUpdateUser();" >修改</a>&nbsp;&nbsp;&nbsp;&nbsp;
			 <a href="javascript:void(0)" class="easyui-linkbutton" iconCls="icon-cancel" onclick="resetUserForm();">重置</a>
		</div>
	</form>  
	
	<div id="win_user_config"  />   
	<script type="text/javascript" charset="utf-8" src="<%=basePath %>js/view/config/loginUserInfo.js"></script>
  </body>
</html>
