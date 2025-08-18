package com.abhinavgpt;

import com.abhinavgpt.books.Book;
import com.abhinavgpt.books.BookRepository;
import com.abhinavgpt.books.BookRepositoryImpl;
import com.abhinavgpt.books.BookService;
import com.abhinavgpt.utils.DatasetReader;

import java.util.List;
import java.util.Map;

public class Driver {
    
    public static void main(String[] args) {
        System.out.println("=== Amazon Bestsellers Book Database ===");
        System.out.println("Loading dataset from data.csv...\n");
        
        try {
            DatasetReader csvDatasetReader = new DatasetReader();
            List<Book> allBooksFromDataset = csvDatasetReader.readBooksFromSource("data.csv");
            
            if (allBooksFromDataset.isEmpty()) {
                System.err.println("No books were loaded from the dataset. Please check the data.csv file.");
                return;
            }
            
            System.out.println("Successfully loaded " + allBooksFromDataset.size() + " books from the dataset.\n");
            
            BookRepository optimizedBookRepository = new BookRepositoryImpl(allBooksFromDataset);
            BookService bookService = new BookService(optimizedBookRepository);
            
            demonstrateAllBookDatabaseFeatures(bookService);
            
        } catch (Exception e) {
            System.err.println("An error occurred while running the application: " + e.getMessage());
            e.printStackTrace();
        }
    }
    
    private static void demonstrateAllBookDatabaseFeatures(BookService service) {
        System.out.println("=== FEATURE DEMONSTRATIONS ===\n");
        
        demonstrateTotalBookCountByAuthor(service);
        demonstrateAllAuthorsListing(service);
        demonstrateBookTitlesByAuthorWithDeduplication(service);
        demonstrateBookClassificationByRating(service);
        demonstrateBookPricesByAuthor(service);
        demonstratePerformanceOptimizationFeatures(service);
        
        System.out.println("=== END OF DEMONSTRATIONS ===");
    }  
  
    private static void demonstrateTotalBookCountByAuthor(BookService service) {
        System.out.println("1. TOTAL BOOKS BY AUTHOR");
        System.out.println("------------------------");
        
        String[] testAuthors = {"Jeff Kinney", "Suzanne Collins", "Rick Riordan", "NonExistentAuthor"};
        
        for (String author : testAuthors) {
            int totalBooks = service.getTotalBookCountByAuthor(author);
            System.out.printf("Author: %-20s | Total Books: %d%n", author, totalBooks);
        }
        System.out.println();
    }
    
    private static void demonstrateAllAuthorsListing(BookService service) {
        System.out.println("2. ALL AUTHORS IN DATASET");
        System.out.println("-------------------------");
        
        List<String> allAuthors = service.getAllAuthorsInDataset();
        System.out.println("Total unique authors: " + allAuthors.size());
        System.out.println("Authors list:");
        
        for (int i = 0; i < allAuthors.size(); i++) {
            System.out.printf("%3d. %s%n", (i + 1), allAuthors.get(i));
        }
        System.out.println();
    }
    
    private static void demonstrateBookTitlesByAuthorWithDeduplication(BookService service) {
        System.out.println("3. BOOK NAMES BY AUTHOR (DEDUPLICATION COMPARISON)");
        System.out.println("--------------------------------------------------");
        
        String[] testAuthors = {"Jeff Kinney", "Suzanne Collins", "Rick Riordan"};
        
        for (String author : testAuthors) {
            List<Book> allBooksIncludingDuplicates = service.getAllBooksIncludingDuplicatesByAuthor(author);
            List<String> uniqueBookTitles = service.getBookTitlesByAuthor(author);
            
            System.out.println("Books by " + author + ":");
            System.out.printf("  Total entries (with duplicates): %d books%n", allBooksIncludingDuplicates.size());
            System.out.printf("  Unique titles (deduplicated): %d books%n", uniqueBookTitles.size());
            
            if (uniqueBookTitles.isEmpty()) {
                System.out.println("  No books found for this author.");
            } else {
                System.out.println("  Deduplicated book titles:");
                for (int i = 0; i < uniqueBookTitles.size(); i++) {
                    System.out.printf("    %d. %s%n", (i + 1), uniqueBookTitles.get(i));
                }
                
                if (allBooksIncludingDuplicates.size() > uniqueBookTitles.size()) {
                    System.out.printf("  Note: %d duplicate entries removed%n", 
                        allBooksIncludingDuplicates.size() - uniqueBookTitles.size());
                }
            }
            System.out.println();
        }
    }
    
