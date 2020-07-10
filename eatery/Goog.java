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
	public static final String INPUT_FILES_DRIECTORY_PATH = "C:\\Users\\sgrh\\Documents\\googleconsumer-master\\input";
	public static final String OUTPUT_FILE_PATH = "C:\\Users\\sgrh\\Documents\\googleconsumer-master\\output\\output.txt";
	
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
		System.out.println("1. Input search queries file path");
		System.out.println("2. Read from hardcoded path");
		System.out.println("3. Formatting help");
		int opt = scn.nextInt();
		
		ArrayList<String> filePaths = null;
		
		switch(opt) {
		case 1:
			System.out.println("Input location of file:");
			
//			Scanner scn2 = new Scanner(System.in);
			String inputPath = "";
//			inputPath = scn2.nextLine();
			Scanner in = new Scanner(System.in);
	        inputPath = in.nextLine();
	        
	        filePaths = getFilePathsFromFolder(new File(inputPath));
			
	        for (String s : filePaths) {
				placesToLookFor = getQueriesFromFile(s);		
			}
			in.close();
			break;
		case 2:
			// Attempt to read file at hard-coded path
			System.out.println("Attempting to read hard-coded path...");
			
			filePaths = getFilePathsFromFolder(new File(INPUT_FILES_DRIECTORY_PATH));
			for (String s : filePaths) {
				placesToLookFor = getQueriesFromFile(s);		
			}
			break;
		case 3:
			System.out.println("The formatted file should be a txt file that has each separate query on a separate line.");
			System.out.println("You don't have to replace spaces with plus signs but you can if you want.");
			System.out.println("The resulting output file is like a *.CSV but instead of commas it is pipes ( | ).");
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
			
			Document doc = getDocument(s);
			
			results.add(s + "|" + getResult(s, doc, GField.NAME) 
			+ "|" + getResult(s, doc, GField.STREET_ADDRESS)
			+ "|" + getResult(s, doc, GField.TELEPHONE_NUMBER) 
			+ "|" + getResult(s, doc, GField.RATING) );
			
		}
		return results;
	}

	private static String getResult(String s, Document doc, GField cssSelector) throws IOException {
		String result = "";
		
		for ( Element e : doc.select(cssSelector.getCssSelector())) {
			final String title = e.text();
			result += title + " ";
		}
		
		return result;
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
		PrintWriter write = new PrintWriter(OUTPUT_FILE_PATH, "UTF-8");
		for (String s : results)
		{
			write.println(s);
		}
		write.close();
	}

	private static ArrayList<String> getFilePathsFromFolder( File folder) {
		
		ArrayList<String> paths = new ArrayList<String>();
		ArrayList<String> additionalFilePaths = null;
			
			
		for ( File fileListing : folder.listFiles()) {
			if (fileListing.isDirectory()) {
				 additionalFilePaths = getFilePathsFromFolder(fileListing);
			} else {
				paths.add(fileListing.getPath());
			}
		}
		
		if (additionalFilePaths !=null && additionalFilePaths.size() > 0) {
			for ( String s : additionalFilePaths) {
				paths.add(s);
			}
		}
		
		return paths;
	}
	
	private static String[] getQueriesFromFile(String filePath) {
		ArrayList<String> list = new ArrayList<String>();
		
		File file = new File(filePath);
		System.out.println("Getting input files...");
		
		
		
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

	private static String getResult(String searchQuery, GField cssSelector) throws IOException {
		String result = "";
		
		//get the damn page
		final Document doc = Jsoup.connect("https://www.google.com/search?source=hp&ei=Na5BXcm4KMmsswWCjb7oAw&q="+searchQuery).userAgent(USER_AGENT).get();
		
		for ( Element e : doc.select(cssSelector.getCssSelector())) {
			final String title = e.text();
			result += title + " ";
		}
		
		return result;
	}
	
	private static Document getDocument(String searchQuery) throws IOException {
		return Jsoup.connect("https://www.google.com/search?source=hp&ei=Na5BXcm4KMmsswWCjb7oAw&q="+searchQuery).userAgent(USER_AGENT).get();
	}
	
	private static String getResult(String searchQuery) throws IOException {
		String result = "";
		
		//get the damn page
		final Document doc = Jsoup.connect("https://www.google.com/search?source=hp&ei=Na5BXcm4KMmsswWCjb7oAw&q="+searchQuery).userAgent(USER_AGENT).get();
				
		//for ( Element e : doc.select("h3.r a")) {
		//atempt to get phone number
		for ( Element e : doc.select(".LrzXr")) {
			final String title = e.text();
			result += title + " ";
		}
		
		//.zdqRlf > span:nth-child(1) > span:nth-child(1)
		//attempt to get hours or whether open
		for (Element e : doc.select(".zdqRlf")) {
			final String title = e.text();
			result += title + " ";
		}
		
		//attempt to get real business name
		for (Element e : doc.select(".kno-ecr-pt")) {
			final String title = e.text();
			result += title + " ";
		}
		
		
		return result;
	}
	
	private static String getResult(String searchQuery, String cssSelector) throws IOException {
		String result = "";
		
		//get the damn page
		final Document doc = Jsoup.connect("https://www.google.com/search?source=hp&ei=Na5BXcm4KMmsswWCjb7oAw&q="+searchQuery).userAgent(USER_AGENT).get();
		
		for ( Element e : doc.select(cssSelector)) {
			final String title = e.text();
			result += title + " ";
		}
		
		return result;
		
	}
	
	
}
