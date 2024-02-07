package io.leaderli.litool.core.io;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;

class FileUtilTest {


    @Test
    void test() throws MalformedURLException, ClassNotFoundException {

        URLClassLoader urlClassLoader = URLClassLoader.newInstance(new URL[]{FileUtil.getJavaToolsJava().toURI().toURL()}, null);
        Assertions.assertNotNull(Class.forName("com.sun.tools.attach.VirtualMachine", false, urlClassLoader));
    }

}
