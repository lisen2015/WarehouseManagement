/**
 * 审核处理
 * @Author lina
 * @Date 2016-09-08
 **/

//页面刷新
function reloadReview(){
    loadCenterPanelNoLock('编辑回复',BASE_HREF+"manager/review?id="+$("#review_feedbackId").val());
}

//意见分析展开
function showReviewAnalyseList(){
    $('#review_showAnalyse').dialog('open');
}

//反馈回复展开
function showReviewAnswerList(){
    $('#review_showAnswer').dialog('open');
}

//电话备注展开
function showReviewContactList(){
    $('#review_showContact').dialog('open');
}

//处理列表中的附件
function showAnalyseAttachment(value, rowData, rowIndex){
    if(value!= "" && value!=undefined){
        var data = value.split(";");var link = "";
        for(var i =0;i<data.length;++i){
            var data2 = data[i].split("|");
            link += "<a class='showWindowImg' href='javascript:void(0)' _rel='"+BASE_HREF+"manager/getPicByReview?id="+data2[0]+"&rel=analyse'>"+data2[1]+"</a><br>";
        }
        return link;
    }else{
        return value;
    }
}

function showAnswerAttachment(value, rowData, rowIndex){
    if(value!= "" && value!=undefined){
        var data = value.split(";");var link = "";
        for(var i =0;i<data.length;++i){
            var data2 = data[i].split("|");
            link += "<a class='showWindowImg' href='javascript:void(0)' _rel='"+BASE_HREF+"manager/getPicByReview?id="+data2[0]+"&rel=answer'>"+data2[1]+"</a><br>";
        }
        return link;
    }else{
        return value;
    }
}




//通过
function saveReview(){
    var feedbackState = 4;

    var isDevelop = $('input[name="review_isDevelop"]:checked').val();
    var developOrg = $("#review_developOrg option:selected").val();
    var developState = $("#review_developState option:selected").val();
    var developOpinion = $("#review_developOpinion").val();
    var developTodo = $("#review_developTodo").val();
    var reviewFeedbackId = $("#review_feedbackId").val();

    var lastFeedbackAnswer = $("#review_lastAnswer").val();
    if( lastFeedbackAnswer==""){
        $.messager.alert('提示：', '反馈回复不允许为空！', 'warning');
        return;
    }

    $.ajax({
        url : BASE_HREF+"manager/reviewPassUserFeedback",
        type : 'post',
        data : {
            id:reviewFeedbackId,
            feedbackState:feedbackState,
            isDevelop:isDevelop,
            developOrg:developOrg,
            developState:developState,
            developOpinion:developOpinion,
            developTodo:developTodo
        },
        beforeSend : function () {
            $.messager.progress({
                text : '正在处理中...',
            });
        },
        success : function (data, response, status) {
            $.messager.progress('close');
            if (data.state == "1") {
                $.messager.show({
                    title : '提示',
                    msg : '已通过',
                });
                //页面刷新
                reloadReview();
            } else {
                $.messager.alert('失败！', '未知错误导致失败，请重试！', 'warning');
            }
        }
    });

    //发站内信
    addUserMessage();
    //发邮件
    sendUserFeedbackEmail();
    //发短信
    sendUserFeedbackMobileMessage();
}

//驳回
function rejectFeedback(){
    var feedbackState = 3;
    var isDevelop = $('input[name="review_isDevelop"]:checked').val();
    var developOrg = $("#review_developOrg option:selected").val();
    var developState = $("#review_developState option:selected").val();
    var developOpinion = $("#review_developOpinion").val();
    var developTodo = $("#review_developTodo").val();
    var reviewFeedbackId = $("#review_feedbackId").val();

    $.ajax({
        url : BASE_HREF+"manager/reviewRejectUserFeedback",
        type : 'post',
        data : {
            id:reviewFeedbackId,
            feedbackState:feedbackState,
            isDevelop:isDevelop,
            developOrg:developOrg,
            developState:developState,
            developOpinion:developOpinion,
            developTodo:developTodo
        },
        beforeSend : function () {
            $.messager.progress({
                text : '正在处理中...',
            });
        },
        success : function (data, response, status) {
            $.messager.progress('close');
            if (data.state == "1") {
                $.messager.show({
                    title : '提示',
                    msg : '已驳回',
                });
                //页面刷新
                reloadReview();
            } else {
                $.messager.alert('失败！', '未知错误导致失败，请重试！', 'warning');
            }
        }
    });
}

//返回
function returnReviewList(){
    loadCenterPanel('审核管理',BASE_HREF+"manager/reviewManager");
}

//发送站内信
function addUserMessage(){
    var userId =  $("#review_userId").val();
    if(userId==""){
        return;
    }
    var messageContent = $("#review_lastAnswer").val();

    $.ajax({
        url : BASE_HREF+"manager/addUserMessage",
        type : 'post',
        data :{
            userId:userId,
            messageContent : messageContent,
        },
        beforeSend : function () {
            $.messager.progress({
                text : '正在处理中...',
            });
        },
        success : function (data, response, status) {
            $.messager.progress('close');
            if (data.state == "1") {
                $.messager.show({
                    title : '提示',
                    msg : '站内信发送成功',
                });
            } else if(data.state == "2"){
                $.messager.show({
                    title : '提示',
                    msg : '站内信发送失败，该用户设置了不接收反馈消息！',
                });
            } else {
                $.messager.alert('站内信发送失败！', '未知错误导致失败，请重试！', 'warning');
            }
        }
    });
}

function sendUserFeedbackEmail(){
    var userFeedbackQuestion = $("#review_userQuestion").val();
    var emailContent = $("#review_lastAnswer").val();
    var userName = $("#review_userName").val();
    var userEmail = $("#review_userEmail").val();
    var feedbackId = $("#review_feedbackId").val();
    $.ajax({
        url : BASE_HREF+"manager/sendUserFeedbackEmail",
        type : 'post',
        data :{
            toEmail:userEmail,
            userName:userName,
            question:userFeedbackQuestion,
            content:emailContent,
            feedbackId:feedbackId,

        },
        beforeSend : function () {
            $.messager.progress({
                text : '正在处理中...',
            });
        },
        success : function (data, response, status) {
            $.messager.progress('close');
            $.messager.show({
                title : '提示',
                msg : '邮件已发送！',
            });
        }
    });
}


function sendUserFeedbackMobileMessage(){
    var mobile = $("#review_userMobile").val();
    $.ajax({
        url : BASE_HREF+"manager/sendMobileMessage",
        type : 'post',
        data :{
            mobile:mobile
        },
        beforeSend : function () {
            $.messager.progress({
                text : '正在处理中...',
            });
        },
        success : function (data, response, status) {
            $.messager.progress('close');
            $.messager.show({
                title : '提示',
                msg : '短信已发送！',
            });
        }
    });
}

$('#review_box').window({
    width : 500,
    top:100,
    shadow:false,
    title:"图片附件",
    closable : true,
    closed : true,
    zIndex : 9999,
    collapsible:false,
    minimizable:false,
    maximizable:false,
    draggable : true,
    resizable : true,
    modal : true,
    inline : true
});

$(document).delegate(".showWindowImg","click",function(e){
    e.stopPropagation();
    $('#review_box').window('open');
    var src = $(this).attr("_rel");
    $("#review_box").find("img").attr("src",src);
});







