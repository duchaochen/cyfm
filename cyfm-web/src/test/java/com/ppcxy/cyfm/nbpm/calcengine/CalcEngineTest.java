package com.ppcxy.cyfm.nbpm.calcengine;


import org.junit.Assert;
import org.junit.Test;
import org.springframework.test.context.ContextConfiguration;
import org.springside.modules.test.spring.SpringContextTestCase;

import java.util.List;


@ContextConfiguration(locations = {"/applicationContext.xml", "/applicationContext-cyfm.xml"})
public class CalcEngineTest extends SpringContextTestCase {
    
    
    @Test
    public void evalSqlTest() {
        
        List result = CalcEngine.newInstence().eval("executeSql('select * from cy_sys_user')");
        
        Assert.assertTrue(result.size() > 0);
        
    }
}
