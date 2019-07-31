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
		getUserInput();
		
		ArrayList<String> results = createPipeFormatedList(placesToLookFor);
		
		// fix formating
		results = fixPipesByDestroyingParentheses(results);
		
		writeResultsToFile(results);
		
		
	}
	
	private static void getUserInput() {
		// Get user input
		Scanner scn = new Scanner(System.in);
		System.out.println("Select your option from the menu (Input a number)");
		System.out.println("1. Input file path");
		System.out.println("2. Read from hardcoded path");
		System.out.println("3. Formatting help");
		int opt = scn.nextInt();
		
		switch(opt) {
		case 1:
			System.out.println("Input file path:");
			
//			Scanner scn2 = new Scanner(System.in);
			String inputPath = "";
//			inputPath = scn2.nextLine();
			Scanner in = new Scanner(System.in);
	        inputPath = in.nextLine();
			placesToLookFor = getFileContent(inputPath);
			in.close();
			break;
		case 2:
			// Attempt to read file at hard-coded path
			System.out.println("Attempting to read hard-coded path...");
			placesToLookFor = getFileContent(FILE_PATH);
			break;
		case 3:
			System.out.println("The formatted file should be a txt file that has each separate query on a separate line.");
			System.out.println("Replace spaces with plus signs.");
		default: 
			System.out.println("Exiting...");
			//scn.close();
			System.exit(0);
		}
		
		//close the scanner so as to prevent memory hogging.
		scn.close();
	}

	private static ArrayList<String> createPipeFormatedList(String[] placesToLookFor) throws IOException {
		ArrayList<String> results = new ArrayList<String>();
		for (String s : placesToLookFor){
			//System.out.println(s + "|" + getResult(s)); 
			results.add(s+"|"+getResult(s));
		}
		return results;
	}

	private static ArrayList<String> fixPipesByDestroyingParentheses(ArrayList<String> list) {
		for(int i = 0; i < list.size(); i++)
		{
			if (list.get(i).contains("(")) {
				System.out.println("Found one. Index:" + i);
				var symb = list.get(i);
				int indexParen1 = symb.indexOf('(');
				int indexParen2 = symb.indexOf(')');
				StringBuilder sb = new StringBuilder(symb );
				sb.replace(indexParen1,indexParen1+1, "|");
				sb.replace(indexParen2, indexParen2+1, "");
				list.set(i,sb.toString());// = (sb.toString());
			}
		}
		return list;
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
		
		System.out.println("Attempting to read list file...");
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
