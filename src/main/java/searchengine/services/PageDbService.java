package searchengine.services;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import searchengine.Repositories.PageDBRepository;
import searchengine.Repositories.SiteDBRepository;
import searchengine.dto.statistics.model.PageDBDto;
import searchengine.model.PageDB;
import searchengine.model.SiteDB;


import java.util.List;

@Service
@Slf4j
@Data
public class PageDbService {

    private final PageDBRepository pageDBRepository;
    private final SiteDBRepository siteDBRepository;

    public void add(PageDBDto pageDBDto)
    {
        PageDB pageDB = mapToModel(pageDBDto);
        Integer siteId = pageDBDto.getSiteId();
        SiteDB siteDB = siteDBRepository.findById(siteId).orElseThrow();
        pageDB.setSite(siteDB);
        pageDBRepository.save(pageDB);
    }
    public PageDBDto getById(Integer id)
    {
        PageDB pageDB = pageDBRepository.findById(id).orElseThrow();
        return mapToDto(pageDB);
    }
    public void update(PageDBDto pageDBDto)
    {
        PageDB pageDB = mapToModel(pageDBDto);
        Integer siteId = pageDBDto.getSiteId();
        SiteDB siteDB = siteDBRepository.findById(siteId).orElseThrow();
        pageDB.setSite(siteDB);
        pageDBRepository.save(pageDB);
    }
    public void delete(Integer id)
    {
        pageDBRepository.deleteById(id);
    }
    public List<PageDBDto> getAll()
    {
        return pageDBRepository.findAll()
                .stream()
                .map(PageDbService:: mapToDto)
                .toList();
    }

    public static PageDBDto mapToDto(PageDB pageDB)
    {
        PageDBDto pageDBDto = new PageDBDto();
        pageDBDto.setId(pageDBDto.getId());
        pageDBDto.setCode(pageDB.getCode());
        pageDBDto.setSiteId(pageDB.getSite().getId());
        pageDBDto.setPath(pageDB.getPath());
        pageDBDto.setContent(pageDB.getContent());
        return pageDBDto;
    }

    public static PageDB mapToModel(PageDBDto pageDBDto)
    {
        PageDB pageDB = new PageDB();
        pageDB.setId(pageDBDto.getId());
        pageDB.setCode(pageDBDto.getCode());
        pageDB.setPath(pageDBDto.getPath());
        pageDB.setContent(pageDBDto.getContent());
        return pageDB;
    }
}
