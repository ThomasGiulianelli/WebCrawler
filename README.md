# WebCrawler

This is a simple crawler that retrieves every href on a webpage, then repeats this for each webpage linked by the hrefs. When thought of as a tree of hrefs, the crawler traverses it using Breadth First Search. 

The crawler stops when it runs out of hrefs to follow or it reaches the max page limit - whichever comes first. 

You may provide it with a starting webpage as an argument, e.g. `java Crawler https://github.com`. In addition, you may add on the `-d` flag to tell it to only crawl webpages with the same domain as the first webpage you give to it. In this case your command may look like so: `java Crawler https://github.com -d`. 


The `.settings`, `.classpath`, and `.project` files were created by eclipse which I used to create this project. The only external library I used is [jsoup](https://jsoup.org/) to retrieve and parse the HTML documents. If you wish to use my webcrawler, download the jsoup jar file and add it to the build path of this project after cloning it to your machine.
