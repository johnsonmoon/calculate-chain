package com.github.johnsonmoon.calculate.chain.utils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.net.URI;
import java.net.URL;
import java.util.*;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;
import java.util.stream.Collectors;

/**
 * Create by xuyh at 2019/8/30 21:43.
 */
public class ClassUtils {
    private static Logger logger = LoggerFactory.getLogger(ClassUtils.class);

    private static List<ClassNameHandler> classNameHandlers = new ArrayList<>();
    private static List<PackagePathHandler> packagePathHandlers = new ArrayList<>();

    static {
        classNameHandlers.add(new SpringBootFatJarClassNameHandler());
        packagePathHandlers.add(new SpringBootFatJarPackagePathHandler());
    }

    public static void addClassNameHandler(ClassNameHandler handler) {
        if (classNameHandlers == null) {
            classNameHandlers = new ArrayList<>();
        }
        classNameHandlers.add(handler);
    }

    public static void addPackagePathHandler(PackagePathHandler handler) {
        if (packagePathHandlers == null) {
            packagePathHandlers = new ArrayList<>();
        }
        packagePathHandlers.add(handler);
    }

    private static String handleClassName(String source) {
        String result = source;
        if (classNameHandlers != null) {
            for (ClassNameHandler handler : classNameHandlers) {
                try {
                    result = handler.handle(result);
                } catch (Exception e) {
                    logger.warn(e.getMessage(), e);
                }
            }
        }
        return result;
    }

    private static URL handlePackagePath(String packagePath) {
        ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
        URL url = null;
        try {
            url = classLoader.getResource(packagePath);
        } catch (Exception e) {
            logger.warn(e.getMessage(), e);
        }
        if (url == null && packagePathHandlers != null) {
            for (PackagePathHandler handler : packagePathHandlers) {
                try {
                    url = classLoader.getResource(handler.handle(packagePath));
                } catch (Exception e) {
                    logger.warn(e.getMessage(), e);
                }
                if (url != null) {
                    break;
                }
            }
        }
        return url;
    }

    public static List<String> getAllClassNamesFromClassPath() {
        Collection<URL> urls = new ArrayList<>();
        String javaClassPath = System.getProperty("java.class.path");
        if (javaClassPath != null) {
            for (String path : javaClassPath.split(File.pathSeparator)) {
                try {
                    urls.add(new File(path).toURI().toURL());
                } catch (Exception e) {
                    logger.debug(e.getMessage(), e);
                }
            }
        }
        Map<String, URL> distinct = new LinkedHashMap<>(urls.size());
        for (URL url : urls) {
            distinct.put(url.toExternalForm(), url);
        }
        Collection<URL> distinctUrls = distinct.values();

        List<String> classNames = new ArrayList<>();
        for (URL url : distinctUrls) {
            if (url.toString().endsWith(".jar")) {
                JarFile jarFile = null;
                try {
                    jarFile = new JarFile(url.getFile());
                } catch (Exception e) {
                    logger.debug(e.getMessage(), e);
                }
                if (jarFile == null) {
                    continue;
                }
                Enumeration<JarEntry> entries = jarFile.entries();
                if (entries == null) {
                    continue;
                }
                while (entries.hasMoreElements()) {
                    String entryName = entries.nextElement().getName();
                    if (entryName.endsWith(".class")) {
                        String className = null;
                        try {
                            className = entryName.substring(0, entryName.length() - 6);
                        } catch (Exception e) {
                            logger.debug(e.getMessage(), e);
                        }
                        if (className != null && !className.contains("$")) {
                            classNames.add(handleClassName(className.replaceAll("/", ".")));
                        }
                    }
                }
            } else {
                String fileStr = url.toString();
                List<File> resourceFiles = null;
                try {
                    resourceFiles = getAllSubFiles(new File(new URI(url.toString()).getPath()));
                } catch (Exception e) {
                    logger.debug(e.getMessage(), e);
                }
                if (resourceFiles == null) {
                    continue;
                }
                for (File file : resourceFiles) {
                    String filePathName = null;
                    try {
                        filePathName = file.getCanonicalPath();
                    } catch (Exception e) {
                        logger.debug(e.getMessage(), e);
                    }
                    if (filePathName != null && filePathName.endsWith(".class")) {
                        String className = null;
                        try {
                            className = filePathName.substring(fileStr.substring(5, fileStr.length()).length(), filePathName.length() - 6);
                        } catch (Exception e) {
                            logger.debug(e.getMessage(), e);
                        }
                        if (className != null && !className.contains("$")) {
                            classNames.add(handleClassName(className.replaceAll("/", ".")));
                        }
                    }
                }
            }
        }
        return classNames;
    }

