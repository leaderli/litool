package io.leaderli.litool.ast;

import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.ast.body.ClassOrInterfaceDeclaration;
import com.github.javaparser.ast.body.VariableDeclarator;
import com.github.javaparser.ast.expr.SimpleName;
import com.github.javaparser.ast.type.ClassOrInterfaceType;
import com.github.javaparser.ast.type.Type;
import io.leaderli.litool.core.io.FileUtil;
import io.leaderli.litool.core.meta.Lira;
import io.leaderli.litool.core.type.ClassScanner;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.Map;
import java.util.Optional;

class SourceCodeUtilTest {


    @Test
    void getClassSource() {

        Assertions.assertNotNull(SourceCodeUtil.getClassSource(SourceCodeUtil.class));
    }

    @Test
    void testClassAndSource() {

        Map<Class<?>, CompilationUnit> classAndSource = SourceCodeUtil.classAndSource(SourceCodeUtil.getSources());

        Assertions.assertEquals(SourceCodeUtil.class.getSimpleName(), classAndSource.get(SourceCodeUtil.class).getType(0).getNameAsString());
    }

    @Test
    void getImportClassByName() {
        CompilationUnit cu = SourceCodeUtil.getClassSource(TestBean.class, FileUtil.getWorkDir() + "/src/test/java/");
    }

    @Test
    void test() {


        Assertions.assertFalse(SourceCodeUtil.getSources().isEmpty());

        CompilationUnit cu = Lira.of(SourceCodeUtil.getSources()).filter(f -> f.findFirst(SimpleName.class, s -> s.toString().equals(CompilationUnit.class.getSimpleName()))).first().get();

        Assertions.assertTrue(SourceCodeUtil.isImport(cu, CompilationUnit.class));


        System.out.println(SourceCodeUtil.getClassDeclare(cu, String.class));
    }

    @Test
    void test2() {

        ClassOrInterfaceDeclaration classOrInterfaceDeclaration = Lira.of(SourceCodeUtil.getSources(FileUtil.getWorkDir() + "/src/test/java"))
                .map(cu -> cu.getClassByName(TestBean.class.getSimpleName()))
                .filter(o -> o)
                .map(Optional::get)
                .first()
                .get();
        for (VariableDeclarator variableDeclarator : classOrInterfaceDeclaration.findAll(VariableDeclarator.class)) {
            System.out.println(variableDeclarator.getTypeAsString());
            Type type = variableDeclarator.getType();
            System.out.println(((ClassOrInterfaceType) type).getNameWithScope());
            System.out.println(type);

        }


    }


    @Test
    void test111() {
        String packageName = CompilationUnit.class.getPackage().getName();

        ClassScanner classScanner = new ClassScanner(packageName);
        for (Class<?> aClass : classScanner.scan()) {
            System.out.println(aClass);

        }
    }
}
