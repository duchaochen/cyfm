package com.ppcxy.cyfm.sys.service.permission;

import com.ppcxy.common.service.BaseService;
import com.ppcxy.cyfm.sys.entity.permission.Role;
import com.ppcxy.cyfm.sys.entity.user.User;
import com.ppcxy.cyfm.sys.repository.jpa.permission.RoleDao;
import com.ppcxy.cyfm.sys.repository.jpa.user.UserDao;
import com.ppcxy.cyfm.sys.service.authorize.AuthorizeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import javax.transaction.Transactional;
import java.util.List;

/**
 * Created by weep on 2016-7-7.
 */
@Service
@Transactional
public class RoleService extends BaseService<Role, Long> {
    
    @Resource
    private RoleDao roleDao;
    @Resource
    private UserDao userDao;
    
    @Autowired
    private AuthorizeService authorizeService;
    
    public Role findByName(String name) {
        return roleDao.findByName(name);
    }
    
    public Role findByValue(String value) {
        return roleDao.findByValue(value);
    }
    
    @Override
    public Role update(Role entity) {
        Role update = super.update(entity);
        authorizeService.refreshAll();
        return update;
    }
    
    public List<User> findUsers(Role role) {
        return userDao.findByRole(role);
    }
    
    public void allotRole2User(Long roleId, Long[] userIds) {
        Role role = roleDao.findOne(roleId);
        
        for (Long userId : userIds) {
            User user = userDao.findOne(userId);
            user.getRoleList().add(role);
            userDao.save(user);
        }
    }
    
    public void removeRoleAllot(Long roleId, Long userId) {
        User user = userDao.findOne(userId);
        user.getRoleList().remove(roleDao.findOne(roleId));
        userDao.save(user);
    }
    
    public void addRoleAllot(String type, Long roleId, Long[] targetIds) {
        switch (type) {
            case "user":
                allotRole2User(roleId, targetIds);
                break;
            case "department":
                break;
            case "group":
                break;
            default:
                ///
        }
        authorizeService.refreshAll();
    }
    
    public void removeRoleAllot(String type, Long roleId, Long targetId) {
        switch (type) {
            case "user":
                removeRoleAllot(roleId, targetId);
                break;
            case "department":
                break;
            case "group":
                break;
            default:
                ///
        }
        authorizeService.refreshAll();
    }
}
