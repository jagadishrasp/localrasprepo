package org.eclipse.rasp.prudle.studio.plugin.scan;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class TryPattern {

	
	public static void Main() {
		
		
		
		String currentLine = "First statement";
		
		Pattern pattern = Pattern.compile(".*\\\"(.*)\\\".*");

					
		Matcher matcher = pattern.matcher(currentLine);
			 
			// System.out.println(m2.find());

			while (matcher.find()) {
				
				 System.out.println(matcher.group(1));

			}
	}
}
