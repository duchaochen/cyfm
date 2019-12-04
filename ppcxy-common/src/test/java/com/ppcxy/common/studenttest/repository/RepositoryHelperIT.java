package com.ppcxy.common.studenttest.repository;

import com.ppcxy.common.base.BaseStudentIT;
import com.ppcxy.common.entity.search.Searchable;
import com.ppcxy.common.repository.jpa.RepositoryHelper;
import com.ppcxy.common.repository.jpa.support.callback.DefaultSearchCallback;
import com.ppcxy.common.repository.jpa.support.callback.SearchCallback;
import com.ppcxy.common.student.entity.Sex;
import com.ppcxy.common.student.entity.Student;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;

import javax.persistence.EntityManagerFactory;
import javax.persistence.PersistenceUnit;
import javax.persistence.Query;
import java.util.List;

/**
 */
public class RepositoryHelperIT extends BaseStudentIT {
    
    @PersistenceUnit
    private EntityManagerFactory entityManagerFactory;
    
    private RepositoryHelper repositoryHelper;
    
    @Before
    public void setUp() {
        RepositoryHelper.setEntityManagerFactory(entityManagerFactory);
        repositoryHelper = new RepositoryHelper(Student.class);
    }
    
    
    @Test
    public void testGetEntityManager() {
        Assert.assertNotNull(repositoryHelper.getEntityManager());
    }
    
    @Test
    public void testCount() {
        String ql = "select count(o) from Student o";
        long expectedCount = repositoryHelper.count(ql) + 1;
        
        Student student = createStudent();
        repositoryHelper.getEntityManager().persist(student);
        
        Assert.assertEquals(expectedCount, repositoryHelper.count(ql));
        
    }
    
    
    @Test
    public void testCountWithCondition() {
        
        Student student = createStudent();
        repositoryHelper.getEntityManager().persist(student);
        
        String ql = "select count(o) from Student o where id >= ? and id <=?";
        Assert.assertEquals(1, repositoryHelper.count(ql, student.getId(), student.getId()));
        Assert.assertEquals(0, repositoryHelper.count(ql, student.getId(), 0L));
    }
    
    @Test
    public void testFindAll() {
        String ql = "select o from Student o";
        
        List<Student> before = repositoryHelper.findAll(ql);
        Student student1 = createStudent();
        Student student2 = createStudent();
        repositoryHelper.getEntityManager().persist(student1);
        repositoryHelper.getEntityManager().persist(student2);
        
        List<Student> after = repositoryHelper.findAll(ql);
        
        Assert.assertEquals(before.size() + 2, after.size());
        
        Assert.assertTrue(after.contains(student1));
        
    }
    
