package com.ppcxy.cyfm.nbpm.calcengine.funs.impl;

import com.ppcxy.common.spring.SpringContextHolder;
import com.ppcxy.cyfm.nbpm.calcengine.funs.exception.CalcExecuteException;
import com.ppcxy.cyfm.nbpm.calcengine.funs.FunctionExt;
import org.apache.commons.lang3.ArrayUtils;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;

import java.util.Arrays;
import java.util.List;
import java.util.Map;


public class SqlExecuteFun extends FunctionExt {
    
    private JdbcTemplate jdbcTemplate;
    
    @Override
    public Object call(Object[] objects) {
        if (objects.length == 0) {
            throw new CalcExecuteException("执行公式 executeSql 发生错误：错误的参数个数, Expected >=1  Actual== 0 ");
        }
        
        jdbcTemplate = SpringContextHolder.getBean("jdbcTemplate");
        
        List<Map<String, Object>> result = null;
        String sql = (String) objects[0];
        Object[] params = null;
        try {
            //第一个参数固定为sql语句
            if (objects.length == 1) {
                result = jdbcTemplate.queryForList(sql);
                
            } else {
                //截取除了sql语句剩余参数为sql参数
                params = ArrayUtils.subarray(objects, 1, objects.length);
                result = jdbcTemplate.queryForList(sql, params);
            }
        } catch (DataAccessException e) {
            throw new CalcExecuteException(String.format("执行公式中的sql发生错误，sql: %s , params [%s]", sql, Arrays.toString(params)), e);
        }
        
        
        return result;
    }
    
    @Override
    public String getName() {
        return "executeSql";
    }
    
}
