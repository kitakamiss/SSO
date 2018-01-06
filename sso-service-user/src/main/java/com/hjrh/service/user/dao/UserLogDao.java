/**
 * UserDao.java	  V1.0   2017年10月17日 下午6:05:16
 *
 * Copyright 2015 Hjrh Technology Co.,Ltd. All rights reserved.
 *
 * Modification history(By    Time    Reason):
 * 
 * Description:
 */

package com.hjrh.service.user.dao;

import org.springframework.stereotype.Repository;

import com.hjrh.common.core.biz.GenericDAO;
import com.hjrh.facade.user.entity.UserLog;
/**
 * 
 * 功能描述：用户
 *
 * @author  吴俊明 
 *
 * <p>修改历史：(修改人，修改时间，修改原因/内容)</p>
 */
@Repository("userLogDao")
public class UserLogDao extends GenericDAO<UserLog> {

}
