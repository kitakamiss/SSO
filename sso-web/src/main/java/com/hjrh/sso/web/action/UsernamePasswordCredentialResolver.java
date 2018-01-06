package com.hjrh.sso.web.action;

import javax.servlet.http.HttpServletRequest;

import com.hjrh.sso.core.authentication.Credential;
import com.hjrh.sso.core.authentication.UsernamePasswordCredential;
import com.hjrh.sso.web.util.RSALoginService;

/**
 * 
 * 功能描述：用户名和密码凭据解析器，从参数中解析出用户的用户名和密码信息
 *
 * @author  吴俊明
 *
 * <p>修改历史：(修改人，修改时间，修改原因/内容)</p>
 */
public class UsernamePasswordCredentialResolver extends AbstractParameterCredentialResolver{
	
	/**
	 * 用户名的参数名。
	 */
	public static final String USERNAME_PARAM_NAME = "username";
	
	/**
	 * 密码的参数名。
	 */
	public static final String PASSWORD_PARAM_NAME = "password";

	@Override
	public Credential doResolveCredential(HttpServletRequest request) {
		if(request!=null && request.getParameter(USERNAME_PARAM_NAME)!=null && 
				request.getParameter(PASSWORD_PARAM_NAME)!=null){
			UsernamePasswordCredential credential = new UsernamePasswordCredential();
			credential.setUsername(request.getParameter(USERNAME_PARAM_NAME));
			
			String password;
			try {
				password = RSALoginService.decryptParameter(request.getParameter(PASSWORD_PARAM_NAME));
				credential.setPassword(password);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			return credential;
		}
		return null;
	}
}
