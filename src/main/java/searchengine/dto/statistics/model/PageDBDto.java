package searchengine.dto.statistics.model;

import lombok.Data;
import searchengine.model.SiteDB;
@Data
public class PageDBDto {
    private int id;
    private Integer siteId;
    String path;
    private int code;
    private String content;
}
