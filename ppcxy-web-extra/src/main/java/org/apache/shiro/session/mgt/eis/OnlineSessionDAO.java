package org.apache.shiro.session.mgt.eis;

import com.ppcxy.cyfm.sys.entity.user.UserOnline;
import com.ppcxy.cyfm.sys.service.user.UserOnlineService;
import org.apache.shiro.ShiroConstants;
import org.apache.shiro.session.Session;
import org.apache.shiro.session.mgt.OnlineSession;
import org.apache.shiro.session.mgt.OnlineSessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.context.event.EventListener;

import java.io.Serializable;
import java.util.Date;

/**
 * 对于db的操作 考虑使用 异步+队列机制
 * <p/>
 */
public class OnlineSessionDAO extends EnterpriseCacheSessionDAO {
    /**
     * 上次同步数据库的时间戳
     */
    private static final String LAST_SYNC_DB_TIMESTAMP =
            OnlineSessionDAO.class.getName() + "LAST_SYNC_DB_TIMESTAMP";

    private UserOnlineService userOnlineService;
    @Autowired
    private OnlineSessionFactory onlineSessionFactory;
    /**
     * 同步session到数据库的周期 单位为毫秒（默认5分钟）
     */
    private long dbSyncPeriod = 5 * 60 * 1000;
    
    public void setUserOnlineService(UserOnlineService userOnlineService) {
        this.userOnlineService = userOnlineService;
    }

    @EventListener
    public void handleContextRefresh(ContextRefreshedEvent event) {
        ApplicationContext context = event.getApplicationContext();
        this.setUserOnlineService(context.getBean(UserOnlineService.class));
    }

    public void setDbSyncPeriod(long dbSyncPeriod) {
        this.dbSyncPeriod = dbSyncPeriod;
    }

    @Override
    protected Session doReadSession(Serializable sessionId) {
        UserOnline userOnline = userOnlineService.findOne(String.valueOf(sessionId));
        if (userOnline == null) {
            return null;
        }
        return onlineSessionFactory.createSession(userOnline);
    }

    /**
     * 将会话同步到DB
     *
     * @param session
     */
    public void syncToDb(OnlineSession session) {

        Date lastSyncTimestamp = (Date) session.getAttribute(LAST_SYNC_DB_TIMESTAMP);

        //当会话中的属性改变时-->强制同步
        //如果对于属性丢失影响不大 可以考虑把这块功能去掉
        if (lastSyncTimestamp != null) {
            boolean needSync = true;
            long deltaTime = session.getLastAccessTime().getTime() - lastSyncTimestamp.getTime();
            if (deltaTime < dbSyncPeriod) { //时间差不足  无需同步
                needSync = false;
            }
            boolean isGuest = session.getUserId() == null || session.getUserId() == 0L;

            //如果不是游客 且session 数据变更了 同步
            if (isGuest == false && session.isAttributeChanged()) {
                needSync = true;
            }

            if (needSync == false) {
                return;
            }
        }

        session.setAttribute(LAST_SYNC_DB_TIMESTAMP, session.getLastAccessTime());

        //更新完后 重置标识
        if (session.isAttributeChanged()) {
            session.resetAttributeChanged();
        }

        userOnlineService.online(UserOnline.fromOnlineSession(session));

    }

    /**
     * 会话过期时 离线处理
     *
     * @param session
     */
    @Override
    protected void doDelete(Session session) {
        OnlineSession onlineSession = (OnlineSession) session;
        //定时任务删除的此时就不删除了
        if (onlineSession.getAttribute(ShiroConstants.ONLY_CLEAR_CACHE) == null) {
            try {
                userOnlineService.offline(String.valueOf(onlineSession.getId()));
            } catch (Exception e) {
                //即使删除失败也无所谓
            }
        }

    }


}
