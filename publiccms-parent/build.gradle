buildscript {
    repositories {
        mavenCentral()
        maven {
            name = "aliyun"
            url "https://maven.aliyun.com/repository/public/"
        }
    }
    dependencies {
        // https://mvnrepository.com/artifact/org.springframework.boot/spring-boot-gradle-plugin
        classpath("org.springframework.boot:spring-boot-gradle-plugin:2.5.1")
    }
}

plugins {
    id 'idea'
    id 'eclipse'
}

subprojects {
    apply plugin: "java"
    apply plugin: 'idea'
    apply plugin: "eclipse-wtp"

    group = 'com.publiccms'
    version = 'V4.0.202204.b'
    
    test.enabled = false
    
    ext {
        // http://mvnrepository.com/artifact/org.springframework/spring-core
        // https://spring.io/projects/spring-framework
        springVersion = "5.3.19"
        // https://mvnrepository.com/artifact/org.springframework.boot/spring-boot-starter-web
        // https://spring.io/projects/spring-boot
        springBootVersion = "2.6.6"
        // https://mvnrepository.com/artifact/org.aspectj/aspectjweaver
        aspectjVersion = "1.9.7"
        // http://mvnrepository.com/artifact/org.apache.httpcomponents/httpclient
        // http://hc.apache.org/downloads.cgi
        httpclientVersion = "4.5.13"
        // http://mvnrepository.com/artifact/com.fasterxml.jackson.core/jackson-databind
        jacksonVersion = "2.13.2.2"
        // http://mvnrepository.com/artifact/org.hibernate/hibernate-core
        // http://hibernate.org/orm/downloads/
        hibernateVersion = "5.6.7.Final"
        // http://mvnrepository.com/artifact/org.hibernate/hibernate-tools
        hibernateToolsVersion = "5.6.2.Final"
        // https://mvnrepository.com/artifact/org.hibernate.search/hibernate-search-mapper-orm
        // http://hibernate.org/search/downloads/
        hibernateSearchVersion = "6.1.4.Final"
        // https://mvnrepository.com/artifact/org.ehcache/ehcache
        ehcacheVersion = "3.9.9"
        // http://mvnrepository.com/artifact/org.freemarker/freemarker
        freemarkerVersion = "2.3.31"
        // http://mvnrepository.com/artifact/org.mybatis/mybatis
        mybatisVersion = "3.5.9"
        // http://mvnrepository.com/artifact/org.mybatis/mybatis-spring
        mybatisSpringVersion = "2.0.7"
        // http://mvnrepository.com/artifact/org.mybatis.generator/mybatis-generator-core
        mybatisGenerator = "1.4.1"
        // https://mvnrepository.com/artifact/com.github.pagehelper/pagehelper
        pageHelperVersion = "5.3.0"
        // https://mvnrepository.com/artifact/org.mybatis.dynamic-sql/mybatis-dynamic-sql
        dynamicSqlVersion = "1.2.1"
        // https://mvnrepository.com/artifact/org.apache.lucene/lucene-analyzers-smartcn
        luceneVersion = "8.11.1"
        // http://mvnrepository.com/artifact/redis.clients/jedis
        jedisVersion = "4.2.2"
        // http://mvnrepository.com/artifact/org.quartz-scheduler/quartz
        quartzVersion = "2.3.2"
        // https://mvnrepository.com/artifact/com.zaxxer/HikariCP
        hikariCPVersion = "4.0.3"
        // https://mvnrepository.com/artifact/mysql/mysql-connector-java
        mysqlVersion = "8.0.28"
        // https://mvnrepository.com/artifact/org.apache.poi/poi-ooxml
        poiVersion = "4.1.2"
        // http://mvnrepository.com/artifact/commons-fileupload/commons-fileupload
        fileuploadVersion = "1.4"
        // http://mvnrepository.com/artifact/org.apache.commons/commons-collections4
        collectionsVersion = "4.4"
        // http://mvnrepository.com/artifact/org.apache.commons/commons-lang3
        langVersion = "3.12.0"
        // https://mvnrepository.com/artifact/org.apache.commons/commons-text
        textVersion = "1.9"
        // http://mvnrepository.com/artifact/commons-codec/commons-codec
        codecVersion = "1.15"
        // https://mvnrepository.com/artifact/org.apache.logging.log4j/log4j-core
        log4jVersion = "2.17.2"
        // https://mvnrepository.com/artifact/com.drewnoakes/metadata-extractor
        extractorVersion = "2.17.0"
        // https://mvnrepository.com/artifact/org.sejda.imageio/webp-imageio
        webpVersion = "0.1.6"
        // http://mvnrepository.com/artifact/eu.bitwalker/UserAgentUtils
        userAgentUtilsVersion = "1.21"
        // https://mvnrepository.com/artifact/jakarta.annotation/jakarta.annotation-api
        annotationVersion = "1.3.5"
        // https://mvnrepository.com/artifact/jakarta.mail/jakarta.mail-api
        mailVersion = "1.6.7"
        // http://mvnrepository.com/artifact/org.apache.ant/ant
        antVersion = "1.10.12"
        // http://mvnrepository.com/artifact/javax.transaction/jta
        jtaVersion = "1.1"
        // https://mvnrepository.com/artifact/javax.servlet/javax.servlet-api
        servletVersion = "4.0.1"
        // https://mvnrepository.com/artifact/org.junit.jupiter/junit-jupiter-engine
        junitVersion = "5.7.2"
    }
    sourceCompatibility = 1.8
    targetCompatibility = 1.8

    [compileJava]*.options*.encoding = "UTF-8"
    [compileTestJava]*.options*.encoding = "UTF-8"

    repositories {
        mavenLocal()
        mavenCentral()
    }
    eclipse {
        wtp {
            facet {
                facet name: "jst.java", version: "1.8"
            }
        }
    }
}