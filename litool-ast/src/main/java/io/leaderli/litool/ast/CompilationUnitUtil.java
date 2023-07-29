package io.leaderli.litool.ast;

import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.ast.body.MethodDeclaration;
import com.github.javaparser.ast.body.TypeDeclaration;
import io.leaderli.litool.core.meta.Lino;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

public class CompilationUnitUtil {


    public static Map<Method, MethodDeclaration> methodAndSource(CompilationUnit cu) {
        Map<Method, MethodDeclaration> declaretion = new HashMap<>();
        for (TypeDeclaration<?> type : cu.getTypes()) {
            Class<?> cls = Lino.optional(type.getFullyQualifiedName()).mapIgnoreError(Class::forName).get();
            for (MethodDeclaration method : type.getMethods()) {
                System.out.println(method.getNameAsString());

//                declaretion.put(method, method);
            }
        }
//        cu.getType(0).getMethods().var;

        return null;
    }
}
