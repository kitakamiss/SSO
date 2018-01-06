package com.hjrh.sso.core.service;

import java.io.Serializable;

import com.hjrh.sso.core.authentication.Authentication;

/**
 * 
 * 功能描述：登录结果对象
 *
 * @author  吴俊明
 *
 * <p>修改历史：(修改人，修改时间，修改原因/内容)</p>
 */
public class LoginResult implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 2497393519640031346L;

	/**
	 * 认证是否成功，
	 */
	private boolean success;

	/**
	 * 认证失败的错误代码。
	 */
	private String code;

	/**
	 * 认证失败的错误提示信息键值。
	 */
	private String msgKey;

	/**
	 * 认证结果信息对象。
	 */
	private Authentication authentication;

	public boolean isSuccess() {
		return success;
	}

	public void setSuccess(boolean success) {
		this.success = success;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getMsgKey() {
		return msgKey;
	}

	public void setMsgKey(String msgKey) {
		this.msgKey = msgKey;
	}

	public Authentication getAuthentication() {
		return authentication;
	}

	public void setAuthentication(Authentication authentication) {
		this.authentication = authentication;
	}
}
