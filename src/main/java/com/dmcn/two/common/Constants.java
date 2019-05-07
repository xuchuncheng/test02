package com.dmcn.two.common;

import java.nio.charset.Charset;

/**
 * 常量定义
 */
public class Constants {
    /** 编码UTF-8 */
    public static final String CHARSET_UTF = "UTF-8";
    /** 编码GB2312 */
	public static final Charset GB2312 = Charset.forName("GB2312");
    /**字符编码UTF-8*/
    public static final Charset UTF8 = Charset.forName("UTF-8");
    /**字符编码ISO-8859-1*/
    public static final Charset ISO8859 = Charset.forName("ISO-8859-1");
    /**状态*/
    public static final String CODE = "code";
    /** 提示消息 */
    public static final String MSG = "msg";
    /** 错误消息 */
    public static final String ERR = "err";
    /** 默认错误编码 */
    public static final String ERR_CODE = "errCode";
    /** 默认错误信息 */
    public static final String ERR_MSG = "errMsg";
    /** 数据内容 */
    public static final String DATA = "data";
    /** session中UserView对象的名字 */
	public static final String LOGIN_USER = "loginUser";
    
    

}
