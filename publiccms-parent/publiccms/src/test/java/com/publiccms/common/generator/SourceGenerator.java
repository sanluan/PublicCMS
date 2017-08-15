package com.publiccms.common.generator;

import static com.publiccms.common.tools.FreeMarkerUtils.generateFileByFile;
import static com.publiccms.common.tools.ScanClassUtils.getClasses;
import static org.apache.commons.lang3.StringUtils.isNotBlank;
import static org.apache.commons.lang3.StringUtils.uncapitalize;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.publiccms.common.base.Base;
import com.publiccms.common.generator.annotation.GeneratorColumn;
import com.publiccms.common.generator.entity.EntityColumn;
import com.publiccms.common.generator.entity.EntityCondition;

import freemarker.template.Configuration;
import freemarker.template.TemplateException;

/**
 *
 * SourceMaker 代码生成工具
 *
 */
public class SourceGenerator implements Base {
    /**
     * 
     */
    public static final String ENTITY_BASE_PACKAGE = "entities";
    /**
     * 
     */
    public static final String DAO_BASE_PACKAGE = "logic.dao";
    /**
     * 
     */
    public static final String DAO_SUFFIX = "Dao";
    /**
     * 
     */
    public static final String SERVICE_BASE_PACKAGE = "logic.service";
    /**
     * 
     */
    public static final String SERVICE_SUFFIX = "Service";
    /**
     * 
     */
    public static final String DIRECTIVE_BASE_PACKAGE = "views.directive";
    /**
     * 
     */
    public static final String DIRECTIVE_SUFFIX = "Directive";
    /**
     * 
     */
    public static final String CONTROLLER_BASE_PACKAGE = "controller.admin";
    /**
     * 
     */
    public static final String CONTROLLER_SUFFIX = "AdminController";
    /**
     * 
     */
    public static final String JAVA_BASE_PATH = "src/test/java/";
    /**
     * 
     */
    public static final String WEB_BASE_PATH = "src/test/main/resources/";
    /**
     * 
     */
    public static final String TEMPLATE_BASE_PATH = WEB_BASE_PATH + "templates/";

    /**
     * @param arg
     * @throws ClassNotFoundException
     * @throws IOException
     */
    public static void main(String[] arg) throws ClassNotFoundException, IOException {
        SourceGenerator sourceGenerator= new SourceGenerator();
        boolean overwrite = false;// 是否覆盖已有代码
        String basePackage = "org.publiccms";// 基础包名
        // 生成所有实体类的代码
        // sourceMaker.generate(basePackage, overwrite);
        // 生成某个包所有实体类的代码
        // sourceMaker.generate(basePackage, "trade", overwrite);
        // 生成某个实体类的代码
        sourceGenerator.generate(Class.forName("org.publiccms.entities.cms.CmsDictionaryData"), basePackage, overwrite);
    }

    /**
     * 生成所有实体类的代码
     *
     * @param basePackage
     * @param overwrite
     * @throws ClassNotFoundException
     * @throws IOException
     */
    public void generate(String basePackage, boolean overwrite) throws ClassNotFoundException, IOException {
        String entitiesFullPackage = basePackage + DOT + ENTITY_BASE_PACKAGE;
        for (Class<?> c : getClasses(new String[] { entitiesFullPackage })) {
            generate(c, basePackage, overwrite);
        }
    }

    /**
     * 生成某个包所有实体类的代码
     *
     * @param basePackage
     * @param entityPackage
     * @param overwrite
     * @throws IOException
     * @throws ClassNotFoundException
     */
    public void generate(String basePackage, String entityPackage, boolean overwrite) throws ClassNotFoundException, IOException {
        String entitiesFullPackage = basePackage + DOT + ENTITY_BASE_PACKAGE + DOT + entityPackage;
        for (Class<?> c : getClasses(new String[] { entitiesFullPackage })) {
            generate(c, basePackage, overwrite);
        }
    }

