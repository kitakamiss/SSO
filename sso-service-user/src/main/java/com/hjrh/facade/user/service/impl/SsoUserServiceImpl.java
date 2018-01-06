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
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.hjrh.common.core.dao.IGenericDAO;
import com.hjrh.common.core.service.BaseServiceImpl;
import com.hjrh.common.utils.string.StringUtil;
import com.hjrh.facade.user.entity.SsoUser;
import com.hjrh.facade.user.service.SsoUserService;
/**
 * 
 * 功能描述：用户接口
 *
 * @author  吴俊明 
 *
 * <p>修改历史：(修改人，修改时间，修改原因/内容)</p>
 */
@Service("ssoUserService")
@Transactional
public class SsoUserServiceImpl extends BaseServiceImpl<SsoUser> implements SsoUserService {
	
	@Resource(name = "userDao")
	private IGenericDAO<SsoUser> dao;
	
	@Override
	protected IGenericDAO<SsoUser> getDao() {
		return dao;
	}
	
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
	public Boolean checkUserNameOrPassword(String userName,String password,String appUserId){
		StringBuffer sql = new StringBuffer();
		sql.append("select id from sso_user where 1=1 ");
		List<Object> objs = new ArrayList<Object>();
		if(StringUtil.isNotEmpty(userName) && StringUtil.isNotEmpty(password)){
			sql.append(" and (username = ? or mobile = ? or email=? ) and password = ? ");
			objs.add(userName);
			objs.add(userName);
			objs.add(userName);
			objs.add(password);
		}else{
			return false;
		}
		if(StringUtil.isNotEmpty(appUserId)){
			sql.append(" and appUserId = ? ");
			objs.add(appUserId);
		}
		List<Map> list = this.dao.queryForList(sql.toString(), objs.toArray());
		if(list!=null && list.size()>0){
			return true;
		}
		return false;
	}
	
}
