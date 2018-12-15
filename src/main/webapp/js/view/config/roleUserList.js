/*															
 * FileName：roleUserList.js
 *			
 * Description：用于roleUserList.jsp
 * 																
 * History：
 * 版本号 		作者 			日期       			简介
 *  1.0 	nijiaqi		2014-10-10		Create
 */
var IS_CHANGE=false;
$("#userForm").validate({
	onkeyup:false,
	onclick:false,
	rules: {
		loginName: {
			required:true,
			rangelength:[1,20],
			remote: {
			    url: BASE_HREF+"manager/roleUser/exist",     //后台处理程序
			    type: "post",               //数据发送方式
			    dataType: "json",           //接受数据格式   
			    data: { 
		    	//要传递的数据
			    	filed:"loginName",
			    	value: function() {
			            return $("#loginName").val();
			        },
			        id:function(){
			        	var id=$("#userId").val();
			        	if(id==null || id=="" || id=="-1" || id=="null"){
			        		return -1;
			        	}else{
			        		return id;
			        	}
			        	
			       	}
		    	}
		 	}
		},
		loginPassword: {
			required:true,
			rangelength:[6,20]
		},
		secondPassword: {
			required: true,
			equalTo: "#loginPassword"
		},
		userName: {
			required: true,
			rangelength:[1,20]
		},
		email:{
			email:true 
		},
		tel:{
			digits:true
		}
	},
	messages: {
		loginName: {
			required:"请输入用户名",
			rangelength:"用户名长度必须在{0}-{1}至之间",
			remote:"用户名已存在"
		},
		loginPassword: {
			required:"请输入密码",
			rangelength:"密码长度必须在{0}-{1}至之间"
		},
		secondPassword: {
			required: "请再次输入密码",
			equalTo: "二次输入不一致"
		},
		userName: {
			required: "请输入姓名",
			rangelength:"姓名长度必须在{0}-{1}至之间"
		},
		email:{
			email:"请输入正确的邮箱"
		},
		tel:{
			digits:"必须为正整数包括0"
		}
	}
});

function addOrUpdateUser() {
	if(!$("#userForm").valid()){
		return;
	}
	$.ajax({
		url : BASE_HREF+"manager/roleUser/update",
		data : $("form").serialize(),
		type : "POST",
		beforeSend : function() {
			$("#dlg_m_user").dialog("close");
			ajaxLoading("正在提交请稍候。。。");
		},
		success : function(data) {
			ajaxLoadEnd();
			switch (data.state) {
			case 0:
				$.messager.alert("失败", data.message, "error", function() {
					$("#dlg_m_user").dialog("open");
				});
				break;
			case 1:
				$.messager.alert("成功", "操作成功");
				$("#userTable").datagrid("reload");
				break;
			case 2:
				$.messager.alert("失败", "验证不通过", "error", function() {
					$("#dlg_m_user").dialog("open");
				});
			
				break;
			default:
				break;
			}
			$("#userTable").datagrid("clearChecked");
		}
	});
}

function dblClickRow(rowIndex, row){
	if (row != null) {
		$("form").form("load",row);
		$("#loginPassword").val("11111111");
	}else{
		$.messager.alert("出错", "请先选择要修改的用户", "error");
		return;
	}
	$("#passtd").hide();
	$("#passtd2").hide();
	openDlgUser("修改");
}
function updateUserDialog(flag){
	if(!hasAuthority()){
		$.messager.alert("出错", "没有权限！", "error");
		return;
	}
	var row;
	if(flag!=null){
		 row = $("#userTable").datagrid("getChecked");
		 if(row==null ||row.length==0){
			 $.messager.alert("出错", "请先选择要修改的用户", "error");
			 return;
		 }else if(row.length>1){
			 $.messager.alert("出错", "只允许选择一个用户进行修改", "error");
			 return; 
		 }else{
			 row=row[0];
		 }
	}else{
		 row = $("#userTable").datagrid("getSelected");
	}
	if (row != null) {
		$("form").form("load",row);
		$("#id").val($("#userId").val());
		$("#loginPassword").val("11111111");
	}else{
		$.messager.alert("出错", "请先选择要修改的用户", "error");
		return;
	}
	$("#passtd").hide();
	$("#passtd2").hide();
	openDlgUser("修改");
}

