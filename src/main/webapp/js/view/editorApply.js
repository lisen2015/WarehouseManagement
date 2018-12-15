/**
 * 编辑回复
 * @Author lina
 * @Date 2016-09-01
 */


//页面刷新
function reloadEditorApply(){
    loadCenterPanelNoLock('编辑回复',BASE_HREF+"manager/editorApply?id="+$("#editorApply_feedbackId").val());
}

/*********************************意见分析  start************************************************/
//新增意见分析弹框
function showAddEditorApplyAnalyse(){
    $('#editorApply_analyse_add').show();
    $('#editorApply_analyse_add').dialog({
        closed: true,
        width : 600,
        align : 'center',
        top:100,
        inline: true,
        title : '新增意见分析',
        modal: true,
        buttons: '#editorApply_analyse_add_button',
        onOpen: function () {
            $('#editorApply_analyseContent').focus();
        }
    });
    $('#editorApply_analyse_add').dialog('open');
    $('#editorApply_analyse_add').form('resetValidation');
    $('#editorApply_analyse_add').form('disableValidation');
    event.stopPropagation();
}
//新增意见分析
function addEditorApplyAnalyse(){
    $('#editorApply_analyse_add').form('enableValidation');
    if ($('#editorApply_analyse_add').form('validate')) {
        $.ajax({
            url : BASE_HREF+"manager/addFeedbackAnalyse",
            type : 'post',
            data : {
                feedbackId : $("#editorApply_feedbackId").val(),
                analyseContent :$('#editorApply_analyseContent').val(),
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
                        msg : '新增意见分析成功',
                    });
                    $('#editorApply_analyse_add').dialog('close').form('reset');
                    //页面刷新
                    reloadEditorApply();
                } else {
                    $.messager.alert('新增失败！', '未知错误导致失败，请重试！', 'warning');
                }
            }
        });
    }
}

//意见分析展开
function showEditorApplyAnalyseList(){
    $('#editorApply_showAnalyse').dialog('open');
}

/*********************************意见分析  end************************************************/


/*********************************反馈回复  start************************************************/
//新增反馈回复弹框
function showAddEditorApplyAnswer(){
    $('#editorApply_answer_add').show();
    $('#editorApply_answer_add').dialog({
        closed: true,
        width : 600,
        align : 'center',
        top:100,
        inline: true,
        title : '新增反馈回复',
        modal: true,
        buttons: '#editorApply_answer_add_button',
        onOpen: function () {
            $('#editorApply_answerContent').focus();
        }
    });
    $('#editorApply_answer_add').dialog('open');
    $('#editorApply_answer_add').form('resetValidation');
    $('#editorApply_answer_add').form('disableValidation');
    event.stopPropagation();
}

//新增反馈回复
function addEditorApplyAnswer(){
    $('#editorApply_answer_add').form('enableValidation');
    if ($('#editorApply_answer_add').form('validate')) {
        $.ajax({
            url : BASE_HREF+"manager/addFeedbackAnswer",
            type : 'post',
            data : {
                feedbackId : $("#editorApply_feedbackId").val(),
                answerContent :$('#editorApply_answerContent').val(),
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
                    $('#editorApply_answer_add').dialog('close').form('reset');
                    //页面刷新
                    reloadEditorApply();
                } else {
                    $.messager.alert('新增失败！', '未知错误导致失败，请重试！', 'warning');
                }
            }
        });
    }
}

//反馈回复展开
function showEditorApplyAnswerList(){
    $('#editorApply_showAnswer').dialog('open');
}

/*********************************反馈回复  end************************************************/


/*********************************电话备注  start************************************************/
function showAddEditorApplyContact(){
    $('#editorApply_contact_add').show();
    $('#editorApply_contact_add').dialog({
        closed: true,
        width : 600,
        align : 'center',
        top:100,
        inline: true,
        title : '新增电话备注',
        modal: true,
        buttons: '#editorApply_contact_add_button',
        onOpen: function () {
            $('#editorApply_contactContent').focus();
        }
    });
    $('#editorApply_contact_add').dialog('open');
    $('#editorApply_contact_add').form('resetValidation');
    $('#editorApply_contact_add').form('disableValidation');
    event.stopPropagation();
}

//新增电话备注
function addEditorApplyContact(){
    $('#editorApply_contact_add').form('enableValidation');
    if ($('#editorApply_contact_add').form('validate')) {
        $.ajax({
            url : BASE_HREF+"manager/addFeedbackContact",
            type : 'post',
            data : {
                feedbackId : $("#editorApply_feedbackId").val(),
                contactContent :$('#editorApply_contactContent').val(),
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
                    $('#editorApply_contact_add').dialog('close').form('reset');
                    //页面刷新
                    reloadEditorApply();
                } else {
                    $.messager.alert('新增失败！', '未知错误导致失败，请重试！', 'warning');
                }
            }
        });
    }
}

