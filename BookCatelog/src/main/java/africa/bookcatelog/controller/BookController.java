package africa.bookcatelog.controller;

import africa.bookcatelog.data.models.Book;
import africa.bookcatelog.services.BookService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;

import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/books")
@RequiredArgsConstructor
public class BookController {

    private final BookService bookService;

    @GetMapping("/category/{category}")
    public ResponseEntity<List<Book>> getBooksByCategory(
            @PathVariable String category) {

        return ResponseEntity.ok(
                bookService.getBooksByCategory(category)
        );
    }

    @GetMapping("/author/{authorName}")
    public ResponseEntity<List<Book>> getBooksByAuthor(
            @PathVariable String authorName) {

        return ResponseEntity.ok(
                bookService.getBooksByAuthor(authorName)
        );
    }

    @GetMapping
    public ResponseEntity<List<Book>> getAllBooks() {

        return ResponseEntity.ok(
                bookService.getAllBooks()
        );
    }

    @PostMapping("/sync")
    public ResponseEntity<List<Book>> syncBooks(
            @RequestParam String q) {

        return ResponseEntity.ok(
                bookService.syncBooks(q)
        );
    }
}