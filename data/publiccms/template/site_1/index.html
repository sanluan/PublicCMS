<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="utf-8">
    <title>${metadata.alias!}</title>
    <link href="${site.sitePath}assets/style.css" rel="stylesheet"/>
</head>
<body>
    <header>
        <h1>Sites list</h1>
    </header>    
    <main>
<@_sysSiteList pageSize=10>
        <ul>
    <#list page.list?reverse as a><#if a.id!=site.id>
        <li>
            <div class="site-box">
                <h3><a href="${a.dynamicPath!}" target="_blank">${a.name}</a><#if a.useStatic>[Enable static]</#if></h3>
                <div>
                    <p><a href="${a.dynamicPath!}admin/" target="_blank">Management system</a></p>
                    Domains list:
                    <@_sysDomainList advanced=true siteId=a.id>
                        <#list page.list as a>
                    <p>${a.name}<#if a.wild>[Wild domain]</#if> <#if a.path?has_content>Template root path:${(a.path)!}</#if></p>
                        </#list>
                    </@_sysDomainList>                    
                </div>
            </div>
        </li>
    </#if></#list>
        </ul>
</@_sysSiteList>
        <div class="clear"></div>
        <p>The default user name / password for all sites: admin / admin</p>
        <p>The domain name list indicates that you use these domain names to access the program, the corresponding site will be displayed! Wildcard domain name means that all subdomains of this domain name can also access this site。The template root directory means access using this domain name, the template files in this directory are accessed</p>
        <p>The first visit to the static site requires you to log in to the background and perform "重新生成全站" in Maintenance-> System Maintenance-> Task Schedule to browse normally</p>
        <p>For testing purposes, we have pointed dev.publiccms.com and all its subdomains to 127.0.0.1, which can be used to test local services in the same way as localhost and loopback when you are connected to the Internet.</p>
    </main>
    <footer>
        <#if base?has_content>
            <a href="?locale=zh_CN">中文</a>
        </#if>
        Copyright &copy; ${.now?string('yyyy')}
    </footer>
</body>
</html>