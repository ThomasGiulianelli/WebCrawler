package crawler;

import java.io.IOException;
import java.util.*;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;
import org.jsoup.nodes.Element;

public class Crawler {
	
	protected static LinkedList<String> queue = new LinkedList<String>();
	protected static int pagesVisited = 0;

	public static void main(String[] args) {
		String url;
		int maxPages = 100;
		
		if (args.length == 1)
			url = args[0];
		else url = "https://old.reddit.com";
		
		System.out.println("running...");
		
		//put first url in the queue
		queue.add(url);
		
		//calls getLinks on the first url in the queue until queue is empty or maxPages limit is reached
		while(queue.peekFirst() != null && pagesVisited < maxPages) {
			getLinks(queue.pollFirst()); //retrieves and removes first element of the queue
		}
		System.out.println("done.");
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
	            queue.add(link.attr("abs:href"));
	        	System.out.printf(" * a: <%s>  (%s)", link.attr("abs:href"), trim(link.text(), 35));
	        }  
	        System.out.print("\n\n");
	        pagesVisited++;
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	private static String trim(String s, int width) {
        if (s.length() > width)
            return s.substring(0, width-1) + ".";
        else
            return s;
    }
}
