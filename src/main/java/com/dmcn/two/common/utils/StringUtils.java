package com.dmcn.two.common.utils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Collection;
import java.util.Map;
import java.util.UUID;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 字符串工具类
 */
public class StringUtils {

	/** 记录日志 */
	private static Logger logger = LoggerFactory.getLogger(StringUtils.class);
	
	/**
	 * 是否为空判断(trim)
	 * 
	 * @param obj
	 * @return
	 */
    public static boolean isBlank(Object obj){
        if (obj == null)
            return true;
        if (obj instanceof CharSequence)
            return ((CharSequence) obj).toString().trim().equals("");
        if (obj instanceof Collection)
            return ((Collection) obj).isEmpty();
        if (obj instanceof Map)
            return ((Map) obj).isEmpty();
        if (obj instanceof Object[]) {
            Object[] object = (Object[]) obj;
            if (object.length == 0) {
                return true;
            }
            boolean empty = true;
            for (int i = 0; i < object.length; i++) {
                if (!isEmpty(object[i])) {
                    empty = false;
                    break;
                }
            }
            return empty;
        }
        return false;
    }
	
	/**
	 * 是否为空判断(trim)
	 * 
	 * @param str
	 * @return
	 */
	public static boolean isNotBlank(Object obj) {
		return !isBlank(obj);
	}
	   
    /**
     * 是否为空判断(trim)
     * 
     * @param args
     * @return
     */
    public static boolean isBlank(Object ...args) {
        for (int i = 0; i < args.length; i++) {
            if(isBlank(args[i])){
                return true;
            }
        }
        return false;
    }
    	
	/**
	 * 是否为空判断(无trim)
	 * 
	 * @param obj
	 * @return
	 */
    public static boolean isEmpty(Object obj){
        if (obj == null)
            return true;
        if (obj instanceof CharSequence)
            return ((CharSequence) obj).length() == 0;
        if (obj instanceof Collection)
            return ((Collection) obj).isEmpty();
        if (obj instanceof Map)
            return ((Map) obj).isEmpty();
        if (obj instanceof Object[]) {
            Object[] object = (Object[]) obj;
            if (object.length == 0) {
                return true;
            }
            boolean empty = true;
            for (int i = 0; i < object.length; i++) {
                if (!isEmpty(object[i])) {
                    empty = false;
                    break;
                }
            }
            return empty;
        }
        return false;
    }
    
	/**
	 * 是否为空判断(无trim)
	 * 
	 * @param obj
	 * @return
	 */
    public static boolean isNotEmpty(Object obj){
        return !isEmpty(obj);
    }
    
    /**
     * 是否为空判断(无trim)
     * 
     * @param args
     * @return
     */
    public static boolean isEmpty(Object ...args) {
        for (int i = 0; i < args.length; i++) {
            if(isEmpty(args[i])){
                return true;
            }
        }
        return false;
    }
    
	/**
	 * 字段串数组第一组不为空
	 * 
	 * @param ss
	 * @return
	 */
	public static boolean firstNotEmpty(final String[] ss) {
		return ss != null && ss.length > 0 && ss[0] != null
				&& ss[0].length() > 0;
	}

	/**
	 * 将Object型转换为字符串
	 * 
	 * @param str
	 * @return
	 */
	public static String valueOf(Object o) {
		if (o == null) {
			return null;
		}
		String s = null;
		if (o instanceof Number) {
			s = String.valueOf(o);
		} else {
			s = o.toString();
		}
		return s;
	}

	/**
	 * 取数组中的第一组值
	 * 
	 * @param obj
	 * @return
	 */
	public static String getFirstString(Object obj) {
		if (obj == null) {
			return "";
		}
		String s = null;
		if (obj instanceof String[]) {
			String[] ss = (String[]) obj;
			s = ss[0];
		} else if (obj instanceof String) {
			s = (String) obj;
		} else {
			s = obj.toString();
		}
		return s;
	}

