<#list conditionList as a><#if a?index%3=2>

                </#if><#if "Date"=a.type>handler.get${a.type}("start${a.name?cap_first}"), handler.get${a.type}("end${a.name?cap_first}"), <#elseif 'siteId'=a.name>getSite(handler).getId(),<#else>handler.get${a.type}("${a.name}"), </#if></#list>
<#assign
    orderSize=0
    order=false
/>
<#list columnList as a><#if a.order><#assign order=true orderSize+=1/></#if></#list>
                <#if order><#if orderSize gt 1>handler.getString("orderField"), </#if>handler.getString("orderType"), </#if>handler.getInteger("pageIndex",1), handler.getInteger("pageSize",30)