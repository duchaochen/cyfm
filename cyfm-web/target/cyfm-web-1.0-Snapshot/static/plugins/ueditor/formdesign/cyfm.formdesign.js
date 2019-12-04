/*
 * 设计器私有的配置说明
 * 一
 * UE.formDesignUrl  插件路径
 *
 * 二
 *UE.getEditor('myFormDesign',{
 *          designer:true,//是否显示，设计器的清单 tools
 */
UE.formDesignUrl = 'formdesign';

/**
 * 文本框
 * @command textfield
 * @method execCommand
 * @param { String } cmd 命令字符串
 * @example
 * ```javascript
 * editor.execCommand( 'textfield');
 * ```
 */
function countSubstr(str, substr) {
    var count;
    var reg = "/" + substr + "/gi";    //查找时忽略大小写
    reg = eval(reg);
    if (str.match(reg) == null) {
        count = 0;
    } else {
        count = str.match(reg).length;
    }
    return count;

    //返回找到的次数
}

function functool(thePlugins) {
    var dialog = new UE.ui.Dialog({
        iframeUrl: UE.getEditor('myFormDesign').options.UEDITOR_HOME_URL + UE.formDesignUrl + '/html/funtool.html?node=' + thePlugins,
        name: "funtool",
        editor: UE.getEditor('myFormDesign'),
        title: '控件事件',
        cssRules: "width:600px;height:310px;",
        buttons: [
            {
                className: 'edui-cancelbutton',
                label: '关闭',
                onclick: function () {
                    dialog.close(false);
                }
            }]
    });
    dialog.render();
    dialog.open();
}

UE.plugins['text'] = function () {
    var me = this, thePlugins = 'text';
    me.commands[thePlugins] = {
        execCommand: function (t, type) {
            var dialog = new UE.ui.Dialog({
                iframeUrl: this.options.UEDITOR_HOME_URL + UE.formDesignUrl + '/template/html/text.html',
                name: thePlugins,
                editor: this,
                title: '文本框',
                cssRules: "width:750px;height:450px;",
                buttons: [
                    {
                        className: 'edui-okbutton',
                        label: '确定',
                        onclick: function (event) {
                            //TODO 添加文本框验证
                            dialog.close(true);
                        }
                    },
                    {
                        className: 'edui-cancelbutton',
                        label: '取消',
                        onclick: function () {
                            dialog.close(false);
                        }
                    }]
            });
            dialog.render();
            dialog.open();
        }
    };

    var popup = new baidu.editor.ui.Popup({
        editor: this,
        content: '',
        className: 'edui-bubble',
        _edittext: function () {
            baidu.editor.plugins[thePlugins].editdom = popup.anchorEl;
            me.execCommand(thePlugins, 1);
            this.hide();
        },
        _delete: function () {
            if (window.confirm('确认删除该控件吗？')) {
                baidu.editor.dom.domUtils.remove(this.anchorEl, false);
            }
            this.hide();
        },
        //添加控件事件
        _addfun: function () {
            baidu.editor.plugins[thePlugins].funtool = popup.anchorEl;
            functool(thePlugins);
            this.hide();
        }
    });
    popup.render();
    me.addListener('mouseover', function (t, evt) {
        evt = evt || window.event;
        var el = evt.target || evt.srcElement;
        var designerPlugins = el.getAttribute('plugins');
        if (/input/ig.test(el.tagName) && designerPlugins == thePlugins) {
            var html = popup.formatHtml(
                '<nobr>文本框: <span onclick=$$._edittext() class="edui-clickable">编辑</span>&nbsp;&nbsp;<span onclick=$$._delete() class="edui-clickable">删除</span>' +
                '&nbsp;&nbsp<span onclick=$$._addfun() class="edui-clickable">控件事件</span></nobr>');
            if (html) {
                popup.getDom('content').innerHTML = html;
                popup.anchorEl = el;
                popup.showAnchor(popup.anchorEl);
            } else {
                popup.hide();
            }
        }
    });
};
/** begin label **/
UE.plugins['label'] = function () {
    var me = this, thePlugins = 'label';
    me.commands[thePlugins] = {
        execCommand: function (t, type) {
            var dialog = new UE.ui.Dialog({
                iframeUrl: this.options.UEDITOR_HOME_URL + UE.formDesignUrl + '/template/html/label.html',
                name: thePlugins,
                editor: this,
                title: '标签',
                cssRules: "width:600px;height:310px;",
                buttons: [
                    {
                        className: 'edui-okbutton',
                        label: '确定',
                        onclick: function (event) {
                            //TODO Ok事件处理,验证
                        }
                    },
                    {
                        className: 'edui-cancelbutton',
                        label: '取消',
                        onclick: function () {
                            dialog.close(false);
                        }
                    }]
            });
            dialog.render();
            dialog.open();
        }
    };

    var popup = new baidu.editor.ui.Popup({
        editor: this,
        content: '',
        className: 'edui-bubble',
        _edittext: function () {
            baidu.editor.plugins[thePlugins].editdom = popup.anchorEl;
            me.execCommand(thePlugins, 1);
            this.hide();
        },
        _delete: function () {
            if (window.confirm('确认删除该控件吗？')) {
                baidu.editor.dom.domUtils.remove(this.anchorEl, false);
            }
            this.hide();
        },
        //添加控件事件
        _addfun: function () {
            baidu.editor.plugins[thePlugins].funtool = popup.anchorEl;
            functool(thePlugins);
            this.hide();
        }
    });
    popup.render();
    me.addListener('mouseover', function (t, evt) {
        evt = evt || window.event;
        var el = evt.target || evt.srcElement;
        var designerPlugins = el.getAttribute('plugins');
        if (/label/ig.test(el.tagName) && designerPlugins == thePlugins) {
            var html = popup.formatHtml(
                '<nobr>标签: <span onclick=$$._edittext() class="edui-clickable">编辑</span>&nbsp;&nbsp;<span onclick=$$._delete() class="edui-clickable">删除</span>' +
                '&nbsp;&nbsp<span onclick=$$._addfun() class="edui-clickable">控件事件</span></nobr>');
            if (html) {
                popup.getDom('content').innerHTML = html;
                popup.anchorEl = el;
                popup.showAnchor(popup.anchorEl);
            } else {
                popup.hide();
            }
        }
    });
};
/** end label **/


