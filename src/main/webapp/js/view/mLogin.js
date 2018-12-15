/*															
 * FileName：mLogin.js						
 *			
 * Description：用于mLogin.jsp
 * 																
 * History：
 * 版本号 		作者 			日期       			简介
 *  1.0 	chenchen	2014-09-17		Create
 */
$(document).ready(function(){
	if($("#error").val()!=""){
		alert($("#error").val());
	}
    //设置页面初始化焦点在用户名输入框
	$("input[name=loginName]").focus();
    //设置登录验证
	$("#loginForm").validate({
		rules: {
			loginName: "required",
			password: "required"
		},
		messages:{
			loginName: "请输入用户名",
			password: "请输入密码"
		}
	});
	//设置注册验证
	$("#userForm").validate({
		rules: {
			loginName: {
				required:true,
				rangelength:[1,20],
				remote: {
				    url: BASE_HREF+"register/check",     //后台处理程序
				    type: "post",               //数据发送方式
				    dataType: "json",           //接受数据格式   
				    data: { 
			    	//要传递的数据
				    	filed:"loginName",
				    	value: function() {
				            return $("#reLoginName").val();
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
				equalTo: "#reLoginPassword"
			},
			userName: {
				required: true,
				rangelength:[1,20]
			},
			email:{
				required: true,
				email:true,
                rangelength:[1,40]
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
				required: "请输入邮箱",
				email:"请输入正确的邮箱",
                rangelength:"邮箱长度必须在{0}-{1}至之间"
			}
		}
	});
	
	$(document).bind("keypress",function(obj){
		if(obj.keyCode==13){
			if(obj.target.name=="loginName"){
				$("input[name=password]").focus();
			}else if(obj.target.name=="password"){
				$("#vcode").focus();
			}else{
				$("#loginForm").submit();
			}
		}
	});


});
function loginSub() {
	if($("#loginForm").valid()){
		// if($("#vcode").val()=="" || $("#vcode").val().length<4){
		// 	alert("请输入4位验证码！");
		// 	$("#vcode").focus();
		// 	return false;
		// }
		$.jCryption.getKeys(BASE_HREF+"mLogin/loginRAS",function(receivedKeys) {  
			$.jCryption.encrypt($("#password").val(), receivedKeys, function(encryptedPasswd) { 
	 			$.ajax({
					url : BASE_HREF+"mLogin/login",
					data : {
						loginName:$("#loginName").val(),
						vcode:$("#vcode").val(),
						password:encryptedPasswd
					},
					type : "POST",
					beforeSend : function() {
						ajaxLoading("正在登录请稍候。。。");
					},
					success : function(data) {
						ajaxLoadEnd();
						switch (data.state) {
						case 1:
							window.location.href=BASE_HREF+"manager";
							break;
						case 2:
							refreshVCode();
							alert(data.message);
							break;
						default:
							break;
						}
					}
				});
	 		}); 
	   	}); 
	}
	return false;
} 




function openDlgUser(title){
	$("input").val(null);
	$("#dlg_m_user").dialog({
		closed : true,
		inline : true,
		modal : true,
		onMove:onMoveDialog,
		buttons : "#dlg_buttons_m_user_add",
		onOpen: function(){
			$("#reLoginName").focus();
		}
	});
	$("#dlg_m_user").dialog("open").dialog("setTitle", title);
	$("#userForm").validate().resetForm();
	$("input").removeClass("error");
}


function addORUpdateUser() {
	if(!$("#userForm").valid()){
		return;
	}

	$.ajax({
		url : BASE_HREF+"/register",
		data : $("#userForm").serialize(),
		type : "POST",
		beforeSend : function() {
			$("#dlg_m_user").dialog("close");
			ajaxLoading("正在提交请稍候。。。");
		},
		success : function(data) {
			ajaxLoadEnd();
			switch (data.state) {
			case 0:
				alert(data.message);
				$("#dlg_m_user").dialog("open");
				break;
			case 1:		
				alert("操作成功");	
				break;
			case 2:
				alert("验证不通过");
				$("#dlg_m_user").dialog("open");
				break;
			default:
				break;
			}
			
		}
	});
}
function refreshVCode(){
		var obj=document.getElementById("vCodeImage");
	   obj.src = "vcode.jsp?"+Math.random();

}