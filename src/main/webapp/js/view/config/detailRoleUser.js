/*
 * FileName：detailRoleUser.js
 *
 * Description：detailRoleUser.jsp 的 js 文件
 *
 * History：
 * 版本号 			作者 			日期       			简介
 *  1.0   			chenchen		16/7/6			Create
 */

function delRoleUser() {
    var rows = $('#roleConfigTable').datagrid('getChecked');
    if (rows == null || rows.length <= 0) {
        $.messager.alert('失败', '请先选择角色！！！', "error");
        return;
    }
    $.messager.confirm('确认', '您是否确认要删除这些数据?', function (t) {
        if (t) {
            var ids = "";
            $.each(rows, function (i, n) {
                if (i == rows.length - 1) {
                    ids += n.id;
                } else {
                    ids += n.id + ',';
                }
            });
            $.ajax({
                url: BASE_HREF+'manager/user/role/del',
                data: 'ids=' + ids,
                type: 'POST',
                beforeSend: function () {
                    $("#win_user_config").window("close");
                    ajaxLoading("正在提交请稍候。。。");
                },
                success: function (data) {
                    ajaxLoadEnd();
                    switch (data.state) {
                        case 0:
                            $.messager.alert('失败', data.message, "error", function () {
                                $("#win_user_config").window("open");
                            });
                            break;
                        case 1:
                            $.messager.alert('成功', '操作成功');
                            $("#win_user_config").window("close");
                            break;
                        case 2:
                            $.messager.alert('失败', data.message, "error", function () {
                                $("#win_user_config").window("open");
                            });
                            break;
                        default:
                            break;
                    }
                }
            });
        }
    });

}
