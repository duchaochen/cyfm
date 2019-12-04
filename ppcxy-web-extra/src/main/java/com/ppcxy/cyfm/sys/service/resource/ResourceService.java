package com.ppcxy.cyfm.sys.service.resource;

import com.google.common.collect.Maps;
import com.ppcxy.common.entity.search.SearchOperator;
import com.ppcxy.common.entity.search.Searchable;
import com.ppcxy.common.extend.service.BaseTreeableService;
import com.ppcxy.cyfm.sys.entity.resource.Resource;
import com.ppcxy.cyfm.sys.entity.resource.dto.Menu;
import com.ppcxy.cyfm.sys.entity.user.User;
import com.ppcxy.cyfm.sys.service.authorize.AuthorizeService;
import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.authz.permission.WildcardPermission;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.Predicate;
import java.util.stream.Collectors;


/**
 * Created by weep on 2016-5-17.
 */
@Service
//因为使用aop做了缓存,导致无法通过注解配置事物.使用配置方式配置了事物.
@Transactional
public class ResourceService extends BaseTreeableService<Resource, Long> {
    
    @Autowired
    private AuthorizeService authorizeService;
    
    @SuppressWarnings("unchecked")
    public static Map<Long, List<Menu>> convertToMenus(List<Resource> resources) {
        
        if (resources.size() == 0) {
            return Maps.newHashMap();
        }
        
        Map<Long, List<Menu>> menus = Maps.newHashMap();
        
        List<Resource> rootResources = resources.stream().filter(new Predicate<Resource>() {
            @Override
            public boolean test(Resource resource) {
                return resource.getParentId() == 0;
            }
        }).collect(Collectors.toList());
        
        for (Resource rootResource : rootResources) {
            resources.remove(rootResource);
            
            Menu root = convertToMenu(rootResource);
            
            recursiveMenu(root, resources);
            List<Menu> menuArray = root.getChildren();
            removeNoLeafMenu(menuArray);
            menus.put(root.getId(), menuArray);
        }
        
        
        return menus;
    }
    
    private static void removeNoLeafMenu(List<Menu> menus) {
        if (menus.size() == 0) {
            return;
        }
        for (int i = menus.size() - 1; i >= 0; i--) {
            Menu m = menus.get(i);
            removeNoLeafMenu(m.getChildren());
            if (!m.isHasChildren() && StringUtils.isEmpty(m.getUrl())) {
                menus.remove(i);
            }
        }
    }
    
    private static void recursiveMenu(Menu menu, List<Resource> resources) {
        for (int i = resources.size() - 1; i >= 0; i--) {
            Resource resource = resources.get(i);
            if (resource.getParentId().equals(menu.getId())) {
                menu.getChildren().add(convertToMenu(resource));
                resources.remove(i);
            }
        }
        
        for (Menu subMenu : menu.getChildren()) {
            recursiveMenu(subMenu, resources);
        }
    }
    
    private static Menu convertToMenu(Resource resource) {
        return new Menu(resource.getId(), resource.getName(), resource.getIcon(), resource.getUrl(), resource.getResourceType());
    }
    
    @Override
    public Resource save(Resource resource) {
        return super.save(resource);
    }
    
    @Override
    public void deleteSelfAndChild(List<Resource> resources) {
        super.deleteSelfAndChild(resources);
    }
    
    @Override
    public void appendChild(Resource parent, Resource child) {
        super.appendChild(parent, child);
    }
    
    @Override
    public int nextWeight(Long aLong) {
        return super.nextWeight(aLong);
    }
    
    /**
     * 得到真实的资源标识  即 父亲:儿子
     *
     * @param resource
     * @return
     */
    public String findActualResourceIdentity(Resource resource) {
        
        if (resource == null) {
            return null;
        }
        
        StringBuilder s = new StringBuilder(resource.getIdentity());
        
        boolean hasResourceIdentity = !StringUtils.isEmpty(resource.getIdentity());
        
        //TODO 迭代父菜单拥有权限则子菜单有用权限的设定权限方式，暂时不采用
        //Resource parent = findOne(resource.getParentId());
        //while (parent != null) {
        //    if (!StringUtils.isEmpty(parent.getIdentity())) {
        //        s.insert(0, parent.getIdentity() + ":");
        //        hasResourceIdentity = true;
        //    }
        //    parent = findOne(parent.getParentId());
        //}
        
        //如果用户没有声明 资源标识  且父也没有，那么就为空
        if (!hasResourceIdentity) {
            Resource parent = findOne(resource.getParentId());
            if (StringUtils.isBlank(parent.getIdentity())) {
                return findActualResourceIdentity(parent);
            }
        }
        
        
        //如果最后一个字符是: 因为不需要，所以删除之
        int length = s.length();
        if (length > 0 && s.lastIndexOf(":") == length - 1) {
            s.deleteCharAt(length - 1);
        }
        
        ////如果有儿子 最后拼一个*
        //boolean hasChildren = false;
        //for (Resource r : findAll()) {
        //    if (resource.getId().equals(r.getParentId())) {
        //        hasChildren = true;
        //        break;
        //    }
        //}
        //if (hasChildren) {
        //    s.append(":*");
        //}
        
        return s.toString();
    }
    
