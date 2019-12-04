package com.ppcxy.common.utils.excel.support.impl;


import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.ppcxy.common.entity.AbstractEntity;
import com.ppcxy.common.service.BaseService;
import com.ppcxy.common.utils.CamelCaseUtils;
import com.ppcxy.common.utils.excel.model.DataColumn;
import com.ppcxy.common.utils.excel.model.FieldMeta;
import com.ppcxy.common.utils.excel.support.ExcelDataAbstract;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.poi.openxml4j.exceptions.OpenXML4JException;
import org.apache.poi.openxml4j.opc.OPCPackage;
import org.apache.poi.xssf.eventusermodel.XSSFReader;
import org.apache.poi.xssf.model.SharedStringsTable;
import org.springside.modules.mapper.BeanMapper;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.XMLReader;
import org.xml.sax.helpers.XMLReaderFactory;

import javax.persistence.JoinColumn;
import java.io.BufferedInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class ExcelDatasImpontUtils<T extends AbstractEntity> extends ExcelDataAbstract {
    
    public void setColumns(List<DataColumn> dataColumns) {
        columns.addAll(dataColumns);
    }
    
    public void setColumn(DataColumn dataColumn) {
        columns.add(dataColumn);
    }
    
    private final List<DataColumn> columns = Lists.newArrayList();
    
    public ExcelDatasImpontUtils() {
    }
    
    
    public ExcelDatasImpontUtils(Class<T> dataClass) {
        this.columns.addAll(initColumns(dataClass));
    }
    
    public void importExcel(String sheetName, InputStream is, Class entityClass, BaseService service) throws IllegalAccessException, InstantiationException, OpenXML4JException, SAXException, IOException {
        Map<String, Object> columnConfig = Maps.newHashMap();
        Field[] fs = entityClass.getDeclaredFields();
        for (Field field : fs) {
            Class<?> type = field.getType();
            columnConfig.put(field.getName(), type);
            
            if (type.isPrimitive()) continue;  //【1】 //判断是否为基本类型
            if (type.getName().startsWith("java.lang")) continue; //getName()返回field的类型全路径；
            
            if (!type.isInterface() && !type.isEnum() && type.newInstance() instanceof AbstractEntity) {
                JoinColumn joinColumn = field.getAnnotation(JoinColumn.class);
                String refColumnName = joinColumn.referencedColumnName();
                if (StringUtils.isNotBlank(refColumnName)) {
                    String refColumn = CamelCaseUtils.toCamelCase(refColumnName);
                    columnConfig.put(field.getName() + "_refColumn", refColumn);
                } else {
                    columnConfig.put(field.getName() + "_refColumn", "id");
                }
                continue;
            }
            
            FieldMeta joinColumn = field.getAnnotation(FieldMeta.class);
            if (joinColumn != null && joinColumn.isRef()) {
                columnConfig.put(field.getName() + "_refColumn", joinColumn.refColumn());
            }
            
            //【2】
            if (type.isAssignableFrom(Set.class) || type.isAssignableFrom(List.class)) {
                Type fc = field.getGenericType(); // 关键的地方，如果是List类型，得到其Generic的类型
                
                if (fc == null) continue;
                // 【3】如果是泛型参数的类型
                if (fc instanceof ParameterizedType) {
                    ParameterizedType pt = (ParameterizedType) fc;
                    
                    Class genericClazz = (Class) pt.getActualTypeArguments()[0]; //【4】 得到泛型里的class类型对象。
                    columnConfig.put(field.getName() + "_genericClazz", genericClazz);
                }
            }
            
        }
        
        BufferedInputStream bis = null;
        try {
            List<Map<String, Object>> dataList = Lists.newArrayList();
            
            bis = new BufferedInputStream(is);
            OPCPackage pkg = OPCPackage.open(bis);
            XSSFReader r = new XSSFReader(pkg);
            SharedStringsTable sst = r.getSharedStringsTable();
            
            XMLReader parser = XMLReaderFactory.createXMLReader();
            
            ExcelDatasImportSheetHandler datasExcelImportSheetHandler = new ExcelDatasImportSheetHandler(sst, dataList, 200);
            datasExcelImportSheetHandler.setEntityClass(entityClass);
            datasExcelImportSheetHandler.setService(service);
            datasExcelImportSheetHandler.setStylesTable(r.getStylesTable());
            datasExcelImportSheetHandler.setColumnConf(columnConfig);
            parser.setContentHandler(datasExcelImportSheetHandler);
            
            Iterator<InputStream> sheets = r.getSheetsData();
            while (sheets.hasNext()) {
                InputStream sheet = null;
                try {
                    sheet = sheets.next();
                    InputSource sheetSource = new InputSource(sheet);
                    parser.parse(sheetSource);
                } catch (Exception e) {
                    throw e;
                } finally {
                    IOUtils.closeQuietly(sheet);
                }
            }
            
            //把最后剩下的不足batchSize大小
            if (dataList.size() > 0) {
                List<AbstractEntity> list = BeanMapper.mapList(dataList, entityClass);
                service.save(list);
            }
            
        } catch (Exception e) {
            throw e;
        } finally {
            IOUtils.closeQuietly(bis);
        }
    }
    
    public static void main(String[] args) throws FileNotFoundException {
        //ExcelDatasImpontUtils<DeletedSample> deletedSampleDatasImpontAndExportService = new ExcelDatasImpontUtils<>();
        //List<DeletedSample> ds = Lists.newArrayList();
        //
        //for (Long i = 0l; i < 20; i++) {
        //    DeletedSample d = new DeletedSample();
        //    d.setId(i);
        //    d.setAge(i.intValue());
        //    d.setName("名字" + i);
        //    d.setBirthday(new DateTime(2012, 12, 12, 11, 22, 11).plusDays(i.intValue()).toDate());
        //    d.setCreateDate(new Date());
        //    d.setSex(Sex.valueOf(i % 2 == 0 ? "male" : "female"));
        //    ds.add(d);
        //}
        //User user = new User();
        //user.setUsername("111");
        
        //deletedSampleDatasImpontAndExportService.exportTableDatas("测试导出", ds, user, "d:/");
        
        //deletedSampleDatasImpontAndExportService.importExcel(new FileInputStream(new File("d:/excel_20160810134051297.xlsx")),DeletedSample.class,new DeletedSampleService());
        
    }
}
