${"<@_"+entityName?replace('Cms','')?uncap_first+" id=id><#assign a=object/></@_"+entityName?replace('Cms','')?uncap_first+">"}
<form method="post" autocomplete="off" action="${entityName?uncap_first}/save.do?callbackType=closeCurrent&navTabId=${entityName?uncap_first}/list" class="pageForm required-validate" onsubmit="return validateCallback(this, navTabAjaxDone);">
    <input name="id" type="hidden" value="<#noparse>${id!}</#noparse>" />
    <input type="hidden" name="_csrf" value="<#noparse><@_csrfToken admin=true/></#noparse>"/>
    <div class="formBar">
        <ul>
            <li><button type="submit" class="buttonActive"><#noparse><@t.page 'button.save'/></#noparse></button></li>
            <li><button type="button" class="button close"><#noparse><@t.page 'button.close'/></#noparse></button></li>
        </ul>
    </div>
    <div class="pageContent">
        <div class="pageFormContent" layoutH="56">
        <#list columnList as a>
            <#if "Date"=a.type>
            <dl>
                <dt>${a.title}:</dt>
                <dd>
                    <input type="text" name="${a.name}" class="required date" size="20" dateFmt="yyyy-MM-dd HH:mm:ss" value="${r"${(a."+a.name+")!}"}"/>
                </dd>
            </dl>
            <#elseif 'id' != a.name && 'siteId' != a.name && 'createDate' != a.name&& 'disabled' != a.name>
            <dl>
                <dt>${a.title}:</dt>
                <dd><input class="required" name="${a.name}" type="text" size="30" value="${r"${(a."+a.name+")!}"}"/></dd>
            </dl>
            </#if>
        </#list>
        </div>
    </div>
</form>