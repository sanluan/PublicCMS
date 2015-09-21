package ${base}.${servicePack};

// Generated ${.now} by SourceMaker

<#include "../include_imports/field_type.ftl">

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

<#include "../include_imports/entity.ftl">

<#include "../include_imports/dao.ftl">

import com.sanluan.common.base.BaseService;
import com.sanluan.common.handler.PageHandler;

@Service
@Transactional
public class ${entityName}Service extends BaseService<${entityName}> {

	@Autowired
	private ${entityName}Dao dao;

	@Transactional(readOnly = true)
	public PageHandler getPage(<#include "../include_condition/condition.ftl">) {
		return dao.getPage(<#include "../include_condition/invoke.ftl">);
	}
}