/** begin signature  **/
UE.plugins['signature'] = function () {
    var me = this, thePlugins = 'signature';
    me.commands[thePlugins] = {
        execCommand: function (t, type) {
            var dialog = new UE.ui.Dialog({
                iframeUrl: this.options.UEDITOR_HOME_URL + UE.formDesignUrl + '/template/html/signature.html',
                name: thePlugins,
                editor: this,
                title: '签章控件',
                cssRules: "width:600px;height:310px;",
                buttons: [
                    {
                        className: 'edui-okbutton',
                        label: '确定',
                        onclick: function (event) {
                            var orgname = $("iframe[src='" + dialog.iframeUrl + "']")[0].contentWindow.document.getElementById("orgname").value;
                            var isChange = $("iframe[src='" + dialog.iframeUrl + "']")[0].contentWindow.document.getElementById("isChange").value;

                            /**新建*/

                            var count = countSubstr(window.parent.mmEditor.getContent(), "name=\"" + orgname + "\"");

                            if (type == undefined) {
                                if (orgname != "" && count != 0) {
                                    bootbox.alert("此字段已被使用");
                                    return false;
                                } else {
                                    dialog.close(true);
                                }
                            }
                            if (type == 1) {   //修改
                                if (orgname != "" && count > 0 && orgname != isChange) {
                                    bootbox.alert("此字段已被使用");
                                    return false;
                                } else {
                                    dialog.close(true);
                                }
                            }
                        }
                    },
                    {
                        className: 'edui-cancelbutton',
                        label: '取消',
                        onclick: function () {
                            dialog.close(false);
                        }
                    }]
            });
            dialog.render();
            dialog.open();
        }
    };

    var popup = new baidu.editor.ui.Popup({
        editor: this,
        content: '',
        className: 'edui-bubble',
        _edittext: function () {
            baidu.editor.plugins[thePlugins].editdom = popup.anchorEl;
            me.execCommand(thePlugins, 1);
            this.hide();
        },
        _delete: function () {
            if (window.confirm('确认删除该控件吗？')) {
                baidu.editor.dom.domUtils.remove(this.anchorEl, false);
            }
            this.hide();
        },
        //添加控件事件
        _addfun: function () {
            baidu.editor.plugins[thePlugins].funtool = popup.anchorEl;
            functool(thePlugins);
            this.hide();
        }
    });
    popup.render();
    me.addListener('mouseover', function (t, evt) {
        evt = evt || window.event;
        var el = evt.target || evt.srcElement;
        var designerPlugins = el.getAttribute('plugins');
        if (/label/ig.test(el.tagName) && designerPlugins == thePlugins) {
            var html = popup.formatHtml(
                '<nobr>标签: <span onclick=$$._edittext() class="edui-clickable">编辑</span>&nbsp;&nbsp;<span onclick=$$._delete() class="edui-clickable">删除</span>' +
                '&nbsp;&nbsp<span onclick=$$._addfun() class="edui-clickable">控件事件</span></nobr>');
            if (html) {
                popup.getDom('content').innerHTML = html;
                popup.anchorEl = el;
                popup.showAnchor(popup.anchorEl);
            } else {
                popup.hide();
            }
        }
    });
};
/** end signature **/
/**
 * 单选框组
 * @command radios
 * @method execCommand
 * @param { String } cmd 命令字符串
 * @example
 * ```javascript
 * editor.execCommand( 'radio');
 * ```
 */
