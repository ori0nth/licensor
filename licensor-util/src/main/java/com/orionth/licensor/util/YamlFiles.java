package com.orionth.licensor.util;

import org.yaml.snakeyaml.Yaml;

import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Map;

public class YamlFiles {

    private static final Yaml yaml = new Yaml();

    public static Map<String, Object> read(InputStream is) {
        return yaml.load(is);
    }

    public static Map<String, Object> read(Path file) {
        try {
            InputStream is = Files.newInputStream(file);
            Map<String, Object> map = read(is);
            is.close();
            return map;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

}
