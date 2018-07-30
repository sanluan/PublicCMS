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

import com.publiccms.common.constants.CommonConstants;

/**
 *
 * ServicePlugin
 * 
 */
public class ServicePlugin extends PluginAdapter {

    private List<TopLevelClass> services = new ArrayList<>();

    /**
     * This plugin is always valid - no properties are required
     */
    @Override
    public boolean validate(List<String> warnings) {
        return true;
    }

    @Override
    public boolean clientGenerated(Interface interfaze, TopLevelClass topLevelClass, IntrospectedTable introspectedTable) {
        String packageName = interfaze.getType().getPackageName();
        String shortName = interfaze.getType().getShortName();
        String mapperName = interfaze.getType().getFullyQualifiedName();
        if (shortName.endsWith("Mapper")) {
            int idx = packageName.lastIndexOf(CommonConstants.DOT);
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
                topLevelClazz.addFileCommentLine(CommonConstants.BLANK);
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
                if (it.getName().endsWith("WithRowbounds")) {
                    m.setName(it.getName().replace("WithRowbounds", "WithPage"));
                    FullyQualifiedJavaType pageHandler = new FullyQualifiedJavaType("com.publiccms.common.handler.PageHandler");
                    m.setReturnType(pageHandler);
                    topLevelClazz.addImportedType(pageHandler);
                    m.addBodyLine(
                            "PageHandler page = new PageHandler(pageIndex, pageSize, mapper.countByExample(example), null);");
                    StringBuilder bodyline = new StringBuilder();
                    bodyline.append("page.setList(mapper.");
                    bodyline.append(it.getName());
                    bodyline.append("(");
                    List<Parameter> params = it.getParameters();
                    List<String> paramTxt = new ArrayList<>();
                    for (Parameter p : params) {
                        FullyQualifiedJavaType t = p.getType();
                        topLevelClazz.addImportedType(t);
                        if ("RowBounds".equals(t.getShortNameWithoutTypeArguments())) {
                            m.addParameter(new Parameter(new FullyQualifiedJavaType("long"), "pageIndex"));
                            m.addParameter(new Parameter(FullyQualifiedJavaType.getIntInstance(), "pageSize"));
                            paramTxt.add("new RowBounds((pageIndex - 1) * pageSize, pageSize)");
                        } else {
                            Parameter tmp = new Parameter(t, p.getName());
                            m.addParameter(tmp);
                            paramTxt.add(tmp.getName());
                        }
                    }
                    bodyline.append(StringUtils.join(paramTxt, ", "));
                    bodyline.append("));");
                    m.addBodyLine(bodyline.toString());
                    m.addBodyLine("return page;");
                } else {
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
                    bodyline += StringUtils.join(paramTxt, CommonConstants.COMMA_DELIMITED);
                    bodyline += ");";
                    m.addBodyLine(bodyline);
                }
                topLevelClazz.addMethod(m);
            }

            services.add(topLevelClazz);
        }
        return true;
    }

    @Override
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

    @Override
    public List<GeneratedJavaFile> contextGenerateAdditionalJavaFiles(IntrospectedTable introspectedTable) {
        return contextGenerateAdditionalJavaFiles();
    }
}
