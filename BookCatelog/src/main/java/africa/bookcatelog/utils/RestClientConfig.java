package africa.bookcatelog.utils;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestClient;

@Configuration
public class RestClientConfig {

    @Bean
    public RestClient restClient() {

        return RestClient.builder()
                .baseUrl("https://project-gutenberg-books-api.p.rapidapi.com")
                .build();
    }

}