package com.github.johnsonmoon.calculate.chain;

import com.github.johnsonmoon.calculate.chain.utils.ClassUtils;

import java.io.File;
import java.net.URI;
import java.net.URL;
import java.util.*;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;
import java.util.stream.Collectors;

/**
 * Create by xuyh at 2020/2/27 18:32.
 */
public class Test {
    public static void main(String[] args) throws Exception {
//        getAllPropertiesFile();
        Class<?> clazz = null;
        try {
            clazz = Thread.currentThread().getContextClassLoader().loadClass("org.apache.catalina.deploy.FilterDef");
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (clazz != null) {
            System.out.println(clazz.getCanonicalName());
        }
    }

    public static void getAllPropertiesFile() throws Exception {
        Collection<URL> urls = new ArrayList<>();
        String javaClassPath = System.getProperty("java.class.path");
        if (javaClassPath != null) {
            for (String path : javaClassPath.split(File.pathSeparator)) {
                try {
                    urls.add(new File(path).toURI().toURL());
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
        Map<String, URL> distinct = new LinkedHashMap<>(urls.size());
        for (URL url : urls) {
            distinct.put(url.toExternalForm(), url);
        }
        Collection<URL> distinctUrls = distinct.values();

        for (URL url : distinctUrls) {
            if (url.toString().endsWith(".jar")) {
                JarFile jarFile = null;
                try {
                    jarFile = new JarFile(url.getFile());
                } catch (Exception e) {
                    e.printStackTrace();
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
                    if (entryName.endsWith(".properties")) {
                        //TODO
                        System.out.println(entryName);
                    }
                }
            } else {
                String fileStr = url.toString();
                List<File> resourceFiles = null;
                try {
                    resourceFiles = getAllSubFiles(new File(new URI(url.toString()).getPath()));
                } catch (Exception e) {
                    e.printStackTrace();
                }
                if (resourceFiles == null) {
                    continue;
                }
                for (File file : resourceFiles) {
                    String filePathName = null;
                    try {
                        filePathName = file.getCanonicalPath();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    if (filePathName != null && filePathName.endsWith(".properties")) {
                        //TODO
                        System.out.println(filePathName);
                    }
                }
            }
        }
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
}
