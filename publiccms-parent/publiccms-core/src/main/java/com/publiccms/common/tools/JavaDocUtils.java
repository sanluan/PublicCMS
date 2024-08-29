package com.publiccms.common.tools;

import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Map.Entry;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

import com.github.therapi.runtimejavadoc.ClassJavadoc;
import com.github.therapi.runtimejavadoc.Comment;
import com.github.therapi.runtimejavadoc.FieldJavadoc;
import com.github.therapi.runtimejavadoc.Link;
import com.github.therapi.runtimejavadoc.MethodJavadoc;
import com.github.therapi.runtimejavadoc.ParamJavadoc;
import com.github.therapi.runtimejavadoc.RuntimeJavadoc;
import com.github.therapi.runtimejavadoc.ToHtmlStringCommentVisitor;

public class JavaDocUtils {
    private JavaDocUtils() {
    }

    public static String getClassComment(String fullyQualifiedClassName) {
        try {
            JavaDocUtils.class.getClassLoader().loadClass(fullyQualifiedClassName);
            ClassJavadoc classDoc = RuntimeJavadoc.getJavadoc(fullyQualifiedClassName);
            if (classDoc.isEmpty()) {
                return null;
            }
            return format(classDoc.getComment());
        } catch (ClassNotFoundException e) {
            return null;
        }
    }

    public static Map<String, String> getFieldsJavadoc(String fullyQualifiedClassName) {
        ClassJavadoc classDoc = RuntimeJavadoc.getJavadoc(fullyQualifiedClassName);
        if (classDoc.isEmpty()) {
            return Collections.emptyMap();
        }
        Map<String, String> map = new LinkedHashMap<>();
        for (FieldJavadoc field : classDoc.getFields()) {
            map.put(field.getName(), format(field.getComment()));
        }
        return map;
    }

    public static Map<String, String> getMethodJavadoc(String fullyQualifiedClassName) {
        ClassJavadoc classDoc = RuntimeJavadoc.getJavadoc(fullyQualifiedClassName);
        if (classDoc.isEmpty()) {
            return Collections.emptyMap();
        }

        Map<String, String> map = new LinkedHashMap<>();
        for (MethodJavadoc method : classDoc.getMethods()) {
            StringBuilder sb = new StringBuilder(format(method.getComment()));
            for (ParamJavadoc paramDoc : method.getParams()) {
                sb.append("<code>").append(paramDoc.getName()).append("</code> : ").append(format(paramDoc.getComment()))
                        .append("\n");
            }
            if (!method.isConstructor()) {
                sb.append("return : ").append(format(method.getReturns())).append("\n");
            }
            map.put(method.getName(), sb.toString());
        }
        return map;
    }

    public static String format(Comment comment) {
        if (comment == null) {
            return "";
        }
        CustomerCommentVisitor visitor = new CustomerCommentVisitor();
        comment.visit(visitor);
        Document document = Jsoup.parse(visitor.build());
        Element body = document.body();
        return body.html();
    }

    static class CustomerCommentVisitor extends ToHtmlStringCommentVisitor {
        @Override
        public void inlineLink(Link link) {
            buf.append("<ul style=\"padding-left:20px;\">");
            Map<String, String> fieldsMap = getFieldsJavadoc(link.getReferencedClassName());
            for (Entry<String, String> field : fieldsMap.entrySet()) {
                buf.append("<li><code>").append(field.getKey()).append("</code> : ").append(field.getValue()).append("</li>");
            }
            buf.append("</ul>");
        }
    }
}
