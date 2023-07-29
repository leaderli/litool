package io.leaderli.litool.ast;

import com.github.javaparser.ParserConfiguration;
import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.ast.ImportDeclaration;
import com.github.javaparser.ast.body.VariableDeclarator;
import com.github.javaparser.ast.expr.SimpleName;
import com.github.javaparser.ast.type.Type;
import com.github.javaparser.utils.SourceRoot;
import io.leaderli.litool.core.exception.LiAssertUtil;
import io.leaderli.litool.core.meta.Lino;
import io.leaderli.litool.core.meta.Lira;

import java.io.IOException;
import java.nio.file.Paths;
import java.util.List;
import java.util.Map;

public class SourceCodeUtil {

    /**
     * 解析工具目录的src目录的源码
     *
     * @return 源码目录的所有源码单元
     */
    public static List<CompilationUnit> getSources() {
        ParserConfiguration parserConfiguration = new ParserConfiguration();
        parserConfiguration.setLanguageLevel(ParserConfiguration.LanguageLevel.JAVA_8);
        return getSources(parserConfiguration);
    }

    /**
     * 解析工具目录的src目录的源码
     *
     * @param parserConfiguration 解析规则
     * @return 源码目录的所有源码单元
     */
    public static List<CompilationUnit> getSources(ParserConfiguration parserConfiguration) {
        return getSources(parserConfiguration, System.getProperty("user.dir") + "/src");
    }

    /**
     * @param parserConfiguration 解析规则
     * @param sourceDir           源码目录,需要绝对目录
     * @return 源码目录的所有源码单元
     */
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

    public static Map<Class<?>, CompilationUnit> classAndSource(List<CompilationUnit> sources) {
        return Lira.of(sources).toMap(c ->
                        Lino.optional(c.getType(0).getFullyQualifiedName())
                                .mapIgnoreError(Class::forName)
                                .get(),
                c -> c);
    }

    /**
     * @param sourceDir 源码目录,需要绝对目录
     * @return 源码目录的所有源码单元
     */
    public static List<CompilationUnit> getSources(String sourceDir) {
        ParserConfiguration parserConfiguration = new ParserConfiguration();
        parserConfiguration.setLanguageLevel(ParserConfiguration.LanguageLevel.JAVA_8);
        return getSources(parserConfiguration, sourceDir);
    }

    /**
     * @param cu  源码
     * @param cls 类
     * @return 判断类是否通过import的方式导入了，如果导入了通配符，那么也视为导入。但是不包括全限定名
     */
    public static boolean isImport(CompilationUnit cu, Class<?> cls) {
        String name = cls.getName();
        return !cu.findAll(ImportDeclaration.class, im -> {
            String importName = im.getNameAsString();
            return importName.equals(name) || im.isAsterisk() && name.startsWith(importName);
        }).isEmpty();
    }

    public static Lira<SimpleName> getClassDeclare(CompilationUnit cu, Class<?> cls) {

        return Lira.of(cu.findAll(VariableDeclarator.class, v -> {
            Type type = v.getType();
            System.out.println(type.asString());
//            return type == cls;
            return true;
        })).map(VariableDeclarator::getName);
    }
}