UE.plugins['radios'] = function () {
    var me = this, thePlugins = 'radios';
    me.commands[thePlugins] = {
        execCommand: function (t, type) {
            var dialog = new UE.ui.Dialog({
                iframeUrl: this.options.UEDITOR_HOME_URL + UE.formDesignUrl + '/template/html/radios.html',
                name: thePlugins,
                editor: this,
                title: '单选框组',
                cssRules: "width:590px;height:430px;",
                cssRules: "width:590px;height:430px;",
                buttons: [
                    {
                        className: 'edui-okbutton',
                        label: '确定',
                        onclick: function () {
                            var orgname = $("iframe[src='" + dialog.iframeUrl + "']")[0].contentWindow.document.getElementById("orgname").value;
                            var isChange = $("iframe[src='" + dialog.iframeUrl + "']")[0].contentWindow.document.getElementById("isChange").value;
                            var dataType = $("iframe[src='" + dialog.iframeUrl + "']")[0].contentWindow.document.getElementById("dataType").value;
                            /**新建*/
                            var count = countSubstr(window.parent.mmEditor.getContent(), "name=\"" + orgname + "\"");
                            if (type == undefined) {
                                if (count != 0) {
                                    bootbox.alert("此字段已被使用");
                                    return false;
                                }
                            }
                            /**修改*/
                            if (type == 1) {
                                if (count > 0 && orgname != isChange) {
                                    bootbox.alert("此字段已被使用");
                                    return false;
                                }
                            }
                            var s = /^[\-\+]?((([0-9]{1,3})([,][0-9]{3})*)|([0-9]+))?([\.]([0-9]+))?$/;
                            if (dataType == "number" || dataType == "dbnumber") {
                                var options = $("iframe[src='" + dialog.iframeUrl + "']")[0].contentWindow.$("#options_table");
                                var result = true;
                                options.find(":text").each(function (index, element) {
                                    if (!s.test($(this).val())) {
                                        alert($(this).val() + "字段类型不匹配");
                                        result = false;
                                    }
                                });
                                if (result == true) {
                                    dialog.close(true);
                                } else {
                                    return false;
                                }

                            } else {
                                dialog.close(true);
                            }
                        }
                    },
                    {
                        className: 'edui-cancelbutton',
                        label: '取消',
                        onclick: function () {
                            dialog.close(false);
                        }
                    }]
            });
            dialog.render();
            dialog.open();
        }
    };
    var popup = new baidu.editor.ui.Popup({
        editor: this,
        content: '',
        className: 'edui-bubble',
        _edittext: function () {
            baidu.editor.plugins[thePlugins].editdom = popup.anchorEl;
            me.execCommand(thePlugins, 1);
            this.hide();
        },
        _delete: function () {
            if (window.confirm('确认删除该控件吗？')) {
                baidu.editor.dom.domUtils.remove(this.anchorEl, false);
            }
            this.hide();
        },
        //添加控件事件
        _addfun: function () {
            baidu.editor.plugins[thePlugins].funtool = popup.anchorEl;
            functool(thePlugins);
            this.hide();
        }
    });
    popup.render();
    me.addListener('mouseover', function (t, evt) {
        evt = evt || window.event;
        var el = evt.target || evt.srcElement;
        var designerPlugins = el.getAttribute('plugins');
        if (/span/ig.test(el.tagName) && designerPlugins == thePlugins) {
            var html = popup.formatHtml(
                '<nobr>单选框组: <span onclick=$$._edittext() class="edui-clickable">编辑</span>&nbsp;&nbsp;<span onclick=$$._delete() class="edui-clickable">删除</span></nobr><br><span onclick=$$._addfun() class="edui-clickable">控件事件</span></nobr>');
            if (html) {
                var elInput = el.getElementsByTagName("input");
                var rEl = elInput.length > 0 ? elInput[0] : el;
                popup.getDom('content').innerHTML = html;
                popup.anchorEl = el;
                popup.showAnchor(rEl);
            } else {
                popup.hide();
            }
        }
    });
};
/**
 * 复选框组
 * @command checkboxs
 * @method execCommand
 * @param { String } cmd 命令字符串
 * @example
 * ```javascript
 * editor.execCommand( 'checkboxs');
 * ```
 */