    /**
     * 生成某个实体类的代码
     *
     * @param c
     * @param basePackage
     * @param overwrite
     */
    public void generate(Class<?> c, String basePackage, boolean overwrite) {
        String name = c.getSimpleName();
        String base = basePackage + DOT + ENTITY_BASE_PACKAGE + DOT;
        String entitiesPackage = c.getPackage().getName().substring((base).length(), c.getPackage().getName().length());

        System.out.println("entity:" + base + entitiesPackage + DOT + name);
        Map<String, Object> model = new HashMap<>();

        String entityPack = ENTITY_BASE_PACKAGE + DOT + entitiesPackage;
        String daoPack = DAO_BASE_PACKAGE + DOT + entitiesPackage;
        String servicePack = SERVICE_BASE_PACKAGE + DOT + entitiesPackage;
        String directivePack = DIRECTIVE_BASE_PACKAGE + DOT + entitiesPackage;
        String controllerPack = CONTROLLER_BASE_PACKAGE + DOT + entitiesPackage;

        model.put("base", basePackage);
        model.put("entityPack", entityPack);
        model.put("daoPack", daoPack);
        model.put("daoSuffix", DAO_SUFFIX);
        model.put("servicePack", servicePack);
        model.put("serviceSuffix", SERVICE_SUFFIX);
        model.put("directivePack", directivePack);
        model.put("directiveSuffix", DIRECTIVE_SUFFIX);
        model.put("controllerPack", controllerPack);
        model.put("controllerSuffix", CONTROLLER_SUFFIX);
        model.put("entityName", name);

        Set<String> imports = new HashSet<String>();
        Map<String, EntityCondition> conditionMap = new LinkedHashMap<>();
        List<EntityColumn> columnList = new ArrayList<>();

        for (Field field : c.getDeclaredFields()) {
            if (0 != (field.getModifiers() & Modifier.PRIVATE) && 0 == (field.getModifiers() & Modifier.FINAL)) {
                String typeName = field.getType().getName();
                switch (typeName) {
                case "int":
                    typeName = "java.lang.Integer";
                    break;
                case "short":
                    typeName = "java.lang.Short";
                    break;
                case "long":
                    typeName = "java.lang.Long";
                    break;
                case "byte":
                    typeName = "java.lang.Byte";
                    break;
                case "char":
                    typeName = "java.lang.Character";
                    break;
                case "double":
                    typeName = "java.lang.Double";
                    break;
                case "float":
                    typeName = "java.lang.Float";
                    break;
                case "boolean":
                    typeName = "java.lang.Boolean";
                    break;
                }

                GeneratorColumn column = field.getAnnotation(GeneratorColumn.class);
                if (null != column) {
                    String shortTypeName = typeName.substring(typeName.lastIndexOf(DOT) + 1, typeName.length());
                    columnList.add(new EntityColumn(field.getName(), shortTypeName, column.order(), column.title()));
                    if (column.condition()) {
                        if (!typeName.startsWith("java.lang")) {
                            imports.add(typeName);
                        }
                        String key = isNotBlank(column.name()) ? column.name() : field.getName();
                        EntityCondition condition = conditionMap.get(key);
                        if (null == condition) {
                            condition = new EntityCondition(key, shortTypeName, column.title(), column.or(), column.like());
                        }
                        condition.getNameList().add(field.getName());
                        conditionMap.put(key, condition);
                    }
                }
            }
        }
        model.put("imports", imports);
        model.put("columnList", columnList);
        model.put("conditionList", conditionMap.values());

        String daoPath = JAVA_BASE_PATH + basePackage.replace('.', '/') + "/" + daoPack.replace('.', '/') + "/";
        String servicePath = JAVA_BASE_PATH + basePackage.replace('.', '/') + "/" + servicePack.replace('.', '/') + "/";
        String directivePath = JAVA_BASE_PATH + basePackage.replace('.', '/') + "/" + directivePack.replace('.', '/') + "/";
        String controllerPath = JAVA_BASE_PATH + basePackage.replace('.', '/') + "/" + controllerPack.replace('.', '/') + "/";

        try {
            generateFileByFile("java/dao.ftl", daoPath + name + DAO_SUFFIX + ".java", config, model, overwrite);
            generateFileByFile("java/service.ftl", servicePath + name + SERVICE_SUFFIX + ".java", config, model, overwrite);
            generateFileByFile("java/directive.ftl", directivePath + name + DIRECTIVE_SUFFIX + ".java", config, model, overwrite);
            generateFileByFile("java/directiveList.ftl", directivePath + name + "List" + DIRECTIVE_SUFFIX + ".java", config, model,
                    overwrite);
            generateFileByFile("java/controller.ftl", controllerPath + name + CONTROLLER_SUFFIX + ".java", config, model, overwrite);
            generateFileByFile("html/list.ftl", TEMPLATE_BASE_PATH + uncapitalize(name) + "/list.html", config, model,
                    overwrite);
            generateFileByFile("html/add.ftl", TEMPLATE_BASE_PATH + uncapitalize(name) + "/add.html", config, model,
                    overwrite);
            generateFileByFile("html/doc.ftl", WEB_BASE_PATH + "doc.txt", config, model, overwrite, true);
            generateFileByFile("html/language.ftl", WEB_BASE_PATH + "language.txt", config, model, overwrite, true);
        } catch (IOException e) {
            System.out.println(e.getMessage());
        } catch (TemplateException e) {
            System.out.println(e.getMessage());
        }
        System.out.println("-------------------------------");
    }

    private Configuration config;

    /**
     * @throws IOException
     *
     */
    public SourceGenerator() throws IOException {
        config = new freemarker.template.Configuration(Configuration.getVersion());
        config.setDirectoryForTemplateLoading(new File("src/test/resources/generator/"));
        config.setDefaultEncoding("utf-8");
    }

}
