package Obfuscator;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.util.Map;

public class FileNameChanger {
	
	public static void changeName(Path path, Map<String, String> map) throws IOException{
		File file = path.toFile();

		String fullName = path.getFileName().toString();
		String name = fullName.substring(0, fullName.length() - 5);
		String newName = path.toString().replace(name + ".java", map.get(name)+ ".java");
				
		File file2 = new File(newName);

		if (!file2.exists())
			file.renameTo(file2);
	}

}
