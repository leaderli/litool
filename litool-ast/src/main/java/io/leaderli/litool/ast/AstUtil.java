package io.leaderli.litool.ast;

import com.github.javaparser.ParserConfiguration;
import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.ast.ImportDeclaration;
import com.github.javaparser.utils.SourceRoot;
import io.leaderli.litool.core.exception.LiAssertUtil;

import java.io.IOException;
import java.nio.file.Paths;
import java.util.List;

public class AstUtil {

    public static List<CompilationUnit> getSources(String sourceDir) {
        ParserConfiguration parserConfiguration = new ParserConfiguration();
        parserConfiguration.setLanguageLevel(ParserConfiguration.LanguageLevel.JAVA_8);
        return getSources(parserConfiguration, sourceDir);
    }

    public static List<CompilationUnit> getSources(ParserConfiguration parserConfiguration, String sourceDir) {
        SourceRoot sourceRoot = new SourceRoot(Paths.get(sourceDir), parserConfiguration);
        try {
            sourceRoot.tryToParse();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        List<CompilationUnit> compilationUnits = sourceRoot.getCompilationUnits();

        LiAssertUtil.assertTrue(!compilationUnits.isEmpty(), new IllegalStateException(sourceRoot.toString()));
        return compilationUnits;
    }

    public static List<CompilationUnit> getSources() {
        ParserConfiguration parserConfiguration = new ParserConfiguration();
        parserConfiguration.setLanguageLevel(ParserConfiguration.LanguageLevel.JAVA_8);
        return getSources(parserConfiguration);
    }

    public static List<CompilationUnit> getSources(ParserConfiguration parserConfiguration) {
        return getSources(parserConfiguration, System.getProperty("user.dir") + "/src");
    }

    public static boolean isImport(CompilationUnit cu, Class<?> cls) {
        String name = cls.getName();
        return !cu.findAll(ImportDeclaration.class, im -> {
            String importName = im.getNameAsString();
            return importName.equals(name) || im.isAsterisk() && name.startsWith(importName);
        }).isEmpty();
    }
}
