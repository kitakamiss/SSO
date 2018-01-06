package com.hjrh.sso.client.web.filters;

import java.io.IOException;
import java.util.logging.Logger;

import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.hjrh.sso.client.handler.AppClientLoginHandler;
import com.hjrh.sso.client.key.DefaultKeyServiceImpl;
import com.hjrh.sso.client.session.SessionStorage;
import com.hjrh.sso.common.utils.StringUtils;
import com.hjrh.sso.core.authentication.EncryCredential;
import com.hjrh.sso.core.authentication.EncryCredentialManagerImpl;
import com.hjrh.sso.core.key.KeyService;
import com.hjrh.sso.core.model.EncryCredentialInfo;
import com.hjrh.sso.web.utils.WebConstants;

/**
 * 
 * 功能描述：sso客户端应用的过滤器，从而实现集成sso单点登录系统
 *
 * @author  吴俊明
 *
 * <p>修改历史：(修改人，修改时间，修改原因/内容)</p>
 */
public class SsoClientFilter extends BaseClientFilter {
	
	private static Logger logger = Logger.getLogger(SsoClientFilter.class.getName());
	
	/**
	 * 在客户端的session中的用户信息，避免频繁认证，提高性能。
	 */
	public static final String USER_STATE_IN_SESSION_KEY = "client_user_info_session_key";
	
	/**
	 * 将sso服务器登出地址设置到request的属性中。
	 */
	public static final String sso_SERVER_LOGOUT_URL = "server_logout_url";
	
	
	/**
	 * sso服务器登录URL地址。
	 */
	protected String ssoServerLoginUrl = ssoServerHost+"login.do";
	
	/**
	 * sso服务器获取应用秘钥信息的URL地址。
	 */
	protected String ssoServerFetchKeyUrl = ssoServerHost+"fetchKey.do";
	
	/**
	 * sso服务器登出URL地址。
	 */
	protected String ssoServerLogoutUrl = ssoServerHost+"logout.do";
	
	/**
	 * 本应用在sso服务器上的应用ID值。
	 */
	protected String ssoClientAppId;
	
	/**
	 * 登录本应用处理器类，由此类进行构造一个对象。
	 */
	protected String appClientLoginHandlerClass;
	/**
	 * 秘钥获取服务。
	 */
	protected KeyService keyService = null;
	
	/**
	 * 凭据管理器。
	 */
	protected EncryCredentialManagerImpl encryCredentialManager;
	
	/**
	 * 登录本应用的处理器。
	 */
	protected AppClientLoginHandler appClientLoginHandler;
	

