/*															
 * FileName：logList.js						
 *			
 * Description：用于logList.jsp
 * 																
 * History：
 * 版本号 		作者 			日期       			简介
 *  1.0 	nijiaqi		2014-12-02		Create
 */
function dblClickRow(rowIndex, row){
	if (row != null) {
		$("#logForm").form("load",row);
	}
	$("#dlg_lab_log").dialog({
		closed : true,
		inline : true,
		onMove:onMoveDialog,
		modal : true
	});
	$("#dlg_lab_log").dialog("open").dialog("setTitle", "日志");
}
function searchData(value,name){
	var tChannel = $("#channelSelect").combotree("tree");	
	var nChannel = tChannel.tree("getSelected");		
	var tRole = $("#roleSelect").combotree("tree");
	var nRole = tRole.tree("getSelected");
	$("#logTable").datagrid("load", { 
		value: (value==null||value=="")?null:value,
		channelId:nChannel==null?null:nChannel.id,
		roleId:nRole==null?null:nRole.id,
		stime:$("#stime").val(),
		etime:$("#etime").val()
	}); 
	
}
function selectChannelChange(val){	
	var tRole = $("#roleSelect").combotree("tree");
	var nRole = tRole.tree("getSelected");
	var searchValue=$("#seachBox").searchbox("getValue");
	$("#logTable").datagrid("load",{
		value: (searchValue==null||searchValue=="")?null:searchValue,
		channelId:val==null?null:val,
		roleId:nRole==null?null:nRole.id,
		stime:$("#stime").val(),
		etime:$("#etime").val()		
	});
}
function selectRoleChange(val){
	var tChannel = $("#channelSelect").combotree("tree");	
	var nChannel = tChannel.tree("getSelected");
	var searchValue=$("#seachBox").searchbox("getValue");
	$("#logTable").datagrid("load",{
		value: (searchValue==null||searchValue=="")?null:searchValue,
		channelId:nChannel==null?null:nChannel.id,
		roleId:val==null?null:val,
		stime:$("#stime").val(),
		etime:$("#etime").val()		
	});
}