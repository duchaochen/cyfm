package com.ppcxy.common.studenttest.repository;

import com.google.common.collect.Lists;
import com.ppcxy.common.base.BaseStudentIT;
import com.ppcxy.common.repository.jpa.RepositoryHelper;
import com.ppcxy.common.student.entity.BaseInfo;
import com.ppcxy.common.student.entity.SchoolInfo;
import com.ppcxy.common.student.entity.Student;
import com.ppcxy.common.student.repository.StudentRepository2;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Date;
import java.util.List;

import static org.junit.Assert.assertNotNull;

/**
 * <p>Student Repository集成测试</p>
 * <p>测试时使用内嵌的HSQL内存数据库完成</p>
 */

public class StudentRepositoryImplIT extends BaseStudentIT {
    
    @Autowired
    private StudentRepository2 studentRepository2;
    
    private Student student;
    
    private RepositoryHelper repositoryHelper = new RepositoryHelper(Student.class);
    
    
    @Before
    public void setUp() {
        student = createStudent();
    }
    
    @After
    public void tearDown() {
        student = null;
    }
    
    @Test
    public void testFindBaseInfoByStudentId() {
        studentRepository2.save(student);
        
        clear();
        
        BaseInfo baseInfo = studentRepository2.findBaseInfoByStudentId(student.getId());
        assertNotNull(baseInfo);
    }
    
    
    @Test
    public void findAllSchoolTypeByStudentId() {
        studentRepository2.save(student);
        
        clear();
        
        List<SchoolInfo> schoolInfoList = studentRepository2.findAllSchoolTypeByStudentId(student.getId());
        Assert.assertEquals(student.getSchoolInfoSet().size(), schoolInfoList.size());
    }
    
    
    @Test
    public void testFindAll() {
        int count = 15;
        List<Long> ids = Lists.newArrayList();
        List<Date> birthdayList = Lists.newArrayList();
        String realnamePrefix = "zhang";
        for (int i = 0; i < count; i++) {
            Student student = createStudent();
            student.getBaseInfo().setRealname(realnamePrefix + i);
            studentRepository2.save(student);
            ids.add(student.getId());
            birthdayList.add(student.getBaseInfo().getBirthday());
        }
        
        String ql = "from Student u where u.id in(?1) and u.baseInfo.realname like ?2 and u.baseInfo.birthday in (?3)";
        Assert.assertEquals(count, repositoryHelper.findAll(ql, ids, realnamePrefix + "%", birthdayList).size());
    }
    
    @Test
    public void testCountAll() {
        int count = 15;
        List<Long> ids = Lists.newArrayList();
        List<Date> birthdayList = Lists.newArrayList();
        String realnamePrefix = "zhang";
        for (int i = 0; i < count; i++) {
            Student student = createStudent();
            student.getBaseInfo().setRealname(realnamePrefix + i);
            studentRepository2.save(student);
            ids.add(student.getId());
            birthdayList.add(student.getBaseInfo().getBirthday());
        }
        
        String ql = "select count(o) from Student o where o.id in(?1) and o.baseInfo.realname like ?2 and o.baseInfo.birthday in (?3)";
        Assert.assertEquals(count, repositoryHelper.count(ql, ids, realnamePrefix + "%", birthdayList));
    }
    
    @Test
    public void testFindOne() {
        int count = 15;
        Student lastStudent = null;
        String realnamePrefix = "zhang";
        for (int i = 0; i < count; i++) {
            Student student = createStudent();
            student.getBaseInfo().setRealname(realnamePrefix + i);
            lastStudent = studentRepository2.save(student);
        }
        String ql = "select u from Student u where u=?1 and u.baseInfo.realname like ?2";
        Assert.assertEquals(lastStudent, repositoryHelper.findOne(ql, lastStudent, realnamePrefix + "%"));
    }
    
    @Test
    public void testBatchUpdate() {
        int count = 15;
        String realname = "123321";
        Student lastStudent = null;
        for (int i = 0; i < count; i++) {
            Student student = createStudent();
            student.getBaseInfo().setRealname(realname);
            lastStudent = studentRepository2.save(student);
        }
        
        String ql = "update BaseInfo set realname=?1";
        Assert.assertEquals(count, repositoryHelper.batchUpdate(ql, realname));
        
        String findOneQL = "select u from Student u where u=?1";
        Student student = repositoryHelper.findOne(findOneQL, lastStudent);
        Assert.assertEquals(realname, student.getBaseInfo().getRealname());
        
    }
    
}
