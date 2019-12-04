package com.ppcxy.common.student.entity;

import com.ppcxy.common.entity.IdEntity;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import java.util.Date;

/**
 * <p>用户基本信息</p>
 */
@Entity
@Table(name = "student_baseinfo")
@SequenceGenerator(name = "seq", sequenceName = "BaseInfo_SEQ")
public class BaseInfo extends IdEntity {
    
    private Student student;
    /**
     * 真实名称
     */
    private String realname;
    /**
     * 性别
     */
    private Sex sex;

    private Date birthday;

    private int age;

    @OneToOne()
    @JoinColumn(name = "student_id", unique = true, nullable = false, updatable = false)
    public Student getStudent() {
        return student;
    }
    
    public void setStudent(Student student) {
        this.student = student;
    }

    @Column(name = "realname", length = 100)
    public String getRealname() {
        return realname;
    }

    public void setRealname(String realname) {
        this.realname = realname;
    }

    @Column(name = "sex", length = 2)
    @Enumerated(EnumType.ORDINAL)
    public Sex getSex() {
        return sex;
    }

    public void setSex(Sex sex) {
        this.sex = sex;
    }

    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @Column(name = "birthday")
    public Date getBirthday() {
        return birthday;
    }

    public void setBirthday(Date birthday) {
        this.birthday = birthday;
    }

    @Column(name = "age")
    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }
}
