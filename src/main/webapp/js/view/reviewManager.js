/**
 * 审核管理
 * @Author lina
 * @Date 2016-09-08
 **/

var html_decode = function(str){
    if (str.length == 0) return "";
    str = str.replace(/>/g, "&gt;");
    str = str.replace(/</g, "&lt;");
    return str;
}
function reviewManagerUserQuestionFormator(value, rowData, rowIndex){
    if(rowData.feedbackState == 3){
        return '<div class="wraptitle"><font style="color: red">[已驳回]</font>' + html_decode(value)+"</div>";
    }else{
        return '<div class="wraptitle">'+html_decode(value)+'</div>';
    }
}

//双击进入审核处理页面
function showReview(rowIndex,rowData){
    CHECK_DATALOCK_FLAG=true;
    loadCenterPanelNoLock('审核处理',BASE_HREF+"manager/review?id="+rowData.id);
}

//待回复等 radio的绑定事件
$('input:radio[name="reviewManager_feedbackState"]').change(function(){
    searchReviewFeedback();
});

//匿名还是实名 radio的绑定事件
$('input:radio[name="reviewManager_comeFrom"]').change(function(){
    searchReviewFeedback();
});

//搜索方法
function searchReviewFeedback(){
    var startDate = $('input[name="reviewManager_date_from"]').val();
    var endDate =  $('input[name="reviewManager_date_to"]').val();
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
    $('#reviewManager').datagrid('load', {
        feedbackState : $('input[name="reviewManager_feedbackState"]:checked').val(),
        comeFrom : $('input[name="reviewManager_comeFrom"]:checked').val(),
        startDate : startDate,
        endDate : endDate,
    });
}

