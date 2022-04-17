package com.orionth.licensor.lang;

import java.io.StringReader;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.List;

public class Language {

    protected final String name;

    protected List<String> aliases = new ArrayList<>();

    protected List<String> extensions = new ArrayList<>();

    protected CommentFactory commentFactory;

    public Language(String name) {
        this.name = name;
    }

    public String name() {
        return name;
    }

    public List<String> aliases() {
        return aliases;
    }

    public List<String> extensions() { return extensions; }

    public Language alias(String al) {
        aliases.add(al);
        return this;
    }

    public CommentFactory commentFactory() {
        return commentFactory;
    }

    public Language commentFactory(CommentFactory commentFactory) {
        this.commentFactory = commentFactory;
        return this;
    }

}
