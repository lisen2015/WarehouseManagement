/*															
 * FileName：functilList.js						
 *			
 * Description：用于functilList.jsp
 * 																
 * History：
 * 版本号 		作者 			日期       			简介
 *  1.0 	chenchen	2014-09-24		Create
 */
/**
 * 用于表单验证
 * 验证项：
 *    functionName（功能名称）：    必填、长度0-50
 *    htmlId（标签名称）：    必填，长度0-50，远程验证唯一性
 *    functionIndex（索引）：整数，0-1000
 * @version
 *    2014-09-23    chenchen    create
 *    2014-09-24    chenchen    去除htmlUrl验证
 */
/**
 * 用于打开新增对话框
 * @version
 *    2014-9-24        chenchen    create
 */
function addFunctionDialog() {
    $("#functionForm").form('clear')
    openDlgFunction("新增", "add");


}
/**
 * 新增或修改数据
 * @version
 *    2014-9-24        chenchen    create
 */
function addORUpdateFunction() {
    $('#functionForm').form('enableValidation');
    if ($('#functionForm').form('validate')) {
        var method = "add";
        if ($("#functionForm #id").val()) {
            method = "update";
        }
        var submitValue = $("#functionForm").serialize();
        submitValue+="&channelId="+$('#channelId').val();
        if ($("#htmlUrl option").length > 0) {
            submitValue += "&htmlUrlList=";
            var htmlUrlList = '';
            $("#htmlUrl option").each(function (n, e) {
                htmlUrlList += $(e).val() + ";";
            });
            htmlUrlList = htmlUrlList.substr(0, htmlUrlList.length - 1);
            if (htmlUrlList.length > 1000) {
                $.messager.alert("失败", "功能URL最大长度不能超过10000", "error");
                return
            } else {
                submitValue += htmlUrlList;
            }
        } else {
            $.messager.alert("失败", "请输入功能URL", "error");
            return;
        }
        $.ajax({
            url: BASE_HREF + "manager/function/" + method,
            data: submitValue,
            type: "POST",
            beforeSend: function () {
                $("#dlg_m_function").dialog("close");
                ajaxLoading("正在提交请稍候。。。");
            },
            success: function (data) {
                ajaxLoadEnd();
                switch (data.state) {
                    case 0:
                        $.messager.alert("失败", data.message, "error", function () {
                            $("#dlg_m_function").dialog("open");
                        });
                        break;
                    case 1:
                        $.messager.alert("成功", "操作成功");
                        $("#functionCofigTable").datagrid("reload");
                        $("#functionCofigTable").datagrid("clearChecked");
                        break;
                    case 2:
                        $.messager.alert("失败", "验证不通过", "error", function () {
                            $("#dlg_m_function").dialog("open");
                        });
                        break;
                    default:
                        break;
                }
            }
        });
    }
}
/**
 * 删除数据
 * @param flag
 * @version
 *    2014-9-24        chenchen    create
 */
function delFunctionDialog(flag) {
    var row;
    if (flag != null) {
        row = $("#functionCofigTable").datagrid("getChecked");
        if (row == null || row.length == 0) {
            $.messager.alert("出错", "请先选择要删除的数据", "error");
            return;
        } else {
            var rowids = "";
            $.each(row, function (i, n) {
                rowids += n.id + ",";
            });
            row = rowids.substr(0, rowids.length - 1);
        }
    } else {
        row = $("#functionCofigTable").datagrid("getSelected").id;
    }
    if (row != null) {
        $.messager.confirm("确认", "您是否确认要删除这些数据?", function (t) {
            if (t) {
                $.ajax({
                    url: BASE_HREF + "manager/function/del",
                    data: {
                        id: row
                    },
                    type: "POST",
                    beforeSend: function () {
                        ajaxLoading("正在提交请稍候。。。");
                    },
                    success: function (data) {
                        ajaxLoadEnd();
                        switch (data.state) {
                            case 0:
                                $.messager.alert("失败", data.message, "error");
                                break;
                            case 1:
                                $.messager.alert("成功", "操作成功");
                                $("#functionCofigTable").datagrid("reload");
                                $("#functionCofigTable").datagrid("clearChecked");
                                break;
                            default:
                                break;
                        }
                    }
                });
            }
        });
    } else {
        $.messager.alert("错误", "请选择要删除的数据！", "error");
    }

}
/**
 * 用于打开修改对话框
 * @param flag
 * @version
 *    2014-9-24        chenchen    create
 */
