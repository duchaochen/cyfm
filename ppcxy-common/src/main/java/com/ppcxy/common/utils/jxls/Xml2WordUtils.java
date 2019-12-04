package com.ppcxy.common.utils.jxls;

import org.w3c.dom.Document;

import java.util.ArrayList;
import java.util.List;

public class Xml2WordUtils {
    public static void main(String[] args) {
        XmlWord test = new XmlWord();
    
        //1、载入模板
    
        Document doc = test.loadXml("/Users/weep/javadev/cyfm/cyfm-web/src/main/java/com/ppcxy/cyfm/experi/web/excel/plan.xml");
    
        //2、设置标记，tagList中存放的标记要同模板中的标记一致；
    
        List<String> tagList = new ArrayList<String>();
    
        tagList.add("${plan.courseName}");
    
        tagList.add("${test_no}");
    
        //3、设置填充标记的值，dataList中存放数据顺序与tagList存放标记顺序一致；
    
        List<String> dataList = new ArrayList<String>();
  
        dataList.add("----用例名");
    
        dataList.add("----用例编号");
    
        //4、将标记和标记值存入dataMap
    
        test.setData(tagList, dataList);
    
    
        test.replaceTagContext(doc.getDocumentElement());
    
        //6、写入文档
    
        test.doc2XmlFile(doc, "xmlword.doc");
        
    }
}
