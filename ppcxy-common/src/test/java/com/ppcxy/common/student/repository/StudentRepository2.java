package com.ppcxy.common.student.repository;

import com.ppcxy.common.entity.search.Searchable;
import com.ppcxy.common.repository.jpa.BaseRepository;
import com.ppcxy.common.student.entity.BaseInfo;
import com.ppcxy.common.student.entity.SchoolInfo;
import com.ppcxy.common.student.entity.Student;
import org.springframework.data.domain.Page;

import java.util.List;


/**
 * <p>用户仓库</p>
 */
public interface StudentRepository2 extends BaseRepository<Student, Long> {


    ////////////////////////////////////////////////////
    /////////以下实现都委托给StudentRepository2Impl///////
    ////////////////////////////////////////////////////
    
    BaseInfo findBaseInfoByStudentId(Long studentId);
    
    public List<SchoolInfo> findAllSchoolTypeByStudentId(Long studentId);
    
    public Page<Student> findAllByDefault(final Searchable searchable);

    public long countAllByDefault(final Searchable searchable);

    public long countAllByCustom(final Searchable searchable);
    
    public Page<Student> findAllByCustom(final Searchable searchable);

}