function updateFunctionDialog(flag) {
    var row;
    if (flag != null) {
        row = $("#functionCofigTable").datagrid("getChecked");
        if (row == null || row.length == 0) {
            $.messager.alert("出错", "请先选择要修改的数据", "error");
            return;
        } else if (row.length > 1) {
            $.messager.alert("出错", "只允许选择一个数据行修改", "error");
            return;
        } else {
            row = row[0];
        }
    } else {
        row = $("#functionCofigTable").datagrid("getSelected");
    }
    if (row != null) {
        $("#functionForm").form("load", row);

    } else {
        $.messager.alert("出错", "请先选择要修改的数据", "error");
        return;
    }
    openDlgFunction("修改", "update", row);
}
/**
 * 用于打开数据详细对话框
 * @param title
 * @param type
 * @version
 *    2014-9-24        chenchen    create
 */
function openDlgFunction(title, type, data) {
    if (type == "add") {
        $("#dlg_buttons_m_function_add").show();
        $("#dlg_buttons_m_function_update").hide();
    } else {
        $("#dlg_buttons_m_function_add").hide();
        $("#dlg_buttons_m_function_update").show();
    }
    $("#dlg_m_function").dialog({
        closed: true,
        inline: true,
        modal: true,
        onMove: onMoveDialog,
        buttons: "#dlg_buttons_m_function",
        onOpen: function () {
            $("#functionName").focus();
        }
    });
    $("#dlg_m_function").dialog("open").dialog("setTitle", title);
    $("#functionForm").validate().resetForm();
    $("input[type=text]").removeClass("error");
    $("textarea").removeClass("error");
    $("#htmlUrl").empty();
    $("#htmlUrlSelect").textbox('setValue', null);
    if (data != null && data.htmlUrl != null) {
        $.each(data.htmlUrl.split(";"), function (n, e) {
            $("#htmlUrl").append("<option title='" + e + "' value='" + e + "'>" + e + "</option>");
        });
    }
    $('#functionForm').form('resetValidation');
    $('#functionForm').form('disableValidation');
}
/**
 * 双击数据行回调方法，用于打开修改对话框
 * @param id
 * @param data
 * @version
 *    2014-9-24        chenchen    create
 */
function dblClickFunctionRow(id, data) {
    if (data != null) {
        $("#functionForm").form("load", data);
    } else {
        $.messager.alert("出错", "请先选择要修改的数据", "error");
        return;
    }
    openDlgFunction("修改", "update", data);
}
/**
 * 用于新增htmlUrl
 * @param e
 * @version
 *    2014-9-24        chenchen    create
 */
function addHtmlValue(e) {
    var value = $(e.data.target).textbox('getValue');
    if (value == null || $.trim(value).length == 0) {
        return;
    }
    if ($("#htmlUrl option").length > 0 && $("#htmlUrl option[value='" + value + "']").length > 0) {
        return;
    }
    $("#htmlUrl").append("<option title='" + value + "' value='" + value + "'>" + value + "</option>");

}
/**
 * 用于移除htmlUrl
 * @param e
 * @version
 *    2014-9-24        chenchen    create
 */
function removeHtmlValue(e) {
    $("#htmlUrl option:selected").remove();
}

/**
 * 表格行拖动排序回调方法，用于将结果保存到服务端
 * @param targetRow 目标对象
 * @param sourceRow 被拖动对象
 * @param point        位置
 * @version
 *    2014-9-23        chenchen    create
 */
function dropConfig(targetRow, sourceRow, point) {
    $.ajax({
        url: BASE_HREF + 'manager/function/move',
        data: {
            sId: sourceRow.id,
            sIndex: sourceRow.functionIndex,
            tIndex: targetRow.functionIndex,
            point: point,
        },
        type: 'POST',
        beforeSend: function () {
            ajaxLoading('正在提交请稍候。。。');
        },
        success: function (data) {
            ajaxLoadEnd();
            switch (data.state) {
                case 0:
                    $.messager.alert('失败', data.message, 'error');
                    $('#functionCofigTable').datagrid('reload');
                    break;
                case 1:
                    $('#functionCofigTable').datagrid('reload');
                    break;
                default:
                    break;
            }
        }
    });
}
/**
 * 表格行拖动排序之前的回调方法，用于检查是否可以拖动
 * @param targetRow 目标对象
 * @param sourceRow 被拖动对象
 * @param point        位置
 * @version
 *    2014-9-23        chenchen    create
 */
function checkDrop(targetRow, sourceRow, point) {
    if (!hasAuthority()) {
        return false;
    }
    if (point == 'top') {
        if (targetRow.functionIndex - 1 == sourceRow.functionIndex) {
            return false;
        }
    } else {
        if (targetRow.functionIndex + 1 == sourceRow.functionIndex) {
            return false;
        }
    }

}