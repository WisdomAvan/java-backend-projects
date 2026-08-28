package africa.bookcatelog.dtos.requestDtos;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

@Getter
@Setter
@Builder

public class BookRequest {

    private UUID id;
    private String title;
    private String author;
    private String subject;
}
