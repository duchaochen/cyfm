package com.ppcxy.common.service;

import com.ppcxy.common.base.BaseStudentIT;
import com.ppcxy.common.entity.search.Searchable;
import com.ppcxy.common.student.entity.Student;
import com.ppcxy.common.student.service.StudentService;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.Assert.*;

/**
 */
public class StudentServiceTest extends BaseStudentIT {
    
    @Autowired
    protected StudentService studentService;
    
    @Test
    public void testSave() {
        Student dbStudent = studentService.save(createStudent());
        assertNotNull(dbStudent.getId());
    }
    
    @Test
    public void testUpdate() {
        Student dbStudent = studentService.save(createStudent());
        clear();
        
        String newStudentName = "zhang$$$$" + System.currentTimeMillis();
        dbStudent.setStudentName(newStudentName);
        studentService.update(dbStudent);
        
        clear();
        
        assertEquals(newStudentName, studentService.findOne(dbStudent.getId()).getStudentName());
    }
    
    @Test
    public void testDeleteById() {
        Student dbStudent = studentService.save(createStudent());
        clear();
        studentService.delete(dbStudent.getId());
        clear();
        
        assertNull(studentService.findOne(dbStudent.getId()));
    }
    
    @Test
    public void testDeleteByEntity() {
        Student dbStudent = studentService.save(createStudent());
        clear();
        studentService.delete(dbStudent);
        clear();
        
        assertNull(studentService.findOne(dbStudent.getId()));
    }
    
    @Test
    public void testFindOne() {
        Student dbStudent = studentService.save(createStudent());
        clear();
        
        assertNotNull(studentService.findOne(dbStudent.getId()));
    }
    
    
    @Test
    public void testExists() {
        Student dbStudent = studentService.save(createStudent());
        clear();
        assertTrue(studentService.exists(dbStudent.getId()));
    }
    
    @Test
    public void testCount() {
        int count = 15;
        for (int i = 0; i < count; i++) {
            studentService.save(createStudent());
        }
        assertEquals(count, studentService.count());
    }
    
    @Test
    public void testFindAll() {
        int count = 15;
        Student student = null;
        for (int i = 0; i < count; i++) {
            student = studentService.save(createStudent());
        }
        List<Student> studentList = studentService.findAll();
        assertEquals(count, studentList.size());
        assertTrue(studentList.contains(student));
    }
    
    
    @Test
    public void testFindAllBySort() {
        int count = 15;
        Student student = null;
        for (int i = 0; i < count; i++) {
            student = studentService.save(createStudent());
        }
        
        Sort sortDesc = new Sort(Sort.Direction.DESC, "id");
        Sort sortAsc = new Sort(Sort.Direction.ASC, "id");
        List<Student> studentDescList = studentService.findAll(sortDesc);
        List<Student> studentAscList = studentService.findAll(sortAsc);
        
        assertEquals(count, studentDescList.size());
        assertEquals(count, studentAscList.size());
        assertTrue(studentDescList.contains(student));
        assertTrue(studentAscList.contains(student));
        
        assertTrue(studentAscList.get(0).getId() < studentAscList.get(1).getId());
        assertTrue(studentDescList.get(0).getId() > studentDescList.get(1).getId());
    }
    
    
    @Test
    public void testFindAllByPageableAndSortDesc() {
        int count = 15;
        Student lastStudent = null;
        for (int i = 0; i < count; i++) {
            lastStudent = studentService.save(createStudent());
        }
        
        Sort sortDesc = new Sort(Sort.Direction.DESC, "id");
        Pageable pageable = new PageRequest(0, 5, sortDesc);
        Page<Student> studentPage = studentService.findAll(pageable);
        
        assertEquals(5, studentPage.getNumberOfElements());
        assertTrue(studentPage.getContent().contains(lastStudent));
        assertTrue(studentPage.getContent().get(0).getId() > studentPage.getContent().get(1).getId());
    }
    
    @Test
    public void testFindAllByPageableAndSortAsc() {
        int count = 15;
        Student lastStudent = null;
        for (int i = 0; i < count; i++) {
            lastStudent = studentService.save(createStudent());
        }
        
        Sort sortAsc = new Sort(Sort.Direction.ASC, "id");
        Pageable pageable = new PageRequest(0, 5, sortAsc);
        Page<Student> studentPage = studentService.findAll(pageable);
        
        assertEquals(5, studentPage.getNumberOfElements());
        assertFalse(studentPage.getContent().contains(lastStudent));
        assertTrue(studentPage.getContent().get(0).getId() < studentPage.getContent().get(1).getId());
    }
    
    
    @Test
    public void testFindAllBySearchAndNoPage() {
        int count = 15;
        Student lastStudent = null;
        for (int i = 0; i < count; i++) {
            lastStudent = createStudent();
            lastStudent.setStudentName("zhang" + i);
            studentService.save(lastStudent);
        }
        
        Map<String, Object> searchParams = new HashMap<String, Object>();
        searchParams.put("studentName_like", "zhang");
        Searchable search = Searchable.newSearchable(searchParams);
        
        List<Student> studentList = studentService.findAllWithNoPageNoSort(search);
        assertEquals(count, studentList.size());
        assertTrue(studentList.contains(lastStudent));
    }
    
    
    @Test
    public void testFindAllBySearchAndSort() {
        int count = 15;
        Student lastStudent = null;
        for (int i = 0; i < count; i++) {
            lastStudent = createStudent();
            lastStudent.setStudentName("zhang" + i);
            studentService.save(lastStudent);
        }
        
        Map<String, Object> searchParams = new HashMap<String, Object>();
        searchParams.put("studentName_like", "zhang");
        Sort sortDesc = new Sort(Sort.Direction.DESC, "id");
        Searchable search = Searchable.newSearchable(searchParams).addSort(sortDesc);
        
        List<Student> studentList = studentService.findAllWithSort(search);
        assertEquals(count, studentList.size());
        assertTrue(studentList.contains(lastStudent));
        
        assertTrue(studentList.get(0).getId() > studentList.get(1).getId());
    }
    
    @Test
    public void testFindAllBySearchAndPageableAndSortAsc() {
        int count = 15;
        Student lastStudent = null;
        for (int i = 0; i < count; i++) {
            lastStudent = studentService.save(createStudent());
        }
        
        Sort sortAsc = new Sort(Sort.Direction.ASC, "id");
        Pageable pageable = new PageRequest(0, 5, sortAsc);
        Map<String, Object> searchParams = new HashMap<String, Object>();
        searchParams.put("studentName_like", "zhang");
        Searchable search = Searchable.newSearchable(searchParams).setPage(pageable);
        
        Page<Student> studentPage = studentService.findAll(search);
        assertEquals(5, studentPage.getNumberOfElements());
        assertFalse(studentPage.getContent().contains(lastStudent));
        assertTrue(studentPage.getContent().get(0).getId() < studentPage.getContent().get(1).getId());
    }
    
    
}
