PublicCMS 使用了FreeMarker 2.3.23,SpringMVC 4.2.0.RELEASE,Hibernate 4.3.11.Final,hibernate-search5.3.0.Final等技术及框架,工程编译等工作由Gradle处理，运行部署需要jdk1.7+,tomcat7.0+/jetty8.0+,nginx/apache,mysql5.0+

该软件永久开源免费(MIT 授权协议)，授权协议请阅读 PublicCMS-admin/readme.txt

PublicCMS-core为JAVA工程，也是PublicCMS的核心工程

PublicCMS-admin为JAVA Web工程，其中包含了管理后台和前台站点的动态部分

nginx-conf为PublicCMS的nginx配置文件，该文件为本开源项目 http://www.publiccms.com/ 官网使用的配置文件副本

data\www\publiccms.com为PublicCMS的 数据目录其中：static子目录为静态化页面文件存储目录，upload上传文件与站点其他静态资源存储目录，template为静态化页面模板文件存储目录，data\indexes子目录为Hibernate Search/lucene索引文件目录，data\pages子目录为PublicCMS推荐位数据存储目录

以上目录路径可以通过同时修改PublicCMS-admin\src\config\properties\other.properties、nginx配置文件 改变

PublicCMS-core/Database Init.sql为PublicCMS项目数据库初始化脚本，请根据实际情况修改数据库配置文件PublicCMS-admin\src\config\properties\dbconfig.properties

PublicCMS-core\Code Formatters 目录中包含eclipse开发工具的js和java代码格式化文件

gradle的使用请借助搜索引擎,如果您没有使用过gradle，并且不打算使用它，也可以在https://github.com/sanluan/PublicCMS-lib 这里下载PublicCMS依赖的库，其中PublicCMS-core-1.0.jar是PublicCMS-core工程编译发布的

官方演示站点：http://www.publiccms.com

作者根据地：http://www.sanluan.com