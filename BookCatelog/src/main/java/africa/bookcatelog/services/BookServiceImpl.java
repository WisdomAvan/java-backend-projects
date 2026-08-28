package africa.bookcatelog.services;

import africa.bookcatelog.data.models.Book;
import africa.bookcatelog.data.repositories.BookRepository;
import africa.bookcatelog.dtos.responseDtos.GutenbergBookResponse;
import africa.bookcatelog.dtos.responseDtos.GutenbergResponse;
import africa.bookcatelog.exception.BookNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class BookServiceImpl implements BookService {

    private final BookRepository bookRepository;
    private final GutenbergService gutenbergService;

    @Override
    public List<Book> getBooksByCategory(String category) {

        if (category == null || category.isBlank()) {
            throw new BookNotFoundException("Category cannot be empty");
        }

        log.info("Checking local storage for category: {}", category);
        List<Book> books = bookRepository.findBySubjectsContainingIgnoreCase(category);

        if (!books.isEmpty()) {
            log.info("Found {} books locally for category: {}", books.size(), category);
            return books;
        }

        log.info("No local books found for category '{}', syncing from Gutenberg", category);
        List<Book> externalBooks = syncBooks(category);

        if (externalBooks.isEmpty()) {
            log.warn("No books found externally for category: {}", category);
            throw new BookNotFoundException("No books found for category: " + category);
        }

        return externalBooks;
    }

    @Override
    public List<Book> getBooksByAuthor(String authorName) {

        if (authorName == null || authorName.isBlank()) {
            throw new BookNotFoundException("Author name cannot be empty");
        }

        log.info("Checking local storage for author: {}", authorName);
        List<Book> books = bookRepository.findByAuthorContainingIgnoreCase(authorName);

        if (!books.isEmpty()) {
            log.info("Found {} books locally for author: {}", books.size(), authorName);
            return books;
        }

        log.info("No local books found for author '{}', syncing from Gutenberg", authorName);
        List<Book> externalBooks = syncBooks(authorName);

        if (externalBooks.isEmpty()) {
            log.warn("No books found externally for author: {}", authorName);
            throw new BookNotFoundException("No books found for author: " + authorName);
        }

        return externalBooks;
    }

    @Override
    public List<Book> syncBooks(String query) {

        GutenbergResponse response = gutenbergService.search(query);

        if (response == null || response.getResults() == null) {
            log.warn("No results to sync for query: '{}'", query);
            return List.of();
        }

        List<Book> books = response.getResults()
                .stream()
                .map(this::mapToBook)
                .toList();

        List<Book> saved = bookRepository.saveAll(books);
        log.info("Saved {} books to local database for query: '{}'", saved.size(), query);
        return saved;
    }

    @Override
    public List<Book> getAllBooks() {
        log.info("Fetching all books from local database");
        List<Book> books = bookRepository.findAll();

        if (books.isEmpty()) {
            log.info("Local database empty, triggering sync");
            books = syncBooks(""); // GutenbergService now omits "q" when blank
        }

        log.info("Returning {} books", books.size());
        return books;
    }

    private Book mapToBook(GutenbergBookResponse externalBook) {

        String author = null;

        if (externalBook.getAuthors() != null && !externalBook.getAuthors().isEmpty()) {
            author = externalBook.getAuthors().get(0).getName();
        }

        String subjects = null;

        if (externalBook.getSubjects() != null) {
            subjects = externalBook.getSubjects().stream()
                    .collect(Collectors.joining(", "));
        }

        return Book.builder()
                .gutenbergId(String.valueOf(externalBook.getId()))
                .title(externalBook.getTitle())
                .author(author)
                .subjects(subjects)
                .downloadCount(String.valueOf(externalBook.getDownloadCount()))
                .coverImageUrl(externalBook.getCoverImage())
                .build();
    }
}