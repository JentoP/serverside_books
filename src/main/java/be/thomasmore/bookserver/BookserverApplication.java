package be.thomasmore.bookserver;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * Main entry point for the Book Server application.
 * This class is annotated with @SpringBootApplication which enables auto-configuration,
 * component scanning, and defines this as a configuration class.
 */
@SpringBootApplication
public class BookserverApplication {

    /**
     * The main method that starts the Spring Boot application.
     * @param args Command line arguments passed to the application.
     */
    public static void main(String[] args) {
        SpringApplication.run(BookserverApplication.class, args);
    }
}
