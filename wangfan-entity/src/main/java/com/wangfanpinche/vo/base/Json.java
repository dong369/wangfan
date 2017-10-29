package com.wangfanpinche.vo.base;

/**
 * JSON对象类
 * @author kevin
 * @date 2016-09-28 16:06:16
 */
public class Json {
	private boolean success;
	private String message;
	private Object obj;
	
	public Json() {
	}
	
	

	public Json(boolean success, String message, Object obj) {
		super();
		this.success = success;
		this.message = message;
		this.obj = obj;
	}



	public boolean isSuccess() {
		return success;
	}

	public void setSuccess(boolean success) {
		this.success = success;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public Object getObj() {
		return obj;
	}

	public void setObj(Object obj) {
		this.obj = obj;
	}

}