UE.plugins['checkboxs'] = function () {
    var me = this, thePlugins = 'checkboxs';
    me.commands[thePlugins] = {
        execCommand: function (t, type) {
            var dialog = new UE.ui.Dialog({
                iframeUrl: this.options.UEDITOR_HOME_URL + UE.formDesignUrl + '/template/html/checkboxs.html',
                name: thePlugins,
                editor: this,
                title: '复选框组',
                cssRules: "width:590px;height:430px",
                buttons: [
                    {
                        className: 'edui-okbutton',
                        label: '确定',
                        onclick: function () {
                            var orgname = $("iframe[src='" + dialog.iframeUrl + "']")[0].contentWindow.document.getElementById("orgname").value;
                            var isChange = $("iframe[src='" + dialog.iframeUrl + "']")[0].contentWindow.document.getElementById("isChange").value;
                            var dataType = $("iframe[src='" + dialog.iframeUrl + "']")[0].contentWindow.document.getElementById("dataType").value;
                            /**新建*/
                            var count = countSubstr(window.parent.mmEditor.getContent(), "name=\"" + orgname + "\"");
                            if (type == undefined) {
                                if (count != 0) {
                                    bootbox.alert("此字段已被使用");
                                    return false;
                                }
                            }
                            if (type == 1) {
                                if (count > 0 && orgname != isChange) {
                                    bootbox.alert("此字段已被使用");
                                    return false;
                                }
                            }


                            var s = /^[\-\+]?((([0-9]{1,3})([,][0-9]{3})*)|([0-9]+))?([\.]([0-9]+))?$/;
                            if (dataType == "number" || dataType == "dbnumber") {
                                var options = $("iframe[src='" + dialog.iframeUrl + "']")[0].contentWindow.$("#options_table");
                                var result = true;
                                options.find(":text").each(function (index, element) {
                                    if (!s.test($(this).val())) {
                                        alert($(this).val() + "字段类型不匹配");
                                        result = false;
                                    }
                                });
                                if (result == true) {
                                    dialog.close(true);
                                } else {
                                    return false;
                                }
                            } else {
                                dialog.close(true);
                            }

                            //
                            // if (dataType == "number" || dataType == "dbnumber" || dataType == "date") {
                            //     bootbox.alert("字段类型必须为字符型");
                            //     return false;
                            // }

                        }
                    },
                    {
                        className: 'edui-cancelbutton',
                        label: '取消',
                        onclick: function () {
                            dialog.close(false);
                        }
                    }]
            });
            dialog.render();
            dialog.open();
        }
    };
    var popup = new baidu.editor.ui.Popup({
        editor: this,
        content: '',
        className: 'edui-bubble',
        _edittext: function () {
            baidu.editor.plugins[thePlugins].editdom = popup.anchorEl;
            me.execCommand(thePlugins, 1);
            this.hide();
        },
        _delete: function () {
            if (window.confirm('确认删除该控件吗？')) {
                baidu.editor.dom.domUtils.remove(this.anchorEl, false);
            }
            this.hide();
        },
        //添加控件事件
        _addfun: function () {
            baidu.editor.plugins[thePlugins].funtool = popup.anchorEl;
            functool(thePlugins);
            this.hide();
        }
    });
    popup.render();
    me.addListener('mouseover', function (t, evt) {
        evt = evt || window.event;
        var el = evt.target || evt.srcElement;
        var designerPlugins = el.getAttribute('plugins');
        if (/span/ig.test(el.tagName) && designerPlugins == thePlugins) {
            var html = popup.formatHtml(
                '<nobr>复选框组: <span onclick=$$._edittext() class="edui-clickable">编辑</span>&nbsp;&nbsp;<span onclick=$$._delete() class="edui-clickable">删除</span></nobr>' +
                '&nbsp;&nbsp<span onclick=$$._addfun() class="edui-clickable">控件事件</span></nobr>');
            if (html) {
                var elInput = el.getElementsByTagName("input");
                var rEl = elInput.length > 0 ? elInput[0] : el;
                popup.getDom('content').innerHTML = html;
                popup.anchorEl = el;
                popup.showAnchor(rEl);
            } else {
                popup.hide();
            }
        }
    });
};
/**
 * 多行文本框
 * @command textarea
 * @method execCommand
 * @param { String } cmd 命令字符串
 * @example
 * ```javascript
 * editor.execCommand( 'textarea');
 * ```
 */
UE.plugins['textarea'] = function () {
    var me = this, thePlugins = 'textarea';
    me.commands[thePlugins] = {
        execCommand: function (t, type) {
            var dialog = new UE.ui.Dialog({
                iframeUrl: this.options.UEDITOR_HOME_URL + UE.formDesignUrl + '/template/html/textarea.html',
                name: thePlugins,
                editor: this,
                title: '多行文本框',
                cssRules: "width:750px;height:450px;",
                buttons: [
                    {
                        className: 'edui-okbutton',
                        label: '确定',
                        onclick: function () {
                            dialog.close(true);
                        }
                    },
                    {
                        className: 'edui-cancelbutton',
                        label: '取消',
                        onclick: function () {
                            dialog.close(false);
                        }
                    }]
            });
            dialog.render();
            dialog.open();
        }
    };
    var popup = new baidu.editor.ui.Popup({
        editor: this,
        content: '',
        className: 'edui-bubble',
        _edittext: function () {
            baidu.editor.plugins[thePlugins].editdom = popup.anchorEl;
            me.execCommand(thePlugins, 1);
            this.hide();
        },
        _delete: function () {
            if (window.confirm('确认删除该控件吗？')) {
                baidu.editor.dom.domUtils.remove(this.anchorEl, false);
            }
            this.hide();
        },
        _addfun: function () {
            baidu.editor.plugins[thePlugins].funtool = popup.anchorEl;
            functool(thePlugins);
            this.hide();
        }
    });
    popup.render();
    me.addListener('mouseover', function (t, evt) {
        evt = evt || window.event;
        var el = evt.target || evt.srcElement;
        if (/textarea/ig.test(el.tagName)) {
            var html = popup.formatHtml(
                '<nobr>多行文本框: <span onclick=$$._edittext() class="edui-clickable">编辑</span>&nbsp;&nbsp;<span onclick=$$._delete() class="edui-clickable">删除</span>' +
                '&nbsp;&nbsp<span onclick=$$._addfun() class="edui-clickable">控件事件</span></nobr>');
            if (html) {
                popup.getDom('content').innerHTML = html;
                popup.anchorEl = el;
                popup.showAnchor(popup.anchorEl);
            } else {
                popup.hide();
            }
        }
    });
};
/**
 * 下拉菜单
 * @command select
 * @method execCommand
 * @param { String } cmd 命令字符串
 * @example
 * ```javascript
 * editor.execCommand( 'select');
 * ```
 */