function userList(value) {
	if(!hasAuthority()){
		$.messager.alert("出错", "没有权限！", "error");
		return;
	}
	$("#win_user_config").window({
		 title:"用户列表",
		 width:450,
		 height:400,
		 draggable:true,
		 modal:true,
		 resizable:true,
		 collapsible:false,
		 minimizable:false,
		 maximizable:false,
		 inline:true,
		 onMove:onMoveWindow,
		 href:BASE_HREF+"manager/roleUser/user/list",
		 onClose:function(){
			 if(IS_CHANGE){
				 $("#userTable").datagrid("reload");
				 
			 }
		 }
	});
}

function openDlgUser(title) {
	$("#dlg_m_user").dialog({
		closed : true,
		inline : true,
		modal : true,
		onMove:onMoveDialog,
		buttons : "#dlg_buttons_m_user_add",
		onOpen: function(){
			$("#loginName").focus();
		}
	});
	$("#dlg_m_user").dialog("open").dialog("setTitle", title);
	$("#userForm").validate().resetForm();
	$("input").removeClass("error");
}

function deleteUserDialog(flag) {
	if(!hasAuthority()){
		$.messager.alert("出错", "没有权限！", "error");
		return;
	}
	var row;
	if(flag!=null){
		 row = $("#userTable").datagrid("getChecked");
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
	}else{
		 row = $("#userTable").datagrid("getSelected").id;
	}
	if (row != null) {
		$.messager.confirm("确认", "您是否确认要删除这些数据?",function(t){
			if(t){
				$.ajax({
					url : BASE_HREF+"manager/roleUser/user/del",
					data :"ids="+row, 
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
							$("#userTable").datagrid("reload");
							$("#userTable").datagrid("clearChecked");
							break;
						default:
							break;
						}
					}
				});
			}
		});
	}else{
		$.messager.alert("错误","请选择要删除的数据！","error");
	}
}
function resetPassWord(flag) {
	if(!hasAuthority()){
		$.messager.alert("出错", "没有权限！", "error");
		return;
	}
	var row;
	if(flag!=null){
		 row = $("#userTable").datagrid("getChecked");
		 if(row==null ||row.length==0){
			 $.messager.alert("出错", "请先选择要重置密码的用户", "error");
			 return;
		 }else{
			 var rowids="";
			 $.each(row,function(i,n){
				 rowids+=n.userId+",";
			 });
			 row=rowids.substr(0,rowids.length-1);
		 }
	}else{
		 row = $("#userTable").datagrid("getSelected").userId;
	}
	if (row != null) {
		$.messager.confirm("确认", "您是否确认要为这些用户重置密码?",function(t){
			if(t){
				$.ajax({
					url : BASE_HREF+"manager/roleUser/resetPass",
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
							$("#userTable").datagrid("clearChecked");
							break;
						default:
							break;
						}
					}
				});
			}
		});
	}else{
		$.messager.alert("错误","请选择要重置密码的用户！","error");
	}
}
function contextMenu(e, rowIndex, row) {
	e.preventDefault();
	$(this).datagrid("unselectAll");
	$(this).datagrid("selectRow",rowIndex);
	$("#menu_mUser_user").menu("show", {
		left : e.pageX,
		top : e.pageY
	});
	e.stopPropagation();
}
function divContextMenu() {
	$(".datagrid-view2").bind("contextmenu", function(e) {
		e.preventDefault();
		$("#userTable").datagrid("unselectAll");
		$("#menu_mUser_user").menu("show", {
			left : e.pageX,
			top : e.pageY
		});
	});
}
var parentIds="";
var roleId="";
function treeClick(node){
	parentIds="";
	roleId=node.id;
	$("#roleId").val(roleId);
	while((node=$("#roleTree").tree("getParent",node.target))!=null){
		parentIds+=node.id+",";
	}
	if(parentIds.length>0){
		$("#userTable").datagrid("reload",{roleId:roleId,parentIds:parentIds.substr(0 ,parentIds.length -1)});
	}else{
		$("#userTable").datagrid("reload",{roleId:roleId});
	}
}
function searchData(value,name){
	
	if(parentIds.length>0){
		$("#userTable").datagrid("load",{roleId:roleId,parentIds:parentIds.substr(0 ,parentIds.length -1),"type": name, "value": $.trim(value)});
	}else{
		$("#userTable").datagrid("load",{roleId:roleId,"type": name, "value": $.trim(value)});
	}
}
/**
 * 格式化等级字段
 * @param value
 * @param row
 * @param index
 * @returns {String}
 * @version
 *	2014-12-9		chenchen	create
 */
