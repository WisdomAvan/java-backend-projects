package africa.bookcatelog.dtos.responseDtos;


import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class GutenbergResponse {

    private String next;
    private String previous;
    private List< GutenbergBookResponse > results;

}
