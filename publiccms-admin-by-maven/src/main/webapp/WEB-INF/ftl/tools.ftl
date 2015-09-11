<#ftl>
<#macro m code>${springMacroRequestContext.getMessage(code,code)}</#macro>
<#macro ma code args>${springMacroRequestContext.getMessage(code, args, code)}</#macro>
<#macro cut string l=50 append=''><#if string?length lt l>${string}<#else>${string[0..l-1]}${append}</#if></#macro>