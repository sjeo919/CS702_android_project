package Obfuscator;

import java.io.File;
import java.io.IOException;

public class ob_layout {
	protected static String obfuscate_layout (File file) throws IOException{
		LayoutCommentRemover cr = new LayoutCommentRemover();
		cr.removeComments(file);
		return null;
	}
}
