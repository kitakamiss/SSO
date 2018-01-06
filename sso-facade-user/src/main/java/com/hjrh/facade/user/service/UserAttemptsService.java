package com.hjrh.facade.user.service;

import java.util.List;
import java.util.Map;

import com.hjrh.common.core.service.BaseService;
import com.hjrh.facade.user.entity.UserAttempts;

/**
 * 
 * 功能描述：用户登录失败次数服务
 *
 * @author  吴俊明 
 *
 * <p>修改历史：(修改人，修改时间，修改原因/内容)</p>
 */
public interface UserAttemptsService extends BaseService<UserAttempts> {
	/**
	 * 
	 * 功能描述：查询指定间隔段用户登陆错误记录
	 *
	 * @author  吴俊明
	 * <p>创建日期 ：2017年12月26日 下午2:02:06</p>
	 *
	 * @param userName 用户名
	 * @param interval 间隔时间
	 * @return
	 *
	 * <p>修改历史 ：(修改人，修改时间，修改原因/内容)</p>
	 */
	List<Map> getUserAttemptsByUserId(String userName,String interval,Integer maxAttempts);
	
	/**
	 * 
	 * 功能描述：验证用户指定间隔段用户登陆错误次数
	 *
	 * @author  吴俊明
	 * <p>创建日期 ：2017年12月26日 下午2:02:06</p>
	 *
	 * @param userName 用户名
	 * @param interval 间隔时间
	 * @return
	 *
	 * <p>修改历史 ：(修改人，修改时间，修改原因/内容)</p>
	 */
	Boolean checkUserAttempts(String userName,String interval,Integer maxAttempts);
	
	/**
	 * 
	 * 功能描述：保存或修改用户登陆失败记录
	 *
	 * @author  吴俊明
	 * <p>创建日期 ：2017年12月26日 下午4:00:33</p>
	 *
	 * @param userAttempts
	 * @param interval
	 * @param maxAttempts
	 *
	 * <p>修改历史 ：(修改人，修改时间，修改原因/内容)</p>
	 */
	void saveOrUpdateUserAttempts(String userName,String interval,Integer maxAttempts);
}
