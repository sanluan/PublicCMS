indexes
	全文搜索索引文件目录

task
	任务计划脚本目录

template
	站点模板目录

web
	静态文件目录

install.lock
	安装锁，参考内容：

2017.0801

database.properties
	数据库配置,参考内容：

jdbc.driverClassName=com.mysql.jdbc.Driver
jdbc.url=jdbc\:mysql\://localhost/public_cms?useUnicode\=true&characterEncoding\=UTF-8&zeroDateTimeBehavior\=round&useSSL\=false
jdbc.username=publiccms
jdbc.password=publiccms_password
cpool.maxIdleTime=25
cpool.checkoutTimeout=10000
cpool.acquireIncrement=10
cpool.autoCommitOnClose=true
cpool.maxIdleTimeExcessConnections=1800
cpool.maxPoolSize=20
cpool.minPoolSize=5
