package com.hjrh.sso.web.action;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import com.hjrh.common.utils.CommUtil;
import com.hjrh.common.utils.cache.redis.RedisUtils;
import com.hjrh.common.utils.string.StringUtil;
import com.hjrh.sso.core.authentication.Credential;
import com.hjrh.sso.core.service.LoginResult;
import com.hjrh.sso.core.service.SsoService;
import com.hjrh.sso.web.util.RSAUtils;
import com.hjrh.sso.web.utils.WebConstants;

/**
 * 登入web控制器类，处理登录的请求。
 * @author burgess yang
 *
 */
@Controller
public class LoginAction {
	
	private static final Logger LOGGER = Logger.getLogger(LoginAction.class);
	
	@Autowired
	protected CredentialResolver credentialResolver;
	
	@Autowired
	protected SsoService ssoService;
	
	@Autowired
	protected LoginResultToView loginResultToView;

	public void setLoginResultToView(LoginResultToView loginResultToView) {
		this.loginResultToView = loginResultToView;
	}

	public void setssoService(SsoService ssoService) {
		this.ssoService = ssoService;
	}

	/**
	 * 设置用户凭据解析器。
	 * @param credentialResolver
	 */
	public void setCredentialResolver(CredentialResolver credentialResolver) {
		this.credentialResolver = credentialResolver;
	}

	/**
	 * 登录接口，该接口处理所有与登录有关的请求。 包括以下几种情况： 1.
	 * 
	 * @param model
	 * @return
	 */
	@RequestMapping("/login*")
	public ModelAndView login(HttpServletRequest request,
			HttpServletResponse response) {
		
		ModelAndView mv = new ModelAndView("login"); 
		String serviceUrl = request.getParameter(WebConstants.SERVICE_PARAM_NAME);
		if(StringUtil.isNotEmpty(serviceUrl)){
			mv.addObject(WebConstants.SERVICE_PARAM_NAME, serviceUrl);
		}
		
		String modulus = CommUtil.null2String(RedisUtils.get("modulus"));
		Map<String, Object> map = new HashMap<String, Object>();
		if(StringUtil.isNullOrEmpty(modulus)){
			map = RSAUtils.getRsa();
			mv.addObject("modulus", map.get("modulus"));
			mv.addObject("exponent", map.get("exponent"));
		}else{
			mv.addObject("modulus", RedisUtils.get("modulus"));
			mv.addObject("exponent", RedisUtils.get("exponent"));
		}
		
		//解析用户凭据。
		Credential credential = credentialResolver.resolveCredential(request);
		//没有提供任何认证凭据。
		if(credential==null){
			//设置serivce地址到session中。
			String service = request.getParameter(WebConstants.SERVICE_PARAM_NAME);
			if(!StringUtils.isEmpty(service)){
				request.getSession().setAttribute(WebConstants.sso_SERVICE_KEY_IN_SESSION, service);
			}
			return mv;
		}
		//提供了用户凭据
		else{
			//调用核心结果进行凭据认证。
			LoginResult result = ssoService.login(credential);
			//将验证结果转换为视图输出结果。
			mv = loginResultToView.loginResultToView(mv, result, request, response);
		}
		return mv;
	}
}
