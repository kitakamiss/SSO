package com.hjrh.sso.core.authentication;

import java.util.Map;

/**
 * 
 * 功能描述：参数，定义了获得动态参数列表的接口
 *
 * @author  吴俊明
 *
 * <p>修改历史：(修改人，修改时间，修改原因/内容)</p>
 */
public interface Parameter {
	
	/**
	 * 通过参数名获得参数值的方法。
	 * @param paramName 参数名。
	 * @return 对应的参数值。
	 */
	public Object getParameterValue(String paramName);
	
	
	/**
	 * 获得所有的参数表。
	 * @return 所有的参数列表。
	 */
	public Map<String, Object> getParameters();
	
	/**
	 * 设置参数列表。
	 * @param parameters 要设置的参数表。
	 */
	public void setParameters(Map<String, Object> parameters);

}
