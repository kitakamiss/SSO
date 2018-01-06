package com.hjrh.sso.web.action;

import java.util.Map;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.util.StringUtils;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.view.RedirectView;

import com.hjrh.common.utils.CommUtil;
import com.hjrh.common.utils.string.StringUtil;
import com.hjrh.sso.core.authentication.Authentication;
import com.hjrh.sso.core.authentication.AuthenticationPostHandler;
import com.hjrh.sso.core.message.MessageUtils;
import com.hjrh.sso.core.service.LoginResult;
import com.hjrh.sso.web.utils.WebConstants;

/**
 * 
 * 功能描述：
 *
 * @author  吴俊明
 *
 * <p>修改历史：(修改人，修改时间，修改原因/内容)</p>
 */
public class DefaultLoginResultToView implements LoginResultToView {

	@Override
	public ModelAndView loginResultToView(ModelAndView mv, LoginResult result, HttpServletRequest request,
			HttpServletResponse response) {
		// 若登录成功，则返回成功页面。
		if(mv==null){
			mv = new ModelAndView();
		}
		if(result==null || request==null || response==null){
			return mv;
		}
		if (result.isSuccess()) {
			//登录结果对象。
			Authentication authentication = result.getAuthentication();

			//清除session中的状态信息service值。
			request.getSession().removeAttribute(WebConstants.sso_SERVICE_KEY_IN_SESSION);
			
			// 如果有加密凭据信息，则写入加密凭据值到cookie中。
			if (authentication != null
					&& authentication.getAttributes() != null) {
				Map<String, Object> attributes = authentication.getAttributes();
				// sso服务端加密的凭据存在，则写入cookie中。
				if (attributes
						.get(AuthenticationPostHandler.sso_SERVER_EC_KEY) != null) {
					response.addCookie(new Cookie(
							WebConstants.sso_SERVER_ENCRYPTED_CREDENTIAL_COOKIE_KEY,
							attributes
									.get(AuthenticationPostHandler.sso_SERVER_EC_KEY)
									.toString()));
				}
				// 参数service存在，则跳转到对应的页面中。
				String service = CommUtil.null2String(attributes.get(WebConstants.SERVICE_PARAM_NAME));
				String cli_ec_key = CommUtil.null2String(attributes.get(AuthenticationPostHandler.sso_CLIENT_EC_KEY)
												.toString());
				if(StringUtil.isNullOrEmpty(service)){
					service = request.getParameter(WebConstants.SERVICE_PARAM_NAME);
				}
				if(StringUtil.isNotEmpty(service)){
					mv.setView(this.buildRedirectView(service,cli_ec_key));
					return mv;
				}
				mv.setViewName("loginSucess");
			}
		} else {
			//删除以前不合法的凭据信息。
			//清除cookie值。
			Cookie[] cookies = request.getCookies();
			if(cookies!=null && cookies.length>0){
				for(Cookie cookie:cookies){
					if(WebConstants.sso_SERVER_ENCRYPTED_CREDENTIAL_COOKIE_KEY.equals(cookie.getName())){
						//设置过期时间为立即。
						cookie.setMaxAge(0);
						response.addCookie(cookie);
					}
				}
			}
			String serviceUrl = request.getParameter(WebConstants.SERVICE_PARAM_NAME);
			if(StringUtil.isNotEmpty(serviceUrl)){
				mv.addObject(WebConstants.SERVICE_PARAM_NAME, serviceUrl);
			}
			mv.addObject("code", result.getCode());
			mv.addObject("errormsg",
					MessageUtils.getMessage(result.getMsgKey()));
		}
		return mv;
	}

	/**
	 * 构造跳转的URL地址。
	 * 
	 * @return
	 */
	private RedirectView buildRedirectView(String service,
			String encryCredential) {
		StringBuffer sb = new StringBuffer(service);
		if (service.contains("?")) {
			sb.append("&");
		} else {
			sb.append("?");
		}
		sb.append(WebConstants.sso_CLIENT_ENCRYPTED_CREDENTIAL_COOKIE_KEY)
		.append("=").append(encryCredential);
		return new RedirectView(sb.toString());
	}

}
