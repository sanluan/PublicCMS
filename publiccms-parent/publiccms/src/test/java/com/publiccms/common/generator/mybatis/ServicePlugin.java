/*
 * @Author: xinlu
 * @Email: 442559691@qq.com
 */

package com.publiccms.common.generator.mybatis;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.mybatis.generator.api.GeneratedJavaFile;
import org.mybatis.generator.api.IntrospectedTable;
import org.mybatis.generator.api.PluginAdapter;
import org.mybatis.generator.api.dom.java.Field;
import org.mybatis.generator.api.dom.java.FullyQualifiedJavaType;
import org.mybatis.generator.api.dom.java.Interface;
import org.mybatis.generator.api.dom.java.JavaVisibility;
import org.mybatis.generator.api.dom.java.Method;
import org.mybatis.generator.api.dom.java.Parameter;
import org.mybatis.generator.api.dom.java.TopLevelClass;
import org.mybatis.generator.config.PropertyRegistry;

/**
 *
 * ServicePlugin
 * 
 */
public class ServicePlugin extends PluginAdapter{
    
    private List<TopLevelClass> services = new ArrayList<>();

    /**
     * This plugin is always valid - no properties are required
     */
    public boolean validate(List<String> warnings) {
        return true;
    }

    public boolean clientGenerated(Interface interfaze, TopLevelClass topLevelClass, IntrospectedTable introspectedTable) {
        String packageName = interfaze.getType().getPackageName();
        String shortName = interfaze.getType().getShortName();
        String mapperName = interfaze.getType().getFullyQualifiedName();
        int idx = shortName.lastIndexOf("Mapper");
        if (shortName.endsWith("Mapper")) {
            idx = packageName.lastIndexOf(".");
            if (idx < 0) {
                idx = packageName.length();
            }
            String servicePackage = packageName.substring(0, idx);
            String serviceName = shortName.substring(0, shortName.length() - 6);
            serviceName += "Service";
            String fullName = servicePackage + ".service." + serviceName;
            TopLevelClass topLevelClazz = new TopLevelClass(fullName);
            topLevelClazz.setVisibility(JavaVisibility.PUBLIC);
            topLevelClazz.addImportedType(mapperName);
            topLevelClazz.addImportedType("org.springframework.stereotype.Service");

            {
                // 增加备注信息
                topLevelClazz.addFileCommentLine("/*");
                topLevelClazz.addFileCommentLine("    this file generator by ServicePlugin");
                topLevelClazz.addFileCommentLine("    @Author: xinlu");
                topLevelClazz.addFileCommentLine("    @Email: 442559691@qq.com");
                topLevelClazz.addFileCommentLine("*/");
                topLevelClazz.addFileCommentLine("");
            }
            topLevelClazz.addAnnotation("@Service");
            topLevelClazz.addAnnotation("@Transactional");
            topLevelClazz.addImportedType("org.springframework.transaction.annotation.Transactional");
            {
                Field field = new Field();
                field.setVisibility(JavaVisibility.PRIVATE);
                field.setType(new FullyQualifiedJavaType(mapperName));
                field.setName("mapper");
                field.addAnnotation("@Autowired");
                topLevelClazz.addImportedType("org.springframework.beans.factory.annotation.Autowired");
                topLevelClazz.addField(field);
            }
            List<Method> methods = interfaze.getMethods();

            for (Method it : methods) {
                Method m = new Method();
                m.setVisibility(JavaVisibility.PUBLIC);
                m.setName(it.getName());
                m.setReturnType(it.getReturnType());
                topLevelClazz.addImportedType(it.getReturnType());
                String bodyline = "return mapper.";
                bodyline += m.getName();
                bodyline += "(";
                List<Parameter> params = it.getParameters();
                List<String> paramTxt = new ArrayList<>();
                for (Parameter p : params) {
                    FullyQualifiedJavaType t = p.getType();
                    topLevelClazz.addImportedType(t);
                    Parameter tmp = new Parameter(t, p.getName());
                    m.addParameter(tmp);
                    paramTxt.add(tmp.getName());
                }
                bodyline += StringUtils.join(paramTxt, ",");
                bodyline += ");";
                m.addBodyLine(bodyline);
                topLevelClazz.addMethod(m);
            }
            {
                Method m = new Method();
                m.setVisibility(JavaVisibility.PUBLIC);
                m.setName("setDataSourceName");
                m.addParameter(new Parameter(new FullyQualifiedJavaType("String"), "dataSourceName"));
                m.addBodyLine("MultiDataSource.setDataSourceName(dataSourceName);");
                topLevelClazz.addMethod(m);
            }
            {
                Method m = new Method();
                m.setVisibility(JavaVisibility.PUBLIC);
                m.setName("setDefaultDataSource");
                m.addBodyLine("MultiDataSource.resetDataSourceName();");
                topLevelClazz.addMethod(m);
            }
            topLevelClazz.addImportedType("com.publiccms.common.datasource.MultiDataSource");

            services.add(topLevelClazz);
        }
        return true;
    }

    public List<GeneratedJavaFile> contextGenerateAdditionalJavaFiles() {
        List<GeneratedJavaFile> listGjf = new ArrayList<>();
        for (TopLevelClass topLevelClazz : services) {
            GeneratedJavaFile gjf = new GeneratedJavaFile(topLevelClazz,
                    context.getJavaClientGeneratorConfiguration().getTargetProject(),
                    context.getProperty(PropertyRegistry.CONTEXT_JAVA_FILE_ENCODING), context.getJavaFormatter());
            listGjf.add(gjf);
        }
        return listGjf;
    }

    public List<GeneratedJavaFile> contextGenerateAdditionalJavaFiles(IntrospectedTable introspectedTable) {
        return contextGenerateAdditionalJavaFiles();
    }
}
