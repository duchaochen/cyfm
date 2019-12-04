package com.ppcxy.common.student.entity;

import com.ppcxy.common.entity.IdEntity;

import javax.persistence.*;

/**
 * <p>学校信息</p>
 */
@Entity
@Table(name = "student_schoolinfo")
@SequenceGenerator(name="seq", sequenceName="SchoolInfo_SEQ")
public class SchoolInfo extends IdEntity {
    
    
    private Student student;
    /**
     * 学校名称
     */

    private String name;


    private SchoolType type;

    @ManyToOne()
    @JoinColumn(name = "student_id", nullable = false)
    public Student getStudent() {
        return student;
    }
    
    public void setStudent(Student student) {
        this.student = student;
    }
    @Column(name = "name", length = 200)
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
    @Column(name = "type", length = 2)
    @Enumerated(EnumType.ORDINAL)
    public SchoolType getType() {
        return type;
    }

    public void setType(SchoolType type) {
        this.type = type;
    }
}
