package com.hjrh.sso.core.authentication;

import java.util.Date;
import java.util.Map;
/**
 * 
 * 功能描述：认证结果
 *
 * @author  吴俊明
 *
 * <p>修改历史：(修改人，修改时间，修改原因/内容)</p>
 */
public class AuthenticationImpl implements Authentication {
	
	private Date authenticatedDate;
	
	private Map<String, Object> attributes;

	private Principal principal;
	
	@Override
	public Map<String, Object> getAttributes() {
		return attributes;
	}

	@Override
	public Date getAuthenticatedDate() {
		return authenticatedDate;
	}

	@Override
	public Principal getPrincipal() {
		return principal;
	}

	public void setAuthenticatedDate(Date authenticatedDate) {
		this.authenticatedDate = authenticatedDate;
	}

	public void setAttributes(Map<String, Object> attributes) {
		this.attributes = attributes;
	}

	public void setPrincipal(Principal principal) {
		this.principal = principal;
	}
	

}
