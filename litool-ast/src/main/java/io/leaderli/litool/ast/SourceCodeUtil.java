package io.leaderli.litool.ast;

import com.github.javaparser.ParserConfiguration;
import com.github.javaparser.StaticJavaParser;
import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.ast.body.VariableDeclarator;
import com.github.javaparser.ast.expr.SimpleName;
import com.github.javaparser.ast.nodeTypes.NodeWithName;
import com.github.javaparser.ast.type.Type;
import com.github.javaparser.printer.YamlPrinter;
import com.github.javaparser.utils.SourceRoot;
import io.leaderli.litool.core.exception.LiAssertUtil;
import io.leaderli.litool.core.meta.Lino;
import io.leaderli.litool.core.meta.Lira;
import io.leaderli.litool.core.resource.ResourceUtil;

import java.io.File;
import java.io.FileNotFoundException;
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
        return getSources(parserConfiguration, ResourceUtil.getWorkDir() + "/src");
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

    /**
     * @param sources 所有的源码单元
     * @return 类及源码单元的map
     */
    public static Map<Class<?>, CompilationUnit> classAndSource(List<CompilationUnit> sources) {
        return Lira.of(sources).toMap(c ->
                        Lino.optional(c.getType(0).getFullyQualifiedName())
                                .mapIgnoreError(Class::forName)
                                .get(),
                c -> c);
    }

    /**
     * 源码目录假定为工作目录下的 src/main/java
     *
     * @param clazz 类
     * @return 返回类的源码单元
     * @see #getClassSource(Class, String)
     */
    public static CompilationUnit getClassSource(Class<?> clazz) {
        return getClassSource(clazz, ResourceUtil.getWorkDir() + "/src/main/java/");
    }

    /**
     * @param clazz      类
     * @param sourcePath 源码目录
     * @return 返回类的源码单元
     */
    public static CompilationUnit getClassSource(Class<?> clazz, String sourcePath) {

        String javaFile = sourcePath + clazz.getName().replace(".", "/") + ".java";

        try {
            return StaticJavaParser.parse(new File(javaFile));
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }
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
        return Lira.of(cu.getImports()).filter(im -> {
            String importName = im.getNameAsString();
            return importName.equals(name) || im.isAsterisk() && name.startsWith(importName);
        }).present();
    }

    /**
     * 如果类名不包含包名，则查询源码单元中的import语句
     *
     * @param cu   源码单元
     * @param name 类名
     * @return 返回类
     */
    public static Class<?> getImportClassByName(CompilationUnit cu, String name) {

        // 查找使用类名的代码
        cu.findAll(SimpleName.class, n -> {
            String nameAsString = n.asString();

//            System.out.println(name+"   "+nameAsString);
            if (nameAsString.equals(name)) {

//                System.out.println(n.getParentNode().get());
                System.out.println(name);
            }
            return false;
        });
        return Lira.of(cu.getImports())
                .filter(i -> name.equals(i.getName().getIdentifier()))
                .map(NodeWithName::getNameAsString)
                .mapIgnoreError(Class::forName)
                .first().get();
    }

    public static Lira<SimpleName> getClassDeclare(CompilationUnit cu, Class<?> cls) {

        return Lira.of(cu.findAll(VariableDeclarator.class, v -> {
            Type type = v.getType();
            System.out.println(type.asString());
//            return type == cls;
            return true;
        })).map(VariableDeclarator::getName);
    }

    /**
     * 按照 yaml 的格式打印
     *
     * @param cu 源码单元
     */
    public static void printYaml(CompilationUnit cu) {
        YamlPrinter printer = new YamlPrinter(true);
        System.out.println(printer.output(cu));
    }
}
