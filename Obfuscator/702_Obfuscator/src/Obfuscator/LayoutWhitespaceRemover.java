package Obfuscator;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.StringReader;

public class LayoutWhitespaceRemover {
		
	protected String removeWhitespace(String fileContents) throws IOException {
		
		BufferedReader br = new BufferedReader(new StringReader(fileContents));
		
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
