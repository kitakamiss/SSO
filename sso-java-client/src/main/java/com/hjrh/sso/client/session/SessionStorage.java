package com.hjrh.sso.client.session;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpSession;

/**
 * 
 * 功能描述：在客户端保存session信息类
 *
 * @author  吴俊明
 *
 * <p>修改历史：(修改人，修改时间，修改原因/内容)</p>
 */
public class SessionStorage {
	
	/**
	 * 以sessionId为key，session为value。在统一退出的时候获取并销毁session
	 */
	private static final Map<String, HttpSession> SESSION_MAP 
		= new HashMap<String, HttpSession>();

	/**
	 * 保存session信息
	 * @param sessionId 
	 * @param session
	 */
	public  static void put(String sessionId, 
			HttpSession session){
		SESSION_MAP.put(sessionId, session);
	}
	
	/**
	 * 获取session
	 * @param sessionId 
	 */
	public static HttpSession  get(String sessionId){
		return SESSION_MAP.get(sessionId);
	}
	
	/**
	 * 是否存session信息
	 * @param sessionId  
	 */
	public static boolean  containsKey(String sessionId){
		return SESSION_MAP.containsKey(sessionId);
	}
	
	/**
	 * 移除session信息
	 * @param sessionId  
	 */
	public static HttpSession  remove(String sessionId){
		return SESSION_MAP.remove(sessionId);
	}
	
}
