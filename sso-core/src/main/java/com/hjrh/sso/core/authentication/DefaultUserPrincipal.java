package com.hjrh.sso.core.authentication;

import java.util.Map;

/**
 * 
 * 功能描述：默认的用户主体对象
 *
 * @author  吴俊明
 *
 * <p>修改历史：(修改人，修改时间，修改原因/内容)</p>
 */
public class DefaultUserPrincipal extends AbstractPrincipal {

	public DefaultUserPrincipal() {
		super();
	}

	public DefaultUserPrincipal(String id, Map<String, Object> attributes) {
		super(id, attributes);
	}

	
}