UE.plugins['select'] = function () {
    var me = this, thePlugins = 'select';
    me.commands[thePlugins] = {
        execCommand: function (t, type) {
            var dialog = new UE.ui.Dialog({
                iframeUrl: this.options.UEDITOR_HOME_URL + UE.formDesignUrl + '/template/html/select.html',
                name: thePlugins,
                editor: this,
                title: '下拉菜单',
                cssRules: "width:590px;height:370px;",
                buttons: [
                    {
                        className: 'edui-okbutton',
                        label: '确定',
                        onclick: function () {
                            var orgname = $("iframe[src='" + dialog.iframeUrl + "']")[0].contentWindow.document.getElementById("orgname").value;
                            var isChange = $("iframe[src='" + dialog.iframeUrl + "']")[0].contentWindow.document.getElementById("isChange").value;
                            /**新建*/
                            var count = countSubstr(window.parent.mmEditor.getContent(), "name=\"" + orgname + "\"");
                            if (type == undefined) {
                                if (count != 0) {
                                    bootbox.alert("此字段已被使用");
                                    return false;
                                } else {
                                    dialog.close(true);
                                }
                            }
                            if (type == 1) {
                                if (count == 1 && orgname != isChange) {
                                    bootbox.alert("此字段已被使用");
                                    return false;
                                } else {
                                    dialog.close(true);
                                }
                            }
                        }
                    },
                    {
                        className: 'edui-cancelbutton',
                        label: '取消',
                        onclick: function () {
                            dialog.close(false);
                        }
                    }]
            });
            dialog.render();
            dialog.open();
        }
    };
    var popup = new baidu.editor.ui.Popup({
        editor: this,
        content: '',
        className: 'edui-bubble',
        _edittext: function () {
            baidu.editor.plugins[thePlugins].editdom = popup.anchorEl;
            me.execCommand(thePlugins, 1);
            this.hide();
        },
        _delete: function () {
            if (window.confirm('确认删除该控件吗？')) {
                baidu.editor.dom.domUtils.remove(this.anchorEl, false);
            }
            this.hide();
        },
        //添加控件事件
        _addfun: function () {
            baidu.editor.plugins[thePlugins].funtool = popup.anchorEl;
            functool(thePlugins);
            this.hide();
        }
    });
    popup.render();
    me.addListener('mouseover', function (t, evt) {
        evt = evt || window.event;
        var el = evt.target || evt.srcElement;
        var designerPlugins = el.getAttribute('plugins');
        if (/select|span/ig.test(el.tagName) && designerPlugins == thePlugins) {
            var html = popup.formatHtml(
                '<nobr>下拉菜单: <span onclick=$$._edittext() class="edui-clickable">编辑</span>&nbsp;&nbsp;<span onclick=$$._delete() class="edui-clickable">删除</span></nobr>' +
                '&nbsp;&nbsp<span onclick=$$._addfun() class="edui-clickable">控件事件</span></nobr>');
            if (html) {
                if (el.tagName == 'SPAN') {
                    var elInput = el.getElementsByTagName("select");
                    el = elInput.length > 0 ? elInput[0] : el;
                }
                popup.getDom('content').innerHTML = html;
                popup.anchorEl = el;
                popup.showAnchor(popup.anchorEl);
            } else {
                popup.hide();
            }
        }
    });

};
/**
 * 文件上传
 * @command progressbar
 * @method execCommand
 * @param { String } cmd 命令字符串
 * @example
 * ```javascript
 * editor.execCommand( 'progressbar');
 * ```
 */
