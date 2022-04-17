package com.orionth.licensor.lang;

import com.orionth.licensor.util.StringReader;

import java.io.StringWriter;
import java.util.function.Function;

public class SimpleCommentFactory
        implements CommentFactory {

    /**
     * Control data source strings.
     * TODO: use for alignment and whatnot
     */
    protected String[] controlStrings;

    /* multiline building blocks */
    protected String          mlStart;
    protected BodyLineFactory mlBody;
    protected String          mlEnd;

    /* single line building blocks */
    protected BodyLineFactory slBody;

    protected CommentFactory delegate;

    @Override
    public void writeMultilineComment(StringWriter writer,
                                      StringReader reader) {

        if (
                (mlStart == null || mlBody == null || mlEnd == null)
                        && delegate != null
        ) {
            delegate.writeMultilineComment(writer, reader);
            return;
        }

        writer.write(mlStart);

        while (reader.current() != StringReader.DONE) {
            // read line
            String line = reader.collect(c -> c != '\n', 1);

            // produce text
            String text = mlBody.create(line);

            // write text
            writer.write(text);
        }

        writer.write(mlEnd);

    }

    @Override
    public void writeSingleLineComments(StringWriter writer,
                                        StringReader reader) {

        if (slBody == null && delegate != null) {
            delegate.writeSingleLineComments(writer, reader);
            return;
        }

        while (reader.current() != StringReader.DONE) {
            // read line
            String line = reader.collect(c -> c != '\n', 1);

            // produce text
            String text = slBody.create(line);

            // write text
            writer.write(text);
        }

    }

    //////////////////////////////////////////////////

    public static SimpleCommentFactory parse(StringReader reader,
                                             Function<String, CommentFactory> resolver) {
        // parse first control line
        String cl1 = reader.collectln();

        CommentFactory inherit = null;

        {
            String[] parts = cl1.split(" ");
            int i = 0;
            while (i < parts.length) {
                String c = parts[i];

                if ("inherit".equals(c)) {
                    i++;
                    String n = parts[i];

                    inherit = resolver.apply(n);
                }

                i++;
            }
        }

        // parse second control line
        // TODO: for alignment and shit

        String cl2 = reader.collectln();

        { }

        if (reader.current() == StringReader.DONE) {
            // build early
            SimpleCommentFactory scf = new SimpleCommentFactory();
            scf.delegate = inherit;
            scf.controlStrings = new String[] { cl1, cl2 };
            return scf;
        }

        // parse lines
        BodyLineFactory slBody = parseBLFSimple(reader.collectln());

        String mlStart = reader.collectln();
        BodyLineFactory mlBody = parseBLFSimple(reader.collectln());
        String mlEnd = reader.collectln();

        // build
        SimpleCommentFactory scf = new SimpleCommentFactory();
        scf.slBody = slBody;
        scf.mlStart = mlStart;
        scf.mlBody = mlBody;
        scf.mlEnd = mlEnd;
        scf.delegate = inherit;
        scf.controlStrings = new String[] { cl1, cl2 };
        return scf;
    }

    public static BodyLineFactory parseBLFSimple(String s) {
        return line -> s.replace("${}", line);
    }

}
