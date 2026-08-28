package africa.bookcatelog.data.repositories;

import africa.bookcatelog.data.models.Book;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface BookRepository extends JpaRepository<Book, UUID> {

    List<Book> findByAuthorContainingIgnoreCase(String author);

    List<Book> findBySubjectsContainingIgnoreCase(String subject);

    Optional<Book> findByGutenbergId(Long gutenbergId);

    boolean existsByGutenbergId(Long gutenbergId);

}