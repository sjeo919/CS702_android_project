package Obfuscator;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.StringReader;

public class LayoutCommentRemover {
	
	protected String removeComments (String fileContents) throws IOException{
		
		BufferedReader br = new BufferedReader(new StringReader(fileContents));
		
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
