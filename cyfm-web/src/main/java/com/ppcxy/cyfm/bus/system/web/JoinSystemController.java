package com.ppcxy.cyfm.bus.system.web;

import com.ppcxy.common.web.controller.BaseCRUDController;
import com.ppcxy.cyfm.bus.system.entity.JoinSystem;
import com.ppcxy.cyfm.bus.system.service.JoinSystemService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * Created by weep on 2016-5-23.
 */
@Controller
@RequestMapping("/bus/system/joinSystem")
public class JoinSystemController extends BaseCRUDController<JoinSystem, Long> {
    
    @Autowired
    private JoinSystemService joinSystemService;
    
    public JoinSystemController() {
        setResourceIdentity("bus:system:joinSystem");
        //和资源一致可以不处理
        setModelName("joinSystem");
    }
    
}
