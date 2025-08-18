package com.abhinavgpt.books;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public final class BookService {
    
    private final BookRepository bookRepository;
    private final BookRepositoryImpl optimizedBookRepository;
    
    public BookService(BookRepository repository) {
        this.bookRepository = repository;
        this.optimizedBookRepository = repository instanceof BookRepositoryImpl impl ? impl : null;
    }
    
    public int getTotalBookCountByAuthor(String author) {
        return bookRepository.getBookCountByAuthor(author);
    }
    
    public List<String> getAllAuthorsInDataset() {
        return bookRepository.getAllAuthors();
    }
    
    public List<String> getBookTitlesByAuthor(String author) {
        return bookRepository.getBooksByAuthor(author)
                .stream()
                .map(Book::title)
                .toList();
    }
    
    public List<String> getUniqueBookTitlesByAuthor(String author) {
        return bookRepository.getUniqueBooksByAuthor(author)
                .stream()
                .map(Book::title)
                .toList();
    }
    
    public List<Book> getAllBooksIncludingDuplicatesByAuthor(String author) {
        return optimizedBookRepository != null 
            ? optimizedBookRepository.getAllBooksByAuthorIncludingDuplicates(author)
            : bookRepository.getBooksByAuthor(author);
    }
    
    public List<Book> getAllBooksIncludingDuplicatesByRating(double rating) {
        return optimizedBookRepository != null 
            ? optimizedBookRepository.getAllBooksByRatingIncludingDuplicates(rating)
            : bookRepository.getBooksByRating(rating);
    }
    
    public List<Book> classifyBooksByUserRating(double rating) {
        return bookRepository.getBooksByRating(rating);
    }
    
    public Map<String, Integer> getBookPricesByAuthor(String author) {
        return bookRepository.getBooksAndPricesByAuthor(author);
    }
    
    public Map<String, Object> getPerformanceOptimizationStats() {
        return optimizedBookRepository != null 
            ? optimizedBookRepository.getPerformanceOptimizationStats()
            : Map.of("optimizationEnabled", false);
    }
    
    public Map<String, List<Book>> getMultiYearBestsellerBooks() {
        return optimizedBookRepository != null 
            ? optimizedBookRepository.getDuplicateBooksAcrossYears()
            : Map.of();
    }
    
    public Map<String, Integer> getMostProlificAuthorsWithBookCount(int limit) {
        return getAllAuthorsInDataset().stream()
                .collect(Collectors.toMap(
                    author -> author,
                    this::getTotalBookCountByAuthor
                ))
                .entrySet()
                .stream()
                .sorted(Map.Entry.<String, Integer>comparingByValue().reversed())
                .limit(limit)
                .collect(Collectors.toMap(
                    Map.Entry::getKey,
                    Map.Entry::getValue,
                    (existing, replacement) -> existing,
                    java.util.LinkedHashMap::new
                ));
    }
}