package africa.bookcatelog.services;

import africa.bookcatelog.data.enums.SearchType;
import africa.bookcatelog.data.models.SearchLog;
import africa.bookcatelog.data.repositories.SearchLogRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;


@Service
@RequiredArgsConstructor
public class SearchLogServiceImpl implements SearchLogService {

    private final SearchLogRepository searchLogRepository;


    @Override
    public SearchLog searchLog(String searchType, String searchTerm, String requestedBy, boolean resultFound, int resultCount) {

        SearchLog searchLog = SearchLog.builder()
                .searchType(SearchType.valueOf(searchType))
                .searchTerm(searchTerm)
                .requestedBy(requestedBy)
                .resultFound(resultFound)
                .resultCount(resultCount)
                .timeStamp(LocalDateTime.now())
                .build();

        return searchLogRepository.save(searchLog);
    }

    @Override
    public Page<SearchLog> getAllSearchLogs(Pageable pageable) {
        return searchLogRepository.findAll(pageable);
    }

    @Override
    public List<SearchLog> getSearchLogsByUser(String requestedBy) {
        return searchLogRepository.findByRequestedBy(requestedBy);
    }

    @Override
    public List<SearchLog> getFailedSearchLogs() {
        return searchLogRepository.findByResultFoundFalse();
    }
}
