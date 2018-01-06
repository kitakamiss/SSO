/*
 * Licensed to Jasig under one or more contributor license
 * agreements. See the NOTICE file distributed with this work
 * for additional information regarding copyright ownership.
 * Jasig licenses this file to you under the Apache License,
 * Version 2.0 (the "License"); you may not use this file
 * except in compliance with the License.  You may obtain a
 * copy of the License at the following location:
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */
package com.hjrh.sso.core.authentication.handlers.jdbc;

import javax.sql.DataSource;

import org.springframework.jdbc.core.JdbcTemplate;

import com.hjrh.sso.core.authentication.handlers.AbstractUsernamePasswordAuthenticationHandler;

/**
 * 
 * 功能描述：
 *
 * @author  吴俊明
 *
 * <p>修改历史：(修改人，修改时间，修改原因/内容)</p>
 */
public abstract class AbstractJdbcUsernamePasswordAuthenticationHandler extends AbstractUsernamePasswordAuthenticationHandler {

    private JdbcTemplate jdbcTemplate;
    
    private DataSource dataSource;

    /**
     * 
     * 功能描述：
     *
     * @author  吴俊明
     * <p>创建日期 ：2017年12月25日 下午2:55:42</p>
     *
     * @param dataSource
     *
     * <p>修改历史 ：(修改人，修改时间，修改原因/内容)</p>
     */
    public final void setDataSource(final DataSource dataSource) {
        this.jdbcTemplate = new JdbcTemplate(dataSource);
        this.dataSource = dataSource;
    }

    /**
     * 
     * 功能描述：
     *
     * @author  吴俊明
     * <p>创建日期 ：2017年12月25日 下午2:55:49</p>
     *
     * @return
     *
     * <p>修改历史 ：(修改人，修改时间，修改原因/内容)</p>
     */
    protected final JdbcTemplate getJdbcTemplate() {
        return this.jdbcTemplate;
    }
    
    protected final DataSource getDataSource() {
        return this.dataSource;
    }
}
