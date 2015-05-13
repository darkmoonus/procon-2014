package Interfaces;

import java.awt.Font;
import java.awt.FontFormatException;
import java.io.File;
import java.io.IOException;
import java.io.Serializable;

public class CustomFont implements Serializable {
	private static final long serialVersionUID = -7655274165987857417L;
	public static Font getFont(String fontName, int fontType, int fontSize) {
		Font myFont = null;
		File fontFile = new File("src/Resources/" + Configure.DEFAULT_FONT + ".bin");
		try {
			myFont = Font.createFont(Font.TRUETYPE_FONT, fontFile).deriveFont(fontType, fontSize);
		} catch (FontFormatException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	    return myFont;
	}
	public static Font getDefaultFont(int fontType, int fontSize) {
		Font myFont = null;
		File fontFile = new File("src/Resources/" + Configure.DEFAULT_FONT + ".bin");
		try {
			myFont = Font.createFont(Font.TRUETYPE_FONT, fontFile).deriveFont(fontType, fontSize);
		} catch (FontFormatException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	    return myFont;
	}
	
}
