<#ftl auto_esc=false/>
[<@_contentList categoryId=categoryId containChild=containChild modelId=modelId parentId=parentId userId=userId orderField=orderField orderType=orderType pageIndex=pageIndex pageSize=count>
    <#list page.list as a>
        {
            "title":"${a.title?json_string}",
            "url":"${a.url!}",
            "description":"${(a.description?json_string)!}",
            "publishDate":"${a.publishDate}",
            "cover":"${a.cover!}"
        }<#sep>,
    </#list>
</@_contentList>]