/**
 * FileDataServiceImpl.java	  V1.0   2017年10月17日 下午6:19:54
 *
 * Copyright 2015 Hjrh Technology Co.,Ltd. All rights reserved.
 *
 * Modification history(By    Time    Reason):
 * 
 * Description:
 */

package com.hjrh.facade.user.service.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.hjrh.common.core.dao.IGenericDAO;
import com.hjrh.common.core.service.BaseServiceImpl;
import com.hjrh.common.utils.CommUtil;
import com.hjrh.common.utils.string.StringUtil;
import com.hjrh.facade.user.entity.UserAttempts;
import com.hjrh.facade.user.service.UserAttemptsService;
/**
 * 
 * 功能描述：用户登录失败次数接口
 *
 * @author  吴俊明 
 *
 * <p>修改历史：(修改人，修改时间，修改原因/内容)</p>
 */
@Service("userAttemptsService")
@Transactional
public class UserAttemptsServiceImpl extends BaseServiceImpl<UserAttempts> implements UserAttemptsService {
	
	@Resource(name = "userAttemptsDao")
	private IGenericDAO<UserAttempts> dao;
	
	@Override
	protected IGenericDAO<UserAttempts> getDao() {
		return dao;
	}
	
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
	public Boolean checkUserAttempts(String userName,String interval,Integer maxAttempts){
		StringBuffer sql = new StringBuffer();
		List<Object> objs = new ArrayList<Object>();
		Date now = new Date();
    	if(StringUtil.isNullOrEmpty(interval)){
    		interval = "-60";
    	}
    	Date begin = com.hjrh.common.utils.DateUtil.addMinutes(now, interval);
    	Date end = com.hjrh.common.utils.DateUtil.addMinutes(now, "0");
    	
		sql.append("select id,userName,attempts from sso_user_attempts where userName = ? and lastUpdateTime BETWEEN  ? AND  ?");
		objs.add(userName);
		objs.add(begin);
		objs.add(end);
		List<Map> list = this.dao.queryForList(sql.toString(), objs.toArray());
		if(list!=null && list.size()>0){
			Map map = list.get(0);
			int attempts =  CommUtil.null2Int(map.get("attempts"));
			if(attempts>=(maxAttempts == null ? UserAttempts.MAX_ATTEMPTS : maxAttempts) ){
				return false;
			}else{
				return true;
			}
		}
		return true;
	}
	
	/**
	 * 
	 * 功能描述：查询指定间隔段用户登陆错误
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
	public List<Map> getUserAttemptsByUserId(String userName,String interval,Integer maxAttempts){
		StringBuffer sql = new StringBuffer();
		List<Object> objs = new ArrayList<Object>();
		Date now = new Date();
    	if(StringUtil.isNullOrEmpty(interval)){
    		interval = "-60";
    	}
    	Date begin = com.hjrh.common.utils.DateUtil.addMinutes(now, interval);
    	Date end = com.hjrh.common.utils.DateUtil.addMinutes(now, "0");
    	
		sql.append("select id,userName,attempts from sso_user_attempts where userName = ? and lastUpdateTime BETWEEN  ? AND  ?");
		objs.add(userName);
		objs.add(begin);
		objs.add(end);
		List<Map> list = this.dao.queryForList(sql.toString(), objs.toArray());
		
		return list;
	}
	
	
	
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
	public void saveOrUpdateUserAttempts(String userName,String interval,Integer maxAttempts){
		List<Map> list = this.getUserAttemptsByUserId(userName, interval, maxAttempts);
		if(list!=null && list.size()>0){
			Map mp = list.get(0);
			UserAttempts userAttempts = this.getObjById(CommUtil.null2Long(mp.get("id")));
			userAttempts.setAttempts(userAttempts.getAttempts()+1);
			userAttempts.setLastUpdateTime(new Date());
			this.dao.update(userAttempts);
		}else{
			UserAttempts userAttempts = new UserAttempts();
			userAttempts.setUserName(userName);
			userAttempts.setAttempts(1);
			userAttempts.setLastUpdateTime(new Date());
			this.dao.save(userAttempts);
		}
	}
}
