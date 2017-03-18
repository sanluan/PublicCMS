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
import static org.springframework.util.ClassUtils.*;

import com.sanluan.common.base.Base;

/**
 * 
 * ScanClassUtils 类扫描工具类
 *
 */
public class ScanClassUtils extends Base {
    private static final String RESOURCE_PATTERN = "/**/*.class";
    private static final ResourcePatternResolver resourcePatternResolver = new PathMatchingResourcePatternResolver();

    /**
     * @param cls
     * @param packages
     * @return
     * @throws IOException
     * @throws ClassNotFoundException
     */
    public static List<Class<?>> getAllAssignedClass(Class<?> cls, String[] packages) throws ClassNotFoundException, IOException {
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
     * @throws IOException
     * @throws ClassNotFoundException
     */
    public static Set<Class<?>> getClasses(String[] packagesToScan) throws IOException, ClassNotFoundException {
        Set<Class<?>> classSet = new HashSet<Class<?>>();
        if (null != packagesToScan) {
            for (String pkg : packagesToScan) {
                String pattern = ResourcePatternResolver.CLASSPATH_ALL_URL_PREFIX + convertClassNameToResourcePath(pkg)
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
