<#macro meger name value>
	<#if !.vars[name]??>
		<@"<#assign ${name}=''>"?interpret />
	</#if>
	<#if value??>
		<@"<#assign ${name}=${name}+'${value},'>"?interpret />
	</#if>
</#macro>