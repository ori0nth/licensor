package com.orionth.licensor.util;

import java.io.InputStream;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;

public class IOUtil {

    public static StringReader reader(Path file,
                                      Charset charset) {
        try {
            InputStream is = Files.newInputStream(file);
            String str = new String(is.readAllBytes(), charset);
            is.close();
            return new StringReader(str, 0);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

}
