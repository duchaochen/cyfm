package com.ppcxy.cyfm.sys.repository.jpa.permission;

import com.ppcxy.common.repository.jpa.BaseRepository;
import com.ppcxy.cyfm.sys.entity.permission.Role;

public interface RoleDao extends BaseRepository<Role, Long> {

    Role findByName(String name);
    
    Role findByValue(String value);
}
