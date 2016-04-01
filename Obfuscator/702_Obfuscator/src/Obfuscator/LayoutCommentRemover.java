package Obfuscator;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;

public class LayoutCommentRemover {
	protected String removeComments (File file) throws IOException{
		
		File obfuscatedFile = new File(System.getProperty("user.dir") + "/" + "obfuscated.txt");
		
		if (!obfuscatedFile.exists()) {
			obfuscatedFile.createNewFile();
		}
		
		FileWriter fw = new FileWriter(obfuscatedFile.getAbsoluteFile());
		BufferedWriter bw = new BufferedWriter(fw);
		FileInputStream fis = new FileInputStream(file);
		
		try (BufferedReader br = new BufferedReader(new InputStreamReader(fis))) {
		    String readLine;
	    	boolean blockComment = false;
	    	
		    while ((readLine = br.readLine()) != null) {
		    	String line = readLine.trim();
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
				    	bw.write(writeLine);
				    	bw.newLine();
			    	}
		    	}

		    }

		} catch (FileNotFoundException e) {
	        e.printStackTrace();  
	    }
		
		bw.close();
		
		return null;
	}

}