UE.plugins['fileup'] = function () {
    var me = this, thePlugins = 'fileup';
    me.commands[thePlugins] = {
        execCommand: function (t, type) {
            var dialog = new UE.ui.Dialog({
                iframeUrl: this.options.UEDITOR_HOME_URL + UE.formDesignUrl + '/template/html/fileupload.html',
                name: thePlugins,
                editor: this,
                title: '文本框',
                cssRules: "width:600px;height:310px;",
                buttons: [
                    {
                        className: 'edui-okbutton',
                        label: '确定',
                        onclick: function (event) {
                            var orgname = $("iframe[src='" + dialog.iframeUrl + "']")[0].contentWindow.document.getElementById("orgname").value;
                            var isChange = $("iframe[src='" + dialog.iframeUrl + "']")[0].contentWindow.document.getElementById("isChange").value;

                            /**新建*/

                            var count = countSubstr(window.parent.mmEditor.getContent(), "name=\"" + orgname + "\"");

                            if (type == undefined) {
                                if (orgname != "" && count != 0) {
                                    bootbox.alert("此字段已被使用");
                                    return false;
                                } else {
                                    dialog.close(true);
                                }
                            }
                            if (type == 1) {   //修改
                                if (orgname != "" && count > 0 && orgname != isChange) {
                                    bootbox.alert("此字段已被使用");
                                    return false;
                                } else {
                                    dialog.close(true);
                                }
                            }
                        }
                    },
                    {
                        className: 'edui-cancelbutton',
                        label: '取消',
                        onclick: function () {
                            dialog.close(false);
                        }
                    }]
            });
            dialog.render();
            dialog.open();
        }
    };

    var popup = new baidu.editor.ui.Popup({
        editor: this,
        content: '',
        className: 'edui-bubble',
        _edittext: function () {
            baidu.editor.plugins[thePlugins].editdom = popup.anchorEl;
            me.execCommand(thePlugins, 1);
            this.hide();
        },
        _delete: function () {
            if (window.confirm('确认删除该控件吗？')) {
                baidu.editor.dom.domUtils.remove(this.anchorEl, false);
            }
            this.hide();
        },
    });
    popup.render();
    me.addListener('mouseover', function (t, evt) {
        evt = evt || window.event;
        var el = evt.target || evt.srcElement;
        var designerPlugins = el.getAttribute('plugins');
        if (/input/ig.test(el.tagName) && designerPlugins == thePlugins) {
            var html = popup.formatHtml(
                '<nobr>附件上传: <span onclick=$$._edittext() class="edui-clickable">编辑</span>&nbsp;&nbsp;<span onclick=$$._delete() class="edui-clickable">删除</span>' +
                '</nobr>');
            if (html) {
                popup.getDom('content').innerHTML = html;
                popup.anchorEl = el;
                popup.showAnchor(popup.anchorEl);
            } else {
                popup.hide();
            }
        }
    });
};

UE.plugins['button'] = function () {
    var me = this, thePlugins = 'button';
    me.commands[thePlugins] = {
        execCommand: function (t) {
            var dialog = new UE.ui.Dialog({
                iframeUrl: this.options.UEDITOR_HOME_URL + UE.formDesignUrl + '/template/html/button.html',
                name: thePlugins,
                editor: this,
                title: '按钮',
                cssRules: "width:600px;height:310px;",
                buttons: [
                    {
                        className: 'edui-okbutton',
                        label: '确定',
                        onclick: function (event) {
                            dialog.close(true);

                        }
                    },
                    {
                        className: 'edui-cancelbutton',
                        label: '取消',
                        onclick: function () {
                            dialog.close(false);
                        }
                    }]
            });
            dialog.render();
            dialog.open();
        }
    };

    var popup = new baidu.editor.ui.Popup({
        editor: this,
        content: '',
        className: 'edui-bubble',
        _edittext: function () {
            baidu.editor.plugins[thePlugins].editdom = popup.anchorEl;
            me.execCommand(thePlugins, 1);
            this.hide();
        },
        _delete: function () {
            if (window.confirm('确认删除该控件吗？')) {
                baidu.editor.dom.domUtils.remove(this.anchorEl, false);
            }
            this.hide();
        },
        //添加控件事件
        _addfun: function () {
            baidu.editor.plugins[thePlugins].funtool = popup.anchorEl;
            functool(thePlugins);
            this.hide();
        }
    });
    popup.render();
    me.addListener('mouseover', function (t, evt) {
        evt = evt || window.event;
        var el = evt.target || evt.srcElement;
        var designerPlugins = el.getAttribute('plugins');
        if (/input/ig.test(el.tagName) && designerPlugins == thePlugins) {
            var html = popup.formatHtml(
                '<nobr>文本框: <span onclick=$$._edittext() class="edui-clickable">编辑</span>&nbsp;&nbsp;<span onclick=$$._delete() class="edui-clickable">删除</span>' +
                '&nbsp;&nbsp<span onclick=$$._addfun() class="edui-clickable">控件事件</span></nobr>');
            if (html) {
                popup.getDom('content').innerHTML = html;
                popup.anchorEl = el;
                popup.showAnchor(popup.anchorEl);
            } else {
                popup.hide();
            }
        }
    });
};

