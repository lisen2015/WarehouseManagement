/**
 * 热线回复
 * @Author lina
 * @Date 2016-09-01
 */


//页面刷新
function reloadHotlineApply(){
    loadCenterPanelNoLock('热线回复',BASE_HREF+"manager/hotlineApply?id="+$("#hotlineApply_feedbackId").val());
}

/*********************************反馈回复  start************************************************/
//新增反馈回复弹框
function showAddHotlineApplyAnswer(){
    $('#hotlineApply_answer_add').show();
    $('#hotlineApply_answer_add').dialog({
        closed: true,
        width : 600,
        align : 'center',
        top:100,
        inline: true,
        title : '新增反馈回复',
        modal: true,
        buttons: '#hotlineApply_answer_add_button',
        onOpen: function () {
            $('#hotlineApply_AnswerContent').focus();
        }
    });
    $('#hotlineApply_answer_add').dialog('open');
    $('#hotlineApply_answer_add').form('resetValidation');
    $('#hotlineApply_answer_add').form('disableValidation');
    event.stopPropagation();
}

//新增反馈回复
function addHotlineApplyAnswer(){
    $('#hotlineApply_answer_add').form('enableValidation');
    if ($('#hotlineApply_answer_add').form('validate')) {
        $.ajax({
            url : BASE_HREF+"manager/addFeedbackAnswerByHotline",
            type : 'post',
            data : {
                feedbackId : $("#hotlineApply_feedbackId").val(),
                answerContent :$('#hotlineApply_answerContent').val(),
            },
            beforeSend : function () {
                $.messager.progress({
                    text : '正在新增中...',
                });
            },
            success : function (data, response, status) {
                $.messager.progress('close');
                if (data.state == "1") {
                    $.messager.show({
                        title : '提示',
                        msg : '新增反馈回复成功',
                    });
                    $('#hotlineApply_answer_add').dialog('close').form('reset');
                    //页面刷新
                    reloadHotlineApply();
                } else {
                    $.messager.alert('新增失败！', '未知错误导致失败，请重试！', 'warning');
                }
            }
        });
    }
}

//反馈回复展开
function showHotlineApplyAnswerList(){
    $('#hotlineApply_showAnswer').dialog('open');
}

/*********************************反馈回复  end************************************************/


/*********************************电话备注  start************************************************/
function showAddHotlineApplyContact(){
    $('#hotlineApply_contact_add').show();
    $('#hotlineApply_contact_add').dialog({
        closed: true,
        width : 600,
        align : 'center',
        top:100,
        inline: true,
        title : '新增电话备注',
        modal: true,
        buttons: '#hotlineApply_contact_add_button',
        onOpen: function () {
            $('#hotlineApply_contactContent').focus();
        }
    });
    $('#hotlineApply_contact_add').dialog('open');
    $('#hotlineApply_contact_add').form('resetValidation');
    $('#hotlineApply_contact_add').form('disableValidation');
    event.stopPropagation();
}

//新增电话备注
function addHotlineApplyContact(){
    $('#hotlineApply_contact_add').form('enableValidation');
    if ($('#hotlineApply_contact_add').form('validate')) {
        $.ajax({
            url : BASE_HREF+"manager/addFeedbackContactByHotline",
            type : 'post',
            data : {
                feedbackId : $("#hotlineApply_feedbackId").val(),
                contactContent :$('#hotlineApply_contactContent').val(),
            },
            beforeSend : function () {
                $.messager.progress({
                    text : '正在新增中...',
                });
            },
            success : function (data, response, status) {
                $.messager.progress('close');
                if (data.state == "1") {
                    $.messager.show({
                        title : '提示',
                        msg : '新增电话备注成功',
                    });
                    $('#hotlineApply_contact_add').dialog('close').form('reset');
                    //页面刷新
                    reloadHotlineApply();
                } else {
                    $.messager.alert('新增失败！', '未知错误导致失败，请重试！', 'warning');
                }
            }
        });
    }
}

//电话备注展开
function showHotlineApplyContactList(){
    $('#hotlineApply_showContact').dialog('open');
}

/*********************************电话备注  end************************************************/


//处理列表中的附件--公用方法
function showAttachment(value, rowData, rowIndex){
    if(value!= "" && value!=undefined){
        var data = value.split(";");var link = "";
        for(var i =0;i<data.length;++i){
            var data2 = data[i].split("|");
            link += "<a class='showWindowImg'  href='javascript:void(0)' _rel='"+BASE_HREF+"manager/getPicByHotline?id="+data2[0]+"&rel=answer'>"+data2[1]+"</a><br>";
        }
        return link;
    }else{
        return value;
    }
}

//上传图片
var uploader;
function uploaderCheck(){
    return true;
}
function upLoaderSuccess(btn){
    $.messager.alert("成功", "上传成功", "info",function(){
        $("#dlg_pic_up").dialog("close");
        reloadHotlineApply();
    });
}
function upLoaderError(code) {
    switch (code) {
        case "Q_EXCEED_NUM_LIMIT":
            $.messager.alert("错误", "文件太多了。。。", "error");
            break;
        case "Q_EXCEED_SIZE_LIMIT":
            $.messager.alert("错误", "文件太大了。。。", "error");
            break;
        case "Q_TYPE_DENIED":
            $.messager.alert("错误", "文件类型错误。。。", "error");
            break;
        default:
            $.messager.alert("错误", "添加文件出错了。。。", "error");
            break;
    }
}


