package searchengine.services;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.springframework.stereotype.Service;
import searchengine.config.Site;
import searchengine.config.SitesList;
import searchengine.dto.statistics.model.PageDto;
import searchengine.dto.statistics.model.SiteDto;
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
    private final SiteService siteService;
    private final PageService pageService;

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
                siteService.delete(findIdSite(urlMain));
            }
            SiteDto siteDto = new SiteDto();
            siteDto.setName(nameSite);
            siteDto.setUrl(urlMain);
            siteDto.setStatus(Status.INDEXING);
            List<PageDto> pages = new ArrayList<>();
            siteService.add(siteDto);
            addPages(siteMap, siteDto);
            siteDto.setStatus(Status.INDEXED);
            siteService.update(siteDto);
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
        List<SiteDto> sites = siteService.getAll();
        for(SiteDto site: sites)
        {
            if (site.getUrl().equals(urlMain)) {
                id = site.getId();
            }
        }
        return id;
    }

    private void addPages(SiteMap siteMap, SiteDto siteDto) {
        if (siteMap.getUrl().contains(siteDto.getUrl())) {
            PageDto page = new PageDto();
            page.setPath(siteMap.getUrl().replaceAll(siteDto.getUrl(), ""));
            page.setSiteId(siteDto.getId());
            Connection.Response responsePage;
            Document documentPage;
            try {
                responsePage = Jsoup.connect(siteMap.getUrl())
                        .followRedirects(false).execute();
                    documentPage = (Document) Jsoup.connect(siteMap.getUrl())
                            .ignoreHttpErrors(true)
                            .timeout(5000)
                            .followRedirects(false)
                            .get();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            page.setCode(responsePage.statusCode());
            if (responsePage.statusCode() >= 200 && responsePage.statusCode() < 400) {
                page.setContent(documentPage.toString());
            } else {page.setContent("Error - " + responsePage.statusMessage());}
            responsePage.statusMessage();
            List<PageDto> pages = siteDto.getPages();
            pages.add(page);
            pageService.add(page);
            siteDto.setPages(pages);
            if (responsePage.statusCode() >= 400) {
                siteDto.setLastError(responsePage.statusMessage());
            }
            siteDto.setStatusTime(new Date());
            siteService.update(siteDto);
            siteMap.getSiteMapChildrens().forEach(child ->
                addPages(child, siteDto)
            );
        }
    }
}
