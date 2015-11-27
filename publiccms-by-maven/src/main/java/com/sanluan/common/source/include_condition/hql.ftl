<#list conditionList as a>
	<#if "Date"=a.type>
		if (notEmpty(start${a.name?cap_first})) {
			queryHandler.condition("bean.${a.name} > :start${a.name?cap_first}").setParameter("start${a.name?cap_first}", start${a.name?cap_first});
		}
		if (notEmpty(end${a.name?cap_first})) {
			queryHandler.condition("bean.${a.name} <= :end${a.name?cap_first}").setParameter("end${a.name?cap_first}", end${a.name?cap_first});
		}
	<#else>
		if (notEmpty(${a.name})) {
			queryHandler.condition("<@condition a/>").setParameter("${a.name}", <#if "String"=a.type&&a.like>like(${a.name})<#else>${a.name}</#if>);
		}
	</#if>
</#list>
<#list columnList as a><#if a.order><#assign order=true/><#break/></#if></#list>
<#if order??&&order>
		if("asc".equalsIgnoreCase(orderType)){
			orderType = "asc";
		}else{
			orderType = "desc";
		}
		if(!notEmpty(orderField)){
			orderField="";
		}
		switch(orderField) {
		<#list columnList as a><#if a.order>
			case "${a.name}" : queryHandler.order("bean.${a.name} " + orderType); break;
		</#if></#list>
			default : queryHandler.order("bean.id "+orderType);
		}
<#else>
		queryHandler.order("bean.id desc");
</#if>
<#macro condition a><#if a.or>(</#if><#list a.nameList as n>bean.${n} <#if "String"=a.type&&a.like>like<#else>=</#if> :${a.name}<#sep> or </#sep></#list><#if a.or>)</#if></#macro>