/** 本文件内容为easyUI环境下公共方法，不涉及具体的业务场景 */
;(function ($, top, window, document, undefined) {
    $.extend({
        showMsg:showMsg
        ,alert:alertFun
        ,progressLoad: progressLoad
        ,progressClose: progressClose
        ,progressAlert:progressAlert
        ,modalDialog:modalDialog
        ,defaultFormOnSubmit:defaultFormOnSubmit
        ,dealJSONObj:dealJSONObj
        ,dealError:dealError
        ,pagerFilter:pagerFilter
        ,showColumn:showColumn
    });

    /**
     * 全局显示消息
     * @param msg
     */
    function showMsg(msg) {
        top.window.$.messager.show({
            title: '提示',
            msg:  msg || "消息内容！",
            timeout: 2000,
            showType: 'slide'
            , style: {
                right: '',
                top: document.body.scrollTop + document.documentElement.scrollTop,
                bottom: ''
            }
        });
    };

    /**
     * 全局警告提示
     *
     * @param msg
     */
    function alertFun(msg) {
        top.window.$.messager.alert('警告',msg);
    };

    /**
     * 全局页面加载加载进度条启用
     *
     * @requires jQuery
     **/
    function progressLoad() {
        top.window.$.messager.progress({text:'处理中...'});
    };

    /**
     * 全局页面加载加载进度条关闭
     *
     * @requires jQuery
     * **/
    function progressClose() {
        top.window.$.messager.progress('close');
    };

    /**
     * 全局页面加载加载进度条显示不可撤销的警告
     *
     * @requires jQuery
     * **/
    function progressAlert(str) {
        top.window.$.messager.progress({text:str});
    };

    /**
     * 创建一个模式化的dialog
     *
     * @requires jQuery,EasyUI
     *
     * @returns $.modalDialog.handler 这个handler代表弹出的dialog句柄
     *
     * @returns $.modalDialog.xxx 这个xxx是可以自己定义名称，主要用在弹窗关闭时，刷新某些对象的操作，可以将xxx这个对象预定义好
     */
    function modalDialog(options) {
        if ($.modalDialog.handler == undefined) {// 避免重复弹出
            var opts = $.extend({
                title : '',
                width : 840,
                height : 680,
                modal : true,
                onClose : function() {
                    $.modalDialog.handler = undefined;
                    $(this).dialog('destroy');
                },
                onOpen : function() {
                }
            }, options);
            opts.modal = true;// 强制此dialog为模式化，无视传递过来的modal参数
            return $.modalDialog.handler = $('<div class="_temp"/>').dialog(opts);
        }
    };

    /**
     * 通用错误提示
     *
     * @requires jQuery,EasyUI
     *
     *
     * 用于datagrid/treegrid/tree/combogrid/combobox/form加载数据出错时的操作
     */
    $.map(['datagrid','treegrid','tree','combogrid','combobox','form'],function (plugin) {
        if($.fn[plugin]){
            $.fn[plugin].defaults.onLoadError=function (XMLHttpRequest) {
                progressClose();
                alertFun('系统异常');
                console.log({type:'系统异常',msg:XMLHttpRequest.responseText});
            };
        }
    });

    /**
     * easyUI：defaultFormOnSubmit
     * @returns {*|jQuery}
     */
    function defaultFormOnSubmit() {
        var isValid = $(this).form('validate');
        if (!isValid) progressClose();
        return isValid;
    };

    /**
     * alert错误信息，并跳转error页面
     *
     * @requires jQuery,Base.error（错误页面相对路径）
     *
     * @param data
     */
    function dealError(data) {
        alert(data.msg);
        location.href = Base.error.parseUrl(Base.path);
    };

    /**
     * 对json字符串转化为的obj信息进行处理
     * code = 1XXX      警告权限错误，并跳转到登录页面
     * code = 其他数值   不处理
     * code = 其他非数值 设置code为5000
     *
     * @param data
     * @param func
     * @return obj

     */
    function dealJSONObj(data,func) {
        var obj = {};
        if($.type(data)==='string'){
            try{ obj = $.parseJSON(data);}catch (e){}
        }else if($.isPlainObject(data)){
            obj = $.extend({},data);
        }
        if($.type(obj.code)!=='number' ||$.type(obj.msg)!=='string') obj = {code:5000,msg:'数据加载异常'};
        if(999 < obj.code && 2000 > obj.code){
            alert(obj.msg);
            top.location.href = (Base.login || "" ).parseUrl(Base.path);
        }else {
            if($.isFunction(func)) return func(obj);
        }
        return obj;
    };

    /**
     * 当页面加载完毕关闭加载进度
     *
     * @requires jQuery
     */
    $(window).load(function(){
        $("#loading").fadeOut();
    });

    /**
     * 防止退格键导致页面回退
     *
     * @requires jQuery
     */
    $(document).keydown(function (e) {
        var doPrevent;
        if (e.keyCode == 8) {
            var d = e.srcElement || e.target;
            if (d.tagName.toUpperCase() == 'INPUT' || d.tagName.toUpperCase() == 'TEXTAREA') {
                doPrevent = d.readOnly || d.disabled;
            } else {
                doPrevent = true;
            }
        } else {
            doPrevent = false;
        }
        if (doPrevent)
            e.preventDefault();
    });

    /**
     * 使panel和datagrid在加载时提示
     *
     * @requires jQuery,EasyUI
     *
     */
    $.map(['panel','datagrid','dialog'], function(plugin){
        if ($.fn[plugin]){
            $.fn[plugin].defaults.loadingMessage = '加载中....';
        }
    });

    /**
     * panel关闭时回收内存，主要用于layout使用iframe嵌入网页时的内存泄漏问题
     *
     *  @requires jQuery,EasyUI
     */
    $.fn.panel.defaults.onBeforeDestroy = function() {
        var frame = $('iframe', this);
        try {
            if (frame.length > 0) {
                for ( var i = 0; i < frame.length; i++) {
                    frame[i].src = '';
                    frame[i].contentWindow.document.write('');
                    frame[i].contentWindow.close();
                }
                frame.remove();
                if (navigator.userAgent.indexOf("MSIE") > 0) {// IE特有回收内存方法
                    try {
                        CollectGarbage();
                    } catch (e) {
                    }
                }
            }
        } catch (e) {
        }
    };

    /**
     * 防止panel/window/dialog组件超出浏览器边界
     *
     * @requires jQuery,EasyUI
     *
     * @param left
     * @param top
     */
    $.map(['dialog','window','panel'],function(plugin){
        if ($.fn[plugin]){
            $.fn[plugin].defaults.onMove = function(left, top) {
                var l = left;
                var t = top;
                if (l < 1) {
                    l = 1;
                }
                if (t < 1) {
                    t = 1;
                }
                var width = parseInt($(this).parent().css('width')) + 14;
                var height = parseInt($(this).parent().css('height')) + 14;
                var right = l + width;
                var buttom = t + height;
                var browserWidth = $(window).width();
                var browserHeight = $(window).height();
                if (right > browserWidth) {
                    l = browserWidth - width;
                }
                if (buttom > browserHeight) {
                    t = browserHeight - height;
                }
                $(this).parent().css({/* 修正面板位置 */
                    left : l,
                    top : t
                });
            };
        }
    });

    /**
     * 扩展validatebox，添加验证两次密码功能
     *
     * @requires jQuery,EasyUI
     *
     */
    $.extend($.fn.validatebox.defaults.rules, {
        eqPwd : {
            validator : function(value, param) {
                return value == $(param[0]).val();
            },
            message : '密码不一致！'
        }
    });

    /**
     * 扩展validatebox，添加通过标准json串判断存在与否的功能
     *
     * @requires jQuery,EasyUI
     *
     * 代码案例:
     *
     *  validType="jsonHas[Base.path+'local/json/sys_function/hasFunctionName.txt?functionName=','功能名称已存在','safeValueId']"
     *
     */
    $.extend($.fn.validatebox.defaults.rules, {
        jsonHas : {
            validator : function(value, param) {
                var safeValue = $('#'+param[2]).val();
                if(!$.isEmpty(value) && value == safeValue )return true;
                var url = param[0] + value;
                var data =  $.ajax({url:url.parseUrl(Base.path),async:false,type:"get"}).responseText;
                return $.dealJSONObj(data,function (record) {
                    if(!record.code){
                        $.fn.validatebox.defaults.rules.jsonHas.message = param[1];
                        return false;
                    }else if(record.code === -1) {
                        return true;
                    }else{
                        $.fn.validatebox.defaults.rules.jsonHas.message = record.msg;
                        return false;
                    }
                });
            }
            ,message : ''
        }
    });

    /**
     *为datagrid、treegrid增加表头菜单，用于显示或隐藏列，注意：冻结列不在此菜单中
     *
     * @requires jQuery,EasyUI
     *
     */
    $.map(['datagrid','treegrid'],function (plugin) {
        if($.fn[plugin]){
            $.fn[plugin].defaults.onHeaderContextMenu=function(e, field) {
                e.preventDefault();
                var grid = $(this);/* grid本身 */
                var headerContextMenu = this.headerContextMenu;/* grid上的列头菜单对象 */
                if (!headerContextMenu) {
                    var tmenu = $('<div style="width:100px;"></div>').appendTo('body');
                    var fields = grid.datagrid('getColumnFields');
                    for ( var i = 0; i < fields.length; i++) {
                        var fildOption = grid.datagrid('getColumnOption', fields[i]);
                        if (!fildOption.hidden) {
                            $('<div iconCls="tick" field="' + fields[i] + '"/>').html(fildOption.title).appendTo(tmenu);
                        } else {
                            $('<div iconCls="bullet_blue" field="' + fields[i] + '"/>').html(fildOption.title).appendTo(tmenu);
                        }
                    }
                    headerContextMenu = this.headerContextMenu = tmenu.menu({
                        onClick : function(item) {
                            var field = $(item.target).attr('field');
                            if (item.iconCls == 'tick') {
                                grid.datagrid('hideColumn', field);
                                $(this).menu('setIcon', {
                                    target : item.target,
                                    iconCls : 'bullet_blue'
                                });
                            } else {
                                grid.datagrid('showColumn', field);
                                $(this).menu('setIcon', {
                                    target : item.target,
                                    iconCls : 'tick'
                                });
                            }
                        }
                    });
                }
                headerContextMenu.menu('show', {
                    left : e.pageX,
                    top : e.pageY
                });
            };
        }
    });

    /**
     * grid tooltip参数
     *
     * Datagrid扩展方法tooltip 基于Easyui 1.3.3，可用于Easyui1.3.3+
     *
     * 简单实现，如需高级功能，可以自由修改
     *
     * 使用说明:
     *
     * 在easyui.min.js之后导入本js
     *
     * 代码案例:
     *
     * $("#dg").datagrid('tooltip'); 所有列
     *
     * $("#dg").datagrid('tooltip',['productid','listprice']); 指定列
     *
     */
    $.map(['datagrid','treegrid'],function (plugin) {
        if($.fn[plugin]){
            $.extend($.fn[plugin].methods, {
                tooltip : function(jq, fields) {
                    return jq.each(function() {
                        var panel = $(this).datagrid('getPanel');
                        if (fields && typeof fields == 'object' && fields.sort) {
                            $.each(fields, function() {
                                var field = this;
                                bindEvent($('.datagrid-body td[field=' + field + '] .datagrid-cell', panel));
                            });
                        } else {
                            bindEvent($(".datagrid-body .datagrid-cell", panel));
                        }
                    });
                    function bindEvent(jqs) {
                        jqs.mouseover(function() {
                            var content = $(this).text();
                            if (content.replace(/(^\s*)|(\s*$)/g, '').length > 5) {
                                $(this).tooltip({
                                    content : content,
                                    trackMouse : true,
                                    position : 'bottom',
                                    onHide : function() {
                                        $(this).tooltip('destroy');
                                    },
                                    onUpdate : function() {
                                        var tip = $(this).tooltip('tip');
                                        if (parseInt(tip.css('width')) > 500) {
                                            tip.css('width', 500);
                                        }
                                    }
                                }).tooltip('show');
                            }
                        });
                    }
                }
            });
        }
    });

    /**
     * 前台分页函数
     * @param data
     * @returns {*}
     */
    function pagerFilter (data, dg) {
        if ($.isNumeric(data.total)) {   // is array
            data = $.extend({},data);
        } else {
            data = {
                total:data.length,
                rows: data
            }
        }
        var opts = dg.datagrid('options');

        var pager = dg.datagrid('getPager');
        pager.pagination({
            onSelectPage: function (pageNum, pageSize) {
                opts.pageNumber = pageNum ;
                opts.pageSize = pageSize;
                pager.pagination('refresh', {
                    pageNumber: pageNum,
                    pageSize: pageSize
                });
                dg.datagrid('loadData', data);
            }
        });
        if (!data.originalRows) {
            data.originalRows = (data.rows);
        };

        var sortName = opts.sortName;
        var sortOrder = opts.sortOrder;
        if (sortName !== null) {
            data.originalRows = data.originalRows.sort(compare(sortName, sortOrder));
        }

        var start = (opts.pageNumber - 1) * parseInt(opts.pageSize);
        var end = start + parseInt(opts.pageSize);
        data.rows = (data.originalRows.slice(start, end));
        return data;
    };

    /**
     * 分页方法
     * @param sort
     * @param order
     */
    function showColumn(sort, order) {
        var dg = $(this);
        var pager = dg.datagrid('getPager');
        pager.pagination('select', 1);
        dg.datagrid("reload");
    }

    /**
     * 前台排序函数
     * @param prop
     * @param val
     * @returns {Function}
     */
    function compare (prop, val) {
        if (val === 'asc') {
            return function (obj1, obj2) {
                var val1 = obj1[prop];
                var val2 = obj2[prop];
                if (val1 < val2) {
                    return -1;
                } else if (val1 > val2) {
                    return 1;
                } else {
                    return 0;
                }
            }
        } else {
            return function (obj1, obj2) {
                var val1 = obj1[prop];
                var val2 = obj2[prop];
                if (val1 < val2) {
                    return 1;
                } else if (val1 > val2) {
                    return -1;
                } else {
                    return 0;
                }
            }
        }
    };

    /**
     * 数字排序方法 正序
     * @param a
     * @param b
     * @returns {number}
     */
    function numberSort(a,b){
        var number1 = parseFloat(a);
        var number2 = parseFloat(b);
        return (number1 > number2 ? 1 : -1);
    }
    /**
     * 数字排序方法 倒序
     * @param a
     * @param b
     * @returns {number}
     */
    function numberSort(a,b){
        var number1 = parseFloat(a);
        var number2 = parseFloat(b);
        return (number1 > number2 ? -1 : 1);
    }
    /**
     * 数字排序方法 正序
     * @param a
     * @param b
     * @returns {number}
     */
    function stringSort(a,b){
        return (a > b ? 1 : -1);
    }
    /**
     * 数字排序方法 倒序
     * @param a
     * @param b
     * @returns {number}
     */
    function stringSort(a,b){
        return (number1 > number2 ? -1 : 1);
    }
})($, top, window, document);






















