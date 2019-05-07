$.extend($.fn.textbox.defaults.rules, {
    number: {
        validator: function (value, param) {
            return /^[0-9]*$/.test(value);
        },
        message: "请输入数字"
    },
    chinese: {
        validator: function (value, param) {
            var reg = /^[\u4e00-\u9fa5]+$/i;
            return reg.test(value);
        },
        message: "请输入中文"
    },
    checkLength: {
        validator: function (value, param) {
            return param[0] >= get_length(value);
        },
        message: '请输入最大{0}位字符'
    },
    specialCharacter: {
        validator: function (value, param) {
            var reg = new RegExp("[`~!@#$^&*()=|{}':;'\\[\\]<>~！@#￥……&*（）——|{}【】‘；：”“'、？]");
            return !reg.test(value);
        },
        message: '不允许输入特殊字符'
    },
    tel: {
        validator: function (value, param) {
            var partten = /^1[3,5,8]\d{9}$/;
            return partten.test(value);
        },
        message: '请填写正确的手机号！'
    },
    num: {
        validator: function (value, param) {
            var partten = /^[1-9]\d*|0$/;
            return partten.test(value);
            //return true;
        },
        message: '请填写数字学分！'
    },
    cishu: {
        validator: function (value, param) {
            var partten = /^[1-9]\d*$/;
            return partten.test(value);
        },
        message: '请填写正确数字！'
    },
    startMinute: {
        validator: function (value, param) {
            var partten = /^[0-9]\d*$/;
            return partten.test(value);
        },
        message: '请填写正确数字！'
    },
    pass: {
        validator: function (value, param) {
            var password = $("#password").val();
            return password == value;
        },
        message: '确认密码与密码不同，请重新输入！'
    },
    money: {
        validator: function (value, param) {
            var partten = /^(([1-9]\d*)|0)(\.\d{1,2})?$/;
            return partten.test(value);
        },
        message: '请填写正确金额！'
    },
    number: {
        validator: function (value, param) {
            var partten = /^\d{1,3}(\.\d{1,2})?$/;
            return partten.test(value);
        },
        message: '请填写正确数字！'
    },
    jdfen: {
        validator: function (value, param) {
            var shitifen = param[0];
            shitifen = parseFloat(shitifen);
            return value <= shitifen;
        },
        message: '请填写正确分值，得分不能大于试题分数！'
    },
    oKNumber: {
        validator: function (value, param) {
            var partten = /^[0-9]\d*$/;
            return partten.test(value);
        },
        message: '请填写正整数！'
    },
    doublenum: {
        validator: function (value, param) {
            var partten = /^(([0-9]\d*)|0)(\.\d{1,2})?$/;
            //return partten.test(value);
            return true;
        },
        message: '请填写正确数据！'
    },
    loginName: {
        validator: function (value, param) {
            var partten = /^[a-zA-Z0-9]{4,20}$/;
            return partten.test(value);
        },
        message: '请填写4-20位字母或数字！'
    },
//    excel:{
//        validator: function(value, param){
//         var partten ="^.*\.(?:xls)$";
//            return partten.test(value);
//        },
//        message: '请选择excel文件'
//    },
    roleName: {
        validator: function (value, param) {
            // var partten =/^([\u4e00-\u9fa5]{2,10})|([A-Za-z0-9 ]{2,10})$/;
            var partten = /^[0-9a-zA-Z\u4e00-\u9fa5]{2,10}$/;
            return partten.test(value);
        },
        message: '请填写2-10位字母、数字或汉字！'
    },
    sutdyTimeNum: {
        validator: function (value, param) {
            var partten = /^\d*(\.\d{1,2})?$/;
            return partten.test(value);
        },
        message: '请填写正整数或保留两位小数的学时！'
    },
    coursewareName: {
        validator: function (value, param) {
            var partten = /^[0-9a-zA-Z\u4e00-\u9fa5]{2,10}$/;
            return partten.test(value);
        },
        message: '请填写2-20位字母、数字或汉字！'
    },
    httpPath: {
        validator: function (value, param) {
            var strRegex = "^((https|http|ftp|rtsp|mms)?://)"
                + "?(([0-9a-z_!~*'().&=+$%-]+: )?[0-9a-z_!~*'().&=+$%-]+@)?" //ftp的user@
                + "(([0-9]{1,3}\.){3}[0-9]{1,3}" // IP形式的URL- 199.194.52.184
                + "|" // 允许IP和DOMAIN（域名）
                + "([0-9a-z_!~*'()-]+\.)*" // 域名- www.
                + "([0-9a-z][0-9a-z-]{0,61})?[0-9a-z]\." // 二级域名
                + "[a-z]{2,6})" // first level domain- .com or .museum
                + "(:[0-9]{1,4})?" // 端口- :80
                + "((/?)|" // a slash isn't required if there is no file name
                + "(/[0-9a-z_!~*'().;?:@&=+$,%#-]+)+/?)$";
            var partten = new RegExp(strRegex);
            return partten.test(value);
        },
        message: '请填写正确的网址！'

    },
    messageTitleLength: {
        validator: function (value, param) {
            var partten = /^[0-9a-zA-Z\u4e00-\u9fa5]{2,10}$/;
            return partten.test(value);
        },
        message: '请填写2-30位字母、数字或汉字！'
    }
    , minuteBegin: {
        validator: function (value, param) {
            var partten = /^[1-9]\d*$/;
            return partten.test(value);
        },
        message: '请填写正确弹试题的时间！格式为大于0的整数。'
    }, md: {
        validator: function (value, param) {
            startTime2 = $(param[0]).datetimebox('getValue');
            var d1 = new Date(startTime2.replace(/\-/g, '/'));
            var d2 = new Date(value.replace(/\-/g, '/'));
            varify = d2 > d1;
            return varify;

        },
        message: '结束时间要大于开始时间！'
    },
    /* ================================================================== */
//校验身份证号码
    isIdCard: {
        validator: function (value, param) {
            var partten = /^(\d{15}$|^\d{18}$|^\d{17}(\d|X|x))$/;
            return partten.test(value);
        },
        message: '身份证号错误！'
    },
    englishLowerCase: {// 验证英语小写
        validator: function (value) {
            return /^[a-z]+$/.test(value);
        },
        message: '请输入小写字母'
    }
});