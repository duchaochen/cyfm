$cy = function () {
    var noticeTimer;
    var longPollingAjax;
    var $notice_count;
    var $notification_list;
    var pollingUrl = ctx + "/polling";

    var notice_template = "<li data-id='' data-content='{content}'><a href=\"javascript:;\">" +
        "   <span class=\"time\">{time}</span>" +
        "   <span class=\"details\">" +
        "       <span class=\"label label-sm label-icon label-info\">" +
        //TODO 先不适用消息通知图标，后续扩展 "           <i class=\"fa fa-bullhorn\"></i>" +
        "       </span> </span>" +
        "</a></li>"

    var longPolling = function (url, callback, auto) {
        longPollingAjax = $.ajax({
            type: 'post',
            url: url,
            async: true,
            cache: false,
            global: false,
            timeout: 30 * 1000,
            dataType: "json",
            success: function (data, status, request) {
                //console.log(data)
                callback(data);
                data = null;
                status = null;
                request = null;
                if (auto) {
                    noticeTimer = setTimeout(
                        function () {
                            longPolling(url, callback, auto);
                        },
                        5000
                    );
                }
            },
            error: function (xmlHR, textStatus, errorThrown) {
                xmlHR = null;
                textStatus = null;
                errorThrown = null;
                if (auto) {
                    noticeTimer = setTimeout(
                        function () {
                            longPolling(url, callback, true);
                        },
                        8 * 1000
                    );
                }
            }
        });
    };

    function refreshNotification(data) {
        $notification_list.html("");

        if (data.unreadNotificationsCount == 0) {
            $notice_count.text("")
        }else{
            $notice_count.text(data.unreadNotificationsCount);
        }

        $(data.unreadNotifications).each(function (i, o) {
            var notice = $(notice_template)
            notice.data("id", o.id).data("content", o.content);
            notice.find(".time").text(o.date);
            notice.find(".details").append(o.title);
            $notification_list.append(notice);
        });
    }

    var flushPolling = function () {
        //console.log("lc");
        longPolling(pollingUrl + "?flush=true", function (data) {
            refreshNotification(data);
            longPollingAjax.abort();
            clearTimeout(noticeTimer);

            longPolling(pollingUrl, function (data) {
                console.debug("长轮训获取推送...");
                if (data) {
                    refreshNotification(data);
                }
            }, true);
        }, false)
    };

    //初始化系统消息推送
    var initNotice = function () {
        $notice_count = $(".notice_count");
        $notification_list = $("ul.notification-list");
        flushPolling();

        $("ul.notification-list").on("click", "li", function () {
            var $item = $(this);

            var id = $item.data("id");
            var title = $item.find(".details").text();
            var content = $item.data("content").replace(/{ctx}/g, _ctx);

            var message = top.layer.alert(content, {
                skin: 'layui-layer-lan'
                , title: title
                , closeBtn: 0
                , shift: 5 //动画类型
            }, function () {
                $.get(_ctx + "/manage/maintain/notification/markRead/" + id, function (result) {
                    flushPolling()
                });
                top.layer.close(message)
            });

            $item.remove();
            $("li.external .bold").text($(".notice_list li").size() - 1);
        });

        document.addEventListener("visibilitychange", function () {
            console.log(document.visibilityState);
            if (document.visibilityState == "hidden") {
                console.debug('页面失去焦点，不在接受推送数据...');
                longPollingAjax.abort();
                clearTimeout(noticeTimer);
            } else if (document.visibilityState == "visible") {
                console.debug('页面获得焦点，立即刷新推送数据...');
                flushPolling();
            }
        });
    };


    //当前页面刷新,避免冲洗提交表单的刷新操作
    //刷新页面
    var refreshPage = function () {
        console.debug("刷新页面")
        var whref = window.location.href;
        if (whref.lastIndexOf("#") == whref.length - 1) {
            whref = whref.substring(0, whref.length - 1);
        }
        window.location.href = whref;
    };
    //锁屏加载
    var waiting = function (message, timeout) {
        if (message) {
            layer.msg(message, {icon: 16, shade: [0.6, '#000']});
        } else {
            layer.load('', {
                shade: [0.4, '#fff'] //0.1透明度的白色背景
                , time: timeout ? timeout : 10000
            });
        }

    };
    //结束任何锁屏
    var waitingOver = function (current) {
        layer.closeAll('loading');
        layer.closeAll('dialog');
        layer.closeAll('tips');
        if (current) {
            top.layer.closeAll('loading');
            top.layer.closeAll('dialog');
            top.layer.closeAll('tips');
        }
    };
    //关闭我(当我是弹出框时)
    var closeme = function (nosync) {
        var index = parent.layer.getFrameIndex(window.name);
        //先得到当前iframe层的索引,然后是否异步执行,异步执行是避免影所在控件原来的行为
        if (!nosync) {
            setTimeout(
                function () {
                    parent.layer.close(index)
                }, 100);
        } else {
            parent.layer.close(index)
        }

    };
    //消息弹出
    var info = function (message, callback) {
        top.layer.alert(message, {
            skin: 'layui-layer-lan'
            , title: '消息(3秒后自动关闭)'
            , content: message
            , time: 3000
            , zindex: 16777271
            , closeBtn: 0
            , btn: ['关闭']
            , shift: 5 //动画类型
        }, callback);
    };
    //成功弹出
    var success = function (message, callback) {
        layer.alert(message, {
            skin: 'layui-layer-lan'
            , title: '成功消息(3秒后自动关闭)'
            , content: message
            , time: 3000
            , icon: 1
            , zindex: 16777271
            , shift: 5 //动画类型
            , closeBtn: 0
            , btn: ['关闭']
        }, callback);
    };
    //警告弹出
    var warn = function (message, callback) {
        top.layer.alert(message, {
            skin: 'layui-layer-lan'
            , title: '警告'
            , content: message
            , icon: 0
            , zindex: 16777271
            , closeBtn: 0
            , btn: ['关闭']
            , shift: 5 //动画类型
        }, callback);
    };

    //错误弹出
    var error = function (message, callback) {
        top.layer.alert(message, {
            skin: 'layui-layer-lan'
            , title: '错误消息'
            , content: message
            , icon: 2
            , zindex: 16777271
            , closeBtn: 0
            , btn: ['关闭']
            , shift: 5 //动画类型
        }, callback);
    };
    //确认操作弹出
    var confirm = function (options) {
        opts = $.extend({
            'title': '确认操作',
            'message': '确定执行操作?',
            width: 'auto',
            height: 'auto',
            async: false,
            btn: ['确定', '取消'],
            yes: $.noop,
            no: $.noop
        }, options);
        top.layer.confirm(opts.message, {
            area: [opts.width, opts.height]
            , title: opts.title
            , icon: 3
            , btn: opts.btn //按钮
        }, function (index, layero) {
            if (opts.yes) {
                opts.yes(index, layero);
            }
            if (top) {
                top.layer.close(index);
            }
        }, function (index, layero) {
            if (opts.no) {
                opts.no(index, layero);
            }
            if (top) {
                top.layer.close(index);
            }
        });
    };
    //美化加工表单
    var handleUniform = function () {
        if (!$().uniform) {
            return;
        }
        var test = $('input[type=checkbox]:not(.toggle, .md-check, .md-radiobtn, .make-switch, .icheck), input[type=radio]:not(.toggle, .md-check, .md-radiobtn, .star, .make-switch, .icheck)');
        if (test.size() > 0) {
            test.each(function () {
                if ($(this).parents('.checker').size() === 0) {
                    $(this).show();
                    $(this).uniform();
                }
            });
        }
    };   //初始化自动补全
    //初始化时间控件
    var initDatePick = function () {
        // 时间组建初始化
        $('.datepicker[data-format]').each(function (index, obj) {
            var format = $(obj).data('format');
            var event = $(obj).data('event');
            console.debug(format, event)
            var type = 'datetime';
            var istime = true;
            if (format == 'both') {
                format = 'yyyy-MM-dd HH:mm:ss';
            } else if (format == 'date') {
                format = 'yyyy-MM-dd';
                type = 'date';
            } else if (format == 'time') {
                format = 'HH:mm:ss';
                type = 'time';
            }

            laydate.render({
                elem: this,
                type: type,
                format: format,
                istoday: true,
                isclear: true, //是否显示清空
                issure: true,
                trigger: event ? event : "focus",
                //festival: true
            });
        })
    };
    //初始化自动补全
    var initAutocomplete = function (options) {
        options = options || {};

        var configs = {
            minLength: 1,
            enterSearch: false,
            focus: function (event, ui) {
                jQuery(options.input).val(ui.item.label);
                return false;
            },
            renderItem: function (ul, item) {
                return jQuery('<li>')
                    .data('ui-autocomplete-item', item)
                    .append('<a>' + item.label + '</a>')
                    .appendTo(ul);
            }
        };

        configs = jQuery.extend(true, configs, options);

        jQuery(configs.input)
            .on('keydown', function (e) {
                var ev = window.event || e;
                //回车查询
                if (configs.enterSearch && ev.keyCode === $.ui.keyCode.ENTER) {
                    configs.select(ev, {item: {value: jQuery(this).val()}});
                }
            })
            .autocomplete({
                source: configs.source,
                minLength: configs.minLength,
                focus: configs.focus,
                select: configs.select
            }).data('ui-autocomplete')._renderItem = configs.renderItem;
    };

    var submiting = false;
    var validFiledTips = {};

    //移除分页参数
    var removePageParam = function (pageURL) {
        pageURL = pageURL.replace(/&\w*page.pn=\d+/gi, '');
        pageURL = pageURL.replace(/\?\w*page.pn=\d+&/gi, '?');
        pageURL = pageURL.replace(/\?\w*page.pn=\d+/gi, '');
        pageURL = pageURL.replace(/&\w*page.size=\d+/gi, '');
        pageURL = pageURL.replace(/\?\w*page.size=\d+&/gi, '?');
        pageURL = pageURL.replace(/\?\w*page.size=\d+/gi, '');
        return pageURL;
    };
    //移除排序参数
    var removeSortParam = function (sortURL) {
        sortURL = sortURL.replace(/&\w*sort.*=((asc)|(desc))/gi, '');
        sortURL = sortURL.replace(/\?\w*sort.*=((asc)|(desc))&/gi, '?');
        sortURL = sortURL.replace(/\?\w*sort.*=((asc)|(desc))/gi, '');
        return sortURL;
    };
    //移除查询参数
    var removeSearchParam = function (url, form) {
        url = url.replace("?", "?1&")

        $.each(form.serializeArray(), function () {
            var name = this.name;
            if ($('[name="' + name + '"]').is(':input:hidden')) {
                return false;
            }
            url = url.replace(new RegExp(name + "=.*?\&", "g"), '');
            url = url.replace(new RegExp("[\&\?]" + name + "=.*$", "g"), '');
        });
        return url;
    };
    //移除backurl参数
    var removeBackURLParam = function (url) {
        url = url.replace(new RegExp("BackURL=.*?\&", "g"), '');
        url = url.replace(new RegExp("[\&\?]BackURL=.*$", "g"), '');
        return url;
    };

    var findSortParam = function (url) {
        var sortParam = '';
        if (!url) {
            url = window.location.href;
        }
        var results = url.match(/\w*(sort.*=((asc)|(desc)))/);
        if (results) {
            sortParam = results[1];
        }

        return sortParam;
    };

    var encodeBackURL = function (backUrl) {
        if (!backUrl) {
            var $form = $('form.form-search');
            var backUrlPrefix = removeSearchParam(removeBackURLParam(currentUrl), $form);
            backUrl = backUrlPrefix + (backUrlPrefix.indexOf("?") > 0 ? "&" : "?") + $form.find(":not(:hidden)").serialize()

        }
        return encodeURIComponent(backUrl);
    };


    //获取第一个选中行
    var getFirstSelectedCheckbox = function ($table, quiet) {
        var checkbox = $table.find('td.check input[type=checkbox]:checked');//:first
        if (!checkbox.length) {
            //静默模式不做任何提示
            if (quiet) {
                return checkbox;
            }

            warn('请先选择要操作的数据！');
            return [];
        }
        if (checkbox.size() > 1) {
            warn('只能选择一条记录进行操作！');
            return [];
        }
        return checkbox;
    };
    //获取全部选中行
    var getAllSelectedCheckbox = function ($table, quiet) {
        var checkbox = $table.find('td.check input[type=checkbox]:checked');
        if (!checkbox.length) {
            //静默模式不做任何提示
            if (quiet) {
                return checkbox;
            }

            warn('请先选择要操作的数据！');
        }
        return checkbox;
    };

    return {
        //表单验证初始化
        validate: {
            submiting: function () {
                return submiting;
            },
            validFiledTips: validFiledTips,
            init: function () {
                $.validator.setDefaults({
                    focusInvalid: true
                    , focusCleanup: true
                    , onclick: function (element) {
                        $(element).valid();
                    }
                    , onfocusin: function (element) {
                        $(element).valid();
                    }
                    , submitHandler: function (form) {
                        submiting = true;
                        if (form.onsubmit) {
                            if (form.onsubmit() !== false) {
                                waiting();
                                form.submit();
                            }
                        } else {
                            waiting();
                            form.submit();
                        }
                    }
                    , success: function (label, element) {
                        var fieldName = element.name;

                        if (validFiledTips[fieldName]) {
                            layer.close(validFiledTips[fieldName]);
                            validFiledTips[fieldName] = undefined;
                        }
                        $(element).removeClass('has-error').addClass('has-success');
                    }
                    , errorPlacement: function (error, element) {
                        if ($(error).html() == '') {
                            return true;
                        }
                        var point = 1;
                        var target = element;

                        var fieldName = element[0].name;

                        //如果存在label,则显示在label右侧.
                        var fieldLabel = $('label[for="' + fieldName + '"]');
                        if (fieldLabel.size() > 0) {
                            point = 2;
                            target = fieldLabel;
                        }

                        if (validFiledTips[fieldName]) {
                            $('#error-tips-' + fieldName).find('.error').html($(error).html());
                        } else {
                            validFiledTips[fieldName] = layer.tips($('<div></div>').append(error).html(), target, {
                                id: 'error-tips-' + fieldName
                                , tipsMore: true
                                , tips: [point, '#f98932']
                                , time: 0
                                , area: ['auto', '30px']
                                , topRevised: -6
                            });

                            $(element).removeClass('has-success').addClass('has-error');
                        }

                    },
                    invalidHandler: function (event, validator) {
                        // 'this' refers to the form
                        var errors = validator.numberOfInvalids();
                        if (errors) {
                            var message = errors == 1
                                ? '有一项表单验证未通过.'
                                : '有 ' + errors + ' 项表单验证未通过. 请处理后提交.';
                            warn(message);
                        } else {
                            $('div.error').hide();
                        }
                    }
                });
            }
        },
        // table组件工具
        // update to 2018-10-11 代码重构,使所有初始化范围固定在$table里,默认仅初始化当前选择器获取到的第一个table
        table: {
            resetTableResize: function (table) {
                new ResetTableResize(table, {
                    cidAttrName: 'data-tid'
                })
                window.location.reload();
            },
            //获取第一个选中行
            getFirstSelectedCheckbox: getFirstSelectedCheckbox,
            //获取全部选中行
            getAllSelectedCheckbox: getAllSelectedCheckbox,
            //初始化table
            init: function (table) {
                //根据选择器获取table
                var $table = $(table).eq(0);
                //如果table不存在则结束
                if ($table.size() == 0) return;


                //给table添加按钮和注册resize table
                if ($table.find('td.action').size() > 0 && $table.find('th.action').size() == 0) {
                    $table.find('thead tr').append('<th class="action">操作</th>');
                }

                //初始化排序和变更列宽支持
                if ($table.is(".table-sort")) {
                    new TableDragSortResize($table[0], {
                        cidAttrName: 'data-tid',
                        resizeable: true,
                        sort: {
                            callback: function (cell, type, event) {
                                if ($table.is(".table-ajax")) {
                                    //TODO 自行实现
                                    try {
                                        var isSort = tableAjaxSort(cell, type, event);
                                        if (!isSort) {
                                            return false;
                                        }
                                    } catch (e) {
                                        console.error("未实现 tableAjaxSort(cell, type, event) 方法.")
                                    }
                                } else {
                                    var ev = window.event || event;

                                    var sortName = $(cell).data('sort');
                                    if (sortName) {

                                        var softType = type == 'sort-down' ? 'desc' : 'asc';

                                        var currentSortParam = '';

                                        if ((ev.ctrlKey || ev.metaKey)) {
                                            currentSortParam = findSortParam().replace('sort.' + sortName + '=' + (softType == 'desc' ? 'asc' : 'desc'), '');
                                        }

                                        var sortParam = '?sort.' + sortName + '=' + softType + (currentSortParam.indexOf('sort') >= 0 ? ('&' + currentSortParam) : '');
                                        window.location.href = (sortParam + '&' + $('form.form-search').serialize())
                                    }
                                }
                            },
                            localEnable: false
                        }
                    });
                }

                var sortURL = currentUrl;
                $table.find('[data-sort]').each(function () {
                    var th = jQuery(this);
                    var sortPropertyName = 'sort.' + th.data('sort');

                    var matchResult = sortURL.match(new RegExp(sortPropertyName + '=(asc|desc)', 'gi'));
                    var order = null;

                    if (matchResult) {
                        order = RegExp.$1;

                        if (order == 'asc') {
                            th.addClass('sort-up')
                        } else if (order == 'desc') {
                            th.addClass('sort-down')
                        }
                    }

                });

                //Table init 开始
                $table.find('th input[type=checkbox],th div.checker').on('click', function () {
                    var $ck = $(this);
                    if (!$ck.is("[type=checkbox]")) {
                        $ck = $ck.find("[type=checkbox]");
                    }
                    $table.find('td.check input[type=checkbox]').prop('checked', $ck.is(':checked'));
                    $.uniform.update($table.find('td.check input[type=checkbox]'))
                });

                var beginCheck = undefined;
                //初始化表格快捷键
                $('body').keydown(function (event) {
                    var ev = window.event || event;
                    //shfit按住多选操作支持
                    if (ev.shiftKey) {
                        var $tr = $table.find('tr:hover.selected');

                        var check = $tr.find('td.check input[type=checkbox]');
                        if (check.size() != 0) {
                            beginCheck = $tr.find('td.check input[type=checkbox]');
                        } else {
                            beginCheck = 'no';
                        }
                        return true;
                    }

                    //shift+d 删除选中行
                    if (ev.shiftKey && (ev.key == 'D' || ev.key == 'd')) {
                        $('.btn.delete').click();
                        return true;
                    }
                    //当前如果有输入框在焦点状态则后续快捷键不生效
                    if ($(':input:focus').size() > 0) {
                        return true;
                    }

                    //无焦点输入框状态按e进入选中行编辑界面
                    if ((ev.key == 'e' || ev.keyCode == 13)) {
                        if ($('.btn.update').size() > 0) {
                            //如果存在修改按钮,则点击按钮
                            $('.btn.update').click();
                            return false;
                        }
                        return true;
                    }

                    if (((ev.keyCode == 38) || (ev.keyCode == 40))) {
                        var checkbox = getFirstSelectedCheckbox($table, true);
                        var focusInput = $(':input:focus').size() > 0;

                        //上下切换选中table行
                        if (!focusInput && (ev.keyCode == 38)) {
                            if (checkbox.size() > 0) {
                                checkbox.parents('tr').prev().find('td').eq(2).click()
                            } else {
                                $table.find('tbody tr:last').find('td').eq(2).click()
                            }
                            return false;
                        }
                        //上下切换选中table行
                        if (!focusInput && (ev.keyCode == 40)) {
                            if (checkbox.size() > 0) {
                                checkbox.parents('tr').next().find('td').eq(2).click()
                            } else {
                                $table.find('tbody tr:first').find('td').eq(2).click()
                            }
                            return false;
                        }
                    }
                });

                //如果没有数据自动添加无数据提示行
                if ($table.find('tbody tr').size() == 0) {
                    $('<tr></tr>').append($('<td align="center">暂无可查看数据</td>').attr('colspan', $('table.table-list thead th').size())).appendTo($table.find("tbody:not(.nullable)"));
                    return;
                }
                //注册点击行选中当前行

                $table.on('click', 'tbody:not(.notc)>tr>td:not(.check):not(.action)', function (event) {
                    var $current = $(this);
                    setTimeout(function () {
                        var selectTR = $current.parents('tr');
                        if (beginCheck == 'no') {
                            beginCheck = $('tr.selected').find('td.check input[type=checkbox]');
                        }
                        if (selectTR.is(".selected")) {
                            selectTR.removeClass("selected");
                        } else {
                            selectTR.addClass('selected').siblings().removeClass('selected');
                        }

                        var tdCheckBox = selectTR.find('td.check').find('input[type=checkbox]');

                        if (tdCheckBox.size() > 0) {
                            var checked = tdCheckBox.is(':checked');

                            var ev = window.event || event;
                            if (ev.shiftKey) {
                                var beginChecked = beginCheck.is(':checked');

                                if (checked != beginChecked) {
                                    tdCheckBox.prop('checked', !checked);
                                    var beginIndex = beginCheck.parents('tr').index();
                                    var endIndex = tdCheckBox.parents('tr').index();
                                    if (beginIndex > endIndex) {
                                        var tem = endIndex;
                                        endIndex = beginIndex;
                                        beginIndex = tem;
                                    }
                                    for (var i = beginIndex + 1; i <= endIndex; i++) {
                                        $('tr').eq(i).find('td.check input[type=checkbox]').prop('checked', !checked);
                                        if (!checked) {
                                            $('tr').eq(i).addClass("selected");
                                        }
                                    }
                                }
                                $.uniform.update($('td.check input[type=checkbox]'));
                                beginCheck = undefined;
                                return false
                            }

                            beginCheck = undefined;

                            if (ev.ctrlKey) {
                                tdCheckBox.prop('checked', !checked);
                                $.uniform.update($('td.check input[type=checkbox]'))
                                return false
                            }

                            tdCheckBox.prop('checked', !checked).parents('tr').siblings().find('td.check input[type=checkbox]').prop('checked', false);
                            $.uniform.update($('td.check input[type=checkbox]'))
                            return false;
                        }
                    }, 50)

                }).on('dblclick', 'tbody:not(.notdbc)>tr>td:not(.check):not(.action)', function (event) {
                    //如果存在修改按钮,则双击条进入修改页面
                    var updateUrl = $('.btn.update').data('baseurl');
                    if (updateUrl) {
                        var selectTR = $(this).parents('tr');
                        var tdCheckBox = selectTR.find('td.check').find('input[type=checkbox]');
                        window.location.href = updateUrl.replace('{id}', tdCheckBox.val()) + (updateUrl.indexOf('?') > 0 ? '&' : '?') + 'BackURL=' + encodeBackURL();
                    }

                });
                //注册toolbar按钮事件,忽略custom的按钮
                $('.tools a.btn:not(.custom)').on('click', function (event) {
                    if ($(this).prop('href')) return;
                    var baseUrl = $(this).data('baseurl');
                    if (!baseUrl) return;

                    var $tr = $('tr td.check input:checked');

                    if ($tr.length) {
                        var checkItemVal = $tr.val();

                        if ($(this).hasClass('batch')) {
                            checkItemVal = '';
                            $tr.each(function () {
                                checkItemVal = checkItemVal + $(this).val() + ',';
                            });
                            checkItemVal = checkItemVal.substring(0, checkItemVal.length - 1);
                        } else {
                            if ($tr.length > 1) {
                                warn('仅可选择一条记录.');
                                return false;
                            }
                        }

                        if ($(this).hasClass('delete')) {
                            confirm({
                                message: '将要执行删除数据操作,是否继续?', yes: function () {
                                    window.location.href = baseUrl.replace('{id}', checkItemVal) + (baseUrl.indexOf('?') > 0 ? '&' : '?') + 'BackURL=' + encodeBackURL();
                                }
                            });

                            return false;
                        }
                        var confirmMessage = $(this).data("confirm");
                        if (confirmMessage) {
                            confirm({
                                message: confirmMessage, yes: function () {
                                    window.location.href = baseUrl.replace('{id}', checkItemVal) + (baseUrl.indexOf('?') > 0 ? '&' : '?') + 'BackURL=' + encodeBackURL();
                                }
                            });
                        } else {
                            window.location.href = baseUrl.replace('{id}', checkItemVal) + (baseUrl.indexOf('?') > 0 ? '&' : '?') + 'BackURL=' + encodeBackURL();
                        }
                    } else {
                        warn('请选择一条记录.');
                    }
                });
                //删除行事假注册
                $('.action a.deleteRow').click(function () {
                    var deleteAction = this;
                    confirm({
                        message: '将要执行删除数据操作,是否继续?', yes: function () {
                            window.location.href = deleteAction.href;
                        }
                    });
                    return false;
                });
                //Table init 结束

            },
        },
        //页面导航条相关封装
        place: {
            //添加导航节点
            appendUrl: function (title, url, param) {
                var item = $("<li><i class=\"fa fa-angle-right\"></i><a target='rightFrame' href='" + url + '?' + param + "'>" + title + "</a></li>");

                var exist = false;
                $(".placeul", top.document).find("a").each(function (i, o) {
                    if (o.href.indexOf(url.substring(0, url.length - 1)) != -1) {
                        $(o).parent("li").nextAll().remove();
                        if (param) {
                            $(o).attr("href", url + '?' + param)
                        }
                        exist = true;
                        return true;
                    }
                });
                if (exist) {
                    return;
                }

                $(".placeul", top.document).append(item);
            },
            //单纯添加节点
            append: function (item) {
                $(".placeul", top.document).append(item);
            },
            //后退一个节点
            back: function () {
                $(".placeul li:not(.default):last", top.document).remove()
            },
            //重置当前导航节点
            change: function (item) {
                $(".placeul", top.document).append(item);
            },
            //清空导航节点
            clean: function () {
                $(".placeul li:not(.default)", top.document).remove()
            },
            //获取当前导航节点钱一个节点地址
            preUrl: function () {
                return $(".placeul li:not(.default):last", top.document).prev().find("a").attr("href");
            },
            //获取默认导航节点地址
            defaultUrl: function () {
                return $(".placeul li.default", top.document).find("a").attr("href");
            }


        },
        //工具
        tools: {
            //生成uuid
            uuid: function (len, radix) {
                var chars = '0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz'.split('');
                var uuid = [], i;
                radix = radix || chars.length;

                if (len) {
                    // Compact form
                    for (i = 0; i < len; i++) uuid[i] = chars[0 | Math.random() * radix];
                } else {
                    // rfc4122, version 4 form
                    var r;

                    // rfc4122 requires these characters
                    uuid[8] = uuid[13] = uuid[18] = uuid[23] = '-';
                    uuid[14] = '4';

                    // Fill in random data.  At i==19 set the high bits of clock sequence as
                    // per rfc4122, sec. 4.1.5
                    for (i = 0; i < 36; i++) {
                        if (!uuid[i]) {
                            r = 0 | Math.random() * 16;
                            uuid[i] = chars[(i == 19) ? (r & 0x3) | 0x8 : r];
                        }
                    }
                }

                return uuid.join('');
            },
            //用户选择组件
            chooseUser: function (options) {

                var configs = {
                    width: '600px',
                    height: '500px',
                    multi: false,
                    okBtn: "确定",
                    cancelBtn: "取消",
                    callback: $.noop
                };

                configs = jQuery.extend(configs, options);

                //iframe窗
                top.layer.open({
                    id: 'user_choose',
                    type: 2,
                    title: '用户选择器',
                    shadeClose: true,
                    shade: 0.3,
                    maxmin: true, //开启最大化最小化按钮
                    area: [configs.width, configs.height],
                    content: _ctx + '/sys/common/userChoose'
                    , btn: [configs.okBtn, configs.cancelBtn]
                    , btn1: function (index, layero) {
                        var results = undefined;

                        if (configs.multi) {
                            results = getAllSelectedCheckbox($("table", $("#user_choose iframe", top.document)[0].contentWindow.document))
                        } else {
                            results = getFirstSelectedCheckbox($("table", $("#user_choose iframe", top.document)[0].contentWindow.document))
                        }

                        if (!results || results.size() === 0) {
                            warn("请选择用户后确认...")
                        } else {
                            configs.callback(results.eq(0).data("show"), results);
                            top.layer.close(index)
                        }
                    }
                });
            },
            //文件上传组件
            uploadFile: function (options) {
                options = options || {};

                var configs = {
                    width: '750px',
                    height: '550px',
                    multi: false,
                    okBtn: '关闭',
                    callback: $.noop,
                    identity: "common",
                    secondIdentity: "common",
                    tag: 'all'
                };

                configs = jQuery.extend(configs, options);

                //iframe窗
                top.layer.open({
                    id: 'file_upload_dialog',
                    type: 2,
                    title: '文件上传',
                    shadeClose: false,
                    shade: 0.3,
                    maxmin: false, //开启最大化最小化按钮
                    area: [configs.width, configs.height],
                    content: _ctx + '/filestore/upload?identity=' + configs.identity + '&secondIdentity=' + configs.secondIdentity + '&tag=' + configs.tag
                    , btn: [configs.okBtn]
                    , success: function (layero, index) {
                        //获取frame,并且传入参数
                        var childFrameWindow = top.layer.getChildFrameWindow(index)
                        childFrameWindow.param = {
                            parentWindow: window,
                            callback: configs.callback
                        }
                    }
                });
            },
        },
        //url操作工具
        urlTools: {
            queryString: function (name) {
                var reg = new RegExp("(^|&)" + name + "=([^&]*)(&|$)", "i");
                var reg_rewrite = new RegExp("(^|/)" + name + "/([^/]*)(/|$)", "i");
                var r = window.location.search.substr(1).match(reg);
                var q = window.location.pathname.substr(1).match(reg_rewrite);
                if (r != null) {
                    return unescape(r[2]);
                } else if (q != null) {
                    return unescape(q[2]);
                } else {
                    return null;
                }
            },
            //重置排序
            resetSortUrl: function (url) {
                if (!url) {
                    url = currentUrl;
                }

                window.location.href = removePageParam(removeSortParam(url));
            },
            //重置查询参数
            resetSearchParamUrl: function (url, form) {
                if (!url) {
                    url = currentUrl;
                }

                if (!form) {
                    form = $("form.form-search")
                }
                window.location.href = removePageParam(removeSearchParam(url, form));
            },
            //获取排序参数
            findSortParam: findSortParam,

            //格式化url前缀，默认清除所有?后面的参数
            formatUrlPrefix: function (urlPrefix, $table) { //格式化url前缀，默认清除url ? 后边的

                if (!urlPrefix) {
                    urlPrefix = $table.data("prefix-url");
                }

                if (!urlPrefix) {
                    urlPrefix = currentUrl;
                }

                if (urlPrefix.indexOf("?") >= 0) {
                    return urlPrefix.substr(0, urlPrefix.indexOf("?"));
                }
                return urlPrefix;
            },
            //对backurl参数进行url编码
            encodeBackURL: encodeBackURL,
        },
        //toolbar 操作
        toolbar: {
            addMoreDivider: function () {
                var $toolsMore = $('.toolbar .more');
                if ($toolsMore.is(':hidden')) {
                    $toolsMore.removeClass('hidden');
                }

                var $toolsMoreList = $toolsMore.find('.more_list');

                $('<li class="divider"></li>').insertBefore($toolsMoreList, null)
            },
            //在更多按钮中添加操作项
            addMore: function (options) {
                var $toolsMore = $('.toolbar .more');
                if ($toolsMore.is(':hidden')) {
                    $toolsMore.removeClass('hidden');
                }

                var $toolsMoreList = $toolsMore.find('.more_list');

                var opts = $.extend({
                    classText: '',
                    href: 'javascript:;',
                    text: '未命名',
                    icon: ''
                }, options);

                $('<li><a class="' + opts.classText + '" href="' + opts.href + '"><i class="' + opts.icon + '"></i>' + opts.text + '</a></li>').insertBefore($toolsMoreList, null)

            }
        },
        //初始化推送通知
        sysNotice: {
            init: initNotice,
            flush: flushPolling
        },
        scrollTo: function (el, offeset) {
            var pos = (el && el.size() > 0) ? el.offset().top : 0;

            if (el) {
                if ($('body').hasClass('page-header-fixed')) {
                    pos = pos - $('.page-header').height();
                } else if ($('body').hasClass('page-header-top-fixed')) {
                    pos = pos - $('.page-header-top').height();
                } else if ($('body').hasClass('page-header-menu-fixed')) {
                    pos = pos - $('.page-header-menu').height();
                }
                pos = pos + (offeset ? offeset : -1 * el.height());
            }

            $('html,body').animate({
                scrollTop: pos
            }, 'slow');
        },
        //表单加工美化
        handleUniform: handleUniform,
        //初始化自动补全
        initAutocomplete: initAutocomplete,
        //初始化时间选择
        initDatePick: initDatePick,
        //安全刷新页面
        refreshPage: refreshPage,
        //关闭当前所在弹出层
        closeme: closeme,
        //loading
        waiting: waiting,
        //loading 结束
        waitingOver: waitingOver,
        //消息
        info: info,
        //警告消息
        warn: warn,
        //成功消息
        success: success,
        //错误消息
        error: error,
        //确认操作消息
        confirm: confirm,
        //使用引导
        guide: {
            form: function (joyrideKey, joyRideTip, review) {
                if (!joyRideTip) {
                    joyRideTip = "#joyRideTipContent";
                }
                if (!review) {
                    review = ".portlet-title span";
                }
                $(joyRideTip).joyride({
                    autoStart: true,
                    // modal: true,
                    expose: true,
                    localStorage: true,
                    localStorageKey: joyrideKey,
                    postRideCallback: function (index, tip) {
                        $('.scroll-to-top').click();
                        $cy.info("可以通过点击表单标题看引导信息！", function (index, layero) {
                            top.layer.close(index);
                            setTimeout(function () {
                                $('.tooltips').tooltip('show');
                            }, 0);

                        });
                    }
                });
                $(review)
                    .addClass("tooltips")
                    .attr("data-placement", "right")
                    .attr("data-original-title", "点击查看引导")
                    .click(function () {
                        localStorage.removeItem(joyrideKey);
                        $(joyRideTip).joyride("destroy");
                        $cy.guide.form(joyrideKey, joyRideTip);
                    });
            },
            //在更多按钮中添加操作项
            list: function (joyrideKey, joyRideTip) {
                console.debug("guide for list")

                if (!joyRideTip) {
                    joyRideTip = "#joyRideTipContent";
                    $("body").append("<ol id=\"joyRideTipContent\" class=\"hidden\">\n" +
                        "    <li data-text=\"开始\" class=\"custom\" data-options=\"tipAnimation:fade\">\n" +
                        "        <h4>新手引导</h4>\n" +
                        "        <p>点击开始，了解此界面使用方式。</p>\n" +
                        "    </li>\n" +
                        "    <li data-target=\".toolbar\" data-button=\"下一项\" data-options=\"tipLocation:right\">\n" +
                        "        <h4>表格操作按钮区</h4>\n" +
                        "        <p>对表格数据进行操作，添加、修改、删除、更多（导出数据、特有功能）等操作。</p>\n" +
                        "    </li>\n" +
                        "    <li data-target=\".toolbar-right .config\" data-button=\"下一项\" data-options=\"tipLocation:left\">\n" +
                        "        <h4>设置按钮</h4>\n" +
                        "        <p>辅助功能折叠按钮，一些辅助功能集合。</p>\n" +
                        "    </li>\n" +
                        "    <li data-target=\".form-search\" data-button=\"下一项\" data-options=\"tipLocation:top\">\n" +
                        "        <h4>条件检索区</h4>\n" +
                        "        <p>可以通过输入检索条件，筛选列表数据。</p>\n" +
                        "    </li>\n" +
                        "    <li data-target=\".listTableWrap\" data-button=\"下一项\" data-options=\"tipLocation:top\">\n" +
                        "        <h4>数据表格区域</h4>\n" +
                        "        <p>列表数据区域，可通过点击表头对数据进行排序，拖动表头调整列宽；可以通过双击行进入详情\\编辑界面。</p>\n" +
                        "    </li>\n" +
                        "</ol>");
                }
                $(joyRideTip).joyride({
                    autoStart: true,
                    modal: true,
                    expose: true,
                    localStorage: true,
                    localStorageKey: joyrideKey,
                    postRideCallback: function (index, tip) {
                        $('.scroll-to-top').click();
                        $cy.info("可以通过点击右上角 “设置” 按钮中的 “查看引导” 选项，查看引导！", function (index, layero) {
                            top.layer.close(index);
                        });
                    }
                });

                $(".toolbar-right .config .more_list").append("<a class='open-guide'>查看引导</a>");
                $(".open-guide").click(function () {
                    localStorage.removeItem(joyrideKey);
                    $(joyRideTip).joyride("destroy");
                    $cy.guide.list(joyrideKey);
                });
            }
        }
    }

}();


