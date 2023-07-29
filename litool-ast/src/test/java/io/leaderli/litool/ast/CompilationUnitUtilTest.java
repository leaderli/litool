package io.leaderli.litool.ast;

import com.github.javaparser.ast.CompilationUnit;
import org.junit.jupiter.api.Test;

class CompilationUnitUtilTest {
    @Test
    void testMethodAndSource() {

        CompilationUnit cu = SourceCodeUtil.getClassSource(CompilationUnitUtil.class);
        System.out.println(CompilationUnitUtil.methodAndSource(cu));
    }
}
