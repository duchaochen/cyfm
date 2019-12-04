package com.ppcxy.jdbc.page.util;

import org.apache.commons.lang3.StringUtils;
import org.springside.modules.utils.Collections3;

import java.util.Collection;
import java.util.Map;


/**
 * 编译分页sql工具类.
 *
 * @author weep
 */
public class DynamicFiltersUtil {
	private final StringBuffer queryPageSql;
	private StringBuffer orderBySql;
	private String whereSql;
	
	private DynamicFiltersUtil(String queryPageSql) {
		this.queryPageSql = new StringBuffer(queryPageSql);
	}
	
	/**
	 * 实例化工具类实例.
	 *
	 * @param sql
	 * @return
	 */
	public static DynamicFiltersUtil newInstance(String sql) {
		return new DynamicFiltersUtil(sql);
	}
	
	/**
	 * 编译查询参数.
	 *
	 * @param searchParams
	 * @return
	 */
	public DynamicFiltersUtil buildParams(Map<String, Object> searchParams) {
		Collection<JdbcSeachFilter> filters = JdbcSeachFilter.parse(searchParams).values();
		StringBuffer resultBuffer = new StringBuffer();
		if (Collections3.isNotEmpty(filters)) {
			JdbcSeachFilter[] filtersArray = filters.toArray(new JdbcSeachFilter[]{});
			for (int i = 0; i < filtersArray.length; i++) {
				JdbcSeachFilter filter = filtersArray[i];
				
				// logic operator
				switch (filter.operator) {
					case EQ:
						resultBuffer.append(filter.fieldName).append("='").append(filter.value).append("'");
						break;
					case LIKE:
						resultBuffer.append(filter.fieldName).append(" like '%").append(filter.value).append("%'");
						break;
					case GT:
						resultBuffer.append(filter.fieldName).append(">").append("'").append(filter.value).append("'");
						break;
					case LT:
						resultBuffer.append(filter.fieldName).append("<").append("'").append(filter.value).append("'");
						break;
					case GTE:
						resultBuffer.append(filter.fieldName).append(">=").append("'").append(filter.value).append("'");
						break;
					case LTE:
						resultBuffer.append(filter.fieldName).append("<=").append("'").append(filter.value).append("'");
						break;
					case IN:
						resultBuffer.append(filter.fieldName).append(" in (").append("'").append(filter.value).append("'").append(") ");
						break;
				}
				if (i + 1 != filtersArray.length) {
					resultBuffer.append(" and ");
				}
			}
		}
		this.whereSql = resultBuffer.toString();
		return this;
	}
	
	/**
	 * 添加排序字段,可链式操作添加多个.
	 *
	 * @param byName
	 * @param order
	 * @return
	 */
	public DynamicFiltersUtil addSoft(String byName, String order) {
		StringBuffer softSb = null;
		if (orderBySql == null || "".equals(orderBySql)) {
			softSb = new StringBuffer(" order by ");
			softSb.append(byName).append(" ").append(order);
			this.orderBySql = softSb;
			return this;
		}
		this.orderBySql.append(",").append(byName).append(" ").append(order);
		
		return this;
	}
	
	/**
	 * 添加排序字符串，会覆盖之前添加的排序信息.
	 *
	 * @param orderBy
	 * @return
	 */
	public DynamicFiltersUtil addSoft(String orderBy) {
		StringBuffer softSb = new StringBuffer(" order by ");
		this.orderBySql = softSb.append(orderBy);
		return this;
	}
	
	/**
	 * 编译完整查询sql,返回String 类型sql语句.
	 *
	 * @return
	 */
	public String builder() {
		String groupByStr = "";
		
		//TODO 为了处理查询语句结尾是group by语句，存在不必要性
		String tsql = queryPageSql.toString().toLowerCase();
		if (tsql.contains("group by")) {
			int groupByIndex = tsql.lastIndexOf("group by");
			int lastKhIndex = tsql.lastIndexOf(") ");
			
			if (groupByIndex > lastKhIndex) {
				groupByStr = queryPageSql.toString().substring(groupByIndex);
				queryPageSql.delete(groupByIndex, groupByIndex + groupByStr.length());
				
			}
		}
		
		if (whereSql != null && !"".equals(whereSql)) {
			queryPageSql.append(" where ").append(whereSql).append(" ");
		}
		
		if (StringUtils.isNotBlank(groupByStr)) {
			queryPageSql.append(groupByStr);
			
		}
		if (orderBySql != null && !"".equals(orderBySql)) {
			queryPageSql.append(orderBySql);
		}
		
		return queryPageSql.toString();
	}
	
}
