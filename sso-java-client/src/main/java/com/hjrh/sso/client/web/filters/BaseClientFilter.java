package com.hjrh.sso.client.web.filters;

import javax.servlet.Filter;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;

import com.hjrh.sso.common.utils.StringUtils;

/**
 * 
 * 功能描述：公共基础的客户端过滤器类，定义了一些公共的方法
 *
 * @author  吴俊明
 *
 * <p>修改历史：(修改人，修改时间，修改原因/内容)</p>
 */
public abstract class BaseClientFilter implements Filter{
	
	/**
	 * sso服务器主机地址。
	 */
	protected String ssoServerHost;
	
	@Override
	public void init(FilterConfig filterConfig) throws ServletException {
		//初始化参数。
		ssoServerHost = getInitParameterWithDefalutValue(filterConfig, "ssoServerHost", ssoServerHost);
		//调用子类的初始化方法。
		doInit(filterConfig);
	}
	
	/**
	 * 
	 * 功能描述：由子类实现的初始化方法
	 *
	 * @author  吴俊明
	 * <p>创建日期 ：2017年11月30日 上午10:02:26</p>
	 *
	 * @param filterConfig
	 * @throws ServletException
	 *
	 * <p>修改历史 ：(修改人，修改时间，修改原因/内容)</p>
	 */
	protected abstract void doInit(FilterConfig filterConfig) throws ServletException;

	/**
	 * 
	 * 功能描述：获取过滤器参数值，带有默认值，若没有配置，则使用默认值
	 *
	 * @author  吴俊明
	 * <p>创建日期 ：2017年11月30日 上午10:01:58</p>
	 *
	 * @param filterConfig
	 * @param paramName
	 * @param defalutValue
	 * @return
	 *
	 * <p>修改历史 ：(修改人，修改时间，修改原因/内容)</p>
	 */
	protected String getInitParameterWithDefalutValue(FilterConfig filterConfig, String paramName, String defalutValue){
		String value = filterConfig.getInitParameter(paramName);
		if(StringUtils.isEmpty(value)){
			value = defalutValue;
		}
		return value;
	}
	
	
	/**
	 * 
	 * 功能描述：
	 *
	 * @author  吴俊明
	 * <p>创建日期 ：2017年11月30日 上午10:02:18</p>
	 *
	 * @param request
	 * @param name
	 * @return
	 *
	 * <p>修改历史 ：(修改人，修改时间，修改原因/内容)</p>
	 */
	public static Cookie getCookie(HttpServletRequest request, String name) {
		Cookie cookies[] = request.getCookies();
		if (cookies != null) {
			for (Cookie cookie : cookies) {
				if (name.equals(cookie.getName())) {
					return cookie;
				}
			}
		}
		return null;
	}

	public String getSsoServerHost() {
		return ssoServerHost;
	}

	public void setSsoServerHost(String ssoServerHost) {
		this.ssoServerHost = ssoServerHost;
	}
	
	
}
