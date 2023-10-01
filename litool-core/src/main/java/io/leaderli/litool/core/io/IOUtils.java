package io.leaderli.litool.core.io;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public class IOUtils {
    public static final int DEFAULT_BUFFER_SIZE = 8192;
    public static final int EOF = -1;


    public static void copy(InputStream source, OutputStream target) throws IOException {
        copy(DEFAULT_BUFFER_SIZE, source, target);
    }

    public static void copy(int buf_size, InputStream source, OutputStream target) throws IOException {
        byte[] buf = new byte[buf_size];
        int length;
        while ((length = source.read(buf)) != -1) {
            target.write(buf, 0, length);
        }
    }

    public static void copy(InputStream source, OutputStream... targets) throws IOException {
        copy(DEFAULT_BUFFER_SIZE, source, targets);
    }

    public static void copy(int buf_size, InputStream source, OutputStream... targets) throws IOException {
        byte[] buf = new byte[buf_size];
        int length;
        while ((length = source.read(buf)) != -1) {
            for (OutputStream target : targets) {
                target.write(buf, 0, length);
            }
        }
    }

}
