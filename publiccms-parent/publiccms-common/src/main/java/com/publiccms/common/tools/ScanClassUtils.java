package com.publiccms.common.tools;

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
 * 类扫描工具类
 * 
 * ScanClassUtils
 *
 */
public class ScanClassUtils {

    private static final String RESOURCE_PATTERN = "/**/*.class";
    private static final ResourcePatternResolver resourcePatternResolver = new PathMatchingResourcePatternResolver();

    /**
     * @param cls
     * @param packages
     * @return classes list
     * @throws IOException
     * @throws ClassNotFoundException
     */
    public static List<Class<?>> getAllAssignedClass(Class<?> cls, String[] packages) throws ClassNotFoundException, IOException {
        List<Class<?>> classes = new ArrayList<>();
        for (Class<?> c : getClasses(packages)) {
            if (cls.isAssignableFrom(c) && !cls.equals(c)) {
                classes.add(c);
            }
        }
        return classes;
    }

    /**
     * @param packagesToScan
     * @return classes set
     * @throws IOException
     * @throws ClassNotFoundException
     */
    public static Set<Class<?>> getClasses(String[] packagesToScan) throws IOException, ClassNotFoundException {
        Set<Class<?>> classSet = new HashSet<>();
        if (null != packagesToScan) {
            for (String pkg : packagesToScan) {
                String pattern = ResourcePatternResolver.CLASSPATH_ALL_URL_PREFIX + ClassUtils.convertClassNameToResourcePath(pkg)
                        + RESOURCE_PATTERN;
                Resource[] resources = resourcePatternResolver.getResources(pattern);
                MetadataReaderFactory readerFactory = new CachingMetadataReaderFactory(resourcePatternResolver);
                for (Resource resource : resources) {
                    if (resource.isReadable()) {
                        MetadataReader reader = readerFactory.getMetadataReader(resource);
                        String className = reader.getClassMetadata().getClassName();
                        classSet.add(Class.forName(className));
                    }
                }
            }
        }
        return classSet;
    }
}
