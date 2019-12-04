package com.ppcxy.jdbc.page;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.ppcxy.jdbc.dialect.DialectUtils;
import com.ppcxy.jdbc.dialect.JDBCPageDialect;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.core.JdbcTemplate;

import java.util.Iterator;
import java.util.List;
import java.util.Map;


/**
 * JDBC 公共分页.
 *
 * @author weep
 */
public class JdbcPagination {

    /**
     * slf日志类
     **/
    private final Logger logger = LoggerFactory.getLogger(JdbcPagination.class);

    /**
     * 默认分页数
     **/
    public static final int DEFAULT_PAGE_SIZE = 10;

    // 单页记录数
    private int size = 0;
    // 记录总数
    private int total;
    // 总页数
    private int totalPages;
    // 当前页码
    private int currentPage = 1;
    // 起始行数
    private int begin;
    // 结束行数
    private int end;

    // 结果集存放List
    private List<Map<String, Object>> content;
    // JdbcTemplate jdbcTemplate
    @JsonIgnore
    private JdbcTemplate jdbcTemplate;

    /**
     * 私有构造器，喜欢这样写.
     */
    private JdbcPagination() {

    }

    /**
     * 实例化分页对象，并设置分页使用的jdbcTemplate,在我的项目里有JdbcTemplate的子类,用来实现多数据源。
     *
     * @param jdbcTemplate
     * @return
     */
    public static JdbcPagination newInstance(JdbcTemplate jdbcTemplate) {
        JdbcPagination page = new JdbcPagination();
        page.setJdbcTemplate(jdbcTemplate);
        return page;
    }

    /**
     * 检查分页工具是否已经准备好.
     *
     * @param sql
     * @return
     */
    private boolean vilaQuery(String sql) {
        if (jdbcTemplate == null) {
            logger.warn("未初始化查询jdbcTemplate,Sql：[{}]", sql);
            throw new IllegalArgumentException("jdbcTemplate is null,please initial it first. ");
        } else if (sql.equals("")) {
            logger.warn("传入的sql语句为空");
            throw new IllegalArgumentException("sql is empty,please initial it first. ");
        }
        return true;
    }

    /**
     * 传入sql按照默认参数进行分页查询，每页显示10条.
     *
     * @param sql
     * @return
     */
    public JdbcPagination findPage(String sql) {
        vilaQuery(sql);
        return findPage(sql, currentPage, getPageSize());
    }

    /**
     * 传入sql和页数进行分页查询，每页显示10条.
     *
     * @param sql
     * @param pageNumber
     * @return
     */
    public JdbcPagination findPage(String sql, int pageNumber) {
        vilaQuery(sql);
        return findPage(sql, pageNumber, getPageSize());
    }

    /**
     * 传入sql,显示的页码, 每页显示条数进行分页.
     *
     * @param sql
     * @param pageNumber
     * @param pageSize
     */
    public JdbcPagination findPage(String sql, int pageNumber, int pageSize) {

        vilaQuery(sql);

        // 设置每页显示记录数
        setSize(pageSize != 0 ? pageSize : DEFAULT_PAGE_SIZE);
        // 设置要显示的页数
        setCurrentPage(pageNumber);
        // 计算总记录数
        StringBuffer totalSQL = new StringBuffer(" SELECT count(*) as count FROM ( ");
        int orderIndex = sql.toUpperCase().lastIndexOf(" ORDER ");
        if (orderIndex != -1) {
            totalSQL.append(sql.substring(0, orderIndex));
        } else {
            totalSQL.append(sql);
        }
        totalSQL.append(" ) totalTable ");

        // 总记录数
        setTotal(Integer.parseInt(getJdbcTemplate().queryForMap(totalSQL.toString()).get("count").toString()));
        // 计算总页数
        setTotalPages();
        // 计算起始行数
        setBegin();
        // 计算结束行数
        setEnd();
        JDBCPageDialect md = JDBCPageDialect.valueOf(DialectUtils.getDialect(this.jdbcTemplate.getDataSource()));

        String queryPageSql = md.generatePageSql(sql, this);
        // 装入结果集
        setContent(getJdbcTemplate().queryForList(queryPageSql));
        return this;
    }

    public int getCurrentPage() {
        return currentPage;
    }

    public void setCurrentPage(int currentPage) {
        this.currentPage = currentPage;
    }

    public int getSize() {
        return size;
    }

    public void setSize(int size) {
        this.size = size;
    }

    public List<Map<String, Object>> getContent() {
        return content;
    }

    public void setContent(List<Map<String, Object>> content) {
        this.content = content;
    }

    public int getTotalPages() {
        return totalPages;
    }

    public void setTotalPages() {
        if (total % size == 0) {
            this.totalPages = total / size;
        } else {
            this.totalPages = (total / size) + 1;
        }
    }

    public int getTotal() {
        return total;
    }

    public void setTotal(int total) {
        this.total = total;
    }

    public int getBegin() {
        return begin;
    }

    public void setBegin() {
        this.begin = (currentPage - 1) * size;
    }

    public int getEnd() {
        return end;
    }

    // 计算结束时候的索引
    public void setEnd() {
        if (total < size) {
            this.end = total;
        } else if ((total % size == 0) || (total % size != 0 && currentPage < totalPages)) {
            this.end = currentPage * size;
        } else if (total % size != 0 && currentPage == totalPages) {
            // 最后一页
            this.end = total;
        }
    }

    public Iterator<Map<String, Object>> iterator() {
        return content.iterator();
    }

    public boolean hasContent() {
        return !content.isEmpty();
    }

    public boolean hasNext() {
        return getCurrentPage() < getTotalPages();
    }

    public boolean isLast() {
        return !hasNext();
    }

    public boolean hasPrevious() {
        return getCurrentPage() > 1;
    }

    public boolean isFirst() {
        return !hasPrevious();
    }

    public JdbcTemplate getJdbcTemplate() {
        return jdbcTemplate;
    }

    public void setJdbcTemplate(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    /**
     * huo
     *
     * @return
     */
    private int getPageSize() {
        return size != 0 ? size : DEFAULT_PAGE_SIZE;
    }
}
