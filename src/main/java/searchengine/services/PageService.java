package searchengine.services;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import searchengine.Repositories.PageDBRepository;
import searchengine.Repositories.SiteDBRepository;
import searchengine.dto.statistics.model.PageDto;
import searchengine.model.Page;
import searchengine.model.Site;


import java.util.List;

@Service
@Slf4j
@Data
public class PageService {

    private final PageDBRepository pageDBRepository;
    private final SiteDBRepository siteDBRepository;

    public void add(PageDto pageDto)
    {
        Page page = mapToModel(pageDto);
        Integer siteId = pageDto.getSiteId();
        Site site = siteDBRepository.findById(siteId).orElseThrow();
        page.setSite(site);
        pageDBRepository.save(page);
    }
    public PageDto getById(Integer id)
    {
        Page page = pageDBRepository.findById(id).orElseThrow();
        return mapToDto(page);
    }
    public void update(PageDto pageDto)
    {
        Page page = mapToModel(pageDto);
        Integer siteId = pageDto.getSiteId();
        Site site = siteDBRepository.findById(siteId).orElseThrow();
        page.setSite(site);
        pageDBRepository.save(page);
    }
    public void delete(Integer id)
    {
        pageDBRepository.deleteById(id);
    }
    public List<PageDto> getAll()
    {
        return pageDBRepository.findAll()
                .stream()
                .map(PageService:: mapToDto)
                .toList();
    }

    public static PageDto mapToDto(Page page)
    {
        PageDto pageDto = new PageDto();
        pageDto.setId(pageDto.getId());
        pageDto.setCode(page.getCode());
        pageDto.setSiteId(page.getSite().getId());
        pageDto.setPath(page.getPath());
        pageDto.setContent(page.getContent());
        return pageDto;
    }

    public static Page mapToModel(PageDto pageDto)
    {
        Page page = new Page();
        page.setId(pageDto.getId());
        page.setCode(pageDto.getCode());
        page.setPath(pageDto.getPath());
        page.setContent(pageDto.getContent());
        return page;
    }
}
