package searchengine.services;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import searchengine.Repositories.SiteDBRepository;
import searchengine.dto.statistics.model.PageDBDto;
import searchengine.dto.statistics.model.SiteDBDto;
import searchengine.model.PageDB;
import searchengine.model.SiteDB;

import java.util.List;

@Service
@Slf4j
@Data
public class SiteDBService {
    private final SiteDBRepository siteDBRepository;
    public void add(SiteDBDto siteDBDto)
    {
        SiteDB siteDB = mapToModel(siteDBDto);
        siteDBRepository.save(siteDB);
    }
    public SiteDBDto getById(Integer id)
    {
        SiteDB siteDB = siteDBRepository.findById(id).orElseThrow();
        return mapToDto(siteDB);
    }
    public void update(SiteDBDto siteDBDto)
    {
        SiteDB siteDB = mapToModel(siteDBDto);
        siteDBRepository.save(siteDB);
    }
    public void delete(Integer id)
    {
        siteDBRepository.deleteById(id);
    }
    public List<SiteDBDto> getAll()
    {
        return siteDBRepository.findAll()
                .stream()
                .map(SiteDBService::mapToDto)
                .toList();
    }

    public static SiteDBDto mapToDto(SiteDB siteDB)
    {
        SiteDBDto siteDBDto = new SiteDBDto();
        siteDBDto.setId(siteDB.getId());
        siteDBDto.setName(siteDB.getName());
        siteDBDto.setStatus(siteDB.getStatus());
        siteDBDto.setStatusTime(siteDB.getStatusTime());
        siteDBDto.setUrl(siteDB.getUrl());
        siteDBDto.setLastError(siteDB.getLastError());
        siteDBDto.setPages(siteDB.getPages()
                .stream()
                .map(PageDbService::mapToDto)
                .toList());
        return siteDBDto;
    }

    public static SiteDB mapToModel(SiteDBDto siteDBDto)
    {
        SiteDB siteDB = new SiteDB();
        siteDB.setId(siteDBDto.getId());
        siteDB.setName(siteDBDto.getName());
        siteDB.setStatus(siteDBDto.getStatus());
        siteDB.setStatusTime(siteDBDto.getStatusTime());
        siteDB.setUrl(siteDBDto.getUrl());
        siteDB.setLastError(siteDBDto.getLastError());
        siteDB.setPages(siteDBDto.getPages()
                .stream()
                .map(PageDbService::mapToModel)
                .toList());
        return siteDB;
    }
}
