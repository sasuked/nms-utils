package me.saiintbrisson.nms.api.util;

import com.google.common.collect.Lists;
import lombok.RequiredArgsConstructor;

import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Enumeration;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

@RequiredArgsConstructor
public class ClassCollector<T> {

    private final Class<?> parent;
    private final Class<T> type;

    private String packageName;
    private boolean selectInterfaces = false;

    public ClassCollector<T> filterByPackage(String packageName) {
        this.packageName = packageName + ".";
        return this;
    }

    public ClassCollector<T> selectInterfaces(boolean selectInterfaces) {
        this.selectInterfaces = selectInterfaces;
        return this;
    }

    public Collection<Class<T>> collect() throws IOException {
        ArrayList<Class<T>> list = Lists.newArrayList();

        File file;
        try {
            URI uri = parent.getProtectionDomain().getCodeSource().getLocation().toURI();
            file = new File(uri);
        } catch (URISyntaxException e) {
            throw new IllegalArgumentException("Could not find specified file");
        }

        if(!file.exists()) {
            throw new IllegalArgumentException("Could not find specified file");
        }
        if(file.isDirectory()) {
            throw new IllegalArgumentException("The specified folder must be a file");
        }

        JarFile jarFile = new JarFile(file);
        Enumeration<JarEntry> entries = jarFile.entries();

        while(entries.hasMoreElements()) {
            JarEntry entry = entries.nextElement();
            String name = entry.getName().replace("/", ".");

            if(!name.startsWith(packageName) || !name.endsWith(".class")) continue;

            name = name.substring(0, name.length() - 6);
            if(name.endsWith(".")) continue;

            try {
                Class<?> aClass = Class.forName(name);

                if(aClass.isInterface() && !selectInterfaces) continue;

                if(type.isAssignableFrom(aClass)) {
                    list.add((Class<T>) aClass);
                }
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }
        }

        return list;
    }

}
