package com.hjrh.sso.core.authentication.handlers;

import org.springframework.beans.factory.annotation.Autowired;

import com.hjrh.sso.core.authentication.Credential;
import com.hjrh.sso.core.authentication.EncryCredential;
import com.hjrh.sso.core.authentication.EncryCredentialManager;
import com.hjrh.sso.core.exception.AuthenticationException;
import com.hjrh.sso.core.exception.InvalidEncryCredentialException;
import com.hjrh.sso.core.model.EncryCredentialInfo;

/**
 * 
 * 功能描述：认证后的凭据认证处理器实现类，需要验证认证后的凭据是否有效，凭据是否过期等等其它合法性验证。
 *
 * @author  吴俊明
 *
 * <p>修改历史：(修改人，修改时间，修改原因/内容)</p>
 */
public class EncryCredentialAuthenticationHandler extends
		AbstractPreAndPostProcessingAuthenticationHandler {
	
	@Autowired
	private EncryCredentialManager encryCredentialManager;
	
	public void setEncryCredentialManager(
			EncryCredentialManager encryCredentialManager) {
		this.encryCredentialManager = encryCredentialManager;
	}

	/** Default class to support if one is not supplied. */
	private static final Class<EncryCredential> DEFAULT_CLASS = EncryCredential.class;

	/**
	 * @return true if the credentials are not null and the credentials class is
	 *         equal to the class defined in classToSupport.
	 */
	public final boolean supports(final Credential credential) {
		return credential != null
				&& (DEFAULT_CLASS.equals(credential.getClass()) || (DEFAULT_CLASS
						.isAssignableFrom(credential.getClass())));
	}

	@Override
	protected boolean doAuthentication(Credential credential)
			throws AuthenticationException {
		//不支持的凭据直接返回false.
		if(!this.supports(credential)){
			return false;
		}
		if(credential!=null && credential instanceof EncryCredential){
			EncryCredential encryCredential = (EncryCredential)credential;
			try{
				//解密凭据信息。
				EncryCredentialInfo encryCredentialInfo = this.encryCredentialManager.decrypt(encryCredential);
				//设置凭据信息的关联性。
				if(encryCredentialInfo!=null){
					encryCredential.setEncryCredentialInfo(encryCredentialInfo);
					//检查加密凭据的合法性。
					return this.encryCredentialManager.checkEncryCredentialInfo(encryCredentialInfo);
				}
			}catch (InvalidEncryCredentialException e) {
				return false;
			}
		}
		return false;
	}

}
