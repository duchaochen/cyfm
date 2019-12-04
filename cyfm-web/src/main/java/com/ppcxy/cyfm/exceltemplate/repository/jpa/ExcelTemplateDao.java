package com.ppcxy.cyfm.exceltemplate.repository.jpa;

import com.ppcxy.common.repository.jpa.BaseRepository;
import com.ppcxy.cyfm.exceltemplate.entity.ExcelTemplate;

public interface ExcelTemplateDao  extends BaseRepository<ExcelTemplate, Long> {
    ExcelTemplate findByResourceIdentity(String modelName);
}
