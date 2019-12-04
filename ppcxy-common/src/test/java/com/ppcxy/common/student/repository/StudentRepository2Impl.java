package com.ppcxy.common.student.repository;

import com.ppcxy.common.entity.search.Searchable;
import com.ppcxy.common.repository.jpa.RepositoryHelper;
import com.ppcxy.common.repository.jpa.support.callback.DefaultSearchCallback;
import com.ppcxy.common.repository.jpa.support.callback.SearchCallback;
import com.ppcxy.common.student.entity.BaseInfo;
import com.ppcxy.common.student.entity.SchoolInfo;
import com.ppcxy.common.student.entity.Student;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import java.util.List;

/**
 * <p>跟以前普通DAO实现一样，无需加@Repository，系统会自动扫描，并加到相关的Repository接口中，实现格式：</p>
 * <pre>
 *     JpaRepository接口+<jpa:repositories repository-impl-postfix="Impl"></jpa:repositories>中的repository-impl-postfix
 *     repository-impl-postfix默认为Impl
 * </pre>
 */
public class StudentRepository2Impl {
    
    private String findAllQL = "from Student o where 1=1 ";
    private String countAllQL = "select count(o) from Student o where 1=1 ";


    @PersistenceContext
    private EntityManager entityManager;
    
    private RepositoryHelper repositoryHelper = new RepositoryHelper(Student.class);
    private SearchCallback customSearchCallback = new DefaultSearchCallback() {
        @Override
        public void prepareQL(StringBuilder hql, Searchable search) {
            if (search.containsSearchKey("realname")) {
                hql.append(" and exists(select 1 from BaseInfo bi where o = bi.student and bi.realname like :realname )");
            }
        }
        
        @Override
        public void setValues(Query query, Searchable search) {
            if (search.containsSearchKey("realname")) {
                query.setParameter("realname", "%" + search.getValue("realname") + "%");
            }
        }
    };
    
    public BaseInfo findBaseInfoByStudentId(Long studentId) {
        String ql = "select bi from BaseInfo bi where bi.student.id=?1";
        Query query = entityManager.createQuery(ql);
        query.setParameter(1, studentId);
        query.setMaxResults(1);
        List<BaseInfo> baseInfoList = query.getResultList();
        if (baseInfoList.size() > 0) {
            return baseInfoList.get(0);
        }
        return null;
    }
    
    public List<SchoolInfo> findAllSchoolTypeByStudentId(Long studentId) {
        String ql = "select si from SchoolInfo si where si.student.id=?1";
        Query query = entityManager.createQuery(ql);
        query.setParameter(1, studentId);
        return query.getResultList();
    }
    
    /**
     * 按条件统计
     *
     * @param searchable
     * @return
     */
    public long countAllByDefault(final Searchable searchable) {
        return repositoryHelper.count(countAllQL, searchable, SearchCallback.DEFAULT);
    }

    /**
     * 按条件分页/排序查询，
     *
     * @param searchable
     * @return
     */
    public Page<Student> findAllByDefault(final Searchable searchable) {
        long total = countAllByDefault(searchable);
        List<Student> contentList = repositoryHelper.findAll(findAllQL, searchable, SearchCallback.DEFAULT);
        return new PageImpl(contentList, searchable.getPage(), total);
    }

    /**
     * 按条件统计
     *
     * @param searchable
     * @return
     */
    public long countAllByCustom(final Searchable searchable) {
        return repositoryHelper.count(countAllQL, searchable, customSearchCallback);
    }

    /**
     * 按条件分页/排序查询，
     *
     * @param searchable
     * @return
     */
    public Page<Student> findAllByCustom(final Searchable searchable) {
        long total = countAllByCustom(searchable);
        List<Student> contentList = repositoryHelper.findAll(findAllQL, searchable, customSearchCallback);
        return new PageImpl(contentList, searchable.getPage(), total);
    }


}
