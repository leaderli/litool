package io.leaderli.litool.ast;

import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.ast.expr.SimpleName;
import io.leaderli.litool.core.meta.Lira;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

class AstUtilTest {

    @Test
    void test() {


        Assertions.assertFalse(AstUtil.getSources().isEmpty());

        CompilationUnit cu = Lira.of(AstUtil.getSources()).filter(f -> f.findFirst(SimpleName.class, s -> s.toString().equals(CompilationUnit.class.getSimpleName()))).first().get();

        Assertions.assertTrue(AstUtil.isImport(cu, CompilationUnit.class));

    }

}
