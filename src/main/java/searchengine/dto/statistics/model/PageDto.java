package searchengine.dto.statistics.model;

import lombok.Data;

@Data
public class PageDto {
    private int id;
    private Integer siteId;
    String path;
    private int code;
    private String content;
}
