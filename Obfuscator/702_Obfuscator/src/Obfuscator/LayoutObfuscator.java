package Obfuscator;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.github.javaparser.JavaParser;
import com.github.javaparser.ParseException;
import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.ast.Node;
import com.github.javaparser.ast.body.AnnotationDeclaration;
import com.github.javaparser.ast.body.AnnotationMemberDeclaration;
import com.github.javaparser.ast.body.ClassOrInterfaceDeclaration;
import com.github.javaparser.ast.body.ConstructorDeclaration;
import com.github.javaparser.ast.body.EmptyTypeDeclaration;
import com.github.javaparser.ast.body.FieldDeclaration;
import com.github.javaparser.ast.body.MethodDeclaration;
import com.github.javaparser.ast.body.TypeDeclaration;
import com.github.javaparser.ast.body.VariableDeclarator;
import com.github.javaparser.ast.body.VariableDeclaratorId;
import com.github.javaparser.ast.comments.BlockComment;
import com.github.javaparser.ast.comments.Comment;
import com.github.javaparser.ast.comments.JavadocComment;
import com.github.javaparser.ast.comments.LineComment;
import com.github.javaparser.ast.expr.FieldAccessExpr;
import com.github.javaparser.ast.expr.MemberValuePair;
import com.github.javaparser.ast.expr.MethodCallExpr;
import com.github.javaparser.ast.expr.MethodReferenceExpr;
import com.github.javaparser.ast.expr.NameExpr;
import com.github.javaparser.ast.expr.QualifiedNameExpr;
import com.github.javaparser.ast.stmt.BlockStmt;
import com.github.javaparser.ast.stmt.ExpressionStmt;
import com.github.javaparser.ast.type.ClassOrInterfaceType;
import com.github.javaparser.ast.visitor.ModifierVisitorAdapter;
import com.github.javaparser.ast.visitor.VoidVisitor;
import com.github.javaparser.ast.visitor.VoidVisitorAdapter;

/**
 * Class that provides various utility methods to obfuscate the layout of a java file.
 * @author Jonny Lu
 */
public class LayoutObfuscator {
	
	private LayoutObfuscator() {}
	
	/**
	 * The main layout obfuscation method. Takes a list of java source files (as strings) 
	 * that should be obfuscated together as input, and returns a list of obfuscated source
	 * files, in the same order.
	 */
	public static List<String> Obfuscate(List<String> programFiles) throws ParseException, IOException {
		
		List<CompilationUnit> fileASTs = new ArrayList<CompilationUnit>();
		Map<String, String> globalNameMap = new HashMap<String, String>();
		int stringGenID = 0;

		// Parse each program file into an abstract syntax tree using JavaParser library.
		for (String file : programFiles) {
			InputStream in = new ByteArrayInputStream(file.getBytes());
			try {
				fileASTs.add(JavaParser.parse(in));
			} finally {
				in.close();
			}
		}
        
		for (CompilationUnit fileAST : fileASTs) {
			
			// Remove comments from each file
			List<Node> ASTs = new ArrayList<Node>();
			ASTs.add(fileAST);
			recursiveRemoveComment(ASTs);
			
			// Search for all classes/interfaces(types) and register them in the
			// global name space.
			
			// Remove types that implements/extends non-user-defined types from the
			// name space.
			
			// Register all methods from the remaining user-defined types.
			
			// Register all fields/variable from the user-defined types.
			
			// Rename all method and variables, provided that they are not
			// invoked from a non-user-defined type.
			
			// Rename all user-defined types.
			
		}
		
		
        List<String> obfuscatedFiles = new ArrayList<String>();
        for (CompilationUnit fileAST : fileASTs) {
        	obfuscatedFiles.add(fileAST.toString());
        }
		return obfuscatedFiles; // this should return the final obfuscated code from this class
	}
	
	/**
	 * Recursively traverse all nodes in the AST and remove related 'comment nodes' from each.
	 * @param nodes
	 */
	private static void recursiveRemoveComment(List<Node> nodes) {
		if (nodes != null) {
			for (Node n : nodes) {
				n.setComment(null);
				for (Comment c : n.getOrphanComments()) {
					c.setParentNode(null);
				}
				n.getOrphanComments().clear();
				recursiveRemoveComment(n.getChildrenNodes());
			}
		}
	}
	
	public void obfuscateFieldNames() {
		new VariableRegisterVisitor().visit(cu, null);
		new NameChangeVisitor().visit(cu, null);
	}
	
	/**
	 * Generates a unique meaningless string that can be used to replace variable and method
	 * identifiers. The string is guaranteed to be unique to all previously generated ones 
	 * until MAX_INT strings are generated or resetStringGen() is called.
	 * @return
	 */
	private String generateUniqueString() {	
		String s = Integer.toString(stringGenID);
		stringGenID++;
		return "a" + s;
	}
	
	/**
	 * Resets the string generator.
	 */
	public void resetStringGen() {
		stringGenID = 0;
	}
    
    private class VariableRegisterVisitor extends VoidVisitorAdapter<Object> {
    	    	
    	@Override 
		public void visit(final VariableDeclaratorId v, Object arg) {
    		super.visit(v, arg);
			String variableName = v.getName();
			if (!globalNameMap.containsKey(variableName)) {
				String genString = generateUniqueString();
				globalNameMap.put(variableName, genString);
			}
		}
    	
    	/*@Override 
		public void visit(final MethodDeclaration m, Object arg) {
    		super.visit(m, arg);
			String methodName = m.getName();
			System.out.println(m.getType());
			if (!globalNameMap.containsKey(methodName)) {
				String genString = generateUniqueString();
				globalNameMap.put(methodName, genString);
			}
		}*/
    	
    }
    
    private class NameChangeVisitor extends VoidVisitorAdapter<Object> {
    	    	
    	@Override 
    	public void visit(final NameExpr n, Object arg) {
    		super.visit(n, arg);
    		if (globalNameMap.containsKey(n.getName())) {
				n.setName(globalNameMap.get(n.getName()));
			} 
    	}
    	
    	@Override 
		public void visit(final VariableDeclaratorId v, Object arg) {
    		super.visit(v, arg);
			if (globalNameMap.containsKey(v.getName())) {
				v.setName(globalNameMap.get(v.getName()));
			}
		}
    	
    	/*@Override 
		public void visit(final MethodCallExpr m, Object arg) {
    		super.visit(m, arg);
			String methodName = m.getName();
			//System.out.println(methodName);
			if (globalNameMap.containsKey(methodName)) {
				m.setName(globalNameMap.get(methodName));

			}
		}*/
    }
    
}
