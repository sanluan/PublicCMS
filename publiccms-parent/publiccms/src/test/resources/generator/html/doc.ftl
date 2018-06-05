-- ${entityName} ---
字段列表：

<#list columnList as a>
    ${a.name}:${a.title}
</#list>

模块数据:

    名称：列表
 ID:${entityName?uncap_first}_list
    地址：${entityName?uncap_first}/list
    授权访问地址：

    名称：增加/修改
 ID:${entityName?uncap_first}_add
    地址：${entityName?uncap_first}/add
    授权访问地址：${entityName?uncap_first}/save

    名称：删除
 ID:${entityName?uncap_first}_delete
    地址：
    授权访问地址：${entityName?uncap_first}/delete