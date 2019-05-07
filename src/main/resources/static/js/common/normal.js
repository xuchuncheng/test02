/** 本文件内容为公共方法，不涉及具体的业务场景 */

;(function ($, top, window, document, undefined) {
    $.extend({
        isEmpty:isEmpty
        ,getHrefParam:getHrefParam
        ,array2TreeFormat: array2TreeFormat
        ,dealJSONObj: dealJSONObj
        ,arrayCount:arrayCount
        ,arrayValue:arrayValue
});

    /**
     * 通过数组渲染select下拉单
     * @param array
     * @param valueColumn
     * @param nameColumn
     * @param defaultValue
     * @param defaultText
     * @return {jQuery}
     */
    $.fn.selectRenderByArray = function (array, valueColumn, nameColumn, defaultValue, defaultText) {
        var $element = this;
        var str = '';
        if(!$.isEmpty(defaultText))
            str = ('' === defaultValue )
                ? ( "<option value='' selected>" + defaultText + "</option>")
                : ( "<option value=''>" + defaultText + "</option>");
        $.each(array,function (key,item) {
            var select = (item[valueColumn] + '' === defaultValue + '') ? (' selected') : ('');
            str += "<option value='" + item[valueColumn] + "'" + select + ">"
                + item[nameColumn] + "</option>";
        });
        return $element.html(str);
    };

    /**
     * 通过obj渲染form表单
     * @required jQuery moment numeral
     * @param obj
     * @return {jQuery}
     */
    $.fn.formRenderByObj = function (obj) {
        var $element = this;
        if(!$.isEmpty(obj)){
            $element.find('input,select').each(function (index, item) {
                var inputName = $(item).attr('name');
                if (inputName !== undefined) {
                    if($(item).attr('type') === 'radio'){
                        if($(item).val() && String($(item).val()) === String(obj[inputName]))
                            $(item).prop('checked', 'true');
                    }else{
                        var val = obj[inputName];
                        var pattern;
                        /* 支持日期格式化，并且保留long值精度 */
                        if((pattern = $(item).attr('data-date-format')) !== undefined){
                            $(item).attr('data-date-value', val);
                            if ($.isNumeric(val)) { //number
                                val = moment(val, 'x').format(pattern);
                            } else if ($.type(val) === 'date') {
                                val = moment(val).format(pattern);
                            }
                        }
                        /* 支持数字格式化，并保留long精度 */
                        if((pattern = $(item).attr('data-number-format')) !== undefined){
                            $(item).attr('data-number-value', val);
                            if ($.isNumeric(val)) { //number
                                val = numeral(val).format(pattern);
                            }
                        }
                        $(item).val(val);
                    }
                }
            });
            $element.find('textarea').each(function (index, item) {
                var inputName = $(item).attr('name');
                if (inputName !== undefined) $(item).text(obj[inputName]);
            });
        }
        return $element;
    };

    /**
     * 通过obj渲染Div（主要应用于view页面）
     * @required jQuery moment numeral
     * @param obj
     * @return {jQuery}
     */
    $.fn.divRenderByObj = function (obj) {
        var $element = this;
        if(!$.isEmpty(obj)){
            $element.find('div,span').each(function (index, item) {
                var inputName = $(item).attr('name');
                if (inputName !== undefined) {
                    var val = obj[inputName];
                    var pattern;
                    /* 支持日期格式化，并且保留long值精度 */
                    if((pattern = $(item).attr('data-date-format')) !== undefined){
                        $(item).attr('data-date-value', val);
                        if ($.isNumeric(val)) { //number
                            val = moment(val, 'x').format(pattern);
                        } else if ($.type(val) === 'date') {
                            val = moment(val).format(pattern);
                        }
                    }
                    /* 支持数字格式化，并保留long精度 */
                    if((pattern = $(item).attr('data-number-format')) !== undefined){
                        $(item).attr('data-number-value', val);
                        if ($.isNumeric(val)) { //number
                            val = numeral(val).format(pattern);
                        }
                    }
                    $(item).html(val);
                }
            });
        }
        return $element;
    };

    /**
     * 清除表单数据
     * @return {jQuery}
     */
    $.fn.formClear = function () {
        var $element = this;
        $element.find(':input').each(function () {
            switch (this.type) {
                case 'passsword':
                case 'select-multiple':
                case 'select-one':
                case 'text':
                case 'file':
                case 'textarea':
                    $(this).val('');
                    break;
                case 'checkbox':
                case 'radio':
                    this.checked = false;
                    break;
            }
        });
        $element.find('span').each(function () {
            $(this).text('');
        });
        return $element;
    };

    /**
     * form表单转Obj
     * @return {{}}
     */
    $.fn.form2Obj = function () {
        var $element = this;
        var o = {};
        var a = $element.serializeArray(); // 有name和value值才返回
        $.each(a, function () {
            if (o[this.name]) {
                if (!o[this.name].push) {
                    o[this.name] = [o[this.name]];
                }
                o[this.name].push($.trim(this.value) || '');
            } else {
                o[this.name] = $.trim(this.value) || '';
            }
        });
        //如果有保存Date的long精度的，把值带回后台
        $element.find("input[data-date-value]").each(function (index, item) {
            if ($(item).attr('data-date-value')) {
                o[this.name] = $.trim($(item).attr('data-date-value')) || '';
            }
        });
        //如果有保存Number的long精度的，把值带回后台
        $element.find("input[data-number-value]").each(function (index, item) {
            if ($(item).attr('data-number-value')) {
                o[this.name] = $.trim($(item).attr('data-number-value')) || '';
            }
        });
        return o;
    };

    /**
     * 判断val是否为空
     * --包括null、"null"、undefined、"undefined"、NaN、"NaN"、" "、"\t\r\n"、[]、{}
     * --不包括0、"0"、true、false、['']
     */
    function isEmpty(val) {
        if($.isPlainObject(val)) return $.isEmptyObject(val);
        if($.isArray(val)) return (val.length === 0);
        if (val === null || val === undefined || val !== val) return true;
        val = (val + '').replace(/^\s*|\s$/g, '');
        return (val === '' || val === 'null' || val === 'undefined' || val === 'NaN');
    }


    /**
     * 根据地址中的参数获取值
     * @param key
     * @return {string}
     */
    function getHrefParam(key) {
        var s = window.location.href;
        var reg = new RegExp(key + "=\\w+");
        var rs = reg.exec(s);
        if (rs === null || rs === undefined) {
            return "";
        } else {
            return rs[0].split("=")[1];
        }
    }

    /**
     * 把数据库取出的简单格式数据转成多层级的Tree格式数据
     * 建议简单数据格式应含有，主键列，父id列，排序列，格式化前应先排序
     * @required jQuery
     *
     * @param array         array数据
     * @param key           string，主键的名字，如'id'
     * @param parentKey     string，父id的名字，如'parentId'
     * @param childKey      string，生成tree数据时，嵌套数组的名字，如'submenu'
     * @return {Array}      多层级的Tree格式数据
     */
    function array2TreeFormat(array, key, parentKey, childKey) {
        var i, l;
        if (!key || key === "" || !array) return [];

        if ($.isArray(array)) {
            var r = [];
            var tmpMap = {};
            for (i = 0, l = array.length; i < l; i++) {
                tmpMap[array[i][key]] = array[i];
            }
            for (i = 0, l = array.length; i < l; i++) {
                if (tmpMap[array[i][parentKey]] && String(array[i][key]) !== String(array[i][parentKey])) {
                    if (!tmpMap[array[i][parentKey]][childKey])
                        tmpMap[array[i][parentKey]][childKey] = [];
                    tmpMap[array[i][parentKey]][childKey].push(array[i]);
                } else {
                    r.push(array[i]);
                }
            }
            return r;
        } else {
            return [array];
        }
    }

    /**
     * 对json字符串转化为的obj信息进行处理
     * 如果数据异常 设置code为5000
     * @param data
     * @param func
     * @return {{}}
     */
    function dealJSONObj(data,func) {
        var obj = {};
        if($.type(data) === 'string'){
            try{ obj = $.parseJSON(data);}catch (e){}
        }else if($.isPlainObject(data)){
            obj = $.extend({},data);
        }
        if($.type(obj.code)!=='number' ||$.type(obj.msg)!=='string') obj = {code:5000,msg:'数据加载异常'};
        if($.isFunction(func)) return func(obj);
        return obj;
    }

    /**
     * 返回数组中key的个数
     * @param arr
     * @param key
     * @returns {number}
     */
    function arrayCount (arr, key) {
        var count = 0;
        for (var i = 0; i < arr.length; i++) {
            if (arr[i].name === key) {
                count++;
            }
        }
        return count;
    }

    /**
     * 返回数组中第一个key的值
     * @param arr
     * @param key
     * @returns {*}
     */
    function arrayValue (arr, key) {
        for (var i = 0; i < arr.length; i++) {
            if (arr[i].name === key) {
                return arr[i].value;
            }
        }
        return undefined;
    }
/*
    var Render = function (ele, opt) {
        this.$element = ele;
        this.defaults = {};
        this.options = $.extend({}, this.defaults, opt)
    };
    Render.prototype = {
        renderSelect : function () {
            return this.$element;
        }
    };
*/

})($, top, window, document);