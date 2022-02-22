<#ftl>
<#macro message code args=[]><#if args?has_content><#if args?is_sequence>${springMacroRequestContext.getMessage(code, args)}<#else>${springMacroRequestContext.getMessage(code)}</#if><#else>${springMacroRequestContext.getMessage(code)}</#if></#macro>
<#macro menu code args...><@message 'menu.'+code args/></#macro>
<#macro page code args...><@message 'page.'+code args/></#macro>
<#macro cut string l=50 append=''>${string[0..*l]}<#if string?length gt l>${append}</#if></#macro>
<#macro merge name value><#if !.vars[name]??><@"<#assign ${name}=''>"?interpret /></#if><#if value?has_content><@"<#assign ${name}=${name}+'${value},'>"?interpret /></#if></#macro>
<#macro compare sequence=[] string='' output1='' output2=''>${(sequence?is_sequence&&sequence?seq_contains(string)||sequence?is_string&&sequence=string)?then(output1,output2)?no_esc}</#macro>
<#macro dump object><#if object?has_content><#if object?is_string>"${object!}"<#elseif object?is_number>${object}<#elseif object?is_boolean>${object?string('true','false')}<#elseif object?is_date_like>"<#if object?is_datetime>${object?datetime}<#elseif object?is_date_only>${object?date}<#elseif object?is_time>${object?time}</#if>"<#elseif object?is_hash_ex>{<#list object as k,v>"${k}":<@dump v!/><#if k?has_next>,</#if></#list>}<#elseif object?is_enumerable>[<#list object as o><@dump o/><#if o?has_next>,</#if></#list>]</#if></#if></#macro>
<#macro getParameters directive><@("<@"+directive+" showParameters=true><@dump parameters/></@"+directive+">")?interpret/></#macro>
<#macro fileSize size><#if size gt 10*1024*1024*1024*1024>${size/1024/1024/1024/1024}TB<#elseif size gt 1024*1024*1024*1024>${(size/1024/1024/1024/1024)?string('0.##')}TB<#elseif size gt 10*1024*1024*1024>${size/1024/1024/1024}GB<#elseif size gt 1024*1024*1024>${(size/1024/1024/1024)?string('0.##')}GB<#elseif size gt 10*1024*1024>${size/1024/1024}MB<#elseif size gt 1024*1024>${(size/1024/1024)?string('0.##')}MB<#elseif size gt 10*1024>${size/1024}KB<#elseif size gt 1024>${(size/1024)?string('0.##')}KB<#elseif size gt 0>${size}B<#else>0</#if></#macro>
<#function pageMessage code><#return springMacroRequestContext.getMessage('page.'+code,code)/></#function>
<#macro secrecy text maxshow><#local length=text?length/><#if length gt maxshow*2>${text[0..*maxshow]+text[length-maxshow..]?left_pad(length-maxshow,'*')}<#else>${text}</#if></#macro>
<#function fileType fileName>
    <#assign fileSuffix = fileName?keep_after_last('.')/>
    <#switch fileSuffix>
        <#case 'jpg'>
        <#case 'png'>
        <#case 'bmp'>
        <#case 'jpeg'>
        <#case 'gif'>
        <#case 'svg'>
            <#return 'image'/><#break>
        <#case 'ogg'>
        <#case 'webm'>
        <#case 'mpeg'>
        <#case 'mpg'>
        <#case 'mov'>
        <#case 'rm'>
        <#case 'ram'>
        <#case 'avi'>
        <#case 'wmv'>
        <#case 'mp4'>
        <#case 'flv'>
        <#case 'swf'>
        <#case 'mkv'>
            <#return 'video'/><#break>
        <#case 'wav'>
        <#case 'acc'>
        <#case 'mid'>
        <#case 'midi'>
        <#case 'wma'>
        <#case 'mpga'>
        <#case 'mp3'>
            <#return 'audio'/><#break>
        <#default>
            <#return 'other'/>
    </#switch>
</#function>