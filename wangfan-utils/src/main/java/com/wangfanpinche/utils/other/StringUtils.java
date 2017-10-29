package com.wangfanpinche.utils.other;

import java.io.UnsupportedEncodingException;

public class StringUtils {

	public static String ascConveterUTF8(String str){
		String string = null;
		try {
			string = new String(str.getBytes("ISO-8859-1"), "UTF-8");
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		return string;
	}

}
