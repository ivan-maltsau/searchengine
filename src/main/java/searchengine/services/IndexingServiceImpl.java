package searchengine.services;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import searchengine.config.Site;
import searchengine.config.SitesList;
import searchengine.dto.statistics.model.PageDBDto;
import searchengine.dto.statistics.model.SiteDBDto;
import searchengine.model.PageDB;
import searchengine.model.SiteDB;
import searchengine.model.Status;
import searchengine.services.crawlingPage.NodeParseHtml;
import searchengine.services.crawlingPage.SiteMap;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.ForkJoinPool;

@Service
@Slf4j
@Data
public class IndexingServiceImpl implements IndexingService{
    private final SitesList sites;
    private boolean processIndexing;
    private List<Site> configSitesList = sites.getSites();
    private String urlMain;
    private String nameSite;
    private final SiteDBService siteDBService;
    private final PageDbService pageDbService;

    @Override
    public void startIndexing() throws IOException {
        processIndexing = true;
        Date dateStart = new Date();
        log.info("Start indexing - " + dateStart.toString());
        for (int i = 0; i < sites.getSites().size(); i++) {
            urlMain = configSitesList.get(i).getUrl();
            nameSite = configSitesList.get(i).getName();
            SiteMap siteMap = new SiteMap(urlMain);
            NodeParseHtml task = new NodeParseHtml(siteMap);
            new ForkJoinPool().invoke(task);
            if (findIdSite(urlMain) != null) {
                siteDBService.delete(findIdSite(urlMain));
            }
            SiteDBDto siteDBDto = new SiteDBDto();
            siteDBDto.setName(nameSite);
            siteDBDto.setUrl(urlMain);
            siteDBDto.setStatus(Status.INDEXING);
            List<PageDBDto> pages = new ArrayList<>();
            PageDBDto mainPage = new PageDBDto();
            mainPage.setSiteId(siteDBDto.getId());
            mainPage.setPath("/");
            Document document = (Document) Jsoup.connect(urlMain)
                    .ignoreHttpErrors(true)
                    .timeout(5000)
                    .followRedirects(false)
                    .get();
            String content = document.toString();
        }
        Date dateEnd = new Date();
        log.info("End indexing - " + dateEnd.toString());
    }

    @Override
    public void stopIndexing()
    {
        processIndexing = false;
    }

    private Integer findIdSite(String urlMain)
    {
        Integer id = null;
        List<SiteDBDto> sites = siteDBService.getAll();
        for(SiteDBDto site: sites)
        {
            if (site.getUrl().equals(urlMain)) {
                id = site.getId();
            }
        }
        return id;
    }
}
