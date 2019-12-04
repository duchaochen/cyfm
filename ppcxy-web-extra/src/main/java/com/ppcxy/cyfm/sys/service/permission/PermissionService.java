package com.ppcxy.cyfm.sys.service.permission;

import com.ppcxy.common.service.BaseService;
import com.ppcxy.cyfm.sys.entity.permission.Permission;
import com.ppcxy.cyfm.sys.repository.jpa.permission.PermissionDao;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import javax.transaction.Transactional;

/**
 * Created by weep on 2016-7-7.
 */
@Service
@Transactional
public class PermissionService extends BaseService<Permission, Long> {
    @Resource
    private PermissionDao permissionDao;

    public Permission findByName(String name) {
        return permissionDao.findByName(name);
    }

    public Permission findByValue(String value) {
        return permissionDao.findByValue(value);
    }
}
