package com.publiccms.common.generator;

import java.io.File;
import java.io.IOException;
import java.io.Reader;
import java.io.StringReader;
import java.nio.file.FileVisitResult;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.SimpleFileVisitor;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;

import com.publiccms.common.constants.Constants;
import com.publiccms.common.tools.CommonUtils;

/**
 * PermissionChecker 用于检查数据库脚本中是否已经录入所有应该录入的url
 * 
 */
public class PermissionChecker {
    public static final String SQL_FILE = "../publiccms-core/src/main/resources/initialization/sql/init.sql";
    public static final String SQL_START = "Records of sys_module";
    public static final String SQL_END = "Table structure for sys_module_lang";
    public static final String TEMPLATES = "src/main/resources/templates/admin";
    public static final Pattern FORM_PATTERN = Pattern.compile("<form.*action=[\"|\']([^\"\']*)[\"|\'].*>");
    public static final Pattern FORM2_PATTERN = Pattern.compile("<input.*remote=[\"|\']([^\"\']*)[\"|\'].*>");
    public static final Pattern AJAXBUTTON_PATTERN = Pattern.compile("<a.*href=[\"|\']([^\"\']*)[\"|\'].*target=\"ajaxTodo\".*>");
    public static final Pattern AJAXBUTTON2_PATTERN = Pattern
            .compile("<a.*target=\"ajaxTodo\".*href=[\"|\']([^\"\']*)[\"|\'].*>");
    public static final Pattern AJAXBUTTON3_PATTERN = Pattern
            .compile("<a.*href=[\"|\']([^\"\']*)[\"|\'].*target=\"selectedTodo\".*>");
    public static final Pattern AJAXBUTTON4_PATTERN = Pattern
            .compile("<a.*target=\"selectedTodo\".*href=[\"|\']([^\"\']*)[\"|\'].*>");
    public static final Pattern[] URL_PATTERNS = new Pattern[] { FORM_PATTERN, AJAXBUTTON_PATTERN, AJAXBUTTON2_PATTERN,
            AJAXBUTTON3_PATTERN, AJAXBUTTON4_PATTERN };

    public static void main(String[] args) {
        try {
            Set<String> authorizedUrlSet = new HashSet<>();
            Set<String> pageUrlSet = new HashSet<>();
            getAuthorizedUrl(SQL_FILE, SQL_START, SQL_END, authorizedUrlSet);
            getPageUrl(TEMPLATES, URL_PATTERNS, pageUrlSet);
            for (String url : pageUrlSet) {
                if (!authorizedUrlSet.contains(url)) {
                    System.out.println(CommonUtils.joinString(url," 没有添加到系统权限中"));
                }
            }
            System.out.println("检查完毕！");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static void getPageUrl(String filePath, Pattern[] patterns, Set<String> pageUrlSet) throws IOException {
        Path parentPath = new File(filePath).toPath();
        Files.walkFileTree(parentPath, new SimpleFileVisitor<Path>() {
            @Override
            public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) throws IOException {
                scanPageFile(parentPath, file, patterns, pageUrlSet);
                return FileVisitResult.CONTINUE;
            }
        });
    }

    private static void scanPageFile(Path parentPath, Path filePath, Pattern[] patterns, Set<String> pageUrlSet)
            throws IOException {
        String pageFile = FileUtils.readFileToString(filePath.toFile(), Constants.DEFAULT_CHARSET);
        String url = parentPath.relativize(filePath).toString();
        addPageUrl(url, pageUrlSet);
        addUrlInPage(pageFile, patterns, pageUrlSet);
    }

    private static void addPageUrl(String url, Set<String> pageUrlSet) throws IOException {
        if (null != url && !url.startsWith("include_page") && !url.startsWith("common") && !url.startsWith("main")
                && !url.startsWith("login") && !url.startsWith("logout") && !url.startsWith("menus")
                && !url.startsWith("sysSite\\") && !url.startsWith("sysSite/") && !url.startsWith("cmsTemplate\\demo\\")
                && !url.startsWith("sysModule\\") && !url.startsWith("sysModule/") && !url.startsWith("sysDomain/save")
                && !url.startsWith("sysDomain/delete") && !url.startsWith("dict/save") && !url.startsWith("sysDomain\\add")
                && !url.startsWith("sysDomain\\list") && !url.startsWith("sysCluster\\") && !url.startsWith("changeLocale")
                && !url.startsWith("index") && !url.startsWith("<") && !url.startsWith("$")) {
            addUrl(url, pageUrlSet);
        }
    }

    private static void addUrlInPage(String pageContent, Pattern[] patterns, Set<String> pageUrlSet) throws IOException {
        for (Pattern pattern : patterns) {
            Matcher matcher = pattern.matcher(pageContent);
            while (matcher.find()) {
                addPageUrl(matcher.group(1), pageUrlSet);
            }
        }
    }

    private static void getAuthorizedUrl(String filePath, String start, String end, Set<String> authorizedUrlSet)
            throws IOException {
        String sql = FileUtils.readFileToString(new File(filePath), Constants.DEFAULT_CHARSET);
        if (null != sql) {
            int index = sql.indexOf(start);
            int endindex = sql.indexOf(end);
            if (0 < index && index < endindex) {
                sql = sql.substring(index, endindex);
                Reader reader = new StringReader(sql);
                List<String> lines = IOUtils.readLines(reader);
                for (String line : lines) {
                    dealLine(line, authorizedUrlSet);
                }
            }
        }
    }

    private static void dealLine(String line, Set<String> authorizedUrlSet) {
        int index = line.indexOf("(");
        int index2 = line.indexOf(")");
        if (0 < index && index < index2) {
            String temp = line.substring(index + 1, index2);
            String[] parameters = temp.split(", ");
            if (null != parameters && 2 < parameters.length) {
                dealParameter(parameters[1], authorizedUrlSet);
                dealParameter(parameters[2], authorizedUrlSet);
            }
        }
    }

    private static void dealParameter(String parameter, Set<String> authorizedUrlSet) {
        if (null != parameter && !"NULL".equals(parameter)) {
            if (parameter.startsWith("'") && parameter.endsWith("'")) {
                parameter = parameter.substring(1, parameter.length() - 1);
            }
            if (parameter.contains(",")) {
                String[] urls = parameter.split(",");
                for (String url : urls) {
                    addUrl(url, authorizedUrlSet);
                }
            } else {
                addUrl(parameter, authorizedUrlSet);
            }
        }
    }

    private static void addUrl(String url, Set<String> authorizedUrlSet) {
        if (CommonUtils.notEmpty(url)) {
            int index = url.indexOf(".");
            if (0 < index) {
                url = url.substring(0, index);
            }
            index = url.indexOf("?");
            if (0 < index) {
                url = url.substring(0, index);
            }
            index = url.indexOf("<");
            if (0 < index) {
                url = url.substring(0, index);
            }
            if (url.contains("\\")) {
                url = url.replace("\\", "/");
            }
            authorizedUrlSet.add(url);
        }
    }
}
