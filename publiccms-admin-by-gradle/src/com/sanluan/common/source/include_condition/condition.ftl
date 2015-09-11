<#list conditionList as a><#if a?index%3=2>

				</#if><#if "Date"=a.type>${a.type} start${a.name?cap_first}, ${a.type} end${a.name?cap_first}, <#else>${a.type} ${a.name}, </#if></#list>
<#list columnList as a><#if a.order><#assign order=true/><#break/></#if></#list>
				<#if order??&&order>String orderField, String orderType, </#if>Integer pageIndex, Integer pageSize