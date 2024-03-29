package searchengine.model;

import lombok.Data;

import javax.persistence.*;

@Entity
@Table(name = "page", indexes = @Index(name = "path_index", columnList = "path"))
@Data
public class Page {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    @ManyToOne
    @JoinColumn(name = "site_id")
    private Site site;
    @Column(columnDefinition="VARCHAR(255)")
    String path;
    private int code;
    @Column(columnDefinition="MEDIUMTEXT")
    private String content;

}
