/**
 * 热线回复列表页
 * @Author lina
 * @Date 2016-09-08
 **/
var html_decode = function(str){
    if (str.length == 0) return "";
    str = str.replace(/>/g, "&gt;");
    str = str.replace(/</g, "&lt;");
    return str;
}

function hotlineApplyManagerUserQuestionFormator(value, rowData, rowIndex){
    if(rowData.feedbackState == 3){
        return '<div class="wraptitle"><font style="color: red">[已驳回]</font>' + html_decode(value)+"</div>";
    }else{
        return '<div class="wraptitle">'+html_decode(value)+'</div>';
    }
}

function showHotlineApply(rowIndex,rowData){
    CHECK_DATALOCK_FLAG=true;
    loadCenterPanelNoLock('编辑回复',BASE_HREF+"manager/hotlineApply?id="+rowData.id);
}

//待回复等 radio的绑定事件
$('input:radio[name="hotlineApplyManager_feedbackState"]').change(function(){
    searchHotlineFeedback();
});

//匿名还是实名 radio的绑定事件
$('input:radio[name="hotlineApplyManager_comeFrom"]').change(function(){
    searchHotlineFeedback();
});

//是否开发 radio的绑定事件
$('input:radio[name="hotlineApplyManager_developState"]').change(function(){
    searchHotlineFeedback();
});

//搜索方法
function searchHotlineFeedback(){
    var startDate = $('input[name="hotlineApplyManager_date_from"]').val();
    var endDate =  $('input[name="hotlineApplyManager_date_to"]').val();
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
    $('#hotlineApplyManager').datagrid('load', {
        feedbackState : $('input[name="hotlineApplyManager_feedbackState"]:checked').val(),
        developState : $('input[name="hotlineApplyManager_developState"]:checked').val(),
        comeFrom : $('input[name="hotlineApplyManager_comeFrom"]:checked').val(),
        startDate : startDate,
        endDate : endDate,
    });
}