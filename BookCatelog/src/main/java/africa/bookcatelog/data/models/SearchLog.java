package africa.bookcatelog.data.models;


import africa.bookcatelog.data.enums.SearchType;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.data.annotation.CreatedDate;

import java.time.LocalDateTime;
import java.util.UUID;


@Entity
@Table(name="Search_Logs")
@Setter
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class SearchLog {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID searchLogId;

    @Enumerated(EnumType.STRING)
    @Column(nullable=false)
    private SearchType searchType;

    @Column(nullable = false)
    private String searchTerm;

    @Column(nullable = false)
    private String requestedBy;

    @Column(nullable = false)
    private boolean resultFound;

    private Integer resultCount;

    @Column(nullable = false)
    private LocalDateTime timeStamp;
}
