package com.ppcxy.common.utils.jxls;

import org.springside.modules.utils.Encodes;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.io.*;
import java.util.*;

public class XmlWord {
    
    private Map<String,String> dataMap = new HashMap<String,String>();
    public Map<String, String> getDataMap() {
        return dataMap;
    }

    public void setDataMap(Map<String, String> dataMap) {
        this.dataMap = dataMap;
    }
    
    /**
     * 设置标识值
     * @param tagList 标识
     * @param dataList 数据
     */
    public void setData(List<String> tagList,List<String> dataList){
        Iterator<String> it1 = tagList.iterator();
        Iterator<String> it2 = dataList.iterator();
        while(it1.hasNext()){
            this.dataMap.put(it1.next(), it2.next());
        }
    }

    /**
     * 载入一个xml文档
     * @param filename 文件路径
     * @return 成功返回Document对象，失败返回null
     */
    public Document loadXml(String filename){
        File file = new File(filename);
    
        return loadXml(file);
    }
    
    /**
     * 载入一个xml文档
     * @param file 文件
     * @return 成功返回Document对象，失败返回null
     */
    public Document loadXml(File file){
        
        Document doc = null;
        try {
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            DocumentBuilder builder = factory.newDocumentBuilder();
            doc = (Document) builder.parse(file);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return doc;
    }
    /**
     * 图片转码
     * @return 返回图片base64字符串
     * @throws Exception
     */
    public String getImageStr(String imgFile){
        InputStream in = null;
        byte[] data = null;
        try {
            in = new FileInputStream(imgFile);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        try {
            data = new byte[in.available()];
            in.read(data);
            in.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        
        return Encodes.encodeBase64(data);
    }
    /**
     * doc2XmlFile
     * 将Document对象保存为一个xml文件
     * @return true:保存成功  flase:失败
     * @param filename 保存的文件名
     * @param document 需要保存的document对象
     */
    public boolean doc2XmlFile(Document document,String filename)
    {
       boolean flag = true;
       try{
           TransformerFactory transFactory = TransformerFactory.newInstance();
              Transformer transformer = transFactory.newTransformer();
           DOMSource source=new DOMSource();
           source.setNode(document);
           StreamResult result=new StreamResult();
           FileOutputStream fileOutputStream = new FileOutputStream(filename);
           result.setOutputStream(fileOutputStream);
           transformer.transform(source, result);
           fileOutputStream.close();
       }catch(Exception ex){
           flag = false;
           ex.printStackTrace();
       }
       return flag;
    }
    
    /**
     * doc2XmlFile
     * 将Document对象保存为一个xml文件
     * @return true:保存成功  flase:失败
     * @param outputStream 保存的文件名
     * @param document 需要保存的document对象
     */
    public boolean doc2XmlOutputstream(Document document,OutputStream outputStream)
    {
        boolean flag = true;
        try{
            TransformerFactory transFactory = TransformerFactory.newInstance();
            Transformer transformer = transFactory.newTransformer();
            DOMSource source=new DOMSource();
            source.setNode(document);
            StreamResult result=new StreamResult();
        
            result.setOutputStream(outputStream);
            transformer.transform(source, result);
            outputStream.flush();
        }catch(Exception ex){
            flag = false;
            ex.printStackTrace();
        }
        return flag;
    }
    
    
    /**
     * 替换标识内容：单个文本标记
     * @param element 要替换内容的节点
     * @param tag 标识名称
     * @param data 替换参数
     * @return 返回替换后的节点
     * @throws Exception
     */
    public Element replaceTagContext(Object  element,String tag,String data){
        Element xElement = null;
        xElement = (Element) element;
        NodeList tElements = xElement.getElementsByTagName("w:t");//w:t标签组 
        for(int i=0; i<tElements.getLength(); i++){
            Element tElement = (Element)tElements.item(i);
            if(tElement.getTextContent().equals(tag)){
                 tElement.setTextContent(data);
             }
        }
        return xElement;
    }
    
    /**
     * 替换标识内容：多个文本标记
     * @param element 要替换内容的节点
     * @return 返回替换后的节点
     * @throws Exception
     */
    public Element replaceTagContext(Element  element){
        Element xElement = element;
        NodeList tElements = xElement.getElementsByTagName("w:t");//w:t标签组 
        Set<String> dataSet = this.dataMap.keySet();
        Iterator<String> it = dataSet.iterator();
        while(it.hasNext()){
            String tag = it.next();
            String data = dataMap.get(tag);
            for(int i=0; i<tElements.getLength(); i++){
                Element tElement = (Element)tElements.item(i);
                if(tElement.getTextContent().equals(tag)){
                     tElement.setTextContent(data);
                 }
            }
        }
        return xElement;
    }
    
    /**
     * 添加图片
     * @param element 需要替换内容的节点 
     * @param tag 标识名称
     * @param imgName 图片名称，若word中有多张图，图片名必须唯一
     * @param imgFile 图片文件地址
     * @return 返回替换后的节点
     */
    public Element replacePic(Element element,String tag,String imgName,String imgFile){
        Element xElement = element;
        NodeList tElements = xElement.getElementsByTagName("w:binData");//w:t标签组  pkg:binaryData
        String wName = "wordml://"+imgName;
        for(int i=0; i<tElements.getLength(); i++){
            Element picElement = (Element)tElements.item(i);
            if(picElement.getTextContent().equals(tag)){
                 picElement.setTextContent(this.getImageStr(imgFile));/*图片编码*/
                 picElement.setAttribute("w:name",wName);//设置名字
                 Element imagedataElement = (Element) xElement.getElementsByTagName("v:imagedata").item(i);
                 imagedataElement.setAttribute("src",wName);
             }
        }
        return xElement;
    }
    
    
    /**
     * 插入图片
     * @param parentElement 图片添加至何处
     * @param imgFile 图片路径
     * @param isnewLine 是否换行
     * @return 返回添加图片节点后的节点
     */
    public Element addPic(Element parentElement,String imgFile,boolean isnewLine){
        Document parent = parentElement.getOwnerDocument();
        Element p = null;
        Element pict = null;
        Element binData = null;
        Element shape = null;
        Element imagedata = null;
        String src = "wordml://" + new Date().getTime();
        if(isnewLine){
            p = parent.createElement("w:p");
        }
        pict = parent.createElement("w:pict");
        binData = parent.createElement("w:binData");
        binData.setAttribute("w:name", src);
        binData.setAttribute("xml:space", "preserve");
        binData.setTextContent(this.getImageStr(imgFile));
        shape = parent.createElement("v:shape");
        imagedata = parent.createElement("v:imagedata");
        imagedata.setAttribute("src", src);
        //构造图片节点
        shape.appendChild(imagedata);
        pict.appendChild(binData);
        pict.appendChild(shape);
        if(isnewLine){
            p.appendChild(pict);
            parentElement.appendChild(p);
        }else{
            parentElement.appendChild(pict);
        }
        return parentElement;
    }
    
    /**
     * 插入段落
     * @param parentElement 待添加段落的节点
     * @param data 待插入数据
     * @return
     */
    public Element addParagraph(Element parentElement,String data){
        Document parent = parentElement.getOwnerDocument();
        Element p = null;
        Element r = null;
        Element t = null;
        p = parent.createElement("w:p");
        r = parent.createElement("w:r");
        t = parent.createElement("w:t");
        t.setTextContent(data);
        //构造图片节点
        r.appendChild(t);
        p.appendChild(r);
        parentElement.appendChild(p);
        return parentElement;
    }

}
