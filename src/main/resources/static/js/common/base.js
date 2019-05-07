/*补充JavaScript基础类库，优先引用。*/

;(function (top,window,document,undefined) {
    /**
     * 去除前后空格
     * @return {string}
     */
    String.prototype.trim = function(){
        return this.replace(/(^\s*)|(\s*$)/g, "");
    };

    /**
     * 得到右边的字符串
     * @param len
     * @return {string}
     */
    String.prototype.right = function(len){
        if(isNaN(len)||len===null||len === undefined) {
            len = this.length;
        }else {
            if(parseInt(len)<0||parseInt(len)>this.length) {
                len = this.length;
            }
        }
        return this.substring(this.length-len,this.length);
    };

    /**
     * JS获取字符串的实际长度，区分中英文字符，中文字符计2个字
     * @return {int}
     */
    String.prototype.strlength= function() {
        if (this === null || this === undefined || this === '') {
            return 0;
        }
        var len = this.length;
        for ( var i = 0; i < len; i++) {
            if (this.charCodeAt(i) > 127) {
                len++;
            }
        }
        return len;
    };

    /**
     * 增加formatString功能
     * 使用方法：'字符串{0}字符串{1}字符串'.formatStr('第一个变量','第二个变量');
     * @return {string}
     */
    String.prototype.formatStr = function () {
        var args = [];
        for(var i = 0; i < arguments.length; i++) {
            args.push(arguments[i]);
        }
        return this.replace(/{(\d+)}/g, function(m, i) {
            return {"true":m,"false":args[i]}[$.type(args[i]) === 'undefined'];
        });
    };

    /**
     * 处理一个以逗号分割的字符串，返回Array，Array里每一项都是一个字符串
     * @return {Array}
     */
    String.prototype.toArray = function () {
        var _this = this;
        if(_this.trim() === '') return [];
        var values = [];
        var t = _this.split(',');
        for ( var i = 0; i < t.length; i++) {
            values.push('' + t[i]);/* 避免他将ID当成数字 */
        }
        return values;
    };

    /**
     * 格式化url，给url增加前缀和时间戳
     * @param profix
     * @return {string}
     */
    String.prototype.parseUrl = function (profix) {
        var _this = this;
        if(profix === null || profix === undefined ) profix = '';
        return profix.trim() + _this
            + {true:"&xxtime=",false:"?xxtime="}[_this.indexOf('?') >= 0]
            + (new Date().getTime());
    };

    /**
     * 对字符串进行Base64加密
     * @param salt      加密盐，字符串，默认''
     * @param times     加密次数，自然数，默认1
     * @param isURL     是否URL格式，布尔，默认true
     * @param hasCN     是否支持中文，布尔，默认true
     * @return {string}
     */
    String.prototype.encodeBase64 = function (salt, times, isURL, hasCN) {
        if(salt === undefined || salt === null || salt === '') salt = '';
        if(times === undefined || times === null || times === '') times = 1;
        times = parseInt(times + '');
        if(isURL === undefined || isURL === null || isURL === '') isURL = true;
        if(hasCN === undefined || hasCN === null || hasCN === '') hasCN = true;
        return BASE64.encodeUtil(this,times,salt,isURL,hasCN);
    };
    /**
     * 对字符串进行Base64解密
     * @param salt      加密盐，字符串，默认''
     * @param times     加密次数，自然数，默认1
     * @param isURL     是否URL格式，布尔，默认true
     * @return {string}
     */
    String.prototype.decodeBase64 = function (salt, times, isURL) {
        if(salt === undefined || salt === null || salt === '') salt = '';
        if(times === undefined || times === null || times === '') times = 1;
        times = parseInt(times + '');
        if(isURL === undefined || isURL === null || isURL === '') isURL = true;
        return BASE64.decodeUtil(this,times,salt,isURL);
    };

    var BASE64={
        enKey: 'ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789+/'
        ,deKey: new Array(
            -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1,
            -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1,
            -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, 62, -1, -1, -1, 63,
            52, 53, 54, 55, 56, 57, 58, 59, 60, 61, -1, -1, -1, -1, -1, -1,
            -1,  0,  1,  2,  3,  4,  5,  6,  7,  8,  9, 10, 11, 12, 13, 14,
            15, 16, 17, 18, 19, 20, 21, 22, 23, 24, 25, -1, -1, -1, -1, -1,
            -1, 26, 27, 28, 29, 30, 31, 32, 33, 34, 35, 36, 37, 38, 39, 40,
            41, 42, 43, 44, 45, 46, 47, 48, 49, 50, 51, -1, -1, -1, -1, -1
        )

        ,encode: function(src){
            //用一个数组来存放编码后的字符，效率比用字符串相加高很多。
            var str=[];
            var ch1, ch2, ch3;
            var pos=0;
            //每三个字符进行编码。
            while(pos+3<=src.length){
                ch1=src.charCodeAt(pos++);
                ch2=src.charCodeAt(pos++);
                ch3=src.charCodeAt(pos++);
                str.push(this.enKey.charAt(ch1>>2), this.enKey.charAt(((ch1<<4)+(ch2>>4))&0x3f));
                str.push(this.enKey.charAt(((ch2<<2)+(ch3>>6))&0x3f), this.enKey.charAt(ch3&0x3f));
            }
            //给剩下的字符进行编码。
            if(pos<src.length){
                ch1=src.charCodeAt(pos++);
                str.push(this.enKey.charAt(ch1>>2));
                if(pos<src.length){
                    ch2=src.charCodeAt(pos);
                    str.push(this.enKey.charAt(((ch1<<4)+(ch2>>4))&0x3f));
                    str.push(this.enKey.charAt(ch2<<2&0x3f), '=');
                }else{
                    str.push(this.enKey.charAt(ch1<<4&0x3f), '==');
                }
            }
            //组合各编码后的字符，连成一个字符串。
            return str.join('');
        }

        ,decode: function(src){
            //用一个数组来存放解码后的字符。
            var str=[];
            var ch1, ch2, ch3, ch4;
            var pos=0;
            //过滤非法字符，并去掉'='。
            src=src.replace(/[^A-Za-z0-9+\/]/g, '');
            //decode the source string in partition of per four characters.
            while(pos+4<=src.length){
                ch1=this.deKey[src.charCodeAt(pos++)];
                ch2=this.deKey[src.charCodeAt(pos++)];
                ch3=this.deKey[src.charCodeAt(pos++)];
                ch4=this.deKey[src.charCodeAt(pos++)];
                str.push(String.fromCharCode(
                    (ch1<<2&0xff)+(ch2>>4), (ch2<<4&0xff)+(ch3>>2), (ch3<<6&0xff)+ch4));
            }
            //给剩下的字符进行解码。
            if(pos+1<src.length){
                ch1=this.deKey[src.charCodeAt(pos++)];
                ch2=this.deKey[src.charCodeAt(pos++)];
                if(pos<src.length){
                    ch3=this.deKey[src.charCodeAt(pos)];
                    str.push(String.fromCharCode((ch1<<2&0xff)+(ch2>>4), (ch2<<4&0xff)+(ch3>>2)));
                }else{
                    str.push(String.fromCharCode((ch1<<2&0xff)+(ch2>>4)));
                }
            }
            //组合各解码后的字符，连成一个字符串。
            return str.join('');
        }
        /**
         * 对明文进行Base64加密
         * @param mingwen   明文，字符串
         * @param times     加密次数，数字
         * @param salt      加密盐，字符串
         * @param isURL     是URL格式，布尔
         * @param hasCN     含中文，布尔
         * @return {string}
         */
        ,encodeUtil:function (mingwen, times, salt, isURL, hasCN) {
            var code = salt + mingwen;
            if(code === '') return code;
            if(hasCN) code = escape(code);
            for (var i = 0; i < times; i++) {
                code = this.encode(code);
            }
            if(isURL){
                code = code.split('=')[0];
                code = code.replace('+','-');
                code = code.replace('/','_');
            }
            return code;
        }
        
        ,decodeUtil:function (miwen, times, salt, isURL) {
            var code = '';
            if(miwen === '' && salt !== ''){
                console.log('密文字符串格式错误，不能解密!');
                return code;
            }
            if(miwen === '' && salt === '') return code;
            code = miwen;
            if(isURL){
                code = code.replace('-','+');
                code = code.replace('_','/');
                switch (code.length % 4){
                    case 0:
                        break;
                    case 2:
                        code += '==';
                        break;
                    case 3:
                        code += '=';
                        break;
                    default:
                        console.log('密文字符串格式错误，不能解密!');
                        return '';
                }
            }
            for (var i = 0; i < times; i++) {
                code = this.decode(code);
            }
            code = unescape(code);
            if(code.indexOf(salt) === 0){
                return code.slice(salt.length);
            }else {
                console.log('盐和密文不匹配，不能解密!');
                return '';
            }
        }
    };
})(top,window,document);

