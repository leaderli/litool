package io.leaderli.litool.ast;

import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.ast.body.ClassOrInterfaceDeclaration;
import com.github.javaparser.ast.body.VariableDeclarator;
import com.github.javaparser.ast.expr.SimpleName;
import com.github.javaparser.ast.type.ClassOrInterfaceType;
import com.github.javaparser.ast.type.Type;
import io.leaderli.litool.core.meta.Lira;
import io.leaderli.litool.core.resource.ResourceUtil;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.Map;
import java.util.Optional;

class AstUtilTest {


    @Test
    void test1() {

        for (Map.Entry<Class<?>, CompilationUnit> entry : AstUtil.classAndSource(AstUtil.getSources()).entrySet()) {
            System.out.println(entry.getKey() + " " + entry.getValue().getClass());
        }
    }

    @Test
    void test() {


        Assertions.assertFalse(AstUtil.getSources().isEmpty());

        CompilationUnit cu = Lira.of(AstUtil.getSources()).filter(f -> f.findFirst(SimpleName.class, s -> s.toString().equals(CompilationUnit.class.getSimpleName()))).first().get();

        Assertions.assertTrue(AstUtil.isImport(cu, CompilationUnit.class));


        System.out.println(AstUtil.getClassDeclare(cu, String.class));
    }

    @Test
    void test2() {

        ClassOrInterfaceDeclaration classOrInterfaceDeclaration = Lira.of(AstUtil.getSources(ResourceUtil.getWorkDir() + "/src/test/java"))
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

}
