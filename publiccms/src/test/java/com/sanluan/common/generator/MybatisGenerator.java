package com.sanluan.common.generator;

import java.util.ArrayList;
import java.util.List;

import org.mybatis.generator.api.MyBatisGenerator;
import org.mybatis.generator.config.Configuration;
import org.mybatis.generator.config.xml.ConfigurationParser;
import org.mybatis.generator.internal.DefaultShellCallback;

/**
 * Created by xinlu on 2017/1/8.
 */
public class MybatisGenerator {
    public static void main(String[] arg) throws Throwable {
        List<String> warnings = new ArrayList<String>();
        ConfigurationParser cp = new ConfigurationParser(warnings);
        Configuration config = cp.parseConfiguration(MybatisGenerator.class.getResourceAsStream("mybatis/generatorConfig.xml"));
        DefaultShellCallback shellCallback = new DefaultShellCallback(true);
        MyBatisGenerator myBatisGenerator = new MyBatisGenerator(config, shellCallback, warnings);
        myBatisGenerator.generate(null);
    }
}
