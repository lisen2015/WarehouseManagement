<%@ page contentType="text/html;charset=UTF-8" language="java" pageEncoding="utf-8" %>
<jsp:include page="./include/tag.jsp" />
<title>WarehouseManagement-管理系统</title>
<jsp:include page="./include/head.jsp"/>
<link rel="stylesheet" type="text/css" href="${basePath}css/login_system.css">
<script type="text/javascript" src="${basePath}js/view/mLogin.js"></script>
</head>
<body >
	<form id="loginForm" method="post" onsubmit="return loginSub();" >
		<input id="error" type="hidden" value="${error}">
		<div class="hearder"></div>
		<div class="con">
			<div class="part">
				<div class="part1">
					<div class="map" id="echartMap"></div>
					<div class="login">
						<h1></h1>
						<div class="login_con">
							<div class="name">
								<input type="text" id="loginName"  name="loginName" class="inputLogin" placeholder="请输入用户名" />
							</div>
							<div style="height:10px"></div>
							<div class="password">
								<input type="password"  id="password" name="password" class="inputLogin" placeholder="请输入密码" autocomplete="off"/>
							</div>
							<div class="yzm">
								<input id="vcode" name="vcode" class="yzdd" type="text"  />
								<img id="vCodeImage" src="vcode.jsp" width="99" height="31" border="0" style="cursor: pointer;" title="看不清楚？点击更换" onclick="refreshVCode(this);" />
							</div>
							<a id="loginButton" class="dl_button" onclick="loginSub()" href="javascript:void(0)" id="btnSub">登 录</a>
							<a class="dl_button" onclick="selectRole()" href="javascript:void(0)" id="btnSelectRole" style="display: none">确定</a>
							<%--<a id="regUser" class="lan" 	onclick="openDlgUser('申请注册')" href="javascript:void(0)">申请注册</a>--%>
						</div>
						<div class="login_foot"></div>
					</div>
					<div class="clear"></div>
				</div>
				<p></p>
			</div>
		</div>
	</form>
	<div id="dlg_m_user" class="easyui-dialog" data-options="closed: true,buttons : '#dlg_buttons_m_user_add'" style="width:450px;height:300px;padding:10px 10px; " >	
		 	<div  id="errorMessage" style="color: red"></div>
			<form  id="userForm" method="post"  >
				<table width="390">
					<tr>
						<td width="70" class="font_td">用户名　:</td>
						<td colspan="3" width="320" ><input type="text" id="reLoginName"  name="loginName" style="width:315px"   ></td>
					</tr>
					<tr >
						<td class="font_td" >密　　码:</td>
						<td colspan="3" width="320" ><input type="password" id="reLoginPassword"  name="loginPassword"  style="width:315px"  ></td>
					</tr>
					<tr >
						<td class="font_td" >确认密码:</td>
						<td colspan="3" width="320" ><input type="password" id="reSecondPassword" name="secondPassword"  style="width:315px"  ></td>
					</tr>
					<tr>
						<td width="70" class="font_td" >姓　　名:</td>
						<td colspan="3" width="320" ><input id="reUserName" name="userName" type="text" style="width:315px" ></td>
					</tr>
					<tr>
						<td width="70" class="font_td" >邮　　箱:</td>
						<td colspan="3" width="320" ><input id="reEmail" name="email" type="text" style="width:315px" ></td>
					</tr>
				</table>
		    </form> 
	    </div>
		<div id="dlg_buttons_m_user_add"  style="text-align: center">
			<a href="javascript:void(0)" class="easyui-linkbutton" iconCls="icon-ok"  onclick="addORUpdateUser();" >确定</a>&nbsp;&nbsp;&nbsp;&nbsp;
			<a href="javascript:void(0)" class="easyui-linkbutton" iconCls="icon-cancel" onclick="$('#dlg_m_user').dialog('close')">关闭</a>
		</div>
			
		</center>
		<div class="clear"></div>
	</div>
	<div id="win_loading"   title="My Window" style="width:300px;height:100px; display: none"
        data-options="modal:true,title:'',collapsible:false,minimizable:false,maximizable:false,closable:false,resizable:false" align="center">
   		<div style="width: 100%;text-align: center"></div>
	</div>
</body>
</html>
