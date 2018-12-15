/*															
 * FileName：index.js						
 *			
 * Description：全站通用js						
 * 																	
 * History：
 * 版本号 		作者 			日期       			简介
 *  1.0 	chenchen	2013-09-16		Create	
 *  1.0.1	chenchen	2014-09-13		update
 *  	open1方法为loadCenterPanel
 *  1.0.2	chenchen	2014-09-14		update
 *  	所有的basehref参数——》BASE_HREF
 *  1.0.3	chenchen	2014-09-17		update
 *  	reloadPanel方法——》reloadCenterPanel
 *  1.0.4	chenchen	2014-09-17		update
 *  	将Date.prototype.pattern移动到dateUtil.js
 *  1.0.5	chenchen	2014-09-24		update
 *  	增加hasAuthority()方法
 *  1.0.9	chenchen	2014-10-09		update
 *  	新增formatDate()方法
 *  1.1.0	chenchen	2014-10-11		增加浏览器判断
 */
var CHECK_DATALOCK_FLAG=false;;

$(document).ready(function () {
    $(document).ajaxError(ajaxLoadError);
    /*
     * 设置浏览器访问必须>ie9
     */
    if (navigator.userAgent.indexOf("MSIE") > 0 && document.documentMode < 9) {
        window.location.href = "./browserError.html";
    }
    /*
     * 设置阻止回退键
     */
    $(document).keydown(function (e) {
        var doPrevent;
        if (e.keyCode == 8) {
            var d = e.srcElement || e.target;
            if (d.tagName.toUpperCase() == 'INPUT' || d.tagName.toUpperCase() == 'TEXTAREA') {
                doPrevent = d.readOnly || d.disabled;
            }
            else
                doPrevent = true;
        }
        else
            doPrevent = false;

        if (doPrevent)
            e.preventDefault();
    });

});

$.extend($.fn.validatebox.defaults.rules, {
    mobile: {
        validator: function (value) {
            var length = value.length;
            var mobile = /^(((13[0-9]{1})|(15[0-9]{1})|(18[0-9]{1}))+\d{8})$/;
            return length == 11 && mobile.test(value);
        },
        message: '请输入正确的手机号码'
    },
    chrnum: {
        validator: function (value) {
            var chrnum = /^([a-zA-Z0-9_]+)$/;
            return chrnum.test(value);
        },
        message: '只能输入数字、字母及下划线'
    },
    idcard: {
        validator: function (value) {
            return IdCardValidate(value);
        },
        message: '请输入正确的身份证号码'
    },
    integer: {// 验证整数
        validator: function (value) {
            return /^[+]?[0-9]+\d*$/i.test(value);
        },
        message: '请输入整数'
    },
    range: {
        validator: function (value,param) {
            return value >= param[0] && value <= param[1];
        },
        message: "该字段必须介于{0}和{1}之间."

    },
    remoteByException: {
        validator: function (value, param) {
            if (value ==null){
                return true;
            }
            var data= {}
            for(var key in param[2]){
                data[key]=$(param[2][key]).val();
                if (data[key] == null || data[key] == '' || data[key] == '-1' || data[key] == 'null') {
                    data[key]= -1;
                }
            }
            data.field = param[1];
            data.value = value;
            var response = $.ajax({
                url: param[0],
                dataType: 'json',
                data: data,
                async: false,
                cache: false,
                type: 'post'
            }).responseText;
            return response == 'true';
        },
        message: '该字段数据已存在'
    },
    equals: {
        validator: function(value,param){
            return value == $(param[0]).val();
        },
        message: '内容与上一个输入项不匹配'
    }
});
/**
 * 用于重新加载中间面板内容
 * @param href 加载内容的url地址
 * @param title 标题
 * @version
 *     2013-09-01 陈晨 创建
 *   2014-09-13 陈晨 修改
 *    修改方法open1——》loadCenterPanel
 *    取消右侧面板加载，修改加载方式从原来的重新构建，改为refresh方法
 */
function loadCenterPanel(title, href) {
    //每一个面板跳转时均设置检查标志为 false,不进行数据检查
    CHECK_DATALOCK_FLAG=false;
    //获取中间面板
    var center = $("body").layout("panel", "center");
    //判断传入的字符串是否为Null或者为空字符串或者全是空格。
    if (href == null || href == 'null' || $.trim(href) == "") {
        center.panel("refresh");
    } else {//重新加载页面
        center.panel("refresh", href);
        //设置标题
        center.panel("setTitle", title);
    }
}

