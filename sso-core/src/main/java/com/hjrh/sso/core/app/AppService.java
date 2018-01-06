package com.hjrh.sso.core.app;

import com.hjrh.sso.core.app.App;

public interface AppService {
	
	/**
	 * 根据应用ID查询对应的应用信息。
	 * @param appId
	 * @return
	 */
	public App findAppById(String appId);
	
	/**
	 * 查找系统中sso服务对应的应用信息。
	 * @return
	 */
	public App findssoServerApp();
	
	/**
	 * 根据Host的主机查找对应的应用信息。
	 * @param host 应用的服务地址，包括主机信息。
	 * @return 对应的唯一的应用信息。
	 */
	public App findAppByHost(String host);

}
