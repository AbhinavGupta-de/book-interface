package com.abhinavgpt.utils;

import com.abhinavgpt.books.Book;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.IntStream;

public final class DatasetReader implements DataReader {
    
    private static final Map<String, Integer> COLUMN_MAPPING = Map.ofEntries(
        Map.entry("name", 0), Map.entry("title", 0), Map.entry("book name", 0), Map.entry("book title", 0),
        Map.entry("author", 1), Map.entry("author name", 1), Map.entry("writer", 1),
        Map.entry("user rating", 2), Map.entry("rating", 2), Map.entry("user_rating", 2), Map.entry("userrating", 2),
        Map.entry("reviews", 3), Map.entry("review count", 3), Map.entry("number of reviews", 3), Map.entry("review_count", 3),
        Map.entry("price", 4), Map.entry("cost", 4), Map.entry("book price", 4),
        Map.entry("year", 5), Map.entry("publication year", 5), Map.entry("pub year", 5), Map.entry("published year", 5),
        Map.entry("genre", 6), Map.entry("category", 6), Map.entry("type", 6), Map.entry("book genre", 6)
    );
    
    @Override
    public List<Book> readBooksFromSource(String source) {
        if (!isValidDataSource(source)) return List.of();
        
        try {
            List<String> lines = Files.readAllLines(Path.of(source));
            if (lines.isEmpty()) return List.of();
            
            int[] columnIndices = mapHeaderColumns(lines.get(0));
            if (columnIndices == null) return List.of();
            
            return lines.stream()
                    .skip(1)
                    .map(line -> parseBookFromLine(line, columnIndices))
                    .filter(book -> book != null)
                    .toList();
                    
        } catch (Exception e) {
            System.err.println("Error reading file: " + source + " - " + e.getMessage());
            return List.of();
        }
    }
    
    @Override
    public boolean isValidDataSource(String source) {
        try {
            Path path = Path.of(source);
            return Files.exists(path) && Files.isReadable(path) && source.toLowerCase().endsWith(".csv");
        } catch (Exception e) {
            return false;
        }
    }
    
    private int[] mapHeaderColumns(String headerLine) {
        String[] headers = parseCsvLine(headerLine);
        int[] indices = new int[7];
        Arrays.fill(indices, -1);
        
        IntStream.range(0, headers.length)
                .forEach(i -> {
                    String cleanHeader = cleanString(headers[i]).toLowerCase();
                    Integer columnIndex = COLUMN_MAPPING.get(cleanHeader);
                    if (columnIndex != null) indices[columnIndex] = i;
                });
        
        boolean allFound = Arrays.stream(indices).allMatch(i -> i != -1);
        if (!allFound) {
            System.err.println("Missing columns. Found headers: " + Arrays.toString(headers));
            System.err.println("Mapped indices: " + Arrays.toString(indices));
        }
        
        return allFound ? indices : null;
    }
    
    private Book parseBookFromLine(String line, int[] columnIndices) {
        try {
            String[] fields = parseCsvLine(line);
            if (fields.length <= Arrays.stream(columnIndices).max().orElse(0)) return null;
            
            return new Book(
                cleanString(fields[columnIndices[0]]),
                cleanString(fields[columnIndices[1]]),
                parseDouble(fields[columnIndices[2]]),
                parseInt(fields[columnIndices[3]]),
                parseInt(fields[columnIndices[4]]),
                parseInt(fields[columnIndices[5]]),
                cleanString(fields[columnIndices[6]])
            );
        } catch (Exception e) {
            return null;
        }
    }
    
    private String[] parseCsvLine(String line) {
        return line.split(",(?=(?:[^\"]*\"[^\"]*\")*[^\"]*$)", -1);
    }
    
    private String cleanString(String value) {
        return value == null ? "" : value.trim().replaceAll("^\"|\"$", "");
    }
    
    private int parseInt(String value) {
        try {
            return Integer.parseInt(cleanString(value).replaceAll("[^0-9-]", ""));
        } catch (Exception e) {
            return 0;
        }
    }
    
    private double parseDouble(String value) {
        try {
            return Double.parseDouble(cleanString(value).replaceAll("[^0-9.-]", ""));
        } catch (Exception e) {
            return 0.0;
        }
    }
}