package Obfuscator;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

public class LayoutCommentRemover {
	
	protected String removeComments (String fileContents) throws IOException{
		
		// convert String into InputStream
		InputStream is = new ByteArrayInputStream(fileContents.getBytes());
		// read it with BufferedReader
		BufferedReader br = new BufferedReader(new InputStreamReader(is));
		
		try {
			StringBuilder sb = new StringBuilder();
	        String line = br.readLine();
	        boolean blockComment = false;
	        
	        while (line != null) {
	        	
	        	line = line.trim();
	        	String[] words = line.split("\\s+");
	        	String writeLine = "";
	        	
	        	if(words[0].equals("/**") || words[0].equals("*")){
		    		blockComment = true; ////// also check for when * is missing
		    	} else if (words[0].equals("*/")) {
		    		blockComment = false;
		    	} else {
			    	for (int i=0; i<words.length; i++){
			    		if (!words[i].equals("//") && blockComment == false) {
			   				writeLine = writeLine + words[i] + " ";
			   			} else {
			   				i = words.length;
			   			}
			   		}
			    	if(!writeLine.equals("")){
				    	sb.append(writeLine);
				    	sb.append("\n");
			    	}
		    	}
	        	line = br.readLine();
	        }
	        return sb.toString();
			
		} finally {
			br.close();
		}
	}
}
