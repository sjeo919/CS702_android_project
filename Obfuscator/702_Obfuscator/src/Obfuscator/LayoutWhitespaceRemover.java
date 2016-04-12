package Obfuscator;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

public class LayoutWhitespaceRemover {
	
	private String fileContents;
	
	public LayoutWhitespaceRemover (File javaFile) throws IOException{
		fileContents = readFileAndRemoveWhitespace(javaFile.getAbsolutePath());
		System.out.println(fileContents);
	}	
	
	private String readFileAndRemoveWhitespace(String fileName) throws IOException {
	    BufferedReader br = new BufferedReader(new FileReader(fileName));
	    try {
	        StringBuilder sb = new StringBuilder();
	        String line = br.readLine();
	        
	        while (line != null) {
	        	
	        	line = line.trim();
	        	
	        	if (line.length() == 0) {
	        		//Dont append empty lines.
	        	} else if (line.endsWith(";") || line.endsWith("{") || line.endsWith("}")){
	        		sb.append(line);
	        	} else {
	        		sb.append(line);
		            sb.append("\n");	
	        	}
	            line = br.readLine();
	        }
	        return sb.toString();
	    } finally {
	        br.close();
	    }
	}

}
