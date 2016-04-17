package Obfuscator;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;

import com.github.javaparser.ASTHelper;
import com.github.javaparser.JavaParser;
import com.github.javaparser.ParseException;
import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.ast.body.BodyDeclaration;
import com.github.javaparser.ast.body.MethodDeclaration;
import com.github.javaparser.ast.body.TypeDeclaration;
import com.github.javaparser.ast.expr.FieldAccessExpr;
import com.github.javaparser.ast.expr.MethodCallExpr;
import com.github.javaparser.ast.expr.NameExpr;
import com.github.javaparser.ast.expr.StringLiteralExpr;
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
		
		//System.out.println(cu.toString());
		
		
	}
	
    private void changeMethods(CompilationUnit cu) {
    	
        List<TypeDeclaration> types = cu.getTypes();
        
        for (TypeDeclaration type : types) {
            List<BodyDeclaration> members = type.getMembers();
            for (BodyDeclaration member : members) {
            	//System.out.println(member.toString() + "    HAHA");
                if (member instanceof MethodDeclaration) {
                    MethodDeclaration method = (MethodDeclaration) member;
                    addToMethod(method, null);
                }
            }
        }
    }

	
	
	private void addToMethod(MethodDeclaration method, Statement statement) {

    	
		BlockStmt body = method.getBody();
		
		
		
        NameExpr clazz = new NameExpr("System");
        FieldAccessExpr field = new FieldAccessExpr(clazz, "out");
        MethodCallExpr call = new MethodCallExpr(field, "println");
        ASTHelper.addArgument(call, new StringLiteralExpr("Hello World!"));
        ASTHelper.addStmt(body, call);

		
        System.out.println(cu.toString());
		
		for(Statement stmt : body.getStmts()){
			//System.out.println(stmt.toString() + "XXX");
			//System.out.println(stmt.getBeginLine() + ", " + stmt.getEndLine() + ", " + stmt.getBeginColumn() + ", " + stmt.getEndColumn());
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