    public static List<String> getAllClassNamesFromPackage(String packageName) {
        List<String> classNames = new ArrayList<>();
        String packagePath = packageName.replaceAll("\\.", "/");
        URL packageResourceUrl = handlePackagePath(packagePath);
        if (packageResourceUrl == null) {
            return classNames;
        }
        if (packageResourceUrl.getProtocol().equals("jar")) {
            String jarFileName = packageResourceUrl.getFile();
            JarFile jarFile = null;
            try {
                jarFile = new JarFile(jarFileName.substring(5, jarFileName.indexOf("!")));
            } catch (Exception e) {
                logger.debug(e.getMessage(), e);
            }
            if (jarFile != null) {
                Enumeration<JarEntry> entries = jarFile.entries();
                while (entries != null && entries.hasMoreElements()) {
                    String entryName = entries.nextElement().getName();
                    if (entryName.contains(packagePath) && entryName.endsWith(".class")) {
                        String className = null;
                        try {
                            className = entryName.substring(entryName.indexOf(packagePath), entryName.length() - 6);
                        } catch (Exception e) {
                            logger.debug(e.getMessage(), e);
                        }
                        if (className != null && !className.contains("$")) {
                            classNames.add(handleClassName(className.replaceAll("/", ".")));
                        }
                    }
                }
            }
        } else if (packageResourceUrl.getProtocol().equals("file")) {
            List<File> resourceFiles = null;
            try {
                resourceFiles = getAllSubFiles(new File(new URI(packageResourceUrl.toString()).getPath()));
            } catch (Exception e) {
                logger.debug(e.getMessage(), e);
            }
            if (resourceFiles != null) {
                for (File file : resourceFiles) {
                    String filePathName = null;
                    try {
                        filePathName = file.getCanonicalPath();
                    } catch (Exception e) {
                        logger.debug(e.getMessage(), e);
                    }
                    if (filePathName != null && filePathName.endsWith(".class")) {
                        String className = null;
                        try {
                            className = filePathName.substring(filePathName.indexOf(packagePath), filePathName.length() - 6);
                        } catch (Exception e) {
                            logger.debug(e.getMessage(), e);
                        }
                        if (className != null && !className.contains("$")) {
                            classNames.add(handleClassName(className.replaceAll("/", ".")));
                        }
                    }
                }
            }
        }
        return classNames;
    }

    private static List<File> getAllSubFiles(File parent) {
        List<File> subFiles = new ArrayList<>();
        if (parent.isDirectory()) {
            File[] files = parent.listFiles();
            if (files != null) {
                List<File> subs = Arrays.stream(files).filter(File::isFile).collect(Collectors.toList());
                List<File> dirs = Arrays.stream(files).filter(File::isDirectory).collect(Collectors.toList());
                subFiles.addAll(subs);
                for (File dir : dirs) {
                    subFiles.addAll(getAllSubFiles(dir));
                }
            }
        } else {
            subFiles.add(parent);
        }
        return subFiles;
    }

    public static boolean isImplemented(Class<?> clazz, Class<?> interfaceClazz) {
        List<Class<?>> interfaces = getAllImplementedInterfaces(clazz);
        if (interfaces == null || interfaces.isEmpty()) {
            return false;
        }
        for (Class<?> interfaceClass : interfaces) {
            if (interfaceClass.getCanonicalName().equals(interfaceClazz.getCanonicalName())) {
                return true;
            }
        }
        return false;
    }

    private static List<Class<?>> getAllImplementedInterfaces(Class<?> clazz) {
        List<Class<?>> interfaces = new ArrayList<>();
        if (!clazz.isInterface() && !clazz.isAnnotation()) {
            Class<?> superClass = clazz.getSuperclass();
            if (superClass != null) {
                interfaces.addAll(getAllImplementedInterfaces(superClass));
            }
        }
        interfaces.addAll(Arrays.asList(clazz.getInterfaces()));
        return interfaces;
    }

    public interface ClassNameHandler {
        String handle(String source);
    }

    private static class SpringBootFatJarClassNameHandler implements ClassNameHandler {
        private static final String SPRING_BOOT_FAT_JAR_CLASS_NAME_PREFIX = "BOOT-INF.classes.";

        @Override
        public String handle(String source) {
            if (source.startsWith(SPRING_BOOT_FAT_JAR_CLASS_NAME_PREFIX)) {
                return source.substring(SPRING_BOOT_FAT_JAR_CLASS_NAME_PREFIX.length(), source.length());
            } else {
                return source;
            }
        }
    }

    public interface PackagePathHandler {
        String handle(String source);
    }

    private static class SpringBootFatJarPackagePathHandler implements PackagePathHandler {
        private static final String SPRING_BOOT_FAT_JAR_PACKAGE_PATH_PREFIX = "BOOT-INF/classes/";

        @Override
        public String handle(String source) {
            return SPRING_BOOT_FAT_JAR_PACKAGE_PATH_PREFIX + source;
        }
    }
}
