package eatery;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Scanner;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

// Created by Seth G. R. Herendeen
// This code is public domain CC0.
// No rights reserved. Do whatever you want.
public class Goog {
	
	//https://www.google.com/search?source=hp&ei=Na5BXcm4KMmsswWCjb7oAw&q=Walmart+Macedon+NY
	
	private static String[] placesToLookFor;
	
	
	public static final String USER_AGENT = "Mozilla/5.0 (Windows NT 10.0; Win64; x64; rv:68.0) Gecko/20100101 Firefox/68.0 ";
	public static final String FILE_PATH = "C:\\Users\\Most-User1\\Desktop\\NRW-AREA-LOCATIONS.txt";
	
	
	public static void main(String[] args) throws Exception {
		
		Scanner scn = new Scanner(System.in);
		
		System.out.println("1. Input file path\nAnything else for a hard-coded path");
		int opt = scn.nextInt();
		if (opt==1) {
			
			System.out.println("Input file path:");
			String inputPath = scn.nextLine();
			placesToLookFor = getFileContent(inputPath);
			
		} else {
		
			placesToLookFor = getFileContent(FILE_PATH);
		
		}
		
		ArrayList<String> results = new ArrayList<String>();
		
		for (String s : placesToLookFor){
			//System.out.println(s + "|" + getResult(s)); 
			results.add(s+"|"+getResult(s));
		}
		
		// fix formating
		for(int i = 0; i < results.size(); i++)
		{
			//results.get(i);
			
			results.get(i).replace(") ", "-");
			results.get(i).replace('(', '|');
			System.out.println(results.get(i));
		}
		
		writeResultsToFile(results);
		
	}
	
	private static void writeResultsToFile(ArrayList<String> results) throws FileNotFoundException, UnsupportedEncodingException {
		PrintWriter write = new PrintWriter("auto-results.txt", "UTF-8");
		for (String s : results)
		{
			write.println(s);
		}
		write.close();
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
			
			
			
			result += title + " ";
		}
		
		
		
		return result;
	}
	
	
}
