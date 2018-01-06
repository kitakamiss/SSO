package com.hjrh.sso.client.web.filters;

import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;

import com.hjrh.sso.client.key.DefaultKeyServiceImpl;
import com.hjrh.sso.common.utils.StringUtils;
import com.hjrh.sso.core.key.KeyService;

/**
 * 
 * 功能描述：
 *
 * @author  吴俊明
 *
 * <p>修改历史：(修改人，修改时间，修改原因/内容)</p>
 */
public class SsoGeneratePrivateKeyFileFilter extends BaseClientFilter{

	private static Logger logger = Logger.getLogger(DefaultKeyServiceImpl.class
			.getName());
	private String ssoServerFetchKeyUrl = null;
	//运用标识
	private String appId = null;
	/**
	 * 生成私钥文件类
	 */
	protected String ssoGeneratePrivateKeyFileClass = "com.hjrh.sso.client.key.DefaultKeyServiceImpl";
	
	protected KeyService keyService= null; 
	@Override
	public void destroy() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void doFilter(ServletRequest request, ServletResponse response,
			FilterChain filter) throws IOException, ServletException {
		// TODO Auto-generated method stub
		try {
			keyService.generateKeyFile(appId);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			logger.log(Level.SEVERE, "秘钥生成失败");
		}
		//过滤器继续执行
		filter.doFilter(request, response);
		
	}

	@Override
	protected void doInit(FilterConfig filterConfig) throws ServletException {
		// TODO Auto-generated method stub
		ssoGeneratePrivateKeyFileClass = getInitParameterWithDefalutValue(filterConfig, "appClientDefaultKeyServiceClass", ssoGeneratePrivateKeyFileClass);
		//获取appId参数值
		appId = getInitParameterWithDefalutValue(filterConfig,"ssoClientAppId","1001");
		//获取服务器访问路径参数值
		ssoServerFetchKeyUrl = getInitParameterWithDefalutValue(filterConfig,"ssoServerFetchKeyUrl","http://localhost:8080/sso-web/fetchKey.do");
		//构造登录本应用的处理器对象。
		if(!StringUtils.isEmpty(ssoGeneratePrivateKeyFileClass)){
			try{
				//实例化
				this.keyService = (KeyService) (Class.forName(ssoGeneratePrivateKeyFileClass)
											.getConstructor(String.class,String.class)).newInstance(ssoServerFetchKeyUrl,appId);	//实现类需无参构造方法
			}catch (Exception e) {
				logger.log(Level.SEVERE, e.getMessage());		//记录初始化类日志
			}
		}
	}
}
