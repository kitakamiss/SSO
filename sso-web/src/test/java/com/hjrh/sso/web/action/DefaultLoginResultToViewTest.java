package com.hjrh.sso.web.action;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.Cookie;

import org.junit.Assert;
import org.junit.Test;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.view.RedirectView;

import com.hjrh.sso.core.authentication.AuthenticationImpl;
import com.hjrh.sso.core.authentication.AuthenticationPostHandler;
import com.hjrh.sso.core.exception.InvalidCredentialException;
import com.hjrh.sso.core.message.MessageUtils;
import com.hjrh.sso.core.service.LoginResult;
import com.hjrh.sso.web.action.DefaultLoginResultToView;
import com.hjrh.sso.web.utils.WebConstants;

/**
 * 对类DefaultLoginResultToView的单元测试类。
 * @author burgess yang
 *
 */
public class DefaultLoginResultToViewTest {

	private DefaultLoginResultToView loginResultToView = new DefaultLoginResultToView();
	
	/**
	 * 测试被测类对应的方法。
	 */
	@Test
	public void testLoginResultToView(){
		
		/**
		 * 测试输入null的情况。
		 */
		Assert.assertNotNull(loginResultToView.loginResultToView(null, null, null, null));
		/**
		 * 测试输入null的情况。
		 */
		Assert.assertNotNull(loginResultToView.loginResultToView(new ModelAndView(), null, null, null));
		
		/**
		 * 测试输入null的情况。
		 */
		Assert.assertNotNull(loginResultToView.loginResultToView(new ModelAndView(), new LoginResult(), null, null));
		
		/**
		 * 测试输入null的情况。
		 */
		Assert.assertNotNull(loginResultToView.loginResultToView(new ModelAndView(), new LoginResult(), new MockHttpServletRequest(), null));
		
		
		/**
		 * 测试登录失败的情况。
		 */
		//准备测试输入数据。
		ModelAndView mv = new ModelAndView();
		MockHttpServletRequest request = new MockHttpServletRequest();
		MockHttpServletResponse response = new MockHttpServletResponse();
		LoginResult result = new LoginResult();
		result.setSuccess(false);
		result.setCode(InvalidCredentialException.CODE);
		result.setMsgKey(InvalidCredentialException.MSG_KEY);
		request.setCookies(new Cookie(WebConstants.sso_SERVER_ENCRYPTED_CREDENTIAL_COOKIE_KEY, "test"));
		ModelAndView mvResult = loginResultToView.loginResultToView(mv, result, request, response);
		//检查输出结果。
		Assert.assertNotNull(mvResult);
		Assert.assertEquals(InvalidCredentialException.CODE, mvResult.getModel().get("code"));
		Assert.assertEquals(MessageUtils.getMessage(InvalidCredentialException.MSG_KEY), mvResult.getModel().get("msg"));
		Assert.assertEquals("test", response.getCookie(WebConstants.sso_SERVER_ENCRYPTED_CREDENTIAL_COOKIE_KEY).getValue());
		
		
		/**
		 * 测试登录成功的情况，不存在service参数的情况。
		 */
		//准备测试输入数据。
		mv = new ModelAndView();
		request = new MockHttpServletRequest();
		response = new MockHttpServletResponse();
		result = new LoginResult();
		request.getSession().setAttribute(WebConstants.sso_SERVICE_KEY_IN_SESSION, "service");
		result.setSuccess(true);
		AuthenticationImpl authentication = new AuthenticationImpl();
		Map<String, Object> attributes = new HashMap<String, Object>();
		authentication.setAttributes(attributes);
		attributes.put(AuthenticationPostHandler.sso_SERVER_EC_KEY, "sso-sever-key");
		result.setAuthentication(authentication);
		mvResult = loginResultToView.loginResultToView(mv, result, request, response);
		//检查输出结果。
		Assert.assertNotNull(mvResult);
		Assert.assertNull(request.getSession().getAttribute(WebConstants.sso_SERVICE_KEY_IN_SESSION));
		Assert.assertEquals("loginSucess", mvResult.getViewName());
		Assert.assertEquals(authentication, mvResult.getModel().get("authentication"));
		Assert.assertEquals("sso-sever-key", response.getCookie(WebConstants.sso_SERVER_ENCRYPTED_CREDENTIAL_COOKIE_KEY).getValue());
		
		

		/**
		 * 测试登录成功的情况，结果中存在service参数，则应该跳转到该地址去。
		 */
		mv = new ModelAndView();
		request = new MockHttpServletRequest();
		response = new MockHttpServletResponse();
		result = new LoginResult();
		result.setSuccess(true);
		authentication = new AuthenticationImpl();
		attributes = new HashMap<String, Object>();
		authentication.setAttributes(attributes);
		attributes.put(AuthenticationPostHandler.sso_SERVER_EC_KEY, "sso-sever-key");
		attributes.put(AuthenticationPostHandler.sso_CLIENT_EC_KEY, "sso-client-key");
		attributes.put(WebConstants.SERVICE_PARAM_NAME, "htpp://localhost:8080/space-web/hello.jsp");
		result.setAuthentication(authentication);
		mvResult = loginResultToView.loginResultToView(mv, result, request, response);
		//检查输出结果。
		Assert.assertNotNull(mvResult);
		Assert.assertNull(request.getSession().getAttribute(WebConstants.sso_SERVICE_KEY_IN_SESSION));
		Assert.assertTrue(mvResult.getView() instanceof RedirectView);
		RedirectView view = (RedirectView)mvResult.getView();
		Assert.assertEquals("htpp://localhost:8080/space-web/hello.jsp?"+WebConstants.sso_CLIENT_ENCRYPTED_CREDENTIAL_COOKIE_KEY+"=sso-client-key",view.getUrl());
		Assert.assertEquals(authentication, mvResult.getModel().get("authentication"));
		Assert.assertEquals("sso-sever-key", response.getCookie(WebConstants.sso_SERVER_ENCRYPTED_CREDENTIAL_COOKIE_KEY).getValue());
		
		
		/**
		 * 测试登录成功的情况，结果中存在service参数，且URL地址中是带"?"的。
		 */
		mv = new ModelAndView();
		request = new MockHttpServletRequest();
		response = new MockHttpServletResponse();
		result = new LoginResult();
		result.setSuccess(true);
		authentication = new AuthenticationImpl();
		attributes = new HashMap<String, Object>();
		authentication.setAttributes(attributes);
		attributes.put(AuthenticationPostHandler.sso_SERVER_EC_KEY, "sso-sever-key");
		attributes.put(AuthenticationPostHandler.sso_CLIENT_EC_KEY, "sso-client-key");
		attributes.put(WebConstants.SERVICE_PARAM_NAME, "htpp://localhost:8080/space-web/hello.jsp?hello=word");
		result.setAuthentication(authentication);
		mvResult = loginResultToView.loginResultToView(mv, result, request, response);
		//检查输出结果。
		Assert.assertNotNull(mvResult);
		Assert.assertNull(request.getSession().getAttribute(WebConstants.sso_SERVICE_KEY_IN_SESSION));
		Assert.assertTrue(mvResult.getView() instanceof RedirectView);
		view = (RedirectView)mvResult.getView();
		Assert.assertEquals("htpp://localhost:8080/space-web/hello.jsp?hello=word&"+WebConstants.sso_CLIENT_ENCRYPTED_CREDENTIAL_COOKIE_KEY+"=sso-client-key",view.getUrl());
		Assert.assertEquals(authentication, mvResult.getModel().get("authentication"));
		Assert.assertEquals("sso-sever-key", response.getCookie(WebConstants.sso_SERVER_ENCRYPTED_CREDENTIAL_COOKIE_KEY).getValue());
	}
	
	
}
