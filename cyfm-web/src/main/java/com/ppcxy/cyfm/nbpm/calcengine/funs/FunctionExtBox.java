package com.ppcxy.cyfm.nbpm.calcengine.funs;

import com.google.common.collect.Sets;

import java.util.Set;

public class FunctionExtBox {
    private Set<FunctionExt> functionExts = Sets.newHashSet();
    
    
    public FunctionExtBox() {
    }
    
    public Set<FunctionExt> getFunctionExts() {
        return functionExts;
    }
    
    public void setFunctionExts(Set<FunctionExt> functionExts) {
        this.functionExts = functionExts;
    }
}
