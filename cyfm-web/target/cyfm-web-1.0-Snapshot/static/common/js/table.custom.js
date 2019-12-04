(function ($) {
    $.fn.extend({
        "customTable": function (option) {
            var ct = this; 	//本控件容器
            ct.option = {
                /***********可配置内容(可扩展)************/
                dragType: 'cookie',				//启用cookie记录拖拽保存宽度以及字段列的隐藏或者关闭
                key: '-1',						//必填,用于区分当前页面唯一标识通常使用currentMenuId
                dragSize: true,					//是否启用grid头拖拽控制列宽
                dragSizeTr: ct.find("thead tr"),//控制列换的行,选择器
                dragSizeBefor: function (dragObj) {
                    $(dragObj).parents("div.listTableWrap").removeClass("table-scrollable")
                },
                dragSizeAfter: function () {
                },
                overHide: true,					//为true时td中超长内容自适应隐藏

                afterInit: false//初始化完成后执行
            };
            ct.option = $.extend(ct.option, option);

            if (ct.option.dragSize) {
                ct.option.dragSizeTr.find("th:not(.check):not(.action)").each(function (index, item) {
                    var wz = $(item).index();
                    $(item).append("<span class='columnResize' min='" + ($(this).html().length * 12 + 20) + "' >&nbsp;</span>").attr("i", wz).attr("index", wz);
                });

                var gridTable = ct
                var mPressed = false;
                var startX = 0;
                var eventTd = false;
                var startWidth = 0;
                var startTableWidth = 0;
                var eventColumn = false;

                $(".columnResize").bind('selectstart', function () {
                    return false;
                }).click(function () {
                    return false;
                }).dblclick(function () {
                    //双击事件
                    mPressed = false;
                    return false;
                }).mousedown(function (event) {
                    if (event.which != 1)
                        return false;
                    //鼠标按下事件,执行拖拽
                    mPressed = true;
                    ct.option.dragSizeBefor(this)
                    startX = event.pageX;
                    eventTd = $(this).parent();
                    startWidth = eventTd.width();
                    startTableWidth = gridTable.width();
                    eventColumn = $(this);
                    var moveDrag = function (e) {
                        if (mPressed) {
                            var width = startWidth + (e.pageX - startX);
                            if (width > parseInt(eventColumn.attr("min"))) {
                                eventTd.width(width);
                                ct.option.dragSizeTr.children(":eq(" + eventTd.attr("i") + ")").width(width);
                                width = startTableWidth + (e.pageX - startX);
                                gridTable.width(width);
                            } else {
                                width = parseInt(eventColumn.attr("min"));
                                eventTd.width(width);
                                ct.option.dragSizeTr.children(":eq(" + eventTd.attr("i") + ")").width(width);
                                width = startTableWidth - startWidth + width;
                                gridTable.width(width);
                            }

                        }
                    };
                    var endDrag = function (e) {
                        //TODO 储存至cookie
                        putCookie(eventTd);
                        $(document).unbind("mousemove", moveDrag).unbind("mouseup", endDrag).unbind('selectstart', selectDrag);
                        mPressed = false;
                        startX = 0;
                        eventTd = false;
                        startWidth = 0;
                        startTableWidth = 0;
                        eventColumn = false;
                        ct.option.dragSizeAfter(this)
                    };
                    var selectDrag = function () {
                        return false;
                    }
                    $(document).bind("mousemove", moveDrag).bind("mouseup", endDrag).bind('selectstart', selectDrag);
                    return false;
                });
            }

            var getCookie = function (cookieName) {
                var cookieString = document.cookie;
                var cookieValue = "";
                if (cookieString != null && cookieString != "") {
                    var cookieArray = cookieString.split("; ");
                    for (var i = cookieArray.length - 1; i >= 0; i--) {
                        var array = cookieArray[i].split("=");
                        if (array[0] == cookieName) {
                            cookieValue = array[1];
                            break;
                        }
                    }
                }
                return cookieValue;
            }

            var putCookie = function (putTd) {
                var index = putTd.attr("index");
                var width = putTd.width();
                var cookieName = ct.option.key;
                var cookieValue = getCookie(cookieName);
                if (cookieValue != "") {
                    //需要连接之前的值
                    var cookieArray = new Array(0);
                    try {
                        cookieArray = eval(cookieValue.replace(/@/g, ","));
                    } catch (exception) {
                    }
                    var newValue = "";
                    for (var i = 0; i < cookieArray.length; i++) {
                        var source = cookieArray[i];
                        if (source.i != index) {
                            newValue += "{i:" + source.i;
                            if (source.w)
                                newValue += "@w:" + source.w;
                            if (typeof(source.o) != "undefined")
                                newValue += "@o:" + source.o;
                            if (source.d)
                                newValue += "@d:" + source.d;
                            newValue += "}@";
                        }
                    }
                    cookieValue = newValue;
                }
                document.cookie = cookieName + "=[" + cookieValue + "{i:" + index + "@w:" + width + "@o:" + putTd.attr("i") + "}];path=/;expires=Sun, 05 Nov 10000 06:25:40 GMT";
            }

            var deleteCookie = function (cookieName) {
                var expires = new Date();
                expires.setTime(expires.getTime() - 1);
                document.cookie = cookieName + '=1;path=/;expires=' + expires.toGMTString();
            }

            var initDrag = function () {
                //初始化TR拖拽元素
                var dragArray = new Array(0);
                //读取COOKIE记录拖拽后的宽度
                var cookieValue = getCookie(ct.option.key);

                if (cookieValue != "") {
                    try {
                        dragArray = eval(cookieValue.replace(/@/g, ","));
                    } catch (exception) {
                    }

                    var tableWidth = 0;

                    $(ct.option.dragSizeTr).children().each(function (index) {
                        if ($(this).children().html() == "操作")
                            return;
                        //读取COOKIE记录拖拽后的宽度
                        for (var i = 0; i < dragArray.length; i++) {
                            if (parseInt(dragArray[i].i) == index) {
                                $(this).attr("width", dragArray[i].w);
                                dragArray[i] = dragArray[dragArray.length - 1];
                                dragArray.length = dragArray.length - 1;
                                break;
                            }
                        }

                        tableWidth += $(this).attr("width") && $(this).attr("width").indexOf("%") == -1 ? parseInt($(this).attr("width")) : $(this).width();
                    });
                    $("table").css("width", tableWidth + "px");
                }
            };

            //ctrl + 双击左键重置设定
            $("body").dblclick(function (e) {
                var ev = window.event || e;

                if (ev.ctrlKey) {
                    if (ct.option.dragType == 'cookie') {
                        deleteCookie(ct.option.key);
                    }
                    window.location.reload();
                }

            });

            return {
                //main function to initiate the theme
                init: function () {
                    initDrag();
                    ct.parent("div.listTableWrap").css("width", $(".tools").css("width"));

                    if ($("table").width() + 2 < $("table").parent("div").width()) {
                        $("table").parent("div").addClass("table-scrollable");
                    }
                }
            };
        }
    });
})
(jQuery);

