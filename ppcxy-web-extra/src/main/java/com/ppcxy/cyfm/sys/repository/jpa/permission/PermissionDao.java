package com.ppcxy.cyfm.sys.repository.jpa.permission;

import com.ppcxy.common.repository.jpa.BaseRepository;
import com.ppcxy.cyfm.sys.entity.permission.Permission;

public interface PermissionDao extends BaseRepository<Permission, Long> {

    Permission findByName(String name);

    Permission findByValue(String value);
}
