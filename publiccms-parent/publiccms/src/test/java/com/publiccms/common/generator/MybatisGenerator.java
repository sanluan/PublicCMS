package com.publiccms.common.generator;

import org.mybatis.generator.api.MyBatisGenerator;
import org.mybatis.generator.api.VerboseProgressCallback;
import org.mybatis.generator.config.Configuration;
import org.mybatis.generator.config.xml.ConfigurationParser;
import org.mybatis.generator.internal.DefaultShellCallback;

/**
 * Created by xinlu on 2017/1/8.
 */
public class MybatisGenerator {

    /**
     * @param arg
     * @throws Throwable
     */
    public static void main(String[] arg) throws Throwable {
        ConfigurationParser cp = new ConfigurationParser();
        Configuration config = cp
                .parseConfiguration(MybatisGenerator.class.getResourceAsStream("/generator/mybatis/generatorConfig.xml"));
        DefaultShellCallback shellCallback = new DefaultShellCallback();
        MyBatisGenerator.Builder builder = new MyBatisGenerator.Builder();
        builder.withConfiguration(config).withShellCallback(shellCallback).withProgressCallback(new VerboseProgressCallback());
        MyBatisGenerator myBatisGenerator = builder.build();
        myBatisGenerator.generateAndWrite();
    }
}
