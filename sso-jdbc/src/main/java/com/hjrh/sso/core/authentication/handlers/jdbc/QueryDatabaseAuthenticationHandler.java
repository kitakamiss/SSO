
package com.hjrh.sso.core.authentication.handlers.jdbc;

import org.springframework.dao.IncorrectResultSizeDataAccessException;

import com.hjrh.sso.core.authentication.UsernamePasswordCredential;
import com.hjrh.sso.core.exception.AuthenticationException;


/**
 * 
 * 功能描述：
 *
 * @author  吴俊明
 *
 * <p>修改历史：(修改人，修改时间，修改原因/内容)</p>
 */
public class QueryDatabaseAuthenticationHandler extends AbstractJdbcUsernamePasswordAuthenticationHandler {

    private String sql;

    protected final boolean authenticateUsernamePasswordInternal(final UsernamePasswordCredential credentials) throws AuthenticationException {
        final String username = credentials.getUsername();
        final String password = credentials.getPassword();
        final String encryptedPassword = this.getPasswordEncoder().encode(
            password);
        
        try {
            final String dbPassword = getJdbcTemplate().queryForObject(this.sql, String.class, username);
            return dbPassword.equals(encryptedPassword);
        } catch (final IncorrectResultSizeDataAccessException e) {
            return false;
        }
    }

    /**
     * @param sql The sql to set.
     */
    public void setSql(final String sql) {
        this.sql = sql;
    }
}