$.fn.extend({
    "preventScroll": function () {
        $(this).each(function () {
            var _this = this;
            if (navigator.userAgent.indexOf('Firefox') >= 0) {   //firefox
                _this.addEventListener('DOMMouseScroll', function (e) {
                    _this.scrollTop += e.detail > 0 ? 60 : -60;
                    e.preventDefault();
                }, false);
            } else {
                var startY = endY = scrollTop = 0;
                var preventStart = false;
                _this.addEventListener('touchstart', function (e) {
                    preventStart = true;
                    scrollTop = _this.scrollTop;
                    var touch = event.targetTouches[0];
                    startY = touch.pageY;
                    e.preventDefault();
                });
                _this.addEventListener('touchmove', function (e) {
                    if (!preventStart) return;
                    var touch = event.targetTouches[0];
                    endY = touch.pageY;
                    _this.scrollTop = scrollTop + (endY - startY) * -1;
                    e.preventDefault();
                });
                _this.addEventListener('touchend', function () {
                    preventStart = false;
                });
                _this.onmousewheel = function (e) {
                    e = e || window.event;
                    _this.scrollTop += e.wheelDelta > 0 ? -60 : 60;
                    return false;
                };
            }
        })
    }
});

// Handles the go to top button at the footer
var handleGoTop = function () {
    var offset = 300;
    var duration = 500;

    if (navigator.userAgent.match(/iPhone|iPad|iPod/i)) {  // ios supported
        $(window).bind("touchend touchcancel touchleave", function (e) {
            if ($(this).scrollTop() > offset) {
                $('.scroll-to-top').fadeIn(duration);
            } else {
                $('.scroll-to-top').fadeOut(duration);
            }
        });
    } else {  // general
        $(window).scroll(function () {
            if ($(this).scrollTop() > offset) {
                $('.scroll-to-top').fadeIn(duration);
            } else {
                $('.scroll-to-top').fadeOut(duration);
            }
        });
    }

    $('.scroll-to-top').click(function (e) {
        e.preventDefault();
        $('html, body').animate({scrollTop: 0}, duration);
        return false;
    });
};

