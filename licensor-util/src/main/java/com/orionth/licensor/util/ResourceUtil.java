package com.orionth.licensor.util;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;

public class ResourceUtil {

    public static InputStream open(Class<?> resolver,
                                   String path) {
        if (!path.startsWith("/"))
            path = "/" + path;
        try {
            return resolver.getResourceAsStream(path);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public static String readString(Class<?> resolver,
                                    String path,
                                    Charset charset) {
        try {
            InputStream is = open(resolver, path);
            String r = new String(is.readAllBytes(), charset);
            is.close();
            return r;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public static String readString(Class<?> resolver,
                                    String path) {
        return readString(resolver, path, StandardCharsets.UTF_8);
    }

}
