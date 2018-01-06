package com.hjrh.sso.web.handler;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import com.hjrh.facade.user.entity.UserLog;
import com.hjrh.facade.user.enums.UserLogContentConstants;
import com.hjrh.facade.user.service.SsoUserService;
import com.hjrh.facade.user.service.UserAttemptsService;
import com.hjrh.facade.user.service.UserLogService;
import com.hjrh.sso.core.authentication.UsernamePasswordCredential;
import com.hjrh.sso.core.authentication.handlers.AbstractUsernamePasswordAuthenticationHandler;
import com.hjrh.sso.core.exception.AuthenticationException;

@Controller
public class SsoUserAuthenticationHandler extends AbstractUsernamePasswordAuthenticationHandler {

	/**
	 * 用户接口
	 */
	@Autowired
	private SsoUserService ssoUserService;
	/**
	 * 用户日志接口
	 */
	@Autowired
	private UserLogService userLogService;
	/**
	 * 用户登陆失败接口
	 */
	@Autowired
	private UserAttemptsService userAttemptsService;

	/**
	 * 最大错误次数，默认为5次
	 */
	private Integer maxAttempts;
	/**
	 * 错误计算间隔时间，默认60分钟
	 */
	private String interval;

	@Override
	protected boolean authenticateUsernamePasswordInternal(UsernamePasswordCredential credential)
			throws AuthenticationException {
		if (credential == null) {
			return false;
		} else {
			UserLog userlog = new UserLog();
			userlog.setUsername(credential.getUsername());
			/**
			 * 检查是否超过错误次数
			 */
			boolean isExceed = userAttemptsService.checkUserAttempts(credential.getUsername(), this.getInterval(), this.getMaxAttempts());
			if(!isExceed){
				userlog.setContent(UserLogContentConstants.ERROR_ATTEMPTS);
				userLogService.save(userlog);
				return false;
			}
			
			boolean checkUser = ssoUserService.checkUserNameOrPassword(credential.getUsername(), credential.getPassword(), null);
			if(checkUser){
				userlog.setContent(UserLogContentConstants.SUCCESS);
				userLogService.save(userlog);
			}else{
				userlog.setContent(UserLogContentConstants.ERROR_USERNAMEORPASSWORD);
				userLogService.save(userlog);
				userAttemptsService.saveOrUpdateUserAttempts(credential.getUsername(),  this.getInterval(), this.getMaxAttempts());
			}
			return checkUser;
		}
	}

	public Integer getMaxAttempts() {
		return maxAttempts;
	}

	public void setMaxAttempts(Integer maxAttempts) {
		this.maxAttempts = maxAttempts;
	}

	public String getInterval() {
		return interval;
	}

	public void setInterval(String interval) {
		this.interval = interval;
	}

}