// Handles Bootstrap Tooltips.
var handleTooltips = function () {
    // global tooltips
    $('.tooltips').tooltip();

    // portlet tooltips
    $('.portlet > .portlet-title .fullscreen').tooltip({
        container: 'body',
        title: 'Fullscreen'
    });
    $('.portlet > .portlet-title > .tools > .reload').tooltip({
        container: 'body',
        title: 'Reload'
    });
    $('.portlet > .portlet-title > .tools > .remove').tooltip({
        container: 'body',
        title: 'Remove'
    });
    $('.portlet > .portlet-title > .tools > .config').tooltip({
        container: 'body',
        title: 'Settings'
    });
    $('.portlet > .portlet-title > .tools > .collapse, .portlet > .portlet-title > .tools > .expand').tooltip({
        container: 'body',
        title: 'Collapse/Expand'
    });
};

//=================================
//当前是否在iframe中
var isFrame = top != window;
//获取当前显示内容页面
var contextWindow = top.document.getElementById('rightFrame') ? top.document.getElementById('rightFrame').contentWindow : top;
var contextDocument = contextWindow.document;

//获取url相关信息
urlPrefix = currentUrl = window.location.href;
urlSuffix = '';
if (currentUrl.indexOf('?') > 0) {
    urlPrefix = currentUrl.substring(0, currentUrl.indexOf('?'));
    urlSuffix = currentUrl.substring(currentUrl.indexOf('?') + 1);
}

