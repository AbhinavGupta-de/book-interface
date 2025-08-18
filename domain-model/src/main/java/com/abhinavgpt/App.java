package com.abhinavgpt;

import com.abhinavgpt.books.Book;
import com.abhinavgpt.utils.DatasetReader;


import java.util.List;

public class App
{
    public static void main( String[] args )
    {
        if (args.length == 0) {
            System.out.println("Usage: java -jar domain-model.jar <path-to-books.csv>");
            return;
        }
        String csvPath = args[0];
        DatasetReader reader = new DatasetReader();
        List<Book> books = reader.readBooksFromSource(csvPath);
        System.out.println("Loaded books: " + books.size());
        for (int i = 0; i < Math.min(3, books.size()); i++) {
            Book b = books.get(i);
            System.out.println((i+1) + ". " + b.title() + " by " + b.author());
        }
    }
}
