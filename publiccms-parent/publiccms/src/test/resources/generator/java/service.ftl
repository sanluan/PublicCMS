package ${base}.${servicePack};

// Generated ${.now} by com.publiccms.common.generator.SourceGenerator
<#include "../include_imports/field_type.ftl">

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

<#include "../include_imports/entity.ftl">

<#include "../include_imports/dao.ftl">

import com.publiccms.common.base.BaseService;
import com.publiccms.common.handler.PageHandler;

/**
 *
 * ${entityName}${serviceSuffix}
 * 
 */
@Service
@Transactional
public class ${entityName}${serviceSuffix} extends BaseService<${entityName}> {

    <#include "../include_condition/comment.ftl">
    @Transactional(readOnly = true)
    public PageHandler getPage(<#include "../include_condition/condition.ftl">) {
        return dao.getPage(<#include "../include_condition/invoke.ftl">);
    }
    
    @Autowired
    private ${entityName}Dao dao;
    
}