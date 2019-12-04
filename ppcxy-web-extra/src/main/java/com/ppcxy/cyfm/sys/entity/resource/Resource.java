package com.ppcxy.cyfm.sys.entity.resource;

import com.ppcxy.common.entity.IdEntity;
import com.ppcxy.common.extend.entity.Treeable;
import com.ppcxy.common.repository.jpa.support.annotation.EnableQueryCache;
import com.ppcxy.cyfm.sys.entity.resource.dto.MenuType;
import org.apache.commons.lang3.StringUtils;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.Formula;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.Transient;

@Entity
@Table(name = "cy_sys_resource")
@EnableQueryCache
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class Resource extends IdEntity implements Treeable<Long> {
    
    /**
     * 标题
     */
    private String name;
    /**
     * 菜单打开类型
     */
    private MenuType resourceType = MenuType.rightFrame;
    /**
     * 资源标识符 用于权限匹配的 如sys:resource
     */
    private String identity;
    /**
     * 点击后前往的地址
     * 菜单才有
     */
    private String url;
    /**
     * 父路径
     */
    private Long parentId;
    
    private String parentIds;
    
    private Integer weight;
    /**
     * 图标
     */
    private String icon;
    /**
     * 是否有叶子节点
     */
    private boolean hasChildren;
    /**
     * 是否显示
     */
    private Boolean show = Boolean.FALSE;
    
    public String getName() {
        return name;
    }
    
    public void setName(String name) {
        this.name = name;
    }
    
    public MenuType getResourceType() {
        return resourceType;
    }
    
    public void setResourceType(MenuType resourceType) {
        this.resourceType = resourceType;
    }
    
    @Column(name = "_identity")
    public String getIdentity() {
        return identity;
    }
    
    public void setIdentity(String identity) {
        this.identity = identity;
    }
    
    public String getUrl() {
        return url;
    }
    
    public void setUrl(String url) {
        this.url = url;
    }
    
    public Long getParentId() {
        return parentId;
    }
    
    public void setParentId(Long parentId) {
        this.parentId = parentId;
    }
    
    public String getParentIds() {
        return parentIds;
    }
    
    public void setParentIds(String parentIds) {
        this.parentIds = parentIds;
    }
    
    @Override
    @Transient
    public String getSeparator() {
        return "/";
    }
    
    @Override
    public String makeSelfAsNewParentIds() {
        return getParentIds() + getId() + getSeparator();
    }
    
    public Integer getWeight() {
        return weight;
    }
    
    public void setWeight(Integer weight) {
        this.weight = weight;
    }
    
    public String getIcon() {
        if (!StringUtils.isEmpty(icon)) {
            return icon;
        }
        if (isRoot()) {
            return getRootDefaultIcon();
        }
        if (isLeaf()) {
            return getLeafDefaultIcon();
        }
        return getBranchDefaultIcon();
    }
    
    public void setIcon(String icon) {
        this.icon = icon;
    }
    
    @Formula(value = "(select count(*) from cy_sys_resource f_t where f_t.parent_id = id)")
    public boolean isHasChildren() {
        return hasChildren;
    }
    
    public void setHasChildren(boolean hasChildren) {
        this.hasChildren = hasChildren;
    }
    
    @Column(name = "is_show")
    public Boolean getShow() {
        return show;
    }
    
    public void setShow(Boolean show) {
        this.show = show;
    }
    
    
    /**
     * 根节点默认图标 如果没有默认 空即可
     *
     * @return 默认根节点图标样式
     */
    @Transient
    public String getRootDefaultIcon() {
        return "ztree_setting";
    }
    
    /**
     * 树枝节点默认图标 如果没有默认 空即可
     *
     * @return 默认分支节点图标样式
     */
    @Transient
    public String getBranchDefaultIcon() {
        return "ztree_folder";
    }
    
    /**
     * 树叶节点默认图标 如果没有默认 空即可
     *
     * @return 默认叶子节点图标样式
     */
    @Transient
    public String getLeafDefaultIcon() {
        return "fa fa-folder-open";
    }
    
    @Transient
    public boolean isRoot() {
        return getParentId() != null && getParentId() == 0;
    }
    
    @Transient
    public boolean isLeaf() {
        if (isRoot()) {
            return false;
        }
        return !isHasChildren();
    }
}
