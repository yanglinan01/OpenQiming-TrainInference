package com.ctdi.cnos.llm.util;

import org.apache.shiro.codec.Base64;
import org.apache.shiro.crypto.hash.Md5Hash;

/**
 * 加密工具类
 * 
 * @author lw
 *
 */
public class CryptographyUtil {
	/**
	 * base64加密
	 * 
	 * @param str
	 * @return
	 */
	public static String encBase64(String str) {
		return Base64.encodeToString(str.getBytes());
	}

	/**
	 * base64解密
	 * 
	 * @param str
	 * @return
	 */
	public static String decBase64(String str) {
		return Base64.decodeToString(str);
	}

	/**
	 * Md5加密 Shiro中自带MD5没有解密
	 * 
	 * @param str  要加密的值
	 * @param salt 拌料
	 * @return
	 */
	public static String md5(String str, String salt) {
		return new Md5Hash(str, salt).toString();
	}

	public static String md5(String str) {
		return new Md5Hash(str).toString();
	}
}
