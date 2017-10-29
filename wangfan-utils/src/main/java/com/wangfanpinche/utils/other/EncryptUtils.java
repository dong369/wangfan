package com.wangfanpinche.utils.other;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;

/**
 * 加密工具类（包含 摘要和加密）
 * 摘要加密支持以下<code>MD5，SHA-1</code>
 * @author 马凯
 *
 */
public class EncryptUtils {

	/**
	 * md5加密
	 * 
	 * @param str
	 * @return
	 */
	public static String md5(String str) {
		return encrypt(str, "MD5");
	}
	
	/**
	 * SHA-1加密
	 * 
	 * @param str
	 * @return
	 */
	public static String sha1(String str) {
		return encrypt(str, "SHA-1" );
	}

	private static String encrypt(String str, String type){
		try {
			MessageDigest md = MessageDigest.getInstance(type);
			md.update(str.getBytes());
			byte[] byteDigest = md.digest();
			int i;
			StringBuffer buf = new StringBuffer("");
			for (int offset = 0; offset < byteDigest.length; offset++) {
				i = byteDigest[offset];
				if (i < 0)
					i += 256;
				if (i < 16)
					buf.append("0");
				buf.append(Integer.toHexString(i));
			}
			return buf.toString();
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
			return null;
		}
	}

	/**
	 * base64加密
	 * @param str
	 * @return
	 */
	public static String toBase64(String str){
		try {
			return Base64.getEncoder().encodeToString(str.getBytes("utf-8"));
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
			return null;
		}
	}

	/**
	 * Base64解密
	 * @param str
	 * @return
	 */
	public static String fromBase64(String str){
		byte[] decode = Base64.getDecoder().decode(str);
		try {
			return new String(decode, "utf-8");
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
			return null;
		}
	}
}
