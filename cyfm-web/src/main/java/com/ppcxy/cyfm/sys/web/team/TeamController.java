package com.ppcxy.cyfm.sys.web.team;

import com.ppcxy.common.web.controller.BaseCRUDController;
import com.ppcxy.cyfm.sys.entity.team.Team;
import com.ppcxy.cyfm.sys.entity.user.User;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * Created by weep on 2016-5-16.
 */
@Controller
@RequestMapping(value = "/sys/team")
public class TeamController extends BaseCRUDController<Team, Long> {
    
    public TeamController() {
        setResourceIdentity("sys:team");
        //和资源一致可以不处理
        setModelName("team");
    }
    
    @Override
    public void beforCreate(Model model, Team entity, BindingResult result, RedirectAttributes redirectAttributes) {
        super.beforCreate(model, entity, result, redirectAttributes);
        
        bindTeamNewUserList(entity);
    }
    
    @Override
    public void beforUpdate(Model model, Team entity, BindingResult result, String backURL, RedirectAttributes redirectAttributes) {
        super.beforUpdate(model, entity, result, backURL, redirectAttributes);
        
        bindTeamNewUserList(entity);
    }
    
    private void bindTeamNewUserList(Team entity) {
        HttpServletRequest request = ((ServletRequestAttributes) (RequestContextHolder.currentRequestAttributes())).getRequest();
        String[] userIds = request.getParameterValues("userList.id");
        
        if (userIds != null) {
            List<User> userList = entity.getNewUserList();
            userList.clear();
            //重新初始化用户列表
            for (String userId : userIds) {
                User user = new User();
                user.setId(new Long(userId));
                userList.add(user);
            }
        }
    }
    
}
