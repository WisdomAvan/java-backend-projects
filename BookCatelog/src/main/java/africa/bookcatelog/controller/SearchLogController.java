package africa.bookcatelog.controller;

import africa.bookcatelog.data.models.SearchLog;
import africa.bookcatelog.services.SearchLogService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;


@RestController
    @RequestMapping("/api/search-logs")
    @RequiredArgsConstructor
    public  class SearchLogController {

        private final SearchLogService searchLogService;

        @GetMapping
        public ResponseEntity<Page<SearchLog>> getAllSearchLogs(Pageable pageable) {
            return ResponseEntity.ok(searchLogService.getAllSearchLogs(pageable));
        }

        @GetMapping("/user/{requestedBy}")
        public ResponseEntity<List<SearchLog>> getSearchLogsByUser(@PathVariable String requestedBy) {

            return ResponseEntity.ok(searchLogService.getSearchLogsByUser(requestedBy));
        }

        @GetMapping("/failed")
        public ResponseEntity<List<SearchLog>> getSearchLogsByFailed() {
            return ResponseEntity.ok(searchLogService.getFailedSearchLogs());
        }

    }


