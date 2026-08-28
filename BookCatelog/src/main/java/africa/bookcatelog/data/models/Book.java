package africa.bookcatelog.data.models;

import jakarta.persistence.*;
import lombok.*;

import java.util.UUID;

@Entity
@Table(name = "Book")
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Book {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID bookId;

    @Column(unique= true, nullable = false)
    private String gutenbergId;

    @Column(nullable = false)
    private String title;

    private String author;

    @Column(length = 2000)
    private String subjects;

    private String downloadCount;

    private String coverImageUrl;

}

