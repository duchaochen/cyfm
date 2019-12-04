package com.ppcxy.cyfm.bus.system.web;

import com.ppcxy.common.web.controller.BaseCRUDController;
import com.ppcxy.cyfm.bus.system.entity.RemoteApiModel;
import com.ppcxy.cyfm.bus.system.service.JoinSystemService;
import com.ppcxy.cyfm.bus.system.service.RemoteApiModelService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springside.modules.mapper.JsonMapper;

import java.util.HashMap;

/**
 * Created by weep on 2016-5-23.
 */
@Controller
@RequestMapping("/bus/system/remoteApi")
public class RemoteApiController extends BaseCRUDController<RemoteApiModel, Long> {
    
    @Autowired
    private JoinSystemService joinSystemService;
    @Autowired
    private RemoteApiModelService remoteApiModelService;
    
    @Override
    protected void preResponse(Model model) {
        super.preResponse(model);
        model.addAttribute("joinSystems", joinSystemService.findAll());
    }
    
    public RemoteApiController() {
        setResourceIdentity("bus:system:remoteApi");
        //和资源一致可以不处理
        setModelName("remoteApi");
    }
    
    @RequestMapping(value = "test", method = RequestMethod.GET)
    public String testRemoteApi(Model model) {
        preResponse(model);
        return viewName("test");
    }
    
    @RequestMapping(value = "test", method = RequestMethod.POST)
    @ResponseBody
    public String testRemoteApi(@RequestParam(value = "api") Long api, String params) {
        return remoteApiModelService.requestApi(api, JsonMapper.nonDefaultMapper().fromJson(params, HashMap.class));
    }
    
}
