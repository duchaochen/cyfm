package com.ppcxy.common.studenttest.service;

import com.ppcxy.common.service.StudentServiceTest;
import com.ppcxy.common.student.service.StudentService2;
import org.junit.Before;
import org.springframework.beans.factory.annotation.Autowired;

/**
 */
public class StudentServiceWithRespository2ImplTest extends StudentServiceTest {
    
    
    @Autowired
    private StudentService2 studentService2;
    
    @Before
    public void setUp() {
        studentService = studentService2;
    }
    
    
}
