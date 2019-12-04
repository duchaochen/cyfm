package com.ppcxy.common.studenttest.repository;

import com.ppcxy.common.base.BaseStudentIT;
import com.ppcxy.common.entity.search.Searchable;
import com.ppcxy.common.student.entity.Student;
import com.ppcxy.common.student.repository.StudentRepository2;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.HashMap;
import java.util.Map;

import static org.junit.Assert.assertEquals;

/**
 * <p>Student Repository集成测试</p>
 * <p>测试时使用内嵌的HSQL内存数据库完成</p>
 */

public class StudentRepositoryImplForCustomSearchIT extends BaseStudentIT {

    @Autowired
    private StudentRepository2 studentRepository2;


    @Test
    public void testFindAllByCustomSearch1() {
        int count = 15;
        for (int i = 0; i < count; i++) {
            Student student = createStudent();
            student.getBaseInfo().setRealname("zhang" + i);
            studentRepository2.save(student);
        }
        Searchable search = Searchable.newSearchable().addSearchParam("realname_custom", "zhang");
        assertEquals(count, studentRepository2.findAllByCustom(search).getNumberOfElements());
    }


    @Test
    public void testFindAllByPageAndCustomSearch2() {
        int count = 15;
        for (int i = 0; i < count; i++) {
            Student student = createStudent();
            student.getBaseInfo().setRealname("zhang" + i);
            studentRepository2.save(student);
        }
        Map<String, Object> searchParams = new HashMap<String, Object>();
        searchParams.put("realname_custom", "zhang");
        Searchable search = Searchable.newSearchable(searchParams).setPage(0, 5);
        assertEquals(5, studentRepository2.findAllByCustom(search).getSize());
    }

    @Test
    public void testCountAllByCustomSearch1() {
        int count = 15;
        for (int i = 0; i < count; i++) {
            Student student = createStudent();
            student.getBaseInfo().setRealname("zhang" + i);
            studentRepository2.save(student);
        }
        Searchable search = Searchable.newSearchable().addSearchParam("realname", "zhang1");
        assertEquals(6, studentRepository2.countAllByCustom(search));
    }

    @Test
    public void testCountAllByCustomSearch2() {
        int count = 15;
        for (int i = 0; i < count; i++) {
            Student student = createStudent();
            student.getBaseInfo().setRealname("zhang" + i);
            studentRepository2.save(student);
        }
        Searchable search = Searchable.newSearchable().addSearchParam("realname", "zhanga");
        assertEquals(0, studentRepository2.countAllByCustom(search));
    }


}
