package com.hjrh.sso.core.authentication;

import java.util.List;

import org.apache.log4j.Logger;

import com.hjrh.sso.core.authentication.handlers.AuthenticationHandler;
import com.hjrh.sso.core.authentication.resolvers.CredentialToPrincipalResolver;
import com.hjrh.sso.core.exception.AuthenticationException;
import com.hjrh.sso.core.exception.EmptyCredentialException;
import com.hjrh.sso.core.exception.InvalidCredentialException;
import com.hjrh.sso.core.exception.NoAuthenticationPostHandlerException;
import com.hjrh.sso.core.exception.UnsupportedCredentialsException;

/**
 * 
 * 功能描述：认证管理器默认的实现类
 *
 * @author  吴俊明
 *
 * <p>修改历史：(修改人，修改时间，修改原因/内容)</p>
 */
public class AuthenticationManagerImpl implements AuthenticationManager {
	
	private static final Logger logger = Logger.getLogger(AuthenticationManagerImpl.class);
	
	/**
	 * 认证处理器集合，使用多个认证处理器对凭证逐一校验，只要有一个通过即可。
	 */
	private List<AuthenticationHandler> authenticationHandlers;
	
	/**
	 * 用户凭据转为用户主体的解析器对象。
	 */
	private List<CredentialToPrincipalResolver> credentialToPrincipalResolvers;
	
	/**
	 * 认证成功后处理对象。
	 */
	private AuthenticationPostHandler authenticationPostHandler;
	

	public void setAuthenticationPostHandler(
			AuthenticationPostHandler authenticationPostHandler) {
		this.authenticationPostHandler = authenticationPostHandler;
	}

	public void setCredentialToPrincipalResolvers(
			List<CredentialToPrincipalResolver> credentialToPrincipalResolvers) {
		this.credentialToPrincipalResolvers = credentialToPrincipalResolvers;
	}

	public void setAuthenticationHandlers(
			List<AuthenticationHandler> authenticationHandlers) {
		this.authenticationHandlers = authenticationHandlers;
	}
	
	public AuthenticationManagerImpl(){

	}


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
	public Authentication authenticate(Credential credential) throws InvalidCredentialException{
		//是否找到支持的凭据认证处理器。
		boolean foundSupported = false;
		//是否认证通过。
        boolean authenticated = false;
		//若凭据为空，则跑出异常。
		if(credential==null){
			logger.info("credential is null");
			throw new EmptyCredentialException();
		}
		
		//初始化的认证异常信息。
		AuthenticationException unAuthSupportedHandlerException = InvalidCredentialException.INSTANCE; 
		
		//循环调用所有的认证处理器。
		if(authenticationHandlers!=null && authenticationHandlers.size()>0){
			for(AuthenticationHandler handler:authenticationHandlers){
				//认证处理器是否支持该凭据。
				if(handler.supports(credential)){
					foundSupported = true;
					try {
						authenticated = handler.authenticate(credential);
						//若认证成功，则跳出循环。
						if(authenticated){
							break;
						}
					}catch (AuthenticationException e) {
						logger.error("authenticate error", e);
						unAuthSupportedHandlerException = e;
					}
					
				}
			}
		}
		//未找到支持的认证处理器。
		if(!foundSupported){
			throw new UnsupportedCredentialsException();
		}
		//若未认证通过，则抛出最后一个异常信息。
		if(!authenticated){
			throw unAuthSupportedHandlerException;
		}
		
		Principal principal = null;
		//初始化是否找到合适的凭据转换器。
		foundSupported = false;
		//循环调用所有的用户凭据解析器。
		if(credentialToPrincipalResolvers!=null && credentialToPrincipalResolvers.size()>0){
			for(CredentialToPrincipalResolver resolver:credentialToPrincipalResolvers){
				//用户凭据解析器是否支持该凭据。
				if(resolver.supports(credential)){
					foundSupported = true;
					principal = resolver.resolvePrincipal(credential);
					//若解析成功，则跳出循环。
					if(principal!=null){
						break;
					}
				}
			}
		}
		//未找到支持的用户凭据解析器。
		if(!foundSupported){
			logger.error("not found any supported credentials resolvers");
			throw new UnsupportedCredentialsException();
		}
		//若认证后处理器对象为空，则抛出异常。
		if(authenticationPostHandler==null){
			logger.error("No Authentication Post HandlerException");
			throw new NoAuthenticationPostHandlerException();
		}
		
		//交由认证后处理器进行处理。
		return authenticationPostHandler.postAuthentication(credential, principal);
	}

}