jQuery(document).ready(function () {
    //注册通用快捷键
    jQuery("body").keydown(function (event) {
        // console.log(ev.keyCode, ev.key)
        var ev = window.event || event;
        var code = ev.keyCode || ev.which;

        if (code == 116) {
            // 阻止默认的F5事件
            if (ev.preventDefault) {
                ev.preventDefault();
            } else {
                ev.keyCode = 0;
                ev.returnValue = false;
            }
            $cy.refreshPage();
            return false;
        }
        //空格快速关闭提示框
        if ((ev.keyCode == 32)) {
            console.debug("空格关闭提示")
            var $btn = top.$('.layui-layer[type=dialog]:last .layui-layer-btn').children('a,input,button').eq(0);

            if ($btn.size() <= 0) {
                $btn = $('.layui-layer[type=dialog]:last .layui-layer-btn').children('a,input,button').eq(0);
            }

            if ($btn.size() > 0) {
                $btn.click();
                return false;
            }

            return true;
        }
        //ctrl+回车|s 或cmd+回车|s 提交表单
        if ((ev.ctrlKey || ev.metaKey) && (ev.keyCode == 13 || ev.key == 's' || ev.key == 'S')) {
            if ($('#inputForm').size() > 0) {
                // fixbug to 2018-10-11 快捷键提交表单的onsubmit事件支持修复
                if ($('#inputForm').size() > 0 && $('#inputForm')[0].onsubmit) {
                    if ($('#inputForm')[0].onsubmit() !== false) {
                        $('#inputForm').submit();
                    }
                } else {
                    $('#inputForm').submit();
                }

                return false;
            }
            return true;
        }
        var focusInput = $(':input:focus').size() > 0;
        if (focusInput) {
            return true;
        }
        //无焦点状态输入框 'Backspace' 返回上一层
        if (!focusInput && (ev.key == 'Backspace' || ev.key == 'z')) {
            if ($cy.place.preUrl()) {
                if (isFrame) {
                    //
                    contextDocument.location.href = $cy.place.preUrl();
                    return false;
                }
            }
            return true;
        }
        return true;
    });

    //查询框中内容有变化未提交查询时变色.
    $("form.form-search :input").change(function () {
        $("form.form-search button[type=submit]").addClass("blue");
    })

    $("form.form-search").submit(function () {
        var pageSize = $cy.urlTools.queryString("page.size");
        if (pageSize) {
            $(this).append("<input type='hidden' name='page.size' value='" + pageSize + "'>")
        }
        return true;
    })
});


