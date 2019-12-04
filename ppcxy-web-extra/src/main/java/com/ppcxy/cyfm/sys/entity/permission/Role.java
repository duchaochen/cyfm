package com.ppcxy.cyfm.sys.entity.permission;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.Sets;
import com.ppcxy.common.entity.IdEntity;
import com.ppcxy.common.repository.jpa.support.annotation.EnableQueryCache;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;

import javax.persistence.*;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

/**
 * 角色.
 *
 * @author calvin
 */
@Entity
@Table(name = "cy_sys_role")
@EnableQueryCache
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class Role extends IdEntity {
    private String name;

    private String value;

    private String permissions;

    private String description;

    private Set<RoleResourcePermission> roleResourcePermissions = Sets.newHashSet();
    
    public Role() {
    }

    public Role(Long id) {
        this.id = id;
    }

    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER, targetEntity = RoleResourcePermission.class, mappedBy = "role", orphanRemoval = true)
    @Fetch(FetchMode.SELECT)
    @Basic(optional = true, fetch = FetchType.EAGER)
//    @NotFound(action = NotFoundAction.IGNORE)
    @org.hibernate.annotations.Cache(usage = CacheConcurrencyStrategy.READ_WRITE)//集合缓存
    @OrderBy
    public Set<RoleResourcePermission> getRoleResourcePermissions() {
        if (roleResourcePermissions == null) {
            roleResourcePermissions = Sets.newHashSet();
        }
        return roleResourcePermissions;
    }

    public void setRoleResourcePermissions(Set<RoleResourcePermission> roleResourcePermissions) {
        this.roleResourcePermissions = roleResourcePermissions;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public String getPermissions() {
        return permissions;
    }

    public void setPermissions(String permissions) {
        this.permissions = permissions;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    @Transient
    public List<String> getPermissionList() {
        return ImmutableList.copyOf(StringUtils.split(permissions, ","));
    }

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this);
    }


    /**
     * 增加新的授权信息,验证是否已有资源,如果已有资源替换权限.
     *
     * @param roleResourcePermission 角色资源权限对象
     */
    public void addResourcePermission(RoleResourcePermission roleResourcePermission) {
        Iterator<RoleResourcePermission> it = roleResourcePermissions.iterator();

        while (it.hasNext()) {
            RoleResourcePermission current = it.next();
            if (current.getResourceId().equals(roleResourcePermission.getResourceId())) {
                current.setPermissionIds(roleResourcePermission.getPermissionIds());
                return;
            }
        }

        roleResourcePermission.setRole(this);
        getRoleResourcePermissions().add(roleResourcePermission);
    }

    public void clearResourcePermission(Long[] resourceIds) {
        Iterator<RoleResourcePermission> it = roleResourcePermissions.iterator();

        while (it.hasNext()) {
            RoleResourcePermission resourcePermission = it.next();
            if (!ArrayUtils.contains(resourceIds, resourcePermission.getResourceId())) {
                it.remove();
            }
        }
    }
  
}
