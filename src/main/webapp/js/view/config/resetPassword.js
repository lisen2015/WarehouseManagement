/*
 * FileName：resetPassword.js
 *
 * Description：
 *
 * History：
 * 版本号 			作者 			日期       			简介
 *  1.0   			chenchen		16/7/7			Create
 */
$(document).ready(function () {
    setTimeout(function () {
        $('#userPasswordForm').form('resetValidation');
        $('#userPasswordForm').form('disableValidation');
    },200);
});
function resetPassword() {
    $('#userPasswordForm').form('enableValidation');
    if ($('#userPasswordForm').form('validate')) {
        $.ajax({
            url: BASE_HREF + 'manager/resetPassword/Info',
            data: $("form").serialize(),
            type: 'POST',
            beforeSend: function () {
                ajaxLoading("正在提交请稍候。。。");
            },
            success: function (data) {
                ajaxLoadEnd();
                switch (data.state) {
                    case 0:
                        $.messager.alert('要重置密码的用户不存在', data.message, "error");
                        break;
                    case 1:
                        $.messager.alert('成功', '操作成功');
                        break;
                    case 2:
                        $.messager.alert('失败', "原密码错误！", "error");
                        break;
                    default:
                        break;
                }
            }
        });
    }
}
function resetUserForm() {
    $("#userPasswordForm")[0].reset();
    $('#userPasswordForm').form('resetValidation');
    $('#userPasswordForm').form('disableValidation');

}

$("#userPasswordForm").validate({
    onkeyup: false,
    onclick: false,
    onfocusout: false,
    rules: {
        loginPassword: {
            required: true,
            rangelength: [6, 20]
        },
        newLoginPassword: {
            required: true,
            rangelength: [6, 20]
        },

        secondPassword: {
            required: true,
            equalTo: "#newLoginPassword"
        }
    },
    messages: {

        loginPassword: {
            required: "请输入原密码",
            rangelength: "密码长度必须在{0}-{1}至之间"
        },
        newLoginPassword: {
            required: "请输入新密码",
            rangelength: "密码长度必须在{0}-{1}至之间"
        },
        secondPassword: {
            required: "请再次输入密码",
            equalTo: "二次输入不一致"
        }
    }
});


