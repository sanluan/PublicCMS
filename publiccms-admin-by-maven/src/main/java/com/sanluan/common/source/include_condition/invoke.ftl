<#list conditionList as a><#if a?index%3=2>

				</#if><#if "Date"=a.type>start${a.name?cap_first}, end${a.name?cap_first}, <#else>${a.name}, </#if></#list>
<#list columnList as a><#if a.order><#assign order=true/><#break/></#if></#list>
				<#if order??&&order>orderField, orderType, </#if>pageIndex, pageSize