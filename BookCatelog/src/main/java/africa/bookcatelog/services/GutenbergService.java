package africa.bookcatelog.services;

import africa.bookcatelog.dtos.responseDtos.GutenbergResponse;
import africa.bookcatelog.exception.ExternalApiException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;

@Slf4j
@Service
@RequiredArgsConstructor
public class GutenbergService {

    private final RestClient restClient;

    @Value("${gutenberg.api.key}")
    private String apiKey;

    @Value("${gutenberg.api.host}")
    private String apiHost;

    public GutenbergResponse search(String query) {

        try {
            log.info("Calling Gutenberg API with query: '{}'", query);

            GutenbergResponse response = restClient.get()
                    .uri(uriBuilder -> {
                        uriBuilder.path("/api/books")
                                .queryParam("page_size", 20);

                        // Only add the "q" param when there's an actual search term.
                        // Sending q="" to the external API returns zero results,
                        // which was silently causing empty responses on getAllBooks().
                        if (query != null && !query.isBlank()) {
                            uriBuilder.queryParam("q", query);
                        }

                        return uriBuilder.build();
                    })
                    .header("X-RapidAPI-Key", apiKey)
                    .header("X-RapidAPI-Host", apiHost)
                    .retrieve()
                    .body(GutenbergResponse.class);

            if (response != null && response.getResults() != null) {
                log.info("Gutenberg API returned {} results for query: '{}'",
                        response.getResults().size(), query);
            } else {
                log.warn("Gutenberg API returned null response/results for query: '{}'", query);
            }

            return response;

        } catch (Exception exception) {
            log.error("Gutenberg API call failed for query '{}': {}", query, exception.getMessage(), exception);
            throw new ExternalApiException(
                    "Unable to communicate with Gutenberg API", exception
            );
        }
    }
}