package Obfuscator;

import java.nio.file.Path;

public class FileModel {
	private String fileContentBefore;
	private String fileContentAfter;
	private Path filePath;
	
	public FileModel(String s1, String s2, Path p) {
		this.fileContentBefore = s1;
		this.fileContentAfter = s2;
		this.filePath = p;
	}
	
	public String getFileContentBefore(){
		return fileContentBefore;
	}
	
	public void setFileContentBefore(String s){
		this.fileContentBefore = s;
	}
	
	public String getFileContentAfter(){
		return fileContentAfter;
	}
	
	public void setFileContentAfter(String s){
		this.fileContentAfter = s;
	}
	
	public Path getFilePath(){
		return filePath;
	}
	
	public void setFilePath(Path p){
		this.filePath = p;
	}
}
