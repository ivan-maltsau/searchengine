package searchengine.Repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import searchengine.model.Site;

@Repository
public interface SiteDBRepository extends JpaRepository<Site, Integer> {
}
