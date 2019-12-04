package com.ppcxy.common.repository.mybatis;

import com.github.pagehelper.ISelect;
import com.github.pagehelper.PageHelper;
import com.ppcxy.common.entity.search.Searchable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;

public class MybatisPageHalper {
    
    public static <T> Page<T> pageExecute(Searchable searchable, MybatisSearchDTO searchDTO, ISelect select, long total) {
        //设置动态排序参数
        searchDTO.setOrder(searchable.getSort().toString().replace(":", " "));
        
        //进行page对象转换并执行分页查询
        com.github.pagehelper.Page page = PageHelper.startPage(searchable.getPage().getPageNumber() + 1, searchable.getPage().getPageSize()).setCount(total == -1).doSelectPage(select);
        
        return (Page<T>) new PageImpl(page.getResult(), new PageRequest(page.getPageNum() - 1, page.getPageSize()), total != -1 ? total : page.getTotal());
    }
    
    public static <T> Page<T> pageExecute(Searchable searchable, MybatisSearchDTO searchDTO, ISelect select) {
        //进行page对象转换并执行分页查询
        return pageExecute(searchable, searchDTO, select, -1);
    }
}
