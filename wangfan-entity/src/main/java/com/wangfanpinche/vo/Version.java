package com.wangfanpinche.vo;

/**
 * 版本号
 * @version 2.0
 */
public class Version {
	
	public final static int MajorVersion = 2;
	public final static int MinorVersion = 0;
	public final static int RevisionVersion = 0;
	
	public final static String IosUrl = "http://app.wangfanpinche.com:8888/ios.ipa";
	public final static String AndroidUrl = "http://app.wangfanpinche.com:8888/wangfan.apk";

	/**
	 * 获取版本号
	 * @return
	 */
	public static String getVersionNumber() {
		return Version.MajorVersion + "." + Version.MinorVersion + "." + Version.RevisionVersion;
	}

	/**
	 * 获取IOS的APP下载地址
	 * @return
	 */
	public static String getIosDownloadUrl(){
		return IosUrl;
	}
	
	/**
	 * 获取Android下载地址
	 * @return
	 */
	public static String getAndroidDownloadUrl(){
		return AndroidUrl;
	}

}