//电话备注展开
function showEditorApplyContactList(){
    $('#editorApply_showContact').dialog('open');
}

/*********************************电话备注  end************************************************/


//处理列表中的附件
function showAnalyseAttachment(value, rowData, rowIndex){
    if(value!= "" && value!=undefined){
        var data = value.split(";");var link = "";
        for(var i =0;i<data.length;++i){
            var data2 = data[i].split("|");
            link += "<a class='showWindowImg'  href='javascript:void(0)' _rel='"+BASE_HREF+"manager/getPic?id="+data2[0]+"&rel=analyse'>"+data2[1]+"</a><br>";
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
            link += "<a class='showWindowImg' href='javacript:void(0)' _rel='"+BASE_HREF+"manager/getPic?id="+data2[0]+"&rel=answer'>"+data2[1]+"</a><br>";
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
        reloadEditorApply();
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
                    server: BASE_HREF+"manager/uploadPic?rel="+$(th).attr("rel")+"&analyseIdOrAnswerId="+analyseIdOrAnswerId,
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


    $.messager.confirm('确定操作', '您确定要删除所选图片吗？', function (flag) {
        if (flag) {
            $.ajax({
                type : 'POST',
                url :BASE_HREF+ 'manager/deleteAttachment',
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
function editorSaveUserFeedback(th){
    var feedbackState = $("#editorApply_feedbackState").val();
    var feedbackLevel = $("#editorApply_feedbackLevel option:selected").val();
    var feedbackType = $("#editorApply_feedbackType option:selected").val();
    var isDevelop = $('input[name="editorApply_isDevelop"]:checked').val();
    var developOrg = $("#editorApply_developOrg option:selected").val();
    var developState = $("#editorApply_developState option:selected").val();
    var developOpinion = $("#editorApply_developOpinion").val();
    var developTodo = $("#editorApply_developTodo").val();
    var editorApplyFeedbackId = $("#editorApply_feedbackId").val();

    $.ajax({
        url : BASE_HREF+"manager/editorSaveUserFeedback",
        type : 'post',
        data : {
            id:editorApplyFeedbackId,
            feedbackLevel:feedbackLevel,
            feedbackType:feedbackType,
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
                    msg : '保存成功',
                });
                //页面刷新
                reloadEditorApply();
            } else {
                $.messager.alert('保存失败！', '未知错误导致失败，请重试！', 'warning');
            }
        }
    });

}

//提交
function editorCommitUserFeedback(th){
    var feedbackState = 2;
    var feedbackLevel = $("#editorApply_feedbackLevel option:selected").val();
    var feedbackType = $("#editorApply_feedbackType option:selected").val();
    var isDevelop = $('input[name="editorApply_isDevelop"]:checked').val();
    var developOrg = $("#editorApply_developOrg option:selected").val();
    var developState = $("#editorApply_developState option:selected").val();
    var developOpinion = $("#editorApply_developOpinion").val();
    var developTodo = $("#editorApply_developTodo").val();
    var editorApplyFeedbackId = $("#editorApply_feedbackId").val();

    var lastFeedbackAnalyse = $("#editorApply_lastAnalyse").val();
    var lastFeedbackAnswer = $("#editorApply_lastAnswer").val();
    var lastFeedbackContact = $("#editorApply_lastContact").val();

    if( lastFeedbackAnalyse =="" &&  lastFeedbackAnswer=="" && lastFeedbackContact ==""){
        $.messager.alert('提示：', '意见分析、反馈回复、电话备注不允许同时为空！', 'warning');
        return;
    }

    $.ajax({
        url : BASE_HREF+"manager/editorCommitUserFeedback",
        type : 'post',
        data : {
            id:editorApplyFeedbackId,
            feedbackLevel:feedbackLevel,
            feedbackType:feedbackType,
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
                    msg : '提交成功',
                });
                //页面刷新
                reloadEditorApply();
            } else {
                $.messager.alert('提交失败！', '未知错误导致失败，请重试！', 'warning');
            }
        }
    });

}

//返回
function returnEditorApplyList(){
    loadCenterPanel('编辑回复',BASE_HREF+"manager/editorApplyManager");
}


$('#editorApply_box').window({
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
    $('#editorApply_box').window('open');
    var src = $(this).attr("_rel");
    $("#editorApply_box").find("img").attr("src",src);
});








