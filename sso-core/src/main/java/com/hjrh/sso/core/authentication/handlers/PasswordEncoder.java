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
package com.hjrh.sso.core.authentication.handlers;

/**
 * 
 * 功能描述：
 *
 * @author  吴俊明
 *
 * <p>修改历史：(修改人，修改时间，修改原因/内容)</p>
 */
public interface PasswordEncoder {

    /**
     * 
     * 功能描述：
     *
     * @author  吴俊明
     * <p>创建日期 ：2017年11月29日 下午7:58:16</p>
     *
     * @param password
     * @return
     *
     * <p>修改历史 ：(修改人，修改时间，修改原因/内容)</p>
     */
    String encode(String password);
}