function loadCenterPanelNoLock(title, href) {
    //获取中间面板
    var center = $("body").layout("panel", "center");
    //判断传入的字符串是否为Null或者为空字符串或者全是空格。
    if (href == null || href == 'null' || $.trim(href) == "") {
        center.panel("refresh");
    } else {//重新加载页面
        center.panel("refresh", href);
        //设置标题
        center.panel("setTitle", title);
    }
}

/**
 * 用于重新加载中间面板内容
 * @version
 *     2013-09-01 陈晨 创建
 *   2014-09-13 陈晨 修改
 *   修改方法reloadPanel——》reloadCenterPanel<br />
 *   取消右侧面板加载，修改加载方式从原来的重新构建，改为refresh方法
 */
function reloadCenterPanel() {
    CHECK_DATALOCK_FLAG=false;
    $('body').layout('panel', 'center').panel("refresh");

}
function ajaxLoading(value) {
    if ($("#win_loading").length > 0) {
        $("#win_loading>div").html("<br/><br/>" + value);
        $("#win_loading").window();
    }

}
function ajaxLoadEnd() {
    if ($("#win_loading").attr("class") != undefined) {
        $("#win_loading").window('close');
    }
}
function ajaxLoadError(e, r, s, t) {
    if (r.status == 999) {
        $.messager.alert('提示', "您尚未登录或您的登录已过期，请先登录！！！", "error", function () {
                window.location.href = BASE_HREF + "mLogin";
            }
        );
    } else if (r.status == 998) {
        $.messager.alert('提示', "您的帐号已在其它地方登录！！！", "error", function () {
                window.location.href = BASE_HREF + "mLogin";
            }
        );
    } else if (r.status == 997) {
        $.messager.alert('提示', "您没有访问权限/操作！！！", "error");
    } else if (r.status == 996) {
        $.messager.alert('提示', "请使用IE10+或其他非IE内核浏览器", "error", function () {
            window.location.href = "./browserError.html";
        });
    } else {
        $.messager.alert('出错', '服务器出错啦！<br />错误内容：' + t, "error");
    }
    ajaxLoadEnd();
}

/**
 * 用于easyui接受日期数据并进行格式
 * @param value
 * @param row
 * @param index
 * @version
 *    2014-10-9        chenchen    create
 */
function formatDate(value, row, index) {
    if (value == null || value == "") {
        return "";
    } else {
        return new Date(value).pattern("yyyy-MM-dd");
    }
}

/**
 * 限制弹出框window的边界
 * @param left 左边界
 * @param top  上边界
 * @version
 *    2015-7-9        nijiaqi        create
 */
function onMoveWindow(left, top) {
    if (top < 2) {
        $(this).window("move", {top: 3})
    }
    if (left < 2) {
        $(this).window("move", {left: 3})
    }
}

/**
 * 限制弹出框dialog的边界
 * @param left 左边界
 * @param top  上边界
 * @version
 *    2015-7-9        nijiaqi        create
 */
function onMoveDialog(left, top) {
    if (top < 2) {
        $(this).dialog("move", {top: 3})
    }
    if (left < 2) {
        $(this).dialog("move", {left: 3})
    }
}
/*
 * 检查数据是否已锁定
 * 未锁定数据,返回 false
 * 已锁定数据返回 true
 */
function checkLock(key) {
    if (CHECK_DATALOCK_FLAG){
        var flag=true;
        var time=1000;
        $.ajax({
            url: BASE_HREF + 'manager/checkDataLocked',
            data: {
                key:key
            },
            async : false,
            type: 'POST',
            success: function (data) {
                /*
                 * 判断返回状态是否未1
                 *　true,未锁定,或当前用户锁定
                 *　false,已锁定
                 */
                if(data.state ==1){
                    flag=true;
                    time = data.continueTime*1000;
                }else{
                    $.messager.alert('提示', "当前数据已被用户:"+data.lockedUser+"锁定,请稍候再试!", "info");
                    CHECK_DATALOCK_FLAG=false;
                    flag = false;
                }
            }
        });
        if (flag){
            setTimeout("checkLock('"+key+"')",time);
            return true;
        }else{
            return false;
        }
    }else{
        return true;
    }

}

/**
 * HTML代码转译
 * @param str
 * @returns {string}
 */
