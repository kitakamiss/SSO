package com.hjrh.sso.web.utils;

/**
 * 
 * 功能描述：web相关的常量类，定义了与web相关的所有常量值
 *
 * @author  吴俊明
 *
 * <p>修改历史：(修改人，修改时间，修改原因/内容)</p>
 */
public interface WebConstants {
	
	/**
	 * sso中心认证服务器写入到用户web客户端cookie中的认证加密后的凭据的键名称。
	 */
	public static final String sso_SERVER_ENCRYPTED_CREDENTIAL_COOKIE_KEY = "SERVER_EC";
	
	/**
	 * sso客户端应用服务器写入到用户web客户端cookie中的认证加密后的凭据的键名称。
	 */
	public static final String sso_CLIENT_ENCRYPTED_CREDENTIAL_COOKIE_KEY = "CLIENT_EC";
	
	/**
	 * 目的服务地址service的参数名。
	 */
	public static final String SERVICE_PARAM_NAME = "service";
	
	/**
	 * 目的服务地址存储在session中的key值。
	 */
	public static final String sso_SERVICE_KEY_IN_SESSION = "SERVICE_KEY";
	
	/**
	 * 用户标识的参数名。
	 */
	public static final String USER_ID_PARAM_NAME = "userId";

}
