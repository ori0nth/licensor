package com.orionth.licensor;

import com.orionth.licensor.lang.LanguageLoader;
import com.orionth.licensor.project.LicensorProject;

public class Licensor {

    protected final LicensorProject project;

    protected final LanguageLoader languageLoader = new LanguageLoader();

    public Licensor(LicensorProject project) {
        this.project = project;

        // TODO: defaults
    }

    public LicensorProject getProject() {
        return project;
    }

    public LanguageLoader getLanguageLoader() {
        return languageLoader;
    }

}
