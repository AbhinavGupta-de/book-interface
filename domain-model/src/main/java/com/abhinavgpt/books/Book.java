package com.abhinavgpt.books;

public record Book(String title, String author, double userRating, int reviewCount, int price, int year, String genre)
{

	public Book
	{
		validateAllInputParameters(title, author, userRating, reviewCount, price, year, genre);
	}

	private static void validateAllInputParameters(String title, String author, double userRating, int reviewCount, int price,
									   int year, String genre)
	{
		if (title == null || title.trim().isEmpty())
		{
			throw new IllegalArgumentException("Title cannot be null or blank");
		}
		if (author == null || author.trim().isEmpty())
		{
			throw new IllegalArgumentException("Author cannot be null or blank");
		}
		if (userRating < 0 || userRating > 5)
		{
			throw new IllegalArgumentException("User rating must be between 0 and 5");
		}
		if (reviewCount < 0)
		{
			throw new IllegalArgumentException("Review count cannot be negative");
		}
		if (price < 0)
		{
			throw new IllegalArgumentException("Price cannot be negative");
		}
		if (year < 1900 || year > 2030)
		{
			throw new IllegalArgumentException("Year must be between 1900 and 2030");
		}
		if (genre == null || genre.trim().isEmpty())
		{
			throw new IllegalArgumentException("Genre cannot be null or blank");
		}
	}

	public String getTitle() { return title; }
	public String getAuthor() { return author; }
	public double getUserRating() { return userRating; }
	public int getReviews() { return reviewCount; }
	public int getPrice() { return price; }
	public int getYear() { return year; }
	public String getGenre() { return genre; }

	public void printFormattedBookDetails()
	{
		System.out.printf("""
				Title: %s
				Author: %s
				User Rating: %.1f
				Reviews: %,d
				Price: $%d
				Year: %d
				Genre: %s
				------------------------
				""", title, author, userRating, reviewCount, price, year, genre);
	}
}
