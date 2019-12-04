package com.ppcxy.cyfm.sys.service.team;

import com.ppcxy.common.entity.search.Searchable;
import com.ppcxy.common.service.BaseService;
import com.ppcxy.cyfm.sys.entity.team.Team;
import com.ppcxy.cyfm.sys.entity.user.User;
import com.ppcxy.cyfm.sys.repository.jpa.team.TeamDao;
import com.ppcxy.cyfm.sys.repository.jpa.user.UserDao;
import org.javasimon.aop.Monitored;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Created by weep on 2016-5-16.
 */
@Service
@Transactional
@Monitored
public class TeamService extends BaseService<Team, Long> {
    @Autowired
    private TeamDao teamDao;
    
    @Autowired
    private UserDao userDao;
    
    public Page<Team> test(Searchable searchable) {
        return teamDao.findAll(searchable);
    }
    
    @Override
    public Team save(Team entity) {
        
        entity = super.save(entity);
        //初始化团队成员信息
        updateUserTeam(entity);
        
        return entity;
    }
    
    @Override
    public Team update(Team entity) {
        
        //将当前团队用户移除先
        List<User> userList = entity.getUserList();
        for (User user : userList) {
            user.setTeam(null);
        }
        //刷新新的团队成员信息
        updateUserTeam(entity);
        return super.update(entity);
    }
    
    /**
     * 将用户加入到当前团队
     *
     * @param entity
     */
    private void updateUserTeam(Team entity) {
        List<User> newUserList = entity.getNewUserList();
        for (User user : newUserList) {
            user = userDao.findOne(user.getId());
            user.setTeam(entity);
        }
    }
    
    @Override
    public void delete(Long id) {
        //移除团队所有成员
        teamDao.removeAllMember(id);
        super.delete(id);
    }
    
    @Override
    public void delete(Long[] ids) {
        for (Long id : ids) {
            //移除团队所有成员
            teamDao.removeAllMember(id);
        }
        super.delete(ids);
    }
    
    @Override
    public void delete(Team team) {
        //移除团队所有成员
        teamDao.removeAllMember(team.getId());
        super.delete(team);
    }
    
}
