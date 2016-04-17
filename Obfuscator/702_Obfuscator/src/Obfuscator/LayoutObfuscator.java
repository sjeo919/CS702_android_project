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
import com.github.javaparser.ast.ImportDeclaration;
import com.github.javaparser.ast.Node;
import com.github.javaparser.ast.PackageDeclaration;
import com.github.javaparser.ast.body.AnnotationDeclaration;
import com.github.javaparser.ast.body.AnnotationMemberDeclaration;
import com.github.javaparser.ast.body.BodyDeclaration;
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
import com.github.javaparser.ast.expr.AnnotationExpr;
import com.github.javaparser.ast.expr.ArrayAccessExpr;
import com.github.javaparser.ast.expr.ArrayCreationExpr;
import com.github.javaparser.ast.expr.ArrayInitializerExpr;
import com.github.javaparser.ast.expr.AssignExpr;
import com.github.javaparser.ast.expr.BinaryExpr;
import com.github.javaparser.ast.expr.EnclosedExpr;
import com.github.javaparser.ast.expr.FieldAccessExpr;
import com.github.javaparser.ast.expr.MemberValuePair;
import com.github.javaparser.ast.expr.MethodCallExpr;
import com.github.javaparser.ast.expr.MethodReferenceExpr;
import com.github.javaparser.ast.expr.NameExpr;
import com.github.javaparser.ast.expr.QualifiedNameExpr;
import com.github.javaparser.ast.expr.UnaryExpr;
import com.github.javaparser.ast.expr.VariableDeclarationExpr;
import com.github.javaparser.ast.stmt.BlockStmt;
import com.github.javaparser.ast.stmt.ExpressionStmt;
import com.github.javaparser.ast.type.ClassOrInterfaceType;
import com.github.javaparser.ast.visitor.ModifierVisitorAdapter;
import com.github.javaparser.ast.visitor.VoidVisitor;
import com.github.javaparser.ast.visitor.VoidVisitorAdapter;

/**
 * Class that provides various utility methods to obfuscate the layout of a java file.
 * It uses the JavaParser open source library (https://github.com/javaparser) to 
 * parse and manipulate the file contents.
 * @author Jonny Lu
 */
public class LayoutObfuscator {
	
	private List<CompilationUnit> fileASTs;
	private List<String> globalTypeList;
	private List<String> globalNonUserTypeList;
	private List<String> globalMethodList;
	private List<String> globalVariableList;
	private Map<String, String> globalNameMap;
	private int stringGenID = 0;
	
	public LayoutObfuscator() {
		fileASTs = new ArrayList<CompilationUnit>();
		globalTypeList = new ArrayList<String>();
		globalNonUserTypeList = new ArrayList<String>();
		globalMethodList = new ArrayList<String>();
		globalVariableList = new ArrayList<String>();
		globalNameMap = new HashMap<String, String>();
		stringGenID = 0;
	}

