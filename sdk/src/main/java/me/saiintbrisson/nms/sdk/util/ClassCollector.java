package me.saiintbrisson.nms.sdk.util;

import com.google.common.collect.Lists;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang.ArrayUtils;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Type;
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

    private Class<T> type;
    private String packageName;

    public ClassCollector<T> filterByType(Class<T> type) {
        this.type = type;
        return this;
    }

    public ClassCollector<T> filterByPackage(String packageName) {
        this.packageName = packageName + ".";
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

            System.out.println(name);

            try {
                Class<?> aClass = Class.forName(name);
                System.out.println(aClass);

                for(Type genericInterface : aClass.getGenericInterfaces()) {
                    System.out.println(genericInterface.getTypeName());
                }

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
