package com.hjrh.sso.client.handler;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * 本应用登出处理接口。
 * @author Administrator
 *
 */
public interface AppClientLogoutHandler {
	
	/**
	 * 登出本应用。
	 * @param request http请求对象。
	 * @param response http响应对象。
	 * @param userId 用户需要登出的用户Id值。
	 */
	public void logoutClient(HttpServletRequest request, HttpServletResponse response, String userId);


}
