<#list conditionList as a><#if a?index%3=2>

			</#if><#if "Date"=a.type>start${a.name?cap_first}=queryStart${a.name?cap_first} end${a.name?cap_first}=queryEnd${a.name?cap_first} <#else>${a.name}=query${a.name?cap_first} </#if></#list>
<#list columnList as a><#if a.order><#assign order=true/><#break/></#if></#list>
			<#if order??&&order>orderField=orderField orderType=orderType </#if>pageIndex=pageNum count=numPerPage