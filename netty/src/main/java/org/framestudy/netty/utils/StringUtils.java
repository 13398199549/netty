package org.framestudy.netty.utils;
/**
 * String字符串处理工具类
 * @author Administrator
 *
 */
public class StringUtils {
	/**
	 * 判断字符串是否为空
	 * @param string
	 * @return
	 */
	public static boolean isEmpty(String string) {
		if(string == null || "".equals(string)) {
			return true;
		}
		return false;
	}
}
