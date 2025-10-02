package be.thomasmore.bookserver.controllers;

import be.thomasmore.bookserver.model.dto.AuthorDTO;
import be.thomasmore.bookserver.model.dto.BookDTO;
import be.thomasmore.bookserver.model.dto.BookDetailedDTO;
import be.thomasmore.bookserver.services.BookService;
import io.swagger.v3.oas.annotations.Operation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import jakarta.validation.Valid;
import java.util.List;

/**
 * Controller for handling book-related HTTP requests.
 * Provides REST endpoints for CRUD operations on books.
 * All endpoints are prefixed with "/api/books".
 */
@RestController
@RequestMapping("/api/books")
@Slf4j
public class BookController {
    @Autowired
    private BookService bookService;

    /**
     * Retrieves a list of books, optionally filtered by title keyword.
     *
     * @param titleKeyWord Optional parameter to filter books by title (case-insensitive)
     * @return List of BookDTO objects containing basic book information
     */
    @Operation(summary = "list of books in the database.",
            description = "If Request Parameter <b>titleKeyWord</b> is given: " +
                    "only books where the title contains this titleKeyWord (ignore-case). </br>" +
                    "Otherwise all books are returned. </br>" +
                    "</br>" +
                    "The authors Collection contains only id and name. </br>" +
                    "Use GET api/authors/{id}/authors  to fetch more info about the authors. ")
    @GetMapping("")
    public Iterable<BookDTO> findAll(@RequestParam(required = false) String titleKeyWord) {
        log.info("##### findAll books - titleKeyWord=" + titleKeyWord);
        return bookService.findAll(titleKeyWord);
    }

    /**
     * Retrieves detailed information about a specific book by its ID.
     *
     * @param id The ID of the book to retrieve
     * @return BookDetailedDTO containing detailed information about the book
     */
    @Operation(summary = "get 1 book from the database.",
            description = "Book with id is fetched from database - returns detailed info. " +
                    "</br>" +
                    "The authors Collection contains only id and name. </br>" +
                    "Use GET api/authors/{id} to fetch more info about the authors. ")
    @GetMapping("{id}")
    public BookDetailedDTO findOne(@PathVariable int id) {
        log.info(String.format("##### findOne book %d", id));
        return bookService.findOne(id);
    }

    /**
     * Creates a new book in the database.
     * Note: This endpoint does not handle author relationships. Use PUT /api/books/{id}/authors to update authors.
     *
     * @param bookDto The book data to create
     * @return The created book's detailed information
     */
    @Operation(summary = "create a new book in the database.",
            description = "The authors are <b>not</b> updated in the new book.</br>" +
                    "Use PUT api/books/{id}/authors to update those. </br>" +
                    "</br>" +
                    "Returns new book (containing id from database). ")
    @PostMapping("")
    public BookDetailedDTO create(@Valid @RequestBody BookDetailedDTO bookDto) {
        log.info("##### create book");
        return bookService.create(bookDto);
    }

    /**
     * Updates an existing book's information.
     *
     * @param id The ID of the book to update
     * @param bookDto The updated book data
     * @return The updated book's detailed information
     */
    @Operation(summary = "edit existing book in the database.",
            description = "The authors are <b>not</b> updated in the new book.</br>" +
                    "Use PUT api/books/{id}/authors to update those. </br>" +
                    "</br>" +
                    "Returns updated book. ")
    @PutMapping("{id}")
    public BookDetailedDTO edit(@PathVariable int id, @RequestBody BookDetailedDTO bookDto) {
        log.info(String.format("##### edit book %d", id));
        return bookService.edit(id, bookDto);
    }

    /**
     * Deletes a book from the database.
     *
     * @param id The ID of the book to delete
     */
    @DeleteMapping("{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable int id) {
        log.info(String.format("##### delete book %d", id));
        bookService.delete(id);
    }

    /**
     * Retrieves the list of authors for a specific book.
     *
     * @param id The ID of the book
     * @return List of AuthorDTO objects containing author information
     */
    @Operation(summary = "find the authors for the given book. ",
            description = "Returns authors collection that contains only id and name. </br>" +
                    "Use GET api/authors/{id}/authors to fetch more info about the authors. ")
    @GetMapping("{id}/authors")
    public Iterable<AuthorDTO> authorsForBook(@PathVariable int id) {
        log.info(String.format("##### get authors for book with id %d", id));
        return bookService.authorsForBook(id);
    }

    /**
     * Updates the authors for a specific book.
     *
     * @param id The ID of the book to update
     * @param authorIds List of author IDs to associate with the book
     * @return The updated book's detailed information
     */
    @Operation(summary = "update the authors for the given book. ",
            description = "The authors Collection has to contain ids of existing authors. </br>" +
                    "Returns updated book containing id and name of the authors. ")
    @PutMapping("{id}/authors")
    public BookDetailedDTO editAuthorsForBook(@PathVariable int id, @RequestBody List<Integer> authorIds) {
        log.info(String.format("##### edit authors for book %d", id));
        return bookService.editAuthorsForBook(id, authorIds);
    }
}
