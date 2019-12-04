package com.ppcxy.cyfm.sys.repository.jpa.user;

import com.ppcxy.common.repository.jpa.BaseRepository;
import com.ppcxy.cyfm.sys.entity.permission.Role;
import com.ppcxy.cyfm.sys.entity.user.User;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface UserDao extends BaseRepository<User, Long> {
    
    User findByName(String name);
    
    User findByUsername(String username);
    
    User findByEmail(String email);
    
    User findByTel(String tel);
    
    @Query("select u from User u where exists (select r from Role r where r = ?1 and r member of u.roleList) order by u.name")
    List<User> findByRole(Role role);
}
