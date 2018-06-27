[<@_categoryList parentId=parentId allowContribute=allowContribute pageIndex=pageIndex count=count>
    <#list page.list as a>
        {
            name:"${a.name?json_string}",
            url:"${a.url!}",
            code:"${a.code}",
            pageSize:${a.pageSize}
        }<#sep>,
    </#list>
</@_categoryList>]