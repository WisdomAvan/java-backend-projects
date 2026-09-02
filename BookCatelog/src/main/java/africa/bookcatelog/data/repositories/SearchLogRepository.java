package africa.bookcatelog.data.repositories;

import africa.bookcatelog.data.models.SearchLog;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface SearchLogRepository extends JpaRepository<SearchLog, UUID> {

    List<SearchLog> findByRequestedBy(String requestedBy);

    List <SearchLog> findByResultFoundFalse();

//    List <SearchLog> findAll(Pageable pageable);


}
