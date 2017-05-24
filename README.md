# PublicCMS 2017

<a target="_blank" href="//shang.qq.com/wpa/qunwpa?idkey=89ffe8cd3abc04f6794965a330b0a278fdbc31f53e46fd5ee1c4f54ed43a6b28"><img border="0" src="//pub.idqqimg.com/wpa/images/group.png" alt="Public CMS-开源JAVACMS 1群" title="Public CMS-开源JAVACMS 1群"></a> <a target="_blank" href="//shang.qq.com/wpa/qunwpa?idkey=088c921c4eb74328eef0192bac1e63c7228eb31b0524a373d40cdd907ddd2d3c"><img border="0" src="//pub.idqqimg.com/wpa/images/group.png" alt="Public CMS-开源JAVACMS 2群" title="Public CMS-开源JAVACMS 2群"></a>

## 简介

PublicCMS是采用2017年最新主流技术开发的免费开源JAVACMS系统。架构科学，轻松支持上千万数据、千万PV；支持全站静态化，SSI，动态页面局部静态化等为您快速建站，建设大规模站点提供强大驱动，也是企业级项目产品原型的良好选择。

## 获取可运行程序

https://git.oschina.net/sanluan/PublicCMS-war
https://github.com/sanluan/PublicCMS-war

## 获取稳定版源码

https://git.oschina.net/sanluan/PublicCMS
https://github.com/sanluan/PublicCMS
https://code.csdn.net/zyyy358/publiccms

## 参与研发(预览版)

https://git.oschina.net/sanluan/PublicCMS-preview
https://github.com/sanluan/PublicCMS-preview

## 相关下载及文档(知识库)

https://github.com/sanluan/PublicCMS-lib
https://git.oschina.net/sanluan/PublicCMS-lib

## 授权

该软件永久开源免费(MIT 授权协议)

## 目录说明

* data\publiccms	PublicCMS数据目录
* doc			文档
* publiccms		工程源码
* publiccms-gradle	Gradle配置文件
* publiccms-maven	Maven配置文件
* LISCENSE		授权文件
* README.md		说明
* Update History.md	更新历史

## 部署运行

* 根据文档部署工程
* 数据脚本内置管理员账号admin，密码admin

## 演示

* 演示站点：https://www.publiccms.com/
* 后台演示：https://cms.publiccms.com/admin/ 演示账号/密码 test/test
* 接口演示：https://cms.publiccms.com/interface.html

## 更新记录

### 2017.0520


框架升级:

1. Freemarker 2.3.26-incubating
1. Jackson 2.8.8
1. Spring 4.3.8.RELEASE
1. Spring Boot 1.5.3.RELEASE
1. Quartz 2.3.0
1. lucene 5.5.4
1. Hibernate 5.1.6.Final
1. Hibernate Search 5.6.1.Final
1. Mybatis 3.4.4
1. C3p0 0.9.5.2

BUG修复:

1. CMS部署路径有空格时配置错误
1. 导入数据库脚本乱码
1. 内容列表没有子内容模型新建按钮
1. 网站文件列表中查看文件路径错误
1. 高版本Mysql打印警告信息修复
1. 推荐位匿名投稿错误
1. 删除分类后父分类childIds不为空错误
1. 模板帮助getDateNumber错误
1. 分类类型删除提示有分类已使用错误
1. Chrome浏览器上传文件按钮反应延迟
1. 500错误页面报错信息不支持Throwable类型
1. 我的登陆日志不能过滤成功登陆类型

新增功能:

1. 前台推荐位投稿管理
1. 内容投稿
1. 内容投稿模板帮助
1. 启动命令指定数据目录位置

其他提升:

1. 删除cms_content_tag表及相关代码
1. 界面修改、字体行距等调大
1. 将数据库配置文件位置改为数据目录下
1. 将包名改为org.publiccms,将com.sanluan包独立到publiccms-common工程
1. 统计代码清理
1. 取消cms.autoInstall配置文件项
1. 将数据库配置文件改到数据目录中