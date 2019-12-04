package com.ppcxy.common.studenttest.repository;

import com.ppcxy.common.base.BaseStudentIT;
import com.ppcxy.common.entity.search.SearchRequest;
import com.ppcxy.common.entity.search.Searchable;
import com.ppcxy.common.student.entity.Sex;
import com.ppcxy.common.student.entity.Student;
import com.ppcxy.common.student.repository.StudentRepository2;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.Assert.assertEquals;

/**
 * <p>Student Repository集成测试</p>
 * <p>测试时使用内嵌的HSQL内存数据库完成</p>
 */

public class StudentRepositoryImplForDefaultSearchIT extends BaseStudentIT {

    @Autowired
    private StudentRepository2 studentRepository2;

    @Test
    public void testEqForEnum() {
        int count = 15;
        for (int i = 0; i < count; i++) {
            Student student = createStudent();
            student.getBaseInfo().setSex(Sex.male);
            studentRepository2.save(student);
        }
        Searchable search = Searchable.newSearchable().addSearchParam("baseInfo.sex_eq", "male");
        assertEquals(count, studentRepository2.countAllByDefault(search));
    }

    @Test
    public void testNotEqForEnum() {
        int count = 15;
        for (int i = 0; i < count; i++) {
            Student student = createStudent();
            student.getBaseInfo().setSex(Sex.male);
            studentRepository2.save(student);
        }
        Searchable search = Searchable.newSearchable().addSearchParam("baseInfo.sex_ne", "male");
        assertEquals(0, studentRepository2.countAllByDefault(search));
    }

    @Test
    public void testEqForInteger() {
        int count = 15;
        for (int i = 0; i < count; i++) {
            Student student = createStudent();
            student.getBaseInfo().setAge(15);
            studentRepository2.save(student);
        }
        Map<String, Object> searchParams = new HashMap<String, Object>();
        searchParams.put("baseInfo.age_eq", "15");
        SearchRequest search = new SearchRequest(searchParams);
        assertEquals(count, studentRepository2.countAllByDefault(search));
    }


    @Test
    public void testEqForDate() throws ParseException {
        int count = 15;
        String dateStr = "2012-01-15 16:59:00";
        DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        for (int i = 0; i < count; i++) {
            Student student = createStudent();
            student.setRegisterDate(df.parse(dateStr));
            studentRepository2.save(student);
        }

        Searchable search = Searchable.newSearchable().addSearchParam("registerDate_eq", dateStr);
        assertEquals(count, studentRepository2.countAllByDefault(search));
    }


    @Test
    public void testLtAndGtForDate() throws ParseException {
        int count = 15;
        String dateStr = "2012-01-15 16:59:00";
        String dateStrFrom = "2012-01-15 16:58:00";
        String dateStrEnd = "2012-01-15 16:59:01";
        DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        for (int i = 0; i < count; i++) {
            Student student = createStudent();
            student.getBaseInfo().setBirthday(new Timestamp(df.parse(dateStr).getTime()));
            studentRepository2.save(student);
        }
        Map<String, Object> searchParams = new HashMap<String, Object>();
        searchParams.put("baseInfo.birthday_gt", dateStrFrom);
        searchParams.put("baseInfo.birthday_lt", dateStrEnd);
        Searchable search = Searchable.newSearchable(searchParams);
        assertEquals(count, studentRepository2.countAllByDefault(search));
    }


    @Test
    public void testLteAndGteForDate() throws ParseException {
        int count = 15;
        String dateStr = "2012-01-15 16:59:00";
        String dateStrFrom = "2012-01-15 16:59:00";
        String dateStrEnd = "2012-01-15 16:59:00";
        DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        for (int i = 0; i < count; i++) {
            Student student = createStudent();
            student.getBaseInfo().setBirthday(new Timestamp(df.parse(dateStr).getTime()));
            studentRepository2.save(student);
        }
        Map<String, Object> searchParams = new HashMap<String, Object>();
        searchParams.put("baseInfo.birthday_gte", dateStrFrom);
        searchParams.put("baseInfo.birthday_lte", dateStrEnd);
        Searchable search = Searchable.newSearchable(searchParams);
        assertEquals(count, studentRepository2.countAllByDefault(search));
    }


    @Test
    public void testIsNotNull() throws ParseException {
        int count = 15;
        for (int i = 0; i < count; i++) {
            Student student = createStudent();
            studentRepository2.save(student);
        }
        Map<String, Object> searchParams = new HashMap<String, Object>();
        searchParams.put("studentName_isNotNull", null);
        Searchable search = Searchable.newSearchable(searchParams);
        assertEquals(count, studentRepository2.countAllByDefault(search));
    }

    @Test
    public void testInWithList() throws ParseException {
        List<Long> uuids = new ArrayList<Long>();
        int count = 15;
        for (int i = 0; i < count; i++) {
            Student student = createStudent();
            studentRepository2.save(student);
            uuids.add(student.getId());
        }
        Map<String, Object> searchParams = new HashMap<String, Object>();
        searchParams.put("id_in", uuids);
        Searchable search = Searchable.newSearchable(searchParams);
        assertEquals(count, studentRepository2.countAllByDefault(search));
    }


    @Test
    public void testInWithArray() throws ParseException {
        List<Long> uuids = new ArrayList<Long>();
        int count = 15;
        for (int i = 0; i < count; i++) {
            Student student = createStudent();
            studentRepository2.save(student);
            uuids.add(student.getId());
        }
        Map<String, Object> searchParams = new HashMap<String, Object>();
        searchParams.put("id_in", uuids.toArray(new Long[count]));
        Searchable search = Searchable.newSearchable(searchParams);
        assertEquals(count, studentRepository2.countAllByDefault(search));
    }

    @Test
    public void testInWithSingleValue() throws ParseException {
        List<Long> uuids = new ArrayList<Long>();
        int count = 15;
        for (int i = 0; i < count; i++) {
            Student student = createStudent();
            studentRepository2.save(student);
            uuids.add(student.getId());
        }
        Map<String, Object> searchParams = new HashMap<String, Object>();
        searchParams.put("id_in", uuids.get(0));
        Searchable search = Searchable.newSearchable(searchParams);
        assertEquals(1, studentRepository2.countAllByDefault(search));
    }


}
