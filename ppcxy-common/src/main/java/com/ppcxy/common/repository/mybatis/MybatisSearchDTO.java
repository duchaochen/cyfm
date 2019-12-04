package com.ppcxy.common.repository.mybatis;

import org.apache.commons.lang3.StringUtils;

public abstract class MybatisSearchDTO {
    
    protected MybatisSearchPage page;
    protected String order;
    
    public String getOrder() {
        return order;
    }
    
    public void setOrder(String order) {
        this.order = order;
    }
    
    public MybatisSearchPage getPage() {
        return page;
    }
    
    public void setPage(MybatisSearchPage page) {
        this.page = page;
    }
    
    public long total() {
        return page == null ? -1 : page.getTotal();
    }
    
    public String getOrderProperty() {
        if (StringUtils.isNotBlank(order)) {
            return order.split(" ")[0];
        }
        return null;
    }
    
    public String getOrderSort() {
        if (StringUtils.isNotBlank(order)) {
            return order.split("[\\s]+")[1];
        }
        return null;
    }
}


