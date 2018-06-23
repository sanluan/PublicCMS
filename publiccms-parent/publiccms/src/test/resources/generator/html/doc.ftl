-- ${entityName} ---
Field list:

<#list columnList as a>
    ${a.name}:${a.title}
</#list>

Module list:

    Name:List
        ID:${entityName?uncap_first}_list
        URL：${entityName?uncap_first}/list
        Authorized Url：

    Name:Add/Edit
        ID:${entityName?uncap_first}_add
        URL：${entityName?uncap_first}/add
        Authorized Url：${entityName?uncap_first}/save
    
    Name:Delete
        ID:${entityName?uncap_first}_delete
        URL:
        Authorized Url：${entityName?uncap_first}/delete