package com.sanluan.common.tools;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.core.io.support.ResourcePatternResolver;
import org.springframework.core.type.classreading.CachingMetadataReaderFactory;
import org.springframework.core.type.classreading.MetadataReader;
import org.springframework.core.type.classreading.MetadataReaderFactory;
import org.springframework.util.ClassUtils;

/**
 * 
 * MyClassUtils 
 *
 */
public class MyClassUtils {
    private static final String RESOURCE_PATTERN = "/**/*.class";
    private static final ResourcePatternResolver resourcePatternResolver = new PathMatchingResourcePatternResolver();

    /**
     * @param cls
     * @param packages
     * @return
     */
    public static List<Class<?>> getAllAssignedClass(Class<?> cls, String[] packages) {
        List<Class<?>> classes = new ArrayList<Class<?>>();
        for (Class<?> c : getClasses(packages)) {
            if (cls.isAssignableFrom(c) && !cls.equals(c)) {
                classes.add(c);
            }
        }
        return classes;
    }

    /**
     * @param packagesToScan
     * @return
     */
    public static Set<Class<?>> getClasses(String[] packagesToScan) {
        Set<Class<?>> classSet = new HashSet<Class<?>>();
        if (null != packagesToScan) {
            for (String pkg : packagesToScan) {
                String pattern = ResourcePatternResolver.CLASSPATH_ALL_URL_PREFIX
                        + ClassUtils.convertClassNameToResourcePath(pkg) + RESOURCE_PATTERN;
                try {
                    Resource[] resources = resourcePatternResolver.getResources(pattern);
                    MetadataReaderFactory readerFactory = new CachingMetadataReaderFactory(resourcePatternResolver);
                    for (Resource resource : resources) {
                        if (resource.isReadable()) {
                            MetadataReader reader = readerFactory.getMetadataReader(resource);
                            String className = reader.getClassMetadata().getClassName();
                            try {
                                classSet.add(Class.forName(className));
                            } catch (ClassNotFoundException e) {
                            }
                        }
                    }
                } catch (IOException e) {
                }
            }
        }
        return classSet;
    }
}
