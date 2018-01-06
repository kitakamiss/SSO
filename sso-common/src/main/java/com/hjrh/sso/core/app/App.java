package com.hjrh.sso.core.app;

import java.io.Serializable;
/**
 * 
 * 功能描述：应用描述信息
 *
 * @author  吴俊明
 *
 * <p>修改历史：(修改人，修改时间，修改原因/内容)</p>
 */
public class App implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -8909901916281726150L;
	
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

	public boolean isSsoServer() {
		return ssoServer;
	}

	public void setSsoServer(boolean ssoServer) {
		this.ssoServer = ssoServer;
	}

	@Override
	public String toString() {
		return "App [appId=" + appId + ", appName=" + appName + ", host="
				+ host + ", logoutUrl=" + logoutUrl + ", ssoServer="
				+ ssoServer + "]";
	}
	
}
