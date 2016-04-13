package Obfuscator;

import java.io.File;
import java.io.IOException;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.PathMatcher;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.io.FileUtils;

public class main {

	public static void main(String[] args) throws Exception {
		// TODO Auto-generated method stub
		
		String destDirPath = System.getProperty("user.home") + "/newfolder2";
		String srcDirPath = System.getProperty("user.home") + "/newfolder";
//		File srcDir = new File(args[0]);
//		File destDir = new File(args[1]);
		File srcDir = new File(srcDirPath);
		File destDir = new File(destDirPath);
		PathMatcher matcher = FileSystems.getDefault().getPathMatcher("glob:**.java");
		List<Path> pathList = new ArrayList<Path>();
		FileUtils.copyDirectory(srcDir, destDir);

		Files.walk(Paths.get(destDirPath)).forEach(filePath -> {
		    if (Files.isRegularFile(filePath)) {
				if (matcher.matches(filePath)) {
					pathList.add(filePath);
				}
		    }
		});
		
		for (int i = 0; i < pathList.size(); i++) {
			System.out.println(pathList.get(i));
		}
		
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
