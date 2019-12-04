package com.ppcxy.cyfm.manage.service.maintaion.datasource;

import com.alibaba.druid.pool.DruidDataSource;
import com.ppcxy.common.exception.BaseException;
import com.ppcxy.common.service.BaseService;
import com.ppcxy.common.spring.SpringContextHolder;
import com.ppcxy.cyfm.manage.entity.maintaion.datasource.SourceManage;
import com.ppcxy.manage.maintain.datasource.support.DataSourceGenerates;
import com.ppcxy.manage.maintain.datasource.support.DsourceTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Created by weep on 2016-5-23.
 */
@Service
@Transactional
public class SourceManageService extends BaseService<SourceManage, Long> {
    
    public DsourceTemplate genDsTemplate(SourceManage sourceManage) {
        
        if (sourceManage == null) {
            throw new BaseException("未找到数据源配置项.");
        }
        DruidDataSource druidDataSource = DataSourceGenerates.newInstance().genDataSource(sourceManage.getDsName(), sourceManage.loadConDriver(), sourceManage.loadConUrl(), sourceManage.getDbUsername(), sourceManage.getDbPassword());
        
        DsourceTemplate dsourceTemplate = SpringContextHolder.getBean(DsourceTemplate.class);
        
        if (!dsourceTemplate.changeDatasource(druidDataSource)) {
            throw new BaseException("创建多数据源失败.");
        }
        
        return dsourceTemplate;
    }
    
    public boolean validateDatasource(SourceManage sourceManage) {
        DataSourceGenerates dataSourceGenerates = DataSourceGenerates.newInstance();
        DruidDataSource druidDataSource = dataSourceGenerates.genDataSource(sourceManage.getDsName(), sourceManage.loadConDriver(), sourceManage.loadConUrl(), sourceManage.getDbUsername(), sourceManage.getDbPassword());
        
        Boolean result = false;
        try {
            result = !druidDataSource.getConnection().isClosed();
        } catch (Exception e) {
            dataSourceGenerates.removeDataSource(sourceManage.getDsName(), sourceManage.loadConUrl(), sourceManage.getDbUsername());
            druidDataSource.close();
            throw new BaseException("验证数据源可用性时发生异常,作为验证连接失败处理.", e);
        }
        return result;
    }
}
