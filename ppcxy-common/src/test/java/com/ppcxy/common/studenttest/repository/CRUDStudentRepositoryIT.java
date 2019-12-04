package com.ppcxy.common.studenttest.repository;

import com.ppcxy.common.base.BaseStudentIT;
import com.ppcxy.common.student.entity.SchoolType;
import com.ppcxy.common.student.entity.Student;
import com.ppcxy.common.student.repository.StudentRepository;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import static org.junit.Assert.assertNotNull;

/**
 * <p>Student Repository集成测试</p>
 * <p>测试时使用内嵌的HSQL内存数据库完成</p>
 */

public class CRUDStudentRepositoryIT extends BaseStudentIT {
    
    @PersistenceContext
    private EntityManager entityManager;
    
    @Autowired
    private StudentRepository studentRepository;
    
    private Student student;
    
    @Before
    public void setUp() {
        student = createStudent();
    }
    
    @After
    public void tearDown() {
        student = null;
    }
    
    @Test
    public void testSave() {
        Student dbStudent = studentRepository.save(student);
        assertNotNull(dbStudent.getId());
    }
    
    @Test
    public void testUpdate() {
        Student dbStudent = studentRepository.save(student);
        clear();
        
        String newStudentName = "zhang$$$$" + System.currentTimeMillis();
        dbStudent.setStudentName(newStudentName);
        studentRepository.save(dbStudent);
        
        clear();
        
        Assert.assertEquals(newStudentName, studentRepository.findOne(dbStudent.getId()).getStudentName());
    }
    
    
    @Test
    public void testUpdateRealname() {
        String realname = "lisi";
        Student dbStudent = studentRepository.save(student);
        studentRepository.updateRealname(realname, dbStudent.getId());
        
        studentRepository.flush();
        entityManager.clear();
        
        dbStudent = studentRepository.findOne(dbStudent.getId());
        Assert.assertEquals(realname, dbStudent.getBaseInfo().getRealname());
    }
    
    @Test
    public void testUpdateRealnameWithNamedParam() {
        String realname = "lisi";
        Student dbStudent = studentRepository.save(student);
        studentRepository.updateRealnameWithNamedParam(realname, dbStudent.getId());
        
        studentRepository.flush();
        entityManager.clear();
        
        dbStudent = studentRepository.findOne(dbStudent.getId());
        Assert.assertEquals(realname, dbStudent.getBaseInfo().getRealname());
    }
    
    @Test
    public void testDeleteBystudentName() {
        Student dbStudent = studentRepository.save(student);
        studentRepository.deleteBaseInfoByStudent(dbStudent.getId());
        
        studentRepository.flush();
        entityManager.clear();
        
        dbStudent = studentRepository.findOne(dbStudent.getId());
        Assert.assertNull(dbStudent.getBaseInfo());
    }
    
    @Test
    public void testFindBystudentName() {
        studentRepository.save(student);
        Student dbStudent = studentRepository.findByStudentName(student.getStudentName());
        
        assertNotNull(dbStudent);
    }
    
    
    @Test
    public void testFindByBaseInfoSex() {
        studentRepository.save(student);
        Student dbStudent = studentRepository.findByBaseInfoSex(student.getBaseInfo().getSex());
        assertNotNull(dbStudent);
    }
    
    @Test
    public void testFindByBaseInfoSexAndShcoolInfoType() {
        studentRepository.save(student);
        Student dbStudent = studentRepository.findByBaseInfoSexAndShcoolInfoSetType(
                student.getBaseInfo().getSex(),
                SchoolType.secondary_school);
        
        assertNotNull(dbStudent);
    }
}
