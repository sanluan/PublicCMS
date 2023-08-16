package com.publiccms.common.generator;

import static com.publiccms.common.constants.Constants.DEFAULT_CHARSET_NAME;

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

import javax.persistence.Entity;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.publiccms.common.constants.Constants;
import com.publiccms.common.generator.annotation.GeneratorColumn;
import com.publiccms.common.generator.entity.EntityColumn;
import com.publiccms.common.generator.entity.EntityCondition;
import com.publiccms.common.tools.CommonUtils;
import com.publiccms.common.tools.FreeMarkerUtils;
import com.publiccms.common.tools.ScanClassUtils;

import freemarker.template.Configuration;
import freemarker.template.TemplateException;

/**
 *
 * SourceMaker 代码生成工具
 *
 */
public class SourceGenerator {
    protected static final Log log = LogFactory.getLog(SourceGenerator.class);

    /**
     * @param arg
     * @throws ClassNotFoundException
     * @throws IOException
     */
    public static void main(String[] arg) throws ClassNotFoundException, IOException {
        SourceGenerator sourceGenerator = new SourceGenerator();
        boolean overwrite = false;// 是否覆盖已有代码
        String basePackage = "com.publiccms";// 基础包名
        // 生成所有实体类的代码
        // sourceGenerator.generate(basePackage, overwrite);
        // 生成某个包所有实体类的代码
        // sourceGenerator.generate(basePackage, "cms", overwrite);
        // 生成某个实体类的代码
        sourceGenerator.generate(Class.forName("com.publiccms.entities.trade.TradeAddress"), basePackage, overwrite);
    }

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
    public static final String JAVA_BASE_PATH = "src/main/java/";
    /**
     *
     */
    public static final String WEB_BASE_PATH = "src/main/resources/";
    /**
     *
     */
    public static final String TEMPLATE_BASE_PATH = CommonUtils.joinString(WEB_BASE_PATH, "templates/admin/");

