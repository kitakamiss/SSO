package com.hjrh.sso.core.authentication;

import com.hjrh.sso.core.authentication.Credential;
import com.hjrh.sso.core.exception.InvalidCredentialException;

/**
 * 
 * 功能描述：认证管理器，负责对用户凭证进行有效性认证
 *
 * @author  吴俊明
 *
 * <p>修改历史：(修改人，修改时间，修改原因/内容)</p>
 */
public interface AuthenticationManager {
	
	/**
	 * 
	 * 功能描述：对用户凭据进行认证，若认证失败则抛出异常，若成功返回认证结果
	 *
	 * @author  吴俊明
	 * <p>创建日期 ：2017年12月26日 下午6:20:14</p>
	 *
	 * @param credential  用户凭据
	 * @return   当认证通过后，返回认证结果
	 * @throws InvalidCredentialException        当输入的凭据不合法的时候会抛出该异常
	 *
	 * <p>修改历史 ：(修改人，修改时间，修改原因/内容)</p>
	 */
	public Authentication authenticate(Credential credential) throws InvalidCredentialException;

}
