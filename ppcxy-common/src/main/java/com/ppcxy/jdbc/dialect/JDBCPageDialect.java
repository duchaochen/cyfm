package com.ppcxy.jdbc.dialect;

import com.ppcxy.jdbc.page.JdbcPagination;
import org.hibernate.dialect.Dialect;
import org.hibernate.dialect.SQLServer2008Dialect;
import org.hibernate.engine.spi.RowSelection;

import java.util.HashMap;
import java.util.Map;


/**
 * JDBC分页自定义方言
 *
 * @author weep
 */
public enum JDBCPageDialect {
    
    mysql {
        @Override
        public String generatePageSql(String sql, JdbcPagination page) {
            StringBuffer pageSql = new StringBuffer(sql);
            if (page != null)
                pageSql.append(" limit " + page.getBegin() + "," + page.getSize());
            return pageSql.toString();
        }
        
        @Override
        public void setDateDialect() {
            // TODO mysql时间类型方言等待写入
        }
        
    },
    oracle {
        @Override
        public String generatePageSql(String sql, JdbcPagination page) {
            StringBuffer pageSql = new StringBuffer(sql);
            if (page != null) {
                pageSql.insert(0, "select * from (select tmp_tb.*,ROWNUM row_id from (")
                        .append(") tmp_tb where ROWNUM<=").append(page.getEnd()).append(") where row_id>")
                        .append(page.getBegin());
            }
            return pageSql.toString();
        }
        
        @Override
        public void setDateDialect() {
            // TODO oracle时间类型方言等待写入
            
        }
        
    },
    sqlserver {
        @Override
        public String generatePageSql(String sql, JdbcPagination page) {
            String newSql = sql;
            String orderBy = null;
            
            int orderIndix = newSql.toUpperCase().lastIndexOf(" ORDER ");
            if (orderIndix != -1) {
                orderBy = newSql.substring(orderIndix);
                newSql = newSql.substring(0, orderIndix);
            }
            
            if (page != null) {
                Dialect dialect = null;
                dialect = new SQLServer2008Dialect();
                RowSelection rs = new RowSelection();
                rs.setFirstRow(1);
                rs.setMaxRows(2);
                newSql = dialect.buildLimitHandler(newSql, rs).getProcessedSql();
                newSql = this.setPageParam(newSql, page);
            }
            if (orderIndix != -1) {
                newSql = newSql.replaceFirst("ORDER BY CURRENT_TIMESTAMP", orderBy);
            }
            return newSql;
        }
        
        @Override
        public void setDateDialect() {
            dateDialect.put("castDateTime", "cast('{0}' as datetime)");
            dateDialect.put("convertTime", "convert(varchar(50),{0},120)");
        }
        
    };
    
    public static final Map<String, String> dateDialect = new HashMap<String, String>();
    
    protected String setPageParam(String sql, JdbcPagination page) {
        StringBuilder sb = new StringBuilder(sql);
        
        sb.replace(sb.lastIndexOf("?"), sb.lastIndexOf("?") + 1, +page.getEnd() + 1 + "");
        sb.replace(sb.lastIndexOf("?"), sb.lastIndexOf("?") + 1, page.getBegin() + 1 + "");
        return sb.toString();
    }
    
    public abstract String generatePageSql(String sql, JdbcPagination page);
    
    public abstract void setDateDialect();
}
