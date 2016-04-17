package Obfuscator;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;

import com.github.javaparser.JavaParser;
import com.github.javaparser.ParseException;
import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.ast.body.BodyDeclaration;
import com.github.javaparser.ast.body.MethodDeclaration;
import com.github.javaparser.ast.body.TypeDeclaration;
import com.github.javaparser.ast.stmt.BlockStmt;
import com.github.javaparser.ast.stmt.Statement;

public class TestClass {
	
	FileInputStream in;
	CompilationUnit cu;
	
	public TestClass(){
		try {
			in = new FileInputStream("./src/Obfuscator/TestClass.java");
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		try {
			cu = JavaParser.parse(in);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		
		changeMethods(cu);
		
		
		
		
	}
	
    private void changeMethods(CompilationUnit cu) {
        List<TypeDeclaration> types = cu.getTypes();
        for (TypeDeclaration type : types) {
            List<BodyDeclaration> members = type.getMembers();
            for (BodyDeclaration member : members) {
                if (member instanceof MethodDeclaration) {
                    MethodDeclaration method = (MethodDeclaration) member;
                    addToMethod(method, null);
                }
            }
        }
    }

	
	
	private void addToMethod(MethodDeclaration method, Statement statement) {
		BlockStmt body = method.getBody();
		System.out.println(body.toString());
		
		for(Statement stmt : body.getStmts()){
			
			System.out.println(stmt.getBeginLine() + ", " + stmt.getEndLine() + ", " + stmt.getBeginColumn() + ", " + stmt.getEndColumn());
		}
		
		if (body==null) {
			body = new BlockStmt();
			method.setBody(body);
		}
		if (body.getStmts()==null) {
			body.setStmts(new ArrayList<Statement>());
		}
		body.getStmts().add(statement);
	}


}
