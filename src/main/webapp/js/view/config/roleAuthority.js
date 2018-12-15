/*															
 * FileName：roleAuthority.js
 *			
 * Description：用于roleAuthority.jsp
 * 																
 * History：
 * 版本号 		作者 			日期       			简介
 *  1.0 	nijiaqi		2014-10-13		Create
 */
function functionLoadSuccess(data){
	$("#functionCofigTable").datagrid("clearChecked");
	$("input[type=checkbox]").click(function(event){
		   event.stopPropagation();
	});
}
	
function submitChannelAuth(){
	var jsonString="[";
	$("#channelDiv").find("input[type=hidden]").each(function(i,n){
		if(n.value!=null && n.value!=""){
		  	var id=$("#authorityTable").treegrid('find',n.name).ID;
			jsonString+="{\"id\":"+id+",\"channelId\":"+n.name+",\"isAuth\":"+n.value+",\""+TYPE_ID+"\":"+ID+"},";  
		}
	});
  	if(jsonString.length>2){
	  jsonString=jsonString.substr(0,jsonString.length-1);
	  jsonString+="]";
	  $.ajax({
			url : BASE_HREF+"manager/"+AUTH+"/authority/add",
			data : jsonString,            
			dataType:"json",      
            contentType:"application/json",
			type : "POST",
			beforeSend : function() {
				$("#win_user_config").window("close");
				ajaxLoading("正在提交请稍候。。。");
			},
			success : function(data) {
				ajaxLoadEnd();
				switch (data.state) {
				case 0:
					$.messager.alert("失败", data.message, "error",function(){
						$("#win_user_config").window("open");
					});
					break;
				case 1:
					$.messager.alert("成功", "操作成功","success",function(){
						$("#authorityTable").treegrid("reload");
						$("#win_user_config").window("open");
					});
					break;
				case 2:
					$.messager.alert("失败", data.message, "error",function(){
						$("#win_user_config").window("open");
					});
					break;
				default:
					break;
				}
			}
		});
  	}
} 
function submitFunctionAuth(){
	var channel=$("#authorityTable").datagrid("getSelected");
	if(channel==null || $("#functionDiv").find("input[type=hidden][name!=fun_id]").length==0 ){
		return;
	}
	var jsonString="[";
	$("#functionDiv").find("input[type=hidden][name!=fun_id]").each(function(i,n){
		if(n.value!=null && n.value!=""){
			var id=$(this).next("input").val();
			if(!id){
			id=null;
		}
		jsonString+="{\"id\":"+id+",\"channelId\":"+channel.CHANNELID+",\"htmlId\":\""+n.name+"\",\"isAuth\":"+n.value+",\""+TYPE_ID+"\":"+ID+"},";  
	  }
	});
  	if(jsonString.length>2){
  		jsonString=jsonString.substr(0,jsonString.length-1);
  		jsonString+="]";
  		$.ajax({
  			url : BASE_HREF+"manager/"+AUTH+"/authority/addFunction",
			data : jsonString,            
			dataType:"json",      
            contentType:"application/json",
			type : "POST",
			beforeSend : function() {
				$("#win_user_config").window("close");
				ajaxLoading("正在提交请稍候。。。");
			},
			success : function(data) {
				ajaxLoadEnd();
				switch (data.state) {
				case 0:
					$.messager.alert("失败", data.message, "error",function(){
						$("#win_user_config").window("open");
					});
					break;
				case 1:
					$.messager.alert("成功", "操作成功","success",function(){
						$("#functionCofigTable").datagrid("reload");
						$("#win_user_config").window("open");
					});
					break;
				case 2:
					$.messager.alert("失败", data.message, "error",function(){
						$("#win_user_config").window("open");
					});
					break;
				default:
					break;
				}
			}
		});
  	}  	  
} 
function formatChannelAuthor(value, row, index){
	var disable="";
	if(LOGIN_NAME!="admin"){
  	  	if(row.MYAUTHORITY==0) disable="disabled";
	}
	if(value==null || value==0){
		return "<input type='checkbox' "+disable+" id='chk_"+row.CHANNELID+"' name='"+row.CHANNELID+"' onchange='changeChannelCheck(this)' />"
		+"<input id='val_cha_"+row.CHANNELID+"'  name='"+row.CHANNELID+"' type='hidden' >";
	}else{
		return "<input type='checkbox' "+disable+" checked='checked' id='chk_"+row.CHANNELID+"' name='"+row.CHANNELID+"' onchange='changeChannelCheck(this)' />"
		+"<input id='val_cha_"+row.CHANNELID+"' name='"+row.CHANNELID+"' type='hidden' >";
	}
}
function formatFunctionAuthor(value, row, index){
	var disable="";
	if(LOGIN_NAME!="admin"){
  	  	if(row.MYAUTHORITY==0) disable="disabled";
	}
	if(value==null || value==0){
		return "<input type='checkbox' "+disable+" id='chk_fun_"+row.HTMLID+"' name='"+row.HTMLID+"' onchange='changeFunctionCheck(this)' />"
		+"<input id='val_fun_"+row.HTMLID+"' name='"+row.HTMLID+"' type='hidden' >"
		+"<input name='fun_id' type='hidden' value='"+row.ID+"' >";
	}else{
		return "<input type='checkbox'checked='checked' "+disable+" id='chk_fun_"+row.HTMLID+"' name='"+row.HTMLID+"' onchange='changeFunctionCheck(this)' />"
		+"<input id='val_fun_"+row.HTMLID+"' name='"+row.HTMLID+"' type='hidden' >"
		+"<input  name='fun_id'' type='hidden' value='"+row.ID+"'>";
	}
}
function changeChannelCheck(obj){
	if($(obj).prop("checked")){
		$("#val_cha_"+$(obj).attr("name")).val(1);
	}else{
		$("#val_cha_"+$(obj).attr("name")).val(0);
	}
}
function changeFunctionCheck(obj){
	if($(obj).prop("checked")){
		$("#val_fun_"+$(obj).attr("name")).val(1);
	}else{
		$("#val_fun_"+$(obj).attr("name")).val(0);
	}	
}
function clickChannelRow(row){	
		$("#functionCofigTable").datagrid("load",{
			channelId:row.CHANNELID
		});
}
function clickFunctionRow(index,row){	
		var checked=$("#chk_fun_"+row.HTMLID);
		changeFunctionCheck(checked);
}
function expendChannelNodeAll(){
	$("#authorityTable").treegrid("expandAll");
}