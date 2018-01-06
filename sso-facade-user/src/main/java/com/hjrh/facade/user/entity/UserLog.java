package com.hjrh.facade.user.entity;

import javax.persistence.Entity;

import com.hjrh.common.entity.IdEntity;

/**
 * 
 * 功能描述：用户登录日志
 *
 * @author  吴俊明 
 *
 * <p>修改历史：(修改人，修改时间，修改原因/内容)</p>
 */
@Entity(name = "sso_user_log")
public class UserLog extends IdEntity{
	/**
	 * 用户名
	 */
	private String username;
	/**
	 * 日志内容
	 */
	private String content;
	
	public String getUsername() {
		return username;
	}
	public void setUsername(String username) {
		this.username = username;
	}

	public String getContent() {
		return content;
	}
	public void setContent(String content) {
		this.content = content;
	}
}
