package com.wangfanpinche.utils.file;

public class MkFile {

	/**
	 * 文件md5
	 */
	private String md5;

	/**
	 * 文件名称
	 */
	private String filename;

	/**
	 * 原始文件名
	 */
	private String oldFilename;

	/**
	 * 文件大小
	 */
	private Long filesize;

	/**
	 * 存放路径
	 */
	private String path;

	/**
	 * 下载链接
	 */
	private String url;

	/**
	 * 扩展名
	 */
	private String extension;

	/**
	 * 用户IP
	 */
	private String userIp;

	public String getMd5() {
		return md5;
	}

	public void setMd5(String md5) {
		this.md5 = md5;
	}

	public String getFilename() {
		return filename;
	}

	public void setFilename(String filename) {
		this.filename = filename;
	}

	public String getOldFilename() {
		return oldFilename;
	}

	public void setOldFilename(String oldFilename) {
		this.oldFilename = oldFilename;
	}

	public Long getFilesize() {
		return filesize;
	}

	public void setFilesize(Long filesize) {
		this.filesize = filesize;
	}

	public String getPath() {
		return path;
	}

	public void setPath(String path) {
		this.path = path;
	}

	public String getExtension() {
		return extension;
	}

	public void setExtension(String extension) {
		this.extension = extension;
	}

	public String getUserIp() {
		return userIp;
	}

	public void setUserIp(String userIp) {
		this.userIp = userIp;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

}
