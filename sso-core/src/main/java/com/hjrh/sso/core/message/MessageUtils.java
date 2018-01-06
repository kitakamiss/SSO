package com.hjrh.sso.core.message;

import java.util.HashMap;
import java.util.Map;

import com.hjrh.sso.core.exception.EmptyCredentialException;
import com.hjrh.sso.core.exception.InvalidCredentialException;
import com.hjrh.sso.core.exception.InvalidEncryCredentialException;
import com.hjrh.sso.core.exception.NoAuthenticationPostHandlerException;
import com.hjrh.sso.core.exception.NoSsoKeyException;
import com.hjrh.sso.core.exception.PasswordInvalidException;
import com.hjrh.sso.core.exception.UnsupportedCredentialsException;
import com.hjrh.sso.core.exception.UsernameOrPasswordEmptyException;
import com.hjrh.sso.core.exception.UsernameOrPasswordInvalidException;

/**
 * 
 * 功能描述：使用简单的国际化策略，只支持中文，以后可以改成更加灵活的能够支持多种语言的消息提示
 *
 * @author  吴俊明
 *
 * <p>修改历史：(修改人，修改时间，修改原因/内容)</p>
 */
public class MessageUtils {
	
	/**
	 * 信息表，其中key是消息键，value是具体对应的中文的消息内容。
	 */
	private static Map<String, String> msgMap = null;
	
	static{
		//初始化消息表。
		msgMap = new HashMap<String, String>();
		msgMap.put(InvalidCredentialException.MSG_KEY, "不合法的凭据");
		msgMap.put(InvalidEncryCredentialException.MSG_KEY, "不合法的加密凭据");
		msgMap.put(UsernameOrPasswordEmptyException.MSG_KEY, "用户名或者密码为空");
		msgMap.put(UsernameOrPasswordInvalidException.MSG_KEY, "用户名或者密码不正确");
		msgMap.put(PasswordInvalidException.MSG_KEY, "用户名或者密码不正确");
		msgMap.put(EmptyCredentialException.MSG_KEY, "凭据为空");
		msgMap.put(UnsupportedCredentialsException.MSG_KEY, "不支持的用户凭据");
		msgMap.put(NoAuthenticationPostHandlerException.MSG_KEY, "无合法的认证后处理器");
		msgMap.put(NoSsoKeyException.MSG_KEY, "系统没有配置sso服务器本身的key信息，请检查配置。");
	}
	
	
	/**
	 * 查询消息键对应的消息提示内容。
	 * @param key 消息键
	 * @return 消息内容。
	 */
	public static String getMessage(String key){
		return msgMap.get(key);
	}
}
