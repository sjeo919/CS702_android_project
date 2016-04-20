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
import com.github.javaparser.ast.body.ClassOrInterfaceDeclaration;
import com.github.javaparser.ast.body.MethodDeclaration;
import com.github.javaparser.ast.body.ModifierSet;
import com.github.javaparser.ast.body.Parameter;
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
	private Random random;
	
	public LayoutExtraCode(){
		random = new Random();
	}
	
	public String addDebugInformation(String fileContents){
		initializeCompilationUnit(fileContents);
		//iterates through each method of the compilationUnit and 
		//then iterates through the method body's statements to add
		//random "log" messages.
		changeMethods(true);

		return cu.toString();
	}
	
	
	
	
	public String addNewMethods(String fileContents){
		
		initializeCompilationUnit(fileContents);
		
        //ASTHelper.addMember(cu.getTypes().get(0), method);
		
		
		
		changeMethods(false);
		return cu.toString();
	}
	
	
    private void changeMethods(boolean randomDebug) {
    	
        List<TypeDeclaration> types = cu.getTypes();

        for (TypeDeclaration type : types) {
            List<BodyDeclaration> members = type.getMembers();
            System.out.println(members);
            if(!randomDebug){
            	//members.add(generateRandomMethod());

                MethodDeclaration method = new MethodDeclaration(ModifierSet.PUBLIC, ASTHelper.VOID_TYPE, "random");
//                method.setModifiers(ModifierSet.addModifier(method.getModifiers(), ModifierSet.STATIC));
                addToMethod(method, null);
               
                MethodCallExpr call = randomLogStatement();
        		method.getBody().getStmts().add(0, new ExpressionStmt(call));
                members.add(method);
            }else{
            	for (BodyDeclaration member : members) {
                    if (member instanceof MethodDeclaration) {
                        MethodDeclaration method = (MethodDeclaration) member;
                        addToMethod(method, null);
                    }
                }
            }
            
        }
    }

	
	
	private void addToMethod(MethodDeclaration method, Statement statement) {
		BlockStmt body = method.getBody();
		        
		if (body==null) {
			body = new BlockStmt();
			method.setBody(body);
		}
		if (body.getStmts()==null) {
			body.setStmts(new ArrayList<Statement>());
		}
		
		
        for(int i = 0; i < body.getStmts().size(); i++){
        	if(random.nextInt(10) > 4){
        		MethodCallExpr call = randomLogStatement();
        		body.getStmts().add(i, new ExpressionStmt(call));
        	}
        }
	}

	private MethodCallExpr randomLogStatement(){
		String[] logTypes = new String[]{"v", "d", "i", "w", "e"};
		String logType = logTypes[new Random().nextInt(5)];
		
        NameExpr clazz = new NameExpr("Log");
        MethodCallExpr call = new MethodCallExpr(clazz, logType);
        ASTHelper.addArgument(call, new StringLiteralExpr(generateRandomString()));
        ASTHelper.addArgument(call, new StringLiteralExpr(generateRandomString()));

        return call;
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
	
	private MethodDeclaration generateRandomMethod(){
		//initialize a method
		MethodDeclaration method = new MethodDeclaration();
		//add parameters to the method
		ASTHelper.addParameter(method, new Parameter());
		
		
		BlockStmt body = method.getBody();
        
		if (body==null) {
			body = new BlockStmt();
			method.setBody(body);
		}
		if (body.getStmts()==null) {
			body.setStmts(new ArrayList<Statement>());
		}
		
		
		return method;
	}
	
	private String generateRandomString(){
		int length = 20;
		String characters = "abcdefghijklmnopqrstuvwxyz";
	    char[] text = new char[length];
	    for (int i = 0; i < length; i++)
	    {
	        text[i] = characters.charAt(random.nextInt(characters.length()));
	    }
	    return new String(text);
	}
	
	public void randomrandom(){
		
        ClassOrInterfaceDeclaration type = new ClassOrInterfaceDeclaration(ModifierSet.PUBLIC, false, "GeneratedClass");
        ASTHelper.addTypeDeclaration(cu, type);

        // create a method

		
		
	}
	
}
