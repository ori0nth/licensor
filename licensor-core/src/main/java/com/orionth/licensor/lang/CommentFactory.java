package com.orionth.licensor.lang;

import com.orionth.licensor.util.StringReader;

import java.io.StringWriter;

public interface CommentFactory {

    void writeMultilineComment(
            StringWriter writer,
            StringReader reader
    );

    void writeSingleLineComments(
            StringWriter writer,
            StringReader reader
    );

}
