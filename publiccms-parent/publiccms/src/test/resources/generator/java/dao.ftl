package ${base}.${daoPack};

// Generated ${.now} by com.publiccms.common.generator.SourceGenerator
import static com.publiccms.common.tools.CommonUtils.notEmpty;

<#include "../include_imports/field_type.ftl">

import org.springframework.stereotype.Repository;

<#include "../include_imports/entity.ftl">

import com.publiccms.common.base.BaseDao;
import com.publiccms.common.handler.PageHandler;
import com.publiccms.common.handler.QueryHandler;

/**
 *
 * ${entityName}${daoSuffix}
 * 
 */
@Repository
public class ${entityName}${daoSuffix} extends BaseDao<${entityName}> {
	
	<#include "../include_condition/comment.ftl">
    public PageHandler getPage(<#include "../include_condition/condition.ftl">) {
        QueryHandler queryHandler = getQueryHandler("from ${entityName} bean");
        <#include "../include_condition/hql.ftl">
        return getPage(queryHandler, pageIndex, pageSize);
    }

    @Override
    protected ${entityName} init(${entityName} entity) {
        return entity;
    }

}