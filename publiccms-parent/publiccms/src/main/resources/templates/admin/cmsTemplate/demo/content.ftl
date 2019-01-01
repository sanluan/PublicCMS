{<@_content id=id containsAttribute=true>
    <@_category id=object.categoryId>
        <#assign category=object/>
    </@_category>
    "id":"${object.id}",
    "title":"${object.title?json_string}",
    "url":"${object.url!}",
    "description":"${(object.description?json_string)!}",
    "categoryId":"${object.categoryId}",
    "categoryTitle":"${category.name?json_string}",
    "categoryUrl":"${category.url!}",
    "editor":"${object.editor}",
    "publishDate":"${object.publishDate}",
    "text":"${(attribute.text?json_string?no_esc)!}",
    "sourceUrl":"${(attribute.sourceUrl)!}",
    "source":"${(attribute.source)!}"<#if object.hasFiles>
    ,"files:[<@_contentFileList contentId=object.id fileTypes='video,audio,other'><#list page.list as file>
        {"filePath":"${(file.filePath)!}"}<#sep>,</#list></@_contentFileList>
    ]</#if><#if object.hasImages>
    ,"images":[<@_contentFileList contentId=object.id fileTypes='image'><#list page.list as file>
        {"filePath":"${(file.filePath)!}"}<#sep>,</#list></@_contentFileList>
    ]</#if>
</@_content>
}