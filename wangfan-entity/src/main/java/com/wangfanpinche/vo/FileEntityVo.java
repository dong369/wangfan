package com.wangfanpinche.vo;


public class FileEntityVo{
	
	private String id;
	
	private String md5;//文件md5

	private String filename;//文件名称

	private String oldFilename;//原始文件名

	private Long filesize;//文件大小

	private String path;//存放路径

	private String url;//下载链接

	private String extension;//扩展名

	private String userIp;//用户IP
	
	private String userId;//用户Id

	public FileEntityVo(String id, String md5, String filename, String oldFilename, Long filesize, String path, String url, String extension, String userIp) {
		super();
		this.id = id;
		this.md5 = md5;
		this.filename = filename;
		this.oldFilename = oldFilename;
		this.filesize = filesize;
		this.path = path;
		this.url = url;
		this.extension = extension;
		this.userIp = userIp;
	}

	public FileEntityVo() {
		super();
	}

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

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
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

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	

}
