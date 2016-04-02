package Obfuscator;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;

public class ob_layout {
	protected static String obfuscate_layout (File file) throws IOException{
		
		File obfuscatedFile = new File(System.getProperty("user.dir") + "/" + "obfuscated.txt");

		LayoutCommentRemover cr = new LayoutCommentRemover();
		
		if (!obfuscatedFile.exists()) {
			obfuscatedFile.createNewFile();
		}
		
		FileWriter fw = new FileWriter(obfuscatedFile.getAbsoluteFile());
		BufferedWriter bw = new BufferedWriter(fw);
		FileInputStream fis = new FileInputStream(file);
		
		try (BufferedReader br = new BufferedReader(new InputStreamReader(fis))) {
			cr.removeComments(file, br, bw);
		} catch (FileNotFoundException e) {
			e.printStackTrace();  
		}
		
		bw.close();
		
		return null;
	}
}
