<#ftl>
<#macro m code>${springMacroRequestContext.getMessage(code,code?html)}</#macro>
<#macro ma code args>${springMacroRequestContext.getMessage(code, args, code?html)}</#macro>
<#macro cut string l=50 append=''>${string[0..*l]}<#if string?length gt l>${append}</#if></#macro>
<#macro merge name value><#if !.vars[name]??><@"<#assign ${name}=''>"?interpret /></#if><#if value??><@"<#assign ${name}=${name}+'${value},'>"?interpret /></#if></#macro>
<#macro compare sequence string output1='' output2=''>${(sequence?is_sequence&&sequence?seq_contains(string)||sequence?is_string&&sequence=string)?then(output1,output2)}</#macro>
<#macro dump object><#if object??><#if object?is_string>"${object!}"<#elseif object?is_number>${object}<#elseif object?is_boolean>${object?string('true','false')}<#elseif object?is_date_like>"<#if object?is_datetime>${object?datetime}<#elseif object?is_date_only>${object?date}<#elseif object?is_time>${object?time}</#if>"<#elseif object?is_hash_ex>{<#list object?keys as k>"${k}":<@dump object[k]!/><#if k?has_next>,</#if></#list>}<#elseif object?is_enumerable>[<#list object as o><@dump o/><#if o?has_next>,</#if></#list>]</#if></#if></#macro>
<#macro getParameters directive><@("<@"+directive+" showParamters=true><@dump parameters/></@"+directive+">")?interpret/></#macro>