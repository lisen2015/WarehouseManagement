

    var allChannelArr = [];

    var sseMenuFun = {
        init: function(arr) {
            var loadTimer = window.setInterval(function(){
                if(SSE_MENU_28 != undefined && SSE_MENU_28 != null){
                    window.clearInterval(loadTimer);
                    //初始化菜单
                    sseMenuFun.getChildren();
                    sseMenuFun.bindEvents();
                    sseMenuFun.selectedAll(arr);
                }
            },500);
        },
        chnlidArr: [],
        mainChnl: [],
        flag: true,
        mainSub: [],
        class: {},
        result:[],
        hideChannel:{},
        selectedAll: function(obj) {
            var allOlick = $('.olick');
            var selectArr = [];
            for (var i = 0; i < allOlick.length; i++) {
                selectArr.push($(allOlick[i]).attr('chnlid'));
            }
            for (var i = 0; i < selectArr.length; i++) {
                for (var l = 0; l < obj.length; l++) {
                    if (selectArr[i] == obj[l]) {
                        $(allOlick[i]).click();
                    }
                }

            }
        },
        bindEvents: function() {
            var menuControl = $(".js_control");
            menuControl.html(this.mainChnl.join(""));
            $(".selections").find(".section0").eq(0).remove();

            var arr = [];
            $('.title0 span').attr('class', 'open');
            $('.title1 span').removeClass('olick');
            menuControl.delegate(".title0 span", "click", function() {
                var $this = $(this);
                if ($this.attr('class') == "open") {
                    $this.parent().siblings().hide();
                    $this.attr('class', 'colse');
                } else {
                    $this.parent().siblings().show();
                    $this.attr('class', 'open');
                }
            }).delegate(".title1 span", "click", function() {
                var $this = $(this).parent();
                var arr = [];
                var mainArr = sseMenuFun.mainSub;
                if ($this.attr('class') == 'title1') {
                    $this.addClass("blue");
                    $this.children().addClass("green");
                } else {
                    $this.removeClass("blue");
                    $this.children().removeClass("green");
                }


                var one = $this.siblings().find('.item2');
                var two = $this.siblings().find('.title2').find('.olick');

                /*获取子集的chnlid*/
                for (var i = 0; i < one.length; i++) {
                    arr.push($(one[i]).attr('chnlid'));
                }
                for (var i = 0; i < two.length; i++) {
                    arr.push($(two[i]).attr('chnlid'));
                }


                /*给子集添加class & 去除class*/
                if ($this.attr('class') == 'title1 blue') {
                    for (var i = 0; i < one.length; i++) {
                        $(one[i]).addClass('green');

                        for (var j = 0; j < mainArr.length; j++) {
                            if (mainArr[j] == $(one[i]).attr('chnlid')) {
                                mainArr.splice(j, 1);
                            } else {

                            }
                        }
                        mainArr.push($(one[i]).attr('chnlid'));
                    }
                    for (var i = 0; i < two.length; i++) {
                        $(two[i]).addClass('green');
                        for (var j = 0; j < mainArr.length; j++) {
                            if (mainArr[j] == $(two[i]).attr('chnlid')) {
                                mainArr.splice(j, 1);
                            } else {

                            }
                        }
                        mainArr.push($(two[i]).attr('chnlid'));
                    }
                } else {
                    for (var i = 0; i < one.length; i++) {
                        $(one[i]).removeClass('green');
                    }
                    for (var i = 0; i < two.length; i++) {
                        $(two[i]).removeClass('green');
                    }
                    /*去除 全部*/
                    for (var i = 0; i < arr.length; i++) {
                        for (var j = 0; j < mainArr.length; j++) {
                            if (mainArr[j] == arr[i]) {
                                mainArr.splice(j, 1);
                                arr.splice(i, 1);
                                // j--;
                            }
                        }
                        i--;
                    }
                }

                /*累加*/
                // console.log(mainArr);

            }).delegate('.olick', 'click', function() {
                $(this).toggleClass("green");
                /*当前的chnlid*/
                var thisChnlid = $(this).attr('chnlid');
                var childGreen = sseMenuFun.class.greenNub;
                var mianArrs = sseMenuFun.mainSub;

                var isAddChnlid = true;
                for (var i = 0; i < mianArrs.length; i++) {
                    if (mianArrs[i] == thisChnlid) {
                        mianArrs.splice(i, 1);
                        isAddChnlid = false;
                    }
                }

                if (isAddChnlid) {
                    mianArrs.push(thisChnlid);
                }

                /*联动 去除关注*/
                $(this).parents('.text').siblings('.title1').children().removeClass('green');
                var thisParent = $(this).parents('.text').siblings('.title1');
                var children = thisParent.siblings('.text').children();
                thisParent.removeClass('blue');

                var g = 0;
                for (var i = 0; i < children.length; i++) {
                    var cd = $(children[i]);
                    if (cd.children().length > 0) {
                        if (cd.find("div.title2 span").hasClass('green')) {
                            g++;
                        }
                    } else if (cd.hasClass('green')) {
                        g++;
                    }
                }
                if (g == children.length) {
                    thisParent.addClass('blue');
                    thisParent.children().addClass('green');
                }

                var childNum = children.length;
                var parentChnlid = $(this).parents('.text').siblings('.title1').children().attr('chnlid');

                /*累加*/
                //console.log(mianArrs);
            });
        },

        getChildren: function(obj, nub) { //获取子栏目
            if (obj == undefined) {
                var nub = 0
                this.mainChnl.push('<div class="tree-multiselect">');
                this.mainChnl.push('<div class="selections">');
                //传入一级栏目
                this.childrenHtml(SSE_MENU_28[0].CHILDREN.split(";"), nub);
                this.mainChnl.push('</div>');
                this.mainChnl.push('</div>');

                this.hideChannel[0]={"CHNLNAME":SSE_MENU_28[0].CHNLNAME,"DISPLAY":SSE_MENU_28[0].DISPLAY,"CHILDREN":SSE_MENU_28[0].CHILDREN};
            } else {
                //获取子栏目
                var chnlArr = obj.CHILDREN.split(";");
                //排序子栏目
                chnlArr = chnlArr.sort(function(a, b) {
                    return a - b;
                });
                nub++;
                this.childrenHtml(chnlArr, nub);

            }
        },
        childrenHtml: function(chnlArr, nub) {
            for (var i = 0; i < chnlArr.length; i++) {
                var chnl = chnlArr[i];
                var childrens = SSE_MENU_28[chnl];
                //判断是否真实节点
                if (childrens != undefined && childrens != "") {
                    //判断是否还存在子栏目
                    if (childrens.CHILDREN != undefined && childrens.DISPLAY == 1 && childrens.CHILDREN != "" ) {
                        this.mainChnl.push('<div class="section' + nub + '" id="section' + chnl + '">');
                        this.mainChnl.push('<div class="title' + nub + '"><span class="chnl open olick" chnlid="' + chnl + '">' + childrens.CHNLNAME + '</span></div>');
                        this.mainChnl.push('<div class="text">');
                        if(nub<2){
                            this.getChildren(childrens, nub);
                        }
                        this.mainChnl.push('</div>');
                        this.mainChnl.push('</div>');
                    } else {
                        this.mainChnl.push(' <div chnlid="' + chnl + '" class="chnl item' + nub + ' olick">');
                        this.mainChnl.push(childrens.CHNLNAME + '</div>');
                    }
                    this.hideChannel[chnl]={"CHNLNAME":childrens.CHNLNAME,"DISPLAY":childrens.DISPLAY,"CHILDREN":childrens.CHILDREN};
                }
            }
        }
    };


    sseMenuFun.init(selectedChannel);


    function unselectedChannel (){
        var unselected = [];
        $(".green").each(function(){
            unselected.push($(this).attr("chnlid"));
        });
        return unselected;

    }


