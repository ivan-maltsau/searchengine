package searchengine.dto.statistics.model;

import lombok.Data;
import searchengine.model.Status;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Data
public class SiteDBDto {
    private int id;
    private Status status;
    private Date statusTime;
    private String lastError;
    private String url;
    private String name;
    private List<PageDBDto> pages = new ArrayList<>();
}
