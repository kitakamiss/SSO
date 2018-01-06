package com.hjrh.sso.core.authentication;

import java.util.Map;
/**
 * 
 * 功能描述：抽象的参数化实现类
 *
 * @author  吴俊明
 *
 * <p>修改历史：(修改人，修改时间，修改原因/内容)</p>
 */
public abstract class AbstractParameter implements Parameter{
	
	/**
	 * 其它参数表。
	 */
	protected Map<String, Object> paramters;
	
	@Override
	public Object getParameterValue(String paramName) {
		return this.paramters==null?null:this.paramters.get(paramName);
	}

	@Override
	public Map<String, Object> getParameters() {
		return this.paramters;
	}

	@Override
	public void setParameters(Map<String, Object> parameters) {
		this.paramters = parameters;
	}

}
