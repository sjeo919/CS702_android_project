package Obfuscator;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.PathMatcher;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.io.FileUtils;

import com.github.javaparser.ParseException;

public class main {

	public static void main(String[] args) throws IOException, ParseException {
		// TODO Auto-generated method stub
		
		String destDirPath = System.getProperty("user.home") + "/702_Obfuscator/MusicPlayer_ob";
//		String srcDirPath = System.getProperty("user.dir") + "/musicplayer";
		String srcDirPath = "D:\\Workspace\\702_project\\MusicPlayer";
//		File srcDir = new File(args[0]);
//		File destDir = new File(args[1]);
		File srcDir = new File(srcDirPath);
		File destDir = new File(destDirPath);
		FileUtils.deleteDirectory(destDir); //delete dest(output) directory incase it already exists.
		PathMatcher matcher = FileSystems.getDefault().getPathMatcher("glob:**.java");
		List<FileModel> FileList = new ArrayList<FileModel>();
		List<FileModel> FileList2 = new ArrayList<FileModel>();
		
		FileUtils.copyDirectory(srcDir, destDir);

		FileToStringConverter fc = new FileToStringConverter();
		Files.walk(Paths.get(destDirPath)).forEach(filePath -> {
		    if (Files.isRegularFile(filePath)) {
		    	System.out.println(filePath);
				if (matcher.matches(filePath)&& filePath.toString().contains("\\src\\main")) {
					try {
						String s1 = fc.read(new File(filePath.toString()));
						FileList.add(new FileModel(s1,"",filePath));
					} catch (Exception e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
		    }
		});
		
		//instantiate obfuscator classes.
		LayoutObfuscator layoutObfuscator = new LayoutObfuscator();
		ExtraDebugInformation extraDebugInformation = new ExtraDebugInformation();
		//LayoutCommentRemover layoutCommentRemover = new LayoutCommentRemover();
		//LayoutWhitespaceRemover layoutWhitespaceRemover = new LayoutWhitespaceRemover();
		
		FileList2 = layoutObfuscator.Obfuscate(FileList);
		
		for (int i = 0; i < FileList2.size(); i++) {
			String output = "";
			
			output = FileList2.get(i).getFileContentAfter();
			
			//comment out lines below depending on which obfuscations you'd like to run (note. whitespaceRemover requires commentRemover to be run prior)
			output = extraDebugInformation.insertDebugStatement(output);
			//output = layoutCommentRemover.removeComments(output);
			//output = layoutWhitespaceRemover.removeWhitespace(output);
			
			FileList2.get(i).setFileContentAfter(output);
		}	
		
		for (int i = 0; i < FileList2.size(); i++) {
			if (FileList2.get(i).getFileContentAfter() != "") {
				PrintWriter writer = new PrintWriter(FileList2.get(i).getFilePath().toString());
				writer.println(FileList2.get(i).getFileContentAfter());
				writer.close();
			}
		}
	}
}
