-- ${entityName} begin ${.now}---
字段列表：
<#list columnList as a>
	${a.name}:${a.title}
</#list>
模块数据:
	地址：${entityName?uncap_first}/list
	授权访问地址：${entityName?uncap_first}/save,${entityName?uncap_first}/add
			
operate.delete.${entityName?uncap_first}=\u5220\u9664
operate.save.${entityName?uncap_first}=\u521B\u5EFA
operate.update.${entityName?uncap_first}=\u66F4\u65B0
-- ${entityName} end --

