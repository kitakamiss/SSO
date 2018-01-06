package com.hjrh.facade.user.entity;

import java.util.Date;

import javax.persistence.Entity;

import com.hjrh.common.entity.IdEntity;

/**
 * 
 * 功能描述：用户登录失败次数
 *
 * @author 吴俊明
 *
 *         <p>
 * 		修改历史：(修改人，修改时间，修改原因/内容)
 *         </p>
 */
@Entity(name = "sso_user_attempts")
public class UserAttempts extends IdEntity {

	/**
	 * serialVersionUID
	 */
	private static final long serialVersionUID = 1L;
	/**
	 * 用户名
	 */
	private String userName;
	/**
	 * 失败次数
	 */
	private int attempts;
	/**
	 * 最后修改时间
	 */
	private Date lastUpdateTime;

	// 最大失败尝试次数
	public static final int MAX_ATTEMPTS = 5;

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public int getAttempts() {
		return attempts;
	}

	public void setAttempts(int attempts) {
		this.attempts = attempts;
	}

	public Date getLastUpdateTime() {
		return lastUpdateTime;
	}

	public void setLastUpdateTime(Date lastUpdateTime) {
		this.lastUpdateTime = lastUpdateTime;
	}
}
