<#macro meger name value>
	<#if !.vars[name]??>
		<#assign temp=r"<#assign "+name+"=''>"/>
		<#assign temp=temp?interpret/>
		<@temp />
	</#if>
	<#if value??>
		<#assign temp=r"<#assign "+name+"="+name+"+value+','>"/>
		<#assign temp=temp?interpret/>
		<@temp />
	</#if>
</#macro>