package com.hjrh.sso.core.exception;

public class NoSsoKeyException extends AuthenticationException{

	/**
	 * 
	 */
	private static final long serialVersionUID = 115935182258831210L;

	public static final NoSsoKeyException INSTANCE = new NoSsoKeyException();
	
	/**
	 * 异常代码值。
	 */
	public static final String CODE = "NO.sso.KEY.CODE";
	
	/**
	 * 异常信息键值，要转换为具体的语言值。
	 */
	public static final String MSG_KEY = "NO.sso.KEY.MSG";
	
	public NoSsoKeyException(String code, String msgKey) {
		super(code, msgKey);
	}
	
	public NoSsoKeyException() {
		super(CODE, MSG_KEY);
	}

}
