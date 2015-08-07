<#list conditionList as a><#if a?index%3=2>

				</#if><#if "Date"=a.type>handler.get${a.type}("start${a.name?cap_first}"), handler.get${a.type}("end${a.name?cap_first}"), <#else>handler.get${a.type}("${a.name}"), </#if></#list>
<#list columnList as a><#if a.order><#assign order=true/><#break/></#if></#list>
				<#if order??&&order>handler.getString("orderField"), handler.getString("orderType"), </#if>handler.getInteger("pageIndex",1), handler.getInteger("count",20)