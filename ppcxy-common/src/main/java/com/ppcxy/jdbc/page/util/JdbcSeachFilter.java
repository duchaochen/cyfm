package com.ppcxy.jdbc.page.util;

import com.google.common.collect.Maps;
import org.apache.commons.lang3.StringUtils;

import java.util.Map;
import java.util.Map.Entry;

/**
 * jdbc 分页条件编译.
 *
 * @author weep
 */
public class JdbcSeachFilter {
    public enum Operator {
        EQ, LIKE, GT, LT, GTE, LTE, IN
    }

    public String fieldName;
    public Object value;
    public Operator operator;

    public JdbcSeachFilter(String fieldName, Operator operator, Object value) {
        this.fieldName = fieldName;
        this.value = value;
        this.operator = operator;
    }

    /**
     * searchParams中key的格式为OPERATOR_FIELDNAME
     */
    public static Map<String, JdbcSeachFilter> parse(Map<String, Object> searchParams) {
        Map<String, JdbcSeachFilter> filters = Maps.newHashMap();

        for (Entry<String, Object> entry : searchParams.entrySet()) {
            // 过滤掉空值
            String key = entry.getKey();
            Object value = entry.getValue();
            if (StringUtils.isBlank((String) value)) {
                continue;
            }

            // 拆分operator与filedAttribute
            String[] names = StringUtils.split(key, "_");
            if (names.length < 2) {
                throw new IllegalArgumentException(key + " is not a valid search filter name");
            }

            String filedName = null;
            Operator operator = null;
            if (names.length == 2) {
                filedName = names[0];
                operator = Operator.valueOf(names[1]);
            } else {
                filedName = key.substring(0, key.lastIndexOf("_"));
                operator = Operator.valueOf(key.substring(key.lastIndexOf("_") + 1));
            }

            // 创建searchFilter
            JdbcSeachFilter filter = new JdbcSeachFilter(filedName, operator, value);
            filters.put(key, filter);
        }

        return filters;
    }
}
