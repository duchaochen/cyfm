package com.ppcxy.common.studenttest.repository;

import com.ppcxy.common.base.BaseStudentIT;
import com.ppcxy.common.student.entity.Student;
import com.ppcxy.common.student.repository.StudentRepository;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.domain.Specification;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import static org.junit.Assert.assertNotNull;

/**
 * <p>测试DDD Specification，Repository必须继承JpaSpecificationExecutor</p>
 */
public class SpecificationStudentRepositoryIT extends BaseStudentIT {
    
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
    public void test() {
        
        studentRepository.save(student);
        
        Specification<Student> spec = new Specification<Student>() {
            @Override
            public Predicate toPredicate(Root<Student> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
                return cb.equal(root.get("id"), student.getId());
            }
        };
        
        clear();
        
        Student dbStudent = studentRepository.findOne(spec);
        assertNotNull(dbStudent);
        
    }
    
}
