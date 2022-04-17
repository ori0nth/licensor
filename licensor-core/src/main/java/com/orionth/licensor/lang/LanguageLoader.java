package com.orionth.licensor.lang;

import com.orionth.licensor.util.IOUtil;
import com.orionth.licensor.util.YamlFiles;

import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class LanguageLoader {

    protected List<Path> extraLangMappingFiles = new ArrayList<>();

    protected List<Path> extraExtMappingFiles = new ArrayList<>();

    protected List<Path> langFolders = new ArrayList<>();

    protected List<Language> loadedLanguages = new ArrayList<>();

    protected Map<String, Language> languagesByAlias = new HashMap<>();

    protected Map<String, CommentFactory> commentFactories = new HashMap<>();

    public LanguageLoader withFolder(Path p) {
        langFolders.add(p);
        return this;
    }

    public LanguageLoader withExtMappingFile(Path p) {
        extraExtMappingFiles.add(p);
        return this;
    }

    public LanguageLoader withLangMappingFile(Path p) {
        extraLangMappingFiles.add(p);
        return this;
    }

    public List<Language> getLoadedLanguages() {
        return loadedLanguages;
    }

    public Map<String, Language> getLanguagesByAlias() {
        return languagesByAlias;
    }

    public Map<String, CommentFactory> getCommentFactories() {
        return commentFactories;
    }

    @SuppressWarnings("unchecked")
    public void loadSingleLanguage(Path folder) {
        try {

            String langName = folder.getFileName().toString();
            Path propertiesFile = folder.resolve(langName + ".prop.yml");
            Path commentFactoryFile = folder.resolve(langName + ".cf.txt");

            // create language
            Language lang = new Language(langName);

            // load properties
            Map<String, Object> props = YamlFiles.read(propertiesFile);
            List<String> aliases = (List<String>) props.get("aliases");
            List<String> extensions = (List<String>) props.get("extensions");

            lang.aliases.addAll(aliases);
            lang.extensions.addAll(extensions);

            // load comment factory
            lang.commentFactory(
                    SimpleCommentFactory.parse(
                            IOUtil.reader(commentFactoryFile, StandardCharsets.UTF_8),
                            name -> commentFactories.get(name)
                    )
            );

            // put comment factory
            commentFactories.put(langName, lang.commentFactory());

        } catch (Exception e) {
            System.out.println("Error while loading language '" + folder.getFileName().toString() + "'");
            e.printStackTrace();
        }
    }

    public void load() {
        final int CF_SUFF_LEN = ".cf.txt".length();

        try {

            // load all languages from folder
            for (Path f : langFolders) {
                Files.list(f).forEach(path -> {
                    String fn = path.getFileName().toString();
                    if (Files.isDirectory(path)) { // is language specification
                        loadSingleLanguage(path);
                    } else if (fn.endsWith(".cf.txt")) {
                        // parse generic comment factory
                        SimpleCommentFactory scf = SimpleCommentFactory.parse(
                                IOUtil.reader(path, StandardCharsets.UTF_8),
                                name -> commentFactories.get(name)
                        );

                        // put
                        commentFactories.put(fn.substring(0, fn.length() - CF_SUFF_LEN), scf);
                    }
                });
            }

        } catch (Exception e) {
            System.out.println("Exception while loading languages;");
            e.printStackTrace();
        }
    }

}
