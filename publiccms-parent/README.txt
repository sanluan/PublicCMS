数据库初始化脚本位置:
publiccms-parent\publiccms-core\src\main\resources\initialization\sql\initDatabase.sql
但是您完全不需要手动执行数据库初始化脚本，直接启动产品，在页面中配置和初始化数据库就可以了

maven(使用以下文件：.mvn,pom.xml,mvnw,mvnw.cmd)

已安装maven完整命令行为  mvn clean
未安装maven完整命令行为 mvnw clean

其中clean可以替换为以下任务：
清空 ：clean
打包 ：package
安装 ：install
清空eclipse配置 ：eclipse:clean
配置eclipse工程 ：eclipse:eclipse
清空myeclipse配置 ：eclipse:myeclipse-clean
配置myeclipse工程 ：eclipse:myeclipse
清空idea配置 ：idea:clean
配置idea工程 ：idea:idea
运行工程：spring-boot:run
检查版本更新：versions:display-dependency-updates

gradle(使用以下文件：gradle,build.gradle,settings.gradle,gradlew,gradlew.bat)

已安装gradle完整命令行为  gradle clean
未安装gradle完整命令行为 gradlew clean

其中clean可以替换为以下任务：
清空 ：clean
打包 ：war
清空eclipse配置 ：cleanEclipse
配置eclipse工程 ：eclipse (在eclipse中运行时，请保持publiccms-parent的子工程关闭)
清空idea配置 ：cleanIdea
配置idea工程 ：idea