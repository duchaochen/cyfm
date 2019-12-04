package com.ppcxy.common.utils.excel.support.impl;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.ppcxy.common.entity.AbstractEntity;
import com.ppcxy.common.service.BaseService;
import org.apache.commons.lang3.StringUtils;
import org.apache.poi.ss.usermodel.BuiltinFormats;
import org.apache.poi.ss.usermodel.DataFormatter;
import org.apache.poi.xssf.model.SharedStringsTable;
import org.apache.poi.xssf.model.StylesTable;
import org.apache.poi.xssf.usermodel.XSSFCellStyle;
import org.apache.poi.xssf.usermodel.XSSFRichTextString;
import org.joda.time.DateTime;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springside.modules.mapper.BeanMapper;
import org.springside.modules.utils.Reflections;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * <p>Date: 13-7-12 下午9:50
 * <p>Version: 1.0
 */
public class ExcelDatasImportSheetHandler extends DefaultHandler {

    private final Logger log = LoggerFactory.getLogger(ExcelDatasImportSheetHandler.class);

    private int batchSize; //批处理大小
    private int totalSize = 0; //总行数

    Pattern pat = Pattern.compile("\\[([\\s\\S]*?)]");

    //=====================================
    private StylesTable stylesTable;

    private SharedStringsTable sst;
    private String lastContents;

    private boolean nextIsString;
    private List<String> rowlist = new ArrayList<String>();
    private int curRow = 0;
    private int curCol = 0;

    //定义前一个元素和当前元素的位置，用来计算其中空的单元格数量，如A6和A8等
    private String preRef = null, ref = null;
    //定义该文档一行最大的单元格数，用来补全一行最后可能缺失的单元格
    private String maxRef = null;

    private CellDataType nextDataType = CellDataType.SSTINDEX;
    private final DataFormatter formatter = new DataFormatter();
    private short formatIndex;
    private String formatString;

    //用一个enum表示单元格可能的数据类型
    enum CellDataType {
        BOOL, ERROR, FORMULA, INLINESTR, SSTINDEX, NUMBER, DATE, NULL
    }

    //=====================================
    private List<Map<String, Object>> dataList;

    private Class<?> entityClass;
    private BaseService service;
    private Map<String, Object> columnConf;
    private List<String> columnNames;

    public void setColumnConf(Map<String, Object> columnConf) {
        this.columnConf = columnConf;
    }

    ExcelDatasImportSheetHandler(SharedStringsTable sst,
                                 final List<Map<String, Object>> datas, final int batchSize) {
        this.sst = sst;
        this.dataList = datas;
        this.batchSize = batchSize;
    }

    /**
     * 解析一个element的开始时触发事件
     */
    public void startElement(String uri, String localName, String name,
                             Attributes attributes) throws SAXException {

        // c => cell
        if (name.equals("c")) {
            //前一个单元格的位置
            if (preRef == null) {
                preRef = attributes.getValue("r");
            } else {
                preRef = ref;
            }
            //当前单元格的位置
            ref = attributes.getValue("r");

            this.setNextDataType(attributes);

            // Figure out if the value is an index in the SST
            String cellType = attributes.getValue("t");
            if (cellType != null && cellType.equals("s")) {
                nextIsString = true;
            } else {
                nextIsString = false;
            }

        }
        // Clear contents cache
        lastContents = "";
    }

    /**
     * 根据element属性设置数据类型
     *
     * @param attributes
     */
    public void setNextDataType(Attributes attributes) {

        nextDataType = CellDataType.NUMBER;
        formatIndex = -1;
        formatString = null;
        String cellType = attributes.getValue("t");
        String cellStyleStr = attributes.getValue("s");
        if ("b".equals(cellType)) {
            nextDataType = CellDataType.BOOL;
        } else if ("e".equals(cellType)) {
            nextDataType = CellDataType.ERROR;
        } else if ("inlineStr".equals(cellType)) {
            nextDataType = CellDataType.INLINESTR;
        } else if ("s".equals(cellType)) {
            nextDataType = CellDataType.SSTINDEX;
        } else if ("str".equals(cellType)) {
            nextDataType = CellDataType.FORMULA;
        }
        if (cellStyleStr != null) {
            int styleIndex = Integer.parseInt(cellStyleStr);
            XSSFCellStyle style = stylesTable.getStyleAt(styleIndex);
            formatIndex = style.getDataFormat();
            formatString = style.getDataFormatString();
            if ("m/d/yy" == formatString) {
                nextDataType = CellDataType.DATE;
                //full format is "yyyy-MM-dd hh:mm:ss.SSS";
                formatString = "yyyy-MM-dd";
            }
            if (formatString == null) {
                nextDataType = CellDataType.NULL;
                formatString = BuiltinFormats.getBuiltinFormat(formatIndex);
            }
        }
    }

