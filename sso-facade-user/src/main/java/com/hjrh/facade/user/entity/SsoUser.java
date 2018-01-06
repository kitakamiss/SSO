package com.hjrh.facade.user.entity;

import java.util.Date;

import javax.persistence.Entity;

import com.hjrh.common.entity.IdEntity;

/**
 * 
 * 功能描述：用户
 *
 * @author 吴俊明
 *
 *         <p>
 * 		修改历史：(修改人，修改时间，修改原因/内容)
 *         </p>
 */
@Entity(name = "sso_user")
public class SsoUser extends IdEntity {
	/**
	 * 用户名
	 */
	private String username;
	/**
	 * 用户密码
	 */
	private String password;
	/**
	 * 手机
	 */
	private String mobile;
	/**
	 * 应用用户ID
	 */
	private String appUserId;
	/**
	 * 用户状态 0：可用，1：禁用
	 */
	private int state;
	/**
	 * 邮箱
	 */
	private String email;
	/**
	 * 昵称
	 */
	private String nickName;
	/**
	 * 最后修改时间
	 */
	private Date updateTime;

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getMobile() {
		return mobile;
	}

	public void setMobile(String mobile) {
		this.mobile = mobile;
	}

	public String getAppUserId() {
		return appUserId;
	}

	public void setAppUserId(String appUserId) {
		this.appUserId = appUserId;
	}

	public int getState() {
		return state;
	}

	public void setState(int state) {
		this.state = state;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getNickName() {
		return nickName;
	}

	public void setNickName(String nickName) {
		this.nickName = nickName;
	}

	public Date getUpdateTime() {
		return updateTime;
	}

	public void setUpdateTime(Date updateTime) {
		this.updateTime = updateTime;
	}

}
