package com.abhinavgpt.utils;

import com.abhinavgpt.books.Book;
import java.util.List;

public interface DataReader {
    
    List<Book> readBooksFromSource(String source);
    boolean isValidDataSource(String source);
    
    default String getReaderTypeName() {
        return this.getClass().getSimpleName();
    }
}
