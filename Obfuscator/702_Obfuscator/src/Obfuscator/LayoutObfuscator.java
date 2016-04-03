package Obfuscator;

import java.io.File;
import java.io.FileInputStream;
import java.util.ArrayList;
import java.util.List;

import com.github.javaparser.JavaParser;
import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.ast.Node;
import com.github.javaparser.ast.body.VariableDeclarator;
import com.github.javaparser.ast.comments.BlockComment;
import com.github.javaparser.ast.comments.Comment;
import com.github.javaparser.ast.comments.JavadocComment;
import com.github.javaparser.ast.comments.LineComment;
import com.github.javaparser.ast.visitor.ModifierVisitorAdapter;

/**
 * Class that provides various utility methods to obfuscate the layout of a java file.
 * @author Jonny Lu
 */
public class LayoutObfuscator {
	
	private CompilationUnit cu;
	
	/**
	 * Parse the source file into an abstract syntax tree using the JavaParser library. 
	 * @param javaSourceFile
	 */
	public LayoutObfuscator(File javaSourceFile) throws Exception {
		
		FileInputStream in = new FileInputStream(javaSourceFile);
        try {
            // parse the file
            cu = JavaParser.parse(in);
        } finally {
            in.close();
        }
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
				n.getOrphanComments().clear();
				recursiveRemoveComment(n.getChildrenNodes());
			}
		}
	}
	
	/**
	 * Call this to get the obfuscated code as a string.
	 * @return
	 */
	public String getObfuscatedCode() {
		return cu.toString();
	}
}