    /**
     * 解析一个element元素结束时触发事件
     */
    @Override
    public void endElement(String uri, String localName, String name)
            throws SAXException {
        // Process the last contents as required.
        // Do now, as characters() may be called more than once

        if (nextIsString) {
            int idx = Integer.parseInt(lastContents);
            lastContents = new XSSFRichTextString(sst.getEntryAt(idx)).toString();
            nextIsString = false;
        }

        // v => contents of a cell
        // Output after we've seen the string contents
        if (name.equals("v")) {
            String value = this.getDataValue(lastContents.trim(), "");
            //补全单元格之间的空单元格
            if (!ref.equals(preRef)) {
                int len = countNullCell(ref, preRef);
                for (int i = 0; i < len; i++) {
                    rowlist.add(curCol, "");
                    curCol++;
                }
            }
            rowlist.add(curCol, value);
            curCol++;
        } else {
            //如果标签名称为 row，这说明已到行尾，调用 optRows() 方法
            if (name.equals("row")) {
                String value = "";
                //默认第一行为表头，以该行单元格数目为最大数目
                if (curRow == 0) {
                    maxRef = ref;
                }
                //补全一行尾部可能缺失的单元格
                if (maxRef != null) {
                    int len = countNullCell(maxRef, ref);
                    for (int i = 0; i <= len; i++) {
                        rowlist.add(curCol, "");
                        curCol++;
                    }
                }

                Map<String, Object> data = Maps.newHashMap();
                if (curRow != 0) {
                    //初始化数据
                    for (int i = 0; i < columnNames.size(); i++) {
                        String val = rowlist.get(i);
                        Object retVal = StringUtils.isBlank(val) ? null : val;
                        Class aClass = (Class) columnConf.get(columnNames.get(i));
                        if (aClass != null && StringUtils.isNotBlank(val)) {
                            if (aClass.equals(Date.class)) {
                                DateTime dateTime = new DateTime(val);
                                retVal = dateTime.toDate();
                            } else if (columnConf.get(columnNames.get(i) + "_genericClazz") != null) {
                                Class genericClazz = (Class) columnConf.get(columnNames.get(i) + "_genericClazz");
                                //TODO 处理集合类型
                                String[] ids = retVal.toString().split(",");
                                try {
                                    List list = Lists.newArrayList();
                                    for (String id : ids) {
                                        String[] idArrays = id.split(";");
                                        AbstractEntity ae = (AbstractEntity) genericClazz.newInstance();
                                        ae.setId(idArrays[0]);
                                        //集合类型处理很麻烦,需要查询之后复制,具体业务具体分析吧~.
                                        //if(idArrays.length ==2){
                                        //    Reflections.setFieldValue(ae, (String) columnConf.get(columnNames.get(i) + "_refColumn"), idArrays[1]);
                                        //}
                                        list.add(ae);
                                    }
                                    retVal = list;
                                } catch (InstantiationException e) {
                                    e.printStackTrace();
                                } catch (IllegalAccessException e) {
                                    e.printStackTrace();
                                }

                            } else
                                try {
                                    if (columnConf.get(columnNames.get(i) + "_refColumn") != null) {
                                        try {
                                            AbstractEntity abstractEntity = (AbstractEntity) aClass.newInstance();
                                            String[] valuarray = val.split(";");
                                            Reflections.setFieldValue(abstractEntity, "id", valuarray[0]);
                                            if(valuarray.length==2){
                                                Reflections.setFieldValue(abstractEntity, (String) columnConf.get(columnNames.get(i) + "_refColumn"), valuarray[1]);
                                            }
                                            retVal = abstractEntity;
                                        } catch (InstantiationException e) {
                                            e.printStackTrace();
                                        } catch (IllegalAccessException e) {
                                            e.printStackTrace();
                                        }
                                    } else if (aClass.isEnum()) {
                                        Field[] fields = aClass.getFields();
                                        try {
                                            for (Field field : fields) {
                                                Object o = field.get(new Object());
                                                String info = (String) Reflections.getFieldValue(o, "info");
                                                if (info.equals(val)) {
                                                    retVal = o;
                                                }
                                            }
                                        } catch (IllegalAccessException e) {
                                            e.printStackTrace();
                                        }
                                    }
                                } catch (Exception e) {
                                }
                        }
                        data.put(columnNames.get(i), retVal);
                    }
                    dataList.add(data);
                } else {
                    if (columnNames == null) columnNames = Lists.newArrayList();
                    for (String cell : rowlist) {
                        Matcher mat = pat.matcher(cell);
                        mat.find();
                        columnNames.add(mat.group(1));
                    }
                }


                totalSize++;

                if (totalSize % batchSize == 0) {
                    List<?> objects = BeanMapper.mapList(dataList, entityClass);
                    if (objects.size() > 0) {
                        service.save(objects);
                        dataList.clear();
                    }
                }

                curRow++;
                //一行的末尾重置一些数据
                rowlist.clear();
                curCol = 0;
                preRef = null;
                ref = null;
            }
        }

    }

