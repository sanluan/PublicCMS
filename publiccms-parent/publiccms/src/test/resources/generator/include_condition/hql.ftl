<#list conditionList as a>
    <#if "Date"=a.type>
        if (null != start${a.name?cap_first}) {
            queryHandler.condition("bean.${a.name} > :start${a.name?cap_first}").setParameter("start${a.name?cap_first}", start${a.name?cap_first});
        }
        if (null != end${a.name?cap_first}) {
            queryHandler.condition("bean.${a.name} <= :end${a.name?cap_first}").setParameter("end${a.name?cap_first}", end${a.name?cap_first});
        }
    <#else>
        if (<#if ["Long","Integer","String"]?seq_contains(a.type)>CommonUtils.notEmpty(${a.name})<#else>null != ${a.name}</#if>) {
            queryHandler.condition("<@condition a/>").setParameter("${a.name}", <#if "String"=a.type&&a.like>like(${a.name})<#else>${a.name}</#if>);
        }
    </#if>
</#list>
<#assign
    orderSize=0
    order=false
/>
<#list columnList as a><#if a.order><#assign order=true orderSize+=1/></#if><#if 'createDate' = a.name><#assign createDate=true/></#if></#list>
<#if order>
        if(!ORDERTYPE_ASC.equalsIgnoreCase(orderType)){
            orderType = ORDERTYPE_DESC;
        }
    <#if orderSize gt 1>
        if(null == orderField){
            orderField = CommonConstants.BLANK;
        }
        switch(orderField) {
        <#list columnList as a><#if a.order>
            case "${a.name}" : queryHandler.order("bean.${a.name} " + orderType); break;
        </#if></#list>
            default : queryHandler.order("bean.id " + orderType);
        }
    <#else>
        <#list columnList as a><#if a.order>
        queryHandler.order("bean.${a.name} " + orderType);
        <#break/>
        </#if></#list>
    </#if>
<#else>
        queryHandler.order("bean.id desc");
</#if>
<#macro condition a><#if a.or>(</#if><#list a.nameList as n>bean.${n} <#if "String"=a.type&&a.like>like<#else>=</#if> :${a.name}<#sep> or </#sep></#list><#if a.or>)</#if></#macro>