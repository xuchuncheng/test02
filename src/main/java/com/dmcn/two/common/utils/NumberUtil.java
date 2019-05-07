package com.dmcn.two.common.utils;

import java.math.BigDecimal;

/**
 * Title: NumberUtil.java Description: 数字处理工具类
 */
public class NumberUtil {

	/**
	 * @discription 转换为字符串
	 */
	public static String toString(Number num) {
		if (num == null) {
			return null;
		}
		return num.toString();
	}

	/**
	 * 将Object型转换Short
	 * 
	 * @param o
	 * @return
	 */
	public static Short dealShort(Object o) {
		if (o == null) {
			return null;
		} else if (o instanceof Number || o instanceof CharSequence) {
			try {
				return Short.parseShort(o.toString().trim());
			} catch (Exception e) {
				return null;
			}
		}
		return null;
	}

	/**
	 * 将Object型转换Integer
	 * 
	 * @param o
	 * @return
	 */
	public static Integer dealInteger(Object o) {
		if (o == null) {
			return null;
		}else if (o instanceof Number || o instanceof CharSequence) {
			try {
				return Integer.parseInt(o.toString().trim());
			} catch (Exception e) {
				return null;
			}
		} 
		return null;
	}

	/**
	 * 将Object型转换Long
	 * 
	 * @param o
	 * @return
	 */
	public static Long dealLong(Object o) {
		if (o == null) {
			return null;
		} else if (o instanceof Number || o instanceof CharSequence) {
			try {
				return Long.parseLong(o.toString().trim());
			} catch (Exception e) {
				return null;
			}
		}
		return null;
	}

	/**
	 * 将Object型转换long
	 * 
	 * @param o
	 * @return
	 */
	public static long longOf(Object o) {
		long v = 0;
		if (o == null) {
			return v;
		}else if (o instanceof Number || o instanceof CharSequence) {
			try {
				return Long.parseLong(o.toString().trim());
			} catch (Exception e) {
				return v;
			}
		} 
		return v;
	}

	/**
	 * 将Object型转换Float
	 * 
	 * @param o
	 * @return
	 */
	public static Float dealFloat(Object o) {
		if (o == null) {
			return null;
		}else if (o instanceof Number || o instanceof CharSequence) {
			try {
				return Float.parseFloat(o.toString().trim());
			} catch (Exception e) {
				return null;
			}
		} 
		return null;
	}

	/**
	 * 将Object型转换Double
	 * 
	 * @param o
	 * @return
	 */
	public static Double dealDouble(Object o) {
		if (o == null) {
			return null;
		}else if (o instanceof Number || o instanceof CharSequence) {
			try {
				return Double.parseDouble(o.toString().trim());
			} catch (Exception e) {
				return null;
			}
		} 
		return null;
	}

	/**
	 * 将Object型转换BigD
	 * 
	 * @param o
	 * @return
	 */
	public static BigDecimal dealBigDecimal(Object o) {
		if (o == null) {
			return null;
		} else if (o instanceof BigDecimal) {
			return (BigDecimal) o;
		} else if (o instanceof Number || o instanceof CharSequence) {
			try {
				return new BigDecimal(o.toString().trim());
			} catch (Exception e) {
				return null;
			}
		}
		return null;
	}
}