    /**
     * 根据数据类型获取数据
     *
     * @param value
     * @param thisStr
     * @return
     */

    public String getDataValue(String value, String thisStr)

    {
        switch (nextDataType) {
            //这几个的顺序不能随便交换，交换了很可能会导致数据错误
            case BOOL:
                char first = value.charAt(0);
                thisStr = first == '0' ? "FALSE" : "TRUE";
                break;
            case ERROR:
                thisStr = "\"ERROR:" + value.toString() + '"';
                break;
            case FORMULA:
                thisStr = '"' + value.toString() + '"';
                break;
            case INLINESTR:
                XSSFRichTextString rtsi = new XSSFRichTextString(value.toString());
                thisStr = rtsi.toString();
                rtsi = null;
                break;
            case SSTINDEX:
                String sstIndex = value.toString();
                thisStr = value.toString();
                break;
            case NUMBER:
                if (formatString != null) {
                    thisStr = formatter.formatRawCellContents(Double.parseDouble(value), formatIndex, formatString).trim();
                } else {
                    thisStr = value;
                }
                thisStr = thisStr.replace("_", "").trim();
                break;
            case DATE:
                try {
                    thisStr = formatter.formatRawCellContents(Double.parseDouble(value), formatIndex, formatString);
                } catch (NumberFormatException ex) {
                    thisStr = value.toString();
                }
                thisStr = thisStr.replace(" ", "");
                break;
            default:
                thisStr = "";
                break;
        }
        return thisStr;
    }

    /**
     * 计算两个单元格之间的单元格数目(同一行)
     *
     * @param ref
     * @param preRef
     * @return
     */
    public int countNullCell(String ref, String preRef) {
        //excel2007最大行数是1048576，最大列数是16384，最后一列列名是XFD
        String xfd = ref.replaceAll("\\d+", "");
        String xfd_1 = preRef.replaceAll("\\d+", "");

        xfd = fillChar(xfd, 3, '@', true);
        xfd_1 = fillChar(xfd_1, 3, '@', true);

        char[] letter = xfd.toCharArray();
        char[] letter_1 = xfd_1.toCharArray();
        int res = (letter[0] - letter_1[0]) * 26 * 26 + (letter[1] - letter_1[1]) * 26 + (letter[2] - letter_1[2]);
        return res - 1;
    }

    /**
     * 字符串的填充
     *
     * @param str
     * @param len
     * @param let
     * @param isPre
     * @return
     */
    String fillChar(String str, int len, char let, boolean isPre) {
        int len_1 = str.length();
        if (len_1 < len) {
            if (isPre) {
                for (int i = 0; i < (len - len_1); i++) {
                    str = let + str;
                }
            } else {
                for (int i = 0; i < (len - len_1); i++) {
                    str = str + let;
                }
            }
        }
        return str;
    }

    public void characters(char[] ch, int start, int length) throws SAXException {
        lastContents += new String(ch, start, length);
    }

    public void setEntityClass(Class<?> entityClass) {
        this.entityClass = entityClass;
    }

    public void setService(BaseService service) {
        this.service = service;
    }

    public void setColumnNames(List<String> columnNames) {
        this.columnNames = columnNames;
    }

    public void setStylesTable(StylesTable stylesTable) {
        this.stylesTable = stylesTable;
    }
}
