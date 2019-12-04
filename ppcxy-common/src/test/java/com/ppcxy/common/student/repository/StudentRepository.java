package com.ppcxy.common.student.repository;

import com.ppcxy.common.repository.jpa.BaseRepository;
import com.ppcxy.common.student.entity.SchoolType;
import com.ppcxy.common.student.entity.Sex;
import com.ppcxy.common.student.entity.Student;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;


/**
 * <p>用户仓库</p>
 */
public interface StudentRepository extends BaseRepository<Student, Long>, JpaSpecificationExecutor<Student> {

    @Modifying
    @Query("update BaseInfo bi set bi.realname=?1 where bi.student.id=?2")
    void updateRealname(String realname, Long studentId);

    @Modifying
    @Query("update BaseInfo bi set bi.realname=:realname where bi.student.id=:studentId")
    void updateRealnameWithNamedParam(
            @Param("realname") String realname, @Param("studentId") Long studentId);

    @Modifying
    @Query("delete from BaseInfo bi where bi.student.id=?1")
    void deleteBaseInfoByStudent(Long studentId);

    /**
     * 条件查询 自动生成
     *
     * @param studentName
     * @return
     */
    Student findByStudentName(String studentName);


    /**
     * 关联查询 自动生成
     *
     * @param sex
     * @return
     */
    Student findByBaseInfoSex(Sex sex);
    
    Page<Student> findByBaseInfoSex(Sex sex, Pageable pageable);
    
    List<Student> findByBaseInfoSex(Sex sex, Sort sort);

    /**
     * 关联查询 和 and逻辑查询
     *
     * @param sex
     * @param type
     * @return
     */
    @Query("select u from Student u, BaseInfo bi, SchoolInfo si where bi.student=u and si.student=u and bi.sex=?1 and si.type=?2")
    Student findByBaseInfoSexAndShcoolInfoSetType(Sex sex, SchoolType type);

}