    public List<Menu> findMenus(User user, Long menuRoot) {
        Searchable searchable =
                Searchable.newSearchable()
                        .addSearchFilter("show", SearchOperator.eq, true)
                        .addSort(new Sort(Sort.Direction.DESC, "parentId", "weight"));
        
        List<Resource> resources = findAllWithSort(searchable);
        //TODO 授权过滤
        Set<String> userPermissions = authorizeService.findStringPermissions(user);
        
        Iterator<Resource> iter = resources.iterator();
        while (iter.hasNext()) {
            Resource next = iter.next();
            //无权限,/**且无子节点时**/,移除这个菜单项目
            if (!hasPermission(next, userPermissions) /** && !next.isHasChildren()**/) {
                iter.remove();
            }
        }
        
        return convertToMenus(resources).get(menuRoot);
    }
    
    private boolean hasPermission(Resource resource, Set<String> userPermissions) {
        String actualResourceIdentity = findActualResourceIdentity(resource);
        if (StringUtils.isEmpty(actualResourceIdentity)) {
            return true;
        }
        
        for (String permission : userPermissions) {
            if (hasPermission(permission, actualResourceIdentity)) {
                return true;
            }
        }
        
        return false;
    }
    
    private boolean hasPermission(String permission, String actualResourceIdentity) {
        //具有所有权限
        if ("*".equals(permission) || permission.startsWith(":")) {
            return true;
        }
        
        String permissionResourceIdentity = null;
        if (permission.indexOf(":") < 0) {
            permissionResourceIdentity = permission;
        } else {
            //得到权限字符串中的 资源部分，如a:b:create --->资源是a:b
            permissionResourceIdentity = permission.substring(0, permission.lastIndexOf(":"));
        }
        
        
        //如果权限字符串中的资源 是 以资源为前缀 则有权限 如a:b 具有a:b的权限
        if (permissionResourceIdentity.startsWith(actualResourceIdentity)) {
            return true;
        }
        
        
        //模式匹配
        WildcardPermission p1 = new WildcardPermission(permissionResourceIdentity);
        WildcardPermission p2 = new WildcardPermission(actualResourceIdentity);
        
        return p1.implies(p2) || p2.implies(p1);
    }
    
    @Override
    public void deleteSelfAndChild(Resource resource) {
        super.deleteSelfAndChild(resource);
    }
    
    public List<Resource> findRoots(User user) {
        Searchable searchable =
                Searchable.newSearchable()
                        .addSearchFilter("show", SearchOperator.eq, true)
                        .addSort(new Sort(Sort.Direction.ASC, "parentId", "weight"));
        
        Set<String> userPermissions = authorizeService.findStringPermissions(user);
        
        List<Resource> rootResources = findAllWithSort(searchable).stream().filter(new Predicate<Resource>() {
            @Override
            public boolean test(Resource resource) {
                return resource.getParentId() == 0 && hasPermission(resource, userPermissions);
            }
        }).collect(Collectors.toList());
        
        return rootResources;
    }
    
    @Override
    public void move(Resource source, Resource target, String moveType) {
        super.move(source, target, moveType);
    }
    
    @Override
    public void updateSelftAndChild(Resource source, Long newParentId, String newParentIds, int newWeight) {
        super.updateSelftAndChild(source, newParentId, newParentIds, newWeight);
    }
    
    @Override
    protected List<Resource> findSelfAndNextSiblings(String parentIds, int currentWeight) {
        return super.findSelfAndNextSiblings(parentIds, currentWeight);
    }
    
    @Override
    public Set<String> findNames(Searchable searchable, String name, Long excludeId) {
        return super.findNames(searchable, name, excludeId);
    }
    
    @Override
    public List<Resource> findChildren(List<Resource> parents, Searchable searchable) {
        return super.findChildren(parents, searchable);
    }
    
    @Override
    public List<Resource> findAllByName(Searchable searchable, Resource excludeM) {
        return super.findAllByName(searchable, excludeM);
    }
    
    @Override
    public List<Resource> findRootAndChild(Searchable searchable) {
        return super.findRootAndChild(searchable);
    }
    
    @Override
    public Set<Long> findAncestorIds(Iterable<Long> currentIds) {
        return super.findAncestorIds(currentIds);
    }
    
    @Override
    public Set<Long> findAncestorIds(Long currentId) {
        return super.findAncestorIds(currentId);
    }
    
    @Override
    public List<Resource> findAncestor(String parentIds) {
        return super.findAncestor(parentIds);
    }
    
    @Override
    public void addExcludeSearchFilter(Searchable searchable, Resource excludeM) {
        super.addExcludeSearchFilter(searchable, excludeM);
    }
}
