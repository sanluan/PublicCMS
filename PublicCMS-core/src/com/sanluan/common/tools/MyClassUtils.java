package com.sanluan.common.tools;

import static org.apache.commons.lang3.StringUtils.isNotBlank;

import java.io.File;
import java.io.IOException;
import java.net.JarURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

import org.springframework.util.ClassUtils;

public class MyClassUtils {
	public static ClassLoader classLoader = ClassUtils.getDefaultClassLoader();

	public static List<Class<?>> getAllAssignedClass(Class<?> cls, String basePackage) {
		List<Class<?>> classes = new ArrayList<Class<?>>();
		for (Class<?> c : getClasses(basePackage)) {
			if (cls.isAssignableFrom(c) && !cls.equals(c)) {
				classes.add(c);
			}
		}
		return classes;
	}

	public static List<Class<?>> getClasses(String basePackage) {
		File dir = null;
		List<Class<?>> classes = new ArrayList<Class<?>>();
		if (isNotBlank(basePackage)) {
			String path = basePackage.replace('.', '/');
			URL url = classLoader.getResource(path);
			if (null != url) {
				if ("jar".equalsIgnoreCase(url.getProtocol())) {
					try {
						classes.addAll(getClasses(((JarURLConnection) url.openConnection()).getJarFile(), basePackage));
					} catch (IOException e) {
					}
				}
				dir = new File(url.getFile());
				classes.addAll(getClasses(dir, basePackage));
			}
		}
		return classes;
	}

	private static List<Class<?>> getClasses(File dir, String currentPackage) {
		List<Class<?>> classes = new ArrayList<Class<?>>();
		if (null == dir || !dir.exists()) {
			return classes;
		}
		for (File f : dir.listFiles()) {
			if (f.isDirectory()) {
				classes.addAll(getClasses(f, currentPackage + "." + f.getName()));
			}
			String name = f.getName();
			if (name.endsWith(".class")) {
				try {
					classes.add(Class.forName(currentPackage + "." + name.substring(0, name.length() - 6)));
				} catch (ClassNotFoundException e) {
				}
			}
		}
		return classes;
	}

	private static List<Class<?>> getClasses(JarFile jar, String packageName) {
		List<Class<?>> classes = new ArrayList<Class<?>>();
		String packageDirName = packageName.replace('.', '/');
		Enumeration<JarEntry> entries = jar.entries();
		while (entries.hasMoreElements()) {
			JarEntry entry = entries.nextElement();
			String name = entry.getName();
			if ('/' == name.charAt(0)) {
				name = name.substring(1);
			}
			if (name.startsWith(packageDirName) && name.endsWith(".class") && !entry.isDirectory()) {
				try {
					classes.add(Class.forName(name.substring(0, name.length() - 6).replace('/', '.')));
				} catch (ClassNotFoundException e) {
				}
			}
		}
		return classes;
	}
}
