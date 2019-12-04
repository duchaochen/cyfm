package com.ppcxy.cyfm.exceltemplate.entity;

import com.ppcxy.common.entity.IdEntity;

import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Table(name = "cy_excel_template")
public class ExcelTemplate extends IdEntity {

    private String templateId;
    private String resourceIdentity;

    public String getTemplateId() {
        return templateId;
    }

    public void setTemplateId(String templateId) {
        this.templateId = templateId;
    }

    public String getResourceIdentity() {
        return resourceIdentity;
    }

    public void setResourceIdentity(String resourceIdentity) {
        this.resourceIdentity = resourceIdentity;
    }
}