//保存结果
    function updateChannel(){
        var allShowChannel = [];
        $(".chnl").each(function(){
            allShowChannel.push($(this).attr("chnlid"));
        });
        //var channels = SSE_MENU_28;
        sseMenuFun.getChildren();
        var channels = sseMenuFun.hideChannel;
        var unselected = unselectedChannel();

        for(var i in unselected){
            delete channels[unselected[i]];
            for(var j in allShowChannel){
                if(unselected[i]==allShowChannel[j]){
                    allShowChannel.splice(j, 1);
                }
            }
        }

        //保存28full
        $.ajax({
            type : 'POST',
            url :BASE_HREF+ 'manager/updateCodeTableForChannel?='+new Date(),
            cache:false,
            data :{
                id:"101102",
                codeValue2:JSON.stringify(channels)
            },
            beforeSend : function () {
                $.messager.progress({
                    text : '正在处理中...',
                });
            },
            success : function (data) {
                $.messager.progress('close');
                if (data.state=="1") {
                    $.messager.show({
                        title : '提示',
                        msg :'保存成功！',
                    });
                }else{
                    $.messager.alert('保存失败！', '未知错误导致失败，请重试！', 'warning');
                }
            }
        });


        //保存选中栏目id
        $.ajax({
            type : 'POST',
            url :BASE_HREF+ 'manager/updateCodeTableForChannel?='+new Date(),
            cache:false,
            data :{
                id:"101101",
                codeValue2:JSON.stringify(unselected)
            },
            beforeSend : function () {
                $.messager.progress({
                    text : '正在处理中...',
                });
            },
            success : function (data) {
                $.messager.progress('close');
            }
        });

        //保存页面所有栏目
        $.ajax({
            type : 'POST',
            url :BASE_HREF+ 'manager/updateCodeTableForChannel',
            data :{
                id:"101104",
                codeValue2:allShowChannel.join(","),
            },
            beforeSend : function () {
                $.messager.progress({
                    text : '正在处理中...',
                });
            },
            success : function (data) {
                $.messager.progress('close');
            }
        });
    }



