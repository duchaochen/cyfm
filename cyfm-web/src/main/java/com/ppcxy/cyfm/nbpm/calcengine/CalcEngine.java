package com.ppcxy.cyfm.nbpm.calcengine;

import com.greenpineyu.fel.FelEngine;
import com.greenpineyu.fel.FelEngineImpl;
import com.greenpineyu.fel.context.FelContext;
import com.ppcxy.common.spring.SpringContextHolder;
import com.ppcxy.cyfm.nbpm.calcengine.funs.FunctionExt;
import com.ppcxy.cyfm.nbpm.calcengine.funs.FunctionExtBox;
import com.ppcxy.cyfm.nbpm.calcengine.funs.impl.SqlExecuteFun;

import java.util.Map;

public class CalcEngine {
    
    private static FunctionExtBox functionExtBox = null;
    
    private FelEngine fel = new FelEngineImpl();
    
    static {
        functionExtBox = SpringContextHolder.getBean(FunctionExtBox.class);
    }
    
    
    private CalcEngine() {
    }
    
    
    public static CalcEngine newInstence() {
        CalcEngine calcEngine = new CalcEngine();
        
        for (FunctionExt functionExt : functionExtBox.getFunctionExts()) {
            calcEngine.fel.addFun(new SqlExecuteFun());
        }
        
        return calcEngine;
    }
    
    public static CalcEngine newInstence(Map<String, Object> initContext) {
        CalcEngine calcEngine = newInstence();
        FelContext ctx = calcEngine.fel.getContext();
        
        initContext.forEach((k, v) -> {
            ctx.set(k, v);
        });
        
        return calcEngine;
    }
    
    
    public <T> T eval(String formula) {
        return (T) fel.eval(formula);
    }
    
}