	@Override
	public void doInit(FilterConfig filterConfig) throws ServletException {
		ssoClientAppId = getInitParameterWithDefalutValue(filterConfig, "ssoClientAppId", ssoClientAppId);
		ssoServerLoginUrl = getInitParameterWithDefalutValue(filterConfig, "ssoServerLoginUrl", ssoServerHost+"login.do");;
		ssoServerLogoutUrl = getInitParameterWithDefalutValue(filterConfig, "ssoServerLogoutUrl", ssoServerHost+"logout.do");;
		ssoServerFetchKeyUrl = getInitParameterWithDefalutValue(filterConfig, "ssoServerFetchKeyUrl", ssoServerHost+"fetchKey.do");
		appClientLoginHandlerClass = getInitParameterWithDefalutValue(filterConfig, "appClientLoginHandlerClass", appClientLoginHandlerClass);
		//构造key服务等相关对象。
		//构造登录本应用的处理器对象。
		if(!StringUtils.isEmpty(appClientLoginHandlerClass)){
			try{
				this.appClientLoginHandler = (AppClientLoginHandler)Class.forName(appClientLoginHandlerClass).newInstance();
			}catch (Exception e) {
			}
		}
		keyService = new DefaultKeyServiceImpl(ssoServerFetchKeyUrl, ssoClientAppId);
		this.encryCredentialManager = new EncryCredentialManagerImpl();
		this.encryCredentialManager.setKeyService(keyService);
		logger.info("the sso sever is :"+this.ssoServerHost+", please check this service is ok.");
	}

	
	@Override
	public void doFilter(ServletRequest request, ServletResponse response,
			FilterChain chain) throws IOException, ServletException {
		HttpServletResponse servletResponse = (HttpServletResponse)response;
		HttpServletRequest servletRequest = (HttpServletRequest)request;
		HttpSession session = servletRequest.getSession();
		try{
			servletRequest.setAttribute(sso_SERVER_LOGOUT_URL, ssoServerLogoutUrl);
			//本地应用未登录。
			if(session.getAttribute(USER_STATE_IN_SESSION_KEY)==null){
				//查找参数中是否存在sso_client_ec值，若没有则重定向到登录页面。
				String sso_client_ec = getClientEC(servletRequest);
				if(StringUtils.isEmpty(sso_client_ec)){
					//跳转到sso登录页面。
					servletResponse.sendRedirect(buildRedirectTossoServer(servletRequest));
					return;
				}
				//解密凭据信息。
				EncryCredentialInfo encryCredentialInfo = this.encryCredentialManager.decrypt(new EncryCredential(sso_client_ec));
				if(encryCredentialInfo!=null){
					//检查凭据合法性。
					boolean valid = this.encryCredentialManager.checkEncryCredentialInfo(encryCredentialInfo);
					//如果合法，则继续其它处理。
					if(valid){
						//设置登录状态到session中。
						session.setAttribute(USER_STATE_IN_SESSION_KEY, encryCredentialInfo);
						//触发登录本应用的处理。
						if(appClientLoginHandler!=null){
							//登录本应用。
							appClientLoginHandler.loginClient(encryCredentialInfo, servletRequest, servletResponse);
						}
						
						//重新定位到原请求，去除EC参数。
						String url = servletRequest.getRequestURL().toString();
						if(!StringUtils.isEmpty(url)){
							//如果请求中存在EC参数，则去除这个参数，重定位。
							if(url.contains(WebConstants.sso_CLIENT_ENCRYPTED_CREDENTIAL_COOKIE_KEY)){
								url = url.substring(0, url.indexOf(WebConstants.sso_CLIENT_ENCRYPTED_CREDENTIAL_COOKIE_KEY));
								//去除末尾的问号。
								if(url.endsWith("?")){
									url = url.substring(0, url.length()-1);
								}
								//去除末尾的&符号。
								if(url.endsWith("&")){
									url = url.substring(0, url.length()-1);
								}
							}
						}
						//保存用户和session的关系
						SessionStorage.put(encryCredentialInfo.getUserId(),session);
						//重新定位请求，避免尾部出现长参数。
						servletResponse.sendRedirect(url);
						return;
					}
				}
				//否则凭据信息不合法，跳转到sso登录页面。
				servletResponse.sendRedirect(buildRedirectTossoServer(servletRequest));
				return;
			}
			//若已经登录过，则直接返回，继续其它过滤器。
			chain.doFilter(request, response);
			return;
		}
		//处理异常信息。
		catch (Exception e) {
			//否则凭据信息不合法，跳转到sso登录页面。
			servletResponse.sendRedirect(buildRedirectTossoServer(servletRequest));
			return;
		}
		
	}
	
	protected String buildRedirectTossoServer(HttpServletRequest servletRequest){
		StringBuffer sb = new StringBuffer(this.ssoServerLoginUrl);
		if(this.ssoServerLoginUrl.contains("?")){
			sb.append("&");
		}
		else{
			sb.append("?");
		}
		sb.append("service=").append(servletRequest.getRequestURL().toString());
		return sb.toString();
	}

	@Override
	public void destroy() {
	}
	
	
	/**
	 * 从客户端参数或者cookie中获取EC值。
	 * @param request http请求对象。
	 * @return EC值。
	 */
	protected String getClientEC(HttpServletRequest request){
		String ec = null;
		if(request!=null){
			ec = request.getParameter(WebConstants.sso_CLIENT_ENCRYPTED_CREDENTIAL_COOKIE_KEY);
		}
		return ec;
	}
	
}
