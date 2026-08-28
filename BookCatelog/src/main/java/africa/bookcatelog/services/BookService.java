package africa.bookcatelog.services;


import africa.bookcatelog.data.models.Book;

import java.util.List;


public interface BookService {

    List<Book> getBooksByCategory(String category);

    List<Book> getBooksByAuthor(String authorName);

    List<Book> getAllBooks();

    List <Book> syncBooks(String query);

}