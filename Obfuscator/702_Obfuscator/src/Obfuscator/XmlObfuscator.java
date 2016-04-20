package Obfuscator;

import java.nio.file.Path;
import java.util.Iterator;
import java.util.Map;

public class XmlObfuscator {
	
	public FileModel obfuscate(Map<String, String> globalTypeList, FileModel xmlFile){
		
		String s = xmlFile.getFileContentBefore();
		
		for (String key : globalTypeList.keySet()) {
		    //System.out.println(globalTypeList.get(key));
		    s = s.replace(key, globalTypeList.get(key));
		}
		
		xmlFile.setFileContentAfter(s);
				
		return xmlFile;
	}
}
