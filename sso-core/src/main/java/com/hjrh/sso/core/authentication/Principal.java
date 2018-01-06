package com.hjrh.sso.core.authentication;

import java.util.Map;

/**
 * 
 * 功能描述：用户主体，代表一个用户
 *
 * @author  吴俊明
 *
 * <p>修改历史：(修改人，修改时间，修改原因/内容)</p>
 */
public interface Principal {

	public Map<String, Object> getAttributes();

	public String getId();

}