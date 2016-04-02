package Obfuscator;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

public class TestFile {

	public TestFile(){
		
	}
	
	public void obfuscate(){
	    FileReader in = null;
		try {
			in = new FileReader("./TestFile.java");
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	    BufferedReader br = new BufferedReader(in);

	    
	    String line;
	    try {
			while ((line = br.readLine()) != null) {
			    System.out.println(line);
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		
	}
	public int test(){
		int i =0;
		
		return i;
		
	}
		
		
		
		
	public void swag(){
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
	}
	
}
