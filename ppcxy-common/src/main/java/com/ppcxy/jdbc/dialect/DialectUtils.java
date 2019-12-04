package com.ppcxy.jdbc.dialect;

import org.apache.commons.lang3.StringUtils;
import org.springside.modules.persistence.Hibernates;

import javax.sql.DataSource;

/**
 * 分页方言工具类.
 *
 * @author weep
 */
public class DialectUtils extends Hibernates {
    
    /**
     * 从DataSoure中取出connection, 根据connection的metadata中的jdbcUrl判断Dialect类型.
     * 仅支持Oracle, H2, MySql, PostgreSql, SQLServer，如需更多数据库类型，请仿照此类自行编写。
     */
    public static String getDatabaseName(String jdbcUrl) {
        // 根据jdbc url判断dialect
        if (StringUtils.contains(jdbcUrl, ":h2:")) {
            return "h2";
        } else if (StringUtils.contains(jdbcUrl, ":mysql:")) {
            return "mysql";
        } else if (StringUtils.contains(jdbcUrl, ":oracle:")) {
            return "oracle";
        } else if (StringUtils.contains(jdbcUrl, ":postgresql:")) {
            return "postgresql";
        } else if (StringUtils.contains(jdbcUrl, ":sqlserver:")) {
            return "sqlserver";
        } else {
            throw new IllegalArgumentException("Unknown Database of " + jdbcUrl);
        }
    }
    
    
    /**
     * 从DataSoure中取出connection, 根据connection的metadata中的jdbcUrl判断Dialect类型.
     * 仅支持Oracle, H2, MySql, PostgreSql, SQLServer，如需更多数据库类型，请仿照此类自行编写。
     */
    public static String getDatabaseName(DataSource dataSource) {
        return getDatabaseName(getJdbcUrlFromDataSource(dataSource));
    }
    
}
