package africa.bookcatelog.services;


import africa.bookcatelog.data.models.Book;

import java.util.List;


public interface BookService {

    List<Book> getBooksByCategory(String category, String requestedBy);

    List<Book> getBooksByAuthor(String authorName,  String requestedBy);

    List<Book> getAllBooks();

    List <Book> syncBooks(String query);

}