    private static void demonstrateBookClassificationByRating(BookService service) {
        System.out.println("4. CLASSIFY BOOKS BY USER RATING (DEDUPLICATED)");
        System.out.println("-----------------------------------------------");
        
        double[] testRatings = {4.9, 4.8, 4.7, 3.3};
        
        for (double rating : testRatings) {
            List<Book> deduplicatedBooksWithRating = service.classifyBooksByUserRating(rating);
            List<Book> allBooksWithRating = service.getAllBooksIncludingDuplicatesByRating(rating);
            
            System.out.printf("Books with rating %.1f:%n", rating);
            System.out.printf("  Total entries: %d | Unique books: %d%n", 
                allBooksWithRating.size(), deduplicatedBooksWithRating.size());
            
            if (deduplicatedBooksWithRating.isEmpty()) {
                System.out.println("  No books found with this rating.");
            } else {
                int maxToShow = Math.min(5, deduplicatedBooksWithRating.size());
                System.out.println("  Unique books:");
                for (int i = 0; i < maxToShow; i++) {
                    Book book = deduplicatedBooksWithRating.get(i);
                    System.out.printf("    %d. %s by %s (Rating: %.1f, Year: %d)%n", 
                        (i + 1), book.title(), book.author(), book.userRating(), book.year());
                }
                if (deduplicatedBooksWithRating.size() > maxToShow) {
                    System.out.printf("    ... and %d more unique books%n", deduplicatedBooksWithRating.size() - maxToShow);
                }
            }
            System.out.println();
        }
    }
    
    private static void demonstrateBookPricesByAuthor(BookService service) {
        System.out.println("5. BOOK PRICES BY AUTHOR");
        System.out.println("------------------------");
        
        String[] testAuthors = {"Jeff Kinney", "Suzanne Collins", "Rick Riordan"};
        
        for (String author : testAuthors) {
            Map<String, Integer> booksAndPrices = service.getBookPricesByAuthor(author);
            System.out.println("Books and prices by " + author + " (" + booksAndPrices.size() + " books):");
            
            if (booksAndPrices.isEmpty()) {
                System.out.println("  No books found for this author.");
            } else {
                booksAndPrices.entrySet().stream()
                    .sorted(Map.Entry.<String, Integer>comparingByValue().reversed())
                    .forEach(entry -> 
                        System.out.printf("  %-50s | $%d%n", 
                            truncateStringToLength(entry.getKey(), 50), entry.getValue())
                    );
            }
            System.out.println();
        }
    }
    
    private static void demonstratePerformanceOptimizationFeatures(BookService service) {
        System.out.println("6. OPTIMIZATION FEATURES");
        System.out.println("------------------------");
        
        Map<String, Object> stats = service.getPerformanceOptimizationStats();
        System.out.println("Performance Statistics:");
        stats.forEach((key, value) -> 
            System.out.printf("  %s: %s%n", formatStatKeyForDisplay(key), value));
        System.out.println();
        
        Map<String, List<Book>> duplicates = service.getMultiYearBestsellerBooks();
        System.out.println("Multi-Year Bestsellers (appeared in multiple years):");
        System.out.println("Total multi-year bestsellers: " + duplicates.size());
        
        duplicates.entrySet().stream()
                .limit(5)
                .forEach(entry -> {
                    List<Book> books = entry.getValue();
                    Book firstBook = books.get(0);
                    System.out.printf("  \"%s\" by %s - appeared in %d years: %s%n",
                        truncateStringToLength(firstBook.title(), 40),
                        firstBook.author(),
                        books.size(),
                        books.stream()
                            .map(Book::year)
                            .sorted()
                            .map(String::valueOf)
                            .collect(java.util.stream.Collectors.joining(", "))
                    );
                });
        
        if (duplicates.size() > 5) {
            System.out.printf("  ... and %d more multi-year bestsellers%n", duplicates.size() - 5);
        }
        System.out.println();
        
        Map<String, Integer> prolificAuthors = service.getMostProlificAuthorsWithBookCount(10);
        System.out.println("Most Prolific Authors (top 10):");
        int rank = 1;
        for (Map.Entry<String, Integer> entry : prolificAuthors.entrySet()) {
            System.out.printf("  %2d. %-25s | %d books%n", 
                rank++, entry.getKey(), entry.getValue());
        }
        System.out.println();
    }
    
    private static String formatStatKeyForDisplay(String key) {
        return switch (key) {
            case "totalBooks" -> "Total Books";
            case "uniqueBooks" -> "Unique Books";
            case "duplicateEntries" -> "Duplicate Entries";
            case "uniqueAuthors" -> "Unique Authors";
            case "uniqueRatings" -> "Unique Ratings";
            case "duplicateTitles" -> "Duplicate Titles";
            case "averageBooksPerAuthor" -> "Avg Books/Author";
            case "deduplicationRatio" -> "Duplication Rate";
            case "optimizationEnabled" -> "Optimization Enabled";
            default -> key;
        };
    }
    
    private static String truncateStringToLength(String text, int maxLength) {
        if (text.length() <= maxLength) {
            return text;
        }
        return text.substring(0, maxLength - 3) + "...";
    }
}