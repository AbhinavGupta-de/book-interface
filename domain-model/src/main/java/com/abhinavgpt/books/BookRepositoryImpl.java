package com.abhinavgpt.books;

import java.util.*;
import java.util.function.Predicate;
import java.util.stream.Collectors;

public final class BookRepositoryImpl implements BookRepository {
    
    private final List<Book> allBooksInDataset;
    private final Map<String, List<Book>> booksByAuthorHashMap;
    private final Map<Double, List<Book>> booksByRatingHashMap;
    private final Map<String, Integer> bookCountByAuthorHashMap;
    private final List<String> uniqueAuthorsList;
    
    public BookRepositoryImpl(List<Book> books) {
        this.allBooksInDataset = List.copyOf(books != null ? books : List.of());
        this.booksByAuthorHashMap = precomputeBooksByAuthorHashMap();
        this.booksByRatingHashMap = precomputeBooksByRatingHashMap();
        this.bookCountByAuthorHashMap = precomputeBookCountByAuthorHashMap();
        this.uniqueAuthorsList = precomputeUniqueAuthorsList();
    }
    
    private Map<String, List<Book>> precomputeBooksByAuthorHashMap() {
        return allBooksInDataset.stream()
                .collect(Collectors.groupingBy(
                    book -> book.author().toLowerCase().trim(),
                    HashMap::new,
                    Collectors.toList()
                ));
    }
    
    private Map<Double, List<Book>> precomputeBooksByRatingHashMap() {
        return allBooksInDataset.stream()
                .collect(Collectors.groupingBy(
                    Book::userRating,
                    HashMap::new,
                    Collectors.toList()
                ));
    }
    
    private Map<String, Integer> precomputeBookCountByAuthorHashMap() {
        return allBooksInDataset.stream()
                .collect(Collectors.groupingBy(
                    book -> book.author().toLowerCase().trim(),
                    HashMap::new,
                    Collectors.collectingAndThen(Collectors.toList(), List::size)
                ));
    }
    
    private List<String> precomputeUniqueAuthorsList() {
        return allBooksInDataset.stream()
                .map(Book::author)
                .distinct()
                .sorted()
                .toList();
    }
    
    @Override
    public List<Book> getBooksByAuthor(String author) {
        return getUniqueBooksByAuthor(author);
    }
    
    public List<Book> getAllBooksByAuthorIncludingDuplicates(String author) {
        if (author == null || author.trim().isEmpty()) {
            return List.of();
        }
        return booksByAuthorHashMap.getOrDefault(author.toLowerCase().trim(), List.of());
    }
    
    @Override
    public List<Book> getBooksByRating(double rating) {
        List<Book> allBooksWithRating = booksByRatingHashMap.getOrDefault(rating, List.of());
        
        return allBooksWithRating.stream()
                .collect(Collectors.toMap(
                    book -> book.title().toLowerCase().trim() + "|" + book.author().toLowerCase().trim(),
                    book -> book,
                    (existing, replacement) -> existing.year() > replacement.year() ? existing : replacement,
                    LinkedHashMap::new
                ))
                .values()
                .stream()
                .toList();
    }
    
    public List<Book> getAllBooksByRatingIncludingDuplicates(double rating) {
        return booksByRatingHashMap.getOrDefault(rating, List.of());
    }
    
    @Override
    public List<String> getAllAuthors() {
        return uniqueAuthorsList;
    }
    
    @Override
    public int getBookCountByAuthor(String author) {
        if (author == null || author.trim().isEmpty()) {
            return 0;
        }
        return bookCountByAuthorHashMap.getOrDefault(author.toLowerCase().trim(), 0);
    }
    
    @Override
    public Map<String, Integer> getBooksAndPricesByAuthor(String author) {
        if (author == null || author.trim().isEmpty()) {
            return Map.of();
        }
        
        List<Book> authorBooks = booksByAuthorHashMap.getOrDefault(author.toLowerCase().trim(), List.of());
        
        return authorBooks.stream()
                .collect(Collectors.toMap(
                    Book::title,
                    Book::price,
                    Integer::max,
                    LinkedHashMap::new
                ));
    }
    
    @Override
    public List<Book> getUniqueBooksByAuthor(String author) {
        if (author == null || author.trim().isEmpty()) {
            return List.of();
        }
        
        List<Book> authorBooks = booksByAuthorHashMap.getOrDefault(author.toLowerCase().trim(), List.of());
        
        return authorBooks.stream()
                .collect(Collectors.toMap(
                    book -> book.title().toLowerCase().trim(),
                    book -> book,
                    (existing, replacement) -> existing.year() > replacement.year() ? existing : replacement,
                    LinkedHashMap::new
                ))
                .values()
                .stream()
                .toList();
    }
    
    @Override
    public List<Book> getAllUniqueBooks() {
        return allBooksInDataset.stream()
                .collect(Collectors.toMap(
                    book -> book.title().toLowerCase().trim() + "|" + book.author().toLowerCase().trim(),
                    book -> book,
                    (existing, replacement) -> existing.year() > replacement.year() ? existing : replacement,
                    LinkedHashMap::new
                ))
                .values()
                .stream()
                .toList();
    }
    
    public Map<String, Object> getPerformanceOptimizationStats() {
        List<Book> uniqueBooks = getAllUniqueBooks();
        Map<String, List<Book>> duplicates = getDuplicateBooksAcrossYears();
        
        return Map.of(
            "totalBooks", allBooksInDataset.size(),
            "uniqueBooks", uniqueBooks.size(),
            "duplicateEntries", allBooksInDataset.size() - uniqueBooks.size(),
            "uniqueAuthors", uniqueAuthorsList.size(),
            "uniqueRatings", booksByRatingHashMap.size(),
            "duplicateTitles", duplicates.size(),
            "averageBooksPerAuthor", allBooksInDataset.size() / (double) uniqueAuthorsList.size(),
            "deduplicationRatio", String.format("%.1f%%", (allBooksInDataset.size() - uniqueBooks.size()) * 100.0 / allBooksInDataset.size()),
            "optimizationEnabled", true
        );
    }
    
    public Map<String, List<Book>> getDuplicateBooksAcrossYears() {
        return allBooksInDataset.stream()
                .collect(Collectors.groupingBy(
                    book -> book.title().toLowerCase().trim() + "|" + book.author().toLowerCase().trim()
                ))
                .entrySet()
                .stream()
                .filter(entry -> entry.getValue().size() > 1)
                .collect(Collectors.toMap(
                    Map.Entry::getKey,
                    Map.Entry::getValue,
                    (existing, replacement) -> existing,
                    LinkedHashMap::new
                ));
    }
}