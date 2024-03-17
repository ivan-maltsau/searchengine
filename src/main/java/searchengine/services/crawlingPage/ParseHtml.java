package searchengine.services.crawlingPage;
import org.jsoup.*;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.Node;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.net.SocketTimeoutException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ConcurrentSkipListSet;

public class ParseHtml {
    private static volatile ConcurrentSkipListSet<String> links;

    public static ConcurrentSkipListSet<String> getLinks(String url){
        links = new ConcurrentSkipListSet<>();
        try {
            Thread.sleep(150);
            Document document = (Document) Jsoup.connect(url)
                    .ignoreHttpErrors(true)
                    .timeout(5000)
                    .followRedirects(false)
                    .get();
            Elements elements = document.select("a[href]");
            for (Element element : elements) {
                String link = element.attr("href");
                if (links.contains(link) || link.contains("#") || isFile(link)) {
                    continue;
                }
                links.add(link);
            }
        } catch (InterruptedException e) {
            System.out.println(e + " - " + url);
        } catch (SocketTimeoutException e) {
            System.out.println(e + " - " + url);
        } catch (IOException e) {
            System.out.println(e + " - " + url);
        }

        return links;
    }

    private static boolean isFile(String link) {
        link.toLowerCase();
        return link.contains(".jpg")
                || link.contains(".jpeg")
                || link.contains(".png")
                || link.contains(".gif")
                || link.contains(".webp")
                || link.contains(".pdf")
                || link.contains(".eps")
                || link.contains(".xlsx")
                || link.contains(".doc")
                || link.contains(".pptx")
                || link.contains(".docx")
                || link.contains("?_ga");
    }
}

