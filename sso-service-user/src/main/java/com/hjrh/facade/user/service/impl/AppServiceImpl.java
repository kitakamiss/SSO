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
import com.hjrh.facade.user.entity.App;
import com.hjrh.facade.user.service.AppService;
/**
 * 
 * 功能描述：应用描述接口
 *
 * @author  吴俊明 
 *
 * <p>修改历史：(修改人，修改时间，修改原因/内容)</p>
 */
@Service("appService")
@Transactional
public class AppServiceImpl extends BaseServiceImpl<App> implements AppService {
	
	@Resource(name = "appDao")
	private IGenericDAO<App> dao;
	
	@Override
	protected IGenericDAO<App> getDao() {
		return dao;
	}
	
	
}
