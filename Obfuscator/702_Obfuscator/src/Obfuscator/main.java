package Obfuscator;

import java.io.File;
import java.io.IOException;

import org.apache.commons.io.FileUtils;

public class main {

	public static void main(String[] args) throws IOException {
		// TODO Auto-generated method stub
		File srcDir = new File(args[0]);
		File destDir = new File(args[1]);
		FileUtils.copyDirectory(srcDir, destDir);
	}

}