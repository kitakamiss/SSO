package com.hjrh.sso.core.service;

public interface LogoutAppService {
	
	/**
	 * 
	 * 功能描述：登出用户ID为userId的用户，登出该用户已经登录过的所有用户
	 *        而service值对应的应用应该优先登出
	 * @author  吴俊明
	 * <p>创建日期 ：2017年12月26日 下午6:25:37</p>
	 *
	 * @param userId    用户ID
	 * @param service   用户优先登出的URL地址
	 *
	 * <p>修改历史 ：(修改人，修改时间，修改原因/内容)</p>
	 */
	public void logoutApp(final String userId, final String service);

}
