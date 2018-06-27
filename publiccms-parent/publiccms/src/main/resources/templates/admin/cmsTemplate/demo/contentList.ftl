[<@_contentList categoryId=categoryId containChild=containChild modelId=modelId parentId=parentId userId=userId orderField=orderField orderType=orderType pageIndex=pageIndex count=count>
    <#list page.list as a>
        {
            title:"${a.title?json_string}",
            url:"${a.url!}",
            description:"${(a.description?json_string)!}",
            publishDate:"${a.publishDate}",
            cover:"${a.cover?has_content?then(site.sitePath+a.cover,'')}"
        }<#sep>,
    </#list>
</@_contentList>]