    @Test
    public void testFindAllWithCondition() {
        String ql = "select o from Student o where id>=? and id<=?";
        
        List<Student> before = repositoryHelper.findAll(ql, 0L, Long.MAX_VALUE);
        Student student1 = createStudent();
        Student student2 = createStudent();
        Student student3 = createStudent();
        Student student4 = createStudent();
        repositoryHelper.getEntityManager().persist(student1);
        repositoryHelper.getEntityManager().persist(student2);
        repositoryHelper.getEntityManager().persist(student3);
        repositoryHelper.getEntityManager().persist(student4);
        
        List<Student> after = repositoryHelper.findAll(ql, 0L, student2.getId());
        
        Assert.assertEquals(before.size() + 2, after.size());
        
        Assert.assertTrue(after.contains(student1));
        Assert.assertTrue(after.contains(student2));
        
        Assert.assertFalse(after.contains(student3));
        Assert.assertFalse(after.contains(student4));
    }
    
    
    @Test
    public void testFindAllWithPage() {
        
        repositoryHelper.batchUpdate("delete from Student");
        
        Student student1 = createStudent();
        Student student2 = createStudent();
        Student student3 = createStudent();
        Student studnet4 = createStudent();
        repositoryHelper.getEntityManager().persist(student1);
        repositoryHelper.getEntityManager().persist(student2);
        repositoryHelper.getEntityManager().persist(student3);
        repositoryHelper.getEntityManager().persist(studnet4);
        
        String ql = "select o from Student o";
        
        Assert.assertEquals(4, repositoryHelper.findAll(ql).size());
        
        List<Student> list = repositoryHelper.findAll(ql, new PageRequest(0, 2));
        Assert.assertEquals(2, list.size());
        Assert.assertTrue(list.contains(student1));
    }
    
    
    @Test
    public void testFindAllWithSort() {
        
        repositoryHelper.batchUpdate("delete from Student");
        
        Student student1 = createStudent();
        Student student2 = createStudent();
        Student student3 = createStudent();
        Student student4 = createStudent();
        repositoryHelper.getEntityManager().persist(student1);
        repositoryHelper.getEntityManager().persist(student2);
        repositoryHelper.getEntityManager().persist(student3);
        repositoryHelper.getEntityManager().persist(student4);
        
        String ql = "select o from Student o";
        
        
        List<Student> list = repositoryHelper.findAll(ql, new Sort(Sort.Direction.DESC, "id"));
        
        Assert.assertEquals(4, list.size());
        
        Assert.assertTrue(list.get(0).equals(student4));
    }
    
    
    @Test
    public void testFindAllWithPageAndSort() {
        
        repositoryHelper.batchUpdate("delete from Student");
        
        Student student1 = createStudent();
        Student student2 = createStudent();
        Student student3 = createStudent();
        Student student4 = createStudent();
        repositoryHelper.getEntityManager().persist(student1);
        repositoryHelper.getEntityManager().persist(student2);
        repositoryHelper.getEntityManager().persist(student3);
        repositoryHelper.getEntityManager().persist(student4);
        
        String ql = "select o from Student o";
        
        
        List<Student> list = repositoryHelper.findAll(ql, new PageRequest(0, 2, new Sort(Sort.Direction.DESC, "id")));
        
        Assert.assertEquals(2, list.size());
        
        Assert.assertTrue(list.get(0).equals(student4));
        Assert.assertTrue(list.contains(student3));
        
        Assert.assertFalse(list.contains(student1));
    }
    
    
    @Test
    public void testFindOne() {
        Student student1 = createStudent();
        Student student2 = createStudent();
        repositoryHelper.getEntityManager().persist(student1);
        repositoryHelper.getEntityManager().persist(student2);
        
        String ql = "select o from Student o where id=? and baseInfo.sex=?";
        Assert.assertNotNull(repositoryHelper.findOne(ql, student1.getId(), Sex.male));
        Assert.assertNull(repositoryHelper.findOne(ql, student1.getId(), Sex.female));
    }
    
    
    @Test
    public void testFindAllWithSearchableAndDefaultSearchCallbck() {
        
        
        Student student1 = createStudent();
        Student student2 = createStudent();
        Student student3 = createStudent();
        Student student4 = createStudent();
        repositoryHelper.getEntityManager().persist(student1);
        repositoryHelper.getEntityManager().persist(student2);
        repositoryHelper.getEntityManager().persist(student3);
        repositoryHelper.getEntityManager().persist(student4);
        
        Searchable searchable = Searchable.newSearchable();
        
        searchable.addSearchParam("id_in", new Long[]{student1.getId(), student2.getId(), student3.getId()});
        
        searchable.setPage(0, 2);
        searchable.addSort(Sort.Direction.DESC, "id");
        
        
        String ql = "from Student where 1=1";
        List<Student> list = repositoryHelper.findAll(ql, searchable, SearchCallback.DEFAULT);
        
        Assert.assertEquals(2, list.size());
        
        Assert.assertEquals(student3, list.get(0));
        
    }
    
    
    @Test
    public void testFindAllWithSearchableAndCustomSearchCallbck() {
        
        
        Student student1 = createStudent();
        Student student2 = createStudent();
        student2.getBaseInfo().setRealname("lisi");
        Student student3 = createStudent();
        Student student4 = createStudent();
        repositoryHelper.getEntityManager().persist(student1);
        repositoryHelper.getEntityManager().persist(student2);
        repositoryHelper.getEntityManager().persist(student3);
        repositoryHelper.getEntityManager().persist(student4);
        
        Searchable searchable = Searchable.newSearchable();
        
        searchable.addSearchParam("realname", "zhang");
        searchable.addSearchParam("id_lt", student4.getId());
        
        searchable.setPage(0, 2);
        searchable.addSort(Sort.Direction.DESC, "id");
        
        
        SearchCallback customCallback = new DefaultSearchCallback() {
            @Override
            public void prepareQL(StringBuilder ql, Searchable search) {
                //默认的
                super.prepareQL(ql, search);
                //自定义的
                if (search.containsSearchKey("realname")) {//此处也可以使用realname_custom
                    ql.append(" and baseInfo.realname like :realname");
                }
            }
            
            @Override
            public void setValues(Query query, Searchable search) {
                //默认的
                super.setValues(query, search);
                //自定义的
                if (search.containsSearchKey("realname")) {
                    query.setParameter("realname", "%" + search.getValue("realname") + "%");
                }
            }
        };
        
        
        String ql = "from Student where 1=1";
        List<Student> list = repositoryHelper.findAll(ql, searchable, customCallback);
        
        Assert.assertEquals(2, list.size());
        
        Assert.assertEquals(student3, list.get(0));
        Assert.assertEquals(student1, list.get(1));
        
    }
    
    
    @Test
    public void testFindAllWithSearchableAndCustomSearchCallbck2() {
        
        
        Student student1 = createStudent();
        Student student2 = createStudent();
        student2.getBaseInfo().setRealname("lisi");
        Student student3 = createStudent();
        Student student4 = createStudent();
        repositoryHelper.getEntityManager().persist(student1);
        repositoryHelper.getEntityManager().persist(student2);
        repositoryHelper.getEntityManager().persist(student3);
        repositoryHelper.getEntityManager().persist(student4);
        
        Searchable searchable = Searchable.newSearchable();
        
        searchable.addSearchParam("realname", "zhang");
        searchable.addSearchParam("id_lt", student4.getId());
        
        searchable.setPage(0, 2);
        searchable.addSort(Sort.Direction.DESC, "id");
        
        
        SearchCallback customCallback = new DefaultSearchCallback() {
            @Override
            public void prepareQL(StringBuilder ql, Searchable search) {
                //不调用默认的
                if (search.containsSearchKey("id_lt")) {
                    ql.append(" and id < :id");
                }
                //自定义的
                if (search.containsSearchKey("realname_custom")) {//此处也可以使用realname_custom
                    ql.append(" and baseInfo.realname like :realname");
                }
            }
            
            @Override
            public void setValues(Query query, Searchable search) {
                //不调用默认的
                if (search.containsSearchKey("id_lt")) {
                    query.setParameter("id", search.getValue("id_lt"));
                }
                //自定义的
                if (search.containsSearchKey("realname")) {
                    query.setParameter("realname", "%" + search.getValue("realname") + "%");
                }
            }
        };
        
        
        String ql = "from Student where 1=1";
        List<Student> list = repositoryHelper.findAll(ql, searchable, customCallback);
        
        Assert.assertEquals(2, list.size());
        
        Assert.assertEquals(student3, list.get(0));
        Assert.assertEquals(student1, list.get(1));
        
    }
    
    
    @Test
    public void testCountWithSearchableAndDefaultSearchCallbck() {
        
        
        Student student1 = createStudent();
        Student student2 = createStudent();
        Student student3 = createStudent();
        Student student4 = createStudent();
        repositoryHelper.getEntityManager().persist(student1);
        repositoryHelper.getEntityManager().persist(student2);
        repositoryHelper.getEntityManager().persist(student3);
        repositoryHelper.getEntityManager().persist(student4);
        
        Searchable searchable = Searchable.newSearchable();
        
        searchable.addSearchParam("id_in", new Long[]{student1.getId(), student2.getId(), student3.getId()});
        
        searchable.addSort(Sort.Direction.DESC, "id");
        
        
        String ql = "select count(*) from Student where 1=1";
        long total = repositoryHelper.count(ql, searchable, SearchCallback.DEFAULT);
        
        Assert.assertEquals(3L, total);
        
    }
    
    
    @Test
    public void testCountWithSearchableAndCustomSearchCallbck() {
        
        
        Student student1 = createStudent();
        Student student2 = createStudent();
        student2.getBaseInfo().setRealname("lisi");
        Student student3 = createStudent();
        Student student4 = createStudent();
        repositoryHelper.getEntityManager().persist(student1);
        repositoryHelper.getEntityManager().persist(student2);
        repositoryHelper.getEntityManager().persist(student3);
        repositoryHelper.getEntityManager().persist(student4);
        
        Searchable searchable = Searchable.newSearchable();
        
        searchable.addSearchParam("realname", "zhang");
        searchable.addSearchParam("id_lt", student4.getId());
        
        searchable.setPage(0, 2);
        searchable.addSort(Sort.Direction.DESC, "id");
        
        
        SearchCallback customCallback = new DefaultSearchCallback() {
            @Override
            public void prepareQL(StringBuilder ql, Searchable search) {
                //默认的
                super.prepareQL(ql, search);
                //自定义的
                if (search.containsSearchKey("realname")) {//此处也可以使用realname_custom
                    ql.append(" and baseInfo.realname like :realname");
                }
            }
            
            @Override
            public void setValues(Query query, Searchable search) {
                //默认的
                super.setValues(query, search);
                //自定义的
                if (search.containsSearchKey("realname")) {
                    query.setParameter("realname", "%" + search.getValue("realname") + "%");
                }
            }
        };
        
        
        String ql = "select count(*) from Student where 1=1";
        long total = repositoryHelper.count(ql, searchable, customCallback);
        
        Assert.assertEquals(2, total);
        
        
    }
    
    
    @Test
    public void testCountWithSearchableAndCustomSearchCallbck2() {
        
        Student student1 = createStudent();
        Student student2 = createStudent();
        student2.getBaseInfo().setRealname("lisi");
        Student student3 = createStudent();
        Student student4 = createStudent();
        repositoryHelper.getEntityManager().persist(student1);
        repositoryHelper.getEntityManager().persist(student2);
        repositoryHelper.getEntityManager().persist(student3);
        repositoryHelper.getEntityManager().persist(student4);
        
        Searchable searchable = Searchable.newSearchable();
        
        searchable.addSearchParam("realname", "zhang");
        searchable.addSearchParam("id_lt", student4.getId());
        
        searchable.setPage(0, 2);
        searchable.addSort(Sort.Direction.DESC, "id");
        
        
        SearchCallback customCallback = new DefaultSearchCallback() {
            @Override
            public void prepareQL(StringBuilder ql, Searchable search) {
                //不调用默认的
                if (search.containsSearchKey("id_lt")) {
                    ql.append(" and id < :id");
                }
                //自定义的
                if (search.containsSearchKey("realname_custom")) {//此处也可以使用realname_custom
                    ql.append(" and baseInfo.realname like :realname");
                }
            }
            
            @Override
            public void setValues(Query query, Searchable search) {
                //不调用默认的
                if (search.containsSearchKey("id_lt")) {
                    query.setParameter("id", search.getValue("id_lt"));
                }
                //自定义的
                if (search.containsSearchKey("realname")) {
                    query.setParameter("realname", "%" + search.getValue("realname") + "%");
                }
            }
        };
        
        
        String ql = "select count(*) from Student where 1=1";
        long total = repositoryHelper.count(ql, searchable, customCallback);
        Assert.assertEquals(2, total);
        
    }
    
    
    @Test
    public void testBatchUpdate() {
        Student student1 = createStudent();
        Student student2 = createStudent();
        student2.getBaseInfo().setRealname("lisi");
        Student student3 = createStudent();
        Student student4 = createStudent();
        repositoryHelper.getEntityManager().persist(student1);
        repositoryHelper.getEntityManager().persist(student2);
        repositoryHelper.getEntityManager().persist(student3);
        repositoryHelper.getEntityManager().persist(student4);
        
        String newPassword = "123321";
        
        String updateQL = "update Student set password=? where id=?";
        repositoryHelper.batchUpdate(updateQL, newPassword, student1.getId());
        
        clear();
        
        student1 = repositoryHelper.findOne("from Student where id=?", student1.getId());
        
        Assert.assertEquals(newPassword, student1.getPassword());
        
    }
    
    
}
