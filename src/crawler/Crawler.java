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
	protected static LinkedList<String> queue = new LinkedList<String>();
	protected static int pagesVisited = 0;
	protected static String domainName;

	public static void main(String[] args) throws URISyntaxException {
		String url;
		String flag = "";
		int maxPages = 100;
		
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
		
		domainName = getDomainName(url);
		System.out.println("Domain: " + domainName);
		System.out.println("Only crawling pages with same domain: " + domainConstant + ".\n");
		
		//put first url in the queue
		queue.add(url);
		
		//calls getLinks on the first url in the queue until queue is empty or maxPages limit is reached
		while(queue.peekFirst() != null && pagesVisited < maxPages) {
			getLinks(queue.pollFirst()); //retrieves and removes first element of the queue
		}
		System.out.println("Done.");
	}
	
	private static void getLinks(String url) {
		try {
			Document doc;
			
			doc = Jsoup.connect(url).get();
			Elements links = doc.select("a[href]");
			
			String title = doc.title();
			System.out.print(" Document " + pagesVisited + "| Title: " + title);
			
			System.out.printf("\nLinks: (%d)\n", links.size());
			
			//add each link to the queue
	        for (Element link : links) {
	            String currLink = link.attr("abs:href");
	            
	        	//checks if the link's domain name is different than original domain before adding it to queue
	            if(domainConstant && !getDomainName(currLink).equals(domainName)) 
	        		continue;
	        	
	        	queue.add(currLink);
	        	System.out.printf(" * a: <%s>  (%s)", currLink, trim(link.text(), 35));
	        }  
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
