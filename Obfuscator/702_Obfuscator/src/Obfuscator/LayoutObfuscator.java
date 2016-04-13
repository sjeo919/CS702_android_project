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
	
	private CompilationUnit cu;

	private int stringGenID = 0;
	private Map<String, String> globalNameMap = new HashMap<String, String>();

	
	/**
	 * Parse the source file into an abstract syntax tree using the JavaParser library. 
	 * @param javaSourceFile
	 * @throws ParseException 
	 * @throws IOException 
	 */
	
	public String Obfuscate(String fileContents) throws ParseException, IOException {
		
		InputStream in = new ByteArrayInputStream(fileContents.getBytes());
        try {
            // parse the file
            cu = JavaParser.parse(in);
        } finally {
            in.close();
        }
        
        //add implementation, methods calls here.
		
		return cu.toString(); // this should return the final obfuscated code from this class
	}
	
	/**
	 * Call this method to remove all comments in the parsed source file.
	 */
	public void removeComments() {
		
		List<Node> ast = new ArrayList<Node>();
		ast.add(cu);
		recursiveRemoveComment(ast);
	}
	
	/**
	 * Recursively traverse all nodes in the AST and remove related 'comment nodes' from each.
	 * @param nodes
	 */
	private void recursiveRemoveComment(List<Node> nodes) {
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
