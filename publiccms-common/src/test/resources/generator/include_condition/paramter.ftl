<#list conditionList as a><#if a?index%3=2>

            </#if><#if "Date"=a.type>start${a.name?cap_first}=start${a.name?cap_first} end${a.name?cap_first}=end${a.name?cap_first} <#else>${a.name}=${a.name} </#if></#list>
<#assign orderSize=0/>
<#assign order=false/>
<#list columnList as a><#if a.order><#assign order=true/><#assign orderSize+=1/></#if></#list>
            <#if order><#if orderSize gt 1>orderField=orderField </#if>orderType=orderType </#if>pageIndex=pageNum count=numPerPage