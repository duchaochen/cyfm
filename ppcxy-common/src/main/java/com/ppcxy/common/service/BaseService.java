package com.ppcxy.common.service;

import com.google.common.collect.Lists;
import com.ppcxy.common.entity.AbstractEntity;
import com.ppcxy.common.entity.search.Searchable;
import com.ppcxy.common.repository.jpa.BaseRepository;
import com.ppcxy.common.spring.SpringContextHolder;
import com.ppcxy.common.utils.ServletUtils;
import com.ppcxy.common.utils.ShiroUserInfoUtils;
import com.ppcxy.common.utils.excel.support.impl.ExcelDatasExportUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.jdbc.core.JdbcTemplate;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

/**
 * <p>抽象service层基类 提供一些简便方法
 * <p/>
 * <p>泛型 ： entity 表示实体类型；ID表示主键类型
 * <p/>
 */
public abstract class BaseService<T extends AbstractEntity, ID extends Serializable> {
    
    protected BaseRepository<T, ID> baseRepository;
    
    
    @Autowired
    public void setBaseRepository(BaseRepository<T, ID> baseRepository) {
        this.baseRepository = baseRepository;
    }
    
    /**
     * 保存单个实体
     *
     * @param entity 实体
     * @return 返回保存的实体
     */
    public T save(T entity) {
        return baseRepository.save(entity);
    }
    
    /**
     * 保存集合实体
     *
     * @param ms 实体集合
     * @return 返回保存的实体
     */
    public Iterable<T> save(Iterable<T> ms) {
        return baseRepository.save(ms);
    }
    
    public T saveAndFlush(T entity) {
        entity = save(entity);
        baseRepository.flush();
        return entity;
    }
    
    /**
     * 更新单个实体
     *
     * @param entity 实体
     * @return 返回更新的实体
     */
    public T update(T entity) {
        return baseRepository.save(entity);
    }
    
    /**
     * 根据主键删除相应实体
     *
     * @param id 主键
     */
    public void delete(ID id) {
        baseRepository.delete(id);
    }
    
    /**
     * 删除实体
     *
     * @param entity 实体
     */
    public void delete(T entity) {
        baseRepository.delete(entity);
    }
    
    /**
     * 根据主键删除相应实体
     *
     * @param ids 实体
     */
    public void delete(ID[] ids) {
        baseRepository.delete(ids);
    }
    
    
    /**
     * 按照主键查询
     *
     * @param id 主键
     * @return 返回id对应的实体
     */
    public T findOne(ID id) {
        return baseRepository.findOne(id);
    }
    
    /**
     * 实体是否存在
     *
     * @param id 主键
     * @return 存在 返回true，否则false
     */
    public boolean exists(ID id) {
        return baseRepository.exists(id);
    }
    
    /**
     * 统计实体总数
     *
     * @return 实体总数
     */
    public long count() {
        return baseRepository.count();
    }
    
    /**
     * 查询所有实体
     *
     * @return
     */
    public List<T> findAll() {
        return baseRepository.findAll();
    }
    
    /**
     * 按照顺序查询所有实体
     *
     * @param sort
     * @return
     */
    public List<T> findAll(Sort sort) {
        return baseRepository.findAll(sort);
    }
    
    /**
     * 分页及排序查询实体
     *
     * @param pageable 分页及排序数据
     * @return
     */
    public Page<T> findAll(Pageable pageable) {
        return baseRepository.findAll(pageable);
    }
    
    /**
     * 按条件分页并排序查询实体
     *
     * @param searchable 条件
     * @return
     */
    public Page<T> findAll(Searchable searchable) {
        return baseRepository.findAll(searchable);
    }
    
    /**
     * 按条件不分页不排序查询实体
     *
     * @param searchable 条件
     * @return
     */
    public List<T> findAllWithNoPageNoSort(Searchable searchable) {
        searchable.removePageable();
        searchable.removeSort();
        return Lists.newArrayList(baseRepository.findAll(searchable).getContent());
    }
    
    /**
     * 按条件排序查询实体(不分页)
     *
     * @param searchable 条件
     * @return
     */
    public List<T> findAllWithSort(Searchable searchable) {
        searchable.removePageable();
        return Lists.newArrayList(baseRepository.findAll(searchable).getContent());
    }
    
    
    /**
     * 按条件分页并排序统计实体数量
     *
     * @param searchable 条件
     * @return
     */
    public Long count(Searchable searchable) {
        return baseRepository.count(searchable);
    }
    
    
    public void exportData2Excel(String identity, Class<T> entityClass, Searchable searchable, String title, String exportModel) {
        JdbcTemplate jdbcTemplate = SpringContextHolder.getBean("jdbcTemplate");
        
        List<Map<String, Object>> exportTemplateIds = jdbcTemplate.queryForList("SELECT * FROM cy_excel_template WHERE resource_identity = ? and template_id <> '' ", identity);
        
        
        final ExcelDatasExportUtils<T> export = new ExcelDatasExportUtils<>(entityClass);
        
        final String realPath = ServletUtils.loadRealPath();
        
        try {
            new Thread(new Runnable() {
                @Override
                public void run() {
                    if (exportModel.equals("all")) {
                        
                        if (exportTemplateIds.size() > 0) {
                            String[] files = exportTemplateIds.get(0).get("template_id").toString().split(",");
                            for (String file : files) {
                                Map<String, Object> fileMap = jdbcTemplate.queryForMap("SELECT * FROM fs_files WHERE id = ?", file);
                                
                                
                                export.exportTableDatasTemplate(fileMap.get("location").toString(), fileMap.get("real_name").toString(), findAllWithNoPageNoSort(searchable), ShiroUserInfoUtils.getUsername(), realPath);
                            }
                            
                        } else {
                            searchable.setPage(0, 200);
                            //TODO 导出全部需要按照分页异步导出
                            export.ready("导出数据", ShiroUserInfoUtils.getUsername(), realPath);
                            Page<T> page = null;
                            do {
                                page = findAll(searchable);
                                searchable.setPage(searchable.getPage().getPageNumber() + 1, 200);
                                export.additionalDataForBean(page.getContent());
                            } while (page.hasNext());
                            
                            export.complete();
                        }
                    } else {
                        if (exportTemplateIds.size() > 0) {
                            String[] files = exportTemplateIds.get(0).get("template_id").toString().split(",");
                            for (String file : files) {
                                Map<String, Object> fileMap = jdbcTemplate.queryForMap("SELECT * FROM fs_files WHERE id = ? ", file);
                                
                                
                                export.exportTableDatasTemplate(fileMap.get("location").toString(), fileMap.get("real_name").toString(), findAll(searchable).getContent(), ShiroUserInfoUtils.getUsername(), realPath);
                            }
                            
                        } else {
                            export.exportTableDatas(title != null ? title : entityClass.getName(), findAll(searchable).getContent(), ShiroUserInfoUtils.getUsername(), realPath);
                        }
                    }
                }
            }).start();
        } catch (Exception e) {
            throw e;
        }
    }
}
