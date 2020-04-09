${"<@_"+entityName?replace('Cms','')?uncap_first+"List"} <#include "../include_condition/parameter.ftl">>
<div class="pageHeader">
    <form class="pagerForm" autocomplete="off" onsubmit="return navTabSearch(this);" method="post">
        <#noparse><#include "../include_page/parameters.html"/></#noparse>
        <div class="searchBar">
            <ul class="searchContent">
            <#list conditionList as a><#if "Date"=a.type>
                <li class="colspan">
                    <label>${a.title}:</label>
                    <input type="text" name="start${a.name?cap_first}" class="date" size="10" dateFmt="yyyy-MM-dd" maxDate="{%y}-%M-{%d}" value="${r"${start"+a.name?cap_first+"!}"}" />
                    <span>-</span>
                    <input type="text" name="end${a.name?cap_first}" class="date" size="10" dateFmt="yyyy-MM-dd" maxDate="{%y}-%M-{%d}" value="${r"${end"+a.name?cap_first+"!}"}" />
                </li>
                <#elseif "siteId"!=a.name>
                <li>
                    <label>${a.title}:</label>
                    <input type="text" name="${a.name}" value="${r"${"+a.name+"!}"}" />
                </li>
                </#if></#list>
            </ul>
            <div class="subBar">
                <ul>
                    <li>
                    	<button type="submit" class="buttonActive"><#noparse><@t.page 'button.search'/></#noparse></button>
                    </li>
                </ul>
            </div>
        </div>
    </form>
</div>
<#noparse><@_sysAuthorized roleIds=admin.roles urls='</#noparse>${entityName?uncap_first}/add,${entityName?uncap_first}/delete<#noparse>'><#assign authorizedMap=map/></@_sysAuthorized></#noparse>
<div class="pageContent">
    <div class="panelBar">
        <ul class="toolBar">
        ${'<#'}if authorizedMap['${entityName?uncap_first}/add']>
            <li><a href="${entityName?uncap_first}/add.html" target="navTab" rel="${entityName?uncap_first}/add"><i class="icon-plus-sign-alt icon-large"></i> <#noparse><@t.page 'button.add'/></#noparse></a></li>
            <li><a href="${entityName?uncap_first}/add.html?id={sid}" target="navTab" rel="${entityName?uncap_first}/edit"><i class="icon-edit icon-large"></i> <#noparse><@t.page 'button.edit'/></#noparse></a></li>
        ${'</#'}if>
        ${'<#'}if authorizedMap['${entityName?uncap_first}/delete']>
            <li><a href="${entityName?uncap_first}/delete.do?_csrf=<#noparse><@_csrfToken admin=true/></#noparse>" title="<#noparse><@t.page 'confirm.batch_delete'/></#noparse>" target="selectedTodo" rel="ids"><i class="icon-trash icon-large"></i> <#noparse><@t.page 'button.batch_delete'/></#noparse></a></li>
        ${'</#'}if>
        </ul>
    </div>
    <table class="list" width="100%" layoutH="92">
        <thead>
            <tr>
                <th width="20"><input type="checkbox" group="ids" class="checkboxCtrl"></th>
                <#list columnList as a>
                <th<#if a.order> orderField="${a.name}" class="<#noparse><#if orderField??&&</#noparse>'${a.name}'<#noparse>==orderField><#if 'asc'=orderType>asc<#else>desc</#if><#else>order</#if></#noparse>"</#if>>${a.title}</th>
                </#list>
                <th><#noparse><@t.page 'operate'/></#noparse></th>
            </tr>
        </thead>
        <tbody>
            <#noparse><#list page.list as a>
            <tr target="sid" rel="${a.id}">
                <td class="center"><input name="ids" value="${a.id}" type="checkbox"></td>
            </#noparse>
                <#list columnList as a>
                <td>${r"${a."+a.name+"!}"}</td>
                </#list>
                <td>
                ${'<#'}if authorizedMap['${entityName?uncap_first}/delete']>
                    <a href="${entityName?uncap_first}/delete.do?ids=<#noparse>${a.id}</#noparse>&_csrf=<#noparse><@_csrfToken admin=true/></#noparse>" title="<#noparse><@t.page 'confirm.delete'/></#noparse>"  target="ajaxTodo"><#noparse><@t.page 'button.delete'/></#noparse></a>
                ${'</#'}if>
                </td>
            <#noparse>
            </tr>
            </#list></#noparse>
        </tbody>
    </table>
    <#noparse><#include "../include_page/page.html"/></#noparse>
</div>
${"</@_"+entityName?replace('Cms','')?uncap_first+"List>"}