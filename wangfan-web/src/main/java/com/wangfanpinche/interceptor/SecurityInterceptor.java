package com.wangfanpinche.interceptor;

import java.util.Enumeration;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import com.wangfanpinche.config.SessionUtils;
import com.wangfanpinche.dto.User;
import com.wangfanpinche.service.UserService;
import com.wangfanpinche.vo.SessionUser;


public class SecurityInterceptor implements HandlerInterceptor {
	
	@Autowired
	private UserService userService;
	
	private List<String> excludeUrls;// 不需要拦截的资源
	/**
	 * 完成页面的render后调用
	 */
	@Override
	public void afterCompletion(HttpServletRequest arg0, HttpServletResponse arg1, Object arg2, Exception arg3) throws Exception {
		// TODO Auto-generated method stub

	}

	/**
	 * 在调用controller具体方法后拦截
	 */
	@Override
	public void postHandle(HttpServletRequest arg0, HttpServletResponse arg1, Object arg2, ModelAndView arg3) throws Exception {
		// TODO Auto-generated method stub

	}

	/**
	 * 在调用controller具体方法前拦截
	 */
	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object object) throws Exception {
		String requestUri = request.getRequestURI();
		String contextPath = request.getContextPath();
		String url = requestUri.substring(contextPath.length());
//		boolean b = true;
//		if(b){
//			return b;
//		}
		if (url.indexOf("/baseController/") > -1 || excludeUrls.contains(url)) {// 如果要访问的资源是不需要验证的
			return true;
		}
		SessionUser sessionUser = (SessionUser) request.getSession().getAttribute(SessionUtils.sessionUser);
		
		if (sessionUser == null || sessionUser.getId().equalsIgnoreCase("")) {// 如果没有登录或登录超时
			String token  = getHeadersPtoken(request);
			
			if(!StringUtils.hasText(token)){
				request.setAttribute("msg", "您还没有登录或登录已超时，请重新登录，然后再刷新本功能！");
				response.setStatus(403);
				request.getRequestDispatcher("/user/loginByPc").forward(request, response);
				return false;
			}
			
			User u = userService.getUserByPhoneToken(token);
			if(u == null){
				request.setAttribute("msg", "header中的ptoken错误,没有找到此ptoken");
				response.setStatus(403);
				request.getRequestDispatcher("/user/loginByPc").forward(request, response);
				return false;
			}
			
			sessionUser = new SessionUser();
			BeanUtils.copyProperties(u, sessionUser);
			sessionUser.setResourceList(userService.getResourceList(u.getId()));
			request.getSession().setAttribute(SessionUtils.sessionUser, sessionUser);
			return true;
		}

		if (!sessionUser.getResourceList().contains(url)) {// 如果当前用户没有访问此资源的权限
			List<String> resourceList = sessionUser.getResourceList();
			for (String string : resourceList) {
				if(url.startsWith(string)){
					return true;
				}
			}
			request.setAttribute("msg", "您没有访问此资源的权限！<br/>请联系超管赋予您<br/>[" + url + "]<br/>的资源访问权限！");
			response.setStatus(403);
			request.getRequestDispatcher("/error/noSecurity.jsp").forward(request, response);
			return false;
		}
		
		return true;
	}
	
	
	
	private String getHeadersPtoken(HttpServletRequest request) {
        Enumeration<String> headerNames = request.getHeaderNames();
        while (headerNames.hasMoreElements()) {
            String key = (String) headerNames.nextElement();
            String value = request.getHeader(key);
            if("ptoken".equals(key)){
            	return value; 
            }
        }
        return null;
    }

	public List<String> getExcludeUrls() {
		return excludeUrls;
	}

	public void setExcludeUrls(List<String> excludeUrls) {
		this.excludeUrls = excludeUrls;
	}

}
