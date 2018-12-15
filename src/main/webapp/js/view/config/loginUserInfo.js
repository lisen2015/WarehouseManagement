/*															
 * FileName：loginUserInfo.js						
 *			
 * Description：用于loginUserInfo.jsp
 * 																
 * History：
 * 版本号 		作者 			日期       			简介
 *  1.0 	chenchen	2014-12-10		Create
 */
$(document).ready(function () {
    $('#userForm').form('resetValidation');
    $('#userForm').form('disableValidation');
});
function addORUpdateUser() {
    $('#userForm').form('enableValidation');
    if ($('#userForm').form('validate')) {
        var isSend = 0;
        if ($("#isSendCK").prop("checked") == true) {
            isSend = 1;
        }
        $.ajax({
            url: BASE_HREF + "manager/loginUser/addUser",
            data: $("form").serialize() + "&isSend=" + isSend,
            type: "POST",
            beforeSend: function () {
                $("#dlg_m_user").dialog("close");
                ajaxLoading("正在提交请稍候。。。");
            },
            success: function (data) {
                ajaxLoadEnd();
                switch (data.state) {
                    case 0:
                        $.messager.alert("失败", data.message, "error", function () {
                            $("#dlg_m_user").dialog("open");
                        });
                        break;
                    case 1:
                        $.messager.alert("成功", "操作成功");
                        resetUserForm();
//					$("#userTable").datagrid("reload");
                        break;
                    case 2:
                        $.messager.alert("失败", data.message, "error", function () {
                            $("#dlg_m_user").dialog("open");
                        });

                        break;
                    default:
                        break;
                }
            }
        });
    }
}

function resetUserForm() {
    reloadCenterPanel();

}