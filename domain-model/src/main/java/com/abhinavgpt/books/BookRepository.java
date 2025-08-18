package com.abhinavgpt.books;

import java.util.List;
import java.util.Map;

public interface BookRepository {
    
    List<Book> getBooksByAuthor(String author);
    List<Book> getBooksByRating(double rating);
    List<String> getAllAuthors();
    int getBookCountByAuthor(String author);
    Map<String, Integer> getBooksAndPricesByAuthor(String author);
    
    default List<Book> getUniqueBooksByAuthor(String author) {
        return getBooksByAuthor(author);
    }
    
    default List<Book> getAllUniqueBooks() {
        return List.of();
    }
}