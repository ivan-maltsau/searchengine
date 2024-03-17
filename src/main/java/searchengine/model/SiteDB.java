package searchengine.model;

import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;

import javax.persistence.*;
import java.util.Date;
import java.util.List;

@Entity
@Table(name = "site")
@Data
public class SiteDB
{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    @Enumerated(EnumType.STRING)
    @Column(columnDefinition = "ENUM('INDEXING', 'INDEXED', 'FAILED')")
    private Status status;
    @CreationTimestamp
    @Column(name = "status_time")
    private Date statusTime;
    @Column(name = "last_error", columnDefinition="TEXT")
    private String lastError;
    @Column(columnDefinition="VARCHAR(255)")
    private String url;
    @Column(columnDefinition="VARCHAR(255)")
    private String name;
    @OneToMany(mappedBy = "site", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<PageDB> pages;
}
