package com.ppcxy.common.web.form;

import com.ppcxy.common.web.form.bind.SearchBindStatus;
import org.springframework.beans.BeanWrapper;
import org.springframework.beans.PropertyAccessorFactory;
import org.springframework.util.ObjectUtils;
import org.springframework.web.servlet.support.BindStatus;
import org.springframework.web.servlet.tags.form.TagWriter;

import javax.servlet.jsp.JspException;
import java.util.Collection;
import java.util.Iterator;
import java.util.Map;

/**
 * 取值时
 * 1、先取parameter
 * 2、如果找不到再找attribute (page--->request--->session--->application)
 */
public class RadioButtonsTag extends org.springframework.web.servlet.tags.form.RadioButtonsTag {

    private String element = "label";
    private Boolean inline = true;
    private BindStatus bindStatus = null;

    @Override
    protected BindStatus getBindStatus() throws JspException {

        if (this.bindStatus == null) {
            this.bindStatus = SearchBindStatus.create(pageContext, getName(), getRequestContext(), false);
        }

        if(this.bindStatus.getValueType()==null) {
            String nestedPath = this.getNestedPath();
            String pathToUse = nestedPath != null?nestedPath + this.getPath():this.getPath();
            if(pathToUse.endsWith(".")) {
                pathToUse = pathToUse.substring(0, pathToUse.length() - 1);
            }

            this.bindStatus = new BindStatus(this.getRequestContext(), pathToUse, false);
        }
        return this.bindStatus;
    }

    @Override
    protected String getPropertyPath() throws JspException {
        return getPath();
    }


    @Override
    public void doFinally() {
        super.doFinally();
        this.bindStatus = null;
    }
    @Override
    protected int writeTagContent(TagWriter tagWriter) throws JspException {
        Object items = this.getItems();
        Object itemsObject = items instanceof String?this.evaluate("items", items):items;
        String itemValue = this.getItemValue();
        String itemLabel = this.getItemLabel();
        String valueProperty = itemValue != null? ObjectUtils.getDisplayString(this.evaluate("itemValue", itemValue)):null;
        String labelProperty = itemLabel != null? ObjectUtils.getDisplayString(this.evaluate("itemLabel", itemLabel)):null;
        Class boundType = this.getBindStatus().getValueType();
        if(itemsObject == null && boundType != null && boundType.isEnum()) {
            itemsObject = boundType.getEnumConstants();
        }

        if(itemsObject == null) {
            throw new IllegalArgumentException("Attribute \'items\' is required and must be a Collection, an Array or a Map");
        } else {
            int itemIndex;
            if(itemsObject.getClass().isArray()) {
                Object[] optionMap = (Object[])((Object[])itemsObject);

                for(itemIndex = 0; itemIndex < optionMap.length; ++itemIndex) {
                    Object it = optionMap[itemIndex];
                    if(this.getInline()){
                        this.writeObjectEntry(tagWriter, valueProperty, labelProperty, it, itemIndex);
                    }

                }
            } else {
                Iterator var15;
                if(itemsObject instanceof Collection) {
                    Collection var13 = (Collection)itemsObject;
                    itemIndex = 0;

                    for(var15 = var13.iterator(); var15.hasNext(); ++itemIndex) {
                        Object entry = var15.next();
                        this.writeObjectEntry(tagWriter, valueProperty, labelProperty, entry, itemIndex);
                    }
                } else {
                    if(!(itemsObject instanceof Map)) {
                        throw new IllegalArgumentException("Attribute \'items\' must be an array, a Collection or a Map");
                    }

                    Map var14 = (Map)itemsObject;
                    itemIndex = 0;

                    for(var15 = var14.entrySet().iterator(); var15.hasNext(); ++itemIndex) {
                        Map.Entry var16 = (Map.Entry)var15.next();
                        this.writeMapEntry(tagWriter, valueProperty, labelProperty, var16, itemIndex);
                    }
                }
            }

            return 0;
        }
    }
    private void writeObjectEntry(TagWriter tagWriter, String valueProperty, String labelProperty, Object item, int itemIndex) throws JspException {
        BeanWrapper wrapper = PropertyAccessorFactory.forBeanPropertyAccess(item);
        Object renderValue;
        if(valueProperty != null) {
            renderValue = wrapper.getPropertyValue(valueProperty);
        } else if(item instanceof Enum) {
            renderValue = ((Enum)item).name();
        } else {
            renderValue = item;
        }

        Object renderLabel = labelProperty != null?wrapper.getPropertyValue(labelProperty):item;
        this.writeElementTag(tagWriter, item, renderValue, renderLabel, itemIndex);
    }
    private void writeMapEntry(TagWriter tagWriter, String valueProperty, String labelProperty, Map.Entry<?, ?> entry, int itemIndex) throws JspException {
        Object mapKey = entry.getKey();
        Object mapValue = entry.getValue();
        BeanWrapper mapKeyWrapper = PropertyAccessorFactory.forBeanPropertyAccess(mapKey);
        BeanWrapper mapValueWrapper = PropertyAccessorFactory.forBeanPropertyAccess(mapValue);
        Object renderValue = valueProperty != null?mapKeyWrapper.getPropertyValue(valueProperty):mapKey.toString();
        Object renderLabel = labelProperty != null?mapValueWrapper.getPropertyValue(labelProperty):mapValue.toString();
        this.writeElementTag(tagWriter, mapKey, renderValue, renderLabel, itemIndex);
    }

    private void writeElementTag(TagWriter tagWriter, Object item, Object value, Object label, int itemIndex) throws JspException {

        tagWriter.startTag("label");
        String id1 = this.resolveId();
        tagWriter.writeAttribute("for", id1);
        tagWriter.writeAttribute("class", "radio-inline");

        if(itemIndex > 0) {
            Object id = this.evaluate("delimiter", this.getDelimiter());
            if(id != null) {
                tagWriter.appendValue(id.toString());
            }
        }

        tagWriter.startTag("input");
        this.writeOptionalAttribute(tagWriter, "id", id1);
        this.writeOptionalAttribute(tagWriter, "name", this.getName());
        this.writeOptionalAttributes(tagWriter);
        tagWriter.writeAttribute("type", this.getInputType());
        this.renderFromValue(item, value, tagWriter);
        tagWriter.endTag();

        tagWriter.appendValue(this.convertToDisplayString(label));
        tagWriter.endTag();
    }

    public Boolean getInline() {
        return inline;
    }

    public void setInline(Boolean inline) {
        this.inline = inline;
    }
}
