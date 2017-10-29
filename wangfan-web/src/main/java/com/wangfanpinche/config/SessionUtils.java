package com.wangfanpinche.config;

import javax.servlet.http.HttpSession;

import com.wangfanpinche.vo.SessionUser;

public class SessionUtils {
	
	public static final String sessionUser = "userSession";

	
	public static SessionUser getUser(HttpSession session){
		return (SessionUser) session.getAttribute(sessionUser);
	}
	
	public static String getUserId(HttpSession session){
		SessionUser user = getUser(session);
		if(user == null){
			return null;
		}
		return user.getId();
	}
	

}