/**
 * 列表控件
 * @command listctrl
 * @method execCommand
 * @param { String } cmd 命令字符串
 * @example
 * ```javascript
 * editor.execCommand( 'qrcode');
 * ```
 */
UE.plugins['children_formlink'] = function () {
    var me = this, thePlugins = 'children_formlink';
    me.commands[thePlugins] = {
        execCommand: function () {
            var dialog = new UE.ui.Dialog({
                iframeUrl: this.options.UEDITOR_HOME_URL + UE.formDesignUrl + '/template/html/children_formlink.html',
                name: thePlugins,
                editor: this,
                title: '列表控件',
                cssRules: "width:300px;height:200px;",
                buttons: [
                    {
                        className: 'edui-okbutton',
                        label: '确定',
                        onclick: function () {
                            dialog.close(true);
                        }
                    },
                    {
                        className: 'edui-cancelbutton',
                        label: '取消',
                        onclick: function () {
                            dialog.close(false);
                        }
                    }]
            });
            dialog.render();
            dialog.open();
        }
    };
    var popup = new baidu.editor.ui.Popup({
        editor: this,
        content: '',
        className: 'edui-bubble',
        _edittext: function () {
            baidu.editor.plugins[thePlugins].editdom = popup.anchorEl;
            me.execCommand(thePlugins);
            this.hide();
        },
        _delete: function () {
            if (window.confirm('确认删除该控件吗？')) {
                baidu.editor.dom.domUtils.remove(this.anchorEl, false);
            }
            this.hide();
        }
    });
    popup.render();
    me.addListener('mouseover', function (t, evt) {
        evt = evt || window.event;
        var el = evt.target || evt.srcElement;
        var designerPlugins = el.getAttribute('class');
        if (/IFRAME/ig.test(el.tagName) && designerPlugins == thePlugins) {
            var html = popup.formatHtml(
                '<nobr>列表控件: <span onclick=$$._delete() class="edui-clickable">删除</span></nobr>');
            if (html) {
                popup.getDom('content').innerHTML = html;
                popup.anchorEl = el;
                popup.showAnchor(popup.anchorEl);
            } else {
                popup.hide();
            }
        }
    });
};
UE.plugins['more'] = function () {
    var me = this, thePlugins = 'more';
    me.commands[thePlugins] = {
        execCommand: function () {
            var dialog = new UE.ui.Dialog({
                iframeUrl: this.options.UEDITOR_HOME_URL + UE.formDesignUrl + '/html/more.html',
                name: thePlugins,
                editor: this,
                title: '玩转表单设计器，一起参与，帮助完善',
                cssRules: "width:600px;height:200px;",
                buttons: [
                    {
                        className: 'edui-okbutton',
                        label: '确定',
                        onclick: function () {
                            dialog.close(true);
                        }
                    }]
            });
            dialog.render();
            dialog.open();
        }
    };
};
UE.plugins['error'] = function () {
    var me = this, thePlugins = 'error';
    me.commands[thePlugins] = {
        execCommand: function () {
            var dialog = new UE.ui.Dialog({
                iframeUrl: this.options.UEDITOR_HOME_URL + UE.formDesignUrl + '/html/error.html',
                name: thePlugins,
                editor: this,
                title: '异常提示',
                cssRules: "width:400px;height:130px;",
                buttons: [
                    {
                        className: 'edui-okbutton',
                        label: '确定',
                        onclick: function () {
                            dialog.close(true);
                        }
                    }]
            });
            dialog.render();
            dialog.open();
        }
    };
};
UE.plugins['main'] = function () {
    var me = this, thePlugins = 'main';
    me.commands[thePlugins] = {
        execCommand: function () {
            var dialog = new UE.ui.Dialog({
                iframeUrl: this.options.UEDITOR_HOME_URL + UE.formDesignUrl + '/html/main.html',
                name: thePlugins,
                editor: this,
                title: '表单设计器 - 清单',
                cssRules: "width:620px;height:190px;",
                buttons: [
                    {
                        className: 'edui-okbutton',
                        label: '确定',
                        onclick: function () {
                            dialog.close(true);
                        }
                    }]
            });
            dialog.render();
            dialog.open();
        }
    };
};
UE.plugins['auto_template'] = function () {
    var me = this, thePlugins = 'auto_template';
    me.commands[thePlugins] = {
        execCommand: function () {
            var dialog = new UE.ui.Dialog({
                iframeUrl: this.options.UEDITOR_HOME_URL + UE.formDesignUrl + '/html/template.html',
                name: thePlugins,
                editor: this,
                title: '表单模板',
                cssRules: "width:640px;height:380px;",
                buttons: [
                    {
                        className: 'edui-okbutton',
                        label: '确定',
                        onclick: function () {
                            dialog.close(true);
                        }
                    }]
            });
            dialog.render();
            dialog.open();
        }
    };
};

