package Obfuscator;

import java.io.ByteArrayInputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.ast.body.BodyDeclaration;
import com.github.javaparser.ast.body.MethodDeclaration;
import com.github.javaparser.ast.body.TypeDeclaration;
import com.github.javaparser.ast.expr.FieldAccessExpr;
import com.github.javaparser.ast.expr.MethodCallExpr;
import com.github.javaparser.ast.expr.NameExpr;
import com.github.javaparser.ast.expr.StringLiteralExpr;
import com.github.javaparser.ast.stmt.BlockStmt;
import com.github.javaparser.ast.stmt.ExpressionStmt;
import com.github.javaparser.ast.stmt.Statement;
import com.github.javaparser.ASTHelper;
import com.github.javaparser.JavaParser;
import com.github.javaparser.ParseException;

public class LayoutExtraCode {

	private CompilationUnit cu;
	
	public String addDebugInformation(String fileContents){
		
		

		initializeCompilationUnit(fileContents);
		
    	
		
		changeMethods();
		
		
		

		
		return cu.toString();
	}
	
	
	
	
	public String addNewMethods(String fileContents){
		
		return cu.toString();
	}
	
	
    private void changeMethods() {
    	
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
		
		int random = getRandomIndex(body.getStmts().size());
        System.out.println(random);
        
        
		if (body==null) {
			body = new BlockStmt();
			method.setBody(body);
		}
		
		if (body.getStmts()==null) {
			System.out.println("does it go here");
			body.setStmts(new ArrayList<Statement>());
		}
		
//		System.out.println(body.getStmts());
		
        NameExpr clazz = new NameExpr("System");
        FieldAccessExpr field = new FieldAccessExpr(clazz, "out");
        MethodCallExpr call = new MethodCallExpr(field, "println");
        ASTHelper.addArgument(call, new StringLiteralExpr("Hello World!"));
        
        System.out.println(call.getName());
        
        //ASTHelper.addStmt(body, call);
        random = getRandomIndex(body.getStmts().size());
        System.out.println(body.getStmts().size());
        body.getStmts().add(random, new ExpressionStmt(call));
    
        
	}

	private MethodCallExpr addDebugStatement(){
		
		
		
		return null;
		
	} 

	
	
	
	
	
	
	
	
	private void initializeCompilationUnit(String fileContents){
		InputStream in = null;
		//InputStream in = new ByteArrayInputStream(fileContents.getBytes());
				
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
		
	}
	
	public int getRandomIndex(int size){
		Random rand = new Random();
	    int randomNum = rand.nextInt(size + 1);

	    return randomNum;

	}
}
