package com.ppcxy.common.studenttest.repository;

import com.ppcxy.common.base.BaseStudentIT;
import com.ppcxy.common.student.entity.*;
import com.ppcxy.common.student.repository.StudentRepository;
import org.apache.commons.lang3.RandomStringUtils;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;

import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

/**
 * <p>Student Repository集成测试</p>
 * <p>测试时使用内嵌的HSQL内存数据库完成</p>
 */

public class PageAndSortStudentRepositoryIT extends BaseStudentIT {

    @Autowired
    private StudentRepository studentRepository;

    @Test
    public void testFindAllForPage() {
        for (int i = 0; i < 15; i++) {
            studentRepository.save(createStudent());
        }
        PageRequest pageRequest = new PageRequest(1, 5);
        Page<Student> page = studentRepository.findAll(pageRequest);

        assertEquals(pageRequest.getPageSize(), page.getNumberOfElements());
        assertEquals(3, page.getTotalPages());
    }

    @Test
    public void testFindByBaseInfoSexForPage() {
        for (int i = 0; i < 15; i++) {
            studentRepository.save(createStudent());
        }
        PageRequest pageRequest = new PageRequest(1, 5);
        Page<Student> page = studentRepository.findByBaseInfoSex(Sex.male, pageRequest);

        assertEquals(pageRequest.getPageSize(), page.getNumberOfElements());
        assertEquals(3, page.getTotalPages());
    
    
        page = studentRepository.findByBaseInfoSex(Sex.female, pageRequest);

        assertEquals(0, page.getNumberOfElements());
        assertEquals(0, page.getTotalPages());
    }

    @Test
    public void testFindAllForSort() {
        for (int i = 0; i < 15; i++) {
            studentRepository.save(createStudent());
        }
        Sort.Order idAsc = new Sort.Order(Sort.Direction.ASC, "id");
        Sort.Order studentNameDesc = new Sort.Order(Sort.Direction.DESC, "studentName");
        Sort sort = new Sort(idAsc, studentNameDesc);
    
        List<Student> studentList = studentRepository.findAll(sort);
    
        assertTrue(studentList.get(0).getId() < studentList.get(1).getId());

    }


    @Test
    public void testFindByBaseInfoSexForSort() {
        for (int i = 0; i < 15; i++) {
            studentRepository.save(createStudent());
        }
        Sort.Order idAsc = new Sort.Order(Sort.Direction.ASC, "id");
        Sort.Order studentNameDesc = new Sort.Order(Sort.Direction.DESC, "studentName");
        Sort sort = new Sort(idAsc, studentNameDesc);
    
        List<Student> studentList = studentRepository.findByBaseInfoSex(Sex.male, sort);
    
        assertTrue(studentList.get(0).getId() < studentList.get(1).getId());

    }


    @Test
    public void testFindAllForPageAndSort() {
        for (int i = 0; i < 15; i++) {
            studentRepository.save(createStudent());
        }

        Sort.Order idAsc = new Sort.Order(Sort.Direction.ASC, "id");
        Sort.Order studentNameDesc = new Sort.Order(Sort.Direction.DESC, "studentName");
        Sort sort = new Sort(idAsc, studentNameDesc);

        PageRequest pageRequest = new PageRequest(1, 5, sort);
        Page<Student> page = studentRepository.findAll(pageRequest);

        assertEquals(pageRequest.getPageSize(), page.getNumberOfElements());
        assertEquals(3, page.getTotalPages());

        assertTrue(page.getContent().get(0).getId() < page.getContent().get(1).getId());

    }
    
    
    public Student createStudent() {
        Student student = new Student();
        student.setStudentName("zhangsan$$$" + System.nanoTime() + RandomStringUtils.random(10));
        student.setPassword("123456");
        BaseInfo baseInfo = new BaseInfo();
        baseInfo.setRealname("zhangsan$$$");
        baseInfo.setSex(Sex.male);
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
