package com.ppcxy.cyfm.sys.repository.mybatis;

import com.ppcxy.common.repository.mybatis.MyBatisRepository;
import com.ppcxy.cyfm.sys.entity.TestModel;

import java.util.List;

@MyBatisRepository
public interface TestMybatisDao {
    
    List<TestModel> getTest();
}
