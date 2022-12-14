${"<@_"+entityName?replace('Cms','')?uncap_first+"List"} <#include "../include_condition/parameter.ftl">>
<div class="pageHeader card">
    <form class="pagerForm" autocomplete="off" onsubmit="return navTabSearch(this);" method="post">
        <#noparse><#include "../include_page/parameters.html"/></#noparse>
        <div class="searchBar">
            <ul class="searchContent">
            <#list conditionList as a><#if "Date"=a.type>
                <li>
                    <label>${a.title}:</label>
                    <input type="text" name="start${a.name?cap_first}" class="date" size="10" dateFmt="yyyy-MM-dd" maxDate="{%y}-%M-{%d}" value="${r"${start"+a.name?cap_first+"!}"}"/>
                    <a class="inputDateButton" href="javascript:void(0);"></a>
                    <span>-</span>
                    <input type="text" name="end${a.name?cap_first}" class="date" size="10" dateFmt="yyyy-MM-dd" maxDate="{%y}-%M-{%d}" value="${r"${end"+a.name?cap_first+"!}"}"/>
                    <a class="inputDateButton" href="javascript:void(0);"></a>
                </li>
                <#elseif "siteId"!=a.name>
                <li>
                    <label>${a.title}:</label>
                    <input type="text" name="${a.name}" value="${r"${"+a.name+"!}"}"/>
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
<div class="pageContent card">
    <div class="panelBar">
        <ul class="toolBar">
        ${'<#'}if authorizedMap['${entityName?uncap_first}/add']>
            <li><a href="${entityName?uncap_first}/add.html" target="navTab" rel="${entityName?uncap_first}/add"><i class="icon-plus-sign-alt icon-large"></i> <#noparse><@t.page 'button.add'/></#noparse></a></li>
        ${'</#'}if>
        ${'<#'}if authorizedMap['${entityName?uncap_first}/delete']>
            <li><a href="${entityName?uncap_first}/delete?_csrf=<#noparse><@_csrfToken admin=true/></#noparse>" title="<#noparse><@t.page 'confirm.batch_delete'/></#noparse>" target="selectedTodo" rel="ids"><i class="icon-trash icon-large"></i> <#noparse><@t.page 'button.batch_delete'/></#noparse></a></li>
        ${'</#'}if>
        </ul>
    </div>
    <table class="table" layoutH orderField="${orderField!}" orderType="${orderType!}">
        <thead>
            <tr>
                <th width="20"><input type="checkbox" group="ids" class="checkboxCtrl"></th>
                <#list columnList as a>
                <th<#if a.order> orderField="${a.name}">${a.title}</th>
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
                ${'<#'}if authorizedMap['${entityName?uncap_first}/add']>
                    <a href="${entityName?uncap_first}/add.html?id=<#noparse>${a.id}</#noparse>" class="edit btnText blue" target="navTab" rel="${entityName?uncap_first}/edit"><i class="icon-edit"></i> <#noparse><@t.page 'button.edit'/></#noparse></a>
                ${'</#'}if>
                ${'<#'}if authorizedMap['${entityName?uncap_first}/delete']>
                    <a href="${entityName?uncap_first}/delete?ids=<#noparse>${a.id}</#noparse>&_csrf=<#noparse><@_csrfToken admin=true/></#noparse>" class="btnText warn" title="<#noparse><@t.page 'confirm.delete'/></#noparse>"  target="ajaxTodo"><i class="icon-trash"></i> <#noparse><@t.page 'button.delete'/></#noparse></a>
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