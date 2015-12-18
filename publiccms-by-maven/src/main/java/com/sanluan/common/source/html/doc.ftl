-- ${entityName} begin ${.now}---
字段列表：
<#list columnList as a>
	${a.name}:${a.title}
</#list>
模块数据:
	地址：${entityName?uncap_first}/list
	授权访问地址：${entityName?uncap_first}/save,${entityName?uncap_first}/add
自定义指令：
	分页列表查询：
		${r"<@_"+entityName?replace('Cms','')?uncap_first+"List"} <#include "../include_condition/paramter.ftl">>${r"</@_"+entityName?replace('Cms','')?uncap_first+"List>"}
		结果：
			page:分页信息，totalCount：总条数，totalPage：总页数，pageSize每页数据条数，pageIndex当前页数
			page.list: List<${entityName}>对象 使用<#noparse><#list t_list as a></#list></#noparse> 遍历数据
	单条记录查询：
		${"<@_"+entityName?replace('Cms','')?uncap_first+" id=id ids=ids></@_"+entityName?replace('Cms','')?uncap_first+">"}
		结果：
			id不为空时：object:属性参考字段列表
			id为空且ids不为空时：t_map:{key:id,value:bean}其中bean属性参考字段列表
-- ${entityName} end --

