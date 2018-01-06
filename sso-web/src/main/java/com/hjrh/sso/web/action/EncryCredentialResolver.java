package com.hjrh.sso.web.action;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;
import org.springframework.util.StringUtils;

import com.hjrh.sso.core.authentication.Credential;
import com.hjrh.sso.core.authentication.EncryCredential;
import com.hjrh.sso.web.utils.WebConstants;

/**
 * 
 * 功能描述：经过认证加密后的凭据信息解析器，从http请求的cookie中解析出对应的加密后的凭据信息
 *
 * @author  吴俊明
 *
 * <p>修改历史：(修改人，修改时间，修改原因/内容)</p>
 */
public class EncryCredentialResolver implements CredentialResolver {
	
	private static final Logger LOGGER = Logger.getLogger(EncryCredentialResolver.class);

	@Override
	public Credential resolveCredential(HttpServletRequest request) {
		if(request!=null){
			//查找请求中的cookie。
			Cookie[] cookies = request.getCookies();
			if(cookies!=null){
				String value = null;
				for(Cookie cookie:cookies){
					//若查找到sso写入的cookie值。
					if(cookie!=null && cookie.getName().equalsIgnoreCase(WebConstants.sso_SERVER_ENCRYPTED_CREDENTIAL_COOKIE_KEY)){
						value = cookie.getValue();
						break;
					}
				}
				//如果cookie中没有凭据值，则从请求参数中获取凭据值。
				if(StringUtils.isEmpty(value)){
					LOGGER.info("the sso_SERVER_EC value is: "+value);
					value = request.getParameter(WebConstants.sso_SERVER_ENCRYPTED_CREDENTIAL_COOKIE_KEY);
				}
				//最终如果加密凭据有值，则直接返回凭据对象。
				if(!StringUtils.isEmpty(value)){
					//去除空串后返回。
					return new EncryCredential(value.trim());
				}
			}
		}
		return null;
	}

}
