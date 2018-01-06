package com.hjrh.facade.user.entity;

import javax.persistence.Entity;

import com.hjrh.common.entity.IdEntity;
/**
 * 应用描述信息。
 * @author Administrator
 *
 */
@Entity(name = "sso_app")
public class App extends IdEntity {

	/**
	 * 应用ID
	 */
	private String appId;
	
	/**
	 * 应用名称。
	 */
	private String appName;
	
	/**
	 * 应用主机地址。
	 */
	private String host;
	
	/**
	 * 应用登出地址。
	 */
	private String logoutUrl;
	
	/**
	 * 是否是sso服务器应用本身。
	 */
	private boolean ssoServer = false;

	public String getAppId() {
		return appId;
	}

	public void setAppId(String appId) {
		this.appId = appId;
	}

	public String getAppName() {
		return appName;
	}

	public void setAppName(String appName) {
		this.appName = appName;
	}

	public String getHost() {
		return host;
	}

	public void setHost(String host) {
		this.host = host;
	}

	public String getLogoutUrl() {
		return logoutUrl;
	}

	public void setLogoutUrl(String logoutUrl) {
		this.logoutUrl = logoutUrl;
	}
	
	public boolean isssoServer() {
		return ssoServer;
	}

	public void setssoServer(boolean ssoServer) {
		this.ssoServer = ssoServer;
	}

	@Override
	public String toString() {
		return "App [appId=" + appId + ", appName=" + appName + ", host="
				+ host + ", logoutUrl=" + logoutUrl + ", ssoServer="
				+ ssoServer + "]";
	}
	
}
