#PublicCMS

<a target="_blank" href="http://shang.qq.com/wpa/qunwpa?idkey=8a633f84fb2475068182d3c447319977faca6a14dc3acf8017a160d65962a175"><img border="0" src="http://pub.idqqimg.com/wpa/images/group.png" alt="Public CMS" title="Public CMS"/></a>

##简介

PublicCMS是采用2016年最新主流技术开发的免费开源JAVACMS系统。商用免费，架构科学。无需任何数据库优化，即可支持上千万数据；支持全站静态化，动态页面缓存，SSI，0xml配置，扩展指令自动加载等为您快速建站，建设大规模站点提供强大驱动，也是企业级项目产品原型的良好选择。

##获取可运行程序

http://git.oschina.net/sanluan/PublicCMS-war

https://github.com/sanluan/PublicCMS-war

##参与研发(预览版)

http://git.oschina.net/sanluan/PublicCMS-preview

https://github.com/sanluan/PublicCMS-preview

##相关下载及文档(知识库)

https://github.com/sanluan/PublicCMS-lib

https://git.oschina.net/sanluan/PublicCMS-lib

##授权

该软件永久开源免费(MIT 授权协议)

##结构说明

* publiccms-by-gradle 为Gradle管理的工程，publiccms-by-maven为Maven管理的工程，两个工程中源码是一样的
* data/publiccms 为PublicCMS的 数据目录
* database/Database Init.sql 为数据库初始化脚本，Database Change Log.sql 为数据库变更记录

##部署运行

![](preview/rt.jpg)
* 根据文档部署工程
* 数据脚本内置管理员账号admin，密码admin

##演示

* 演示站点：http://www.publiccms.com/
* 动态站点演示：http://cms.publiccms.com/
* 后台演示：http://cms.publiccms.com/admin/ 账号/密码 test/test
* 接口演示：http://cms.publiccms.com/interface.html

##更新记录

V2016.0510更新：

1. 动态站点新增分类保存500错误bug修复
1. 分类类型不能删除bug修复
1. 新增分类推荐，页面推荐
1. RenderHandler新增char类型处理

V2016.0423更新

1. 前台站点增加登陆注册功能，友情链接提交功能
1. 网站模板取消使用SSI与不使用SSI的差异化
1. 后台删除模板时元数据未被删除bug修复
1. 后台任务计划生成静态化失败bug修复

V2016新增功能：

1. 新增动态模板在线编辑功能
1. 新增内容推荐
1. 新增内容附件列表
1. 新增内容移动功能
1. 新增内容刷新功能
1. 新增分类移动功能
1. 新增分类生成多页功能
1. 新增分类标签类型管理
1. 新增分类SEO优化设置
1. 新增页面元数据扩展功能
1. 新增支持FreeMarker与HTML语法混合的模板编辑器
1. 新增动态页面管理功能
1. 新增推荐位数据扩展功能
1. 新增动态模板可接受参数配置功能
1. 新增用户登录授权管理功能
1. 新增部门数据权限功能：页面权限，分类权限
1. 新增角色只读权功能，修复权限授权bug
1. 新FTP服务、FTP用户在线管理功能
1. 新增动态域名绑定管理功能
1. 新增站点管理功能
1. 新增分类，模块等排序功能
1. 新增应用授权功能
1. 新增定制接口及测试页面
1. 新增客户端管理

优化修改:

1. 任务计划脚本改为文件
1. 推荐位数据改为数据库存储
1. UI列表样式修改，性能优化
1. UI美观度提升，图标优化
1. 模板与工程彻底分离
1. 登陆超时改为弹出登陆对话框
1. 指令简化

##系统后台截图

###工作台

![](preview/1.jpg)
###内容列表

![](preview/2.jpg)
###内容管理

![](preview/3.jpg)
###内容编辑

编辑器为百度编辑器，支持定时发布等功能
![](preview/4.jpg)
###图集编辑

![](preview/5.jpg)
###分类编辑

分类支持无限自定义字段扩展

![](preview/6.jpg)
###页面元数据管理

![](preview/7.jpg)
###页面推荐位管理

![](preview/8.jpg)
###页面推荐位编辑

![](preview/9.jpg)
###模板在线编辑

通过模板的include和SSI(服务器端包含)实现模板片段，页面片段的最大程度复用，模板编辑器支持FreeMarker语法与html混合高亮显示
![](preview/10.jpg)
###模板元数据管理

![](preview/11.jpg)
###部门编辑

![](preview/12.jpg)
###角色编辑

![](preview/13.jpg)
###内容模型编辑

![](preview/14.jpg)
###任务计划编辑

![](preview/15.jpg)
###任务计划日志查看

![](preview/16.jpg)
###日志管理

![](preview/17.jpg)