/**
 * 编辑回复列表页
 * @Author lina
 * @Date 2016-09-08
 **/
var html_decode = function(str){
    if (str.length == 0) return "";
    str = str.replace(/>/g, "&gt;");
    str = str.replace(/</g, "&lt;");
    return str;
}


function editorApplyManagerUserQuestionFormator(value, rowData, rowIndex){
    if(rowData.feedbackState == 3){
        return '<div class="wraptitle"><font style="color: red">[已驳回]</font>' + html_decode(value)+"</div>";
    }else{
        return '<div class="wraptitle">'+html_decode(value)+'</div>';
    }
}

function showEditorApply(rowIndex,rowData){
    CHECK_DATALOCK_FLAG=true;
    loadCenterPanelNoLock('编辑回复',BASE_HREF+"manager/editorApply?id="+rowData.id);
}

//待回复等 radio的绑定事件
$('input:radio[name="editorApplyManager_feedbackState"]').change(function(){
    searchEditorFeedback();
});

//匿名还是实名 radio的绑定事件
$('input:radio[name="editorApplyManager_comeFrom"]').change(function(){
    searchEditorFeedback();
});

//是否开发 radio的绑定事件
$('input:radio[name="editorApplyManager_developState"]').change(function(){
    searchEditorFeedback();
});

//搜索方法
function searchEditorFeedback(){
    var startDate = $('input[name="editorApplyManager_date_from"]').val();
    var endDate =  $('input[name="editorApplyManager_date_to"]').val();
    if(startDate !="" && endDate==""){
        $.messager.alert('提示：', '结束日期不能为空！', 'warning');
        return;
    }
    if(startDate =="" && endDate!=""){
        $.messager.alert('提示：', '开始日期不能为空！', 'warning');
        return;
    }
    if(startDate !="" && endDate!="" && endDate<startDate){
        $.messager.alert('提示：', '结束日期应大于开始日期！', 'warning');
        return;
    }
    $('#editorApplyManager').datagrid('load', {
        feedbackState : $('input[name="editorApplyManager_feedbackState"]:checked').val(),
        developState : $('input[name="editorApplyManager_developState"]:checked').val(),
        comeFrom : $('input[name="editorApplyManager_comeFrom"]:checked').val(),
        startDate : startDate,
        endDate : endDate,
    });
}