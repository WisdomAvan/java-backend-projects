package africa.bookcatelog.services;

import africa.bookcatelog.data.models.SearchLog;
import org.springframework.data.domain.Pageable;

import org.springframework.data.domain.Page;

import java.util.List;

public interface SearchLogService {

    SearchLog searchLog(String searchType, String searchTerm, String requestedBy, boolean resultFound, int resultCount);

    Page <SearchLog> getAllSearchLogs(Pageable pageable);

    List <SearchLog> getSearchLogsByUser(String requestedBy);

    List <SearchLog> getFailedSearchLogs();
}
