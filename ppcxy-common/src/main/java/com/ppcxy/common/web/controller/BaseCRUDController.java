package com.ppcxy.common.web.controller;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.ppcxy.common.Constants;
import com.ppcxy.common.entity.AbstractEntity;
import com.ppcxy.common.entity.search.Searchable;
import com.ppcxy.common.service.BaseService;
import com.ppcxy.common.spring.SpringContextHolder;
import com.ppcxy.common.utils.CamelCaseUtils;
import com.ppcxy.common.utils.ShiroUserInfoUtils;
import com.ppcxy.common.utils.excel.model.DataColumn;
import com.ppcxy.common.utils.excel.model.FieldMeta;
import com.ppcxy.common.utils.excel.support.impl.ExcelDatasImpontUtils;
import com.ppcxy.common.web.bind.annotation.PageableDefaults;
import com.ppcxy.common.web.controller.permission.PermissionList;
import com.ppcxy.manage.maintain.notification.support.NotificationApi;
import org.apache.commons.beanutils.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.ServletRequestDataBinder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.persistence.JoinColumn;
import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.io.IOException;
import java.io.InputStream;
import java.io.Serializable;
import java.lang.reflect.Field;
import java.util.*;

/**
 * 基础CRUD 控制器
 */
public abstract class BaseCRUDController<T extends AbstractEntity, ID extends Serializable>
        extends BaseController<T, ID> {
    
    protected BaseService<T, ID> baseService;
    protected PermissionList permissionList = null;
    private boolean listAlsoSetCommonData = true;
    
    /**
     * 设置基础service
     *
     * @param baseService
     */
    @Autowired
    public void setBaseService(BaseService<T, ID> baseService) {
        this.baseService = baseService;
    }
    
    /**
     * 用于设置通用数据,默认行为干预请覆盖 befor after,或者直接覆盖具体方法.
     *
     * @param model
     */
    @Override
    protected void preResponse(Model model) {
        super.preResponse(model);
        if (permissionList != null) {
            model.addAttribute("resourceIdentity", permissionList.getResourceIdentity());
        }
    }
    
    /**
     * 列表也设置common data
     */
    public void setListAlsoSetCommonData(boolean listAlsoSetCommonData) {
        this.listAlsoSetCommonData = listAlsoSetCommonData;
    }
    
    /**
     * 权限前缀：如sys:user
     * 则生成的新增权限为 sys:user:create
     */
    public void setResourceIdentity(String resourceIdentity) {
        if (!StringUtils.isEmpty(resourceIdentity)) {
            if (this.getModelName() == null) {
                setModelName(resourceIdentity);
            }
            
            permissionList = PermissionList.newPermissionList(resourceIdentity);
        }
    }
    
    /**
     * 进入 list 执行,默认验证权限.
     *
     * @param searchable
     * @param model
     */
    public void beforList(Searchable searchable, Model model) {
        if (permissionList != null) {
            this.permissionList.assertHasViewPermission();
        }
    }
    
    /**
     * list 逻辑执行完毕,返回页面之前执行.
     *
     * @param searchable
     * @param page
     * @param model
     */
    public void afterList(Searchable searchable, Page<T> page, Model model) {
    }
    
    @RequestMapping(method = RequestMethod.GET)
    @PageableDefaults(sort = "id=desc")
    public String list(Searchable searchable, Model model) {
        
        beforList(searchable, model);
        
        Page<T> page = baseService.findAll(searchable);
        model.addAttribute("page", page);
        
        if (listAlsoSetCommonData) {
            preResponse(model);
        }
        afterList(searchable, page, model);
        return viewName("list");
    }
    
    /**
     * 仅返回表格数据
     *
     * @param searchable
     * @param model
     * @return
     */
    @RequestMapping(method = RequestMethod.GET, headers = "table=true")
    @PageableDefaults(sort = "createDate=desc")
    public String listTable(Searchable searchable, Model model) {
        list(searchable, model);
        return viewName("listTable");
    }
    
    
    /**
     * 进入 view 执行,默认验证权限.
     *
     * @param model
     * @param entity
     */
    public void beforView(Model model, T entity) {
        if (permissionList != null) {
            this.permissionList.assertHasViewPermission();
        }
    }
    
    /**
     * view 逻辑执行完毕,返回页面之前执行.
     *
     * @param model
     * @param entity
     */
    public void afterView(Model model, T entity) {
    }
    
    @RequestMapping(value = "/{id}", method = RequestMethod.GET)
    public String view(Model model, @PathVariable("id") T entity) {
        
        beforView(model, entity);
        preResponse(model);
        
        model.addAttribute("entity", entity);
        model.addAttribute(Constants.OPTION_NAME, "查看");
        
        afterView(model, entity);
        return viewName("form");
    }
    
    /**
     * 进入 createForm 执行,默认验证权限.
     *
     * @param model
     */
    public void beforCreateForm(Model model) {
        if (permissionList != null) {
            this.permissionList.assertHasCreatePermission();
        }
    }
    
    /**
     * createForm 逻辑执行完毕,返回页面之前执行.
     *
     * @param entity
     * @param model
     */
    public void afterCreateForm(T entity, Model model) {
    }
    
    @RequestMapping(value = "create", method = RequestMethod.GET)
    public String createForm(T entity, Model model) {
        beforCreateForm(model);
        preResponse(model);
        model.addAttribute(Constants.OPTION_NAME, "create");
        if (!model.containsAttribute("entity")) {
            entity = newEntity();
            model.addAttribute("entity", entity);
        } else {
            entity = (T) model.asMap().get("entity");
        }
        
        afterCreateForm(entity, model);
        return viewName("form");
    }
    
    
    /**
     * 进入 create 执行,默认验证权限.
     *
     * @param model
     */
    public void beforCreate(Model model, @Valid @ModelAttribute("entity") T entity, BindingResult result,
                            RedirectAttributes redirectAttributes) {
        if (permissionList != null) {
            this.permissionList.assertHasCreatePermission();
        }
    }
    
    /**
     * create 逻辑执行完毕,返回页面之前执行.
     *
     * @param entity
     * @param model
     */
    public void afterCreate(Model model, @Valid @ModelAttribute("entity") T entity, BindingResult result,
                            RedirectAttributes redirectAttributes) {
    }
    
    @RequestMapping(value = "create", method = RequestMethod.POST)
    public String create(
            Model model, @Valid @ModelAttribute("entity") T entity, BindingResult result,
            @RequestParam(value = Constants.BACK_URL, required = false) String backURL,
            RedirectAttributes redirectAttributes) {
        
        
        beforCreate(model, entity, result, redirectAttributes);
        
        if (hasError(entity, result)) {
            return createForm(entity, model);
        }
        baseService.save(entity);
        redirectAttributes.addFlashAttribute(Constants.MESSAGE, "新增成功");
        afterCreate(model, entity, result, redirectAttributes);
        return redirectToUrl(backURL);
    }
    
    
    /**
     * 进入 updateForm 执行,默认验证权限.
     *
     * @param model
     */
    public void beforUpdateForm(T entity, Model model) {
        if (permissionList != null) {
            this.permissionList.assertHasUpdatePermission();
        }
    }
    
    /**
     * updateForm 逻辑执行完毕,返回页面之前执行.
     *
     * @param entity
     * @param model
     */
    public void afterUpdateForm(T entity, Model model) {
    }
    
    @RequestMapping(value = "update/{id}", method = RequestMethod.GET)
    public String updateForm(@PathVariable("id") T entity, Model model) {
        beforUpdateForm(entity, model);
        preResponse(model);
        model.addAttribute(Constants.OPTION_NAME, "update");
        model.addAttribute("entity", entity);
        
        afterUpdateForm(entity, model);
        return viewName("form");
    }
    
    /**
     * 进入 create 执行,默认验证权限.
     *
     * @param model
     */
    public void beforUpdate(Model model, @Valid @ModelAttribute("entity") T entity, BindingResult result, String backURL,
                            RedirectAttributes redirectAttributes) {
        if (permissionList != null) {
            this.permissionList.assertHasUpdatePermission();
        }
    }
    
    /**
     * create 逻辑执行完毕,返回页面之前执行.
     *
     * @param entity
     * @param model
     */
    public void afterUpdate(Model model, @Valid @ModelAttribute("entity") T entity, BindingResult result, String backURL,
                            RedirectAttributes redirectAttributes) {
    }
    
    @RequestMapping(value = "update/{id}", method = RequestMethod.POST)
    public String update(
            Model model, @Valid @ModelAttribute("entity") T entity, BindingResult result,
            @RequestParam(value = Constants.BACK_URL, required = false) String backURL,
            RedirectAttributes redirectAttributes) {
        
        
        beforUpdate(model, entity, result, backURL, redirectAttributes);
        
        if (hasError(entity, result)) {
            return updateForm(entity, model);
        }
        baseService.update(entity);
        redirectAttributes.addFlashAttribute(Constants.MESSAGE, "修改成功");
        afterUpdate(model, entity, result, backURL, redirectAttributes);
        return redirectToUrl(backURL);
    }
    
    /**
     * 进入 delete 执行,默认验证权限.
     */
    public void beforDelete(ID id, String backURL, RedirectAttributes redirectAttributes) {
        if (permissionList != null) {
            this.permissionList.assertHasDeletePermission();
        }
    }
    
    /**
     * create 逻辑执行完毕,返回页面之前执行.
     */
    public void afterDelete(ID id, String backURL, RedirectAttributes redirectAttributes) {
    }
    
    
    @RequestMapping(value = "delete/{id}")
    public String delete(
            @PathVariable("id") ID id,
            @RequestParam(value = Constants.BACK_URL, required = false) String backURL,
            RedirectAttributes redirectAttributes) {
        
        beforDelete(id, backURL, redirectAttributes);
        baseService.delete(id);
        
        redirectAttributes.addFlashAttribute(Constants.MESSAGE, "删除成功");
        afterDelete(id, backURL, redirectAttributes);
        return redirectToUrl(backURL);
    }
    
    /**
     * 进入 deleteInBatch 执行,默认验证权限.
     */
    public void beforDeleteInBatch(ID[] ids, String backURL, RedirectAttributes redirectAttributes) {
        if (permissionList != null) {
            this.permissionList.assertHasDeletePermission();
        }
    }
    
    /**
     * deleteInBatch 逻辑执行完毕,返回页面之前执行.
     */
    public void afterDeleteInBatch(ID[] ids, String backURL, RedirectAttributes redirectAttributes) {
    }
    
    @RequestMapping(value = "batch/delete", method = {RequestMethod.GET, RequestMethod.POST})
    public String deleteInBatch(
            @RequestParam(value = "ids", required = false) ID[] ids,
            @RequestParam(value = Constants.BACK_URL, required = false) String backURL,
            RedirectAttributes redirectAttributes) {
        
        beforDeleteInBatch(ids, backURL, redirectAttributes);
        baseService.delete(ids);
        
        redirectAttributes.addFlashAttribute(Constants.MESSAGE, "删除成功");
        afterDeleteInBatch(ids, backURL, redirectAttributes);
        return redirectToUrl(backURL);
    }
    
    @RequestMapping(value = "exportExcel")
    @PageableDefaults(sort = {"id=desc"})
    @ResponseBody
    public String exportExcel(final Searchable searchable, final String exportModel, final String title, HttpServletRequest request) {
        
        baseService.exportData2Excel(permissionList.getResourceIdentity(), entityClass, searchable, title, exportModel);
        
        return "success";
    }
    
    private boolean canImport(final MultipartFile file, final Model model) {
        if (file == null || file.isEmpty()) {
            model.addAttribute(Constants.ERROR_MESSAGE, "请选择要导入的文件");
            return false;
        }
        
        String filename = file.getOriginalFilename().toLowerCase();
        if (!(filename.endsWith("xls") || filename.endsWith("xlsx"))) {
            model.addAttribute(Constants.ERROR_MESSAGE, "导入的文件格式错误，允许的格式：xls、xlsx");
            return false;
        }
        
        return true;
    }
    
    @RequestMapping(value = "importExcel", method = RequestMethod.GET)
    public String importExcel(String title, Model model) {
        model.addAttribute("title", title);
        return "common/excel/importExcelForm";
    }
    
    @RequestMapping(value = "selectData", method = RequestMethod.GET)
    public String selectData(Model model) {
        List<DataColumn> dataColumns = initColumns(entityClass);
        
        model.addAttribute("modelName", permissionList.getResourceIdentity());
        model.addAttribute("datacolumn", dataColumns);
        return "common/excel/selectDataExcelForm";
    }
    
    @RequestMapping(value = "importExcel", method = RequestMethod.POST)
    @ResponseBody
    public String importExcel(final String title, @RequestParam("file") MultipartFile file, Model model) {
        if (!canImport(file, model)) {
            return "error";
        }
        
        
        InputStream is = null;
        try {
            is = file.getInputStream();
        } catch (IOException e) {
            e.printStackTrace();
            return "导入错误,请重试.";
        }
        final InputStream iis = is;
        try {
            new Thread(new Runnable() {
                @Override
                public void run() {
                    ExcelDatasImpontUtils<T> deletedSampleDatasImpontAndExportService = new ExcelDatasImpontUtils<>();
                    try {
                        deletedSampleDatasImpontAndExportService.importExcel(title, iis, entityClass, baseService);
                    } catch (Exception e) {
                        e.printStackTrace();
                        Map<String, Object> context = Maps.newHashMap();
                        context.put("model", title);
                        context.put("error", e.getMessage());
                        SpringContextHolder.getBean(NotificationApi.class).notify(ShiroUserInfoUtils.getUsername(), "excelImportError", context);
                    }
                    
                }
            }).start();
        } catch (Exception e) {
            return "导入过程发生异常,请联系管理员.";
        }
        
        return "导入成功,导入结果可能有延迟,请刷新查看.";
    }
    
    /**
     * 所有RequestMapping方法调用前的Model准备方法, 实现Struts2 Preparable二次部分绑定的效果,先根据form的id从数据库查出Task对象,再把Form提交的内容绑定到该对象上。
     * 因为仅update()方法的form中有id属性，因此仅在update时实际执行.
     */
    @ModelAttribute
    public void getEntity(@RequestParam(value = "id", defaultValue = "-1") ID id, Model model) {
        if (!id.toString().equals("-1")) {
            try {
                model.addAttribute("entity", BeanUtils.cloneBean(baseService.findOne(id)));
            } catch (Exception e) {
            
            }
            
        }
    }
    
    @InitBinder
    protected void initBinder(HttpServletRequest request,
                              ServletRequestDataBinder binder) throws Exception {
        //对于需要转换为Date类型的属性，使用DateEditor进行处理
        binder.registerCustomEditor(Date.class, new DateEditor());
    }
    
    
    /**
     * 根据类去初始化列信息
     *
     * @param dataClass
     */
    public List<DataColumn> initColumns(Class dataClass) {
        List<DataColumn> columns = Lists.newArrayList();
        
        if (columns.size() == 0) {
            Field[] fs = dataClass.getDeclaredFields();
            
            columns.add(new DataColumn("数据库主键", "id"));
            
            for (Field field : fs) {
                DataColumn dataColumn = new DataColumn();
                dataColumn.setColumnName(field.getName());
                //获取字段中包含fieldMeta的注解
                FieldMeta meta = field.getAnnotation(FieldMeta.class);
                if (meta != null) {
                    dataColumn.setTitle(meta.description());
                    if (meta.isRef()) {
                        dataColumn.setRefColumnName(meta.refColumn());
                    }
                }
                
                JoinColumn joinColumn = field.getAnnotation(JoinColumn.class);
                if (joinColumn != null) {
                    String refColumnName = joinColumn.referencedColumnName();
                    if (org.apache.commons.lang3.StringUtils.isNotBlank(refColumnName)) {
                        String refColumn = CamelCaseUtils.toCamelCase(refColumnName);
                        dataColumn.setRefColumnName(refColumn);
                    }
                }
                columns.add(dataColumn);
            }
            
            Collections.sort(columns, new Comparator<DataColumn>() {
                @Override
                public int compare(DataColumn o1, DataColumn o2) {
                    return o1.getOrder() - o2.getOrder();
                }
            });
        }
        
        return columns;
    }
}
