package org.eclipse.rasp.prudle.studio.plugin.loginpopup;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;

public class ReadWriteLoginCredentials {
	
	private static final String FILENAME = "E:\\test\\filename.txt";
	
	public void writeEncryptedText(String userId) {
		
		try{
		    PrintWriter writer = new PrintWriter(FILENAME, "UTF-8");
		    writer.println(userId);
		    writer.close();
		} catch (IOException e) {
		   // do something
		}
		
	}
	
	public  String readEncryptedText(){
		
		String userId = null;
		
		try(BufferedReader br = new BufferedReader(new FileReader(FILENAME))) {
		    StringBuilder sb = new StringBuilder();
		    String line = br.readLine();

		    while (line != null) {
		        sb.append(line);
		        sb.append(System.lineSeparator());
		        line = br.readLine();
		    }
		    userId = sb.toString();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return userId;		
	}
}
