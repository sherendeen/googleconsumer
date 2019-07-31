package eatery;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Scanner;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;


public class Goog {
	
	//https://www.google.com/search?source=hp&ei=Na5BXcm4KMmsswWCjb7oAw&q=Walmart+Macedon+NY
	
	private static String[] placesToLookFor;
	
	
	public static final String USER_AGENT = "Mozilla/5.0 (Windows NT 10.0; Win64; x64; rv:68.0) Gecko/20100101 Firefox/68.0 ";
	public static final String FILE_PATH = "C:\\Users\\Most-User1\\Desktop\\locations.txt";
	
	
	public static void main(String[] args) throws Exception {
		
		placesToLookFor = getFileContent(FILE_PATH);
		
		for (String s : placesToLookFor){
			System.out.println(s + "|" + getResult(s)); 
		}
		
	}
	
	private static String[] getFileContent(String filePath) {
		ArrayList<String> list = new ArrayList<String>();
		
		File file = new File(filePath);
		
		try {
			Scanner scanner = new Scanner(file);
			
			while(scanner.hasNextLine()) {
				String lline = scanner.nextLine();
				list.add(lline);
			}
			scanner.close();
			
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		
		
		String[] array = new String[list.size()];
		
		for (int i = 0 ; i < array.length; i++) {
			array[i] = list.get(i);
		}
		
		return array;
	}

	private static String getResult(String searchQuery) throws IOException {
		String result = "";
		
		//get the damn page
		final Document doc = Jsoup.connect("https://www.google.com/search?source=hp&ei=Na5BXcm4KMmsswWCjb7oAw&q="+searchQuery).userAgent(USER_AGENT).get();
				
		//for ( Element e : doc.select("h3.r a")) {
		for ( Element e : doc.select(".LrzXr")) {
			final String title = e.text();
		//	final String url = e.attr("href");
					
			result += title + " ";
		}
		
//		for (Element e : doc.select(".zdqRlf > span:nth-child(1) > span:nth-child(1)")) {
//			final String title = e.ownText();
//			
//			result += title ;
//		}
			
		
		return result;
	}
}
