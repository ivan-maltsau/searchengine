package searchengine.services.crawlingPage;

import java.util.concurrent.CopyOnWriteArrayList;

public class SiteMap {
    private String url;
    private CopyOnWriteArrayList<SiteMap> siteMapChildrens;

    public SiteMap(String url) {
        siteMapChildrens = new CopyOnWriteArrayList<>();
        this.url = url;
    }

    public void addChildren(SiteMap children) {
        siteMapChildrens.add(children);
    }

    public CopyOnWriteArrayList<SiteMap> getSiteMapChildrens() {
        return siteMapChildrens;
    }

    public String getUrl() {
        return url;
    }
}

