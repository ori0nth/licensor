package com.orionth.licensor.project;

import com.orionth.licensor.lang.Language;
import com.orionth.licensor.license.License;

import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

public class SourceSpecification {

    private LicensorProject project;

    // matched first for higher priority
    private List<SourceSpecifier> highLevel = new ArrayList<>();

    // matched last for lower priority
    private List<SourceSpecifier> lowLevel  = new ArrayList<>();

    private SourceSpecifier defaultSpecifier;

    private License mainLicense;

    public SourceSpecification(LicensorProject project) {
        this.project = project;
        this.defaultSpecifier = defaultGeneralSpecifier(project);
    }

    public LicensorProject getProject() {
        return project;
    }

    public SourceSpecifier defaultSpecifier() {
        return defaultSpecifier;
    }

    public SourceSpecification defaultSpecifier(SourceSpecifier specifier) {
        this.defaultSpecifier = specifier;
        return this;
    }

    public License mainLicense() {
        return mainLicense;
    }

    public SourceSpecification mainLicense(License license) {
        this.mainLicense = license;
        return this;
    }

    public List<SourceSpecifier> getHighPriority() {
        return highLevel;
    }

    public List<SourceSpecifier> getLowPriority() {
        return lowLevel;
    }

    public SourceSpecifier match(Path path) {
        for (SourceSpecifier specifier : highLevel)
            if (specifier.governs(path))
                return specifier;
        for (SourceSpecifier specifier : lowLevel)
            if (specifier.governs(path))
                return specifier;
        return null;
    }

    //////////////////////////////////////////////

    public static SourceSpecifier defaultGeneralSpecifier(LicensorProject project) {
        return new SourceSpecifier() {
            @Override
            public boolean governs(Path path) {
                return true;
            }

            @Override
            public Language languageOf(Path path) {
                String[] nameSplit = path.getFileName().toString().split("\\.");
                String ext = nameSplit[nameSplit.length - 1];
                return project.main.getLanguageLoader().getLanguagesByExt().get(ext);
            }

            @Override
            public License licenseOf(Path path) {
                return project.sourceSpecification.mainLicense;
            }
        };
    }

}
