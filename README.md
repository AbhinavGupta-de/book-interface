# Amazon Bestsellers Book Database

A Java application for analyzing Amazon's Top 50 bestselling books dataset from 2009 to 2019. This project demonstrates object-oriented programming principles, data processing, and repository pattern implementation.

## Dataset Overview

This project uses a Kaggle dataset containing information about 550 books from Amazon's bestseller lists. The dataset includes the following attributes:

| Column | Description | Range/Type |
|--------|-------------|------------|
| Name | Book title | String |
| Author | Author name | String |
| User Rating | Average Amazon rating | 3.3 - 4.9 |
| Reviews | Number of user reviews | 37 - 87,800 |
| Price | Book price in USD | $0 - $105 |
| Year | Bestseller list year | 2009 - 2019 |
| Genre | Book category | Fiction / Non Fiction |

### Sample Data
```
10-Day Green Smoothie Cleanse,JJ Smith,4.7,17350,8,2016,Non Fiction
1984 (Signet Classics),George Orwell,4.7,21424,6,2017,Fiction
12 Rules for Life: An Antidote to Chaos,Jordan B. Peterson,4.7,18979,15,2018,Non Fiction
```

## Project Structure

```
domain-model/
├── src/main/java/com/abhinavgpt/
│   ├── App.java                    # Simple CLI entry point
│   ├── Driver.java                 # Main demonstration program
│   ├── books/
│   │   ├── Book.java              # Book data model (record)
│   │   ├── BookRepository.java    # Repository interface
│   │   ├── BookRepositoryImpl.java # Repository implementation
│   │   └── BookService.java       # Business logic layer
│   └── utils/
│       ├── DataReader.java        # Data reader interface
│       └── DatasetReader.java     # CSV reader implementation
├── data.csv                       # Dataset file
└── pom.xml                       # Maven configuration
```

## Core Components

### Book Class
A Java record that represents a book with validation:
- Immutable data structure with automatic getters
- Input validation for all fields
- Clean, modern Java approach

### Repository Pattern
- `BookRepository`: Interface defining data access methods
- `BookRepositoryImpl`: Optimized implementation with caching and performance features
- Separation of data access from business logic

### Service Layer
- `BookService`: Business logic and data transformation
- High-level operations for common queries
- Performance optimization features

### Data Reading
- `DataReader`: Interface for different data sources
- `DatasetReader`: CSV file reader with robust parsing
- Extensible design for future data sources

## Features & Functionality

### Core Requirements
1. **Total books by author** - Count all books for a specific author
2. **All authors listing** - Get complete list of authors in dataset
3. **Books by author** - Retrieve all book titles for an author
4. **Books by rating** - Filter books by user rating
5. **Book prices by author** - Get book names and prices for an author

### Advanced Features
- **Duplicate detection** - Identify books appearing across multiple years
- **Performance optimization** - Caching and precomputed data structures
- **Author statistics** - Most prolific authors with book counts
- **Rating classification** - Books above/below rating thresholds
- **Multi-year bestsellers** - Books that appeared in multiple years

## Usage

### Running the Application

#### Simple CLI Version
```bash
# Compile and run
mvn compile exec:java -Dexec.mainClass="com.abhinavgpt.App" -Dexec.args="data.csv"
```

#### Full Demonstration
```bash
# Run the comprehensive demo
mvn compile exec:java -Dexec.mainClass="com.abhinavgpt.Driver"
```

### Building JAR
```bash
mvn clean package
java -jar target/domain-model-1.0-SNAPSHOT.jar data.csv
```

## Example Operations

### Query Books by Author
```java
BookService service = new BookService(repository);
List<String> stephenKingBooks = service.getBookTitlesByAuthor("Stephen King");
```

### Get Author Statistics
```java
Map<String, Integer> prolificAuthors = service.getMostProlificAuthorsWithBookCount(10);
```

### Filter by Rating
```java
List<Book> highRatedBooks = service.classifyBooksByUserRating(4.5);
```

### Price Analysis
```java
Map<String, Integer> bookPrices = service.getBookPricesByAuthor("George R. R. Martin");
```

## Performance Features

The implementation includes several optimization techniques:
- **Precomputed HashMaps** for O(1) lookups by author and rating
- **Caching** of frequently accessed data
- **Duplicate elimination** using Sets and custom logic
- **Memory-efficient** data structures

## Technical Highlights

- **Modern Java**: Uses records, interfaces, and clean architecture
- **Robust CSV Parsing**: Handles quoted fields, commas in titles, malformed data
- **Input Validation**: Comprehensive validation in Book constructor
- **Error Handling**: Graceful handling of file I/O and parsing errors
- **Extensible Design**: Interface-based architecture for easy extension

## Requirements

- Java 21 or higher (for record support)
- Maven 3.6+
- CSV dataset file (`data.csv`)

## Sample Output

```
=== Amazon Bestsellers Book Database ===
Loading dataset from data.csv...

Successfully loaded 550 books from the dataset.

📚 All Authors in Dataset (Total: 248 unique authors)
1. Abby Jimenez
2. Adam Grant
3. Amor Towles
...

🔍 Books by Stephen King:
- 11/22/63: A Novel
- The Institute: A Novel

📊 Most Prolific Authors:
Jeff Kinney: 12 books
Rick Riordan: 10 books
Suzanne Collins: 8 books
```

This project demonstrates practical application of object-oriented design principles, data processing techniques, and performance optimization in Java.