package com.publiccms.common.generator;

import javax.sql.DataSource;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;

import cn.smallbun.screw.core.Configuration;
import cn.smallbun.screw.core.engine.EngineConfig;
import cn.smallbun.screw.core.engine.EngineFileType;
import cn.smallbun.screw.core.engine.EngineTemplateType;
import cn.smallbun.screw.core.execute.DocumentationExecute;
import cn.smallbun.screw.core.process.ProcessConfig;

public class DataDictionaryGenerator {

    public static void main(String[] args) {
        // 数据源
        HikariConfig hikariConfig = new HikariConfig();
        hikariConfig.setDriverClassName("com.mysql.cj.jdbc.Driver");
        hikariConfig.setJdbcUrl("jdbc:mysql://127.0.0.1:3306/publiccms?serverTimezone=GMT%2B08");
        hikariConfig.setUsername("root");
        hikariConfig.setPassword("11111111");
        // 设置可以获取tables remarks信息
        hikariConfig.addDataSourceProperty("useInformationSchema", "true");
        hikariConfig.setMinimumIdle(2);
        hikariConfig.setMaximumPoolSize(5);
        DataSource dataSource = new HikariDataSource(hikariConfig);
        // 生成配置
        EngineConfig engineConfig = EngineConfig.builder()
                .fileOutputDir("../../doc/")
                .openOutputDir(true)
                .fileType(EngineFileType.HTML)
                .produceType(EngineTemplateType.freemarker).build();
        ProcessConfig processConfig = ProcessConfig.builder().build();

        // 配置
        Configuration config = Configuration.builder().version("202406").description("Data Dictionary").dataSource(dataSource)
                .engineConfig(engineConfig).produceConfig(processConfig).build();
        // 执行生成
        new DocumentationExecute(config).execute();

    }

}
