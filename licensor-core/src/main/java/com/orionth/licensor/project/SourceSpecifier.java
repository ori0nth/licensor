package com.orionth.licensor.project;

import com.orionth.licensor.lang.Language;
import com.orionth.licensor.license.License;

import java.nio.file.Path;

public interface SourceSpecifier {

    boolean governs(Path path);

    Language languageOf(Path path);

    License licenseOf(Path path);

}
