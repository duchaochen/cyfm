package com.ppcxy.cyfm.sys.service.user;

import com.ppcxy.common.service.BaseService;
import com.ppcxy.common.utils.LogUtils;
import com.ppcxy.cyfm.sys.entity.user.UserOnline;
import com.ppcxy.cyfm.sys.repository.jpa.user.UserOnlineDao;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;

/**
 *
 */
@Service
@Transactional(value = "transactionManager")
public class UserOnlineService extends BaseService<UserOnline, String> {
    
    private UserOnlineDao getUserOnlineRepository() {
        return (UserOnlineDao) baseRepository;
    }
    
    /**
     * 上线
     *
     * @param userOnline
     */
    public void online(UserOnline userOnline) {
        try {
            save(userOnline);
        } catch (Exception e) {
            /// 上线状态同步错误
            LogUtils.logError("上线状态同步错误", e);
        }
    }
    
    /**
     * 下线
     *
     * @param sid
     */
    public void offline(String sid) {
        UserOnline userOnline = findOne(sid);
        if (userOnline != null) {
            delete(userOnline);
            
            if (userOnline.getSession() != null) {
                userOnline.getSession().markAttributeChanged();
            }
        }
        //游客 无需记录上次访问记录
        //此处使用数据库的触发器完成同步
//        if(userOnline.getUserId() == null) {
//            userLastOnlineService.lastOnline(UserLastOnline.fromUserOnline(userOnline));
//        }
    }
    
    /**
     * 批量下线
     *
     * @param needOfflineIdList
     */
    public void batchOffline(List<String> needOfflineIdList) {
        getUserOnlineRepository().batchDelete(needOfflineIdList);
    }
    
    /**
     * 无效的UserOnline
     *
     * @return
     */
    public Page<UserOnline> findExpiredUserOnlineList(Date expiredDate, Pageable pageable) {
        return getUserOnlineRepository().findExpiredUserOnlineList(expiredDate, pageable);
    }
    
    
}