function openUpPicDialog(th) {

    var analyseIdOrAnswerId = th.id;
    var rel = $(th).attr("rel");
    if(analyseIdOrAnswerId == undefined || analyseIdOrAnswerId == ""){
        if(rel=="analyse"){
            $.messager.alert("提示：", "请先新增一条意见分析！", "info");
        }else if(rel=="answer"){
            $.messager.alert("提示：", "请先新增一条反馈回复！", "info");
        }
        return;
    }
    $("#dlg_pic_up").show();
    $("#dlg_pic_up").dialog({
        closed : true,
        inline : true,
        modal : true,
        align : 'center',
        top:100,
        title : '上传图片',
        onOpen:function(){
            uploader=createUpload("#uploader",{
                    pick: {
                        id: "#filePicker",
                        label: "点击选择图片"
                    },
                    dnd: "#uploader .queueList",
                    paste: document.body,
                    accept: {
                        title: "Images",
                        extensions: "gif,jpg,jpeg,bmp,png",
                        mimeTypes: "image/*"
                    },
                    // swf文件路径
                    swf:BASE_HREF+"js/webuploader/Uploader.swf",
                    disableGlobalDnd: true,
                    server: BASE_HREF+"manager/uploadPicByHotline?rel="+$(th).attr("rel")+"&analyseIdOrAnswerId="+analyseIdOrAnswerId,
                    fileNumLimit: 300,
                    fileSizeLimit: 100 * 1024 * 1024,
                    fileSingleSizeLimit: 1024 * 1024 * 1024   } ,
                uploaderCheck,upLoaderSuccess,upLoaderError);
        },
        onClose:function(){
            if(uploader!=null){
                uploader.destroy();
            }
        }
    });
    $("#dlg_pic_up").dialog("open");
}

//删除图片
function removeImage(th){
    var analyseAttachIdOrAnswerAttachId = th.id;
    var sign = $(th).attr("rel");
    $.messager.confirm('确定操作', '您正在要删除所选的记录吗？', function (flag) {
        if (flag) {
            $.ajax({
                type : 'POST',
                url :BASE_HREF+ 'manager/deleteAttachmentByHotline',
                data : {
                    id:analyseAttachIdOrAnswerAttachId,
                    rel:sign
                },
                beforeSend : function () {
                    $.messager.progress({
                        text : '正在处理中...',
                    });
                },
                success : function (data) {
                    $.messager.progress('close');
                    if (data.state=="1") {
                        $(th).prev().remove();
                        $(th).remove();
                        $.messager.show({
                            title : '提示',
                            msg :'删除成功！',
                        });
                    }
                },
            });
        }
    });
}





//保存
function hotlineSaveUserFeedback(th){
    var feedbackState = $("#hotlineApply_feedbackState").val();
    var feedbackLevel = $("#hotlineApply_feedbackLevel option:selected").val();
    var feedbackType = $("#hotlineApply_feedbackType option:selected").val();
    var hotlineApplyFeedbackId = $("#hotlineApply_feedbackId").val();


    $.ajax({
        url : BASE_HREF+"manager/hotlineSaveUserFeedback",
        type : 'post',
        data : {
            id:hotlineApplyFeedbackId,
            feedbackState:feedbackState,
            feedbackLevel:feedbackLevel,
            feedbackType:feedbackType
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
                    msg : '保存成功',
                });
                //页面刷新
                reloadHotlineApply();
            } else {
                $.messager.alert('保存失败！', '未知错误导致失败，请重试！', 'warning');
            }
        }
    });

}

//一键处理
function hotlineCommitUserFeedback(th){
    var feedbackState = 4;
    var hotlineApplyFeedbackId = $("#hotlineApply_feedbackId").val();
    var feedbackLevel = $("#hotlineApply_feedbackLevel option:selected").val();
    var feedbackType = $("#hotlineApply_feedbackType option:selected").val();

    var lastFeedbackAnswer = $("#hotlineApply_lastAnswer").val();
    //var lastFeedbackContact = $("#hotlineApply_lastContact").val();

    if( lastFeedbackAnswer==""){
        $.messager.alert('提示：', '反馈回复不允许为空！', 'warning');
        return;
    }

    $.ajax({
        url : BASE_HREF+"manager/hotlineCommitUserFeedback",
        type : 'post',
        data : {
            id:hotlineApplyFeedbackId,
            feedbackLevel:feedbackLevel,
            feedbackType:feedbackType,
            feedbackState:feedbackState,
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
                    msg : '一键处理成功',
                });
                //页面刷新
                reloadHotlineApply();
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

//返回
function returnHotlineApplyList(){
    loadCenterPanel('热线回复',BASE_HREF+"manager/hotlineApplyManager");
}

//发送站内信
function addUserMessage(){
    var userId =  $("#hotlineApply_userId").val();
    if(userId==""){
        return;
    }
    var messageContent = $("#hotlineApply_lastAnswer").val();

    $.ajax({
        url : BASE_HREF+"manager/addUserMessageByHotline",
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
    var userFeedbackQuestion = $("#hotlineApply_userQuestion").val();
    var emailContent = $("#hotlineApply_lastAnswer").val();
    var userName = $("#hotlineApply_userName").val();
    var userEmail = $("#hotlineApply_userEmail").val();
    var feedbackId = $("#hotlineApply_feedbackId").val();

    $.ajax({
        url : BASE_HREF+"manager/sendUserFeedbackEmailByHotline",
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
    var mobile = $("#hotlineApply_userMobile").val();
    $.ajax({
        url : BASE_HREF+"manager/sendMobileMessageByHotline",
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


$('#hotlineApply_box').window({
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
    $('#hotlineApply_box').window('open');
    var src = $(this).attr("_rel");
    $("#hotlineApply_box").find("img").attr("src",src);
});
