package com.hjrh.sso.core.authentication;

import com.hjrh.sso.core.authentication.Credential;

/**
 * 
 * 功能描述：认证成功后的处理器，该接口的职责是将
 * 用户认证主体，用户凭据转换为一个合适的
 * 认证结果对象。根据用户凭据中的信息，参数
 * 进行合适的转换。
 * @author  吴俊明
 *
 * <p>修改历史：(修改人，修改时间，修改原因/内容)</p>
 */
public interface AuthenticationPostHandler {
	
	/**
	 * sso服务本身的加密凭据信息存储在验证结果对象authentication的动态属性attributes的key值。
	 */
	public static final String sso_SERVER_EC_KEY = "ser_ec_key";
	
	/**
	 * sso服务本身的加密凭据信息存储在验证结果对象
	 * authentication的动态属性attributes的key值。
	 */
	public static final String sso_CLIENT_EC_KEY = "cli_ec_key";
	
	/**
	 * 
	 * 功能描述：认证后的处理方法，将用户的凭据和主体转换为一个认证结果对象
	 *
	 * @author  吴俊明
	 * <p>创建日期 ：2017年11月29日 下午7:56:35</p>
	 *
	 * @param credential 用户凭据
	 * @param principal 用户主体
	 * @return 认证结果对象信息
	 *
	 * <p>修改历史 ：(修改人，修改时间，修改原因/内容)</p>
	 */
	public Authentication postAuthentication(Credential credential, Principal principal);

}