	/**
	 * 获取数组的第一个元素，并且作了字符串null、""的判断，这两种情况都处理为null
	 * 
	 * @param obj
	 * @return
	 */
	public static String getFirstStr(Object obj) {
		if (obj == null) {
			return null;
		}
		String tmp = null;
		if (obj instanceof String[]) {
			String[] ss = (String[]) obj;
			tmp = ss[0];
		} else if (obj instanceof String) {
			tmp = ((String) obj).trim();
		}
		if ("".equals(tmp)) {
			tmp = null;
		}
		return tmp;
	}

	/**
	 * 功能描述: 驼峰字符串转换成下划线连接<br>
	 * 例如: nextValueMySql 转换为 next_value_my_sql
	 * 
	 * @param param
	 * @return
	 */
	public static String camelVunderline(String param) {
		Pattern p = Pattern.compile("[A-Z]");
		if (param == null || param.equals("")) {
			return "";
		}
		StringBuilder builder = new StringBuilder(param);
		Matcher mc = p.matcher(param);
		int i = 0;
		while (mc.find()) {
			builder.replace(mc.start() + i, mc.end() + i, "_"
					+ mc.group().toLowerCase());
			i++;
		}
		if ('_' == builder.charAt(0)) {
			builder.deleteCharAt(0);
		}
		return builder.toString();
	}

	/**
	 * 对String进行trim操作，并将Null处理为空字符串
	 * 
	 * @param str
	 * @return
	 */
	public static String trimToEmpty(String str) {
		if (str == null) {
			return "";
		}
		return str.trim();
	}

	/**
	 * 删除最后的逗号
	 */
	public static StringBuilder removeLastChar(String sb) {
		if (sb.trim().endsWith(",")) {
			sb = sb.substring(0, sb.lastIndexOf(","));
		}
		return new StringBuilder(sb);
	}

	/**
	 * 去掉连接线的UUID
	 * 
	 * @return
	 */
	public static String getUuid() {
		return UUID.randomUUID().toString().replaceAll("-", "");
	}

	/**
	 * 判断字符串是否为数字
	 * 
	 * @param str
	 * @return
	 */
	public static boolean isNumeric(String str) {
		Pattern pattern = Pattern.compile("[0-9]*");
		Matcher isNum = pattern.matcher(str);
		return isNum.matches();
	}

	/**
	 * 删除字符串中的空白
	 */
	public static String removeBlank(String sb) {
		sb = sb.replaceAll("\\s*", "");
		return sb;
	}

	/**
	 * 验证出生年月(19/20开头四位数字，0-12月)
	 * 
	 * @param str
	 * @return
	 */
	public static boolean checkBirthday(String str) {
		Pattern pattern = Pattern.compile("^(19|20)\\d{2}(0[1-9]|1[0-2])$");
		Matcher isTrue = pattern.matcher(str);
		return isTrue.matches();
	}

	/**
	 * 验证E-mail
	 * 
	 * @param str
	 * @return
	 */
	public static boolean checkEmail(String str) {
		Pattern pattern = Pattern
				.compile(
						"^([a-z0-9]*[-_.]?[a-z0-9]+)*@([a-z0-9]*[-_]?[a-z0-9]+)+[\\.][a-z0-9]{2,4}([\\.][a-z]{2,3})?$",
						Pattern.CASE_INSENSITIVE);
		Matcher isTrue = pattern.matcher(str);
		return isTrue.matches();
	}

	/**
	 * 验证手机号码
	 * 
	 * @param str
	 * @return
	 */
	public static boolean checkMobile(String str) {
		Pattern pattern = Pattern.compile("^1(3|4|5|7|8)\\d{9}$");
		Matcher isTrue = pattern.matcher(str);
		return isTrue.matches();
	}

	/**
	 * 验证手机号码和座机
	 * 
	 * @param str
	 * @return
	 */
	public static boolean checkTel(String str) {

		Pattern pattern = Pattern
				.compile("^1(3|4|5|7|8)\\d{9}$|^0\\d{2,3}-?\\d{7,8}$|^0\\d{2,3}-?\\d{7,8}-\\d{1,5}?$");
		Matcher isTrue = pattern.matcher(str);
		return isTrue.matches();
	}
}
