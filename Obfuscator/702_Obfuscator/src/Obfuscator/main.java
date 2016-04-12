package Obfuscator;

import java.io.File;
import java.io.IOException;

import org.apache.commons.io.FileUtils;

public class main {

	public static void main(String[] args) throws Exception {
		// TODO Auto-generated method stub
		//File srcDir = new File(args[0]);
		//File destDir = new File(args[1]);
		//FileUtils.copyDirectory(srcDir, destDir);
		
		//READ HERE GUYS
		//Declare the file path to the java file you're obfuscating.
		//Uncomment one of the 3 statements below that you're working on. Good luck.
		
		/*String dir = System.getProperty("user.dir");
		String filePath = dir + "/" + "HelloWorld.java";
		String output = "";
		
		File testFile = new File(filePath);
		
		//output = ob_control.obfuscate_control(testFile);
		//output = ob_data.obfuscate_data(testFile);
		output = ob_layout.obfuscate_layout(testFile);
		
		ExtraDebugInformation x = new ExtraDebugInformation();
		x.obfuscate();
		
		System.out.println(output);*/
		LayoutObfuscator l = new LayoutObfuscator(new File(System.getProperty("user.dir") + "/src/Obfuscator/LayoutObfuscator.java"));
		l.removeComments();
		l.obfuscateFieldNames();
		System.out.println(l.getObfuscatedCode());
	}

}