//初始化页面
$(function () {
    $(".data-ajax").click(function () {
        console.debug("ajax 提交...", this);
        var href = $(this).data("ajax-href");
        var callback = $(this).data("ajax-callback")
        $.ajax({
            type: "get",
            cache: false,
            url: href,
            dataType: "text",
            success: function (data) {
                eval(callback);
            }
        })
    })

    $(".data-select").click(function () {
        var href = $(this).data("ajax-href");
        top.layer.open({
            id: 'data-select',
            type: 2,
            title: '数据列查询',
            shadeClose: false,
            shade: 0.3,
            maxmin: false,
            area: ['893px', '600px'],
            content: href,
            btn: ['确定'],
            yes: function (index, layero) {
                var childFrameWindow = top.layer.getChildFrameWindow(index)
                childFrameWindow.sub(index);
            }

        });
    })

    //锁屏加载
    window.onbeforeunload = function () {
        if (!$cy.validate.submiting()) {
            setTimeout(function () {
                console.debug("加载页面锁屏状态", $cy.validate.submiting());
                top.$cy.waiting("", 10000);
            }, 800);
        }
    };

    $cy.handleUniform();
    $cy.initDatePick();
    handleGoTop();
    handleTooltips();


    //需要直接初始化的.
    if ($.validator) {
        $cy.validate.init();
    }

    $cy.table.init('table.table-list');

    //点击左树后将焦点切换到table
    setTimeout(function () {
        if ($("#inputForm").size() == 0) {
            $('a:first').focus().blur();
        } else {
            // 为了能快速返回,暂时禁用第一个可输入框获取焦点
            // $("#inputForm input:visible:not([readonly],[disabled])").eq(0).focus()
        }
    }, 100);

    if (parent !== top && top.$cy) {
        setTimeout(function () {
            top.$cy.waitingOver();
        }, 800);
    }
});
