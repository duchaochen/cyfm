package com.ppcxy.common.student.service;

import com.ppcxy.common.repository.jpa.BaseRepository;
import com.ppcxy.common.student.entity.Student;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.DependsOn;
import org.springframework.stereotype.Service;

/**
 */
@DependsOn("studentRepository2")
@Service()
public class StudentService2 extends StudentService {

    @Autowired
    @Qualifier("studentRepository2")
    @Override
    public void setBaseRepository(BaseRepository<Student, Long> baseRepository) {
        super.setBaseRepository(baseRepository);
    }
}
