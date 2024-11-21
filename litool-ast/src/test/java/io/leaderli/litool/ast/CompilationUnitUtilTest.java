package io.leaderli.litool.ast;

import com.github.javaparser.ast.CompilationUnit;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

class CompilationUnitUtilTest {
    @Test
    void testMethodAndSource() {

        CompilationUnit cu = SourceCodeUtil.getClassSource(CompilationUnitUtil.class);
        Assertions.assertTrue(cu.toString().startsWith("package"));
    }
}
