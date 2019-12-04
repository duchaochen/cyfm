package com.ppcxy.common.base;

import com.ppcxy.common.student.entity.*;
import org.apache.commons.lang3.RandomStringUtils;

import java.sql.Timestamp;
import java.util.Date;

/**
 */
public abstract class BaseStudentIT extends BaseIT {
    
    public Student createStudent() {
        Student student = new Student();
        student.setStudentName("zhangsan$$$" + System.nanoTime() + RandomStringUtils.random(10));
        student.setPassword("123456");
        student.setRegisterDate(new Date());
        BaseInfo baseInfo = new BaseInfo();
        baseInfo.setRealname("zhangsan$$$");
        baseInfo.setSex(Sex.male);
        baseInfo.setBirthday(new Timestamp(System.currentTimeMillis()));
        baseInfo.setAge(15);
        student.setBaseInfo(baseInfo);

        SchoolInfo primary = new SchoolInfo();
        primary.setName("abc");
        primary.setType(SchoolType.primary_school);
        student.addSchoolInfo(primary);

        SchoolInfo secondary = new SchoolInfo();
        secondary.setName("bcd");
        secondary.setType(SchoolType.secondary_school);
        student.addSchoolInfo(secondary);

        SchoolInfo high = new SchoolInfo();
        high.setName("cde");
        high.setType(SchoolType.high_school);
        student.addSchoolInfo(high);

        SchoolInfo university = new SchoolInfo();
        university.setName("def");
        university.setType(SchoolType.university);
        student.addSchoolInfo(university);
        
        return student;
    }


}
