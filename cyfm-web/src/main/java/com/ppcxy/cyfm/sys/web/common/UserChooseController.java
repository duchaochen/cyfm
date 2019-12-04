package com.ppcxy.cyfm.sys.web.common;

import com.ppcxy.common.entity.search.SearchOperator;
import com.ppcxy.common.entity.search.Searchable;
import com.ppcxy.common.entity.search.filter.SearchFilterHelper;
import com.ppcxy.common.web.bind.annotation.PageableDefaults;
import com.ppcxy.common.web.bind.annotation.SearchableDefaults;
import com.ppcxy.cyfm.sys.entity.user.User;
import com.ppcxy.cyfm.sys.service.user.UserService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.annotation.Resource;

@Controller
@RequestMapping(value = "/sys/common/userChoose")
public class UserChooseController {
    
    @Resource
    private UserService userService;
    
    @RequestMapping
    @SearchableDefaults(value = {"status=enabled"}, merge = true)
    @PageableDefaults(value = 5)
    public String dialog(Searchable searchable, Model model) {
        
        if (StringUtils.isNotBlank(searchable.getValue("keyword_custom"))) {
            searchable.or(
                    SearchFilterHelper.newCondition("name", SearchOperator.like, searchable.getValue("keyword_custom")),
                    SearchFilterHelper.newCondition("username", SearchOperator.like, searchable.getValue("keyword_custom"))
                    ,
                    SearchFilterHelper.newCondition("email", SearchOperator.like, searchable.getValue("keyword_custom"))
            );
        }
        
        Page<User> choosePage = userService.findAll(searchable);
        model.addAttribute("choosePage", choosePage);
        return "sys/common/user_choose";
        
    }
    
}
