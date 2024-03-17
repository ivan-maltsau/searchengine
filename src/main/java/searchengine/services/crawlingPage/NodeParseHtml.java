package searchengine.services.crawlingPage;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.concurrent.ConcurrentSkipListSet;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.RecursiveAction;
import java.util.concurrent.RecursiveTask;

public class NodeParseHtml extends RecursiveAction {
    private SiteMap siteMap;
    private static CopyOnWriteArrayList linksPool = new CopyOnWriteArrayList();

    public NodeParseHtml(SiteMap siteMap) {
        this.siteMap = siteMap;
    }

    @Override
    protected void compute() {
        linksPool.add(siteMap.getUrl());
        ConcurrentSkipListSet<String> links = ParseHtml.getLinks(siteMap.getUrl());
        for (String link : links) {
            if (!linksPool.contains(link)) {
                linksPool.add(link);
                siteMap.addChildren(new SiteMap(link));
            }
        }
        List<NodeParseHtml> taskList = new ArrayList<>();
        for (SiteMap child : siteMap.getSiteMapChildrens()) {
            NodeParseHtml task = new NodeParseHtml(child);
            task.fork();
            taskList.add(task);
        }
        for (NodeParseHtml task : taskList) {
            task.join();
        }

    }
}

