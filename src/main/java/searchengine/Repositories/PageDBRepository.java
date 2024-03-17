package searchengine.Repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import searchengine.model.PageDB;

@Repository
public interface PageDBRepository extends JpaRepository<PageDB, Integer> {
}