    /**
     * 生成所有实体类的代码
     *
     * @param basePackage
     * @param overwrite
     * @throws ClassNotFoundException
     * @throws IOException
     */
    public void generate(String basePackage, boolean overwrite) throws ClassNotFoundException, IOException {
        String entitiesFullPackage = CommonUtils.joinString(basePackage, Constants.DOT, ENTITY_BASE_PACKAGE);
        for (Class<?> c : ScanClassUtils.getClasses(new String[] { entitiesFullPackage })) {
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
        String entitiesFullPackage = CommonUtils.joinString(basePackage, Constants.DOT, ENTITY_BASE_PACKAGE, Constants.DOT,
                entityPackage);
        for (Class<?> c : ScanClassUtils.getClasses(new String[] { entitiesFullPackage })) {
            Entity e = c.getAnnotation(Entity.class);
            if (null != e) {
                generate(c, basePackage, overwrite);
            }
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
        String base = CommonUtils.joinString(basePackage, Constants.DOT, ENTITY_BASE_PACKAGE, Constants.DOT);
        String entitiesPackage = c.getPackage().getName().substring((base).length(), c.getPackage().getName().length());

        log.info(CommonUtils.joinString("entity:", base, entitiesPackage, Constants.DOT, name));
        Map<String, Object> model = new HashMap<>();

        String entityPack = CommonUtils.joinString(ENTITY_BASE_PACKAGE, Constants.DOT, entitiesPackage);
        String daoPack = CommonUtils.joinString(DAO_BASE_PACKAGE, Constants.DOT, entitiesPackage);
        String servicePack = CommonUtils.joinString(SERVICE_BASE_PACKAGE, Constants.DOT, entitiesPackage);
        String directivePack = CommonUtils.joinString(DIRECTIVE_BASE_PACKAGE, Constants.DOT, entitiesPackage);
        String controllerPack = CommonUtils.joinString(CONTROLLER_BASE_PACKAGE, Constants.DOT, entitiesPackage);

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
                String shortTypeName = typeName.substring(typeName.lastIndexOf(Constants.DOT) + 1, typeName.length());
                if (null != column) {
                    columnList.add(new EntityColumn(field.getName(), shortTypeName, column.order(), column.title()));
                    if (column.condition()) {
                        if (!typeName.startsWith("java.lang")) {
                            imports.add(typeName);
                        }
                        String key = StringUtils.isNotBlank(column.name()) ? column.name() : field.getName();
                        EntityCondition condition = conditionMap.computeIfAbsent(key,
                                k -> new EntityCondition(k, shortTypeName, column.title(), column.or(), column.like()));
                        condition.getNameList().add(field.getName());
                        conditionMap.put(key, condition);
                    }
                } else {
                    columnList.add(new EntityColumn(field.getName(), shortTypeName, false, field.getName()));
                }
                if ("id".equals(field.getName())) {
                    model.put("idType", shortTypeName);
                }
            }
        }
        model.put("imports", imports);
        model.put("columnList", columnList);
        model.put("conditionList", conditionMap.values());

        String daoPath = CommonUtils.joinString(JAVA_BASE_PATH, basePackage.replace(Constants.DOT, Constants.SEPARATOR),
                Constants.SEPARATOR, daoPack.replace(Constants.DOT, Constants.SEPARATOR),
                Constants.SEPARATOR);
        String servicePath = CommonUtils.joinString(JAVA_BASE_PATH, basePackage.replace(Constants.DOT, Constants.SEPARATOR),
                Constants.SEPARATOR, servicePack.replace(Constants.DOT, Constants.SEPARATOR),
                Constants.SEPARATOR);
        String directivePath = CommonUtils.joinString(JAVA_BASE_PATH,
                basePackage.replace(Constants.DOT, Constants.SEPARATOR), Constants.SEPARATOR,
                directivePack.replace(Constants.DOT, Constants.SEPARATOR), Constants.SEPARATOR);
        String controllerPath = CommonUtils.joinString(JAVA_BASE_PATH,
                basePackage.replace(Constants.DOT, Constants.SEPARATOR), Constants.SEPARATOR,
                controllerPack.replace(Constants.DOT, Constants.SEPARATOR), Constants.SEPARATOR);

        try {
            FreeMarkerUtils.generateFileByFile("java/dao.ftl", CommonUtils.joinString(daoPath, name, DAO_SUFFIX, ".java"), config,
                    model, overwrite);
            FreeMarkerUtils.generateFileByFile("java/service.ftl", CommonUtils.joinString(servicePath, name, SERVICE_SUFFIX, ".java"),
                    config, model, overwrite);
            FreeMarkerUtils.generateFileByFile("java/directive.ftl",
                    CommonUtils.joinString(directivePath, name, DIRECTIVE_SUFFIX, ".java"), config, model, overwrite);
            FreeMarkerUtils.generateFileByFile("java/directiveList.ftl",
                    CommonUtils.joinString(directivePath, name, "List", DIRECTIVE_SUFFIX, ".java"), config, model, overwrite);
            FreeMarkerUtils.generateFileByFile("java/controller.ftl",
                    CommonUtils.joinString(controllerPath, name, CONTROLLER_SUFFIX, ".java"), config, model, overwrite);
            FreeMarkerUtils.generateFileByFile("html/list.ftl",
                    CommonUtils.joinString(TEMPLATE_BASE_PATH, StringUtils.uncapitalize(name), "/list.html"), config, model, overwrite);
            FreeMarkerUtils.generateFileByFile("html/add.ftl",
                    CommonUtils.joinString(TEMPLATE_BASE_PATH, StringUtils.uncapitalize(name), "/add.html"), config, model, overwrite);
            FreeMarkerUtils.generateFileByFile("html/doc.ftl", CommonUtils.joinString(WEB_BASE_PATH, "doc.txt"), config, model,
                    overwrite, true);
            FreeMarkerUtils.generateFileByFile("html/language.ftl", CommonUtils.joinString(WEB_BASE_PATH, "language/operate.txt"),
                    config, model, overwrite, true);
        } catch (IOException e) {
            log.info(e.getMessage());
        } catch (TemplateException e) {
            log.info(e.getMessage());
        }
        log.info(CommonUtils.joinString("file path: ", new File("").getAbsolutePath()));
    }

    private Configuration config;

    /**
     * @throws IOException
     *
     */
    public SourceGenerator() throws IOException {
        config = new freemarker.template.Configuration(Configuration.DEFAULT_INCOMPATIBLE_IMPROVEMENTS);
        config.setDirectoryForTemplateLoading(new File("src/test/resources/generator/"));
        config.setDefaultEncoding(DEFAULT_CHARSET_NAME);
    }

}
