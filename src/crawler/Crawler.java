package crawler;

import java.io.IOException;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;
import org.jsoup.nodes.Element;

public class Crawler {
	public static void main(String[] args) {
		String url;
		if (args.length == 1)
			url = args[0];
		else url = "https://old.reddit.com";
		
		Document doc;
		
		System.out.println("running...");
		try {
			doc = Jsoup.connect(url).get();
			Elements links = doc.select("a[href]");
			
			String title = doc.title();
			System.out.println(" Title: " + title);
			
			System.out.printf("\nLinks: (%d)", links.size());
	        for (Element link : links) {
	            System.out.printf(" * a: <%s>  (%s)", link.attr("abs:href"), trim(link.text(), 35));
	        }
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
