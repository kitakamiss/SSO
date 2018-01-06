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
public interface Authentication {

	public Map<String, Object> getAttributes();

	public Date getAuthenticatedDate();

	public Principal getPrincipal();

}