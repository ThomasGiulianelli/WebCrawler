package crawler;

import java.io.IOException;
import java.util.*;
import java.net.URI;
import java.net.URISyntaxException;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;
import org.jsoup.nodes.Element;

public class Crawler {
	protected static Boolean domainConstant = false; //can be set to true with "-d" flag
	protected static LinkedList<String> queue = new LinkedList<String>(); //crawler traverses the links placed in this queue
	protected static int pagesVisited = 0;
	protected static String domainName;

	public static void main(String[] args) throws URISyntaxException {
		String url;
		String flag = "";
		int maxPages = 100; //TODO: assert maxPages > 0
		int tableSize = (int) Math.ceil(maxPages/0.75 + 1); //This value should produce a large enough HashMap to reduce how often it needs to be resized.
		
		if (args.length == 1) {
			url = args[0];
		}
		else if (args.length == 2) {
			url = args[0];
			flag = args[1];
		}
		else {url = "https://old.reddit.com";}
		
		if (flag.equals("-d")) {
			domainConstant = true; //sets crawler to only traverse pages with the same domain as the original page
		}
		
		// This HashMap is used to keep track of every link that has been added to the queue to prevent identical links from being added
		HashMap<String,Boolean> visited = new HashMap<>(tableSize);
		
		domainName = getDomainName(url);
		System.out.println("Domain: " + domainName);
		System.out.println("Only crawling pages with same domain: " + domainConstant + ".\n");
		
		//put first URL in the queue and HashMap
		queue.add(url);
		visited.put(url,true);
		
		//calls getLinks on the first URL in the queue until queue is empty or maxPages limit is reached
		while(queue.peekFirst() != null && pagesVisited < maxPages) {
			//removes first element of the queue and passes it to getLinks()
			getLinks(queue.pollFirst(), visited);
		}
		System.out.println("Done.");
	}
	
	/**
	 * Given a URL this method retrieves every hyperlink on the page and adds valid ones to the queue.
	 * @param url This is the URL of the page that will be scanned for links to be added to the queue.
	 * @param visited This is the HashMap that will be used to check for previously-seen links
	 * @return Nothing.
	 */
	private static void getLinks(String url, Map<String,Boolean> visited) {
		try {
			Document doc;
			int numLinks = 0; //used to track number of links on the current page that will be visited in the future.
			StringBuilder linkList = new StringBuilder(); //used to build a string of every link found on the page that will be visited in the future.
			
			doc = Jsoup.connect(url).get();
			Elements links = doc.select("a[href]");
			
			String title = doc.title();
			System.out.print("Document " + pagesVisited + " | Title: " + title);
			
			//add each link to the queue
	        for (Element link : links) {
	            String currLink = link.attr("abs:href");
	            
	        	//checks if the link's domain name is different than original domain before adding it to queue
	            if(domainConstant && !getDomainName(currLink).equals(domainName)) 
	        		continue;
	        	
	            //check if the link has already been added to the queue
	            if (visited.containsKey(currLink))
	            	continue;
	            
	        	queue.add(currLink);
	        	visited.put(currLink,true);
	        	numLinks++;
	        	linkList.append(" * a: <" + currLink + "> (" + trim(link.text(), 35) + ")");
	        }  
	        
	        System.out.printf("\nLinks: (%d). Unique links to be added to queue: (%d).\n", links.size(),numLinks);
	        System.out.print(linkList.toString());
	        System.out.print("\n\n");
	        pagesVisited++;
		} catch (IOException | URISyntaxException e) {
			e.printStackTrace();
		}
	}
	
	private static String trim(String s, int width) {
        if (s.length() > width)
            return s.substring(0, width-1) + ".";
        else
            return s;
    }
	
	public static String getDomainName(String url) throws URISyntaxException {
	    URI uri = new URI(url);
	    String domain = uri.getHost();
	    return domain.startsWith("www.") ? domain.substring(4) : domain;
	}
}
