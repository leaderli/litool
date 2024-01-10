package io.leaderli.litool.core.io;

import java.io.OutputStream;
import java.util.function.Consumer;

public class LineOutputStream extends OutputStream {

    private StringBuilder stringBuilder = new StringBuilder();

    private final Consumer<String> readLine;
    private final boolean skipLF;

    public LineOutputStream() {
        this.readLine = System.out::println;
        this.skipLF = true;
    }

    public LineOutputStream(Consumer<String> readLine) {
        this.readLine = readLine;
        this.skipLF = true;
    }

    public LineOutputStream(Consumer<String> readLine, boolean skipLF) {
        this.readLine = readLine;
        this.skipLF = skipLF;
    }

    @Override
    public void write(int b) {

        char ch = (char) b;

        if (ch != '\n') {
            if (ch == '\r' && skipLF) {
                return;
            }
            stringBuilder.append(ch);
        } else {
            readLine.accept(stringBuilder.toString());
            stringBuilder = new StringBuilder();
        }
    }

    @Override
    public void flush() {
        if (stringBuilder.length() > 0) {
            readLine.accept(stringBuilder.toString());
            stringBuilder = new StringBuilder();
        }
    }
}
