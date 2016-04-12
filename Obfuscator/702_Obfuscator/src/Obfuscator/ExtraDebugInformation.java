package Obfuscator;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.StringReader;
import java.util.Random;

public class ExtraDebugInformation {
	
	public String[] debugInformation;
	public ExtraDebugInformation(){
		debugInformation = new String[]{"Testing", "Main Logic of Code", "Difference of the two variables"};
	}
	
	
	public String insertDebugStatement(String fileContents) {
		
		BufferedReader br = new  BufferedReader(new StringReader(fileContents));;
		StringBuilder sb = new StringBuilder();
		String line;
		
		int curlyBraces = 0;
		boolean methodSection = false;
		
		try {
			while((line = br.readLine()) != null) {
				if(line.matches("\\s*\\w+\\s\\w+\\s\\w+[(][)][{]")){
					curlyBraces++;
				}else if (line.contains("{") && methodSection){
					curlyBraces++;
				}
				if(line.contains("}") && methodSection){
					curlyBraces--;
				}				
				if (curlyBraces > 0 )
					methodSection = true;
				else
					methodSection = false;
				
				
				sb.append(line);
				sb.append("\n");
				
				int randomValue = new Random().nextInt(10);
				if(methodSection && (randomValue > 7)){
					sb.append(getRandomDebugStatement());
					sb.append("\n");
				}
				
			}
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return sb.toString();
		//System.out.println(outputFile);
	}
	
	
	//Generates a random debug statement to insert into the file.
	public String getRandomDebugStatement() {
		
		String debugInfo = debugInformation[new Random().nextInt(3)];
		String statement = new String("System.out.println(" + debugInfo + ");");
		
		return statement;
		
	}
	
}
