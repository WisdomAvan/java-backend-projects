package africa.bookcatelog.dtos.responseDtos;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

import java.util.List;
import java.util.UUID;

@Getter
@Setter
public class GutenbergBookResponse {

    private UUID id;

    private String title;

    private List<AuthorResponse> authors;

    private List<String> subjects;

    @JsonProperty("download_count")
    private Integer downloadCount;

    @JsonProperty("cover_image")
    private String coverImage;

    @Getter
    @Setter
    public static class AuthorResponse {

        private Long id;

        private String name;
    }
}