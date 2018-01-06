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

import javax.annotation.Resource;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.hjrh.common.core.dao.IGenericDAO;
import com.hjrh.common.core.service.BaseServiceImpl;
import com.hjrh.facade.user.entity.UserLog;
import com.hjrh.facade.user.service.UserLogService;
/**
 * 
 * 功能描述：用户日志接口
 *
 * @author  吴俊明 
 *
 * <p>修改历史：(修改人，修改时间，修改原因/内容)</p>
 */
@Service("userLogService")
@Transactional
public class UserLogServiceImpl extends BaseServiceImpl<UserLog> implements UserLogService {
	
	@Resource(name = "userLogDao")
	private IGenericDAO<UserLog> dao;
	
	@Override
	protected IGenericDAO<UserLog> getDao() {
		return dao;
	}
	
}
