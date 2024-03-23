package searchengine.services;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import searchengine.Repositories.SiteDBRepository;
import searchengine.dto.statistics.model.SiteDto;
import searchengine.model.Site;

import java.util.List;

@Service
@Slf4j
@Data
public class SiteService {
    private final SiteDBRepository siteDBRepository;
    public void add(SiteDto siteDto)
    {
        Site site = mapToModel(siteDto);
        siteDBRepository.save(site);
    }
    public SiteDto getById(Integer id)
    {
        Site site = siteDBRepository.findById(id).orElseThrow();
        return mapToDto(site);
    }
    public void update(SiteDto siteDto)
    {
        Site site = mapToModel(siteDto);
        siteDBRepository.save(site);
    }
    public void delete(Integer id)
    {
        siteDBRepository.deleteById(id);
    }
    public List<SiteDto> getAll()
    {
        return siteDBRepository.findAll()
                .stream()
                .map(SiteService::mapToDto)
                .toList();
    }

    public static SiteDto mapToDto(Site site)
    {
        SiteDto siteDto = new SiteDto();
        siteDto.setId(site.getId());
        siteDto.setName(site.getName());
        siteDto.setStatus(site.getStatus());
        siteDto.setStatusTime(site.getStatusTime());
        siteDto.setUrl(site.getUrl());
        siteDto.setLastError(site.getLastError());
        siteDto.setPages(site.getPages()
                .stream()
                .map(PageService::mapToDto)
                .toList());
        return siteDto;
    }

    public static Site mapToModel(SiteDto siteDto)
    {
        Site site = new Site();
        site.setId(siteDto.getId());
        site.setName(siteDto.getName());
        site.setStatus(siteDto.getStatus());
        site.setStatusTime(siteDto.getStatusTime());
        site.setUrl(siteDto.getUrl());
        site.setLastError(siteDto.getLastError());
        site.setPages(siteDto.getPages()
                .stream()
                .map(PageService::mapToModel)
                .toList());
        return site;
    }
}
