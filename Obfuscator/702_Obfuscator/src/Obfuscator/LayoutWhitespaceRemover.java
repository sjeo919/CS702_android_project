package Obfuscator;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

public class LayoutWhitespaceRemover {
		
	protected String removeWhitespace(String fileContents) throws IOException {
		
		// convert String into InputStream
		InputStream is = new ByteArrayInputStream(fileContents.getBytes());
		// read it with BufferedReader
		BufferedReader br = new BufferedReader(new InputStreamReader(is));
		
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