UE.registerUI('button_designer', function (editor, uiName) {
    if (!this.options.designer) {
        return false;
    }
    //注册按钮执行时的command命令，使用命令默认就会带有回退操作
    editor.registerCommand(uiName, {
        execCommand: function () {
            editor.execCommand('main');
        }
    });
    //创建一个button
    var btn = new UE.ui.Button({
        //按钮的名字
        name: uiName,
        //提示
        title: "表单设计器",
        //需要添加的额外样式，指定icon图标，这里默认使用一个重复的icon
        cssRules: 'background-position: -401px -40px;',
        //点击时执行的命令
        onclick: function () {
            //这里可以不用执行命令,做你自己的操作也可
            editor.execCommand(uiName);
        }
    });

    //当点到编辑内容上时，按钮要做的状态反射
    editor.addListener('selectionchange', function () {
        var state = editor.queryCommandState(uiName);
        if (state == -1) {
            btn.setDisabled(true);
            btn.setChecked(false);
        } else {
            btn.setDisabled(false);
            btn.setChecked(state);
        }
    });
    //因为你是添加button,所以需要返回这个button
    return btn;
});

UE.registerUI('button_template', function (editor, uiName) {
    if (!this.options.designer) {
        return false;
    }
    //注册按钮执行时的command命令，使用命令默认就会带有回退操作
    editor.registerCommand(uiName, {
        execCommand: function () {
            try {
                formDesign.exec('auto_template');
            } catch (e) {
                alert('打开模板异常');
            }

        }
    });
    //创建一个button
    var btn = new UE.ui.Button({
        //按钮的名字
        name: uiName,
        //提示
        title: "表单模板",
        //需要添加的额外样式，指定icon图标，这里默认使用一个重复的icon
        cssRules: 'background-position: -339px -40px;',
        //点击时执行的命令
        onclick: function () {
            //这里可以不用执行命令,做你自己的操作也可
            editor.execCommand(uiName);
        }
    });

    //当点到编辑内容上时，按钮要做的状态反射
    editor.addListener('selectionchange', function () {
        var state = editor.queryCommandState(uiName);
        if (state == -1) {
            btn.setDisabled(true);
            btn.setChecked(false);
        } else {
            btn.setDisabled(false);
            btn.setChecked(state);
        }
    });
    //因为你是添加button,所以需要返回这个button
    return btn;
});

UE.registerUI('button_preview', function (editor, uiName) {
    if (!this.options.designer) {
        return false;
    }
    //注册按钮执行时的command命令，使用命令默认就会带有回退操作
    editor.registerCommand(uiName, {
        execCommand: function () {
            try {
                formDesign.fnReview();
            } catch (e) {
                alert('formDesign.fnReview 预览异常');
            }
        }
    });
    //创建一个button
    var btn = new UE.ui.Button({
        //按钮的名字
        name: uiName,
        //提示
        title: "预览",
        //需要添加的额外样式，指定icon图标，这里默认使用一个重复的icon
        cssRules: 'background-position: -420px -19px;',
        //点击时执行的命令
        onclick: function () {
            UE.extensions.review()

        }
    });

    //因为你是添加button,所以需要返回这个button
    return btn;
});

UE.registerUI('button_save', function (editor, uiName) {
    if (!this.options.designer) {
        return false;
    }
    //创建一个button
    var btn = new UE.ui.Button({
        //按钮的名字
        name: uiName,
        //提示
        title: "保存表单",
        //需要添加的额外样式，指定icon图标，这里默认使用一个重复的icon
        cssRules: 'background-position: -481px -20px;',
        //点击时执行的命令
        onclick: function () {
            UE.extensions.saveForm("save", editor)
        }
    });
    return btn;
}, 0);

UE.extensions = {}

UE.extensions.saveForm = function (type, editor) {

    if (editor.queryCommandState('source'))
        editor.execCommand('source');//切换到编辑模式才提交，否则有bug

    if (editor.hasContents()) {
        editor.sync();
        //获取表单设计器里的内容
        var formTemplate = formEditor.getContent();
        if (editor.options.designer.saveCallback) {
            //TODO 调用回到方法,用户自行处理保存
            editor.options.designer.saveCallback(formTemplate);
        } else {
            alert("请设定保存回调方法: designer.saveCallback");
        }
    } else {
        return "";
    }
}

UE.extensions.review = function (editor) {


    if (editor.queryCommandState('source'))
        editor.execCommand('source');//切换到编辑模式才提交，否则有bug

    if (editor.hasContents()) {
        editor.sync();
        //获取表单设计器里的内容
        var formTemplate = formEditor.getContent();
        if (editor.options.designer.previewCallback) {
            //TODO 弹出层预览操作
            editor.options.designer.previewCallback(formTemplate);
        } else {
            //TODO 预览默认实现
        }
    } else {
        return "";
    }

}
