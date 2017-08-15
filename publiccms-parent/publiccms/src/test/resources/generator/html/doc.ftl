-- ${entityName} ---
字段列表：
<#list columnList as a>
	${a.name}:${a.title}
</#list>
模块数据:
	地址：${entityName?uncap_first}/list
	授权访问地址：${entityName?uncap_first}/save,${entityName?uncap_first}/add
