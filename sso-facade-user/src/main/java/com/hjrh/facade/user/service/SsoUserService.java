package com.hjrh.facade.user.service;

import com.hjrh.common.core.service.BaseService;
import com.hjrh.facade.user.entity.SsoUser;

/**
 * 
 * 功能描述：用户服务
 *
 * @author  吴俊明 
 *
 * <p>修改历史：(修改人，修改时间，修改原因/内容)</p>
 */
public interface SsoUserService extends BaseService<SsoUser> {
	/**
	 * 
	 * 功能描述：判断用户是否存在
	 *
	 * @author  吴俊明
	 * <p>创建日期 ：2017年12月25日 下午5:41:14</p>
	 *
	 * @param userName 用户名
	 * @param password 密码
	 * @param appUserId 应用ID
	 * @return
	 *
	 * <p>修改历史 ：(修改人，修改时间，修改原因/内容)</p>
	 */
	public Boolean checkUserNameOrPassword(String userName,String password,String appUserId);

}
