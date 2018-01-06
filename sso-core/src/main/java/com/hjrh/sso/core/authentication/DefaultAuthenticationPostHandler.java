package com.hjrh.sso.core.authentication;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.hjrh.sso.core.app.App;
import com.hjrh.sso.core.app.AppService;
import com.hjrh.sso.core.authentication.AbstractParameter;
import com.hjrh.sso.core.authentication.Credential;
import com.hjrh.sso.core.authentication.EncryCredentialManager;
import com.hjrh.sso.core.authentication.status.UserLoggedStatus;
import com.hjrh.sso.core.authentication.status.UserLoggedStatusStore;
import com.hjrh.sso.core.exception.NoSsoKeyException;
import com.hjrh.sso.core.key.KeyService;
import com.hjrh.sso.core.key.SsoKey;
import com.hjrh.sso.core.model.EncryCredentialInfo;
import com.hjrh.sso.web.utils.WebConstants;

/**
 * 
 * 功能描述：默认的认证后处理器实现类，提供抽象方法由具体子类实现
 *
 * @author  吴俊明
 *
 * <p>修改历史：(修改人，修改时间，修改原因/内容)</p>
 */
public class DefaultAuthenticationPostHandler implements
		AuthenticationPostHandler {
	
	private static Logger logger = Logger.getLogger(DefaultAuthenticationPostHandler.class.getName());
	
	/**
	 * 服务端持续过期时间，3个月。
	 */
	private static final long SERVER_DURATION = 3L*30*24*60*60*1000;
	
	/**
	 * 客户端端持续过期时间，60秒钟。
	 */
	private static final long CLIENT_DURATION = 60*1000;
	
	private EncryCredentialManager encryCredentialManager;
	
	private KeyService keyService;
	
	private AppService appService;
	
	private UserLoggedStatusStore userLoggedStatusStore;

	public void setUserLoggedStatusStore(UserLoggedStatusStore userLoggedStatusStore) {
		this.userLoggedStatusStore = userLoggedStatusStore;
	}

	public AppService getAppService() {
		return appService;
	}

	public void setAppService(AppService appService) {
		this.appService = appService;
	}

	public KeyService getKeyService() {
		return keyService;
	}

	public void setKeyService(KeyService keyService) {
		this.keyService = keyService;
	}

	public EncryCredentialManager getEncryCredentialManager() {
		return encryCredentialManager;
	}

	public void setEncryCredentialManager(
			EncryCredentialManager encryCredentialManager) {
		this.encryCredentialManager = encryCredentialManager;
	}

	@Override
	public Authentication postAuthentication(Credential credential, Principal principal){
		Date createTime = new Date();
		//若认证通过，则返回认证结果对象。
		AuthenticationImpl authentication = new AuthenticationImpl();
		authentication.setAuthenticatedDate(createTime);
		authentication.setPrincipal(principal);
		encryCredentialWithssoKey(authentication, credential, principal);
		encryCredentialWithAppKey(authentication, credential, principal);
		return authentication;
	}
	
	/**
	 * 
	 * 功能描述：使用sso服务器本身的key对凭据信息进行加密处理
	 *
	 * @author  吴俊明
	 * <p>创建日期 ：2017年11月29日 下午7:51:14</p>
	 *
	 * @param authentication
	 * @param credential
	 * @param principal
	 *
	 * <p>修改历史 ：(修改人，修改时间，修改原因/内容)</p>
	 */
	private void encryCredentialWithssoKey(AuthenticationImpl authentication, Credential credential, Principal principal){
		//如果是原始凭据，则需要进行加密处理。
		if(credential!=null && credential.isOriginal()){
			//查找sso服务对应的应用信息。
			App ssoApp = appService.findssoServerApp();
			if(ssoApp==null){
				logger.log(Level.SEVERE, "no sso key info.");
				throw NoSsoKeyException.INSTANCE; 
			}
			String encryCredential = encryCredentialManager.encrypt(buildEncryCredentialInfo(ssoApp.getAppId(), authentication, principal, SERVER_DURATION));
			//加密后的凭据信息写入到动态属性中。
			Map<String, Object> attributes = authentication.getAttributes();
			if(attributes==null){
				attributes = new HashMap<String, Object>();
			}
			attributes.put(sso_SERVER_EC_KEY, encryCredential);
			authentication.setAttributes(attributes);
		}
	}
	
	/**
	 * 
	 * 功能描述：使用sso服务器本身的key对凭据信息进行加密处理
	 *
	 * @author  吴俊明
	 * <p>创建日期 ：2017年11月29日 下午7:51:29</p>
	 *
	 * @param authentication
	 * @param credential  密钥持续时间
	 * @param principal
	 *
	 * <p>修改历史 ：(修改人，修改时间，修改原因/内容)</p>
	 */
	private void encryCredentialWithAppKey(AuthenticationImpl authentication, Credential credential, Principal principal){
		//获得登录的应用信息。
		AbstractParameter abstractParameter = null;
		if(credential!=null && credential instanceof AbstractParameter){
			abstractParameter = (AbstractParameter)credential;
		}
		//若登录对应的服务参数service的值不为空，则使用该service对应的应用的key进行加密。
		if(authentication!=null && abstractParameter!=null && abstractParameter.getParameterValue(WebConstants.SERVICE_PARAM_NAME)!=null){
			String service = abstractParameter.getParameterValue(WebConstants.SERVICE_PARAM_NAME).toString().trim().toLowerCase();
			//service不为空，且符合Http协议URL格式，则继续加密。
			if(service.length()>0){
				//查找sso服务对应的应用信息。
				App clientApp = appService.findAppByHost(service);
				if(clientApp!=null){
					String encryCredential = encryCredentialManager.encrypt(buildEncryCredentialInfo(clientApp.getAppId(), authentication, principal, CLIENT_DURATION));
					//加密后的凭据信息写入到动态属性中。
					Map<String, Object> attributes = authentication.getAttributes();
					if(attributes==null){
						attributes = new HashMap<String, Object>();
					}
					attributes.put(sso_CLIENT_EC_KEY, encryCredential);
					attributes.put(WebConstants.SERVICE_PARAM_NAME, service);
					authentication.setAttributes(attributes);
					
					//更新用户登录状态到存储器中。
					UserLoggedStatus status = new UserLoggedStatus(principal.getId(), clientApp.getAppId(), authentication.getAuthenticatedDate());
					userLoggedStatusStore.addUserLoggedStatus(status);
				}
			}
		}
	}
	
	private EncryCredentialInfo buildEncryCredentialInfo(String appId, AuthenticationImpl authentication, Principal principal, long duration){
		EncryCredentialInfo encryCredentialInfo = new EncryCredentialInfo();
                if(authentication==null || principal==null){
                    return encryCredentialInfo;
                }
		SsoKey ssoKey = keyService.findKeyByAppId(appId);
		if(ssoKey==null){
			logger.log(Level.INFO, "no key for app id {0}", appId);
			throw new NoSsoKeyException();
		}
		encryCredentialInfo.setAppId(appId);
		encryCredentialInfo.setCreateTime(authentication.getAuthenticatedDate());
		encryCredentialInfo.setUserId(principal.getId());
		encryCredentialInfo.setKeyId(ssoKey.getKeyId());
		Date expiredTime = new Date((authentication.getAuthenticatedDate().getTime()+ duration)); 
		encryCredentialInfo.setExpiredTime(expiredTime);
		return encryCredentialInfo;
	}

}
