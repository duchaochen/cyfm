package com.ppcxy.common.student.service;

import com.ppcxy.common.repository.jpa.BaseRepository;
import com.ppcxy.common.service.BaseService;
import com.ppcxy.common.student.entity.Student;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.DependsOn;
import org.springframework.stereotype.Service;

/**
 */
@DependsOn("studentRepository")
@Service()
public class StudentService extends BaseService<Student, Long> {

    @Autowired
    @Qualifier("studentRepository")
    @Override
    public void setBaseRepository(BaseRepository<Student, Long> baseRepository) {
        super.setBaseRepository(baseRepository);
    }
}