	public List<FileModel> Obfuscate(List<FileModel> programFiles) throws ParseException, IOException {

		// Parse each program file into an abstract syntax tree using JavaParser library.
		for (int i = 0; i < programFiles.size(); i++) {
			InputStream in = new ByteArrayInputStream(programFiles.get(i).getFileContentBefore().getBytes());
			try {
				fileASTs.add(JavaParser.parse(in));
			} finally {
				in.close();
			}
		}
		
		for (CompilationUnit fileAST : fileASTs) {
		
			// Search for all classes/interfaces(types) and register them in the
			// global name space.
			new TypeRegisterVisitor().visit(fileAST, null);
		
		}
        
		for (CompilationUnit fileAST : fileASTs) {
			
			//CompilationUnit fileAST = fileASTs.get(4);
			
			// Remove comments from each file
			List<Node> ASTs = new ArrayList<Node>();
			ASTs.add(fileAST);
			recursiveRemoveComment(ASTs);
			
			// Register types that implements/extends non-user-defined types from the
			// name space.
			new NonUserTypeRegisterVisitor().visit(fileAST, null);
			
			// Register all methods from the user-defined types.
			MethodRegisterVisitor m = new MethodRegisterVisitor();
					m.visit(fileAST, null);
			
			// Register all fields and variables from the user-defined types.
			new VariableRegisterVisitor().visit(fileAST, null);
			
		}
		
		for (String name : globalTypeList) {
			globalNameMap.put(name, generateUniqueString());
		}
		resetStringGen();
		for (String name : globalMethodList) {
			globalNameMap.put(name, generateUniqueString());
		}
		resetStringGen();
		for (String name : globalVariableList) {
			globalNameMap.put(name, generateUniqueString());
		}
		resetStringGen();
		
		for (CompilationUnit fileAST : fileASTs) {
			new NameChangeVisitor().visit(fileAST, null);
			//System.out.println(fileAST.toString());
		}
			
        for (int i = 0; i < fileASTs.size(); i++) {
        	programFiles.get(i).setFileContentAfter((fileASTs.get(i).toString()));
        }
		return programFiles;
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
    
    private class TypeRegisterVisitor extends VoidVisitorAdapter<Object> {
    	
    	@Override 
		public void visit(final ClassOrInterfaceDeclaration d, Object arg) {
    		super.visit(d, arg);
			String typeName = d.getName();
			if (!globalTypeList.contains(typeName))
				globalTypeList.add(typeName);
		}  	
    }
    
    private class NonUserTypeRegisterVisitor extends VoidVisitorAdapter<Object> {
    	
    	@Override 
		public void visit(final ClassOrInterfaceDeclaration d, Object arg) {
    		super.visit(d, arg);
    		
    		// Determines whether the type is user-defined. A type is user-defined
    		// only if it does not implement or extend external types.
    		boolean isUserDefined = true;
			for (ClassOrInterfaceType t : d.getExtends()) {
				if (!globalTypeList.contains(t.getName())) {
					isUserDefined = false;
				}
			}
			for (ClassOrInterfaceType t : d.getImplements()) {
				if (!globalTypeList.contains(t.getName())) {
					isUserDefined = false;
				}
			}
			if (!isUserDefined) {
				globalNonUserTypeList.add(d.getName());
			}
		}  	
    }
    
    private class MethodRegisterVisitor extends VoidVisitorAdapter<Object> {
    	
    	@Override 
		public void visit(final ClassOrInterfaceDeclaration d, Object arg) {
    		super.visit(d, arg);
    		if (!globalNonUserTypeList.contains(d.getName())) {
				for (BodyDeclaration bd : d.getMembers()) {
					if (bd instanceof MethodDeclaration) {
						globalMethodList.add(((MethodDeclaration)bd).getName());
					}
				}
    		}	
		}
    	
    }
    
    private class VariableRegisterVisitor extends VoidVisitorAdapter<Object> {
    	
    	@Override 
		public void visit(final VariableDeclaratorId id, Object arg) {
    		super.visit(id, arg);
			if (!globalVariableList.contains(id.getName())) {
				globalVariableList.add(id.getName());
			}
		}
    	
    }
    
    private class NameChangeVisitor extends VoidVisitorAdapter<Object> {
    	    	
    	@Override 
    	public void visit(final MethodDeclaration d, Object arg) {
    		super.visit(d, arg);
    		if (globalNameMap.containsKey(d.getName())) {
    			d.setName(globalNameMap.get(d.getName()));
    		}
    	}
    	
    	@Override 
    	public void visit(final MethodCallExpr d, Object arg) {
    		super.visit(d, arg);
    		if (globalNameMap.containsKey(d.getName())) {
    			d.setName(globalNameMap.get(d.getName()));
    		}
    	}
    	
    	@Override 
    	public void visit(final NameExpr d, Object arg) {
    		super.visit(d, arg);
    		if (globalNameMap.containsKey(d.getName())) {
    			d.setName(globalNameMap.get(d.getName()));
    		}
    	}
    	
    	@Override 
    	public void visit(final VariableDeclaratorId d, Object arg) {
    		super.visit(d, arg);
    		if (globalNameMap.containsKey(d.getName())) {
    			d.setName(globalNameMap.get(d.getName()));
    		}
    	}
    	
    	@Override 
    	public void visit(final ClassOrInterfaceDeclaration d, Object arg) {
    		super.visit(d, arg);
    		if (globalNameMap.containsKey(d.getName())) {
    			d.setName(globalNameMap.get(d.getName()));
    		}
    	}
    	
    	@Override 
    	public void visit(final ClassOrInterfaceType d, Object arg) {
    		super.visit(d, arg);
    		if (globalNameMap.containsKey(d.getName())) {
    			d.setName(globalNameMap.get(d.getName()));
    		}
    	}
    	
    	@Override 
    	public void visit(final ConstructorDeclaration d, Object arg) {
    		super.visit(d, arg);
    		if (globalNameMap.containsKey(d.getName())) {
    			d.setName(globalNameMap.get(d.getName()));
    		}
    	}
    	
    }
    
}