function formatAdmin(value, row, index) {
	switch(value){
		case 1:
			return "普通用户";
		case 2:
			return "组用户";
		case 3:
			return "单室管理员";
		case 4:
			return "超级管理员";
		default:
			return "其他用户";
	}
}
/**
 * 设置用户等级
 * @param level
 * @version
 *	2014-12-10		chenchen	create
 */
function setLevel(level){
	if(!hasAuthority()){
		$.messager.alert("出错", "没有权限！", "error");
		return;
	}
	var rows = $("#userTable").datagrid("getChecked");
	if(rows==null || rows.length<=0){
		$.messager.alert("失败","请先选择用户！！！", "error");
		return;
	}
	var ids="";
	$.each(rows,function(i,n){
		if(i==rows.length-1){
			ids+=n.id;	
		}else{
			ids+=n.id+",";
		}
	});
	$.ajax({
		url : BASE_HREF+"manager/roleUser/user/setAdminLevel",
		data : "ids="+ids+"&level="+level,
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
				$.messager.alert("成功", "操作成功","info",function(){
					$("#userTable").datagrid("reload");
				});
				break;
			case 2:
				$.messager.alert("失败", data.message, "error");
				break;
			default:
				break;
			}
		}
	});
} 

function selectRoleChange(val){
	if(hasAuthority()){
		$("#userManagerArea").show();
	}else{
		$("#userManagerArea").hide();
	}
	$("#userTable").datagrid("clearChecked");
	$("#userTable").datagrid("load",{"roleId":val});
}

function formatRole(value, row, index) {
	return "<a href='javaScript:void(0)' onclick='userRoleList(" + row.userId+ ")'>管理</a>|<a href='javaScript:void(0)' onclick='addUserRole(" + row.userId+ ")'>添加</a>";
}

function formatAuth(value, row, index) {
	return "<a href='javaScript:void(0)'onclick='authList(" + row.userId + ")'>查看</a>";
}

function authList(value) {
	$("#win_user_config").window({
		title : "权限列表",
		width : 600,
		height : 400,
		draggable : true,
		resizable : true,
		modal:true,
		collapsible : false,
		minimizable : false,
		maximizable : false,
		inline : true,
		onMove:onMoveWindow,
		href : BASE_HREF+"manager/roleUser/authority?id=" + value
	});
}

function userRoleList(value) {
	$("#win_user_config").window({
		title : "所属角色",
		width : 400,
		height : 400,
		draggable : true,
		resizable : true,
		modal:true,
		collapsible : false,
		minimizable : false,
		maximizable : false,
		inline : true,
		onMove:onMoveWindow,
		href : BASE_HREF+"manager/roleUser/role/detail?id=" + value
	});
}

function addUserRole(value){
	$("#win_user_config").window({
		title : "添加角色",
		width : 400,
		height : 400,
		draggable : true,
		resizable : true,
		modal:true,
		collapsible : false,
		minimizable : false,
		maximizable : false,
		inline : true,
		onMove:onMoveWindow,
		href : BASE_HREF+"manager/roleUser/role?id=" + value
	});
}